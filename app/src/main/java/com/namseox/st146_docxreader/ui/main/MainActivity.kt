package com.namseox.st146_docxreader.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.base.AbsBaseActivity
import com.namseox.st146_docxreader.databinding.ActivityMainBinding
import com.namseox.st146_docxreader.databinding.NavHeaderBinding
import com.namseox.st146_docxreader.ui.language.LanguageActivity
import com.namseox.st146_docxreader.utils.Const.REQUEST_NOTIFICATION_PERMISSION
import com.namseox.st146_docxreader.utils.Const.REQUEST_STORAGE_PERMISSION
import com.namseox.st146_docxreader.utils.DataHelper.dataFloderCache
import com.namseox.st146_docxreader.utils.RATE
import com.namseox.st146_docxreader.utils.SharedPreferenceUtils
import com.namseox.st146_docxreader.utils.SystemUtils
import com.namseox.st146_docxreader.utils.checkPermision
import com.namseox.st146_docxreader.utils.checkUsePermision
import com.namseox.st146_docxreader.utils.newIntent
import com.namseox.st146_docxreader.utils.onClick
import com.namseox.st146_docxreader.utils.onSingleClick
import com.namseox.st146_docxreader.utils.policy
import com.namseox.st146_docxreader.utils.rateUs
import com.namseox.st146_docxreader.utils.requesPermission
import com.namseox.st146_docxreader.utils.shareApp
import com.namseox.st146_docxreader.utils.showSystemUI
import com.namseox.st146_docxreader.utils.unItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AbsBaseActivity<ActivityMainBinding>() {
    val mViewModel by viewModels<MainViewModel>()
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    var checkOpenPer = false
    override fun getLayoutId(): Int = R.layout.activity_main
    override fun onRestart() {
        super.onRestart()
        if(checkOpenPer){
            checkOpenPer = false
            if(checkPermision(this)){
                mViewModel.getData()
            }else{
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    mViewModel.getData()
                }
            }
        }
    }
    fun openPer(){
        checkOpenPer = true
        SystemUtils.setLocale(this)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.permission)
            .setMessage(R.string.we_need_you_to_provide_storage_management_permission_to_be_able_to_use_the_app_functions)
            .setPositiveButton(R.string.tv_yes) { dialog, _ ->
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.tv_cancel) { dialog, _ ->
                dialog.dismiss()
                showSystemUI()
            }
            .setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()
        val negativeButton =
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        val negativeButton2 =
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        negativeButton2?.setTextColor(resources.getColor(R.color.color_app))
        negativeButton?.setTextColor(resources.getColor(R.color._242424))
    }
    override fun initView() {
        if(dataFloderCache.size==0){
            if(checkPermision(this)){
                mViewModel.getData()
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    openPer()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        checkUsePermision(),
                        REQUEST_STORAGE_PERMISSION
                    )
                }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            when (requesPermission(requestCode)) {
                REQUEST_STORAGE_PERMISSION -> {
                    mViewModel.getData()
                }
            }
    }
}