package fr.istic.mob.starcf.api.core.workers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import fr.istic.mob.starcf.api.contract.StarContract
import fr.istic.mob.starcf.api.core.watchers.CalendarWatcher
import fr.istic.mob.starcf.api.database.BusScheduleApplication
import fr.istic.mob.starcf.api.database.entity.*
import fr.istic.mob.starcf.api.utils.TextFileToEntity
import fr.istic.mob.starcf.api.utils.ZipFileManager
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

class DataPersistenceWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    private val sharedPreferences: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(StarContract.AUTHORITY, Context.MODE_PRIVATE)
    }

    companion object {
        const val Progress = "Progress"
        const val TAG = "Data Persistence"
        private const val delayDuration = 1L
    }

    private val database: BusScheduleApplication by lazy {
        BusScheduleApplication(applicationContext)
    }

    override suspend fun doWork(): Result {
        val calendarName = sharedPreferences.getString(CalendarWatcher.NEW_FILE_NAME, null)
        val path: String = applicationContext.getExternalFilesDir(null)
            .toString() + File.separator.toString() + calendarName
        val directoryName = path.removeSuffix(".zip")

        val firstUpdate = workDataOf(Progress to 0)
        val lastUpdate = workDataOf(Progress to 100)

        var result = Result.failure()

        setProgress(firstUpdate)
        try {
            ZipFileManager.zipFile(path).use { zip ->
                zip.entries().asSequence()
                    .forEach { entry ->
                        zip.getInputStream(entry)
                            .use { input ->
                                val filePath = directoryName + File.separator + entry.name
                                val bufferedOutputStream =
                                    BufferedOutputStream(FileOutputStream(filePath))
                                var read: Int
                                while (input.read(ZipFileManager.BUFFER_SIZE)
                                        .also { read = it } != -1
                                ) {
                                    bufferedOutputStream.write(ZipFileManager.BUFFER_SIZE, 0, read)
                                }
                                bufferedOutputStream.close()

                                // Persist the unzipped file
                                val list: List<Any> = TextFileToEntity(filePath).entities()
                                when (entry.name.removeSuffix(".txt")) {
                                    StarContract.CALENDAR -> {
                                        setProgress(firstUpdate)
                                        val calendars = list as List<Calendar>
                                        database.calendarRepository.insert(calendars)
                                        Log.d(TAG, "Storing calendar...")
                                        val totalSize = list.size
                                        if (totalSize > 0) {
                                            for (i in 0..totalSize) {
                                                val percentage = (i * 100).div(totalSize)
                                                val currentUpdate =
                                                    workDataOf(Progress to percentage)
                                                setProgress(currentUpdate)
                                            }
                                        }
                                        setProgress(lastUpdate)
                                    }
                                    StarContract.ROUTES -> {
                                        setProgress(firstUpdate)
                                        val routes = list as List<BusRoute>
                                        database.routeRepository.insert(routes)
                                        val totalSize = list.size
                                        if (totalSize > 0) {
                                            for (i in 0..totalSize) {
                                                val percentage = (i * 100).div(totalSize)
                                                val currentUpdate =
                                                    workDataOf(Progress to percentage)
                                                setProgress(currentUpdate)
                                            }
                                        }
                                        Log.d(TAG, "Storing routes...")
                                        setProgress(lastUpdate)
                                    }
                                    StarContract.STOPS -> {
                                        setProgress(firstUpdate)
                                        val stops = list as List<Stop>
                                        database.stopRepository.insert(stops)
                                        val totalSize = list.size
                                        if (totalSize > 0) {
                                            for (i in 0..totalSize) {
                                                val percentage = (i * 100).div(totalSize)
                                                val currentUpdate =
                                                    workDataOf(Progress to percentage)
                                                setProgress(currentUpdate)
                                            }
                                        }
                                        Log.d(TAG, "Storing routes...")
                                        setProgress(lastUpdate)
                                    }
                                    StarContract.STOP_TIMES -> {
                                        setProgress(firstUpdate)
                                        val stopTimes = list as List<StopTime>
                                        database.stopTimeRepository.insert(stopTimes)
                                        Log.d(TAG, "Storing Stop times...")
                                        val totalSize = list.size
                                        if (totalSize > 0) {
                                            for (i in 0..totalSize) {
                                                val percentage = (i * 100).div(totalSize)
                                                val currentUpdate =
                                                    workDataOf(Progress to percentage)
                                                setProgress(currentUpdate)
                                            }
                                        }
                                        Log.d(TAG, "Storing routes...")
                                        setProgress(lastUpdate)
                                    }
                                    StarContract.TRIPS -> {
                                        setProgress(firstUpdate)
                                        val trips = list as List<Trip>
                                        database.tripRepository.insert(trips)
                                        Log.d(TAG, "Storing trips...")
                                        val totalSize = list.size
                                        if (totalSize > 0) {
                                            for (i in 0..totalSize) {
                                                val percentage = (i * 100).div(totalSize)
                                                val currentUpdate =
                                                    workDataOf(Progress to percentage)
                                                setProgress(currentUpdate)
                                            }
                                        }
                                        Log.d(TAG, "Storing routes...")
                                        setProgress(lastUpdate)
                                    }
                                }

                            }
                    }
                Log.d(TAG, "Persistence completed !")
                result = Result.success()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Failure", e)
            return Result.failure()
        }

        sharedPreferences.edit {
            putString(CalendarWatcher.NEW_FILE_NAME, null)
            putString(CalendarWatcher.OLD_FIELD_ID, CalendarWatcher.fieldId)
            apply()
        }
        return result
    }


}