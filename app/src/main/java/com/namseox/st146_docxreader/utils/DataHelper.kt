package com.namseox.st146_docxreader.utils

import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.model.FolderModel
import com.namseox.st146_docxreader.model.LanguageModel
import com.namseox.st146_docxreader.model.ListFileModel
import com.namseox.st146_docxreader.utils.Const._DOC
import com.namseox.st146_docxreader.utils.Const._EXCEL
import com.namseox.st146_docxreader.utils.Const._HTML
import com.namseox.st146_docxreader.utils.Const._PDF
import com.namseox.st146_docxreader.utils.Const._PPT
import com.namseox.st146_docxreader.utils.Const._TXT

object DataHelper {
    var positionLanguageOld: Int = 0
    var checkSort = 0     //0: name, 1: size, 2: date, 3: type
    var listLanguage = arrayListOf<LanguageModel>(
        LanguageModel("Spanish", "es", R.drawable.ic_flag_spanish),
        LanguageModel("French", "fr", R.drawable.ic_flag_french),
        LanguageModel("Hindi", "hi", R.drawable.ic_flag_hindi),
        LanguageModel("English", "en", R.drawable.ic_flag_english),
        LanguageModel("Portuguese", "pt", R.drawable.ic_flag_portugeese),
        LanguageModel("German", "de", R.drawable.ic_flag_germani),
        LanguageModel("Indonesian", "in", R.drawable.ic_flag_indo)
    )
    var dataFloderCache = arrayListOf<FolderModel>()
    var dataListFile = arrayListOf<ListFileModel>(
        ListFileModel("Doc", arrayListOf(), _DOC,R.drawable.imv_doc),
        ListFileModel("PDF", arrayListOf(), _PDF,R.drawable.imv_pdf),
        ListFileModel("Excel", arrayListOf(), _EXCEL,R.drawable.imv_excel),
        ListFileModel("PPT", arrayListOf(), _PPT,R.drawable.imv_ppt),
        ListFileModel("TXT", arrayListOf(), _TXT,R.drawable.imv_txt),
        ListFileModel("HTML", arrayListOf(), _HTML,R.drawable.imv_html),
    )
}