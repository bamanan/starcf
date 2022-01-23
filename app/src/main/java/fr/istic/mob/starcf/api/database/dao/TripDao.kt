package fr.istic.mob.starcf.api.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import fr.istic.mob.starcf.api.contract.StarContract
import fr.istic.mob.starcf.api.database.entity.Trip

@Dao
interface TripDao : DaoBase<Trip> {
    @Query("SELECT * FROM  ${StarContract.Trips.CONTENT_PATH}")
    override fun all(): LiveData<List<Trip>>

    @Query("SELECT * FROM  ${StarContract.Trips.CONTENT_PATH}")
    override fun list(): List<Trip>

    @Query(
        "SELECT DISTINCT ${StarContract.Trips.TripColumns.ID}, ${StarContract.Trips.TripColumns.SERVICE_ID} , ${StarContract.Trips.TripColumns.ROUTE_ID}, ${StarContract.Trips.TripColumns.DIRECTION_ID}, ${StarContract.Trips.TripColumns.BLOCK_ID}, ${StarContract.Trips.TripColumns.HEADSIGN}, ${StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE} FROM ${StarContract.Trips.CONTENT_PATH} " +
                "WHERE  ${StarContract.Trips.TripColumns.ROUTE_ID}=:routeId " +
                "ORDER BY ${StarContract.Trips.TripColumns.ID}"
    )
    fun tripsByRouteCursor(routeId: String): Cursor?

    @Query("DELETE FROM  ${StarContract.Trips.CONTENT_PATH}")
    override fun deleteAll()
}