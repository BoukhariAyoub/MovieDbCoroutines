package com.boukharist.moviedb.view.main.list

import android.widget.ImageView
import com.boukharist.moviedb.R
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MoviesAdapter : BaseQuickAdapter<MovieListItem, BaseViewHolder>(R.layout.list_item_movie) {

    override fun convert(helper: BaseViewHolder?, item: MovieListItem?) {
        item?.let {

            helper?.setText(R.id.title, it.name)

            Glide.with(mContext)
                    .load(it.pictureUrl)
                    .into(helper?.getView(R.id.poster_image) as ImageView)
        }
    }


}