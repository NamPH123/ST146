package com.namseox.st146_docxreader.ui.reader

import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.base.AbsBaseActivity
import com.namseox.st146_docxreader.databinding.ActivityReaderBinding
import com.namseox.st146_docxreader.model.FolderModel
import com.namseox.st146_docxreader.ui.AllFilesAdapter
import com.namseox.st146_docxreader.utils.Const._DOC
import com.namseox.st146_docxreader.utils.Const._EXCEL
import com.namseox.st146_docxreader.utils.Const._HTML
import com.namseox.st146_docxreader.utils.Const._PDF
import com.namseox.st146_docxreader.utils.Const._PPT
import com.namseox.st146_docxreader.utils.Const._TXT
import com.namseox.st146_docxreader.utils.Const._TYPE
import com.namseox.st146_docxreader.utils.DataHelper.dataListFile

class ReaderActivity : AbsBaseActivity<ActivityReaderBinding>() {
    var arrAllFile = arrayListOf<FolderModel>()
    val adapter by lazy {
        AllFilesAdapter()
    }

    override fun getLayoutId(): Int = R.layout.activity_reader

    override fun initView() {
        binding.tvTitle.isSelected = true
        arrAllFile.addAll(dataListFile.find { it.type == intent.getStringExtra(_TYPE) }!!.arrListFile)
        when (intent.getStringExtra(_TYPE)) {
            _DOC -> {
                binding.tvTitle.text = getString(R.string.docx_reader)
            }

            _PDF -> {
                binding.tvTitle.text = getString(R.string.pdf_reader)
            }

            _EXCEL -> {
                binding.tvTitle.text = getString(R.string.excel_reader)
            }

            _PPT -> {
                binding.tvTitle.text = getString(R.string.ppt_reader)
            }

            _TXT -> {
                binding.tvTitle.text = getString(R.string.txt_reader)
            }

            _HTML -> {
                binding.tvTitle.text = getString(R.string.html_reader)
            }
        }
        binding.rcv.adapter = adapter
        binding.rcv.itemAnimator = null
        adapter.submitList(arrAllFile)
    }

    override fun initAction() {

    }
}