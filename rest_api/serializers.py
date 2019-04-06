from rest_framework import serializers
from .models import *

# djangorestframework-recursive


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'username', 'first_name', 'last_name', 'email', 'last_login', 'date_joined', 'is_active')


class TagSerializer(serializers.ModelSerializer):

    class Meta:
        model = Tag
        fields = '__all__'


class ReviewSerializer(serializers.ModelSerializer):
    user_review = UserSerializer()

    class Meta:
        model = Review
        fields = '__all__'


class AuthorSerializer(serializers.ModelSerializer):

    class Meta:
        model = Author
        fields = '__all__'


class CommentSerializer(serializers.ModelSerializer):
    user_comment = UserSerializer()

    class Meta:
        model = Message
        fields = '__all__'


class BookSerializer(serializers.ModelSerializer):
    authors = AuthorSerializer(read_only=True, many=True)
    comments = CommentSerializer(read_only=True, many=True)
    reviews = ReviewSerializer(read_only=True, many=True)
    books_tags = TagSerializer(read_only=True, many=True)

    class Meta:
        model = Book
        fields = '__all__'


class Reading_list_booksSerializer(serializers.ModelSerializer):
    books = BookSerializer(read_only=True, many=True)
    tags = TagSerializer(read_only=True, many=True)

    class Meta:
        model = Reading_list_books
        fields = '__all__'


class MessageSerializer(serializers.ModelSerializer):
    user_sender = UserSerializer()
    user_receiver = UserSerializer()

    class Meta:
        model = Message
        fields = '__all__'


class FollowersSerializer(serializers.ModelSerializer):
    followers = UserSerializer(read_only=True, many=True)

    class Meta:
        model = Profile
        fields = ('followers', )


class FollowingSerializer(serializers.ModelSerializer):
    following = UserSerializer(read_only=True, many=True)

    class Meta:
        model = Profile
        fields = ('following', )


class ProfileSerializer(serializers.ModelSerializer):
    user = UserSerializer()
    reading_lists = Reading_list_booksSerializer(read_only=True, many=True)
    followers = UserSerializer(read_only=True, many=True)
    following = UserSerializer(read_only=True, many=True)
    favorite_tags = TagSerializer(read_only=True, many=True)

    class Meta:
        model = Profile
        fields = '__all__'



