package fr.istic.mob.starcf.api.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import fr.istic.mob.starcf.api.contract.StarContract
import fr.istic.mob.starcf.api.database.entity.Calendar

@Dao
interface CalendarDao : DaoBase<Calendar> {

    @Query("SELECT * FROM  ${StarContract.Calendar.CONTENT_PATH}")
    override fun all(): LiveData<List<Calendar>>

    @Query("SELECT * FROM  ${StarContract.Calendar.CONTENT_PATH}")
    override fun list(): List<Calendar>

    @Query(
        "SELECT DISTINCT ${StarContract.Calendar.CalendarColumns.ID}, ${StarContract.Calendar.CalendarColumns.START_DATE} , ${StarContract.Calendar.CalendarColumns.END_DATE}, ${StarContract.Calendar.CalendarColumns.MONDAY}, ${StarContract.Calendar.CalendarColumns.TUESDAY}, ${StarContract.Calendar.CalendarColumns.WEDNESDAY}, ${StarContract.Calendar.CalendarColumns.THURSDAY} , ${StarContract.Calendar.CalendarColumns.THURSDAY} , ${StarContract.Calendar.CalendarColumns.FRIDAY}, ${StarContract.Calendar.CalendarColumns.SATURDAY}, ${StarContract.Calendar.CalendarColumns.SUNDAY} FROM ${StarContract.Calendar.CONTENT_PATH} ORDER BY ${StarContract.Calendar.CalendarColumns.ID}"
    )
    fun calendarsByCursor(): Cursor?

    @Query("DELETE FROM  ${StarContract.Calendar.CONTENT_PATH}")
    override fun deleteAll()
}