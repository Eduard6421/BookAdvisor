package com.cristidospra.bookadvisor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.cristidospra.bookadvisor.Models.*
import com.cristidospra.bookadvisor.Networking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity: AppCompatActivity() {

    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.main_button)

        var kkt: ArrayList<User>

        updateBook(Book(id = 1))
        updateReadingList(ReadingList(title = "test"))
    }

    private fun login(email: String, password: String) {

        //Send request to server to get vacations for the current month
        val loginApiInterface: LoginApiInterface = ApiClient.getClient()?.create(LoginApiInterface::class.java)!!

        val call: Call<AuthToken> = loginApiInterface.getAuthToken(email, password)

        call.enqueue(object : Callback<AuthToken> {

            override fun onFailure(call: Call<AuthToken>, t: Throwable) {

                print("")
            }

            override fun onResponse(call: Call<AuthToken>, response: Response<AuthToken>) {

                print("")
            }

        })
    }

    private fun register(email: String, password: String) {

        //Send request to server to get vacations for the current month
        val loginApiInterface: LoginApiInterface = ApiClient.getClient()?.create(LoginApiInterface::class.java)!!

        val call: Call<String> = loginApiInterface.register(email, password)

        call.enqueue(object : Callback<String> {

            override fun onFailure(call: Call<String>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {

                print("")
            }

        })
    }

    private fun logOut() {

        val loginApiInterface: LoginApiInterface = ApiClient.getClient()?.create(LoginApiInterface::class.java)!!

        val call: Call<Unit> = loginApiInterface.logout()

        call.enqueue(object : Callback<Unit> {

            override fun onFailure(call: Call<Unit>, t: Throwable) {
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                print("")
            }

        })

    }

    /*----------------------------------------------------------------------------------------------------------------*/


//    private fun sendScannedBook() : Book {
//
//        var book: Book
//
//        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!
//
//        val cover: RequestBody = RequestBody.create(MediaType.parse("image/*"), Utils.getSignature(context))
//        val body: MultipartBody.Part = MultipartBody.Part.createFormData("cover", Utils.getSignature(context).name, signature)
//
//        val call: Call<Book> = bookApiInterface.sendScannedBook()
//
//        call.enqueue(object : Callback<Book> {
//            override fun onFailure(call: Call<Book>, t: Throwable) {
//            }
//
//            override fun onResponse(call: Call<Book>, response: Response<Book>) {
//
//                book = response.body()!!
//            }
//
//        })
//
//        return book
//    }


    private fun getBooks(title: String, onSuccess: (ArrayList<Book>) -> Unit) {

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val call: Call<ArrayList<Book>> = bookApiInterface.getBooks(title)

        call.enqueue(object : Callback<ArrayList<Book>> {

            override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<ArrayList<Book>>, response: Response<ArrayList<Book>>) {
                print("")
                onSuccess(response.body()!!)
            }

        })
    }


    private fun updateBook(book: Book) {

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val call: Call<Unit> = bookApiInterface.updateBook(book.id, book)

        call.enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                print("")
            }

        })
    }

    private fun getCategories(onSuccess: (ArrayList<Genre>) -> Unit) {

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val call: Call<ArrayList<Genre>> = bookApiInterface.getGenres()

        call.enqueue(object : Callback<ArrayList<Genre>> {

            override fun onFailure(call: Call<ArrayList<Genre>>, t: Throwable) {

                print("")
            }

            override fun onResponse(call: Call<ArrayList<Genre>>, response: Response<ArrayList<Genre>>) {
                print("")
                onSuccess(response.body()!!)
            }

        })
    }

    private fun getBooksByGenre(genre: Genre, onSucces: (ArrayList<Book>) -> Unit) {

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val call: Call<ArrayList<Book>> = bookApiInterface.getBooksByGenre(genre.name)

        call.enqueue(object : Callback<ArrayList<Book>> {

            override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<ArrayList<Book>>, response: Response<ArrayList<Book>>) {
                print("")
                onSucces(response.body()!!)
            }
        })

    }

    /*-----------------------------------------------------------------------------------------------------------------*/

    private fun getUser(email: String, onSucces: (User) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<User> = userApiInterface.getUser(email)

        call.enqueue(object : Callback<User> {

            override fun onFailure(call: Call<User>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                print("")
                onSucces(response.body()!!)
            }

        })
    }

    private fun getRecommendedBooks(onSucces: (ArrayList<Book>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<Book>> = userApiInterface.getRecommended()

        call.enqueue(object : Callback<ArrayList<Book>> {

            override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<Book>>, response: Response<ArrayList<Book>>) {
                print("")
                onSucces(response.body()!!)
            }

        })

    }

    private fun updateUser(user: User) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Unit> = userApiInterface.updateUser(user)

        call.enqueue(object : Callback<Unit> {

            // Failed request
            override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                print("")
            }

            // Successful request
            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                print("")
            }

        })
    }

    private fun getFollowers(onSucces: (ArrayList<User>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<User>> = userApiInterface.getFollowers()

        call.enqueue(object : Callback<ArrayList<User>> {

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                print("")
                onSucces(response.body()!!)
            }

        })

    }

    private fun getFollowing(onSucces: (ArrayList<User>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<User>> = userApiInterface.getFollowing()

        call.enqueue(object : Callback<ArrayList<User>> {

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                print("")
                onSucces(response.body()!!)
            }

        })

    }


    private fun follow(userToFollow: User) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Unit> = userApiInterface.follow(userToFollow.email)

        call.enqueue(object : Callback<Unit> {

            // Failed request
            override fun onFailure(call: Call<Unit>?, t: Throwable?) {
            }

            // Successful request
            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                print("")
            }

        })
    }

    private fun follow(email: String) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Unit> = userApiInterface.follow(email)

        call.enqueue(object : Callback<Unit> {

            // Failed request
            override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                print("")
            }

            // Successful request
            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                print("")
            }

        })
    }


    private fun addReadingList(readingList: ReadingList) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Unit> = userApiInterface.addReadingList(readingList)

        call.enqueue(object : Callback<Unit> {

            // Failed request
            override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                print("")
            }

            // Successful request
            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                print("")
            }

        })
    }

    private fun updateReadingList(readingList: ReadingList) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Unit> = userApiInterface.updateReadingList(readingList.title, readingList)

        call.enqueue(object : Callback<Unit> {

            // Failed request
            override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                print("")
            }

            // Successful request
            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                print("")
            }

        })
    }

    private fun getUsers(onSucces: (ArrayList<User>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<User>> = userApiInterface.getUsers()

        call.enqueue(object : Callback<ArrayList<User>> {

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                print("")
                onSucces(response.body()!!)
            }

        })

    }

    private fun getConversations(onSucces: (ArrayList<Conversation>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<Conversation>> = userApiInterface.getConversations()

        call.enqueue(object : Callback<ArrayList<Conversation>> {

            override fun onFailure(call: Call<ArrayList<Conversation>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<Conversation>>, response: Response<ArrayList<Conversation>>) {
                print("")
                onSucces(response.body()!!)
            }
        })

    }

    private fun getConversation(user: User, onSucces: (Conversation) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Conversation> = userApiInterface.getConversation(user.email)

        call.enqueue(object : Callback<Conversation> {

            override fun onFailure(call: Call<Conversation>, t: Throwable) {

            }

            override fun onResponse(call: Call<Conversation>, response: Response<Conversation>) {
                print("")
                onSucces(response.body()!!)
            }

        })
    }
}
