package com.namseox.st146_docxreader.data.api
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.namseox.st146_docxreader.model.FolderModel

@Dao
interface Dao {
    @Query("SELECT * FROM FolderModel")
    fun getAllBookmark(): List<FolderModel>

    @Insert
    fun setBookmark(sound: FolderModel): Long

    @Delete
    fun deleteBookmark(sound: FolderModel): Int

    @Query("SELECT * FROM FolderModel WHERE uri = :uri")
    fun getBookmark(uri : String): List<FolderModel>
}