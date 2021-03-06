package com.themasterspirit.easyflickr.ui.search

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.ui.BaseActivity
import com.themasterspirit.easyflickr.ui.photo.PhotoActivity
import com.themasterspirit.easyflickr.utils.*
import com.themasterspirit.flickr.data.models.FlickrPhoto
import kotlinx.android.synthetic.main.activity_search.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinContext
import org.kodein.di.android.ActivityRetainedScope
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.*

class SearchActivity : BaseActivity() {

    override val kodeinContext: KodeinContext<*> = kcontext(this@SearchActivity)
    override val kodein: Kodein by retainedKodein {
        extend(parentKodein)

        bind<SearchViewModel>() with scoped(ActivityRetainedScope<SearchActivity>()).singleton {
            FlickrAndroidViewModelFactory(application) {
                SearchViewModel(application, repository = instance())
            }.create(SearchViewModel::class.java)
        }
    }

    private val viewModel: SearchViewModel by instance()

    private val adapter by lazy { PhotoAdapter() }

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
        setContentView(R.layout.activity_search)

        initObservers()
        initViews()
        if (adapter.items.isEmpty()) {
            viewModel.search(searchView?.query?.toString() ?: initialSearchText)
        }
    }

    override fun onStop() {
        super.onStop()
        swipeRefreshLayout.isRefreshing = false
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
        recyclerView.layoutManager = GridLayoutManager(application, 2)
        recyclerView.adapter = adapter

        adapter.onItemClickListener = { photo: FlickrPhoto ->
            startActivity(Intent(this@SearchActivity, PhotoActivity::class.java).apply {
                putExtra(FlickrPhoto.TAG, photo)
            })
        }
    }

    private fun initSearchView(search: SearchView) {
        search.queryHint = getString(R.string.search_query_hint_text)
        search.autoCompleteTextView.threshold = 1 // can't be less than 1

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                logger.log(TAG, "onQueryTextSubmit(); query = [$query]")
                val text: String = if (query.isEmpty()) initialSearchText else query
                viewModel.search(text)
                viewModel.updateSuggestions(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                logger.log(TAG, "onQueryTextChange(); newText = [$newText]")
                viewModel.updateSuggestions(newText)
                return true
            }
        })
    }

    private fun initObservers() {
        viewModel.recentPhotos.observe(this, Observer { data ->
            when (data) {
                is Loading -> swipeRefreshLayout.isRefreshing = data.loading
                is Success -> {
                    adapter.items.clear()
                    adapter.items.addAll(data.result)
                    adapter.notifyDataSetChanged()

                    recyclerView.scrollToPosition(0)

                    tvEmptyView.text = getString(R.string.message_no_data)
                    tvEmptyView.isVisible = adapter.items.isEmpty()
                    searchView?.clearFocus()
                }
                is Failure -> {
                    val error = data.error ?: getString(R.string.message_default_error)
                    tvEmptyView.isVisible = adapter.items.isEmpty()
                    if (adapter.items.isEmpty()) {
                        tvEmptyView.text = error
                    } else {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.searchSuggestions.observe(this, Observer { cursor: Cursor ->
            searchView?.let { search ->
                logger.log(TAG, "suggestions updated, query = [${search.query}] count = [${cursor.count}], cursor = [${cursor.hashCode()}]")
                if (search.suggestionsAdapter is SearchSuggestionAdapter) {
                    search.suggestionsAdapter.swapCursor(cursor)
                } else {
                    search.suggestionsAdapter = SearchSuggestionAdapter(search, cursor).apply {
                        onRemoveClickListener = { query: String -> viewModel.deleteSuggestion(query) }
                    }
                }
            }
        })

        viewModel.deleteSuggestionsAction.observe(this, Observer {
            searchView?.query?.toString()?.let { query -> viewModel.updateSuggestions(query) }
        })
    }

    companion object {
        const val TAG = "SearchActivity"
    }
}
