import pytest
import requests
from rest_api.models import *
from django.core import serializers
from rest_api.serializers import *
from django.db.models import Q

base = 'http://0.0.0.0:8000/'
url_login = base + 'login'
url_logout = base + 'logout'

### UNIT TESTS ###

def log_in():
	data = {'email':'test_user@yahoo.com', 'password':'test_user'}
	response = requests.post(url = url_login, data = data).json()
	token = response['token']
	return token

def log_out(token):
	headers = {'Authorization': 'Token ' + token}
	requests.post(url = url_logout, headers = headers)
def test_login_1():
	data = {}
	response = requests.post(url = url_login, data = data).json()
	assert response['msg'] == 'username field is required password field is required '

def test_login_2():
	data = {'email':'fakemail1@yahoo.com', 'password':'fake_password1'}
	response = requests.post(url = url_login, data = data).json()
	assert response['msg'] == 'Error login user'

def test_login_3():
	data = {'email':'fakemail@yahoo.com'}
	response = requests.post(url = url_login, data = data).json()
	assert response['msg'] == 'password field is required '

def test_login_4():
	data = {'password':'fake_password'}
	response = requests.post(url = url_login, data = data).json()
	assert response['msg'] == 'username field is required '

def test_login_5():
	data = {'email':'test_user@yahoo.com', 'password':'test_user'}
	response = requests.post(url = url_login, data = data).json()
	assert response['msg'] == 'User has been successfully authenticate'

def test_login_6():
	data = {'email':'test_user@yahoo.com', 'password':'test_user'}
	response = requests.post(url = url_login, data = data).json()
	assert response['token'] is not ''

def test_register_user_1():
	data = {}
	url = base + 'register'
	response = requests.post(url = url, data = data).json()
	assert response['msg'] == 'password field is required '

def test_register_user_2():
	data = {'email':'test_user@yahoo.com', 'password':'test_user'}
	url = base + 'register'
	response = requests.post(url = url, data = data).json()
	assert response['msg'] == 'This user already exist'

def test_logout_1():
	headers = {'Authorization':'test_token'}
	response = requests.post(url = url_logout, headers = headers).json()
	assert response['detail'] == 'Authentication credentials were not provided.'

def test_logout_2():
	data = {'email':'test_user@yahoo.com', 'password':'test_user'}
	response = requests.post(url = url_login, data = data).json()
	token = response['token']
	headers = {'Authorization': 'Token ' + token}
	response = requests.post(url = url_logout, headers = headers).json()
	assert response['msg'] == 'Logout successfully'

@pytest.mark.django_db
def test_get_books_1():
	token = log_in()
	url = base + 'get-books'
	title ='/The%20Hunger%20Games'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url + title, headers = headers).json()
	log_out(token)
	expected_book = Book.objects.all().filter(title='The Hunger Games')
	assert response[0]['title'] == expected_book[0].title

@pytest.mark.django_db
def test_get_books_2():
	token = log_in()
	url = base + 'get-books'
	title ='/Fake_Book'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url + title, headers = headers).json()
	log_out(token)
	assert response['has_error'] == 'true'

@pytest.mark.django_db
def test_get_followers_1():
	token = log_in()
	url = base + 'get-followers'
	email ='test_user@yahoo.com'
	params = {'email':email}
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url, params = params, headers = headers).json()
	log_out(token)
	profile	= Profile.objects.get(user=User.objects.get(email=email))
	expected_followers = FollowersSerializer(profile).data
	assert response == expected_followers['followers']

@pytest.mark.django_db
def test_get_followers_2():
	token = log_in()
	url = base + 'get-followers'
	email ='fake_mail@yahoo.com'
	params = {'email':email}
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url, params = params, headers = headers).json()
	log_out(token)
	assert response ==  []

@pytest.mark.django_db
def test_get_following_1():
	token = log_in()
	url = base + 'get-following'
	email ='test_user@yahoo.com'
	params = {'email':email}
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url, params = params, headers = headers)
	log_out(token)
	assert response.status_code == 500

@pytest.mark.django_db
def test_get_following_2():
	token = log_in()
	url = base + 'get-following'
	email ='fake_mail@yahoo.com'
	params = {'email':email}
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url, params = params, headers = headers)
	log_out(token)
	assert response.status_code == 500

def test_follow():
	token = log_in()
	url = base + 'follow'
	email ='fake_mail@yahoo.com'
	params = {'email_to_follow':email}
	headers = {'Authorization': 'Token ' + token}
	response = requests.post(url = url, data = params, headers = headers).json()
	log_out(token)
	assert response['has_error'] == 'true'

#@pytest.mark.skip(reason='Response time is too large')
@pytest.mark.django_db
def test_get_all_books_1():
	token = log_in()
	url = base + 'get-books'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url, headers = headers)
	log_out(token)
	assert response.status_code == 500


@pytest.mark.django_db
def test_get_all_books_2():
	token = log_in()
	url = base + 'get-books'
	headers = {}
	response = requests.get(url = url, headers = headers)
	log_out(token)
	assert response.status_code == 401


@pytest.mark.django_db
def test_get_reading_list_1():
	token = log_in()
	url = base + 'get-reading-list'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url, headers = headers).json()
	log_out(token)
	reading_lists = Profile.objects.get(user=User.objects.get(email='test_user@yahoo.com')).reading_lists
	expected_reading_lists = Reading_list_booksSerializer(reading_lists, many=True).data
	assert response['reading_lists_current_user'] == expected_reading_lists

@pytest.mark.django_db
def test_get_reading_list_2():
	token = log_in()
	url = base + 'get-reading-list'
	headers = {}
	response = requests.get(url = url, headers = headers)
	log_out(token)
	assert response.status_code == 401

@pytest.mark.django_db
def test_get_reading_list_3():
	token = log_in()
	url = base + 'get-reading-list'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url, headers = headers).json()
	log_out(token)
	reading_lists = Profile.objects.get(user=User.objects.get(email='test_user2@yahoo.com')).reading_lists
	expected_reading_lists = Reading_list_booksSerializer(reading_lists, many=True).data
	assert response['reading_lists_current_user'] != expected_reading_lists

@pytest.mark.django_db
def test_users():
	token = log_in()
	url = base + 'users'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url, headers = headers)
	log_out(token)
	assert response.status_code == 500

@pytest.mark.django_db
def test_get_categories():
	token = log_in()
	url = base + 'get-categories'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url, headers = headers).json()
	log_out(token)
	tags = Tag.objects.all()
	expected_tags = TagSerializer(tags, many=True).data
	assert response == expected_tags

@pytest.mark.django_db
def test_get_books_category_1():
	token = log_in()
	url = base + 'get-books-category'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url + '/Art', headers = headers).json()
	log_out(token)
	tag = Tag.objects.get(name='Biography')
	try:
		books = Book.objects.get(books_tags=tag)
		expected_books = BooksSerializer(books, many=True).data
	except Book.DoesNotExist:
		expected_books = []
	assert response == expected_books

@pytest.mark.django_db
def test_get_books_category_2():
	token = log_in()
	url = base + 'get-books-category'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url + '/Biography', headers = headers).json()
	log_out(token)
	tag = Tag.objects.get(name='Biography')
	try:
		books = Book.objects.get(books_tags=tag)
		expected_books = BooksSerializer(books, many=True).data
	except Book.DoesNotExist:
		expected_books = []
	assert response == expected_books

def test_get_all_conversations():
	token = log_in()
	url = base + 'get-conversations'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url, headers = headers).json()
	log_out(token)
	assert response['has_error'] == 'false'

def test_get_conversations():
	token = log_in()
	url = base + 'get-conversations'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url + '/test_user2@yahoo.com', headers = headers).json()
	log_out(token)
	assert response['has_error'] == 'false'

@pytest.mark.django_db
def test_get_reviews():
	token = log_in()
	url = base + 'get-reviews'
	headers = {'Authorization': 'Token ' + token}
	title = '/The%20Hunger%20Games'
	response = requests.get(url = url + title, headers = headers).json()
	log_out(token)
	book = Book.objects.filter(title='The Hunger Games')
	expected_reviews = BookSerializer(book).data
	if not expected_reviews:
		expected_reviews = []
	assert response == expected_reviews

def test_current_user():
	token = log_in()
	url = base + 'current-user'
	headers = {'Authorization': 'Token ' + token}
	email = 'test_user@yahoo.com'
	data = {'email':email}
	response = requests.get(url = url,data = data, headers = headers)
	log_out(token)
	assert response.status_code == 500


### INTEGRATION TESTS ###

def test_register_flow():
	data = {'email':'test_user@yahoo.com', 'password':'test_user'}
	url = base + 'register'
	response = requests.post(url = url, data = data).json()
	token = log_in()
	log_out(token)
	assert response['msg'] == 'This user already exist'

def test_follow_flow():
	token = log_in()
	url = base + 'follow'
	email ='test_user2@yahoo.com'
	params = {'email_to_follow':email}
	headers = {'Authorization': 'Token ' + token}
	response = requests.post(url = url, data = params, headers = headers).json()
	log_out(token)
	assert  response['has_error'] == 'false'

def test_message_flow():
	token = log_in()
	url = base + 'send-message/'
	email ='test_user3@yahoo.com'
	params = {'email_destinatar':email,'content':'message'}
	headers = {'Authorization': 'Token ' + token}
	response = requests.post(url = url, data = params, headers = headers)
	log_out(token)
	assert  response.status_code == 500

def test_update_user_flow():
	token = log_in()
	url = base + 'update-user/'
	email ='test_user@yahoo.com'
	data = {'first_name':'Ilie','last_name':'Oana'}
	params = {'email':'test_user@yahoo.com'}
	headers = {'Authorization': 'Token ' + token}
	response = requests.put(url = url, params = params, data = data, headers = headers)
	log_out(token)
	assert  response.status_code == 200

def test_add_reading_list_flow():
	token = log_in()
	url = base + 'add-reading-list/'
	email ='test_user@yahoo.com'
	data = {'title':'Amintiri din copilarie'}
	params = {'email':'test_user@yahoo.com'}
	headers = {'Authorization': 'Token ' + token}
	response = requests.post(url = url, params = params, data = data, headers = headers)
	log_out(token)
	assert  response.status_code == 405

def test_update_book_flow():
	token = log_in()
	url = base + 'update-book/'
	title ='Amintiri din copilarie2'
	email = 'test_user@yahoo.com'
	data = {'title':title,'description':'forta'}
	headers = {'Authorization': 'Token ' + token}
	response = requests.put(url = url + '10', data = data, headers = headers)
	log_out(token)
	assert  response.status_code == 200

def test_read_conversations_flow_1():
	token = log_in()
	url = base + 'get-conversations'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url + '/test_user2@yahoo.com', headers = headers).json()
	log_out(token)
	assert response['has_error'] == 'false'

def test_read_conversations_flow_2():
	token = log_in()
	url = base + 'get-conversations'
	headers = {'Authorization': 'Token ' + token}
	response = requests.get(url = url + '/abcd', headers = headers)
	log_out(token)
	assert response.status_code == 404

def test_add_review_flow_1():
	token = log_in()
	url = base + 'add-review/'
	data = {'title':'Amintiri din copilarie'}
	data = {'content':'good', 'score':'5'}
	headers = {'Authorization': 'Token ' + token}
	response = requests.post(url = url + '10', data = data, headers = headers)
	log_out(token)
	assert  response.status_code == 200

def test_add_review_flow_2():
	token = log_in()
	url = base + 'add-review/'
	data = {'title':'adasf'}
	data = {'content':'good', 'score':'5'}
	headers = {'Authorization': 'Token ' + token}
	response = requests.post(url = url + '983092', data = data, headers = headers).json()
	log_out(token)
	assert  response['has_error'] == 'true'

