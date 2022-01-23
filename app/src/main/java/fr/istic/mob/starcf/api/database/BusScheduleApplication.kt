package fr.istic.mob.starcf.api.database

import android.app.Application
import android.content.Context
import fr.istic.mob.starcf.api.database.repository.*

class BusScheduleApplication(context: Context) : Application() {
    private val database: AppDatabase by lazy { AppDatabase.database(context) }
    val routeRepository: BusRouteRepository by lazy { BusRouteRepository(database.busRouteDao()) }
    val calendarRepository: CalendarRepository by lazy { CalendarRepository(database.calendarDao()) }
    val stopRepository: StopRepository by lazy { StopRepository(database.stopDao()) }
    val stopTimeRepository: StopTimeRepository by lazy { StopTimeRepository(database.stopTimeDao()) }
    val tripRepository: TripRepository by lazy { TripRepository(database.tripDao()) }
}