package fr.istic.coulibaly.fazul.horairesbus.api.core.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.core.services.ApiAdapter
import fr.istic.coulibaly.fazul.horairesbus.api.core.watchers.CalendarWatcher
import fr.istic.coulibaly.fazul.horairesbus.api.utils.ZipFileManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class CalendarDownloaderWorker(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val TAG = "Calendar Download"
        const val FILE_DOWNLOADED = "files_downloaded"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(StarContract.AUTHORITY, Context.MODE_PRIVATE)
    }

    override suspend fun doWork(): Result {
        val fileName = sharedPreferences.getString(CalendarWatcher.NEW_FILE_NAME, null).toString()

        var result: Result = Result.success()

        val call: Call<ResponseBody> =
            ApiAdapter.apiDownloadClient.downloadLatestCalendar(fileName)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val downloaded = writeFileOnDisk(response.body()!!, fileName)
                    result = Result.success()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                result = Result.failure()
            }
        }
        )

        return result
    }

    private fun writeFileOnDisk(body: ResponseBody, fileName: String): Boolean {
        return try {
            val path: String = applicationContext.getExternalFilesDir(null)
                .toString() + File.separator.toString() + fileName
            val outputFile = File(path)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(outputFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
//                    Log.d("TAG", "file download: $fileSizeDownloaded of $fileSize")
                }

                ZipFileManager.unzip(outputFile)
                sharedPreferences.edit {
                    putBoolean(FILE_DOWNLOADED, true)
                    commit()
                }

                outputStream.flush()

                true
            } catch (e: IOException) {
                sharedPreferences.edit {
                    putBoolean(FILE_DOWNLOADED, false)
                    commit()
                }
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }
}
