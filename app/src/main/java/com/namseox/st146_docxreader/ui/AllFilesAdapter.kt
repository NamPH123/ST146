package com.namseox.st146_docxreader.ui

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.countryhuman.countryball.maker.base.AbsBaseAdapter
import com.countryhuman.countryball.maker.base.AbsBaseDiffCallBack
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.databinding.ItemAllFilesBinding
import com.namseox.st146_docxreader.databinding.PopupAllFileBinding
import com.namseox.st146_docxreader.model.FolderModel
import com.namseox.st146_docxreader.utils.Const
import com.namseox.st146_docxreader.utils.Const.TAGLOG
import com.namseox.st146_docxreader.utils.dpToPx
import com.namseox.st146_docxreader.utils.formatSize
import com.namseox.st146_docxreader.utils.formatter
import com.namseox.st146_docxreader.utils.onSingleClick
import java.io.File
import java.util.Date

class AllFilesAdapter :
    AbsBaseAdapter<FolderModel, ItemAllFilesBinding>(R.layout.item_all_files, DiffAllFile()) {
    var onClickPopup: ((Int, Int) -> Unit)? = null
    var onClick: ((Int, String) -> Unit)? = null
    @SuppressLint("SetTextI18n")
    override fun bind(
        binding: ItemAllFilesBinding,
        position: Int,
        data: FolderModel,
        holder: RecyclerView.ViewHolder
    ) {
        if(data.checkBookMark) binding.imvBookmark.setImageResource(R.drawable.ic_bookmark_true) else binding.imvBookmark.setImageResource(
            R.drawable.ic_bookmark)
        binding.imv.setImageResource(  when (data.type) {
            Const._DOC -> {
                R.drawable.imv_doc_reader
            }

            Const._PDF -> {
                R.drawable.imv_pdf_reader
            }

            Const._EXCEL -> {
                R.drawable.imv_excel_reader
            }

            Const._PPT -> {
                R.drawable.imv_ppt_reader
            }

            Const._TXT -> {
                R.drawable.imv_txt_reader
            }

            else -> {
                R.drawable.imv_html_reader
            }
        })
        var file = File(data.uri)
        binding.tvNameFile.text = file.name
        binding.tvSize.text =
            formatter.format(Date(file.lastModified())).toString() + " | " + formatSize(data.size)

        val bindingPopup = PopupAllFileBinding.inflate(LayoutInflater.from(binding.root.context))
        val popup = PopupWindow(
            bindingPopup.root,
            dpToPx(162f, binding.root.context).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        binding.imvDot.setOnClickListener {
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            val anchorY = location[1]
            val screenHeight = binding.root.resources.displayMetrics.heightPixels

            val displayFrame = Rect()
            it.getWindowVisibleDisplayFrame(displayFrame)
            val availableHeightBelow = screenHeight - anchorY

            if (availableHeightBelow > dpToPx(180f, binding.root.context)) {
                popup.showAsDropDown(
                    it,
                    -(dpToPx(140f, binding.root.context).toInt()),
                    -(dpToPx(0f, binding.root.context).toInt()),
                    Gravity.CENTER
                )
            } else {
                popup.showAsDropDown(
                    it,
                    -(dpToPx(140f, binding.root.context).toInt()),
                    -(dpToPx(180f, binding.root.context).toInt()),
                    Gravity.CENTER
                )
            }
        }
        bindingPopup.tv1.isSelected = true
        bindingPopup.tv2.isSelected = true
        bindingPopup.tv3.isSelected = true
        bindingPopup.llRename.onSingleClick {
            onClickPopup?.invoke(position, 0)
            popup.dismiss()
        }
        bindingPopup.llShare.onSingleClick {
            onClickPopup?.invoke(position, 1)
            popup.dismiss()
        }
        bindingPopup.llDelete.onSingleClick {
            onClickPopup?.invoke(position, 2)
            popup.dismiss()
        }
        binding.root.onSingleClick {
            onClick?.invoke(position, "item")
        }
        binding.imvBookmark.onSingleClick {
            onClick?.invoke(position, "bookmark")
        }
    }

    class DiffAllFile : AbsBaseDiffCallBack<FolderModel>() {
        override fun itemsTheSame(
            oldItem: FolderModel,
            newItem: FolderModel
        ): Boolean {
            return oldItem.uri == newItem.uri
        }

        override fun contentsTheSame(
            oldItem: FolderModel,
            newItem: FolderModel
        ): Boolean {
            return oldItem.uri != newItem.uri || oldItem.checkBookMark != newItem.checkBookMark
        }

    }
}