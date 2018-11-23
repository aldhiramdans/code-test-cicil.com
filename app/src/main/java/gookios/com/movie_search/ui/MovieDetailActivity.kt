package gookios.com.movie_search.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import gookios.com.movie_search.R
import gookios.com.movie_search.interactor.MovieDetailPresenterImpl
import gookios.com.movie_search.network.NetworkConnection
import gookios.com.movie_search.network.response.MovieDetailResponse
import gookios.com.movie_search.view.MovieDetailView
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.layout_movie_detail.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog

class MovieDetailActivity : AppCompatActivity(), MovieDetailView.View {
    private var presenter: MovieDetailPresenterImpl? = null
    private var imdbID: String? = null
    private var progressDialog: ProgressDialog? = null

    companion object {
        const val IMDB_ID: String = "imdb_id"

        fun navigate(activity: Activity, imdbId: String) {
            val intent = Intent(activity, MovieDetailActivity::class.java)
            intent.putExtra(IMDB_ID, imdbId)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        presenter = MovieDetailPresenterImpl(this)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            imdbID = bundle.getString(IMDB_ID)
        }
        progressDialog = indeterminateProgressDialog("Loading")
        toolbar.setTitleTextColor(resources.getColor(R.color.colorWhite))
        toolbar.title = ""
        setSupportActionBar(toolbar)
        presenter!!.getMovieDetail(imdbID!!)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item!!.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onDestroy()
    }

    override fun onSuccess(result: MovieDetailResponse) {
        setupView(result)
    }

    override fun onError(message: String) {
        alert("The resource you are looking for might have been removed, had its name changed, or is temporarily unavailable.") {
            title = "Ups.."
            yesButton {
                this.dismiss()
                finish()
            }
        }.show()
    }

    override fun showProgressBar() {
        progressDialog!!.show()
    }

    override fun hideProgressBar() {
        progressDialog!!.dismiss()
    }

    override fun isConnected(): Boolean {
        return NetworkConnection.insNetworkConnected(applicationContext)
    }

    private fun setupView(detail: MovieDetailResponse) {
        try {
            tvTitle.text = detail.title
            tvGenre.text = detail.genre + " - "
            tvDuration.text = detail.runtime
            tvYear.text = detail.year
            tvRating.text = detail.imdbRating
            tvPlot.text = detail.plot
            tvDirector.text = detail.director
            tvWriter.text = detail.writer
            tvActor.text = detail.actors
            Glide.with(this).load(detail.poster).into(ivPoster)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}