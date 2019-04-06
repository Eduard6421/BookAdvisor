from django.shortcuts import render
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
from rest_framework.response import Response
from rest_framework.decorators import api_view
from django.http import JsonResponse

from django.shortcuts import render, render_to_response
from django.template import RequestContext
from django.contrib.auth import logout, authenticate, login, logout
from django.http import HttpResponseRedirect, HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.core.files.storage import default_storage
import os
from django.apps import apps
from django.db.models import Q

from django.contrib.auth import authenticate
from django.views.decorators.csrf import csrf_exempt
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import AllowAny
from rest_framework.status import (
	HTTP_400_BAD_REQUEST,
	HTTP_404_NOT_FOUND,
	HTTP_200_OK
)
from rest_framework.response import Response

from django.core import serializers
from django.forms.models import model_to_dict


import uuid
import json

from .models import *
from .serializers import *

# from .tests import *

# Create your views here.

'''

METHOD: POST
URL: login/
PARAMS: email, password
HEADERS:
RETURN: token


METHOD: POST
URL: logout/
PARAMS:
HEADERS: token
RETURN:


METHOD: POST
URL: register/
PARAMS: email, password
HEADERS:
RETURN: token 
'''


@csrf_exempt
@api_view(['POST'])
@permission_classes((AllowAny,))
def login_user(request):
	msg = ''
	if request.user.is_authenticated:
		msg = 'Session already authenticated'
		return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)
	else:  # Not authenticated user
		email = request.POST.get('email', '')
		username = email
		password = request.POST.get('password', None)

		ok_param = True

		# check parameters if exists
		if username is None:
			msg += 'username field is required '
			ok_param = False
		if password is None:
			msg += 'password field is required '
			ok_param = False

		if not ok_param:
			return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)

		user = authenticate(username=username, password=password)
		if user is not None:  # Authenticate user
			login(request, user)
			if user.is_active:  # User active
				token, created = Token.objects.get_or_create(user=user)
				if token is None:  # Check if tocken already exist and return for login
					token = created
				msg = 'User has been successfully authenticate'
				return Response({'has_error': 'false', 'token': str(token), 'msg': msg, }, status=HTTP_200_OK)
			else:  # Inactiv user
				msg = 'User is set to inactiv'
				return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)
		else:  # Not authenticated after created
			msg = 'Error login user'
			return Response({'has_error': 'false', 'msg': msg, }, status=401)


@csrf_exempt
@api_view(['POST'])
@permission_classes((AllowAny,))
def register_user(request):
	msg = ''
	if request.user.is_authenticated:
		msg = 'Session already authenticated'
		return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)
	else:  # Not authenticated user
		email = request.POST.get('email', '')
		username = email
		password = request.POST.get('password', None)
		first_name = request.POST.get('first_name', '')
		last_name = request.POST.get('last_name', '')

		ok_param = True

		# check parameters if exists
		if username is None:
			msg += 'username field is required '
			ok_param = False
		if password is None:
			msg += 'password field is required '
			ok_param = False

		if not ok_param:
			return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)

		# check if already exist an user with this username
		user_already_exist = User.objects.filter(username=username)
		if user_already_exist.first() is not None:
			# username already exist ... 
			msg = 'This user already exist'
			return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)
		else:
			user = User(username=username, first_name=first_name, last_name=last_name, email=email)
			user.set_password(password)
			user.save()
			profile_user = Profile(user=user)

			reading_list = Reading_list_books()
			reading_list.title = 'Already Read'
			wish_to_read_list = Reading_list_books()
			wish_to_read_list.title = 'Wish to read'
			already_read_list = Reading_list_books()
			already_read_list.title = 'Reading Now'

			reading_list.save()
			wish_to_read_list.save()
			already_read_list.save()

			profile_user.save()

			profile_user.reading_lists.add(reading_list)
			profile_user.reading_lists.add(wish_to_read_list)
			profile_user.reading_lists.add(already_read_list)

		user = authenticate(username=username, password=password)
		if user is not None:  # Authenticate user
			login(request, user)
			if user.is_active:  # User active
				token, created = Token.objects.get_or_create(user=user)
				if token is None:  # Check if tocken already exist and return for login
					token = created

				msg = 'User has been created'
				return Response({'has_error': 'false', 'token': str(token), 'msg': msg, }, status=HTTP_200_OK)
			else:  # Inactiv user
				msg = 'User is set to inactiv'
				return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)
		else:  # Not authenticated after created
			msg = 'Error creating user'
			return Response({'has_error': 'false', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)


@csrf_exempt
@api_view(['POST'])
def logout_user(request):
	msg = ''
	if request.user.is_authenticated:
		# Authenticated user
		try:
			request.user.auth_token.delete()
		except (AttributeError, ObjectDoesNotExist):
			msg = 'Token does not exist'
			return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)

		try:
			logout(request)
			msg = 'Logout successfully'
		except Exception as e:
			msg = 'An error has occur in logout method: ' + str(e)
			return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)

		# User is logout now but in this session is still auth
		return Response({'has_error': 'false', 'msg': msg, }, status=HTTP_200_OK)
	else:  # Not authenticated user
		msg = 'Session are not authenticated'
		return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)


'''

METHOD: GET
URL: get-user/{email}
PARAMS: email
HEADERS: token 
RETURN: profile_obj_json


METHOD: GET
URL: recommended-books/
PARAMS: (deduci email din token)
HEADERS: token 
RETURN: List<Books> json format


METHOD: PUT
URL: update-user/{email}
PARAMS: email, fields_user_changed (iti trimit un obiect nou de tip User si copiezi toate campurile lui, chiar daca unele au ramas la fel)
HEADERS: token 
RETURN: 
'''


@csrf_exempt
@api_view(['GET', ])
def get_user(request, email):
	try:
		user = User.objects.get(email=email)
	except User.DoesNotExist:
		return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	try:
		obj_user = Profile.objects.get(user=user)
		serializer = ProfileSerializer(obj_user)
		profile_json = serializer.data

		for key, element in serializer.data['user'].items():
			profile_json[key] = element

		del profile_json['user']

		return Response({'has_error': 'false', 'profile_json': profile_json, }, status=HTTP_200_OK)
	except User.DoesNotExist:
		return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)


@csrf_exempt
@api_view(['GET', ])
def recommended_books(request):
	email = request.user.email
	# AI Edu
	return Response({'has_error': 'false', 'email': email, }, status=HTTP_200_OK)


@csrf_exempt
@api_view(['PUT', ])
def update_user(request, email):
	msg = 'maintenance'
	print(request.body)
	return Response({'has_error': 'false', 'msg': msg, }, status=HTTP_200_OK)


'''
METHOD: GET
URL: get-followers/{email}
PARAMS: email
HEADERS: token 
RETURN: List<Users> json format


METHOD: POST
URL: follow/
PARAMS: email_to_follow (cel care a solicitat follow il deduci din token)
HEADERS: token 
RETURN: 


METHOD: PUT
URL: book/
PARAMS: imagine_coperta (trimit poza, vezi daca ai ceva ce se numeste "Multipart")
HEADERS: token 
RETURN: Book json format

'''


@csrf_exempt
@api_view(['GET', ])
def get_followers(request):
	msg = 'maintenance'
	try:
		user = User.objects.get(email=request.user.email)
	except User.DoesNotExist:
		return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	try:
		obj_user = Profile.objects.get(user=user)
		serializer = FollowersSerializer(obj_user)
		profile_json = serializer.data
		profile_json['has_error'] = 'false'

		return Response(profile_json, status=HTTP_200_OK)
	except User.DoesNotExist:
		return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)


@csrf_exempt
@api_view(['GET', ])
def get_following(request):
	msg = 'maintenance'
	try:
		user = User.objects.get(email=request.user.email)
	except User.DoesNotExist:
		return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	try:
		obj_user = Profile.objects.get(user=user)
		serializer = FollowingSerializer(obj_user)
		profile_json = serializer.data
		profile_json['has_error'] = 'false'

		return Response(profile_json, status=HTTP_200_OK)
	except User.DoesNotExist:
		return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)


@csrf_exempt
@api_view(['GET', ])
def get_users_notfollowing(request):
	msg = 'maintenance'

	try:
		obj_user = Profile.objects.get(user=request.user)
		follow = Profile.objects.get(user=request.user).followers.all()
		follow_name = [obj.email for obj in follow]
		follow_name += [request.user.email]
		print(follow_name)
		notfollowing = User.objects.exclude(email__in=follow_name)
		print(notfollowing)
		serializer = UserSerializer(notfollowing, many=True)
		profile_json = serializer.data

		return Response(profile_json, status=HTTP_200_OK)
	except User.DoesNotExist:
		return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)


@csrf_exempt
@api_view(['POST', ])
def follow(request):
	msg = 'maintenance'
	email_to_follow = request.POST.get('email_to_follow', None)
	if email_to_follow:
		try:
			obj_user = Profile.objects.get(user=request.user)
			user_to_follow = User.objects.get(email=email_to_follow)
			user_followed_profile = Profile.objects.get(user=user_to_follow)

			obj_user.following.add(user_to_follow)
			user_followed_profile.followers.add(request.user)

			return Response({'has_error': 'false', }, status=HTTP_200_OK)
		except User.DoesNotExist:
			return Response({'has_error': 'true', }, status=HTTP_400_BAD_REQUEST)

	return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)


@csrf_exempt
@api_view(['PUT', ])
def book(request):
	msg = 'maintenance'
	image_cover = request.data.get('image_cover')

	# APP_ROOT = apps.get_app_config('rest_api').path
	# ROOT_IMG = os.path.join(APP_ROOT, "images")

	# path = os.path.join(ROOT_IMG, str(image_cover))

	book = Book(cover=image_cover)
	# Send AI Edu
	book.title = 'test'
	book.save()

	print(book.file)
	return Response({'has_error': 'false', 'book_path': str(book.file), }, status=HTTP_200_OK)


'''
METHOD: GET
URL: get-books/{titlu}
PARAMS: titlu
HEADERS: token 
RETURN: List<Book> json format


METHOD: PUT
URL: add-reading-list/
PARAMS: readinglist_object (deduci din token cine adauga)
HEADERS: token 
RETURN:


METHOD: PUT
URL: update-reading-list/{reading_list_name}
PARAMS:  reading_list_name, readinglist_object (deduci cine a facut request-ul din token)
HEADERS: token 
RETURN:

'''


@csrf_exempt
@api_view(['GET', ])
def get_books(request, title):
	msg = 'maintenance'
	books = Book.objects.filter(title=title)
	print(books)
	if books:
		try:
			serializer = BookSerializer(books, many=True)
			books_json = serializer.data
			return Response({'has_error': 'false', 'books': books_json, }, status=HTTP_200_OK)
		except User.DoesNotExist:
			return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_404_NOT_FOUND)


@csrf_exempt
@api_view(['GET', ])
def get_all_books(request):
	msg = 'maintenance'
	books = Book.objects.all()
	print(books)
	if books:
		try:
			serializer = BookSerializer(books, many=True)
			books_json = serializer.data
			return Response({'has_error': 'false', 'books': books_json, }, status=HTTP_200_OK)
		except User.DoesNotExist:
			return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_404_NOT_FOUND)


@csrf_exempt
@api_view(['PUT', ])
def add_reading_list(request):
	msg = 'maintenance'
	email = request.user.email
	return Response({'has_error': 'false', 'email': email, }, status=HTTP_200_OK)


@csrf_exempt
@api_view(['GET', ])
def get_reading_list(request):
	msg = 'maintenance'
	email = request.user.email
	reading_lists = Profile.objects.get(user=request.user).reading_lists
	if reading_lists:
		try:
			serializer = Reading_list_booksSerializer(reading_lists, many=True)
			reading_lists_json = serializer.data
			#profile_json = serializer.data
			#profile_json['has_error'] = 'false'
			return Response({'has_error': 'false', 'reading_lists_current_user': reading_lists_json, }, status=HTTP_200_OK)
		except User.DoesNotExist:
			return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	return Response({'has_error': 'false', 'email': email, }, status=HTTP_200_OK)


@csrf_exempt
@api_view(['PUT', ])
def update_reading_list(request, reading_list_name):
	msg = 'maintenance'
	email = request.user.email
	return Response({'has_error': 'false', 'reading_list_name': reading_list_name, 'email': email, }, status=HTTP_200_OK)


@csrf_exempt
@api_view(['GET', ])
def get_reading_list_books(request):
	msg = 'maintenance'
	email = request.user.email
	reading_lists = Profile.objects.get(user=request.user).reading_lists
	if reading_lists:
		try:
			serializer = Reading_list_booksSerializer(reading_lists)
			reading_lists_json = serializer.data
			return Response({'has_error': 'false', 'reading_lists_current_user': reading_lists_json, }, status=HTTP_200_OK)
		except User.DoesNotExist:
			return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	return Response({'has_error': 'false', 'email': email, }, status=HTTP_404_NOT_FOUND)


'''
METHOD: GET
URL: users/
PARAMS: 
HEADERS: token 
RETURN: List<User> json format


METHOD: PUT
URL: update-book/{title_book}
PARAMS: title_book, fields_book_changed (iti trimit un obiect nou de tip Book si copiezi toate campurile lui, chiar daca unele au ramas la fel)
HEADERS: token 
RETURN: 


METHOD: GET
URL: get-categories/
PARAMS: 
HEADERS: token 
RETURN: List<Tag> json format

'''


@csrf_exempt
@api_view(['GET', ])
def users(request):
	msg = 'maintenance'
	users = Profile.objects.all()
	if users:
		try:
			serializer = ProfileSerializer(users, many=True)
			profile_json = serializer.data
			profile_json = {'users': profile_json, 'has_error': 'false'}
			return Response(profile_json, status=HTTP_200_OK)
		except User.DoesNotExist:
			return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	return Response({'has_error': 'false', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)


@csrf_exempt
@api_view(['PUT', ])
def update_book(request, title_book):
	msg = 'maintenance'
	email = request.user.email
	return Response({'has_error': 'false', 'title_book': title_book, 'email': email, }, status=HTTP_200_OK)


@csrf_exempt
@api_view(['GET', ])
def get_categories(request):
	msg = 'maintenance'
	tags = Tag.objects.all()
	if users:
		try:
			serializer = TagSerializer(tags, many=True)
			tags_json = serializer.data
			return Response({'has_error': 'false', 'categories': tags_json, }, status=HTTP_200_OK)
		except User.DoesNotExist:
			return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	return Response({'has_error': 'false', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)



'''

METHOD: GET
URL: get-books-category/{tag_name}
PARAMS: tag_name
HEADERS: token 
RETURN: List<Book> json format


METHOD: GET
URL: get-conversations/
PARAMS: (deduci cine face cererea din token)
HEADERS: token 
RETURN: List<Conversation> json format


METHOD: GET
URL: get-conversation/{email_destinatar}
PARAMS: email_destinatar (deduci cine face cererea din token)
HEADERS: token 
RETURN: Conversation json format


METHOD: POST
URL: send-message/
PARAMS: email_destinatar content date (deduci cine a trimis din token)


METHOD: GET
URL: current-user/
PARAMS: 
RETURN: Return object of current user
'''


@csrf_exempt
@api_view(['GET', ])
def get_books_category(request, tag_name):
	msg = 'maintenance'
	try:
		tag = Tag.objects.get(name=tag_name)
	except Tag.DoesNotExist:
		return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	books = Book.objects.filter(books_tags=tag)
	if books:
		try:
			serializer = BookSerializer(books, many=True)
			books_json = serializer.data
			return Response(books_json, status=HTTP_200_OK)
		except User.DoesNotExist:
			return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	return Response({'has_error': 'false', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)


@csrf_exempt
@api_view(['GET', ])
def get_conversations(request, email):
	msg = 'maintenance'
	print(email)
	user = User.objects.filter(Q(email=email) & Q(username=email))
	print(user)
	messages = None
	if user:
		user = user.first()
		messages = Message.objects.filter(Q(user_sender=user) | Q(user_receiver=user))

	if messages:
		try:
			serializer = MessageSerializer(messages, many=True)
			conversations_json = serializer.data
			return Response({'has_error': 'false', 'conversations': conversations_json, }, status=HTTP_200_OK)
		except Message.DoesNotExist:
			return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	return Response({'has_error': 'false', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)


@csrf_exempt
@api_view(['GET', ])
def get_all_conversations(request):
	msg = 'maintenance'
	print("ALL")
	messages = Message.objects.filter(Q(user_sender=request.user) | Q(user_receiver=request.user))
	if messages:
		try:
			serializer = MessageSerializer(messages, many=True)
			conversations_json = serializer.data
			return Response({'has_error': 'false', 'conversations': conversations_json, }, status=HTTP_200_OK)
		except Message.DoesNotExist:
			return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

	return Response({'has_error': 'false', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)


@csrf_exempt
@api_view(['POST', ])
def send_message(request):
	content = request.POST.get('content', '')
	email_destinatar = request.POST.get('email_destinatar', '')
	user_destinatar = User.objects.get(email=email_destinatar)
	if user_destinatar:
		msg = Message(user_sender=request.user, user_receiver=user_destinatar, content=content)
		msg.save()
		return Response({'has_error': 'false', }, status=HTTP_200_OK)
	else:
		return Response({'has_error': 'false', }, status=HTTP_400_BAD_REQUEST)


##########

@csrf_exempt
@api_view(['GET', ])
def current_user(request):
	email = request.data.get('email', None)
	obj_user = Profile.objects.get(user=User.objects.get(email=email))
	serializer = ProfileSerializer(obj_user)
	profile_json = serializer.data
	return Response({'has_error': 'false', 'email': email, 'profile_json': profile_json, }, status=HTTP_200_OK)

##########


