from django.db import models

from django.contrib.auth import get_user_model
from django.db import models
import datetime

# Create your models here.

User = get_user_model()


class Tag(models.Model):
    name = models.CharField(max_length=255, default='')


class Author(models.Model):
    name = models.CharField(max_length=255, default='')


class Review(models.Model):
    content = models.CharField(max_length=255, default='')
    score = models.FloatField(default=0)
    date = models.DateTimeField(default=datetime.datetime.now)
    user_review = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user_review', blank=True, null=True)


class Book(models.Model):
    title = models.CharField(max_length=255, default='')  # title
    description = models.CharField(max_length=255, default='')
    publish_date = models.DateTimeField(default=datetime.datetime.now)  # original_publication_year
    rating = models.FloatField(default=0.0)
    no_pages = models.IntegerField(default=0)
    #cover = models.ImageField(upload_to='images/', default='images/default_cover.png')
    cover = models.CharField(max_length=255, default='')
    authors = models.ManyToManyField(Author, related_name='authors', blank=True)
    reviews = models.ManyToManyField(Review, related_name='reviews', blank=True)
    books_tags = models.ManyToManyField(Tag, related_name='books_tags', blank=True)


class Reading_list_books(models.Model):
    title = models.CharField(max_length=255, default='')
    books = models.ManyToManyField(Book, related_name='books', blank=True)
    tags = models.ManyToManyField(Tag, related_name='tags', blank=True)
    read_status = models.CharField(max_length=255, default='nothing')


class Message(models.Model):
    content = models.CharField(max_length=255, default='')
    date = models.DateTimeField(default=datetime.datetime.now)
    user_sender = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user_sender', blank=True, null=True)
    user_receiver = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user_receiver', blank=True, null=True)


class Profile(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user', blank=True)
    profile_picture = models.CharField(max_length=255, default='')
    # profile_picture = models.FileField(blank=True, null=True)
    # users_books = models.ManyToManyField(Book, related_name='users_books', blank=True)
    reading_lists = models.ManyToManyField(Reading_list_books, related_name='reading_lists', blank=True)
    followers = models.ManyToManyField(User, related_name='followers', blank=True)
    following = models.ManyToManyField(User, related_name='following', blank=True)
    favorite_tags = models.ManyToManyField(Tag, related_name='favorite_tags', blank=True)
    firebaseUID = models.CharField(max_length=255, default='')


class FakeUserPreferences(models.Model):
    user_id = models.IntegerField(default=0)
    book_id = models.ForeignKey(Book, on_delete=models.CASCADE, related_name='book_id', blank=True)
    rating = models.IntegerField(default=0)


