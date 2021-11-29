package fr.istic.coulibaly.fazul.horairesbus.api.database.repository

import androidx.annotation.WorkerThread
import fr.istic.coulibaly.fazul.horairesbus.api.database.dao.StopDao
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.Stop
import kotlinx.coroutines.flow.Flow

class StopRepository(private val stopDao: StopDao) {
    val allStops: List<Stop> = stopDao.getAll()

    suspend fun insert(stop: Stop) {
        stopDao.insert(stop)
    }

    suspend fun insertAll(stops: List<Stop>) {
        stopDao.insertAll(stops)
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