package gookios.com.movie_search.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Movie(
        var itemType: Int = 1, // type item : 0 typefooter

        @SerializedName("Title")
        @Expose
        var title: String? = null,
        @SerializedName("Year")
        @Expose
        var year: String? = null,
        @SerializedName("imdbID")
        @Expose
        var imdbId: String? = null,
        @SerializedName("Type")
        @Expose
        var type: String? = null,
        @SerializedName("Poster")
        @Expose
        var poster: String? = null
)