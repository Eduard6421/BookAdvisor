package com.cristidospra.bookadvisor.Networking

import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.Genre
import com.cristidospra.bookadvisor.Models.Review
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object BookApiManager {

    //     fun sendScannedBook() : Book {
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


     fun getBooks(title: String, onSuccess: (ArrayList<Book>) -> Unit) {

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


     fun updateBook(book: Book) {

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

     fun getCategories(onSuccess: (ArrayList<Genre>) -> Unit) {

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

     fun getBooksByGenre(genre: Genre, onSuccess: (ArrayList<Book>) -> Unit) {

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val call: Call<ArrayList<Book>> = bookApiInterface.getBooksByGenre(genre.name)

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

    fun addReview(book: Book, review: Review) {

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val call: Call<Unit> = bookApiInterface.addReview(book.id, review)

        call.enqueue(object : Callback<Unit> {

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                print("")
            }

        })
    }
}