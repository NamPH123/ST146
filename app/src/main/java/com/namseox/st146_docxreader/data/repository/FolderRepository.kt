package com.namseox.st146_docxreader.data.repository

import android.content.Context
import android.util.Log
import com.namseox.st146_docxreader.data.api.BaseRoomDBHelper
import com.namseox.st146_docxreader.model.FolderModel

class FolderRepository(context: Context): BaseRoomDBHelper(context)  {
    fun getAll(): List<FolderModel>{
        return try {
            db.dbDao().getAllBookmark()
        } catch (e: Exception) {
            Log.d("TAG", "exception_of_app getAll from roomDB: ${e} ")
            arrayListOf<FolderModel>()
        }
    }
    fun addBookmark(idol : FolderModel): Long{
        return try {
            db.dbDao().setBookmark(idol)
        } catch (e: Exception) {
            Log.d("TAG", "exception_of_app addIdol from roomDB: ${e} ")
            -1
        }
    }
}