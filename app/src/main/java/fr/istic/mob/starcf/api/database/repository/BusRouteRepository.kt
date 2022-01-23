package fr.istic.mob.starcf.api.database.repository

import android.database.Cursor
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import fr.istic.mob.starcf.api.database.dao.BusRouteDao
import fr.istic.mob.starcf.api.database.entity.BusRoute


class BusRouteRepository(private val busRouteDao: BusRouteDao) {

    val routesLiveData: LiveData<List<BusRoute>> = busRouteDao.all()

    val routes: List<BusRoute> = busRouteDao.list()

    val routesCursor: Cursor? = busRouteDao.routesByCursor()

    fun routesForStopByCursor(stopName: String): Cursor? {
        return busRouteDao.routesForStopByCursor(stopName)
    }

    @WorkerThread
    fun insert(busRoute: BusRoute) {
        busRouteDao.insert(busRoute)
    }

    @WorkerThread
    fun insert(busRoutes: List<BusRoute>) {
        busRouteDao.insert(*busRoutes.toTypedArray())
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