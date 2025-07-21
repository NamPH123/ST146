package com.namseox.st146_docxreader.ui.main.home

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.countryhuman.countryball.maker.base.AbsBaseAdapter
import com.countryhuman.countryball.maker.base.AbsBaseDiffCallBack
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.databinding.ItemHomeFileBinding
import com.namseox.st146_docxreader.model.ListFileModel
import com.namseox.st146_docxreader.utils.dataListFile

class ListFileAdapter : AbsBaseAdapter<ListFileModel, ItemHomeFileBinding>(R.layout.item_home_file, DiffListFile()) {

    @SuppressLint("SetTextI18n")
    override fun bind(
        binding: ItemHomeFileBinding,
        position: Int,
        data: ListFileModel,
        holder: RecyclerView.ViewHolder
    ) {
        binding.imv.setImageResource(dataListFile[position].imv)
        binding.tv.text = "${dataListFile[position].name} ${binding.root.context.getString(R.string.files)} (${dataListFile[position].arrListFile.size})"
    }

    class DiffListFile : AbsBaseDiffCallBack<ListFileModel>() {
        override fun itemsTheSame(
            oldItem: ListFileModel,
            newItem: ListFileModel
        ): Boolean {
            return oldItem.type == newItem.type
        }

        override fun contentsTheSame(
            oldItem: ListFileModel,
            newItem: ListFileModel
        ): Boolean {
            return oldItem.type != newItem.type
        }
    }
}