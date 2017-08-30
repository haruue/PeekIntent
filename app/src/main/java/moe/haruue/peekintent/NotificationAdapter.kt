package moe.haruue.peekintent

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Adapter for the [MainActivity.Layout.list]
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    val list = mutableListOf<NotificationInfo>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        val context = itemView.context
        val pm = context.packageManager
        val ni = list[position]
        with(holder.layout) {
            if (!ni.packageName.isEmpty()) {
                label.text = pm.getApplicationLabel(pm.getApplicationInfo(ni.packageName, 0))
            } else {
                label.text = "Android OS"
            }
            title.text = ni.title
            message.text = ni.message
        }
        itemView.onClick {
            val intent = ni.intent
            if (intent != null) {
                val intentInfo = with(StringBuilder()) {
                    append("Intent:     ").append(intent.toString())
                    append('\n')
                    append("Uri:        ").append(intent.toUri(Intent.URI_ALLOW_UNSAFE))
                    append('\n')
                    append("Extras:     ").append(intent.extras)
                    append('\n')
                    append("Action:     ").append(intent.action)
                    append('\n')
                    append("Categories: ").append(intent.categories)
                    append('\n')
                    append("Data:       ").append(intent.data)
                    append('\n')
                    append("DataString: ").append(intent.dataString)
                    toString()
                }
                with(AlertDialog.Builder(context)) {
                    setTitle("Intent Information")
                    setMessage(intentInfo)
                    setPositiveButton("Copy") { _, _ -> context.clipboardCopy(intentInfo) }
                    setNegativeButton("Cancel") { _, _ -> }
                    create().show()
                }
            } else {
                context.toast("notification.intent == null")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun getItemCount() = list.count()

    class ViewHolder(parent: ViewGroup, val layout: ItemLayout = ItemLayout()) :
            RecyclerView.ViewHolder(layout.createView(AnkoContext.create(parent.context, parent, false)))

    class ItemLayout : AnkoComponent<ViewGroup> {

        lateinit var label: TextView
        lateinit var title: TextView
        lateinit var message: TextView

        override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
            verticalLayout {
                lparams(width = matchParent, height = wrapContent) {
                    padding = dip(10)
                }
                label = textView {
                    textSize = 13f
                }.lparams(width = matchParent, height = wrapContent) {
                    margin = dip(1)
                }
                title = textView {
                    textSize = 15f
                    textColor = ctx.getColor(R.color.black)
                }.lparams(width = matchParent, height = wrapContent) {
                    margin = dip(1)
                }
                message = textView {
                    textSize = 15f
                }.lparams(width = matchParent, height = wrapContent) {
                    margin = dip(1)
                }
            }
        }
    }

    operator fun plusAssign(entry: NotificationInfo) {
        list.add(entry)
        notifyItemInserted(list.size - 1)
        if (list.size == 1) {
            notifyDataSetChanged()
        }
    }

    operator fun plusAssign(entries: List<NotificationInfo>) {
        Log.d("na_pa", "add:    " + entries.toString())
        Log.d("na_pa", "result: " + list.toString())
        val beforeSize = list.size
        list.addAll(entries)
        notifyItemRangeInserted(beforeSize, list.size - 1)
        if (list.size == 1) {
            notifyDataSetChanged()
        }
    }

    operator fun minusAssign(entry: NotificationInfo) {
        val position = list.indexOf(entry)
        if (list.remove(entry)) {
            notifyItemRemoved(position)
        }
    }

    operator fun minusAssign(entries: List<NotificationInfo>) {
        val oldList = listOf(*list.toTypedArray())
        oldList.forEach { t -> if (entries.contains(t)) { this -= t } }
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }


}