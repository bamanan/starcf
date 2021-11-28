package fr.istic.coulibaly.fazul.horairesbus.api.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.Calendar
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao : DaoBase<Calendar> {

    @Query("SELECT * FROM " + StarContract.Calendar.CONTENT_PATH)
    override fun getAll(): Flow<List<Calendar>>

    @Query("SELECT * FROM " + StarContract.Calendar.CONTENT_PATH)
    fun getCalendarWithCursor(): Cursor

    @Query("DELETE FROM " + StarContract.Calendar.CONTENT_PATH)
    override fun deleteAll()
}