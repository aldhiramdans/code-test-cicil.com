package gookios.com.movie_search

import android.app.Application
import android.content.Context
import gookios.com.movie_search.network.ApiClient
import gookios.com.movie_search.network.ApiInterface

class Application : Application() {
    private lateinit var context: Context
    var apiService: ApiInterface = ApiClient.getClientService().create(ApiInterface::class.java)

    companion object {
        @JvmStatic
        var application: gookios.com.movie_search.Application? = null
        val instance: gookios.com.movie_search.Application
            get() {
                if (application == null) {
                    application = gookios.com.movie_search.Application()
                }
                return application as gookios.com.movie_search.Application
            }
    }

    override fun onCreate() {
        super.onCreate()
        context = baseContext
    }
}