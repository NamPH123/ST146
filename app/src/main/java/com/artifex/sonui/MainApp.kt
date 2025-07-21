package com.artifex.sonui

import android.content.Context
import com.artifex.solib.ConfigOptions
import com.namseox.st146_docxreader.MyApplication

class MainApp : MyApplication() {
    private fun a() {
        val var1 = ConfigOptions.a()
        var1.a(true)
        var1.n(true)
        var1.b(true)
        var1.c(true)
        var1.o(false)
        var1.d(true)
        var1.e(true)
        var1.f(true)
        var1.g(true)
        var1.h(true)
        var1.i(true)
        var1.j(true)
        var1.k(true)
        var1.l(false)
        var1.m(true)
        var1.r(true)
        var1.p(true)
        var1.q(true)
        var1.t(false)
    }

    override fun onCreate() {
        this.a()
        super.onCreate()
        appContext = this
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}
