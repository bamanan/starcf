package fr.istic.coulibaly.fazul.horairesbus.api.database.dao

import androidx.room.Dao
import androidx.room.Query
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.Stop
import kotlinx.coroutines.flow.Flow

@Dao
interface StopDao : DaoBase<Stop> {
    @Query("SELECT * FROM " + StarContract.Stops.CONTENT_PATH)
    override fun getAll(): List<Stop>

    @Query("DELETE FROM " + StarContract.Stops.CONTENT_PATH)
    override fun deleteAll()
}