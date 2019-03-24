
from django.conf.urls import url
from .views import logout_user, login_user, register_user, get_recommanded_books, get_books, get_user
from django.conf.urls import (
handler400, handler403, handler404, handler500
)

urlpatterns = [

    url(r'^login$', login_user, name='login'),
    url(r'^logout$', logout_user, name='logout'),
    url(r'^register$', register_user, name='register'),

    url(r'^get-books$', get_books, name='get_books'),
    url(r'^get-user$', get_user, name='get_user'),
    url(r'^get-recommanded-books$', get_recommanded_books, name='get_recommanded_books'),

]
