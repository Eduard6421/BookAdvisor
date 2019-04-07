package com.cristidospra.bookadvisor.Networking

import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.Genre
import com.cristidospra.bookadvisor.Models.Review
import com.cristidospra.bookadvisor.Models.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface BookApiInterface {

    @Multipart
    @PUT("book/")
    fun sendScannedBook(@Part image: MultipartBody.Part) : Call<Book>


    @GET("get-books/{title}")
    fun getBooks(@Path("title") title: String): Call<ArrayList<Book>>


    @PUT("update-book/{id}")
    fun updateBook(@Path("id") id: Int,
                   @Body book: Book) : Call<Unit>


    @GET("get-categories/")
    fun getGenres() : Call<ArrayList<Genre>>


    @GET("get-books-category/{tag_name}")
    fun getBooksByGenre(@Path("tag_name") genreName: String) : Call<ArrayList<Book>>


    @FormUrlEncoded
    @POST("add-book-review")
    fun addReview(@Field("") bookId: Int,
                  @Body review: Review) : Call<Unit>
}