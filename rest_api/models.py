from django.db import models

from django.contrib.auth import get_user_model
from django.db import models
import datetime

# Create your models here.

User = get_user_model()


class Tag(models.Model):
    name = models.CharField(max_length=255, default='')


class Comment(models.Model):
    content = models.CharField(max_length=255, default='')
    date = models.DateTimeField(default=datetime.datetime.now)
    user_comment = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user_comment', blank=True)


class Author(models.Model):
    name = models.CharField(max_length=255, default='')


class Review(models.Model):
    score = models.IntegerField(default=0)
    user_review = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user_review', blank=True)


class Book(models.Model):
    title = models.CharField(max_length=255, default='')
    description = models.CharField(max_length=255, default='')
    publish_date = models.DateTimeField(default=datetime.datetime.now)
    rating = models.IntegerField(default=0)
    cover = models.CharField(max_length=255, default='')
    author = models.ManyToManyField(Author, related_name='author', blank=True)
    comments = models.ManyToManyField(Comment, related_name='comments', blank=True)
    reviews = models.ManyToManyField(Review, related_name='reviews', blank=True)
    books_tags = models.ManyToManyField(Tag, related_name='books_tags', blank=True)


class Reading_list_books(models.Model):
    books = models.ManyToManyField(Tag, related_name='books', blank=True)


class Wishlist(models.Model):
    name = models.CharField(max_length=255, default='')
    books = models.ManyToManyField(Book, related_name='books', blank=True)


class Message(models.Model):
    content = models.CharField(max_length=255, default='')
    date = models.DateTimeField(default=datetime.datetime.now)
    user_sender = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user_sender', blank=True)
    user_receiver = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user_receiver', blank=True)


class Profile(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user', blank=True)
    first_name = models.CharField(max_length=255, default='')
    last_name = models.CharField(max_length=255, default='')
    profile_picture = models.FileField(blank=True, null=True)
    wishlist = models.ForeignKey(Wishlist, on_delete=models.CASCADE, related_name='wishlist', blank=True)
    users_books = models.ManyToManyField(Book, related_name='users_books', blank=True)
    reading_lists = models.ManyToManyField(Reading_list_books, related_name='reading_lists', blank=True)

