package fr.istic.mob.starcf.api.database.repository

import android.database.Cursor
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import fr.istic.mob.starcf.api.database.dao.StopDao
import fr.istic.mob.starcf.api.database.entity.Stop

class StopRepository(private val stopDao: StopDao) {
    val stopsLiveData: LiveData<List<Stop>> = stopDao.all()

    val stops: List<Stop> = stopDao.list()

    val stopsCursor: Cursor? = stopDao.stopsByCursor()

    fun stopsByRouteAndDirectionCursor(routeId: String, directionId: String): Cursor? {
        return stopDao.stopsByRouteAndDirection(routeId = routeId, directionId = directionId)
    }

    fun stopByNameCursor(search: String): Cursor? {
        return stopDao.stopByNameCursor(search = search)
    }

    @WorkerThread
    fun insert(stop: Stop) {
        stopDao.insert(stop)
    }

    @WorkerThread
    fun insert(stops: List<Stop>) {
        stopDao.insert(*stops.toTypedArray())
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun delete(stop: Stop) {
        stopDao.delete(stop)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteAll() {
        stopDao.deleteAll()
    }
}