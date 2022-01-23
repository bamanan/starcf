package fr.istic.mob.starcf.api.database.repository

import android.database.Cursor
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import fr.istic.mob.starcf.api.database.dao.TripDao
import fr.istic.mob.starcf.api.database.entity.Trip

class TripRepository(private val tripDao: TripDao) {
    val tripsLiveData: LiveData<List<Trip>> = tripDao.all()

    val trips: List<Trip> = tripDao.list()

    fun tripsByRouteCursor(routeId: String): Cursor? {
        return tripDao.tripsByRouteCursor(routeId)
    }

    @WorkerThread
    fun insert(trip: Trip) {
        tripDao.insert(trip)
    }

    @WorkerThread
    fun insert(trips: List<Trip>) {
        tripDao.insert(*trips.toTypedArray())
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