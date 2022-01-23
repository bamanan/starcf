package fr.istic.mob.starcf.api.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import fr.istic.mob.starcf.api.contract.StarContract
import fr.istic.mob.starcf.api.database.entity.Stop

@Dao
interface StopDao : DaoBase<Stop> {
    @Query("SELECT * FROM  ${StarContract.Stops.CONTENT_PATH}")
    override fun all(): LiveData<List<Stop>>

    @Query("SELECT * FROM  ${StarContract.Stops.CONTENT_PATH}")
    override fun list(): List<Stop>

    @Query(
        "SELECT DISTINCT ${StarContract.Stops.StopColumns.ID}, ${StarContract.Stops.StopColumns.NAME} , ${StarContract.Stops.StopColumns.DESCRIPTION}, ${StarContract.Stops.StopColumns.LATITUDE}, ${StarContract.Stops.StopColumns.LONGITUDE}, ${StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING} FROM ${StarContract.Stops.CONTENT_PATH} ORDER BY ${StarContract.Stops.StopColumns.ID}"
    )
    fun stopsByCursor(): Cursor?

    /**
     * Fetch stops for given route and direction
     * @param routeId Route ID
     * @param directionId Direction
     *
     * @return a Cursor which list the result from the database
     */
    @Query(
        "SELECT DISTINCT ${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.ID}, " +
                "${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.NAME}, " +
                "${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.DESCRIPTION}, " +
                "${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.LATITUDE}, " +
                "${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.LONGITUDE}, " +
                "${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING}, " +
                "${StarContract.Trips.CONTENT_PATH}.${StarContract.Trips.TripColumns.HEADSIGN}, " +
                "${StarContract.Trips.CONTENT_PATH}.${StarContract.Trips.TripColumns.DIRECTION_ID} " +
                "FROM ${StarContract.Stops.CONTENT_PATH}, ${StarContract.StopTimes.CONTENT_PATH} ,  ${StarContract.Trips.CONTENT_PATH} " +
                "WHERE ${StarContract.Stops.CONTENT_PATH}.${StarContract.Trips.TripColumns.ID} = ${StarContract.StopTimes.CONTENT_PATH}.${StarContract.StopTimes.StopTimeColumns.TRIP_ID} " +
                "AND  ${StarContract.StopTimes.CONTENT_PATH}.${StarContract.StopTimes.StopTimeColumns.STOP_ID} = ${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.ID} " +
                "AND ${StarContract.Trips.CONTENT_PATH}.${StarContract.Trips.TripColumns.ROUTE_ID} =:routeId " +
                "AND  ${StarContract.Trips.CONTENT_PATH}.${StarContract.Trips.TripColumns.DIRECTION_ID} =:directionId " +
                "ORDER BY  ${StarContract.StopTimes.CONTENT_PATH}.${StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME}"
    )
    fun stopsByRouteAndDirection(routeId: String, directionId: String): Cursor?

    /**
     * Fetch stops for given route and direction
     * @param search Stop name
     *
     * @return a Cursor which list the result from the database
     */
    @Query(
        "SELECT DISTINCT ${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.NAME} " +
                "FROM ${StarContract.Stops.CONTENT_PATH} " +
                "WHERE ${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.NAME} LIKE '%' || :search || '%' " +
                "ORDER BY ${StarContract.Stops.CONTENT_PATH}.${StarContract.Stops.StopColumns.NAME}"
    )
    fun stopByNameCursor(search: String): Cursor?


    @Query("DELETE FROM  ${StarContract.Stops.CONTENT_PATH}")
    override fun deleteAll()
}