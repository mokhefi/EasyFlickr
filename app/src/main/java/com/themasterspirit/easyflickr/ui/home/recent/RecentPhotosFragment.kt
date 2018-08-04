package com.themasterspirit.easyflickr.ui.home.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.ui.BaseFragment
import com.themasterspirit.easyflickr.ui.FlickrApplication
import com.themasterspirit.easyflickr.utils.Failure
import com.themasterspirit.easyflickr.utils.Loading
import com.themasterspirit.easyflickr.utils.Success
import kotlinx.android.synthetic.main.fragment_recent.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class RecentPhotosFragment : BaseFragment() {

//    override val kodeinContext = kcontext(activity)

    override val kodein: Kodein by lazy {
        (activity!!.application as FlickrApplication).kodein
    }

    private val viewModel: RecentPhotosViewModel by instance()
//        kodein.newInstance { RecentPhotosViewModel(application = instance(), repository = instance()) }
//    }

    private val adapter by lazy { PhotoAdapter() }
    private val layoutManager: GridLayoutManager by lazy { GridLayoutManager(context, 2) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
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
                is Loading -> progress(data.loading)
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