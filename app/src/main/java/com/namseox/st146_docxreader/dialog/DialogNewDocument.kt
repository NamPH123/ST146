package com.namseox.st146_docxreader.dialog

import android.app.Activity
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.base.BaseDialog
import com.namseox.st146_docxreader.databinding.DialogNewDocBinding
import com.namseox.st146_docxreader.utils.onSingleClick

class DialogNewDocument(context: Activity) : BaseDialog<DialogNewDocBinding>(context, true) {
    var onClick: ((Int) -> Unit)? = null
    override fun getContentView(): Int = R.layout.dialog_new_doc

    override fun initView() {
        binding.apply {
            llCamera.onSingleClick {
                onClick?.invoke(0)
                dismiss()
            }
            llGallery.onSingleClick {
                onClick?.invoke(1)
                dismiss()
            }
            llBlankDocument.onSingleClick {
                onClick?.invoke(2)
                dismiss()
            }
        }
    }

    override fun bindView() {

    }
}