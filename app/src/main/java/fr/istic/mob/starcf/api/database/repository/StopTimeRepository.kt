package fr.istic.mob.starcf.api.database.repository

import android.database.Cursor
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import fr.istic.mob.starcf.api.database.dao.StopTimeDao
import fr.istic.mob.starcf.api.database.entity.StopTime

class StopTimeRepository(private val stopTimeDao: StopTimeDao) {
    val stopTimesLiveData: LiveData<List<StopTime>> = stopTimeDao.all()

    val stopTimes: List<StopTime> = stopTimeDao.list()

    val stopTimesByCursor: Cursor? = stopTimeDao.stopTimesByCursor()

    fun routeDetailsCursor(tripId: String, arrivalTime: String): Cursor? {
        return stopTimeDao.routeDetails(tripId = tripId, arrivalTime = arrivalTime)
    }

    fun stopTimesForMondayCursor(
        routeId: String,
        directionId: String,
        stopId: String,
        arrivalTime: String
    ): Cursor? {
        return stopTimeDao.stopTimesForMondayCursor(
            routeId = routeId,
            directionId = directionId,
            stopId = stopId,
            arrivalTime = arrivalTime
        )
    }

    fun stopTimesForTuesdayCursor(
        routeId: String,
        directionId: String,
        stopId: String,
        arrivalTime: String
    ): Cursor? {
        return stopTimeDao.stopTimesForTuesdayCursor(
            routeId = routeId,
            directionId = directionId,
            stopId = stopId,
            arrivalTime = arrivalTime
        )
    }

    fun stopTimesForWednesdayCursor(
        routeId: String,
        directionId: String,
        stopId: String,
        arrivalTime: String
    ): Cursor? {
        return stopTimeDao.stopTimesForWednesdayCursor(
            routeId = routeId,
            directionId = directionId,
            stopId = stopId,
            arrivalTime = arrivalTime
        )
    }

    fun stopTimesForThursdayCursor(
        routeId: String,
        directionId: String,
        stopId: String,
        arrivalTime: String
    ): Cursor? {
        return stopTimeDao.stopTimesForThursdayCursor(
            routeId = routeId,
            directionId = directionId,
            stopId = stopId,
            arrivalTime = arrivalTime
        )
    }

    fun stopTimesForFridayCursor(
        routeId: String,
        directionId: String,
        stopId: String,
        arrivalTime: String
    ): Cursor? {
        return stopTimeDao.stopTimesForFridayCursor(
            routeId = routeId,
            directionId = directionId,
            stopId = stopId,
            arrivalTime = arrivalTime
        )
    }

    fun stopTimesForSaturdayCursor(
        routeId: String,
        directionId: String,
        stopId: String,
        arrivalTime: String
    ): Cursor? {
        return stopTimeDao.stopTimesForSaturdayCursor(
            routeId = routeId,
            directionId = directionId,
            stopId = stopId,
            arrivalTime = arrivalTime
        )
    }

    fun stopTimesForSundayCursor(
        routeId: String,
        directionId: String,
        stopId: String,
        arrivalTime: String
    ): Cursor? {
        return stopTimeDao.stopTimesForSundayCursor(
            routeId = routeId,
            directionId = directionId,
            stopId = stopId,
            arrivalTime = arrivalTime
        )
    }

    @WorkerThread
    fun insert(stopTime: StopTime) {
        stopTimeDao.insert(stopTime)
    }

    @WorkerThread
    fun insert(stopTimes: List<StopTime>) {
        stopTimeDao.insert(*stopTimes.toTypedArray())
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun delete(stopTime: StopTime) {
        stopTimeDao.delete(stopTime)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteAll() {
        stopTimeDao.deleteAll()
    }
}