package com.themasterspirit.easyflickr.ui.photo

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.github.piasy.biv.view.BigImageView
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.ui.BaseActivity
import com.themasterspirit.flickr.data.models.FlickrPhoto
import kotlinx.android.synthetic.main.activity_photo.*
import java.text.DateFormat


class PhotoActivity : BaseActivity() {

    private val flickrPhoto: FlickrPhoto by lazy {
        intent.getParcelableExtra<FlickrPhoto>(FlickrPhoto.TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_photo)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        tvTitle.text = flickrPhoto.title
        tvDateUpload.text = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(flickrPhoto.dateUpload)
        tvOwnerName.text = flickrPhoto.ownerName
        tvViewCount.text = flickrPhoto.formattedViewCount

        title = ""

        ivPhoto.setOnClickListener {
            if (containerTopControls.visibility == View.VISIBLE) hideUi() else showUi()
        }

        ivPhoto.setInitScaleType(BigImageView.INIT_SCALE_TYPE_CENTER_INSIDE)

        val thumbnail = flickrPhoto.link(FlickrPhoto.Companion.Size.DEFAULT)
        val origin = flickrPhoto.link(FlickrPhoto.Companion.Size.ORIGIN)

        ivPhoto.showImage(Uri.parse(thumbnail), Uri.parse(origin))
    }

    private fun hideUi() {
        hideSystemUi()
        listOf(toolbar, containerTopControls, containerBottomControls).forEach {
            it.visibility = View.GONE
        }
    }

    private fun showUi() {
        showSystemUi()
        listOf(toolbar, containerTopControls, containerBottomControls).forEach {
            it.visibility = View.VISIBLE
        }

    }

    /**
     * Enables regular immersive mode.
     * For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
     * Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
     */
    private fun hideSystemUi() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }


    /**
     * Shows the system bars by removing all the flags
     * except for the ones that make the content appear under the system bars.
     */
    private fun showSystemUi() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}