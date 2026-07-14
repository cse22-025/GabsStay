package com.example.studentnest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Student::class, Listing::class, Reservation::class, ChatMessage::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao
    abstract fun listingDao(): ListingDao
    abstract fun reservationDao(): ReservationDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "studentnest_db"
                )
                    .addCallback(seedCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Pre-seeds 50 students and 50 listings the first time the
         * database file is created, per the assignment's data volume
         * requirement.
         */
        private fun seedCallback(context: Context) = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val database = getDatabase(context)
                    database.studentDao().insertAll(SeedData.students())
                    database.listingDao().insertAll(SeedData.listings())
                }
            }
        }
    }
}
