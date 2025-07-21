package com.namseox.st146_docxreader.utils


import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import androidx.core.graphics.toColorInt
import com.namseox.st146_docxreader.R
import com.namseox.st146_docxreader.model.LanguageModel

@BindingAdapter("setBGCV")
fun ConstraintLayout.setBGCV(check: LanguageModel) {
    if (check.active) {
        this.setBackgroundResource(R.drawable.bg_card_border2)
    } else {
        this.setBackgroundResource(R.drawable.bg_card_border)
    }
}
@BindingAdapter("setTextColor")
fun TextView.setTextColor(check: Boolean) {
    if (check) {
        this.setTextColor("#FCFCFC".toColorInt())
    } else {
        this.setTextColor("#121212".toColorInt())
    }
}
@BindingAdapter("setBG")
fun AppCompatImageView.setBG(id: Int) {
    Glide.with(this).load(id).into(this)
}
@BindingAdapter("setImg")
fun AppCompatImageView.setImg(data : Int){
    Glide.with(this).load(data).into(this)
}