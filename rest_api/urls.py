
from django.conf.urls import url
from .views import *
from django.conf.urls import (
handler400, handler403, handler404, handler500
)

urlpatterns = [

    url(r'^login$', login_user, name='login'),
    url(r'^logout$', logout_user, name='logout'),
    url(r'^register$', register_user, name='register'),

    url(r'^get-user/(?P<email>[a-zA-Z0-9.!#$%&\'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.'
        r'[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*)/$', get_user, name='get_user'),
    url(r'^recommended-books/$', recommended_books, name='recommended_books'),
    url(r'^update-user/(?P<email>[a-zA-Z0-9.!#$%&\'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.'
        r'[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*)$', update_user, name='update_user'),

    url(r'^get-followers/(?P<email>[a-zA-Z0-9.!#$%&\'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])'
        r'?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*)/$', get_followers, name='get_followers'),
    url(r'^follow/$', follow, name='follow'),
    url(r'^book/$', book, name='book'),

    url(r'^get-books/(?P<title>.+)$', get_books, name='get_books'),
    url(r'^add-reading-list/$', add_reading_list, name='add_reading_list'),
    url(r'^update-reading-list/(?P<reading_list_name>.+)/$', update_reading_list, name='update_reading_list'),

    url(r'^users/$', users, name='users'),
    url(r'^update-book/(?P<title_book>.+)$', update_book, name='update_book'),
    url(r'^get-categories/$', get_categories, name='get_categories'),

    url(r'^get-books-category/(?P<tag_name>.+)$', get_books_category, name='get_books_category'),

    url(r'^get-conversations/(?P<email>[a-zA-Z0-9.!#$%&\'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])'
        r'?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*)/$', get_conversations, name='get_conversations'),
    url(r'^get-conversations/$', get_all_conversations, name='get_all_conversations'),

    url(r'^send-message/$', send_message, name='send_message'),



    url(r'^current-user$', current_user, name='current_user'),
]



