package fr.istic.coulibaly.fazul.horairesbus.api.database.repository

import androidx.annotation.WorkerThread
import fr.istic.coulibaly.fazul.horairesbus.api.database.dao.TripDao
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.Trip
import kotlinx.coroutines.flow.Flow

class TripRepository(private val tripDao: TripDao) {
    val allTrips: Flow<List<Trip>> = tripDao.getAll()

    suspend fun insert(trip: Trip) {
        tripDao.insert(trip)
    }

    suspend fun insertAll(trips: List<Trip>) {
        tripDao.insertAll(trips)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun delete(trip: Trip) {
        tripDao.delete(trip)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteAll() {
        tripDao.deleteAll()
    }
}