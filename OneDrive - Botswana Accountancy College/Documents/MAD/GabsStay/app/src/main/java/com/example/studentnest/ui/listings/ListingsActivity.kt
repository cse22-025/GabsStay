package com.example.studentnest.ui.listings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentnest.data.AppDatabase
import com.example.studentnest.databinding.ActivityListingsBinding
import com.example.studentnest.ui.detail.ListingDetailActivity
import com.example.studentnest.ui.filter.FilterActivity
import com.example.studentnest.ui.login.LoginActivity
import com.example.studentnest.util.SessionManager

/**
 * Home screen after login. Shows the full seeded catalogue of listings
 * via LiveData, or a filtered subset returned from FilterActivity.
 */
class ListingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListingsBinding
    private lateinit var adapter: ListingAdapter
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        binding.tvWelcome.text = "Welcome, ${session.getStudentName()}"

        adapter = ListingAdapter { listing ->
            val intent = Intent(this, ListingDetailActivity::class.java)
            intent.putExtra("listingId", listing.listingId)
            startActivity(intent)
        }
        binding.rvListings.layoutManager = LinearLayoutManager(this)
        binding.rvListings.adapter = adapter

        val db = AppDatabase.getDatabase(applicationContext)
        db.listingDao().getAllListings().observe(this) { listings ->
            adapter.submitList(listings)
            binding.tvCount.text = "${listings.size} listings available"
        }

        binding.btnFilter.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            session.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
