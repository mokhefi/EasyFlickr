package com.themasterspirit.easyflickr.ui.search

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
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
        setContentView(R.layout.activity_search)

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
            startActivity(Intent(this@SearchActivity, PhotoActivity::class.java).apply {
                putExtra(FlickrPhoto.TAG, photo)
            })
        }
    }

    private fun initSearchView(search: SearchView) {
        search.queryHint = getString(R.string.search_query_hint_text)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                logger.log(TAG, "onQueryTextSubmit(); query = [$query]")
                val text: String = query ?: initialSearchText
                viewModel.search(text)
                viewModel.updateSuggestions(text)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                logger.log(TAG, "onQueryTextChange(); newText = [$newText]")
//                search.autoCompleteTextView.setText(newText)
                return if (newText?.isNotEmpty() == true) {
                    viewModel.updateSuggestions(newText)
                    true
                } else {
                    false
                }
            }
        })
        search.autoCompleteTextView.threshold = 1
//        search.autoCompleteTextView.setOnFocusChangeListener { view, hasFocus ->
//            if (hasFocus && view is SearchView) {
//                view.autoCompleteTextView.showDropDown()
//            } else {
////                search.autoCompleteTextView.setText("")
//            }
//        }
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

                    tvEmptyView.text = getString(R.string.message_no_data)
                    tvEmptyView.isGone = adapter.items.isEmpty()
                    searchView?.clearFocus()
                }
                is Failure -> {
                    val error = data.error ?: getString(R.string.message_default_error)
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
                if (search.suggestionsAdapter is SearchSuggestionAdapter) {
                    search.suggestionsAdapter.swapCursor(cursor)
                } else {
                    search.suggestionsAdapter = SearchSuggestionAdapter(search, cursor).apply {
                        onRemoveClickListener = { query: String -> viewModel.deleteSuggestion(query) }
                    }
                }
                logger.log(TAG, "suggestions updated, query = [${search.query}] count = [${cursor.count}]")
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
