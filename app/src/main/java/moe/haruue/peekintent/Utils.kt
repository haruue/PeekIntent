package moe.haruue.peekintent

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.ServiceManager
import eu.chainfire.libsuperuser.Shell
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import moe.haruue.server.HaruueGetIntentService
import moe.haruue.server.IHaruueGetIntentService
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.toast

/**
 * Useful utils functions
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

inline fun <reified T> Any.castTo() = this as T

inline fun Context.requestRootPermission(crossinline callback: () -> Unit) {
    val alertDialogWhenRequestPermission = with(AlertDialog.Builder(this)) {
        setTitle("Requesting root permission...")
        setMessage("We need root permission to get Intent from IIntentSender, if you see the permission request dialog, please grant us the root permission by click [Grant].")
        setCancelable(false)
        create()
    }
    val alertDialogWhenPermissionDenied = with(AlertDialog.Builder(this)) {
        setTitle("Permission denied")
        setMessage("Without root permission, we can't complete this operation.")
        setNegativeButton("Cancel") { _, _ -> }
        create()
    }
    alertDialogWhenRequestPermission.show()
    async(UI) {
        val result = bg { Shell.SU.available() }
        val isGranted = result.await()
        alertDialogWhenRequestPermission.dismiss()
        if (isGranted) {
            callback()
        } else {
            alertDialogWhenPermissionDenied.show()
        }
    }
}

inline fun Context.checkOrStartHaruueGetIntentService(callback: (service: IHaruueGetIntentService) -> Unit) {
    val b = ServiceManager.getService(HaruueGetIntentService.NAME)
    if (b != null) {
        callback(IHaruueGetIntentService.Stub.asInterface(b))
    } else {
        toast("HaruueGetIntentService is not available.")
    }
//    requestRootPermission {
//        val path = packageManager.getApplicationInfo(packageName, 0).publicSourceDir
//        val command = "app_process -Djava.class.path=$path /system/bin --nice-name=${HaruueGetIntentService.NAME} moe.haruue.server.Main &"
//        Shell.SU.run(command)
//    }
}

val PendingIntent.intent: Intent?
    get() {
        val s = IHaruueGetIntentService.Stub.asInterface(ServiceManager.getService(HaruueGetIntentService.NAME))
        return s?.getIntentForPendingIntent(this)
    }

fun Context.clipboardCopy(text: String) {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.text = text
}