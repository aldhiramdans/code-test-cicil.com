package gookios.com.movie_search.network.response

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
        @SerializedName("Title") val title: String,
        @SerializedName("Year") val year: String,
        @SerializedName("Rated") val rated: String,
        @SerializedName("Released") val reelased: String,
        @SerializedName("Runtime") val runtime: String,
        @SerializedName("Genre") val genre: String,
        @SerializedName("Director") val director: String,
        @SerializedName("Writer") val writer: String,
        @SerializedName("Actors") val actors: String,
        @SerializedName("Plot") val plot: String,
        @SerializedName("Language") val language: String,
        @SerializedName("Country") val country: String,
        @SerializedName("Awards") val awards: String,
        @SerializedName("Poster") val poster: String,
        @SerializedName("Ratings") val ratings: ArrayList<Ratings>,
        @SerializedName("Metascore") val metascore: String,
        @SerializedName("imdbRating") val imdbRating: String,
        @SerializedName("imdbVotes") val imdbVotes: String,
        @SerializedName("imdbID") val imdbId: String,
        @SerializedName("Type") val type: String,
        @SerializedName("DVD") val dvd: String,
        @SerializedName("BoxOffice") val boxOffice: String,
        @SerializedName("Production") val production: String,
        @SerializedName("Website") val website: String,
        @SerializedName("Response") val response: String
)