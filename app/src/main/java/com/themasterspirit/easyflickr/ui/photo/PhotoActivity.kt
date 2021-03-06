package com.themasterspirit.easyflickr.ui.photo

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.ui.BaseActivity
import com.themasterspirit.easyflickr.utils.navigationBarHeightPx
import com.themasterspirit.easyflickr.utils.statusBarHeightPx
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

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        containerTopControls.updatePadding(top = statusBarHeightPx)
        containerBottomControls.updatePadding(bottom = navigationBarHeightPx)

        tvTitle.text = flickrPhoto.title
        tvDateUpload.text = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(flickrPhoto.dateUpload)
        tvOwnerName.text = flickrPhoto.ownerName
        tvViewCount.text = flickrPhoto.formattedViewCount

        title = ""

        ivPhoto.setOnClickListener {
            if (containerTopControls.visibility == View.VISIBLE) hideUi() else showUi()
        }

        val thumbnail = flickrPhoto.link(FlickrPhoto.Companion.Size.DEFAULT)
        val origin = flickrPhoto.link(FlickrPhoto.Companion.Size.ORIGIN)

        ivPhoto.showImage(thumbnail.toUri(), origin.toUri())
    }

    private fun hideUi() {
        hideSystemUi()
        listOf(toolbar, containerTopControls, containerBottomControls).forEach { it.isGone = true }
    }

    private fun showUi() {
        showSystemUi()
        listOf(toolbar, containerTopControls, containerBottomControls).forEach { it.isVisible = true }
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

    companion object {
        const val TAG = "PhotoActivity"
    }
}