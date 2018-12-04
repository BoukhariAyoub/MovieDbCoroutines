package com.boukharist.moviedb.view.detail

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.boukharist.moviedb.R
import com.boukharist.moviedb.util.extra
import com.boukharist.moviedb.util.getTag
import com.boukharist.moviedb.view.ErrorState
import com.boukharist.moviedb.view.LoadedState
import com.boukharist.moviedb.view.LoadingState
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_view_layout.*
import kotlinx.android.synthetic.main.state_view_layout.*
import org.koin.android.architecture.ext.viewModel
import java.io.IOException


class DetailActivity : AppCompatActivity() {

    private val viewModel by viewModel<DetailViewModel>()
    private val movieId by extra<String>(EXTRA_MOVIE_ID)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setupToolbar()

        //observe viewModel
        viewModel.states.observe(this, Observer { state ->
            when (state) {
                LoadingState -> showLoading()
                is ErrorState -> showError(state.error)
                is LoadedState<*> -> showDetail(state.data as MovieDetailItem)
            }
        })

        //load data
        viewModel.getMovie(movieId)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        collapsingToolbarLayout.also {
            it.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))
            it.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white))
        }


        appBarLayout.setExpanded(false)
    }

    private fun showDetail(item: MovieDetailItem) {
        Log.i(getTag(), "$item")

        stateLayout.visibility = GONE
        stateContentTextView.clearAnimation()

        detailViewLayout.visibility = VISIBLE
        appBarLayout.setExpanded(true, true)

        //set toolbar title
        collapsingToolbarLayout.title = item.title
        toolbar.title = item.title

        //set title
        titleView.text = item.title

        //set backdrop image
        Glide.with(this)
                .load(item.backdropPath)
                .apply(RequestOptions().centerCrop())
                .into(backdropImage)

        //set poster image
        Glide.with(this)
                .load(item.posterPath)
                .apply(RequestOptions().centerCrop())
                .into(posterImageView)

        //set tagLine
        taglineView.text = item.tagLine
        //set overview
        overviewView.text = item.overview
        //set rating
        ratingView.text = getString(R.string.rating_placeholder, item.rating)
    }

    private fun showLoading() {
        //show state layout
        stateLayout.visibility = View.VISIBLE
        stateContentTextView.setText(R.string.loading_text)
        stateImageView.setImageResource(R.drawable.ic_loading)

        //blink animation
        with(AlphaAnimation(0.0f, 1.0f)) {
            duration = 500
            startOffset = 100
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
            stateContentTextView.startAnimation(this)
        }
    }

    private fun showError(error: Throwable) {
        @StringRes val message: Int
        @DrawableRes val image: Int

        when (error) {
            is IOException -> {
                image = R.drawable.ic_network_off
                message = R.string.network_exception_message
            }
            else -> {
                image = R.drawable.ic_error
                message = R.string.unknown_error
            }
        }

        stateLayout.visibility = View.VISIBLE
        stateContentTextView.setText(message)
        stateImageView.setImageResource(image)
        stateContentTextView.clearAnimation()
    }

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        fun newIntent(context: Context, movieId: String): Intent {
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_ID, movieId)
            }
        }
    }
}
