#This code convers SVT Dataset to EAST-architecture input type

import xml.etree.ElementTree as ET
import os

import time
import datetime
import cv2
import numpy as np
import uuid
import json

import functools
import logging
import collections


basePath = os.getcwd()
trainFilename = "train.xml"
testFilename  = "test.xml"

trainTree = ET.parse(basePath+"/"+trainFilename)
trainRoot = trainTree.getroot()

testTree = ET.parse(basePath+"/"+testFilename)
testRoot = testTree.getroot()

for image in trainRoot.iter('image'):
	with open(image[0].text[4:-4]+".txt",'w') as newfile:
		coord = image[4]
		for rectangle in list(coord):
			x = int(rectangle.get('x'))
			y = int(rectangle.get('y'))
			height = int(rectangle.get('height'))
			width = int(rectangle.get('width'))
			print(str(x) + ',' + str(y)+',' + str(x+width)+ ',' + str(y) + ',' + str(x+width) + ',' + str(y+height) + ',' + str(x) + ',' + str(y+height) + ',###' ,file=newfile)

for image in testRoot.iter('image'):
	with open(image[0].text[4:-4]+".txt",'w') as newfile:
		coord = image[4]
		for rectangle in list(coord):
			x = int(rectangle.get('x'))
			y = int(rectangle.get('y'))
			height = int(rectangle.get('height'))
			width = int(rectangle.get('width'))
			print(str(x) + ',' + str(y)+',' + str(x+width)+ ',' + str(y) + ',' + str(x+width) + ',' + str(y+height) + ',' + str(x) + ',' + str(y+height) + ',###' ,file=newfile)