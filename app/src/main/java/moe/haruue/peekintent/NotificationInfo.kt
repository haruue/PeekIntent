package moe.haruue.peekintent

import android.app.Notification
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable

/**
 * Model class to present a notification
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

data class NotificationInfo(
        var id: Int = 0,
        var packageName: String = "",
        var time: Long = 0,
        var intent: Intent? = null,
        var title: String = "",
        var message: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readParcelable(Intent::class.java.classLoader),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(packageName)
        parcel.writeLong(time)
        parcel.writeParcelable(intent, flags)
        parcel.writeString(title)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationInfo> {
        override fun createFromParcel(parcel: Parcel): NotificationInfo {
            return NotificationInfo(parcel)
        }

        override fun newArray(size: Int): Array<NotificationInfo?> {
            return arrayOfNulls(size)
        }
    }

}

val Notification.info: NotificationInfo
    get() {
        val i = NotificationInfo()
        with(i) {
            packageName = this@info.contentIntent?.creatorPackage ?: ""
            time = System.currentTimeMillis()
            intent = this@info.contentIntent?.intent
            title = this@info.extras.getString(Notification.EXTRA_TITLE) ?: ""
            message = this@info.extras.getString(Notification.EXTRA_TEXT) ?: ""
        }
        return i
    }
