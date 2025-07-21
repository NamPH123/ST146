package com.namseox.st146_docxreader.ui.main.home

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.countryhuman.countryball.maker.base.AbsBaseFragment
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.databinding.FragmentHomeBinding
import com.namseox.st146_docxreader.dialog.DialogNewDocument
import com.namseox.st146_docxreader.model.UiState
import com.namseox.st146_docxreader.ui.main.AllFilesAdapter
import com.namseox.st146_docxreader.ui.main.MainActivity
import com.namseox.st146_docxreader.ui.main.MainViewModel
import com.namseox.st146_docxreader.utils.dataFloderCache
import com.namseox.st146_docxreader.utils.dataListFile
import com.namseox.st146_docxreader.utils.onSingleClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    override fun setClick() {
        binding.apply {
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