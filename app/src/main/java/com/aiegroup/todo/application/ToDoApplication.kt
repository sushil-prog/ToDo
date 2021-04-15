package com.aiegroup.todo.application

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

/**
 * .ToDoApplication application
 */

class ToDoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            startNetworkCallback(this)
        } else {
            val filter = IntentFilter()
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            this.registerReceiver(broadcastReceiver, filter)
        }
    }

    companion object {
        /**
         * .Network observable object used in NotificationForegroundService to receive network state
         */
        fun getObservalble(): NetworkObservable? {
            return NetworkObservable.instance
        }
    }

    /**
     * .Register Connectivity Callback for android version O & greater
     */
    fun startNetworkCallback(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val cm: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder: NetworkRequest.Builder = NetworkRequest.Builder()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                cm.registerNetworkCallback(
                    builder.build(),
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            getObservable()?.connectionChanged()
                        }

                        override fun onLost(network: Network) {
                            getObservable()?.connectionChanged()
                        }

                    })

            }
        }
    }

    /**
     * .Broadcast receiver for network connectivity
     */
    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            getObservable()?.connectionChanged()
        }
    }

    class NetworkObservable private constructor() : Observable() {
        fun connectionChanged() {
            setChanged()
            notifyObservers()
        }

        companion object {
            /**
             * .get instance of current NetworkObservable class
             */
            var instance: NetworkObservable? = null
                get() {
                    if (field == null) {
                        field = NetworkObservable()
                    }
                    return field
                }
                private set
        }
    }

    fun getObservable(): NetworkObservable? {
        return NetworkObservable.instance
    }
}