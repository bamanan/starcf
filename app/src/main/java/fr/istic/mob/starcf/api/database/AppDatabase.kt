package fr.istic.mob.starcf.api.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.istic.mob.starcf.api.database.dao.*
import fr.istic.mob.starcf.api.database.entity.*

@Database(
    entities = [
        BusRoute::class,
        Calendar::class,
        Stop::class,
        StopTime::class,
        Trip::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun busRouteDao(): BusRouteDao
    abstract fun calendarDao(): CalendarDao
    abstract fun stopDao(): StopDao
    abstract fun stopTimeDao(): StopTimeDao
    abstract fun tripDao(): TripDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun database(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "star_database"
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }
    }
}