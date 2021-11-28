package fr.istic.coulibaly.fazul.horairesbus

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.core.watchers.CalendarWatcher
import fr.istic.coulibaly.fazul.horairesbus.api.core.workers.CalendarDownloaderWorker
import fr.istic.coulibaly.fazul.horairesbus.api.core.workers.DataPersistenceWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.io.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var workManager: WorkManager

    private val appSharedPreferences: SharedPreferences by lazy {
        this.getSharedPreferences(StarContract.AUTHORITY, Context.MODE_PRIVATE)
    }

    private lateinit var workerConstraints: Constraints

    private lateinit var dataBuilder: Data.Builder

    private lateinit var calendarWatcherRequestOnceBuilder: OneTimeWorkRequest.Builder

    private lateinit var calendarWatcherRequestBuilder: PeriodicWorkRequest.Builder

    private lateinit var downloadCalendarRequestBuilder: OneTimeWorkRequest.Builder

    private lateinit var dataPersistenceWorkerBuilder: OneTimeWorkRequest.Builder

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val downloadButton: Button = findViewById(R.id.button)
        textView = findViewById(R.id.apiResponseView)
        init()


        lifecycleScope.launchWhenCreated {
            launchCalendarWatcherOnce()
            launchCalendarWatcherPeriodic()

            downloadButton.setOnClickListener {
                launchCalendarWatcherOnce()
            }
        }

    }

    private fun init() {
        workManager = WorkManager.getInstance(this@MainActivity)

        workerConstraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        dataBuilder = Data.Builder()

        dataPersistenceWorkerBuilder = OneTimeWorkRequest.Builder(DataPersistenceWorker::class.java)
            .setInputData(dataBuilder.build())
            .setConstraints(workerConstraints)

        downloadCalendarRequestBuilder = OneTimeWorkRequest
            .Builder(CalendarDownloaderWorker::class.java)
            .setInputData(dataBuilder.build())
            .setConstraints(workerConstraints)

        calendarWatcherRequestBuilder = PeriodicWorkRequest
            .Builder(CalendarWatcher::class.java, 30, TimeUnit.MINUTES)
            .setInputData(dataBuilder.build())
            .setConstraints(workerConstraints)

        calendarWatcherRequestOnceBuilder = OneTimeWorkRequest
            .Builder(CalendarWatcher::class.java)
            .setInputData(dataBuilder.build())
            .setConstraints(workerConstraints)
    }

    private fun launchCalendarWatcherOnce() {
        val TAG = "CalendarWatcher"
        val calendarWatcherRequestOnce = calendarWatcherRequestOnceBuilder.build()
        workManager.enqueue(calendarWatcherRequestOnce)
        workManager.getWorkInfoByIdLiveData(calendarWatcherRequestOnce.id)
            .observe(this, { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> {
                        Log.i(TAG, "Enqueued")
                    }
                    WorkInfo.State.BLOCKED -> {
                        Log.e(TAG, "Enqueued")
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        Log.i(TAG, "Success")
                        launchCalendarDownloadAndPersistenceWorkers()
                    }
                    WorkInfo.State.FAILED -> {
                        Log.e(TAG, "Failed")
                    }
                    WorkInfo.State.CANCELLED -> {
                        Log.i(TAG, "Cancelled")
                    }
                }
            })
    }

    private fun launchCalendarWatcherPeriodic() {
        val TAG = "CalendarPeriodicWatcher"
        val calendarWatcherRequestPeriodic = calendarWatcherRequestBuilder.build()
        workManager.enqueue(calendarWatcherRequestPeriodic)
        workManager.getWorkInfoByIdLiveData(calendarWatcherRequestPeriodic.id)
            .observe(this, { workInfo ->
                workManager.getWorkInfoByIdLiveData(calendarWatcherRequestPeriodic.id)
                    .observe(this, { workInfo ->
                        when (workInfo.state) {
                            WorkInfo.State.ENQUEUED -> {
                                Log.i(TAG, "Enqueued")
                                textView.text = TAG + " enqueued "
                            }
                            WorkInfo.State.BLOCKED -> {
                                Log.e(TAG, "Enqueued")
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Log.i(TAG, "Success")
                                launchCalendarDownloadAndPersistenceWorkers()
                            }
                            WorkInfo.State.FAILED -> {
                                Log.e(TAG, "Failed")
                            }
                            WorkInfo.State.CANCELLED -> {
                                Log.i(TAG, "Cancelled")
                            }
                        }
                    })
            })
    }

    private fun launchCalendarDownloadAndPersistenceWorkers(): Boolean {

        if (!appSharedPreferences.getBoolean(
                CalendarWatcher.NEW_DATA_AVAILABLE,
                false
            )
        ) return false

        dataBuilder.putString(
            "fileName",
            appSharedPreferences.getString(CalendarWatcher.NEW_FILE_NAME, null)
        )
        val calendarDownloaderWorker = downloadCalendarRequestBuilder.build()
        val dataPersistenceWorker = dataPersistenceWorkerBuilder.build()
        workManager.enqueue(calendarDownloaderWorker)
        workManager.enqueue(dataPersistenceWorker)

        workManager.getWorkInfoByIdLiveData(calendarDownloaderWorker.id)
            .observe(this, { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> {
                        textView.text = "Calendar Downloader enqueued."
                    }
                    WorkInfo.State.BLOCKED -> {
                        textView.text = "Calendar Downloader blocked."
                    }
                    WorkInfo.State.RUNNING -> {
                        textView.text = "Calendar Downloader running."
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        textView.text = "Calendar Downloader successful."
                    }
                    WorkInfo.State.FAILED -> {
                        textView.text = "Calendar failed to download."
                    }
                    WorkInfo.State.CANCELLED -> {
                        textView.text = "Work request cancelled."
                    }
                }
            })

        workManager.getWorkInfoByIdLiveData(calendarDownloaderWorker.id)
            .observe(this, { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> {
                        textView.text = "Calendar Persistence Worker enqueued."
                    }
                    WorkInfo.State.BLOCKED -> {
                        textView.text = "Calendar Persistence Worker blocked."
                    }
                    WorkInfo.State.RUNNING -> {
                        textView.text = "Calendar Persistence Worker running."
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        textView.text = "Calendar Persistence Worker successful."
                    }
                    WorkInfo.State.FAILED -> {
                        textView.text = "Persistence Worker failed"
                    }
                    WorkInfo.State.CANCELLED -> {
                        textView.text = "Work request cancelled."
                    }
                }
            })

        return true
    }
}