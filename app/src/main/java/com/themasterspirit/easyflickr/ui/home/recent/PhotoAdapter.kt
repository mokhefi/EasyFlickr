package com.themasterspirit.easyflickr.ui.home.recent

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.utils.inflate
import com.themasterspirit.flickr.data.models.FlickrPhoto
import kotlinx.android.synthetic.main.item_image.view.*

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    val items = mutableListOf<FlickrPhoto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_image))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {
            val photo: FlickrPhoto = items[adapterPosition]

            with(itemView) {
                Picasso.get()
                        .load(photo.link)
                        .fit()
                        .centerCrop()
                        .into(ivPhoto)
            }
        }
    }
}