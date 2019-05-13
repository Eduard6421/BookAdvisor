from pymongo import MongoClient
import numpy as numpy
import pandas as pd




Client = MongoClient()
db = Client["recsysdb"]
collection = db["Users"]


csv_file = pd.read_csv('/home/eduard/Private/AI/recsys/ratingsnewsorted.csv')

#print('sorting...')
#csv_file = csv_file.sort_values(by=["user_id"])

#print('exporting...')
#csv_file.to_csv('/home/eduard/Private/AI/recsys/ratingsnewsorted.csv',index=False)


user_ids = csv_file['user_id'].unique()


books_number = csv_file['book_id'].nunique()


print("Inserting " + str(len(user_ids)) + " users")

for x in user_ids:

	if(x%1000 == 0):
		print("Users inserted " + str(x))

	df = csv_file.loc[csv_file['user_id']==x]

	user_pref = {'user_id' : int(x),}

	for index,row in df.iterrows():
		book_name = "book_id_" + str(row['book_id'])
		rating = int(row['rating'])

		user_pref[book_name] = rating

	collection.insert(user_pref)