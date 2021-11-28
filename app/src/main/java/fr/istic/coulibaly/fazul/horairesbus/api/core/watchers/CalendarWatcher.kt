package fr.istic.coulibaly.fazul.horairesbus.api.core.watchers

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.content.edit
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.core.services.ApiAdapter
import fr.istic.coulibaly.fazul.horairesbus.api.core.services.ApiResponse
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarWatcher(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(StarContract.AUTHORITY, Context.MODE_PRIVATE)
    }


    companion object {
        const val NEW_DATA_AVAILABLE = "data_available"
        const val NEW_FILE_NAME = "file_name"
        private const val OLD_FIELD_ID = "old_field_id"
    }

    override suspend fun doWork(): Result {
        val oldFieldId = sharedPreferences.getString(OLD_FIELD_ID, null)


        return try {
            val response = ApiAdapter.apiClient.getLatestCalendar()
            val apiResponse = response.body()

            if (response.isSuccessful && apiResponse != null) {

                val fields: List<ApiResponse.Record.Fields> =
                    apiResponse.records.map { it.fields }

                try {
                    fields.forEach { field ->
                        if (field.id != oldFieldId) {
                            val beginValidity = field.debutvalidite
                            val endValidity = field.finvalidite
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val current =
                                    LocalDateTime.now()
                                val formatter =
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                val formatted = current.format(formatter)

                                if (formatted >= beginValidity && formatted <= endValidity) {
                                    sharedPreferences.edit {
                                        putString(NEW_FILE_NAME, field.fichier.filename)
                                        putBoolean(NEW_DATA_AVAILABLE, true)
                                        putString(OLD_FIELD_ID, field.id)
                                        commit()
                                    }
                                    return@forEach
                                } else {

                                    sharedPreferences.edit {
                                        putBoolean(NEW_DATA_AVAILABLE, false)
                                        commit()
                                    }
                                }
                            }
                        }
                    }

                } catch (e: Exception) {
                    Log.e("Calendar Watcher", "Comparison error", e)
                }
                Result.success()
            } else {
                Result.retry()
            }

        } catch (e: Exception) {
            Result.failure()
        }
    }
}