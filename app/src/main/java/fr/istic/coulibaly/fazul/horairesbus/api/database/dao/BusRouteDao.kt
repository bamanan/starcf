package fr.istic.coulibaly.fazul.horairesbus.api.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.BusRoute
import kotlinx.coroutines.flow.Flow

@Dao
interface BusRouteDao : DaoBase<BusRoute> {
    @Query("SELECT * FROM " + StarContract.BusRoutes.CONTENT_PATH)
    override fun getAll(): Flow<List<BusRoute>>

    @Query("SELECT * FROM " + StarContract.BusRoutes.CONTENT_PATH)
    fun getBusRoutesWithCursor(): Cursor

    @Query("DELETE FROM " + StarContract.BusRoutes.CONTENT_PATH)
    override fun deleteAll()
}