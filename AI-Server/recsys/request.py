import requests

print('fuckyeah')


RecModelAPI = 'http://localhost:9001/v1/models/recom:predict'

headers = {'cache-control':'no-cache','content-type': 'application/json',}
senddata =  { 'signature-name': 'serving_default',
                'inputs' : {
                    'Book-Input' : [1,2,3,4,5,6,7,8,9,10],
                    'User-Input' : [1,1,1,1,1,1,1,1,1,1],
            	} 
        	}

req = requests.Request('POST',RecModelAPI,headers=headers,json=senddata)
prepared = req.prepare()
sess = requests.Session()
resp = sess.send(prepared)

print(resp.text)