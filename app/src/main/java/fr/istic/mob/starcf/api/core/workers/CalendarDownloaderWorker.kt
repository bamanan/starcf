package fr.istic.mob.starcf.api.core.workers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import fr.istic.mob.starcf.api.contract.StarContract
import fr.istic.mob.starcf.api.core.services.ApiAdapter
import fr.istic.mob.starcf.api.core.watchers.CalendarWatcher
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.*


class CalendarDownloaderWorker(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val TAG = "Calendar Download"
        const val FILE_DOWNLOADED = "file_downloaded"
        const val Progress = "Progress"
        private const val delayDuration = 1L
    }

    private val sharedPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(StarContract.AUTHORITY, Context.MODE_PRIVATE)
    }

    override suspend fun doWork(): Result {
        val fileName = sharedPreferences.getString(CalendarWatcher.NEW_FILE_NAME, null)
        val firstUpdate = workDataOf(Progress to 0)
        val lastUpdate = workDataOf(Progress to 100)

        var result: Result = Result.failure()

        if (fileName != null) {
            val call: Call<ResponseBody> =
                ApiAdapter.apiDownloadClient.downloadLatestCalendar(fileName)

            Log.i(TAG, "Downloading..." + fileName)

            val response: Response<ResponseBody> = call.execute()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()

                setProgress(firstUpdate)

                try {
                    val path: String = applicationContext.getExternalFilesDir(null)
                        .toString() + File.separator.toString() + fileName
                    val outputFile = File(path)
                    var inputStream: InputStream? = null
                    var outputStream: OutputStream? = null

                    try {
                        val fileReader = ByteArray(5 * 1024)
                        val fileSize = body!!.contentLength()
                        var fileSizeDownloaded: Long = 0
                        inputStream = body!!.byteStream()
                        outputStream = FileOutputStream(outputFile)
                        while (true) {
                            val read: Int = inputStream.read(fileReader)
                            if (read == -1) {
                                break
                            }
                            outputStream.write(fileReader, 0, read)
                            fileSizeDownloaded += read.toLong()

                            // Progress update
                            val percentage = (fileSizeDownloaded * 100).div(fileSize)
                            val currentUpdate = workDataOf(Progress to percentage)
                            setProgress(currentUpdate)
                            Log.d(TAG, "Downloaded : $fileSizeDownloaded of $fileSize")
                        }

                        sharedPreferences.edit {
                            putBoolean(FILE_DOWNLOADED, true)
                            apply()
                        }
                        result = Result.success()
                        outputStream.flush()
                    } catch (e: IOException) {
                        sharedPreferences.edit {
                            putBoolean(FILE_DOWNLOADED, false)
                            apply()
                        }

                    } finally {
                        inputStream!!.close()
                        outputStream!!.close()
                    }
                } catch (e: IOException) {
                    Log.d(TAG, "Error ....", e)
                }
                delay(delayDuration)
                setProgress(lastUpdate)
            }
        }

        return result
    }
}
