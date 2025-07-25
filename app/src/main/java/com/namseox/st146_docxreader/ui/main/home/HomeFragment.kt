package com.namseox.st146_docxreader.ui.main.home

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import androidx.core.graphics.toColorInt
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.countryhuman.countryball.maker.base.AbsBaseFragment
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.databinding.FragmentHomeBinding
import com.namseox.st146_docxreader.databinding.PopupSortBinding
import com.namseox.st146_docxreader.dialog.DialogNewDocument
import com.namseox.st146_docxreader.dialog.DialogRename
import com.namseox.st146_docxreader.model.UiState
import com.namseox.st146_docxreader.ui.AllFilesAdapter
import com.namseox.st146_docxreader.ui.main.MainActivity
import com.namseox.st146_docxreader.ui.main.MainViewModel
import com.namseox.st146_docxreader.ui.reader.ReaderActivity
import com.namseox.st146_docxreader.utils.Const._TYPE
import com.namseox.st146_docxreader.utils.DataHelper.checkSort
import com.namseox.st146_docxreader.utils.DataHelper.dataFloderCache
import com.namseox.st146_docxreader.utils.DataHelper.dataListFile
import com.namseox.st146_docxreader.utils.dpToPx
import com.namseox.st146_docxreader.utils.newIntent
import com.namseox.st146_docxreader.utils.onSingleClick
import com.namseox.st146_docxreader.utils.shareFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

@AndroidEntryPoint
class HomeFragment : AbsBaseFragment<FragmentHomeBinding, MainActivity>() {
    val mViewModel: MainViewModel by activityViewModels()
    val adapterListFile by lazy {
        ListFileAdapter()
    }
    val adapterAllFiles by lazy {
        AllFilesAdapter()
    }

    override fun getLayout(): Int = R.layout.fragment_home
    var checkCallData = false
    override fun initView() {
        binding.rcv.adapter = adapterListFile
        binding.rcv.itemAnimator = null
        binding.rcvAllFile.adapter = adapterAllFiles
        binding.rcvAllFile.itemAnimator = null
        adapterListFile.submitList(dataListFile)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    mViewModel._CheckCallData.collect { uiState ->
                        when (uiState) {
                            is UiState.Loading -> {
                                checkCallData = false

                            }

                            is UiState.Success -> {
                                checkCallData = true
                                mViewModel.getAllBookmark()
                            }

                            is UiState.Error -> {
                                checkCallData = false
                                mViewModel.getData()
                            }
                        }
                    }
                }
                launch {
                    mViewModel._AllBookmark.collect { uiState ->
                        when (uiState) {
                            is UiState.Success -> {
                                uiState.data.forEach { mFolder ->
                                    dataFloderCache.find { it.uri == mFolder.uri }?.let {
                                        it.checkBookMark = true
                                    } ?: mViewModel.deleteBookmark(mFolder.uri)
                                }
                                adapterListFile.submitList(dataListFile)
                                adapterAllFiles.submitList(dataFloderCache)
                                binding.loading.visibility = View.GONE
                            }

                            is UiState.Error -> {
                                adapterListFile.submitList(dataListFile)
                                adapterAllFiles.submitList(dataFloderCache)
                                binding.loading.visibility = View.GONE
                            }

                            UiState.Loading -> {
                                binding.loading.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }
    lateinit var bindingPopup: PopupSortBinding
    lateinit var popup: PopupWindow
    fun changeBGPopupSort(i: Int) {
        bindingPopup.llName.setBackgroundColor("#ffffff".toColorInt())
        bindingPopup.llSize.setBackgroundColor("#ffffff".toColorInt())
        bindingPopup.llDate.setBackgroundColor("#ffffff".toColorInt())
        bindingPopup.llType.setBackgroundColor("#ffffff".toColorInt())
        when (i) {
            0 -> {
                bindingPopup.llName.setBackgroundColor("#F4F7FF".toColorInt())
            }

            1 -> {
                bindingPopup.llSize.setBackgroundColor("#F4F7FF".toColorInt())
            }

            2 -> {
                bindingPopup.llDate.setBackgroundColor("#F4F7FF".toColorInt())
            }

            3 -> {
                bindingPopup.llType.setBackgroundColor("#F4F7FF".toColorInt())
            }
        }
        popup.dismiss()
    }
    override fun setClick() {
        bindingPopup = PopupSortBinding.inflate(LayoutInflater.from(requireContext()))
        popup = PopupWindow(
            bindingPopup.root,
            dpToPx(162f, requireContext()).toInt(),
            WRAP_CONTENT,
            true
        )
        bindingPopup.llName.onSingleClick {
            sortFile(0)

        }
        bindingPopup.llSize.onSingleClick {
            sortFile(1)

        }
        bindingPopup.llDate.onSingleClick {
            sortFile(2)

        }
        bindingPopup.llType.onSingleClick {
            sortFile(3)

        }
        binding.apply {
            imvSort.setOnClickListener {
                popup.showAsDropDown(
                    it,
                    -(dpToPx(140f, requireContext()).toInt()),
                    -(dpToPx(0f, requireContext()).toInt()),
                    Gravity.CENTER
                )

            }
            imvPlus.onSingleClick {
                var dialogNewDocument = DialogNewDocument(requireActivity())
                dialogNewDocument.onClick = {
                    when (it) {
                        0 -> {}
                        1 -> {}
                        2 -> {}
                    }
                }
                dialogNewDocument.show()
            }
        }
        adapterAllFiles.onClick = { position, type ->
            when (type) {
                "item" -> {}
                "bookmark" -> {
                    dataFloderCache[position].checkBookMark =
                        !dataFloderCache[position].checkBookMark
                    adapterAllFiles.submitList(dataFloderCache)
                    if (dataFloderCache[position].checkBookMark) {
                        mViewModel.addBookmark(dataFloderCache[position])
                    } else {
                        mViewModel.deleteBookmark(dataFloderCache[position].uri)
                    }

                }
            }
        }
        adapterAllFiles.onClickPopup = { position, type ->
            when (type) {
                0 -> {
                    val oldFile = File(dataFloderCache[position].uri)
                    var dialogRename = DialogRename(requireActivity(), oldFile.name)
                    dialogRename.onClick = {
                        val newFile =
                            File(oldFile.parent, it + "." + oldFile.name.substringAfter("."))
                        oldFile.renameTo(newFile)
                        dataFloderCache[position].uri = newFile.path
                        dataFloderCache[position].name = File(dataFloderCache[position].uri).name
                        if (checkSort == 0) {
                            dataFloderCache.sortBy { it.name }
                        }
                        adapterAllFiles.submitList(dataFloderCache)
                        if (dataFloderCache[position].checkBookMark) {
                            mViewModel.addBookmark(dataFloderCache[position])
                        }
                    }
                    dialogRename.show()
                }

                1 -> {
                    shareFile(requireContext(), File(dataFloderCache[position].uri))
                }

                2 -> {
                    File(dataFloderCache[position].uri).delete()
                    mViewModel.deleteBookmark(dataFloderCache[position].uri)
                    dataFloderCache.removeAt(position)
                    adapterAllFiles.submitList(dataFloderCache)
                }
            }
        }
        adapterListFile.onClick = {
            startActivity(newIntent(requireContext(), ReaderActivity::class.java).putExtra(_TYPE,it))
        }
    }

    fun sortFile(i: Int) {
        checkSort = i
        when (i) {
            0 -> {
                changeBGPopupSort(0)
                dataFloderCache.sortBy { it.name }
                adapterAllFiles.submitList(dataFloderCache)
            }

            1 -> {
                changeBGPopupSort(1)
                dataFloderCache.sortBy { it.size }
                adapterAllFiles.submitList(dataFloderCache)
            }

            2 -> {
                changeBGPopupSort(2)
                dataFloderCache.sortBy { Date(File(it.uri).lastModified()) }
                adapterAllFiles.submitList(dataFloderCache)
            }

            3 -> {
                changeBGPopupSort(3)
                dataFloderCache.sortBy { it.type }
                adapterAllFiles.submitList(dataFloderCache)
            }
        }
        binding.rcvAllFile.postDelayed({ binding.rcvAllFile.scrollToPosition(0) }, 500)
    }

    override fun onResume() {
        super.onResume()
        sortFile(checkSort)
        adapterListFile.submitList(dataListFile)
//        adapterAllFiles.submitList(dataFloderCache)
    }
}
