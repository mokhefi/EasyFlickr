package com.themasterspirit.easyflickr.ui.search

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.utils.inflate
import com.themasterspirit.easyflickr.utils.loadFlickrPhoto
import com.themasterspirit.flickr.data.models.FlickrPhoto
import kotlinx.android.synthetic.main.item_image.view.*

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    val items = mutableListOf<FlickrPhoto>()

    var onItemClickListener: ((FlickrPhoto) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_image))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

//        private val kodein: Kodein by closestKodein(view.context)
//        private val logger: FlickrLogger by kodein.instance()

        fun bind() {
            val photo: FlickrPhoto = items[adapterPosition]
//            val thumbnail = photo.link(FlickrPhoto.Companion.Size.THUMBNAIL)
//            val url = photo.link(FlickrPhoto.Companion.Size.DEFAULT)

            with(itemView) {
                ivPhoto.loadFlickrPhoto(photo, expectedSize = FlickrPhoto.Companion.Size.DEFAULT)
                ivPhoto.setOnClickListener { onItemClickListener?.invoke(photo) }

                tvOwnerName.text = photo.ownerName
                tvViewCount.text = photo.formattedViewCount
            }
        }
    }
}