package com.example.studentnest.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.studentnest.data.AppDatabase
import com.example.studentnest.data.Listing
import com.example.studentnest.databinding.ActivityListingDetailBinding
import com.example.studentnest.ui.chat.ChatActivity
import com.example.studentnest.ui.reservation.ReservationActivity
import kotlinx.coroutines.launch

class ListingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListingDetailBinding
    private var listing: Listing? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listingId = intent.getIntExtra("listingId", -1)
        loadListing(listingId)

        binding.btnReserve.setOnClickListener {
            val current = listing ?: return@setOnClickListener
            if (current.status == "RESERVED") {
                // Prevent other users from reserving an already-reserved room
                Toast.makeText(this, "This room is already reserved", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, ReservationActivity::class.java)
            intent.putExtra("listingId", current.listingId)
            startActivity(intent)
        }

        binding.btnChat.setOnClickListener {
            val current = listing ?: return@setOnClickListener
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("listingId", current.listingId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh status in case a reservation completed while we were away
        val listingId = intent.getIntExtra("listingId", -1)
        loadListing(listingId)
    }

    private fun loadListing(listingId: Int) {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val result = db.listingDao().getById(listingId) ?: return@launch
            listing = result
            binding.tvTitle.text = result.title
            binding.tvPrice.text = "BWP ${result.priceBWP} / month"
            binding.tvDeposit.text = "Deposit: BWP ${result.depositBWP}"
            binding.tvLocation.text = result.location
            binding.tvType.text = result.type
            binding.tvAmenities.text = result.amenities
            binding.tvAvailable.text = "Available from ${result.availableFrom}"
            binding.tvStatus.text = result.status
            binding.ivPhoto.setImageResource(result.imageResId)
            binding.btnReserve.isEnabled = result.status != "RESERVED"
            binding.btnReserve.text =
                if (result.status == "RESERVED") "Already Reserved" else "Pay Deposit & Reserve"
        }
    }
}
