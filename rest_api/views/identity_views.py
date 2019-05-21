from .base_views import *


@csrf_exempt
@api_view(['POST', 'GET'])
@permission_classes((AllowAny,))
def index(request):
    return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)

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
                msg = 'User has been successfully logged in'
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
        firebaseUID = request.POST.get('firebaseUID', '')

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
            profile_user.firebaseUID = firebaseUID

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
    obj_user = Profile.objects.get(user=request.user)
    res = parse_user(obj_user)
    return Response(res, status=HTTP_200_OK)


@csrf_exempt
@api_view(['PUT', ])
def update_user(request):
    msg = 'maintenance'
    update_user = request.data
    user = User.objects.get(email=request.user.email)
    user.first_name = update_user['first_name']
    user.last_name = update_user['last_name']
    user.save()
    return Response({'has_error': 'false', 'msg': msg, }, status=HTTP_200_OK)


@csrf_exempt
@api_view(['GET', ])
def get_followers(request):
    msg = 'maintenance'
    try:
        profile_obj = Profile.objects.get(user=request.user)
        obj_user = [Profile.objects.get(user=u) for u in profile_obj.followers.all()]
        res = parse_user(obj_user)

        return Response(res, status=HTTP_200_OK)
    except User.DoesNotExist:
        return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)


@csrf_exempt
@api_view(['GET', ])
def find_user(request, term_filter):
    msg = 'maintenance'

    try:
        users_filter = [Profile.objects.get(user=u) for u in User.objects.filter(Q(first_name__contains=term_filter)|Q(last_name__contains=term_filter))]
        res = parse_user(users_filter)

        return Response(res, status=HTTP_200_OK)
    except User.DoesNotExist:
        return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)




@csrf_exempt
@api_view(['GET', ])
def get_following(request):
    msg = 'maintenance'

    try:
        profile_obj = Profile.objects.get(user=request.user)
        obj_user = [Profile.objects.get(user=u) for u in profile_obj.following.all()]
        res = parse_user(obj_user)

        return Response(res, status=HTTP_200_OK)
    except User.DoesNotExist:
        return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)



@csrf_exempt
@api_view(['GET', ])
def find_new_people(request):
    msg = 'maintenance'

    try:
        obj_user = Profile.objects.get(user=request.user)
        following = obj_user.following.all()
        if following.exists():
            following_user = [o.id for o in obj_user.following.all()]
            new_people = User.objects.all().exclude(id__in=following_user)
            new_people = [Profile.objects.get(user=u) for u in new_people if u.id is not request.user.id]
        else:
            new_people = [Profile.objects.get(user=u) for u in User.objects.all()[:10] if u.id is not request.user.id]
        res = parse_user(new_people)
        return Response(res, status=HTTP_200_OK)
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

        notfollowing = User.objects.exclude(email__in=follow_name)
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




# /get_user_by_firebase/{firebaseUID}
# return obiect user ca la get_user


@csrf_exempt
@api_view(['POST', ])
def get_user_by_firebase(request, firebaseUID):
    msg = 'maintenance'
    try:
        obj_user = Profile.objects.filter(firebaseUID=firebaseUID)

        if obj_user.first() is None:
            return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)
        else:
            obj_user = obj_user.first()

        serializer = ProfileSerializer(obj_user)
        profile_json = serializer.data
        for key, element in serializer.data['user'].items():
            profile_json[key] = element
        del profile_json['user']
        for reading_list in profile_json['reading_lists']:
            for books in reading_list['books']:
                if books:
                    for book in reading_list['books']:
                        authors = []
                        for author in book['authors']:
                            for key, element in author.items():
                                if key == 'name':
                                    authors.append(element)

                        for review in book['reviews']:
                            for key, element in review['user_review'].items():
                                if key == 'id':
                                    pass
                                else:
                                    review[key] = element

                            del review['user_review']
                        del book['authors']
                        book['authors'] = []
                        for author in authors:
                            book['authors'].append(author)

        return Response(profile_json, status=HTTP_200_OK)
    except User.DoesNotExist:
        return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)





@csrf_exempt
@api_view(['PUT', ])
def update_user_pic(request):
    try:
       image_cover = request.data.get('image_profile', None)

       path_img = default_storage.save('/data/BookAdvisor/media/' + str(uuid.uuid4()) + ".jpg", ContentFile(request.data.get('profile',None).read()))
       print(path_img)

       obj_user = Profile.objects.get(user=request.user)
       obj_user.profile_picture = path_img
       obj_user.save()
       serializer = ProfileSerializer(obj_user)
       profile_json = serializer.data

       for key, element in serializer.data['user'].items():
           profile_json[key] = element
       del profile_json['user']
       for reading_list in profile_json['reading_lists']:
           for books in reading_list['books']:
               if books:
                   for book in reading_list['books']:
                       authors = []
                       for author in book['authors']:
                           for key, element in author.items():
                               if key == 'name':
                                   authors.append(element)

                       for review in book['reviews']:
                           for key, element in review['user_review'].items():
                               if key == 'id':
                                   pass
                               else:
                                   review[key] = element

                           del review['user_review']
                       del book['authors']
                       book['authors'] = []
                       for author in authors:
                           book['authors'].append(author)

       return Response(profile_json, status=HTTP_200_OK)
    except User.DoesNotExist:
        return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)




@csrf_exempt
@api_view(['GET', ])
def get_profil_img(request, uuid_img):
    try:
        with open('/data/BookAdvisor/media/' + uuid_img, "rb") as f:
            return HttpResponse(f.read(), content_type="image/jpeg")
    except IOError:
        red = Image.new('RGBA', (1, 1), (255,0,0,0))
        response = HttpResponse(content_type="image/jpeg")
        red.save(response, "JPEG")
        return response



### TEST METHODS


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
@api_view(['GET', ])
def current_user(request):
    email = request.data.get('email', None)
    obj_user = Profile.objects.get(user=User.objects.get(email=email))
    serializer = ProfileSerializer(obj_user)

    profile_json = serializer.data
    return Response({'has_error': 'false', 'email': email, 'profile_json': profile_json, }, status=HTTP_200_OK)
