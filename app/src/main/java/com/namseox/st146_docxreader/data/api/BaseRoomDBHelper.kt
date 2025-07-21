package com.namseox.st146_docxreader.data.api

import android.content.Context
import androidx.room.Room
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.utils.SingletonHolder

open class BaseRoomDBHelper(context: Context) {
    val db = Room.databaseBuilder(context, AppDB::class.java,context.getString(R.string.app_name)).build()
    companion object : SingletonHolder<BaseRoomDBHelper, Context>(::BaseRoomDBHelper)
}