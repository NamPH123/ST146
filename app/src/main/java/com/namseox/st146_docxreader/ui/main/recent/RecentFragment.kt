package com.namseox.st146_docxreader.ui.main.recent

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
import com.namseox.st146_docxreader.databinding.FragmentRecentBinding
import com.namseox.st146_docxreader.databinding.PopupSortBinding
import com.namseox.st146_docxreader.dialog.DialogRename
import com.namseox.st146_docxreader.model.FolderModel
import com.namseox.st146_docxreader.model.RecentModel
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
class RecentFragment : AbsBaseFragment<FragmentRecentBinding, MainActivity>() {
    var arrRecent = arrayListOf<FolderModel>()
    lateinit var bindingPopup: PopupSortBinding
    lateinit var popup: PopupWindow
    val mViewModel: MainViewModel by activityViewModels()
    val adapter by lazy {
        AllFilesAdapter()
    }
    override fun getLayout(): Int = R.layout.fragment_recent

    override fun initView() {
        mViewModel.getAllRecent()
        binding.rcvAllFile.adapter = adapter
        binding.rcvAllFile.itemAnimator = null
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel._AllRecent.collect { uiState ->
                        when (uiState) {
                            is UiState.Error -> {
                                binding.loading.visibility = View.GONE
                            }

                            UiState.Loading -> {
                                binding.loading.visibility = View.VISIBLE
                            }

                            is UiState.Success -> {
                                uiState.data.forEach { mRecent ->
                                    dataFloderCache.find { it.uri == mRecent.path }?.let {
                                        it.checkRecent = true
                                        arrRecent.add(it)
                                    } ?: mViewModel.deleteBookmark(mRecent.path)
                                }
                                adapter.submitList(arrRecent)
                                binding.loading.visibility = View.GONE
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
                        arrRecent[position].checkBookMark = !arrRecent[position].checkBookMark
                        if(arrRecent[position].checkBookMark){
                            mViewModel.addBookmark(arrRecent[position])
                        }else{
                            mViewModel.deleteBookmark(arrRecent[position].uri)
                        }
                        adapter.submitList(arrRecent)
                    }
                }
            }
            adapter.onClickPopup = { position, type ->
                when (type) {
                    0 -> {
                        val oldFile = File(arrRecent[position].uri)
                        var dialogRename = DialogRename(requireActivity(), oldFile.name)
                        dialogRename.onClick = {
                            val newFile =
                                File(oldFile.parent, it + "." + oldFile.name.substringAfter("."))
                            oldFile.renameTo(newFile)
                            arrRecent[position].uri = newFile.path
                            arrRecent[position].name = File(arrRecent[position].uri).name
                            if (checkSort == 0) {
                                arrRecent.sortBy { it.name }
                            }
                            adapter.submitList(arrRecent)
                            if (arrRecent[position].checkBookMark) {
                                mViewModel.addBookmark(arrRecent[position])
                            }
                        }
                        dialogRename.show()
                    }

                    1 -> {
                        shareFile(requireContext(), File(arrRecent[position].uri))
                    }

                    2 -> {
                        var a = dataFloderCache.find { it.uri == arrRecent[position].uri }
                        File(arrRecent[position].uri).delete()
                        mViewModel.deleteBookmark(arrRecent[position].uri)
                        dataFloderCache.remove(a)
                        arrRecent.removeAt(position)
                        adapter.submitList(arrRecent)
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
                arrRecent.sortBy { it.name }
                adapter.submitList(arrRecent)
            }

            1 -> {
                changeBGPopupSort(1)
                arrRecent.sortBy { it.size }
                adapter.submitList(arrRecent)
            }

            2 -> {
                changeBGPopupSort(2)
                arrRecent.sortBy { Date(File(it.uri).lastModified()) }
                adapter.submitList(arrRecent)
            }

            3 -> {
                changeBGPopupSort(3)
                arrRecent.sortBy { it.type }
                adapter.submitList(arrRecent)
            }
        }
        binding.rcvAllFile.postDelayed({ binding.rcvAllFile.scrollToPosition(0) }, 500)
    }
}