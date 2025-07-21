package com.namseox.st146_docxreader.ui.permission

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.graphics.toColorInt
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.base.AbsBaseActivity
import com.namseox.st146_docxreader.databinding.ActivityPermissionBinding
import com.namseox.st146_docxreader.ui.main.MainActivity
import com.namseox.st146_docxreader.utils.Const
import com.namseox.st146_docxreader.utils.Const.REQUEST_NOTIFICATION_PERMISSION
import com.namseox.st146_docxreader.utils.Const.REQUEST_STORAGE_PERMISSION
import com.namseox.st146_docxreader.utils.SharedPreferenceUtils
import com.namseox.st146_docxreader.utils.SystemUtils
import com.namseox.st146_docxreader.utils.changeText
import com.namseox.st146_docxreader.utils.checkPermision
import com.namseox.st146_docxreader.utils.checkUsePermision
import com.namseox.st146_docxreader.utils.onSingleClick
import com.namseox.st146_docxreader.utils.requesPermission
import com.namseox.st146_docxreader.utils.showSystemUI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PermissionActivity : AbsBaseActivity<ActivityPermissionBinding>() {
    var check = false
    var count = 0

    @Inject
    lateinit var sharedPreferenceUtils: SharedPreferenceUtils

    override fun getLayoutId(): Int = R.layout.activity_permission
    override fun initView() {
        count = sharedPreferenceUtils.getNumber("count")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            binding.rl4.visibility = View.VISIBLE
//            binding.rl2.visibility = View.GONE
        } else {
            binding.rl4.visibility = View.GONE
//            binding.rl2.visibility = View.VISIBLE
        }
        val space = " "
        binding.tvTitle.text = TextUtils.concat(
            changeText(
                this,
                getString(R.string.allow),
                "#2F2F2F".toColorInt(),
                R.font.roboto_regular
            ),
            space,
            changeText(
                this,
                getString(R.string.app_name),
                "#45AEF2".toColorInt(),
                R.font.roboto_bold
            ),
            space,
            changeText(
                this,
                getString(R.string.request_permission_to_use_notifications_to_notify_you),
                "#2F2F2F".toColorInt(),
                R.font.roboto_regular
            ),
        )
        checkPer()
    }

    override fun initAction() {

        binding.btnContinue.onSingleClick {
            sharedPreferenceUtils.putBooleanValue(Const.PERMISON, true)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
            finish()
        }
        binding.swiVibrate2.onSingleClick {
            if (!checkPermision(this)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    SystemUtils.setLocale(this)
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.permission)
                        .setMessage(R.string.we_need_you_to_provide_storage_management_permission_to_be_able_to_use_the_app_functions)
                        .setPositiveButton(R.string.tv_yes) { dialog, _ ->
                            val intent =
                                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
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
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        checkUsePermision(),
                        REQUEST_STORAGE_PERMISSION
                    )
                }

            }
        }
        binding.swiVibrate4.onSingleClick {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        count++
        sharedPreferenceUtils.putNumber("count", count)
        if (count > 2) {
            when (requesPermission(requestCode)) {
                REQUEST_STORAGE_PERMISSION -> {
                    binding.swiVibrate2.setImageResource(R.drawable.ic_swith_true_per)
                }

                REQUEST_NOTIFICATION_PERMISSION -> {
                    binding.swiVibrate4.setImageResource(R.drawable.ic_swith_true_per)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkPer()
    }

    private fun checkPer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                binding.swiVibrate4.setImageResource(R.drawable.ic_swith_true_per)
            } else {
                binding.swiVibrate4.setImageResource(R.drawable.ic_swith_false_per)
            }
        }
        if (checkPermision(this)) {
            binding.swiVibrate2.setImageResource(R.drawable.ic_swith_true_per)
        } else {
            binding.swiVibrate2.setImageResource(R.drawable.ic_swith_false_per)
        }
    }
}