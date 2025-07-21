package com.namseox.st146_docxreader.ui.language

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.base.AbsBaseActivity
import com.namseox.st146_docxreader.databinding.ActivityLanguageBinding
import com.namseox.st146_docxreader.model.LanguageModel
import com.namseox.st146_docxreader.ui.main.MainActivity
import com.namseox.st146_docxreader.ui.tutorial.TutorialActivity
import com.namseox.st146_docxreader.utils.Const
import com.namseox.st146_docxreader.utils.Const.LANGUAGE
import com.namseox.st146_docxreader.utils.DataHelper.listLanguage
import com.namseox.st146_docxreader.utils.DataHelper.positionLanguageOld
import com.namseox.st146_docxreader.utils.SharedPreferenceUtils
import com.namseox.st146_docxreader.utils.SystemUtils
import com.namseox.st146_docxreader.utils.onSingleClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.get
import kotlin.text.get

@AndroidEntryPoint
class LanguageActivity : AbsBaseActivity<ActivityLanguageBinding>() {
    lateinit var adapter: LanguageAdapter
    var codeLang: String? = null

    @Inject
    lateinit var providerSharedPreference: SharedPreferenceUtils


    override fun getLayoutId(): Int = R.layout.activity_language
    override fun initView() {
        codeLang = providerSharedPreference.getStringValue("language")
        if (codeLang.equals("")) {
            binding.icBack.visibility = View.GONE
            binding.tvTitle2.visibility = View.GONE
//            codeLang = "en"
        }else{
            binding.tvTitle1.visibility = View.GONE
        }
        binding.rclLanguage.itemAnimator = null
        adapter = LanguageAdapter()
        setRecycleView()
    }

    override fun initAction() {
        binding.icBack.onSingleClick {
            finish()
        }
        binding.imvDone.onSingleClick {
            SharedPreferenceUtils.getInstance(applicationContext).putBooleanValue(Const.FIRSTAPP,true)
            if (codeLang.equals("")) {
                Toast.makeText(
                    this,
                    getString(R.string.you_have_not_selected_anything_yet),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                SystemUtils.setPreLanguage(applicationContext, codeLang)
                providerSharedPreference.putStringValue("language", codeLang)
                if (SharedPreferenceUtils.getInstance(applicationContext).getBooleanValue(LANGUAGE)) {
                    var intent = Intent(
                        applicationContext,
                        MainActivity::class.java
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finishAffinity()
                    startActivity(intent)
                } else {
                    SharedPreferenceUtils.getInstance(applicationContext)
                        .putBooleanValue(LANGUAGE, true)
                    var intent = Intent(applicationContext, TutorialActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }


    private fun setRecycleView() {
        var i = 0
        lateinit var x: LanguageModel
        if (!codeLang.equals("")) {
            listLanguage.forEach {
                listLanguage[i].active = false
                if (codeLang.equals(it.code)) {
                    x = listLanguage[i]
                    x.active = true
                }
                i++
            }

            listLanguage.remove(x)
            listLanguage.add(0, x)
        }
        adapter.getData(listLanguage)
        binding.rclLanguage.adapter = adapter
        val manager = GridLayoutManager(applicationContext, 1, RecyclerView.VERTICAL, false)
        binding.rclLanguage.layoutManager = manager

        adapter.onClick = {
            codeLang = listLanguage[it].code
        }
    }

    override fun onBackPressed() {
        listLanguage[positionLanguageOld].active = false
        positionLanguageOld = 0
        super.onBackPressed()
    }
}