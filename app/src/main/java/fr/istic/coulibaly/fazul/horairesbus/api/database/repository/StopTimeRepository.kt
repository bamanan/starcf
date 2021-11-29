package fr.istic.coulibaly.fazul.horairesbus.api.database.repository

import androidx.annotation.WorkerThread
import fr.istic.coulibaly.fazul.horairesbus.api.database.dao.StopTimeDao
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.StopTime
import kotlinx.coroutines.flow.Flow

class StopTimeRepository(private val stopTimeDao: StopTimeDao) {
    val allStopTimes: List<StopTime> = stopTimeDao.getAll()

    suspend fun insert(stopTime: StopTime) {
        stopTimeDao.insert(stopTime)
    }

    suspend fun insertAll(stopTimes: List<StopTime>) {
        stopTimeDao.insertAll(stopTimes)
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