package fr.istic.coulibaly.fazul.horairesbus.api.core.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.core.BusScheduleApplication
import fr.istic.coulibaly.fazul.horairesbus.api.database.AppDatabase
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.*
import fr.istic.coulibaly.fazul.horairesbus.api.utils.TextFileToEntity
import java.io.File

class DataPersistenceWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {


    override suspend fun doWork(): Result {
        val directoryName = inputData.getString("fileName")

        StarContract.FILES.forEach { fileName ->
            val path: String = applicationContext.getExternalFilesDir(null)
                .toString() + File.separator.toString() + directoryName!!.removeSuffix(".zip") + File.separator.toString() +  fileName + ".txt"

            val busScheduleApplication = BusScheduleApplication()

            val list = TextFileToEntity(path).entities()

            when (fileName) {
                StarContract.CALENDAR -> {
                    busScheduleApplication.calendarRepository.insertAll(list as List<Calendar>)
                }

                StarContract.ROUTES -> {
                    busScheduleApplication.busRouteRepository.insertAll(list as List<BusRoute>)
                }
                StarContract.STOPS -> {
                    busScheduleApplication.stopRepository.insertAll(list as List<Stop>)
                }
                StarContract.STOPTIMES -> {
                    busScheduleApplication.stopTimeRepository.insertAll(list as List<StopTime>)
                }
                StarContract.TRIPS -> {
                    busScheduleApplication.tripRepository.insertAll(list as List<Trip>)
                }
            }
        }

        return Result.success()
    }


}