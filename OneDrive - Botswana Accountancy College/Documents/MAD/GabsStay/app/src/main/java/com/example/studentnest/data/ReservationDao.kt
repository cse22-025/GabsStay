package com.example.studentnest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReservationDao {

    @Insert
    suspend fun insert(reservation: Reservation): Long

    @Query("SELECT * FROM reservations WHERE listingId = :listingId AND status = 'CONFIRMED' LIMIT 1")
    suspend fun activeReservationFor(listingId: Int): Reservation?

    @Query("SELECT * FROM reservations WHERE studentId = :studentId ORDER BY reservationId DESC")
    suspend fun getForStudent(studentId: Int): List<Reservation>
}
