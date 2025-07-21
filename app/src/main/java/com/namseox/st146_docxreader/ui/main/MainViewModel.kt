package com.namseox.st146_docxreader.ui.main

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import com.namseox.st146_docxreader.model.FolderModel
import com.namseox.st146_docxreader.model.UiState
import com.namseox.st146_docxreader.utils.Const._DOC
import com.namseox.st146_docxreader.utils.Const._EXCEL
import com.namseox.st146_docxreader.utils.Const._HTML
import com.namseox.st146_docxreader.utils.Const._PDF
import com.namseox.st146_docxreader.utils.Const._PPT
import com.namseox.st146_docxreader.utils.Const._TXT
import com.namseox.st146_docxreader.utils.dataFloderCache
import com.namseox.st146_docxreader.utils.dataListFile
import com.namseox.st146_docxreader.utils.folder
import com.namseox.st146_docxreader.utils.lastDotIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    var _CheckCallData = MutableStateFlow<UiState<ArrayList<FolderModel>>>(UiState.Loading)
    suspend fun getData() {
        getAllDirectory(File(Environment.getExternalStorageDirectory().absolutePath))
        _CheckCallData.value = UiState.Success(dataFloderCache)
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
                                dataListFile[0].arrListFile.add(folder.apply { type = _DOC })
                                dataFloderCache.add(folder)
                            }

                            "pdf" -> {
                                dataListFile[1].arrListFile.add(folder.apply { type = _PDF })
                                dataFloderCache.add(folder)
                            }

                            "xls", "xlsx" -> {
                                dataListFile[2].arrListFile.add(folder.apply { type = _EXCEL })
                                dataFloderCache.add(folder)
                            }

                            "ppt", "pptx" -> {
                                dataListFile[3].arrListFile.add(folder.apply { type = _PPT })
                                dataFloderCache.add(folder)
                            }

                            "txt" -> {
                                dataListFile[4].arrListFile.add(folder.apply { type = _TXT })
                                dataFloderCache.add(folder)
                            }

                            "html", "htm" -> {
                                dataListFile[5].arrListFile.add(folder.apply { type = _HTML })
                                dataFloderCache.add(folder)
                            }
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }
}