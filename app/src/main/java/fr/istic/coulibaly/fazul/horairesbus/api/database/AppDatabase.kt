package fr.istic.coulibaly.fazul.horairesbus.api.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.istic.coulibaly.fazul.horairesbus.api.database.dao.*
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.*

@Database(entities = [
    BusRoute::class,
    Calendar::class,
    RouteDetails::class,
    Stop::class,
    StopTime::class,
    Trip::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun busRouteDao(): BusRouteDao
    abstract fun calendarDao(): CalendarDao
    abstract fun routeDetailsDao(): RouteDetailsDao
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
                    "db_bus_schedule"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}