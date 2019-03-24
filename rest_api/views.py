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
import uuid

import json

from .models import *

# Create your views here.


class CustomAuthToken(ObtainAuthToken):

	def post(self, request, *args, **kwargs):
	    serializer = self.serializer_class(data=request.data,
	                                       context={'request': request})
	    serializer.is_valid(raise_exception=True)
	    user = serializer.validated_data['user']
	    token, created = Token.objects.get_or_create(user=user)
	    return Response({'token': token.key, 'user_id': user.pk, 'email': user.email })




@api_view(['POST'])
def login_user(request):
	msg = ''
	if request.user.is_authenticated:
		msg = 'Session already authenticated'
		return JsonResponse({ 'has_error': 'true', 'msg': msg,})
	else: # Not authenticated user
		username = request.POST.get('username', None)
		password = request.POST.get('password', None)
		first_name = request.POST.get('first_name', '')
		last_name = request.POST.get('last_name', '')
		email = request.POST.get('email', '')

		ok_param = True
	
		# check parameters if exists
		if username is None:
			msg += 'username field is required '
			ok_param = False
		if password is None:
			msg += 'password field is required '
			ok_param = False

		if not ok_param:
			return JsonResponse({ 'has_error': 'true', 'msg': msg,})

		user = authenticate(username=username, password=password)
		if user is not None: # Authenticate user
			login(request, user)
			if user.is_active: # User active
				token, created = Token.objects.get_or_create(user=user)
				if token is None: # Check if tocken already exist and return for login
					token = created
				msg = 'User has been successfully authenticate'
				return Response({ 'has_error': 'false', 'token': str(token), 
					'msg': msg,})
			else: # Inactiv user
				msg = 'User is set to inactiv'
				return JsonResponse({'has_error': 'true', 'msg': msg,})
		else: # Not authenticated after created
			msg = 'Error login user'
			return JsonResponse({'has_error': 'false', 'msg': msg,})




@api_view(['POST'])
def register_user(request):
	msg = ''
	if request.user.is_authenticated:
		msg = 'Session already authenticated'
		return JsonResponse({ 'has_error': 'true', 'msg': msg,})
	else: # Not authenticated user
		username = request.POST.get('username', None)
		password = request.POST.get('password', None)
		first_name = request.POST.get('first_name', '')
		last_name = request.POST.get('last_name', '')
		email = request.POST.get('email', '')

		ok_param = True
	
		# check parameters if exists
		if username is None:
			msg += 'username field is required '
			ok_param = False
		if password is None:
			msg += 'password field is required '
			ok_param = False

		if not ok_param:
			return JsonResponse({ 'has_error': 'true', 'msg': msg,})

		# check if already exist an user with this username
		user_already_exist = User.objects.filter(username=username)
		if user_already_exist.first() is not None:
			# username already exist ... 
			msg = 'This user already exist'
			return JsonResponse({ 'has_error': 'true', 'msg': msg,})
		else:
			user = User(username=username, first_name=first_name, last_name=last_name, email=email)
			user.set_password(password)
			user.save()
		user = authenticate(username=username, password=password)
		if user is not None: # Authenticate user
			login(request, user)
			if user.is_active: # User active
				token, created = Token.objects.get_or_create(user=user)
				if token is None: # Check if tocken already exist and return for login
					token = created

				msg = 'User has been created'
				return JsonResponse({ 'has_error': 'false', 'token': str(token), 
					'msg': msg,})
			else: # Inactiv user
				msg = 'User is set to inactiv'
				return JsonResponse({'has_error': 'true', 'msg': msg,})
		else: # Not authenticated after created
			msg = 'Error creating user'
			return JsonResponse({'has_error': 'false', 'msg': msg,})


@api_view(['POST'])
def logout_user(request):
	msg = ''
	if request.user.is_authenticated:
		# Authenticated user
		try:
			logout(request)
			msg = 'Logout successfully'
		except Exception as e:
			msg = 'An error has occur in logout method: ' + str(e)
			return JsonResponse({'has_error': 'true', 'msg': msg,})
	
		# User is logout now but in this session is still auth
		return JsonResponse({'has_error': 'false', 'msg': msg,})
	else: # Not authenticated user
		msg = 'Session are not authenticated'
		return JsonResponse({'has_error': 'true', 'msg': msg,})



@api_view(['GET', ])
def get_recommanded_books(request):
	msg = 'maintenance'
	return JsonResponse({'has_error': 'false', 'msg': msg,})


@api_view(['GET', ])
def get_books(request):
	msg = 'maintenance'
	return JsonResponse({'has_error': 'false', 'msg': msg,})


@api_view(['GET', ])
def get_user(request):
	msg = 'maintenance'
	return JsonResponse({'has_error': 'false', 'msg': msg,})


@api_view(['GET', 'POST', ])
def index(request):
	logout_status = None
	if request.user.is_authenticated:
		# Authenticated user
		try:
			logout(request)
			logout_status = 'Logout successfully'
		except Exception as e:
			logout_status = 'An error has occur in logout method: ' + str(e)
	
		# User is logout now but in this session is still auth
		return JsonResponse({'test':'1', 'logout_status': logout_status,})
	else: # Not authenticated user
		username = 'user1'
		password = 'user1'

		already_exist = User.objects.filter(username=username)
		if already_exist.first() is None: # If user not already exist
			user = User(username=username, first_name='first_user', last_name='last_user', email='user@yahoo.com')
			user.set_password(password)
			user.save()
			print("already_exist")

		user = authenticate(username=username, password=password)
		if user is not None: # Authenticate user
			login(request, user)
			if user.is_active: # User active
				token, created = Token.objects.get_or_create(user=user)
				if token is None: # Check if tocken already exist and return for login
					token = created
				return JsonResponse({'token': str(token), 'username': username, })
			else: # Inactiv user
				return JsonResponse({'test':'2'})
		else: # Not authenticated
			return JsonResponse({'test':'3'})
