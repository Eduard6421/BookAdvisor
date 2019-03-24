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


import uuid
import json

from .models import *

# Create your views here.


@csrf_exempt
@api_view(['POST'])
@permission_classes((AllowAny,))
def login_user(request):
	msg = ''
	if request.user.is_authenticated:
		msg = 'Session already authenticated'
		return Response({ 'has_error': 'true', 'msg': msg,},
                        status=HTTP_400_BAD_REQUEST)
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
			return Response({ 'has_error': 'true', 'msg': msg,},
                        status=HTTP_400_BAD_REQUEST)

		user = authenticate(username=username, password=password)
		if user is not None: # Authenticate user
			login(request, user)
			if user.is_active: # User active
				token, created = Token.objects.get_or_create(user=user)
				if token is None: # Check if tocken already exist and return for login
					token = created
				msg = 'User has been successfully authenticate'
				return Response({ 'has_error': 'false', 'token': str(token), 
					'msg': msg,},
                        status=HTTP_200_OK)
			else: # Inactiv user
				msg = 'User is set to inactiv'
				return Response({'has_error': 'true', 'msg': msg,},
                        status=HTTP_400_BAD_REQUEST)
		else: # Not authenticated after created
			msg = 'Error login user'
			return Response({'has_error': 'false', 'msg': msg,},
                        status=HTTP_404_NOT_FOUND)




@csrf_exempt
@api_view(['POST'])
@permission_classes((AllowAny,))
def register_user(request):
	msg = ''
	if request.user.is_authenticated:
		msg = 'Session already authenticated'
		return Response({ 'has_error': 'true', 'msg': msg,},
                        status=HTTP_400_BAD_REQUEST)
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
			return Response({ 'has_error': 'true', 'msg': msg,},
                        status=HTTP_400_BAD_REQUEST)

		# check if already exist an user with this username
		user_already_exist = User.objects.filter(username=username)
		if user_already_exist.first() is not None:
			# username already exist ... 
			msg = 'This user already exist'
			return Response({ 'has_error': 'true', 'msg': msg,},
                        status=HTTP_400_BAD_REQUEST)
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
				return Response({ 'has_error': 'false', 'token': str(token), 
					'msg': msg,},
                        status=HTTP_200_OK)
			else: # Inactiv user
				msg = 'User is set to inactiv'
				return Response({'has_error': 'true', 'msg': msg,},
                        status=HTTP_400_BAD_REQUEST)
		else: # Not authenticated after created
			msg = 'Error creating user'
			return Response({'has_error': 'false', 'msg': msg,},
                        status=HTTP_400_BAD_REQUEST)


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
			return Response({'has_error': 'true', 'msg': msg,},
                        status=HTTP_400_BAD_REQUEST)

		try:
			logout(request)
			msg = 'Logout successfully'
		except Exception as e:
			msg = 'An error has occur in logout method: ' + str(e)
			return Response({'has_error': 'true', 'msg': msg,},
                        status=HTTP_400_BAD_REQUEST)
	
		# User is logout now but in this session is still auth
		return Response({'has_error': 'false', 'msg': msg,},
                        status=HTTP_200_OK)
	else: # Not authenticated user
		msg = 'Session are not authenticated'
		return Response({'has_error': 'true', 'msg': msg,},
                        status=HTTP_400_BAD_REQUEST)


@csrf_exempt
@api_view(['GET', ])
def get_recommanded_books(request):
	msg = 'maintenance'

	return Response({'has_error': 'false', 'msg': msg,},
                        status=HTTP_200_OK)


@csrf_exempt
@api_view(['GET', ])
def get_books(request):
	msg = 'maintenance'

	return Response({'has_error': 'false', 'msg': msg,},
                        status=HTTP_200_OK)


@csrf_exempt
@api_view(['GET', ])
def get_user(request):
	msg = 'maintenance'
	
	return Response({'has_error': 'false', 'msg': msg,},
                        status=HTTP_200_OK)




