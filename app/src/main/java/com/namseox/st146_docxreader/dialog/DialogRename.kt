package com.namseox.st146_docxreader.dialog

import android.app.Activity
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.base.BaseDialog
import com.namseox.st146_docxreader.databinding.DialogRenameBinding
import com.namseox.st146_docxreader.utils.onSingleClick
import com.namseox.st146_docxreader.utils.showToast

class DialogRename(var mActivity: Activity, var name: String) : BaseDialog<DialogRenameBinding>(mActivity, false) {
    var onClick: ((String) -> Unit)? = null
    override fun getContentView(): Int = R.layout.dialog_rename

    override fun initView() {
        binding.tvCancel.onSingleClick {
            dismiss()
        }
        binding.tvOk.onSingleClick {
            if(binding.edt.text.toString().trim().isEmpty()){
                showToast(mActivity,R.string.name_cannot_be_left_blank)
            }else{
                if(binding.edt.text.toString().trim()==name){
                    showToast(mActivity,R.string.same_as_old_name)
                }else{
                    onClick?.invoke(binding.edt.text.toString().trim())
                    dismiss()
                }
            }
        }
    }

    override fun bindView() {

    }
}