import requests
import base64 


sess = requests.Session()
url = 'http://localhost:9001/v1/models/aocr:predict'

image = open('ss.png', 'rb') 
image_read = image.read() 
image_64_encode = base64.encodestring(image_read)



senddata =  { 'signature-name': 'serving_default', 
			  'inputs'		  : {
			 			'input' : {
			 				'b64' : image_64_encode,
			 			},
			  },
			}

headers = {'cache-control':'no-cache','content-type': 'application/json'}

req = requests.Request('POST','http://localhost:9001/v1/models/aocr:predict',headers=headers,json=senddata)
prepared = req.prepare()

def pretty_print_POST(req):
    print('{}\n{}\n{}\n\n{}'.format(
        '-----------START-----------',
        req.method + ' ' + req.url,
        '\n'.join('{}: {}'.format(k, v) for k, v in req.headers.items()),
        req.body,
    ))


pretty_print_POST(prepared)
resp = sess.send(prepared)
print(resp.json())

