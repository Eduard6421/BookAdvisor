from Levenshtein import distance
from django.contrib.postgres.search import TrigramSimilarity
from django.contrib.postgres.search import SearchVector
from django.db.models import Func
from django.db.models import F
from django.shortcuts import render
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
from rest_framework.response import Response
from rest_framework.decorators import api_view
from django.http import JsonResponse

from django.core.files.storage import default_storage
from django.core.files.base import ContentFile

from django.shortcuts import render, render_to_response
from django.template import RequestContext
from django.contrib.auth import logout, authenticate, login, logout
from django.http import HttpResponseRedirect, HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.core.files.storage import default_storage
import os
from django.apps import apps
from django.db.models import Q
from django.contrib.postgres.search import TrigramSimilarity

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
from django.db.models import Func
from django.db.models import F


import uuid
import json
import requests
import base64
import ast
import random

from ..models import *
from ..serializers import *
#from ..tests import *

from django.db import connection
with connection.cursor() as cursor:
    cursor.execute('CREATE EXTENSION IF NOT EXISTS pg_trgm')
    print('hentai lover')

print('hentai non-lover')

# receive an profile obj or a list of profiles
def parse_user(user_profile):
    if user_profile is None:
        return None
    try:

        if type(user_profile) is list:
            serializer = ProfileSerializer(user_profile, many=True)
            profile_json = serializer.data
            #print(profile_json)
            for profile_elem in profile_json:
                for key, element in profile_elem['user'].items():
                    profile_elem[key] = element
                del profile_elem['user']
            return profile_json
        else:
            serializer = ProfileSerializer(user_profile)
            profile_json = serializer.data
            print(profile_json)
            for key, element in serializer.data['user'].items():
                profile_json[key] = element
            del profile_json['user']
            return profile_json
    except User.DoesNotExist:
        return None
