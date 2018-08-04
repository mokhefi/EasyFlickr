package com.themasterspirit.easyflickr.ui.home.recent

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.ui.BaseFragment
import com.themasterspirit.easyflickr.utils.Failure
import com.themasterspirit.easyflickr.utils.Loading
import com.themasterspirit.easyflickr.utils.Success
import com.themasterspirit.easyflickr.utils.viewModelProvider
import com.themasterspirit.flickr.data.api.repositories.FlickrRepository
import kotlinx.android.synthetic.main.fragment_recent.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinContext
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class RecentPhotosFragment : BaseFragment() {

    override val kodeinContext: KodeinContext<*> = kcontext(activity)

    override val kodein: Kodein by closestKodein()

    // todo
    private val application: Application by instance()
    private val repository: FlickrRepository by instance()

    private val viewModel: RecentPhotosViewModel by viewModelProvider {
        RecentPhotosViewModel(application, repository)
    }

    private val adapter by lazy { PhotoAdapter() }
    private val layoutManager: GridLayoutManager by lazy { GridLayoutManager(context, 2) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        if (adapter.items.isEmpty()) viewModel.refreshPhotos()
    }

    override fun onDetach() {
        super.onDetach()
        Toast.makeText(context, "detach", Toast.LENGTH_SHORT).show()
    }

    private fun initViews() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshPhotos()
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun initObservers() {
        viewModel.recentPhotos.observe(this, Observer { data ->
            when (data) {
                is Loading -> {
                    progress(data.loading)
                    swipeRefreshLayout.isRefreshing = false
                }
                is Success -> {
                    adapter.items.clear()
                    adapter.items.addAll(data.result)
                    adapter.notifyDataSetChanged()
                }
                is Failure -> {
                    val error = data.error ?: getString(R.string.message_default_error)
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
