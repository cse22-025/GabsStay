package com.example.studentnest.ui.reservation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studentnest.data.AppDatabase
import com.example.studentnest.data.Reservation
import com.example.studentnest.databinding.ActivityReservationBinding
import com.example.studentnest.util.SessionManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

/**
 * Deposit & Reservation feature: simulates a payment, writes a
 * Reservation record with a generated receipt reference, and flips the
 * Listing's status to RESERVED so no other student can book it.
 */
class ReservationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        val listingId = intent.getIntExtra("listingId", -1)

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val listing = db.listingDao().getById(listingId)
            if (listing != null) {
                binding.tvListingTitle.text = listing.title
                binding.tvAmount.text = "BWP ${listing.depositBWP}"
            }
        }

        binding.btnPay.setOnClickListener { payDeposit(listingId) }
        binding.btnDone.setOnClickListener { finish() }
    }

    private fun payDeposit(listingId: Int) {
        binding.btnPay.isEnabled = false
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)

            // Guard against a race where two students try to reserve at once
            val existing = db.reservationDao().activeReservationFor(listingId)
            val listing = db.listingDao().getById(listingId)
            if (existing != null || listing == null || listing.status == "RESERVED") {
                showResult(success = false, reference = null)
                return@launch
            }

            // Simulated payment workflow (Pay2Cell / Orange Money style
            // reference, no real gateway integration is required by the brief)
            val reference = "SN-${Random.nextInt(100000, 999999)}"
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

            db.reservationDao().insert(
                Reservation(
                    listingId = listingId,
                    studentId = session.getStudentId(),
                    amountPaidBWP = listing.depositBWP,
                    referenceNumber = reference,
                    dateBooked = timestamp
                )
            )
            db.listingDao().setStatus(listingId, "RESERVED")

            showResult(success = true, reference = reference)
        }
    }

    private fun showResult(success: Boolean, reference: String?) {
        binding.groupPayment.visibility = android.view.View.GONE
        binding.groupReceipt.visibility = android.view.View.VISIBLE
        if (success) {
            binding.tvReceiptTitle.text = "Payment Successful"
            binding.tvReference.text = "Reference: $reference"
        } else {
            binding.tvReceiptTitle.text = "Reservation Failed"
            binding.tvReference.text = "This room was just reserved by another student."
        }
    }
}
