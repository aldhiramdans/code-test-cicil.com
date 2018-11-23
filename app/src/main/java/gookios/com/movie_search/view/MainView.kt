package gookios.com.movie_search.view

import gookios.com.movie_search.network.response.MovieResponse

interface MainView {

    interface View : BaseView {
        fun onSuccess(result: MovieResponse, isAppend: Boolean)
        fun onAppend(result: ArrayList<MovieResponse>)
        fun onError(message: String)
    }

    interface Presenter : BasePresenter {
        fun loadData(movieTitle: String, page: String)
        fun loadMore(movieTitle: String, page: String)
    }
}