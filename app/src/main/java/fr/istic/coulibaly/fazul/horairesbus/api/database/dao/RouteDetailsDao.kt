package fr.istic.coulibaly.fazul.horairesbus.api.database.dao

import androidx.room.Dao
import androidx.room.Query
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.RouteDetails

@Dao
interface RouteDetailsDao : DaoBase<RouteDetails>{
    @Query("SELECT * FROM " + StarContract.RouteDetails.CONTENT_PATH)
    override fun getAll(): List<RouteDetails>

    @Query("DELETE FROM " + StarContract.RouteDetails.CONTENT_PATH)
    override fun deleteAll(): Boolean
}