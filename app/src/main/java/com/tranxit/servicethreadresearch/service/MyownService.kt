package com.tranxit.servicethreadresearch.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import android.util.Log


class MyownService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    override fun onCreate() {
        super.onCreate()
        val thread = HandlerThread("startService", Process.THREAD_PRIORITY_BACKGROUND)
        thread.start()

        serviceLooper = thread.looper
        serviceHandler = ServiceHandler(serviceLooper!!)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val msg = serviceHandler!!.obtainMessage()
        msg.arg1 = startId
        serviceHandler!!.sendMessage(msg)
        return START_STICKY

    }

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            for (i in 0..9) {
                Log.d("Theard", "" + i)
                try {
                    Thread.sleep(10000)
                } catch (e: InterruptedException) {
                    // Restore interrupt status.
                    Thread.currentThread().interrupt()
                }

            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }
}
