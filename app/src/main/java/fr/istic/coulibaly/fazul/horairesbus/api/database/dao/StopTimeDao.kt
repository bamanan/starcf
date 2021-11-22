package fr.istic.coulibaly.fazul.horairesbus.api.database.dao

import androidx.room.Dao
import androidx.room.Query
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.StopTime

@Dao
interface StopTimeDao : DaoBase<StopTime> {
    @Query("SELECT * FROM " + StarContract.StopTimes.CONTENT_PATH)
    override fun getAll(): List<StopTime>

    @Query("DELETE FROM " + StarContract.StopTimes.CONTENT_PATH)
    override fun deleteAll(): Boolean
}