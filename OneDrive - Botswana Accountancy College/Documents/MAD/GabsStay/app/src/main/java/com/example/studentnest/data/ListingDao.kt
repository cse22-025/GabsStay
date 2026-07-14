package com.example.studentnest.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ListingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(listings: List<Listing>)

    @Query("SELECT COUNT(*) FROM listings")
    suspend fun count(): Int

    @Query("SELECT * FROM listings ORDER BY listingId ASC")
    fun getAllListings(): LiveData<List<Listing>>

    @Query("SELECT * FROM listings WHERE listingId = :id")
    suspend fun getById(id: Int): Listing?

    /**
     * Smart Filtering & Alerts feature: filters by price range, location,
     * and availability date in a single query. Blank/zero parameters are
     * treated as "no filter" for that field.
     */
    @Query(
        """
        SELECT * FROM listings
        WHERE priceBWP BETWEEN :minPrice AND :maxPrice
        AND (:location = '' OR location LIKE '%' || :location || '%')
        AND (:availableFrom = '' OR availableFrom >= :availableFrom)
        ORDER BY priceBWP ASC
        """
    )
    suspend fun filterListings(
        minPrice: Int,
        maxPrice: Int,
        location: String,
        availableFrom: String
    ): List<Listing>

    @Update
    suspend fun update(listing: Listing)

    @Query("UPDATE listings SET status = :status WHERE listingId = :id")
    suspend fun setStatus(id: Int, status: String)
}
