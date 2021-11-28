package fr.istic.coulibaly.fazul.horairesbus.api.database

import android.app.Application
import fr.istic.coulibaly.fazul.horairesbus.api.database.repository.*

class BusScheduleApplication : Application() {
    val database by lazy { AppDatabase.database(this) }
    val busRouteRepository by lazy { BusRouteRepository(database.busRouteDao()) }
    val calendarRepository by lazy { CalendarRepository(database.calendarDao()) }
    val stopRepository by lazy { StopRepository(database.stopDao()) }
    val stopTimeRepository by lazy { StopTimeRepository(database.stopTimeDao()) }
    val tripRepository by lazy { TripRepository(database.tripDao()) }

}