package com.namseox.st146_docxreader.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FolderModel(
    @PrimaryKey
    var uri: String,
    var type: String = "" ,
    var name: String = "",
    var size: Long = 0,
//    var date: Date? = null,
    var checkBookMark: Boolean = false,
    var checkRecent: Boolean = false
)