package com.namseox.st146_docxreader.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.base.AbsBaseActivity
import com.namseox.st146_docxreader.databinding.ActivitySplashBinding
import com.namseox.st146_docxreader.ui.language.LanguageActivity
import com.namseox.st146_docxreader.ui.tutorial.TutorialActivity
import com.namseox.st146_docxreader.utils.Const
import com.namseox.st146_docxreader.utils.SharedPreferenceUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
import javax.inject.Inject
import kotlin.apply

@AndroidEntryPoint
class SplashActivity : AbsBaseActivity<ActivitySplashBinding>() {
    @Inject
    lateinit var sharedPreferenceUtils: SharedPreferenceUtils
    var handle = Handler(Looper.getMainLooper())
    var runable = Runnable {
        if (!SharedPreferenceUtils.getInstance(applicationContext)
                .getBooleanValue(Const.LANGUAGE)
        ) {
            SharedPreferenceUtils.getInstance(applicationContext).clearData()
            startActivity(Intent(this@SplashActivity, LanguageActivity::class.java))
        } else {
            startActivity(Intent(this@SplashActivity, TutorialActivity::class.java))
        }
        finish()
    }

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initView() {
        if (!isTaskRoot() && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
            && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_MAIN)
        ) {
            finish(); return;
        }
        if (!SharedPreferenceUtils.getInstance(applicationContext)
                .getBooleanValue(Const.FIRSTAPP)
        ){
            SharedPreferenceUtils.getInstance(applicationContext).clearData()
        }
        handle.postDelayed(runable, 3000)
    }


    override fun onRestart() {
        super.onRestart()
        handle.postDelayed(runable, 2000)
    }

    override fun onBackPressed() {

    }

    override fun onStop() {
        super.onStop()
        handle.removeCallbacks(runable)
    }

    override fun initAction() {
    }
}