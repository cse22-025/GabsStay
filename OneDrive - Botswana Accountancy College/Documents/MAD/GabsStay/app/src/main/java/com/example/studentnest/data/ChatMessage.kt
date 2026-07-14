package com.example.studentnest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Table 4 of 4: Chat
 * Extension feature: simple persisted chat thread between a student and
 * the landlord/provider of a given listing.
 */
@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val messageId: Int = 0,
    val listingId: Int,
    val senderId: Int,
    val senderName: String,
    val message: String,
    val timestamp: String, // ISO datetime
    val isFromStudent: Boolean
)
