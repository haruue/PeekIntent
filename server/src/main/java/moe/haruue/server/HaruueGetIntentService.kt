package moe.haruue.server

import android.app.ActivityManagerNative
import android.app.PendingIntent
import android.content.IIntentSender
import android.content.Intent

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class HaruueGetIntentService : IHaruueGetIntentService.Stub() {

    companion object {
        const val NAME = "user.hgis"
    }

    override fun getIntentForPendingIntent(p: PendingIntent?): Intent? {
        if (p == null) {
            return null
        }
        return ActivityManagerNative.getDefault().getIntentForIntentSender(p.mTarget)
    }

    val PendingIntent.mTarget: IIntentSender
        get() {
            val mTargetField = this::class.java.getDeclaredField("mTarget")
            mTargetField.isAccessible = true
            return mTargetField.get(this) as IIntentSender
        }

}