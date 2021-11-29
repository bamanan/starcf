package fr.istic.coulibaly.fazul.horairesbus.api.database.repository

import androidx.annotation.WorkerThread
import fr.istic.coulibaly.fazul.horairesbus.api.database.dao.BusRouteDao
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.BusRoute


class BusRouteRepository(private val busRouteDao: BusRouteDao) {

    fun allBusRoutes(): List<BusRoute> = busRouteDao.getAll()

    fun insert(busRoute: BusRoute) {
        busRouteDao.insert(busRoute)
    }

    fun insertAll(busRoutes: List<BusRoute>) {
        busRouteDao.insertAll(busRoutes)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun delete(busRoute: BusRoute) {
        busRouteDao.delete(busRoute)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteAll() {
        busRouteDao.deleteAll()
    }
}