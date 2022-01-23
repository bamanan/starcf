package fr.istic.mob.starcf.api.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import fr.istic.mob.starcf.api.contract.StarContract
import fr.istic.mob.starcf.api.database.entity.BusRoute

@Dao
interface BusRouteDao : DaoBase<BusRoute> {

    @Query("SELECT * FROM  ${StarContract.BusRoutes.CONTENT_PATH}")
    override fun all(): LiveData<List<BusRoute>>

    @Query("SELECT * FROM  ${StarContract.BusRoutes.CONTENT_PATH}")
    override fun list(): List<BusRoute>

    /**
     * Returns a cursor on routes
     *
     * @return a Cursor which list the result from the database
     */
    @Query(
        "SELECT DISTINCT ${StarContract.BusRoutes.BusRouteColumns.ID}, ${StarContract.BusRoutes.BusRouteColumns.SHORT_NAME} , ${StarContract.BusRoutes.BusRouteColumns.LONG_NAME}, ${StarContract.BusRoutes.BusRouteColumns.COLOR}, ${StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR}, ${StarContract.BusRoutes.BusRouteColumns.DESCRIPTION}, ${StarContract.BusRoutes.BusRouteColumns.TYPE} FROM ${StarContract.BusRoutes.CONTENT_PATH} ORDER BY ${StarContract.BusRoutes.BusRouteColumns.ID}"
    )
    fun routesByCursor(): Cursor?

    /**
     * Fetch routes for a given stop
     * @param stop_name Stop Name
     *
     * @return a Cursor which list the result from the database
     */
    @Query(
        "SELECT DISTINCT  ${StarContract.BusRoutes.CONTENT_PATH}.${StarContract.BusRoutes.BusRouteColumns.ID}, " +
                "${StarContract.BusRoutes.CONTENT_PATH}.${StarContract.BusRoutes.BusRouteColumns.SHORT_NAME}, " +
                "${StarContract.BusRoutes.CONTENT_PATH}.${StarContract.BusRoutes.BusRouteColumns.LONG_NAME}, " +
                "${StarContract.BusRoutes.CONTENT_PATH}.${StarContract.BusRoutes.BusRouteColumns.COLOR}, " +
                "${StarContract.BusRoutes.CONTENT_PATH}.${StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR}, " +
                "${StarContract.BusRoutes.CONTENT_PATH}.${StarContract.BusRoutes.BusRouteColumns.DESCRIPTION}, " +
                "${StarContract.BusRoutes.CONTENT_PATH}.${StarContract.BusRoutes.BusRouteColumns.TYPE} " +
                "FROM ${StarContract.BusRoutes.CONTENT_PATH}, ${StarContract.Trips.CONTENT_PATH}, ${StarContract.Stops.CONTENT_PATH}, ${StarContract.StopTimes.CONTENT_PATH} " +
                "WHERE ${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.NAME}=:stopName " +
                "AND ${StarContract.BusRoutes.CONTENT_PATH}.${StarContract.BusRoutes.BusRouteColumns.ID}=${StarContract.Trips.CONTENT_PATH}.${StarContract.Trips.TripColumns.ROUTE_ID} " +
                "AND ${StarContract.Trips.CONTENT_PATH}.${StarContract.Trips.TripColumns.ID}=${StarContract.StopTimes.CONTENT_PATH}.${StarContract.StopTimes.StopTimeColumns.TRIP_ID} " +
                "AND ${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.ID}=${StarContract.StopTimes.CONTENT_PATH}.${StarContract.StopTimes.StopTimeColumns.STOP_ID} " +
                "ORDER BY ${StarContract.BusRoutes.CONTENT_PATH}.${StarContract.BusRoutes.BusRouteColumns.ID}"
    )
    fun routesForStopByCursor(stopName: String): Cursor?

    @Query("DELETE FROM  ${StarContract.BusRoutes.CONTENT_PATH}")
    override fun deleteAll()
}