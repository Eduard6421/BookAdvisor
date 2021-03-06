package com.cristidospra.bookadvisor.Networking

import android.graphics.Bitmap
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.Genre
import com.cristidospra.bookadvisor.Models.Review
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

object BookApiManager {

    fun sendScannedBook(photo: Bitmap, onSuccess: (ArrayList<Book>) -> Unit) {

        var books: ArrayList<Book>

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val stream = ByteArrayOutputStream()

        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        val cover: RequestBody = RequestBody.create(MediaType.parse("image/*"), stream.toByteArray())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("cover", "image_cover", cover)

        val call: Call<ArrayList<Book>> = bookApiInterface.sendScannedBook(body)

        call.enqueue(object : Callback<ArrayList<Book>> {

            override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {

                t.printStackTrace()
            }

            override fun onResponse(call: Call<ArrayList<Book>>, response: Response<ArrayList<Book>>) {

                books = response.body() ?: return
                onSuccess(books)
            }

        })
    }

    fun getBook(id: Int, onSuccess: (Book) -> Unit) {

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val call: Call<Book> = bookApiInterface.getBook(id)

        call.enqueue(object : Callback<Book> {

            override fun onFailure(call: Call<Book>, t: Throwable) {

                t.printStackTrace()
            }

            override fun onResponse(call: Call<Book>, response: Response<Book>) {

                response.body()?.let { onSuccess(it) }
            }

        })
    }


     fun getBooks(title: String, onSuccess: (ArrayList<Book>) -> Unit) {

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val call: Call<ArrayList<Book>> = bookApiInterface.getBooks(title)

        call.enqueue(object : Callback<ArrayList<Book>> {

            override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {

                t.printStackTrace()
            }

            override fun onResponse(call: Call<ArrayList<Book>>, response: Response<ArrayList<Book>>) {

                response.body()?.let { onSuccess(it) }
            }

        })
     }

    fun getBooksFromSearch(searchString: String, onSuccess: (ArrayList<Book>) -> Unit) {

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val call: Call<ArrayList<Book>> = bookApiInterface.getBooksFromSearch(searchString)

        call.enqueue(object : Callback<ArrayList<Book>> {

            override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {

                t.printStackTrace()
            }

            override fun onResponse(call: Call<ArrayList<Book>>, response: Response<ArrayList<Book>>) {

                response.body()?.let { onSuccess(it) }
            }

        })
    }

     fun updateBook(book: Book) {

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val call: Call<Unit> = bookApiInterface.updateBook(book.id, book)

        call.enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {

                t.printStackTrace()
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

                t.printStackTrace()
            }

            override fun onResponse(call: Call<ArrayList<Genre>>, response: Response<ArrayList<Genre>>) {

                response.body()?.let { onSuccess(it) }
            }

        })
    }

     fun getBooksByGenre(genre: Genre, onSuccess: (ArrayList<Book>) -> Unit) {

        val bookApiInterface: BookApiInterface = ApiClient.getClient()?.create(BookApiInterface::class.java)!!

        val call: Call<ArrayList<Book>> = bookApiInterface.getBooksByGenre(genre.name)

        call.enqueue(object : Callback<ArrayList<Book>> {

            override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {

                t.printStackTrace()
            }

            override fun onResponse(call: Call<ArrayList<Book>>, response: Response<ArrayList<Book>>) {
                print("")
                response.body()?.let { onSuccess(it) }
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