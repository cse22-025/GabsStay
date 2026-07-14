package com.example.studentnest.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChatDao {

    @Insert
    suspend fun insert(message: ChatMessage): Long

    @Query("SELECT * FROM chat_messages WHERE listingId = :listingId ORDER BY messageId ASC")
    fun getThread(listingId: Int): LiveData<List<ChatMessage>>
}
