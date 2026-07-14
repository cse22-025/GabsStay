package com.example.studentnest.data

import com.example.studentnest.R

/**
 * Generates the initial 50 students and 50 listing records used to
 * satisfy the assignment's data-volume requirements. Kept in code
 * (rather than 50 hand-typed rows) so the dataset is easy to regenerate
 * or resize while marking.
 */
object SeedData {

    private val gaboroneAreas = listOf(
        "Block 8", "Block 9", "Block 10", "Extension 12", "Extension 9",
        "Village", "Broadhurst", "Gaborone West", "Bontleng", "Old Naledi",
        "Phakalane", "Mogoditshane", "Tlokweng", "Ledumang", "Gaborone North"
    )

    private val houseTypes = listOf(
        "Single Room", "Bachelor Flat", "Shared House", "One-Bedroom Apartment",
        "Two-Bedroom Apartment", "Backyard Room", "Boarding House Room", "Studio Flat"
    )

    private val amenitySets = listOf(
        "WiFi, Water, Electricity",
        "WiFi, Water, Electricity, Furnished",
        "Water, Electricity, Security",
        "WiFi, Water, Electricity, Parking",
        "Water, Electricity, Shared Kitchen",
        "WiFi, Water, Electricity, Security, Parking"
    )

    fun students(): List<Student> {
        return (1..50).map { i ->
            Student(
                fullName = "Student $i",
                email = "student$i@studentnest.bw",
                password = "pass$i23",
                role = "STUDENT",
                campus = "Gaborone"
            )
        }
    }

    fun listings(): List<Listing> {
        return (1..50).map { i ->
            val area = gaboroneAreas[i % gaboroneAreas.size]
            val type = houseTypes[i % houseTypes.size]
            val amenities = amenitySets[i % amenitySets.size]
            val price = 800 + (i % 12) * 150       // BWP 800 - BWP 2450
            val deposit = price                     // one month deposit
            val month = (1 + (i % 6)).toString().padStart(2, '0')
            val day = (1 + (i % 27)).toString().padStart(2, '0')
            Listing(
                title = "$type - $area #$i",
                priceBWP = price,
                location = area,
                type = type,
                amenities = amenities,
                availableFrom = "2026-$month-$day",
                depositBWP = deposit,
                imageResId = R.drawable.ic_listing_placeholder
            )
        }
    }
}
