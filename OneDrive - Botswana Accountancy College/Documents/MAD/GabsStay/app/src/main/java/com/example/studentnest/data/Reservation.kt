package com.example.studentnest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Table 3 of 4: Reservations
 * Created when a student pays a simulated deposit on a listing.
 * A unique reference number acts as the payment receipt.
 */
@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true) val reservationId: Int = 0,
    val listingId: Int,
    val studentId: Int,
    val amountPaidBWP: Int,
    val referenceNumber: String,
    val dateBooked: String, // ISO datetime
    val status: String = "CONFIRMED" // CONFIRMED or CANCELLED
)
