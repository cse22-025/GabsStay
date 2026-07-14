package com.example.studentnest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Table 2 of 4: Listings
 * Each row is one accommodation record. 50 seeded rows are inserted on
 * first run (see SeedData.kt) to satisfy the "50 different types of
 * houses" requirement.
 */
@Entity(tableName = "listings")
data class Listing(
    @PrimaryKey(autoGenerate = true) val listingId: Int = 0,
    val title: String,
    val priceBWP: Int,
    val location: String,       // Gaborone area, e.g. Block 8, Extension 12
    val type: String,           // e.g. Single Room, Bachelor Flat, Shared House
    val amenities: String,      // comma separated: WiFi, Water, Electricity...
    val availableFrom: String,  // ISO date yyyy-MM-dd
    val depositBWP: Int,
    val imageResId: Int,        // drawable resource id for listing photo
    val status: String = "AVAILABLE" // AVAILABLE or RESERVED
)
