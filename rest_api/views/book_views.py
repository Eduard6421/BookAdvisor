from .base_views import *


@csrf_exempt
@api_view(['PUT', ])
def update_book(request, id_book):
    msg = 'maintenance'
    email = request.user.email
    book_update = request.data
    try:
        book = Book.objects.get(id=id_book)
        book.title = book_update['title']
        book.description = book_update['description']

        book.save()
        return Response({'has_error': 'false', 'email': email, }, status=HTTP_200_OK)
    except Book.DoesNotExist:
        return Response({'has_error': 'false', 'email': email, }, status=HTTP_400_BAD_REQUEST)


@csrf_exempt
@api_view(['GET', ])
def get_categories(request):
    msg = 'maintenance'
    tags = Tag.objects.all()
    try:
        serializer = TagSerializer(tags, many=True)
        tags_json = serializer.data

        return Response(tags_json, status=HTTP_200_OK)
    except User.DoesNotExist:
        return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

    return Response({'has_error': 'false', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)




@csrf_exempt
@api_view(['GET',])
def get_book(request, book_id):
    email = request.user.email

    try:
        book = Book.objects.get(id=book_id)
        serializer = BookSerializer(book)
        book_json = serializer.data

        for review in book_json['reviews']:
            firebaseUID_user = Profile.objects.get(user=User.objects.get(email=review['user_review']['email'])).firebaseUID
            profile_picture_user = Profile.objects.get(user=User.objects.get(email=review['user_review']['email'])).profile_picture
            review['user_review']['profile_picture'] = profile_picture_user
            review['user_review']['firebaseUID'] = firebaseUID_user
            #print(firebaseUID_user)
            #print(profile_picture_user)


        #print(book_json)

        return Response(book_json)
    except Book.DoesNotExist:
        return Response({'has_error': 'false', 'email': email, }, status=HTTP_400_BAD_REQUEST)



@csrf_exempt
@api_view(['GET', ])
def recommended_books(request):
    # AI Edu
    user_input = {}
    book_score = {}

    profile_obj = Profile.objects.filter(user=request.user)
    if profile_obj.first() is None:
        return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)
    profile_obj = profile_obj.first()

    for reading_list in profile_obj.reading_lists.all():
        for book in reading_list.books.all():
            str_book = 'book_id_' + str(book.id)
            for review in book.reviews.all():
                book_score[str_book] = review.score

    api_ModelAI = 'http://localhost:8769/api/recsys'
    headers = {'cache-control':'no-cache','content-type': 'application/json',}

    req = requests.Request('POST', api_ModelAI, headers=headers, json=book_score)
    prepared = req.prepare()
    sess = requests.Session()
    resp = sess.send(prepared)

    result_ai = ast.literal_eval(resp.text)['recommandations']
    book_id_recommended = [elem[0] for elem in result_ai]

    # print('After AI module call')
    books_recommended = Book.objects.filter(id__in=book_id_recommended)

    if books_recommended:
        try:
            serializer = BookSerializer(books_recommended, many=True)
            books_json = serializer.data

            return Response(books_json, status=HTTP_200_OK)
        except User.DoesNotExist:
            return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

    return Response({'has_error': 'false', 'email': '', }, status=HTTP_200_OK)




@csrf_exempt
@api_view(['PUT', ])
def book(request):
    msg = 'maintenance'
    image_cover = request.data.get('cover', None)
    #print(request.data)
    # APP_ROOT = apps.get_app_config('rest_api').path
    # ROOT_IMG = os.path.join(APP_ROOT, "images")

    # path = os.path.join(ROOT_IMG, str(image_cover))
    if image_cover is None:
        return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_400_BAD_REQUEST)

    book = Book(cover=image_cover)
    # Send AI Edu

    headers = {"Content-type": "application/x-www-form-urlencoded"}

    EASTModelAPI='http://localhost:8769/api/east'
    #req = requests.Request('POST',EASTModelAPI,headers=headers, files=base64.b64encode(image_cover.read()) )
    r = requests.post(EASTModelAPI, files={'image': image_cover.read()})
    result_ai = ast.literal_eval(r.text)
    result_ai = [val['outputs']['output']  for key,val in result_ai.items() if 'textbox' in key]

    # print(result_ai)
    b = Book.objects.filter(title__icontains=result_ai)

    tmpset = set()
    text_ai_join = ' '.join(result_ai)

    result_list = []
    for i in range(0,7):
        min_pos = -1
        min_value = 100000
        for book in Book.objects.all():
            if book.id not in tmpset :
                curent_value = distance(book.title, text_ai_join)
                if curent_value < min_value:
                    min_value = curent_value
                    min_pos = book.id

        tmpset.add(min_pos)

        result_list.append(Book.objects.filter(id=min_pos).first())
        #for b in Book.objects.all():
        #    if len(b.title) < 4:
        #        print(b.id)

    #print("text: " + text_ai_join)
    #print("book title: " + str(book_ocr.autors.all()[0].name))
    serializer = BookSerializer(result_list,many=True)
    books_json = serializer.data

    #print(books_json)

    return Response(books_json, status=HTTP_200_OK)

    return Response({'has_error': 'false', 'book_path': str(book.cover), }, status=HTTP_200_OK)





@csrf_exempt
@api_view(['GET', ])
def get_books(request,title):
    msg = 'maintenance'

    books = Book.objects.annotate(similarity=TrigramSimilarity('title', title), ).filter(similarity__gt=0.2).order_by('-similarity')[:4]
    authors = Author.objects.annotate(similarity=TrigramSimilarity('name', title), ).filter(similarity__gt=0.2).order_by('-similarity')

    authorbooks = Book.objects.filter(authors__in=authors)[:4]

    books = (books | authorbooks)

    if books:
        try:
            serializer = BookSerializer(books, many=True)
            books_json = serializer.data

            return Response(books_json, status=HTTP_200_OK)
        except User.DoesNotExist:
            return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

    return Response({'has_error': 'true', 'msg': msg, }, status=HTTP_404_NOT_FOUND)


@csrf_exempt
@api_view(['GET', ])
def get_all_books(request):
    msg = 'maintenance'
    books = Book.objects.all()
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
def get_books_category(request, tag_name):
    msg = 'maintenance'
    try:
        tag = Tag.objects.get(name=tag_name)
    except Tag.DoesNotExist:
        return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

    books = Book.objects.filter(books_tags=tag)[:20]
    if books:
        try:
            serializer = BookSerializer(books, many=True)
            books_json = serializer.data

            return Response(books_json, status=HTTP_200_OK)
        except User.DoesNotExist:
            return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

    return Response([], status=HTTP_200_OK)



@csrf_exempt
@api_view(['GET', ])
def get_filter_books(request, term_filter):
    books = Book.objects.filter(Q(title__icontains=term_filter))[:10]
    authors = Author.objects.filter(Q(name__icontains=term_filter))
    #books = [Book.objects.filter(authors__name=author.name) for author in authors]
    book_result = []
    for book in books:
       book_result.append(book)

    a_books = [Book.objects.filter(authors=autor_books) for autor_books in authors]
    for collection_books in a_books:
        for book in collection_books:
            book_result.append(book)

    #print(book_result)
    #print(authors)
    if books:
        serializer = BookSerializer(book_result[:20], many=True)
        books_json = serializer.data

        return Response(books_json, status=HTTP_200_OK)

    return Response([], status=HTTP_200_OK)




@csrf_exempt
@api_view(['GET', ])
def get_reviews(request, title):
    msg = 'maintenance'
    book = Book.objects.filter(title=title)

    if book.first() is None:
        return Response({'has_error': 'true', }, status=HTTP_400_BAD_REQUEST)
    else:
        book = book.first()
        serializer = BookSerializer(book)
        book_json = serializer.data
        for review in book_json['reviews']:
            firebaseUID_user = Profile.objects.get(user=User.objects.get(email=review['user_review']['email'])).firebaseUID
            profile_picture_user = Profile.objects.get(user=User.objects.get(email=review['user_review']['email'])).profile_picture
            review['user_review']['profile_picture'] = profile_picture_user

        reviews = book_json['reviews']



        return Response(reviews, status=HTTP_200_OK)


@csrf_exempt
@api_view(['POST', ])
def add_review(request, book_id):
    msg = 'maintenance'
    reviews_json = request.data
    #print(reviews_json)
    book = Book.objects.filter(id=book_id)

    if book.first() is None:
        return Response({'has_error': 'true', }, status=HTTP_400_BAD_REQUEST)
    else:
        book = book.first()

        review = Review(content=reviews_json['content'], score=float(reviews_json['score']),
                        user_review=request.user)
        review.save()
        #print(book_id)
        #print(review.content)
        #print(review.score)
        #print(review.user_review)
        book.reviews.add(review)
        book.rating = sum([review.score for review in book.reviews.all()]) / len(book.reviews.all())
        book.save()

        return Response({'has_error': 'false'}, status=HTTP_200_OK)



@csrf_exempt
@api_view(['GET', ])
def get_book_cover(request, uuid_img):
    msg = 'maintenance'
    try:
        with open('/data/BookAdvisor/images/' + uuid_img, "rb") as f:
            return HttpResponse(f.read(), content_type="image/jpeg")
    except IOError:
        red = Image.new('RGBA', (1, 1), (255,0,0,0))
        response = HttpResponse(content_type="image/jpeg")
        red.save(response, "JPEG")
        return response
