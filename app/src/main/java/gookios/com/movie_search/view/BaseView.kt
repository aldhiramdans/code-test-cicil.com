package gookios.com.movie_search.view

interface BaseView {
    fun showProgressBar()
    fun hideProgressBar()
    fun isConnected(): Boolean
}