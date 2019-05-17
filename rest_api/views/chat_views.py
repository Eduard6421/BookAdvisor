from .base_views import *

@csrf_exempt
@api_view(['GET', ])
def get_conversations(request, email):
    msg = 'maintenance'
    user = User.objects.filter(Q(email=email) & Q(username=email))
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


