package com.namseox.st146_docxreader.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.namseox.st146_docxreader.utils.formatter
import com.namseox.st146_docxreader.utils.getSize
import kotlinx.serialization.SerialName
import java.io.File
import java.util.Date

@Entity
class FolderModel(
    @PrimaryKey
    var uri: String,
    var type: String = "" ,
    var checkBookMark : Boolean = false) {
//    var file = File(uri)
//    var date = formatter.format(Date(file.lastModified()))
//    var size = getSize(file)

}