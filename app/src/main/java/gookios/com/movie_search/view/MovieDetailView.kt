package gookios.com.movie_search.view

import gookios.com.movie_search.network.response.MovieDetailResponse

interface MovieDetailView {

    interface View: BaseView {
        fun onSuccess(result: MovieDetailResponse)
        fun onError(message: String)
    }

    interface Presenter: BasePresenter {
        fun getMovieDetail(imdbId: String)
    }
}