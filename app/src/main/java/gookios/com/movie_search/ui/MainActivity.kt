package gookios.com.movie_search.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import gookios.com.movie_search.R
import gookios.com.movie_search.interactor.MainPresenterImpl
import gookios.com.movie_search.network.NetworkConnection
import gookios.com.movie_search.network.response.MovieResponse
import gookios.com.movie_search.util.AnimationUtil.circleReveal
import gookios.com.movie_search.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar_search.*
import org.jetbrains.anko.alert

class MainActivity : AppCompatActivity(), MainView.View, SwipeRefreshLayout.OnRefreshListener {
    private var search_menu: Menu? = null
    private var item_search: MenuItem? = null
    private var presenterImpl: MainPresenterImpl? = null
    private var adapter: MovieAdapter? = null

    private lateinit var movieTitle: String
    private var page: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenterImpl = MainPresenterImpl(this)
        adapter = MovieAdapter(this)
        initView()
        adapter!!.setListener(object : MovieAdapter.OnItemClickListener {


            override fun onItemClick(v: View, position: Int) {
                val item = adapter!!.getItem(position)
                MovieDetailActivity.navigate(this@MainActivity, item.imdbId!!)
            }

            override fun onLoadMore() {
                presenterImpl!!.loadMore(movieTitle, page++.toString())
            }

        })
        setSupportActionBar(toolbar)
        setSearchtollbar()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenterImpl!!.onDestroy()
    }

    override fun onBackPressed() {
        if (searchtoolbar!!.visibility == VISIBLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                circleReveal(this@MainActivity, R.id.searchtoolbar, 1, true, false)
            else
                searchtoolbar!!.visibility = View.GONE
        } else {
            alert("Leave from this app?") {
                okButton { finish() }
                cancelButton { dismiss() }
            }.show()
        }
    }

    private fun initView() {
        swipeRefresh.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = this.adapter

        searchIconPanel.visibility = VISIBLE
        searchTitle.text = "Find your movies here.. :D"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(this@MainActivity, R.id.searchtoolbar, 1, true, true)
                else
                    searchtoolbar.visibility = View.VISIBLE
                item_search!!.expandActionView()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getData(title: String, page: String) {
        presenterImpl!!.loadData(title, page)
    }

    override fun onRefresh() {
        try {
            if (adapter!!.itemCount > 0) {
                hideProgressBar()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSuccess(result: MovieResponse, isAppend: Boolean) {
        if (result.results.size > 0) {
            searchIconPanel.visibility = GONE
            adapter!!.addItems(result.results, isAppend)
        }
    }

    override fun onAppend(result: ArrayList<MovieResponse>) {
        // TODO
    }

    override fun showProgressBar() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideProgressBar() {
        swipeRefresh.isRefreshing = false
    }

    override fun isConnected(): Boolean {
        return NetworkConnection.insNetworkConnected(applicationContext)
    }

    @SuppressLint("SetTextI18n")
    override fun onError(message: String) {
        adapter!!.clear()
        searchIconPanel.visibility = VISIBLE
        searchTitle.text = "$message;("
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setSearchtollbar() {
        if (searchtoolbar != null) {
            searchtoolbar!!.inflateMenu(R.menu.menu_search)
            search_menu = searchtoolbar!!.menu

            searchtoolbar!!.setNavigationOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(this@MainActivity, R.id.searchtoolbar, 1, true, false)
                else
                    searchtoolbar!!.visibility = View.GONE
            }

            item_search = search_menu!!.findItem(R.id.action_filter_search)

            MenuItemCompat.setOnActionExpandListener(item_search!!, object : MenuItemCompat.OnActionExpandListener {
                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(this@MainActivity, R.id.searchtoolbar, 1, true, false)
                    } else
                        searchtoolbar.visibility = View.GONE
                    return true
                }

                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    // Do something when expanded
                    return true
                }
            })
            initSearchView()
        } else Log.d("toolbar", "setSearchtollbar: NULL")
    }

    private fun initSearchView() {
        val searchView = search_menu!!.findItem(R.id.action_filter_search).actionView as SearchView
        searchView.isSubmitButtonEnabled = false

        val closeButton = searchView.findViewById<View>(R.id.search_close_btn) as ImageView
        closeButton.setImageResource(R.drawable.ic_close)

        val txtSearch = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as EditText
        txtSearch.apply {
            hint = "Search.."
            setHintTextColor(Color.DKGRAY)
            setTextColor(resources.getColor(R.color.colorTextLightPrimary))
        }

        val searchTextView = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as AutoCompleteTextView
        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //callSearch(newText)
                return true
            }

            fun callSearch(query: String) {
                Log.i("query", "" + query)
                page = 1
                movieTitle = query
                getData(query, page++.toString())
            }
        })
    }

}
