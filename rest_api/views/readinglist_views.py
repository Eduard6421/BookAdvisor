from .base_views import *



@csrf_exempt
@api_view(['PUT', ])
def add_reading_list(request):
    msg = 'maintenance'
    email = request.user.email

    reading_list_json = request.data
    reading_list = Reading_list_books(title=reading_list_json['title'])
    reading_list.save()

    profile_user = Profile.objects.get(user=request.user)
    profile_user.reading_lists.add(reading_list)

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

            return Response({'has_error': 'false', 'reading_lists_current_user': reading_lists_json, }, status=HTTP_200_OK)
        except User.DoesNotExist:
            return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

    return Response({'has_error': 'false', 'email': email, }, status=HTTP_200_OK)


@csrf_exempt
@api_view(['PUT', ])
def update_reading_list(request, reading_list_name):
    msg = 'maintenance'
    print(request.data)
    reading_list_json = request.data
    profile_user = Profile.objects.get(user=request.user)
    reading_list = profile_user.reading_lists.get(title=reading_list_json['title'])

    reading_list.tags.remove()
    reading_list.books.remove()

    for book in reading_list_json['books']:
        book_db = Book.objects.get(id=int(book['id']))
        reading_list.books.add(book_db)

        for tag in book_db.books_tags.all():
            tag_filter = reading_list.tags.filter(tag)
            if tag_filter.first() is not None:
                reading_list.tags.add(tag)


    for tag in reading_list_json['tags']:
        tag_db = Tag.objects.get(id=int(tag['id']))
        reading_list.tags.add(tag_db)

    if reading_list:
        try:
            #serializer = Reading_list_booksSerializer(reading_list, many=True)
            #print(serializer.data)
            #reading_lists_json = serializer.data
            return Response({'has_error': 'false', }, status=HTTP_200_OK)
        except User.DoesNotExist:
            return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

    return Response({'has_error': 'false', 'reading_list_name': reading_list_name, }, status=HTTP_200_OK)


@csrf_exempt
@api_view(['GET', ])
def get_reading_list_books(request):
    msg = 'maintenance'
    email = request.user.email
    reading_lists = Profile.objects.get(user=request.user).reading_lists
    if reading_lists:
        try:
            serializer = Reading_list_booksSerializer(reading_lists, many=True)
            reading_lists_json = serializer.data
            return Response({'has_error': 'false', 'reading_lists_current_user': reading_lists_json, }, status=HTTP_200_OK)
        except User.DoesNotExist:
            return Response({'has_error': 'true', }, status=HTTP_404_NOT_FOUND)

    return Response({'has_error': 'false', 'email': email, }, status=HTTP_404_NOT_FOUND)
