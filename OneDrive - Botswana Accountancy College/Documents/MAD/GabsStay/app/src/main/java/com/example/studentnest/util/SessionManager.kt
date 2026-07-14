package com.example.studentnest.util

import android.content.Context

/**
 * Lightweight session store. Keeps the logged-in student's id and name
 * so other screens (Listings, Reservation, Chat) know who is acting
 * without re-querying the database every time.
 */
class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("studentnest_session", Context.MODE_PRIVATE)

    fun saveSession(studentId: Int, fullName: String) {
        prefs.edit()
            .putInt(KEY_ID, studentId)
            .putString(KEY_NAME, fullName)
            .apply()
    }

    fun getStudentId(): Int = prefs.getInt(KEY_ID, -1)

    fun getStudentName(): String = prefs.getString(KEY_NAME, "") ?: ""

    fun isLoggedIn(): Boolean = getStudentId() != -1

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_ID = "student_id"
        private const val KEY_NAME = "student_name"
    }
}
