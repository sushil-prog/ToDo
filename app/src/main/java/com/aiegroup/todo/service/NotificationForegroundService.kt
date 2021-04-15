package com.aiegroup.todo.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.aiegroup.todo.R
import com.aiegroup.todo.application.ToDoApplication
import com.aiegroup.todo.ui.TodoListActivity
import com.aiegroup.todo.utils.Utility
import java.util.*

/**
 * .Service for showing network status notification.
 */
class NotificationForegroundService : Service(), java.util.Observer {

    val CHANNEL_ID = "network_status_notification_channel"

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroudNotification()
        ToDoApplication.getObservalble()?.addObserver(this);
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * .build and show notification
     */
    fun startForgroundNotification(context: Context, notficationMessage: String) {
        createNotificationChannel()
        val notificationIntent = Intent(this, TodoListActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(resources.getString(R.string.network_status_title))
            .setContentText(notficationMessage)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
    }

    /**
     * .create notification chanel for android version O and greater .
     */

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        startForegroudNotification()
    }

    /**
     * .check network status and show notification with different message ( offline or online )  .
     */
    fun startForegroudNotification() {
        if (Utility.isNetworkConnected(context = this)) {
            startForgroundNotification(this, resources.getString(R.string.online))
        } else {
            startForgroundNotification(this, resources.getString(R.string.offline))
        }
    }

}