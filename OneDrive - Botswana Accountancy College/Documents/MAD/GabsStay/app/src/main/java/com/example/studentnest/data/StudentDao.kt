package com.example.studentnest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun register(student: Student): Long

    @Query("SELECT * FROM students WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): Student?

    @Query("SELECT * FROM students WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): Student?

    @Query("SELECT COUNT(*) FROM students")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(students: List<Student>)
}
