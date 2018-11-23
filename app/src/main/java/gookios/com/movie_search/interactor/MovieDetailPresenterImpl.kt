package gookios.com.movie_search.interactor

import gookios.com.movie_search.Application
import gookios.com.movie_search.network.ApiConstant
import gookios.com.movie_search.network.ApiInterface
import gookios.com.movie_search.view.MovieDetailView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MovieDetailPresenterImpl : MovieDetailView.Presenter {
    var view: MovieDetailView.View
    var service: ApiInterface

    @NonNull
    var disposable: Disposable? = null

    constructor(view: MovieDetailView.View) {
        this.view = view
        this.service = Application.instance.apiService
    }

    override fun getMovieDetail(imdbId: String) {
        view.showProgressBar()
        if (view.isConnected()) {
            disposable = service.getMovieDetail(ApiConstant.APIKEY, imdbId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        view.hideProgressBar()
                        if (it.isSuccessful) {
                            view.onSuccess(it.body()!!)
                        } else {
                            view.onError("Oops..\nSomething when wrong, please refresh page..")
                        }
                    }, {
                        view.hideProgressBar()
                        view.onError("Oops..\nSomething when wrong, please refresh page..")
                    })
        }
    }

    override fun onDestroy() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}