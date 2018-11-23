package gookios.com.movie_search.ui

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import gookios.com.movie_search.R
import gookios.com.movie_search.network.response.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter(var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listener: OnItemClickListener? = null
    private var inflater: LayoutInflater? = LayoutInflater.from(context)
    var items: ArrayList<Movie> = ArrayList()
    var isLoading: Boolean = false

    companion object {
        const val TYPE_ITEM: Int = 1
        const val TYPE_FOOTER: Int = 0
    }

    fun setListener(onItemClickListener: OnItemClickListener) {
        this.listener = onItemClickListener
    }

    fun addItems(result: ArrayList<Movie>, isAppend: Boolean) {
        try {
            if (!isAppend) {
                items.clear()
            } else {
                removeFooter()
            }
            items.addAll(result)
            addFooter()
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Movie {
        return items[position]
    }

    override fun getItemViewType(position: Int): Int {
        val item: Movie = getItem(position)
        return if (item.itemType == 1) TYPE_ITEM else TYPE_FOOTER
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val view: View = inflater!!.inflate(R.layout.item_movie, container, false)
                MovieViewHolder(view)
            }
            TYPE_FOOTER -> {
                val view: View = inflater!!.inflate(R.layout.layout_footer_, container, false)
                MovieHeaderViewHodler(view)
            }
            else -> {
                val view: View = inflater!!.inflate(R.layout.item_movie, container, false)
                MovieViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == items.size - 2) {
            listener!!.onLoadMore()
        }

        val item: Movie = getItem(position)
        if (holder is MovieViewHolder) {
            val movieHolder: MovieViewHolder = holder
            movieHolder.title.text = item.title
            movieHolder.type.text = item.type
            movieHolder.year.text = item.year
            Glide.with(context).load(item.poster).into(holder.poster)
            movieHolder.itemView.setOnClickListener {
                listener!!.onItemClick(it, position)
            }
        }
    }

    // handling for footer view type

    fun addFooter() {
        isLoading = true
        items.add(Movie(itemType = 0))
        notifyItemChanged(items.size - 1)
    }

    fun removeFooter() {
        isLoading = false
        items.removeAt(items.size - 1)
        notifyItemRemoved(items.size)
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster = itemView.ivPoster
        val title = itemView.tvTitle
        val type = itemView.tvType
        val year = itemView.tvYear
        val detail = itemView.tvDetail
    }

    inner class MovieHeaderViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
        fun onLoadMore()
    }
}