package com.themasterspirit.easyflickr.ui.home

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.utils.FlickrLogger
import com.themasterspirit.easyflickr.utils.inflate
import com.themasterspirit.flickr.data.models.FlickrPhoto
import kotlinx.android.synthetic.main.item_image.view.*
import org.kodein.di.Kodein
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.lang.Exception

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    val items = mutableListOf<FlickrPhoto>()

    var onItemClickListener: ((FlickrPhoto, Bitmap?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_image))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val TAG = "ViewHolder"

        private val kodein: Kodein by closestKodein(view.context)

        private val logger: FlickrLogger by kodein.instance()

        private var photo: Bitmap? = null

        fun bind() {
            val photo: FlickrPhoto = items[adapterPosition]

            val url: String = photo.link(FlickrPhoto.Companion.Size.THUMBNAIL_Q)
            logger.log(TAG, "photo url = [$url]")

            with(itemView) {
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.ic_placeholder_photo)
                        .error(R.drawable.ic_placeholder_photo_broken)
                        .into(object : Target {
                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            }

                            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                                logger.log(TAG, "onBitmapFailed(); e = [$e], errorDrawable = [$errorDrawable]", e)
                            }

                            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                this@ViewHolder.photo = bitmap
                                ivPhoto.setImageBitmap(bitmap)
                            }

                        })
                ivPhoto.setOnClickListener {
                    onItemClickListener?.invoke(photo, this@ViewHolder.photo)
                }

            }
        }
    }
}