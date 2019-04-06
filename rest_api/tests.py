# from django.test import TestCase

from random import randint
from .models import *

# Create your tests here.

POPULATE_DB = True

if POPULATE_DB:

    # Users and profiles test case
    find_user = User.objects.filter(username='eduard_poesina@yahoo.com')
    if not find_user:
        user1 = User(username='eduard_poesina@yahoo.com', first_name='eduard', last_name='poesina',
                     email='eduard_poesina@yahoo.com')
        user1.set_password('eduard')
        user1.save()
        profile_user1 = Profile(user=user1)
        profile_user1.save()
    else:
        user1 = find_user.first()

    find_user = User.objects.filter(username='cristi_dospa@yahoo.com')
    if not find_user:
        user2 = User(username='cristi_dospa@yahoo.com', first_name='cristi', last_name='dospra',
                     email='cristi_dospa@yahoo.com')
        user2.set_password('cristi')
        user2.save()
        profile_user2 = Profile(user=user2)
        profile_user2.save()
    else:
        user2 = find_user.first()

    find_user = User.objects.filter(username='madalin_nitu@yahoo.com')
    if not find_user:
        user3 = User(username='madalin_nitu@yahoo.com', first_name='madalin', last_name='nitu',
                     email='madalin_nitu@yahoo.com')
        user3.set_password('madalin')
        user3.save()
        profile_user3 = Profile(user=user3)
        profile_user3.save()
    else:
        user3 = find_user.first()

    authors_list = ['Chinua Achebe', 'Hans Christian Andersen', 'Dante Alighieri', 'Jane Austen', 'Honoré de Balzac', 'Samuel Beckett', 'Giovanni Boccaccio']
    titles_list = [['Things Fall Apart'],
                   ['Fairy tales'],
                   ['The Divine Comedy'],
                   ['Pride and Prejudice'],
                   ['Le Père Goriot'],
                   ['Molloy', 'Malone Dies', 'The Unnamable', 'the trilogy'],
                   ['The Decameron']]

    reviews_content = ['Good book', 'Bad book', 'Try better next time', 'luati la muie cu toate aceste date de test']
    comments_content = ['Good in free time', 'Well', 'Something that must be read by everyone', 'Try another book']
    tags = ['Classic', 'Fairy tail', 'Drama', 'Unknown']

    for i in range(0, len(authors_list)):
        print(authors_list[i])
        author = Author(name=authors_list[i])
        author.save()
        for title in titles_list[i]:
            print((' ' * 4) + title)
            book = Book(title=title)
            book.save()

            book.author.add(author)

    for tag_content in tags:
        tag = Tag(name=tag_content)
        tag.save()

    for i in range(0, len(reviews_content)):
        user_id = randint(1, 3)
        user = None
        if user_id == 1:
            user = user1
        elif user_id == 2:
            user = user2
        elif user_id == 3:
            user = user3
        review = Review(content=reviews_content[i], score=randint(0, 9), user_review=user)
        review.save()

        comment = Comment(content=comments_content[i], user_comment=user)
        comment.save()

        # Add review and a tag to a random book
        books = Book.objects.all()
        tags = Tag.objects.all()
        random_book = randint(0, len(books) - 1)
        random_tag = randint(0, len(tags) - 1)

        books[random_book].reviews.add(review)
        books[random_book].books_tags.add(tags[random_tag])
        books[random_book].comments.add(comment)

        books[random_book].save()

    books = Book.objects.all()
    tags = Tag.objects.all()

    reading_list = Reading_list_books(title='Ceva titlu de readlist')
    reading_list.save()
    reading_list.books.add(books[1])
    reading_list.books.add(books[4])
    reading_list.tags.add(tags[1])
    reading_list.tags.add(tags[2])

    profile_user1 = Profile.objects.get(user=user1)
    profile_user2 = Profile.objects.get(user=user2)
    profile_user3 = Profile.objects.get(user=user3)

    profile_user1.reading_lists.add(reading_list)

    profile_user1.save()
    profile_user2.save()
    profile_user3.save()

    msg1 = Message(content='Ce faci ma pula?', user_sender=user1, user_receiver=user2)
    msg1.save()

    msg2 = Message(content='Uite bine, tu ce pula mea faci?', user_sender=user2, user_receiver=user1)
    msg2.save()

    msg3 = Message(content='Ba bulangiule da-mi atentie', user_sender=user3, user_receiver=user1)
    msg3.save()



