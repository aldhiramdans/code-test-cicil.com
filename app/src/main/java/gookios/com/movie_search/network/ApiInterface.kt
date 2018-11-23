package gookios.com.movie_search.network

import gookios.com.movie_search.network.response.MovieDetailResponse
import gookios.com.movie_search.network.response.MovieResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(ApiConstant.BASEURL + "?type=movie") // only show type movie
    fun getMovies(
            @Query("apikey") apikey: String,
            @Query("s") title: String,
            @Query("page") page: String): Observable<Response<MovieResponse>>

    @GET(ApiConstant.BASEURL)
    fun getMovieDetail(
            @Query("apikey") apikey: String,
            @Query("i") title: String
    ): Observable<Response<MovieDetailResponse>>
}