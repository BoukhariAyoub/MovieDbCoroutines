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
import com.boukharist.moviedb.util.NoNetworkException
import com.boukharist.moviedb.util.extra
import com.boukharist.moviedb.util.getTag
import com.boukharist.moviedb.view.ErrorState
import com.boukharist.moviedb.view.LoadingState
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_view_layout.*
import kotlinx.android.synthetic.main.state_view_layout.*
import org.koin.android.architecture.ext.viewModel


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
                is ErrorState -> showError(state.error)
                LoadingState -> showLoading()
                is DetailViewModel.LoadedState -> showDetail(state.value)
            }
        })

        //load data
        viewModel.getMovie(movieId)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsing_toolbar_layout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))
        collapsing_toolbar_layout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white))
        app_bar_layout.setExpanded(false)
    }

    private fun showDetail(item: MovieDetailItem) {
        Log.i(getTag(), "$item")

        state_layout.visibility = GONE
        state_content_text_view.clearAnimation()

        detail_view_layout.visibility = VISIBLE
        app_bar_layout.setExpanded(true, true)

        //set toolbar title
        collapsing_toolbar_layout.title = item.title
        toolbar.title = item.title

        //set title
        title_view.text = item.title

        //set backdrop image
        Glide.with(this)
                .load(item.backdropPath)
                .apply(RequestOptions().centerCrop())
                .into(backdrop_image)

        //set poster image
        Glide.with(this)
                .load(item.posterPath)
                .apply(RequestOptions().centerCrop())
                .into(poster_image)

        //set tagLine
        tagline_view.text = item.tagLine
        //set overview
        overview_view.text = item.overview
        //set rating
        rating_view.text = getString(R.string.rating_placeholder, item.rating)
    }

    private fun showLoading() {
        //show state layout
        state_layout.visibility = View.VISIBLE
        state_content_text_view.setText(R.string.loading_text)
        state_image_view.setImageResource(R.drawable.ic_loading)

        //blink animation
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500
        anim.startOffset = 100
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
        state_content_text_view.startAnimation(anim)
    }

    private fun showError(error: Throwable) {
        @StringRes val message: Int
        @DrawableRes val image: Int

        when (error) {
            is NoNetworkException -> {
                image = R.drawable.ic_network_off
                message = R.string.network_exception_message
            }
            else -> {
                image = R.drawable.ic_error
                message = R.string.unknown_error
            }
        }

        state_layout.visibility = View.VISIBLE
        state_content_text_view.setText(message)
        state_image_view.setImageResource(image)
        state_content_text_view.clearAnimation()
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
