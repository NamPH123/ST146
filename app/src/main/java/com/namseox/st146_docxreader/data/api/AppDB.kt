package com.namseox.st146_docxreader.data.api
import androidx.room.Database
import androidx.room.RoomDatabase
import com.namseox.st146_docxreader.model.FolderModel
import com.namseox.st146_docxreader.model.RecentModel
import javax.inject.Singleton

@Singleton
@Database(entities = [FolderModel::class, RecentModel::class], version = 1, exportSchema = false)
abstract class AppDB: RoomDatabase() {
    abstract fun dbDao(): Dao
}