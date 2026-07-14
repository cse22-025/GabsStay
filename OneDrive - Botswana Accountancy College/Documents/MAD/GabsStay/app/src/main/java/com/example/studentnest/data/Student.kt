package com.example.studentnest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Table 1 of 4: Students
 * Stores registered app users. Role distinguishes a Student from an
 * (optional) accommodation Provider, satisfying the role-based access
 * requirement in the User Management feature.
 */
@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val studentId: Int = 0,
    val fullName: String,
    val email: String,
    val password: String, // stored hashed in a production build
    val role: String = "STUDENT", // STUDENT or PROVIDER
    val campus: String = "Gaborone"
)
