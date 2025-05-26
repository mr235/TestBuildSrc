package com.mr235.testbuildsrc

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BusUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btPost).setOnClickListener {
            BusUtilsExt.post("testBusPlugin", "hello-world-BusUtilsExt")
            BusUtils.post("testBusPlugin2", "hello-world-BusUtils")
        }
        BusUtilsExt.register(this)
        BusUtils.register(this)
    }

    @BusUtilsExt.Bus(tag = "testBusPlugin")
    fun testBusPlugin(content: String) {
        println(content)
    }

    @BusUtils.Bus(tag = "testBusPlugin2")
    fun testBusPlugin2(content: String) {
        println(content)
    }

    override fun onDestroy() {
        super.onDestroy()
        BusUtilsExt.unregister(this)
        BusUtils.unregister(this)
    }
}