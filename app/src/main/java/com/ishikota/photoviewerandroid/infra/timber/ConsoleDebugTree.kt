package com.ishikota.photoviewerandroid.infra.timber

import android.util.Log
import timber.log.Timber

class ConsoleDebugTree : Timber.DebugTree() {
    // Log format: <log message> @<file name>:<line number>
    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        val callerInfo =
            Throwable().stackTrace.getOrNull(6)?.let { "@${it.fileName}:${it.lineNumber}" }
                .orEmpty()
        Log.println(priority, tag, "$message $callerInfo")
    }
}
