package com.example.tai.mpulsetest

import android.util.Log

//
// Android logger with custom formatting
//

fun getTag() : String {
    val frames = Throwable().stackTrace

    if (frames.size > 3) {
        val f = frames[3]
        return "${f.fileName}:${f.lineNumber}/${f.methodName}"
    }
    return "(unknown)"
}

// TODO: Would be nice if I can delegate other calls to android.util.Log
class Log {
    companion object {
        @JvmStatic
        fun d(msg: String, tr: Throwable? = null) {
            val tag = getTag()
            Log.d(tag, msg, tr)
        }

        @JvmStatic
        fun i(msg: String, tr: Throwable? = null) {
            val tag = getTag()
            Log.i(tag, msg, tr)
        }

        @JvmStatic
        fun w(msg: String, tr: Throwable? = null) {
            val tag = getTag()
            Log.w(tag, msg, tr)
        }

        @JvmStatic
        fun e(msg: String, tr: Throwable? = null) {
            val tag = getTag()
            Log.e(tag, msg, tr)
        }

        @JvmStatic
        fun wtf(msg: String, tr: Throwable? = null) {
            val tag = getTag()
            Log.wtf(tag, msg, tr)
        }
    }
}
