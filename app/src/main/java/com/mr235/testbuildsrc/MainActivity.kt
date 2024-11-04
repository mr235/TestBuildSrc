package com.mr235.testbuildsrc

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val mHandler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testBusPlugin("hello-world")
        mHandler.postDelayed({
            BusUtilsExt.post("testBusPlugin", "hello-world from handler")
        }, 5000)
    }

    @BusUtilsExt.Bus(tag = "testBusPlugin")
    fun testBusPlugin(content: String) {
        println(content)
    }
}