package com.themasterspirit.easyflickr.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.ui.BaseActivity
import com.themasterspirit.easyflickr.ui.photo.PhotoActivity
import com.themasterspirit.easyflickr.utils.Failure
import com.themasterspirit.easyflickr.utils.FlickrAndroidViewModelFactory
import com.themasterspirit.easyflickr.utils.Loading
import com.themasterspirit.easyflickr.utils.Success
import com.themasterspirit.flickr.data.models.FlickrPhoto
import kotlinx.android.synthetic.main.activity_home.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinContext
import org.kodein.di.android.ActivityRetainedScope
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.*


class HomeActivity : BaseActivity() {

    override val kodeinContext: KodeinContext<*> = kcontext(this@HomeActivity)
    override val kodein: Kodein by retainedKodein {
        extend(parentKodein)

        bind<HomeViewModel>() with scoped(ActivityRetainedScope<HomeActivity>()).singleton {
            FlickrAndroidViewModelFactory(application) {
                HomeViewModel(application, instance())
            }.create(HomeViewModel::class.java)
        }
    }

    private val viewModel: HomeViewModel by instance()

    private val adapter by lazy { PhotoAdapter() }
    private val layoutManager: GridLayoutManager by lazy { GridLayoutManager(application, 2) }

    private val initialSearchText: String by lazy {
        getString(R.string.text_search_initial)
    }
    private var searchView: SearchView? = null
        set(value) {
            field = value
            value?.let { initSearchView(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initViews()
        initObservers()

        if (adapter.items.isEmpty()) viewModel.search(initialSearchText)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        searchView = menu.findItem(R.id.actionSearch).actionView as SearchView
        return true
    }

    private fun initViews() {
        swipeRefreshLayout.setOnRefreshListener {
            val query: String? = searchView?.query?.toString()
            viewModel.search(if (query?.isNotEmpty() == true) query else initialSearchText)
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        adapter.onItemClickListener = { photo: FlickrPhoto ->
            startActivity(Intent(this@HomeActivity, PhotoActivity::class.java).apply {
                putExtra(FlickrPhoto.TAG, photo)
            })
        }
    }

    private fun initSearchView(search: SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query ?: initialSearchText)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        search.setOnCloseListener {
            // todo: doesn't work
            viewModel.search(initialSearchText)
            true
        }
    }

    private fun initObservers() {
        viewModel.recentPhotos.observe(this, Observer { data ->
            when (data) {
                is Loading -> {
                    if (data.loading) {
                        if (adapter.items.isEmpty()) {
                        } else {
                            swipeRefreshLayout.isRefreshing = true
                        }
                    } else {
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
                is Success -> {
                    adapter.items.clear()
                    adapter.items.addAll(data.result)
                    adapter.notifyDataSetChanged()
                    tvEmptyView.visibility = if (adapter.items.isEmpty()) View.VISIBLE else View.GONE
                }
                is Failure -> {
                    val error = data.error ?: getString(R.string.message_default_error)
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    companion object {
        const val TAG = "HomeActivity"
    }
}
