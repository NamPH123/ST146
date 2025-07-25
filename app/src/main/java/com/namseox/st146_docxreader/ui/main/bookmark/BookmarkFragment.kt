package com.namseox.st146_docxreader.ui.main.bookmark

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
import com.namseox.st146_docxreader.databinding.FragmentBookmarkBinding
import com.namseox.st146_docxreader.databinding.PopupSortBinding
import com.namseox.st146_docxreader.dialog.DialogRename
import com.namseox.st146_docxreader.model.FolderModel
import com.namseox.st146_docxreader.model.UiState
import com.namseox.st146_docxreader.ui.AllFilesAdapter
import com.namseox.st146_docxreader.ui.main.MainActivity
import com.namseox.st146_docxreader.ui.main.MainViewModel
import com.namseox.st146_docxreader.utils.DataHelper.checkSort
import com.namseox.st146_docxreader.utils.DataHelper.dataFloderCache
import com.namseox.st146_docxreader.utils.dpToPx
import com.namseox.st146_docxreader.utils.onSingleClick
import com.namseox.st146_docxreader.utils.shareFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

@AndroidEntryPoint
class BookmarkFragment : AbsBaseFragment<FragmentBookmarkBinding, MainActivity>() {
    val mViewModel: MainViewModel by activityViewModels()
    lateinit var bindingPopup: PopupSortBinding
    lateinit var popup: PopupWindow
    var arrBookmark = arrayListOf<FolderModel>()
    override fun getLayout(): Int = R.layout.fragment_bookmark
    val adapter by lazy {
        AllFilesAdapter()
    }
    override fun initView() {
        mViewModel.getAllBookmark()
        binding.rcvAllFile.adapter = adapter
        binding.rcvAllFile.itemAnimator = null
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel._AllBookmark.collect { uiState ->
                        when (uiState) {
                            is UiState.Success -> {
                                uiState.data.forEach { mFolder ->
                                    dataFloderCache.find { it.uri == mFolder.uri }?.let {
                                        it.checkBookMark = true
                                        arrBookmark.add(it)
                                    } ?: mViewModel.deleteBookmark(mFolder.uri)
                                }
                                adapter.submitList(arrBookmark)
                                binding.loading.visibility = View.GONE
                            }

                            is UiState.Error -> {
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
            adapter.onClick = { position, type ->
                when (type) {
                    "item" -> {}
                    "bookmark" -> {
                        arrBookmark[position].checkBookMark = false
                        mViewModel.deleteBookmark(arrBookmark[position].uri)
                        arrBookmark.removeAt(position)
                        adapter.submitList(arrBookmark)
                    }
                }
            }
            adapter.onClickPopup = { position, type ->
                when (type) {
                    0 -> {
                        val oldFile = File(arrBookmark[position].uri)
                        var dialogRename = DialogRename(requireActivity(), oldFile.name)
                        dialogRename.onClick = {
                            val newFile =
                                File(oldFile.parent, it + "." + oldFile.name.substringAfter("."))
                            oldFile.renameTo(newFile)
                            arrBookmark[position].uri = newFile.path
                            arrBookmark[position].name = File(arrBookmark[position].uri).name
                            if (checkSort == 0) {
                                arrBookmark.sortBy { it.name }
                            }
                            adapter.submitList(arrBookmark)
                            if (arrBookmark[position].checkBookMark) {
                                mViewModel.addBookmark(arrBookmark[position])
                            }
                        }
                        dialogRename.show()
                    }

                    1 -> {
                        shareFile(requireContext(), File(arrBookmark[position].uri))
                    }

                    2 -> {
                        var a = dataFloderCache.find { it.uri == arrBookmark[position].uri }
                        File(arrBookmark[position].uri).delete()
                        mViewModel.deleteBookmark(arrBookmark[position].uri)
                        dataFloderCache.remove(a)
                        arrBookmark.removeAt(position)
                        adapter.submitList(arrBookmark)
                    }
                }
            }
        }
    }

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

    fun sortFile(i: Int) {
        checkSort = i
        when (i) {
            0 -> {
                changeBGPopupSort(0)
                arrBookmark.sortBy { it.name }
                adapter.submitList(arrBookmark)
            }

            1 -> {
                changeBGPopupSort(1)
                arrBookmark.sortBy { it.size }
                adapter.submitList(arrBookmark)
            }

            2 -> {
                changeBGPopupSort(2)
                arrBookmark.sortBy { Date(File(it.uri).lastModified()) }
                adapter.submitList(arrBookmark)
            }

            3 -> {
                changeBGPopupSort(3)
                arrBookmark.sortBy { it.type }
                adapter.submitList(arrBookmark)
            }
        }
        binding.rcvAllFile.postDelayed({ binding.rcvAllFile.scrollToPosition(0) }, 500)
    }
}