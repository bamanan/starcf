package fr.istic.mob.starcf.api.database.repository

import android.database.Cursor
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import fr.istic.mob.starcf.api.database.dao.CalendarDao
import fr.istic.mob.starcf.api.database.entity.Calendar

class CalendarRepository(private val calendarDao: CalendarDao) {
    val calendarsLiveData: LiveData<List<Calendar>> = calendarDao.all()

    val calendars: List<Calendar> = calendarDao.list()

    val calendarsCursor: Cursor? = calendarDao.calendarsByCursor()

    @WorkerThread
    fun insert(calendar: Calendar) {
        calendarDao.insert(calendar)
    }

    @WorkerThread
    fun insert(calendars: List<Calendar>) {
        calendarDao.insert(*calendars.toTypedArray())
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun delete(calendar: Calendar) {
        calendarDao.delete(calendar)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteAll() {
        calendarDao.deleteAll()
    }
}