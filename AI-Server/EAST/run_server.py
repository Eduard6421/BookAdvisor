#!/usr/bin/env python3

#Python
import os
import time
import datetime
import cv2
import numpy as np
import uuid
import json
import requests
import base64 
import functools
import logging
import collections
import argparse
import random


#Tensorflow
import pytesseract
import tensorflow as tf
import model
import lanms
from icdar import restore_rectangle
from eval import resize_image, sort_poly, detect

#Flask
from flask import jsonify
from PIL import Image
from flask import Flask, request, render_template

#MongoDB
from pymongo import MongoClient


logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)


#Change this to the EAST Model
checkpoint_path = '/data/ai_module/BookAdvisor/AI-Server/EAST/models/trainedmodel'

#Change APIs according to the machine
#VEZI CA AM SCHIMBAT PORT-UL

AOCRModelAPI = 'http://localhost:9001/v1/models/aocr:predict'
RecModelAPI = 'http://localhost:9005/v1/models/recsys:predict'
east_model = {}

def dict_compare(first,second):
    if(int(first['x0']) == int(second['xo'])):
        return int(first['y0']) < int(second['yo'])
    return int(first['x0']) == int(second['yo'])


@functools.lru_cache(maxsize=1)
def get_host_info():
    ret = {}
    with open('/proc/cpuinfo') as f:
        ret['cpuinfo'] = f.read()

    with open('/proc/meminfo') as f:
        ret['meminfo'] = f.read()

    with open('/proc/loadavg') as f:
        ret['loadavg'] = f.read()

    return ret

@functools.lru_cache(maxsize=100)
def get_predictor(checkpoint_path):
    logger.info('loading model')

    input_images = tf.placeholder(tf.float32, shape=[None, None, None, 3], name='input_images')
    global_step = tf.get_variable('global_step', [], initializer=tf.constant_initializer(0), trainable=False)

    f_score, f_geometry = model.model(input_images, is_training=False)

    variable_averages = tf.train.ExponentialMovingAverage(0.997, global_step)
    saver = tf.train.Saver(variable_averages.variables_to_restore())

    sess = tf.Session(config=tf.ConfigProto(allow_soft_placement=True))

    ckpt_state = tf.train.get_checkpoint_state(checkpoint_path)
    model_path = os.path.join(checkpoint_path, os.path.basename(ckpt_state.model_checkpoint_path))
    logger.info('Restore from {}'.format(model_path))
    saver.restore(sess, model_path)

    def predictor(img):
        start_time = time.time()
        rtparams = collections.OrderedDict()
        rtparams['start_time'] = datetime.datetime.now().isoformat()
        rtparams['image_size'] = '{}x{}'.format(img.shape[1], img.shape[0])
        timer = collections.OrderedDict([
            ('net', 0),
            ('restore', 0),
            ('nms', 0)
        ])

        im_resized, (ratio_h, ratio_w) = resize_image(img)
        rtparams['working_size'] = '{}x{}'.format(
            im_resized.shape[1], im_resized.shape[0])
        start = time.time()
        score, geometry = sess.run(
            [f_score, f_geometry],
            feed_dict={input_images: [im_resized[:,:,::-1]]})
        timer['net'] = time.time() - start

        boxes, timer = detect(score_map=score, geo_map=geometry, timer=timer)
        logger.info('net {:.0f}ms, restore {:.0f}ms, nms {:.0f}ms'.format(
            timer['net']*1000, timer['restore']*1000, timer['nms']*1000))

        if boxes is not None:
            scores = boxes[:,8].reshape(-1)
            boxes = boxes[:, :8].reshape((-1, 4, 2))
            boxes[:, :, 0] /= ratio_w
            boxes[:, :, 1] /= ratio_h

        duration = time.time() - start_time
        timer['overall'] = duration
        logger.info('[timing] {}'.format(duration))

        text_lines = []
        if boxes is not None:
            text_lines = []
            for box, score in zip(boxes, scores):
                box = sort_poly(box.astype(np.int32))
                if np.linalg.norm(box[0] - box[1]) < 5 or np.linalg.norm(box[3]-box[0]) < 5:
                    continue
                tl = collections.OrderedDict(zip(
                    ['x0', 'y0', 'x1', 'y1', 'x2', 'y2', 'x3', 'y3'],
                    map(float, box.flatten())))
                tl['score'] = float(score)
                text_lines.append(tl)
        ret = {
            'text_lines': text_lines,
            'rtparams': rtparams,
            'timing': timer,
        }
        ret.update(get_host_info())
        return ret


    return predictor


class Config:
    SAVE_DIR = 'static/results'


config = Config()


app = Flask(__name__)

@app.route('/api/east')
def index():
    return render_template('index.html')


def draw_illu(illu, rst):
    for t in rst['text_lines']:
        d = np.array([t['x0'], t['y0'], t['x1'], t['y1'], t['x2'],
                      t['y2'], t['x3'], t['y3']], dtype='int32')
        d = d.reshape(-1, 2)
        cv2.polylines(illu, [d], isClosed=True, color=(255, 255, 0))
    return illu


def OCR_Model(newimage,ocrImg):


    #Write request header and content
    headers = {'cache-control':'no-cache','content-type': 'application/json',}
    senddata =  { 'signature-name': 'serving_default',
            'inputs' : {
                'input' :
                {
                    'b64' : str(newimage,'utf-8')
                }
            } 
    }

    #Send request
    req = requests.Request('POST',AOCRModelAPI,headers=headers,json=senddata)
    prepared = req.prepare()
    sess = requests.Session()
    resp = sess.send(prepared)

    newobj = {
       'probability' : resp.json()['outputs']['probability'],
        'word' : resp.json()['outputs']['output'],
    }

    if resp.json()['outputs']['probability'] < 0.8 and pytesseract.image_to_string(ocrImg) != '':
        newobj['word'] = str(pytesseract.image_to_string(ocrImg))

    return resp.json()

def save_result(img, rst):
    session_id = str(uuid.uuid1())
    dirpath = os.path.join(config.SAVE_DIR, session_id)
    os.makedirs(dirpath)

    # save input image
    output_path = os.path.join(dirpath, 'input.png')
    cv2.imwrite(output_path, img)

    # save illustration
    output_path = os.path.join(dirpath, 'output.png')
    cv2.imwrite(output_path, draw_illu(img.copy(), rst))


    result = rst['text_lines']
    result = sorted(result,key= lambda k: (k['y0'],k['x0']) )

    cnt=0
    output_path = os.path.join(dirpath,'')

    ocr_result = {}

    for element in result:


        # Write textboxes in file
        output_path = os.path.join(dirpath,'rectangle-' + str(cnt) + '.png')


        #Crop the image for each textbox
        top_left_x = min([int(element['x0']),int(element['x1']),int(element['x2']),int(element['x3'])])
        top_left_y = min([int(element['y0']),int(element['y1']),int(element['y2']),int(element['y3'])])
        bot_right_x = max([int(element['x0']),int(element['x1']),int(element['x2']),int(element['x3'])])
        bot_right_y = max([int(element['y0']),int(element['y1']),int(element['y2']),int(element['y3'])])

        newimage = img[top_left_y:bot_right_y,top_left_x:bot_right_x]
        ocrImg = newimage


        #Calculate scaling factor for images larger than maxwidth/maxheight
        height, width = newimage.shape[:2]

        max_height = 80
        max_width = 130

        scaling_factor = 1

        if max_height < height or max_width < width:
            scaling_factor = max_height / float(height)
        if max_width/float(width) < scaling_factor:
            scaling_factor = max_width / float(width)

        newimage = cv2.resize(newimage, None, fx=scaling_factor, fy=scaling_factor, interpolation=cv2.INTER_AREA)

        cv2.imwrite(output_path,newimage)

        retval, buff = cv2.imencode('.jpg', newimage)
        newimage = base64.b64encode(buff)

        newobj = OCR_Model(newimage,ocrImg)

        ocr_result["textbox"+str(cnt)]  = newobj
        cnt +=1

    ocr_result["session_id"]=session_id
    ocr_result["num"] = cnt


    #Save Output Data
    output_path = os.path.join(dirpath, 'result.json')

    with open(output_path, 'w') as f:
        json.dump(ocr_result, f)

    return ocr_result


@app.route('/api/recsys', methods=['POST'])
def recomandation_request():


    newuser = request.json
    Client = MongoClient()

    db = Client["recsysdb"]
    collection = db["Users"]

    if not bool(newuser):
        for i in range(1,10):
            strusr = 'book_id_' + str(i)
            newuser[strusr] = 0

    #print(newuser)

    current_loss = 100000.0
    similar_user = {}

    #pune .find().limit(numar de useri)

    for user_preference in collection.find().limit(20000):
        similar = False
        loss = 0.0

        for book_id in newuser:
            if(book_id in user_preference):
                similar= True
                loss += (float(user_preference[book_id]) - float(newuser[book_id]))**2

        if(similar and current_loss > loss):
            current_loss = loss
            similar_user = user_preference['user_id']

    similar_user = int(similar_user)

    book_input = list(set(random.sample(range(1,10000),1000)))
    user_input = [similar_user]*len(book_input)

    headers = {'cache-control':'no-cache','content-type': 'application/json',}
    senddata =  { 'signature-name': 'serving_default',
                'inputs' : {
                    'Book-Input' : book_input,
                    'User-Input' : user_input,
                } 
            }

    req = requests.Request('POST',RecModelAPI,headers=headers,json=senddata)
    prepared = req.prepare()
    sess = requests.Session()
    resp = sess.send(prepared)

    outputs = resp.json()['outputs']
    outputs = [item for sublist in outputs for item in sublist]

    recommended_books = zip(book_input,outputs)
    recommended_books = sorted(recommended_books, key=lambda x: x[1] , reverse=True)

    #print(list(recommended_books))


    recommended_books = {
    'recommandations' : list(recommended_books)[:20],
    }

    return jsonify(recommended_books)


@app.route('/api/east', methods=['POST'])
def east():
    global predictor
    import io
    print('i m here u big fat nibba')

    bio = io.BytesIO()
    request.files['image'].save(bio)
    img = cv2.imdecode(np.frombuffer(bio.getvalue(), dtype='uint8'), 1)


    height, width = img.shape[:2]
    max_height = 500
    max_width = 850


    scaling_factor=1

    if max_height < height or max_width < width:
            scaling_factor = max_height / float(height)
    if max_width/float(width) < scaling_factor:
            scaling_factor = max_width / float(width)
    img = cv2.resize(img, None, fx=scaling_factor, fy=scaling_factor, interpolation=cv2.INTER_AREA)
    rst = east_model(img)

    response = save_result(img,rst)
    print(response)


    return jsonify(response)


def main():
    global checkpoint_path
    global east_model
    parser = argparse.ArgumentParser()
    parser.add_argument('--port', default=8769, type=int)
    parser.add_argument('--checkpoint_path', default=checkpoint_path)
    args = parser.parse_args()
    checkpoint_path = args.checkpoint_path

    if not os.path.exists(args.checkpoint_path):
        raise RuntimeError(
            'Checkpoint `{}` not found'.format(args.checkpoint_path))

    app.debug = False  # change this to True if you want to debug
    east_model = get_predictor(checkpoint_path)
    app.run('0.0.0.0', args.port)

if __name__ == '__main__':
    main()
    
