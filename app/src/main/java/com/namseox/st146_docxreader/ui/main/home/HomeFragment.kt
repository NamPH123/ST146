package com.namseox.st146_docxreader.ui.main.home

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.countryhuman.countryball.maker.base.AbsBaseFragment
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.databinding.FragmentHomeBinding
import com.namseox.st146_docxreader.databinding.PopupSortBinding
import com.namseox.st146_docxreader.dialog.DialogNewDocument
import com.namseox.st146_docxreader.model.UiState
import com.namseox.st146_docxreader.ui.main.AllFilesAdapter
import com.namseox.st146_docxreader.ui.main.MainActivity
import com.namseox.st146_docxreader.ui.main.MainViewModel
import com.namseox.st146_docxreader.utils.dataFloderCache
import com.namseox.st146_docxreader.utils.dataListFile
import com.namseox.st146_docxreader.utils.dpToPx
import com.namseox.st146_docxreader.utils.onSingleClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.graphics.toColorInt

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                mViewModel._CheckCallData.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            checkCallData = false
                            binding.loading.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            checkCallData = true
                            adapterListFile.submitList(dataListFile)
                            adapterAllFiles.submitList(dataFloderCache)
                            binding.loading.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            checkCallData = false
                            withContext(Dispatchers.IO) { mViewModel.getData() }
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
        bindingPopup.llName.onSingleClick { changeBGPopupSort(0) }
        bindingPopup.llSize.onSingleClick { changeBGPopupSort(1) }
        bindingPopup.llDate.onSingleClick { changeBGPopupSort(2) }
        bindingPopup.llType.onSingleClick { changeBGPopupSort(3) }
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
                "bookmark" -> {}
            }
        }
        adapterAllFiles.onClickPopup = { position, type ->
            when (type) {
                0 -> {}
                1 -> {}
                2 -> {}
            }
        }
    }
}