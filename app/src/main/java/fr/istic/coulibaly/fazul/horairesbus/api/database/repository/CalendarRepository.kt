package fr.istic.coulibaly.fazul.horairesbus.api.database.repository

import androidx.annotation.WorkerThread
import fr.istic.coulibaly.fazul.horairesbus.api.database.dao.CalendarDao
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.Calendar
import kotlinx.coroutines.flow.Flow

class CalendarRepository(private val calendarDao: CalendarDao) {
    val all: List<Calendar> = calendarDao.getAll()

    suspend fun insert(calendar: Calendar) {
        calendarDao.insert(calendar)
    }

    suspend fun insertAll(calendarItems: List<Calendar>) {
        calendarDao.insertAll(calendarItems)
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