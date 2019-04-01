package com.cristidospra.bookadvisor

import com.cristidospra.bookadvisor.Models.User

class CurrentUser(

        var authToken: String = "",

        var email: String = ""

) : User() {
}