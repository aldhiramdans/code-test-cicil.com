package gookios.com.movie_search.network.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(
        @SerializedName("Search") val results: ArrayList<Movie>,
        @SerializedName("totalResults") val int: String,
        @SerializedName("Response") val response: String
)