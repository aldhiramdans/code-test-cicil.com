package gookios.com.movie_search.interactor

import gookios.com.movie_search.Application
import gookios.com.movie_search.network.ApiConstant
import gookios.com.movie_search.network.ApiInterface
import gookios.com.movie_search.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.annotations.NotNull

class MainPresenterImpl : MainView.Presenter {
    var mainView: MainView.View
    var service: ApiInterface

    @NotNull
    var disposable: Disposable? = null

    constructor(mainView: MainView.View) {
        this.mainView = mainView
        this.service = Application.instance.apiService
    }

    override fun loadData(movieTitle: String, page: String) {
        mainView.showProgressBar()
        if (mainView.isConnected()) {
            disposable = service.getMovies(ApiConstant.APIKEY, movieTitle, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mainView.hideProgressBar()
                        if (it.isSuccessful) {
                            mainView.onSuccess(it.body()!!, false)
                        } else {
                            mainView.onError("Oops.. Something when wrong. \n Please try again..")
                        }
                    }, {
                        mainView.hideProgressBar()
                        mainView.onError("Oops.. Something when wrong. \n Please try again..")
                    })
        } else {
            mainView.hideProgressBar()
            mainView.onError("Please check your internet connection..")
        }
    }

    override fun loadMore(movieTitle: String, page: String) {
        if (mainView.isConnected()) {
            disposable = service.getMovies(ApiConstant.APIKEY, movieTitle, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.isSuccessful) {
                            mainView.onSuccess(it.body()!!, true)
                        } else {
                            mainView.onError("Oops.. Something when wrong. \n Please try again..")
                        }
                    }, {
                        mainView.onError("Oops.. Something when wrong. \n Please try again..")
                    })
        } else {
            mainView.onError("Please check your internet connection..")
        }
    }

    override fun onDestroy() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}