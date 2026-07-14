package com.example.studentnest.ui.filter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentnest.data.AppDatabase
import com.example.studentnest.databinding.ActivityFilterBinding
import com.example.studentnest.ui.detail.ListingDetailActivity
import com.example.studentnest.ui.listings.ListingAdapter
import com.example.studentnest.util.NotificationHelper
import kotlinx.coroutines.launch

class FilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterBinding
    private lateinit var adapter: ListingAdapter

    private val notifPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* result ignored: app still works, just silently skips the alert */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListingAdapter { listing ->
            val intent = Intent(this, ListingDetailActivity::class.java)
            intent.putExtra("listingId", listing.listingId)
            startActivity(intent)
        }
        binding.rvResults.layoutManager = LinearLayoutManager(this)
        binding.rvResults.adapter = adapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        binding.btnApply.setOnClickListener { applyFilters() }
    }

    private fun applyFilters() {
        val minPrice = binding.etMinPrice.text.toString().toIntOrNull() ?: 0
        val maxPrice = binding.etMaxPrice.text.toString().toIntOrNull() ?: Int.MAX_VALUE
        val location = binding.etLocation.text.toString().trim()
        val availableFrom = binding.etAvailableFrom.text.toString().trim()

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val results = db.listingDao().filterListings(minPrice, maxPrice, location, availableFrom)
            adapter.submitList(results)
            binding.tvResultCount.text = "${results.size} matching listings"

            // Smart Alerts: notify the student locally when their saved
            // preferences match one or more current listings.
            if (results.isNotEmpty()) {
                NotificationHelper.notifyMatch(applicationContext, results.size)
            }
        }
    }
}
