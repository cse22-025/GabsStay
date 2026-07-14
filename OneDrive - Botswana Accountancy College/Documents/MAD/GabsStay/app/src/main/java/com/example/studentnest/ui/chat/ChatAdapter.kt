package com.example.studentnest.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentnest.data.ChatMessage
import com.example.studentnest.databinding.ItemChatReceivedBinding
import com.example.studentnest.databinding.ItemChatSentBinding

class ChatAdapter(private val messages: MutableList<ChatMessage> = mutableListOf()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun submitList(newMessages: List<ChatMessage>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int =
        if (messages[position].isFromStudent) VIEW_SENT else VIEW_RECEIVED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_SENT) {
            SentViewHolder(ItemChatSentBinding.inflate(inflater, parent, false))
        } else {
            ReceivedViewHolder(ItemChatReceivedBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is SentViewHolder -> holder.bind(message)
            is ReceivedViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    class SentViewHolder(private val binding: ItemChatSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.tvMessage.text = message.message
            binding.tvTime.text = message.timestamp
        }
    }

    class ReceivedViewHolder(private val binding: ItemChatReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.tvMessage.text = message.message
            binding.tvTime.text = message.timestamp
            binding.tvSender.text = message.senderName
        }
    }

    companion object {
        private const val VIEW_SENT = 1
        private const val VIEW_RECEIVED = 2
    }
}
