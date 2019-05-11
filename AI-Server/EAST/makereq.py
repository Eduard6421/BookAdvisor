import requests

print('fuckyeah')


#RecModelAPI = 'http://localhost:9001/v1/models/recsys:predict'

EASTModelAPI = "http://localhost:8769/api/recsys"


senddata =  { 'book_id_1' : 0,

        	}

req = requests.Request('POST',EASTModelAPI,json=senddata)
prepared = req.prepare()
sess = requests.Session()
resp = sess.send(prepared)

print(resp.text)	