package com.namseox.st146_docxreader.data.api
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.namseox.st146_docxreader.model.FolderModel
import com.namseox.st146_docxreader.model.RecentModel

@Dao
interface Dao {
    @Query("SELECT * FROM FolderModel")
    fun getAllBookmark(): List<FolderModel>

    @Insert
    fun setBookmark(sound: FolderModel): Long

    @Delete
    fun deleteBookmark(sound: FolderModel): Int

    @Query("DELETE FROM FolderModel WHERE uri = :uri")
    fun deleteBookmark(uri: String): Int

    @Query("SELECT * FROM FolderModel WHERE uri = :uri")
    fun getBookmark(uri : String): List<FolderModel>


    //Recent
    @Query("SELECT * FROM RecentModel")
    fun getAllRecent(): List<RecentModel>

    @Insert
    fun setRecent(sound: RecentModel): Long

    @Query("DELETE FROM RecentModel WHERE path = :uri")
    fun deleteRecent(uri: String): Int
}