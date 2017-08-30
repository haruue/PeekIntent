package moe.haruue.peekintent

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

/**
 * A [NotificationListenerService] to get Notification from framework
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

class NotificationListener : NotificationListenerService() {

    companion object {
        const val ACTION_GET_NOTIFICATIONS = "moe.haruue.peekintent.ACTION_GET_NOTIFICATIONS"
    }

    val list = mutableListOf<NotificationInfo>()

    val binder = object : INotificationListener.Stub() {
        override fun getNotifications(): MutableList<NotificationInfo> {
            val temp = mutableListOf(*list.toTypedArray())
            list.clear()
            return temp
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val info = sbn.notification.info
        Log.d("NotificationListener", "NotificationPosted: " + info.title + ": " + info.message)
        list.add(info)
    }

    override fun onBind(intent: Intent?): IBinder {
        return if (intent?.action == ACTION_GET_NOTIFICATIONS) {
            binder
        } else {
            super.onBind(intent)
        }
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("NotificationListener", "onListenerConnected")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d("NotificationListener", "onListenerDisconnected")
    }

}