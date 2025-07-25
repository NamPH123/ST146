package com.namseox.st146_docxreader.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentModel(
    @PrimaryKey
    var path : String
)