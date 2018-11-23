package gookios.com.movie_search.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import gookios.com.movie_search.network.ApiConstant.Companion.BASEURL
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private var retrofit: Retrofit? = null

        fun getClientService(): Retrofit {
            val gson: Gson = GsonBuilder()
                    .setLenient()
                    .create()
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            clientBuilder.addInterceptor(loggingInterceptor)

            retrofit = Retrofit.Builder().baseUrl(BASEURL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(clientBuilder.build())
                    .build()
            return retrofit!!
        }
    }
}