package com.nhatnguyenba.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messages = mutableListOf<Message>()

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSent) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is SentMessageViewHolder -> holder.bind(message)
            is ReceivedMessageViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount() = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage = itemView.findViewById<TextView>(R.id.tvMessage)
        private val tvTimestamp = itemView.findViewById<TextView>(R.id.tvTimestamp)

        fun bind(message: Message) {
            tvMessage.text = message.content
            tvTimestamp.text = SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(Date(message.timestamp))
        }
    }

    class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage = itemView.findViewById<TextView>(R.id.tvMessage)
        private val tvTimestamp = itemView.findViewById<TextView>(R.id.tvTimestamp)

        fun bind(message: Message) {
            tvMessage.text = message.content
            tvTimestamp.text = SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(Date(message.timestamp))
        }
    }
}