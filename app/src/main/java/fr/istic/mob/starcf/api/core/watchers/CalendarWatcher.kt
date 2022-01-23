package fr.istic.mob.starcf.api.core.watchers

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.content.edit
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import fr.istic.mob.starcf.api.contract.StarContract
import fr.istic.mob.starcf.api.core.services.ApiAdapter
import fr.istic.mob.starcf.api.core.services.ApiResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarWatcher(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(StarContract.AUTHORITY, Context.MODE_PRIVATE)
    }

    override suspend fun doWork(): Result {
        val oldFieldId = sharedPreferences.getString(OLD_FIELD_ID, null)
        var result = Result.success()
        return try {
            val response = ApiAdapter.apiClient.getLatestCalendar()
            val apiResponse = response.body()
            if (response.isSuccessful && apiResponse != null) {
                val fields: List<ApiResponse.Record.Fields> =
                    apiResponse.records.map { it.fields }

                //Check if the calendar is different from the old one
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
                                        fieldId = field.id
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
                        } else {
                            sharedPreferences.edit {
                                putBoolean(NEW_DATA_AVAILABLE, false)
                                commit()
                            }
                        }
                    }
                } catch (e: Exception) {
                    result = Result.failure()
                    Log.d(TAG, "Comparison error", e)
                }
            } else {
                result = Result.failure()
            }

            result
        } catch (e: Exception) {
            Result.failure()
        }
    }


    companion object {
        const val NEW_DATA_AVAILABLE = "data_available"
        const val NEW_FILE_NAME = "file_name"
        const val TAG = "Calendar Watcher"
        const val OLD_FIELD_ID = "old_field_id"
        lateinit var fieldId: String
    }


}