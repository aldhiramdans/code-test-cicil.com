package gookios.com.movie_search.network.response

import com.google.gson.annotations.SerializedName

data class Ratings(
        @SerializedName("Source") val source: String,
        @SerializedName("Value") val value: String
)