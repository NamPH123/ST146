package com.namseox.st146_docxreader.ui.main

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.namseox.st146_docxreader.data.repository.FolderRepository
import com.namseox.st146_docxreader.model.FolderModel
import com.namseox.st146_docxreader.model.RecentModel
import com.namseox.st146_docxreader.model.UiState
import com.namseox.st146_docxreader.utils.Const._DOC
import com.namseox.st146_docxreader.utils.Const._EXCEL
import com.namseox.st146_docxreader.utils.Const._HTML
import com.namseox.st146_docxreader.utils.Const._PDF
import com.namseox.st146_docxreader.utils.Const._PPT
import com.namseox.st146_docxreader.utils.Const._TXT
import com.namseox.st146_docxreader.utils.DataHelper.dataFloderCache
import com.namseox.st146_docxreader.utils.DataHelper.dataListFile
import com.namseox.st146_docxreader.utils.getSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(app: Application, val folderRepository: FolderRepository) :
    AndroidViewModel(app) {
    var lastDotIndex = 0
    lateinit var folder: FolderModel
    var _CheckCallData = MutableStateFlow<UiState<ArrayList<FolderModel>>>(UiState.Loading)
    var _AllBookmark = MutableStateFlow<UiState<ArrayList<FolderModel>>>(UiState.Loading)
    var _AllRecent = MutableStateFlow<UiState<ArrayList<RecentModel>>>(UiState.Loading)

    fun getAllBookmark() {
        _AllBookmark.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _AllBookmark.value =
                UiState.Success(folderRepository.getAllBookmark() as ArrayList<FolderModel>)
        }

    }

    fun deleteBookmark(uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            folderRepository.removeBookmark(uri)
        }
    }

    fun addBookmark(folderModel: FolderModel) {
        viewModelScope.launch(Dispatchers.IO) {
            folderRepository.addBookmark(folderModel)
        }
    }

    fun getAllRecent(){
        _AllRecent.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _AllRecent.value = UiState.Success(folderRepository.getAllRecent() as ArrayList<RecentModel>)
        }
    }

    fun deleteRecent(uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            folderRepository.removeRecent(uri)
        }
    }

    fun addRecent(folderModel: RecentModel) {
        viewModelScope.launch(Dispatchers.IO) {
            folderRepository.addRecent(folderModel)
        }
    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllDirectory(File(Environment.getExternalStorageDirectory().absolutePath))
            _CheckCallData.value = UiState.Success(dataFloderCache)
        }
    }

    fun getAllDirectory(directory: File) {
        _CheckCallData.value = UiState.Loading
        directory.listFiles()?.forEach { file ->
            folder = FolderModel(file.path)
            if (File(folder.uri).isDirectory) {
                getAllDirectory(file)
            } else {
                if (file.isFile) {
                    lastDotIndex = file.path.lastIndexOf(".")

                    try {
                        when (file.path.substring(lastDotIndex + 1)) {
                            "doc", "docx" -> {
                                dataListFile[0].arrListFile.add(folder.apply {
                                    type = _DOC
                                    size = getSize(file)
//                                    date = Date(file.lastModified())
                                    name = file.name
                                })
                                dataFloderCache.add(folder)
                            }

                            "pdf" -> {
                                dataListFile[1].arrListFile.add(folder.apply {
                                    type = _PDF
                                    size = getSize(file)
//                                    date = Date(file.lastModified())
                                    name = file.name
                                })
                                dataFloderCache.add(folder)
                            }

                            "xls", "xlsx" -> {
                                dataListFile[2].arrListFile.add(folder.apply {
                                    type = _EXCEL
                                    size = getSize(file)
//                                    date = Date(file.lastModified())
                                    name = file.name
                                })
                                dataFloderCache.add(folder)
                            }

                            "ppt", "pptx" -> {
                                dataListFile[3].arrListFile.add(folder.apply {
                                    type = _PPT
                                    size = getSize(file)
//                                    date = Date(file.lastModified())
                                    name = file.name
                                })
                                dataFloderCache.add(folder)
                            }

                            "txt" -> {
                                dataListFile[4].arrListFile.add(folder.apply {
                                    type = _TXT
                                    size = getSize(file)
//                                    date = Date(file.lastModified())
                                    name = file.name
                                })
                                dataFloderCache.add(folder)
                            }

                            "html", "htm" -> {
                                dataListFile[5].arrListFile.add(folder.apply {
                                    type = _HTML
                                    size = getSize(file)
//                                    date = Date(file.lastModified())
                                    name = file.name
                                })
                                dataFloderCache.add(folder)
                            }
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
        dataFloderCache.sortBy { it.name }
    }
}