package com.example.tai.mpulsetest

//
// Sample App to embed mPulse SDK to measure app performance and user activities
//

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View

import kotlinx.android.synthetic.main.activity_main.responseTextBox

import com.soasta.mpulse.core.MPulse

import okhttp3.*

import java.io.IOException
import java.util.*

import com.soasta.mpulse.core.MPLog

val MPULSE_API_KEY = "S9MG6-ZQB3V-NDCFL-4WYLR-WVNPT"
val rng = Random()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("entered")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enable mPulse logging
        MPLog.setDebug(true)

        MPulse.initializeWithAPIKey(MPULSE_API_KEY)
        Log.d("URL: ${MPulse.DEFAULT_MPULSE_SERVER_URL}");

    }

    fun onTestClick(view: View) {
        Log.d("entered")

        responseTextBox.setText("")

        val mp = MPulse.sharedInstance()
        val ua = OkHttpClient.Builder().build()
        val req = Request.Builder().url("https://www.akamai.com/").get().header("User-Agent", "tai - mPulseTest").build()

        val dim = if (rng.nextInt(10) < 5) { "foo" } else { "bar" }

        mp.enable()
        mp.enableNetworkMonitoring()
        mp.enableFilteredNetworkMonitoring()

        Log.d("serverURL: ${mp.serverURL.toString()}")

        //
        mp.sendMetric("testMetric", 123)

        //mp.setDimension("testDim", dim)
        val t0 = mp.startTimer("testTimer")

        // make an async call
        ua.newCall(req).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
                Log.d("request ok")

                Handler(Looper.getMainLooper()).post {
                    mp.stopTimer(t0)

                    val body = response?.body()?.string()
                    responseTextBox.setText(body)

                    mp.sendTimer("testTimer", 1234)
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                Log.d("request failed")
                Log.d(e?.message!!)
            }
        })
    }
}
