package com.boukharist.moviedb.view.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.boukharist.moviedb.R
import com.boukharist.moviedb.view.ErrorState
import com.boukharist.moviedb.view.LoadedState
import com.boukharist.moviedb.view.LoadingState
import com.boukharist.moviedb.view.detail.DetailActivity
import com.boukharist.moviedb.view.main.list.MovieListItem
import com.boukharist.moviedb.view.main.list.MoviesAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.state_view_layout.*
import org.koin.android.architecture.ext.viewModel
import java.io.IOException

class MainActivity : AppCompatActivity() {

    /*
     ************************************************************************************************
     ** Fields
     ************************************************************************************************
     */
    private var nextPage = 1
    private val viewModel by viewModel<MainViewModel>()
    private lateinit var adapter: MoviesAdapter


    /*
     ************************************************************************************************
     ** LifeCycle Methods
     ************************************************************************************************
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecycler()

        //observe viewModel
        observeData()

        //get movies
        getMovies()
    }


    /*
     ************************************************************************************************
     ** Private Methods
     ************************************************************************************************
     */
    @Suppress("UNCHECKED_CAST")
    private fun observeData() {
        viewModel.states.observe(this, Observer { state ->
            when (state) {
                is ErrorState -> onDataError(state.error)
                LoadingState -> onDataLoading()
                is LoadedState<*> -> onDataLoaded(state.data as List<MovieListItem>)
            }
        })
    }

    private fun onDataLoaded(movies: List<MovieListItem>) {
        //stop loading animation
        swipeRefresh.visibility = View.VISIBLE
        swipeRefresh.isRefreshing = false
        when {
            nextPage == 1 -> {  //first time or refresh
                adapter.setNewData(movies)
                adapter.setEnableLoadMore(true)
            }
            //loadMode data loaded
            movies.isNotEmpty() -> adapter.addData(movies)
            //there is no more data left
            else -> adapter.loadMoreEnd()
        }
        //increase page count
        nextPage++
        //notify load more ended
        adapter.loadMoreComplete()
    }

    private fun onDataLoading() {
        swipeRefresh.isRefreshing = true
    }

    private fun onDataError(error: Throwable) {
        @StringRes val message: String
        @DrawableRes val image: Int

        when (error) {
            is IOException -> {
                image = R.drawable.ic_network_off
                message = getString(R.string.network_exception_message)
            }
            else -> {
                image = R.drawable.ic_error
                message = "${getString(R.string.unknown_error)}  ${error.message}"
            }
        }

        swipeRefresh.visibility = View.GONE
        stateLayout.visibility = View.VISIBLE
        stateContentTextView.text = message
        stateImageView.setImageResource(image)
    }

    private fun getMovies() {
        viewModel.getAllMovies(nextPage)
    }

    private fun loadMore() {
        getMovies()
    }

    private fun refresh() {
        //reset pagination
        nextPage = 1
        adapter.setEnableLoadMore(false)
        getMovies()
    }

    private fun setupRecycler() {
        swipeRefresh.setOnRefreshListener { refresh() }
        recycler.layoutManager = GridLayoutManager(this, 2)
        setupAdapter()
        recycler.adapter = adapter
    }

    private fun setupAdapter() {
        adapter = MoviesAdapter().apply {
            setHasStableIds(true)
            setOnLoadMoreListener({ loadMore() }, recycler)
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
            setPreLoadNumber(1)
            setOnItemClickListener { adapter, _, position ->
                val item = adapter.getItem(position) as MovieListItem
                startActivity(DetailActivity.newIntent(this@MainActivity, item.id))
            }
        }
    }
}
