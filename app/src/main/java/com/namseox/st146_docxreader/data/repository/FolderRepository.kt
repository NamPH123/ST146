package com.namseox.st146_docxreader.data.repository

import android.content.Context
import android.util.Log
import com.namseox.st146_docxreader.data.api.BaseRoomDBHelper
import com.namseox.st146_docxreader.model.FolderModel
import com.namseox.st146_docxreader.model.RecentModel
import com.namseox.st146_docxreader.utils.Const.TAGLOG

class FolderRepository(context: Context): BaseRoomDBHelper(context)  {
    fun getAllBookmark(): List<FolderModel> {
        return try {
            db.dbDao().getAllBookmark()
        } catch (e: Exception) {
            Log.d(TAGLOG, "getAllBookmark: $e")
            arrayListOf<FolderModel>()
        }
    }
    fun addBookmark(idol : FolderModel): Long{
        return try {
            db.dbDao().setBookmark(idol)
        } catch (e: Exception) {
            -1
        }
    }

    fun removeBookmark(uri: String): Int {
        return try {
            db.dbDao().deleteBookmark(uri)
        } catch (e: Exception) {
            -1
        }
    }

    fun getAllRecent(): List<RecentModel> {
        return try {
            db.dbDao().getAllRecent()
        } catch (e: Exception) {
            arrayListOf<RecentModel>()
        }
    }

    fun addRecent(idol : RecentModel): Long{
        return try {
            db.dbDao().setRecent(idol)
        } catch (e: Exception) {
            -1
        }
    }

    fun removeRecent(uri: String): Int {
        return try {
            db.dbDao().deleteRecent(uri)
        } catch (e: Exception) {
            -1
        }
    }
}