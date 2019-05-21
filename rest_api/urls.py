
from django.conf.urls import url


from .views import identity_views
from .views import book_views
from .views import readinglist_views
from .views import chat_views


from django.conf.urls import (
handler400, handler403, handler404, handler500
)

urlpatterns = [

    url(r'\$', identity_views.index, name='index'),
    url(r'^login$',identity_views.login_user, name='login'),
    url(r'^logout$', identity_views.logout_user, name='logout'),
    url(r'^register$', identity_views.register_user, name='register'),
    url(r'^get-user/(?P<email>[a-zA-Z0-9.!#$%&\'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.'
        r'[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*)/$', identity_views.get_user, name='get_user'),
    url(r'^update-user/$', identity_views.update_user, name='update_user'),
    url(r'^get-user-by-firebase/(?P<firebaseUID>.+)$', identity_views.get_user_by_firebase, name='get_user_by_firebase'),
    url(r'^get-followers/$', identity_views.get_followers, name='get_followers'),
    url(r'^get-following/$', identity_views.get_following, name='get_following'),
    url(r'^get-users-notfollowing/$', identity_views.get_users_notfollowing, name='get_users_notfollowing'),
    url(r'^follow$', identity_views.follow, name='follow'),
    url(r'^update-user-pic/$', identity_views.update_user_pic, name='update_user_pic'),
    url(r'^find-user/(?P<term_filter>.+)$', identity_views.find_user, name='find_user'),
    url(r'^/data/BookAdvisor/media/(?P<uuid_img>.+)$', identity_views.get_profil_img, name='get_profil_img'),
    url(r'^find-new-peopl   e$', identity_views.find_new_people, name='find_new_people'),
    url(r'^current-user$', identity_views.current_user, name='current_user'),
    url(r'^users/$', identity_views.users, name='users'),    


    url(r'^recommended-books/$', book_views.recommended_books, name='recommended_books'),
    url(r'^book/$', book_views.book, name='book'),
    url(r'^get-book/(\d+)$', book_views.get_book, name='get_book'),
    url(r'^get-books$', book_views.get_all_books, name='get_all_books'),
    url(r'^get-books/(?P<title>.+)$', book_views.get_books, name='get_books'),
    url(r'^get-books-category/(?P<tag_name>.+)$', book_views.get_books_category, name='get_books_category'),
    url(r'^update-book/(\d+)$', book_views.update_book, name='update_book'),
    url(r'^get-categories/$', book_views.get_categories, name='get_categories'),
    url(r'^get-reviews/(?P<title>.+)$', book_views.get_reviews, name='get_reviews'),
    url(r'^add-review/(\d+)$', book_views.add_review, name='add_review'),
    url(r'^get-filter-books/(?P<term_filter>.+)$', book_views.get_filter_books, name='get_filter_books'),
    url(r'^images/(?P<uuid_img>.+)$', book_views.get_book_cover, name='get_book_cover'),


    url(r'^get-reading-list$', readinglist_views.get_reading_list, name='get_reading_list'),
    url(r'^add-reading-list/$', readinglist_views.add_reading_list, name='add_reading_list'),
    url(r'^update-reading-list/(?P<reading_list_name>.+)$', readinglist_views.update_reading_list, name='update_reading_list'),


    url(r'^get-conversations/(?P<email>[a-zA-Z0-9.!#$%&\'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])'
        r'?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*)/$', chat_views.get_conversations, name='get_conversations'),
    url(r'^get-conversations/$', chat_views.get_all_conversations, name='get_all_conversations'),
    url(r'^send-message/$', chat_views.send_message, name='send_message'),

    ]



