package fr.istic.coulibaly.fazul.horairesbus

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.core.BusScheduleApplication
import fr.istic.coulibaly.fazul.horairesbus.api.core.services.ApiAdapter
import fr.istic.coulibaly.fazul.horairesbus.api.core.watchers.CalendarWatcher
import fr.istic.coulibaly.fazul.horairesbus.api.core.workers.CalendarDownloaderWorker
import fr.istic.coulibaly.fazul.horairesbus.api.core.workers.DataPersistenceWorker
import fr.istic.coulibaly.fazul.horairesbus.api.database.dao.BusRouteDao
import fr.istic.coulibaly.fazul.horairesbus.api.database.repository.BusRouteRepository
import fr.istic.coulibaly.fazul.horairesbus.api.utils.ZipFileManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import java.io.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button)
        val textView: TextView = findViewById(R.id.apiResponseView)

        val workManager: WorkManager = WorkManager.getInstance(this@MainActivity)

        lifecycleScope.launchWhenCreated {
            val workerConstraints = Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val calendarWatcherRequest = PeriodicWorkRequest
                .Builder(CalendarWatcher::class.java, 30, TimeUnit.MINUTES)
                .setConstraints(workerConstraints)
                .build()

            workManager.enqueue(calendarWatcherRequest)
            val appSharedPreferences = applicationContext.getSharedPreferences(
                StarContract.AUTHORITY,
                Context.MODE_PRIVATE
            )

            workManager.getWorkInfoByIdLiveData(calendarWatcherRequest.id)
                .observe(this@MainActivity, { workInfo ->
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {

                        if (appSharedPreferences.getBoolean(
                                CalendarWatcher.NEW_DATA_AVAILABLE,
                                false
                            )
                        ) {

                            val data = Data.Builder()
                            data.putString(
                                "fileName",
                                appSharedPreferences.getString(CalendarWatcher.NEW_FILE_NAME, null)
                            )

                            val downloadCalendarWork = OneTimeWorkRequest
                                .Builder(CalendarDownloaderWorker::class.java)
                                .setInputData(data.build())
                                .setConstraints(workerConstraints)
                                .build()
                            workManager.enqueue(downloadCalendarWork)

                            val dataPersistenceWorker = OneTimeWorkRequest
                                .Builder(DataPersistenceWorker::class.java)
                                .setInputData(data.build())
                                .setConstraints(workerConstraints)
                                .build()

                            workManager.enqueue(dataPersistenceWorker)


                            workManager.getWorkInfoByIdLiveData(downloadCalendarWork.id)
                                .observe(this@MainActivity, { workInfo ->
                                    if (workInfo != null) {
                                        when (workInfo.state) {
                                            WorkInfo.State.ENQUEUED -> {
                                                textView.text = "Download enqueued."
                                            }
                                            WorkInfo.State.BLOCKED -> {
                                                textView.text = "Download blocked."
                                            }
                                            WorkInfo.State.RUNNING -> {
                                                textView.text = "Download running."
                                            }
                                            WorkInfo.State.SUCCEEDED -> {
                                                textView.text = "Download successful."
                                            }
                                            WorkInfo.State.FAILED -> {
                                                textView.text = "Failed to download."
                                            }
                                            WorkInfo.State.CANCELLED -> {
                                                textView.text = "Work request cancelled."
                                            }
                                        }
                                    }
                                })

                            workManager.getWorkInfoByIdLiveData(dataPersistenceWorker.id)
                                .observe(this@MainActivity, { workInfo ->
                                    if (workInfo != null) {
                                        when (workInfo.state) {
                                            WorkInfo.State.ENQUEUED -> {
                                                textView.text = "Persistence enqueued."
                                            }
                                            WorkInfo.State.BLOCKED -> {
                                                textView.text = "Persistence blocked."
                                            }
                                            WorkInfo.State.RUNNING -> {
                                                textView.text = "Persistence running."
                                            }
                                            WorkInfo.State.SUCCEEDED -> {
                                                textView.text = "Persistence successful."
                                            }
                                            WorkInfo.State.FAILED -> {
                                                textView.text = "Failed to Persist."
                                            }
                                            WorkInfo.State.CANCELLED -> {
                                                textView.text = "Work request cancelled."
                                            }
                                        }
                                    }
                                })

                        }
                    }

                })

        }



    }


}