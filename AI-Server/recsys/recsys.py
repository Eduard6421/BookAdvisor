import json
from pymongo import MongoClient
Client = MongoClient()

db = Client["recsysdb"]
collection = db["Users"]

newuser = { "book_id_650" : 2,
			"book_id_529" : 5,
 			"book_id_5425" : 5,
 			"book_id_4602" : 4,
 			"book_id_158" : 4,
 			"book_id_7416" : 3,
 			'book_id_258': 2.3,
 			}

current_loss = 100000.0
similar_user = {}

#pune .find().limit(numar de useri)

for user_preference in collection.find():
	similar = False
	loss = 0.0

	for book_id in newuser:
		if(book_id in user_preference):
			similar= True
			loss += (float(user_preference[book_id]) - float(newuser[book_id]))**2

	if(similar and current_loss > loss):
		current_loss = loss
		similar_user = user_preference['user_id']


print(similar_user)
Client.close()


from flask import Flask, request, jsonify
app = Flask(__name__)

@app.route('/api/add_message/<uuid>', methods=['GET', 'POST'])
def add_message(uuid):
    content = request.json
    print content['mytext']
    return jsonify({"uuid":uuid})

if __name__ == '__main__':
    app.run(host= '0.0.0.0',debug=True)