package fr.istic.coulibaly.fazul.horairesbus.api.database.dao

import androidx.room.Dao
import androidx.room.Query
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao : DaoBase<Trip> {
    @Query("SELECT * FROM " + StarContract.Trips.CONTENT_PATH)
    override fun getAll(): Flow<List<Trip>>

    @Query("DELETE FROM " + StarContract.Trips.CONTENT_PATH)
    override fun deleteAll()
}