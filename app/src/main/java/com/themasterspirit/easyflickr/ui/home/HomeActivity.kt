package com.themasterspirit.easyflickr.ui.home

import android.os.Bundle
import androidx.fragment.app.transaction
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.ui.BaseActivity
import com.themasterspirit.easyflickr.ui.home.recent.RecentPhotosFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

//    override val kodein: Kodein by retainedKodein {
//        extend(parentKodein, copy = Copy.All)
//        bind() from instance(createViewModel(HomeViewModel::class.java))

//        Example: using an Activity retained scope
//        bind<Controller>() with scoped(AndroidLifecycleScope<Activity>()).singleton { ControllerImpl(context) }
//    }

//    private val viewModel: HomeViewModel by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        logger.debug("HomeActivity", "onCreate()")

        initViews()

        if (savedInstanceState == null) {
            supportFragmentManager.transaction {
                replace(R.id.container, RecentPhotosFragment())
            }
        }
    }

    private fun initViews() {
        tvRecent.setOnClickListener {
            title = getString(R.string.title_recent)
            supportFragmentManager.transaction {
                replace(R.id.container, RecentPhotosFragment())
            }
        }
        tvSearch.setOnClickListener {
            title = getString(R.string.title_search)
        }
        tvSettings.setOnClickListener {
            title = getString(R.string.title_settings)
        }
    }
}



//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menu?.let { menuInflater.inflate(R.menu.navigation, it) }
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        return when (item?.itemId) {
//            R.id.navigation_recent -> {
//                title = getString(R.string.title_recent)
//                supportFragmentManager.transaction {
//                    replace(R.id.container, RecentPhotosFragment())
//                }
//                true
//            }
//            R.id.navigation_search -> {
//                title = getString(R.string.title_search)
//                true
//            }
//            R.id.navigation_settings -> {
//                title = getString(R.string.title_settings)
//                true
//            }
//            else -> false
//        }
//    }
