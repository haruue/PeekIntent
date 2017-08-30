package moe.haruue.peekintent

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import java.lang.ref.WeakReference

/**
 * Main Activity, use a recycler view to show caught notifications
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val MSG_GET_NOTIFICATIONS = 6465451
    }

    val layout by lazy { Layout() }
    val notificationsUpdateHandler by lazy { NotificationsUpdateHandler(this) }
    lateinit var notificationListenerService: INotificationListener
    val notificationListenerServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(cn: ComponentName?) {}
        override fun onServiceConnected(cn: ComponentName?, binder: IBinder?) {
            notificationListenerService = INotificationListener.Stub.asInterface(binder)
            val next = Message.obtain()
            next.what = MSG_GET_NOTIFICATIONS
            notificationsUpdateHandler.sendMessageDelayed(next, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout.setContentView(this)
        val serviceIntent = Intent(NotificationListener.ACTION_GET_NOTIFICATIONS)
        serviceIntent.setClass(this, NotificationListener::class.java)
        bindService(serviceIntent, notificationListenerServiceConnection, Service.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(notificationListenerServiceConnection)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_trigger_test_notification -> {
                val notification = with(Notification.Builder(applicationContext)) {
                    setContentTitle("Test")
                    setContentText("Test Notification of PeekIntent")
                    setContentIntent(PendingIntent.getActivity(applicationContext, 0, Intent(applicationContext, MainActivity::class.java), 0))
                    setSmallIcon(R.mipmap.ic_launcher)
                    build()
                }
                notificationManager.notify(0, notification)
                return true
            }
            R.id.item_clear -> {
                layout.list.adapter.castTo<NotificationAdapter>().clear()
                return true
            }
            R.id.grant_notification_listener_permission -> {
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                startActivity(intent)
                return true
            }
            else -> { return false }
        }
    }

    class Layout : AnkoComponent<MainActivity> {

        lateinit var toolbar: Toolbar
        lateinit var list: RecyclerView

        override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
            verticalLayout {
                toolbar = toolbar {
                    elevation = dip(20).toFloat()
                    setTitleTextColor(ctx.getColor(R.color.white))
                    setSubtitleTextColor(ctx.getColor(R.color.semi_transparent))
                    setBackgroundColor(ctx.getColor(R.color.colorPrimary))
                    owner.setSupportActionBar(this)
                }.lparams(width = matchParent, height = wrapContent)
                list = recyclerView {
                    backgroundColor = ctx.getColor(R.color.white)
                    adapter = NotificationAdapter()
                    layoutManager = LinearLayoutManager(ctx)
                }.lparams(width = matchParent, height = matchParent)
            }
        }

    }

    class NotificationsUpdateHandler(activity: MainActivity) : Handler() {

        private val ref = WeakReference<MainActivity>(activity)

        val activity: MainActivity?
            get() = ref.get()

        override fun handleMessage(msg: Message) {
            when(msg.what) {
                MSG_GET_NOTIFICATIONS -> {
                    val activity = this.activity ?: return
                    activity.layout.list.adapter.castTo<NotificationAdapter>() += activity.notificationListenerService.notifications
                    val next = Message.obtain()
                    next.what = MSG_GET_NOTIFICATIONS
                    sendMessageDelayed(next, 1000)
                }
            }
        }
    }

}