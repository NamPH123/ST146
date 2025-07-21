package com.namseox.st146_docxreader.ui.main

import android.os.Environment
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.base.AbsBaseActivity
import com.namseox.st146_docxreader.databinding.ActivityMainBinding
import com.namseox.st146_docxreader.databinding.NavHeaderBinding
import com.namseox.st146_docxreader.ui.language.LanguageActivity
import com.namseox.st146_docxreader.utils.RATE
import com.namseox.st146_docxreader.utils.SharedPreferenceUtils
import com.namseox.st146_docxreader.utils.newIntent
import com.namseox.st146_docxreader.utils.onClick
import com.namseox.st146_docxreader.utils.onSingleClick
import com.namseox.st146_docxreader.utils.policy
import com.namseox.st146_docxreader.utils.rateUs
import com.namseox.st146_docxreader.utils.shareApp
import com.namseox.st146_docxreader.utils.unItem
import androidx.lifecycle.lifecycleScope
import com.namseox.st146_docxreader.utils.dataFloderCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AbsBaseActivity<ActivityMainBinding>() {
    val mViewModel by viewModels<MainViewModel>()
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        if(dataFloderCache.size==0){
            lifecycleScope.launch(Dispatchers.IO) {
                mViewModel.getData()
            }
        }
        navHostFragment = supportFragmentManager.findFragmentById(R.id.flContainer) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        val navView = binding.navView
        val headerBinding = NavHeaderBinding.bind(navView.getHeaderView(0))
        if (SharedPreferenceUtils.getInstance(this).getBooleanValue(RATE)) {
            headerBinding.llRateUs.visibility = View.GONE
        }
        headerBinding.llLanguage.onSingleClick {
            binding.drawerLayout.close()
            startActivity(newIntent(applicationContext, LanguageActivity::class.java))
        }
        headerBinding.llRateUs.onSingleClick {
            rateUs(0)
            binding.drawerLayout.close()
        }
        headerBinding.llShare.onClick {
            shareApp()
            binding.drawerLayout.close()
        }
        headerBinding.llPolicy.onSingleClick {
            policy()
            binding.drawerLayout.close()
        }
        unItem = {
            headerBinding.llRateUs.visibility = View.GONE
        }
    }

    override fun initAction() {
        binding.imvNavi.onSingleClick {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }
    }
}