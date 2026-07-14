package com.example.studentnest.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentnest.data.AppDatabase
import com.example.studentnest.data.ChatMessage
import com.example.studentnest.databinding.ActivityChatBinding
import com.example.studentnest.util.SessionManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Extension feature: a simple persisted chat thread tied to one listing,
 * so a student can message the landlord/provider about that room.
 */
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    private lateinit var session: SessionManager
    private var listingId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        listingId = intent.getIntExtra("listingId", -1)

        adapter = ChatAdapter()
        binding.rvMessages.layoutManager = LinearLayoutManager(this)
        binding.rvMessages.adapter = adapter

        val db = AppDatabase.getDatabase(applicationContext)
        db.chatDao().getThread(listingId).observe(this) { messages ->
            adapter.submitList(messages)
            binding.rvMessages.scrollToPosition(maxOf(0, messages.size - 1))
        }

        binding.btnSend.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val text = binding.etMessage.text.toString().trim()
        if (text.isEmpty()) return

        val timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            db.chatDao().insert(
                ChatMessage(
                    listingId = listingId,
                    senderId = session.getStudentId(),
                    senderName = session.getStudentName(),
                    message = text,
                    timestamp = timestamp,
                    isFromStudent = true
                )
            )
            binding.etMessage.text?.clear()
        }
    }
}
