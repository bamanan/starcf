package fr.istic.mob.starcf

import android.app.*
import android.content.*
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import fr.istic.mob.starcf.api.contract.StarContract
import fr.istic.mob.starcf.api.core.watchers.CalendarWatcher
import fr.istic.mob.starcf.api.core.workers.CalendarDownloaderWorker
import fr.istic.mob.starcf.api.core.workers.DataPersistenceWorker
import fr.istic.mob.starcf.api.database.BusScheduleApplication
import fr.istic.mob.starcf.api.database.entity.BusRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        const val FIRST_LAUNCH = "first_launch"
        private const val CHANNEL_ID: String = "12345"
        private const val CALENDAR_WATCH_FREQUENCE = 30L
    }

    // Workers Declaration
    private lateinit var workManager: WorkManager
    private lateinit var constraints: Constraints
    private lateinit var dataBuilder: Data.Builder
    private val onceCalendarWatcher = OneTimeWorkRequest.Builder(CalendarWatcher::class.java)
    private val downloadWorker = OneTimeWorkRequest.Builder(CalendarDownloaderWorker::class.java)
    private val dataPersistenceWorker =
        OneTimeWorkRequest.Builder(DataPersistenceWorker::class.java)
    private val periodicCalendarWatcher =
        PeriodicWorkRequest.Builder(
            CalendarWatcher::class.java,
            CALENDAR_WATCH_FREQUENCE,
            TimeUnit.MINUTES
        )

    private lateinit var appSharedPreferences: SharedPreferences

    private var cal: Calendar = Calendar.getInstance()

    private lateinit var currentNotification: Notification

    var progressStatus = 0

    private lateinit var progressBar: ProgressBar
    private lateinit var tvPercentage: TextView
    private lateinit var tvProgression: TextView

    private lateinit var spinner: Spinner
    private lateinit var routesLiveData: LiveData<List<BusRoute>>
    private val spinnerViewModel: SpinnerViewModel by viewModels {
        SpinnerViewModelFactory(database = BusScheduleApplication(applicationContext))
    }
    val routes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        progressBar = findViewById(R.id.progressBar)
        tvPercentage = findViewById(R.id.tvPercentage)
        tvProgression = findViewById(R.id.tvProgression)
        spinner = findViewById(R.id.spinner)

        appSharedPreferences =
            applicationContext.getSharedPreferences(StarContract.AUTHORITY, MODE_PRIVATE)

        dataBuilder = Data.Builder()
        constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        workManager = WorkManager.getInstance(this@MainActivity)
        val firstLaunch: Boolean = appSharedPreferences.getBoolean(FIRST_LAUNCH, true)

        lifecycleScope.launch(Dispatchers.Main) {
            if (firstLaunch) {
                // Download calendar if no data in database
                onceCalendarWatcher.setConstraints(constraints)
                val onceCalendarWatcherWork = onceCalendarWatcher.build()
                downloadWorker.setConstraints(constraints)
                dataPersistenceWorker.setConstraints(constraints)
                val downloadWork = downloadWorker.build()
                val dataPersistenceWork = dataPersistenceWorker.build()

                // Enqueue download and persistence works
                workManager
                    .beginWith(onceCalendarWatcherWork)
                    .then(downloadWork)
                    .then(dataPersistenceWork)
                    .enqueue()

                workManager.getWorkInfoByIdLiveData(downloadWork.id).observe(
                    this@MainActivity,
                    { workInfo ->
                        when (workInfo.state) {
                            WorkInfo.State.RUNNING -> {
                                launchProgressBar(
                                    downloadWork.id,
                                    CalendarDownloaderWorker.TAG
                                )
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                tvProgression.text =
                                    resources.getText(R.string.download_succeeded)
                            }
                            WorkInfo.State.CANCELLED -> {
                                tvProgression.text =
                                    resources.getText(R.string.download_canceled)
                            }
                            WorkInfo.State.FAILED -> {
                                tvProgression.text = resources.getText(R.string.download_failed)
                            }
                            else -> {
                                //No Action
                            }
                        }
                    })

                workManager.getWorkInfoByIdLiveData(dataPersistenceWork.id).observe(
                    this@MainActivity,
                    { workInfo ->
                        when (workInfo.state) {
                            WorkInfo.State.RUNNING -> {
                                launchProgressBar(
                                    dataPersistenceWork.id,
                                    DataPersistenceWorker.TAG
                                )
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                tvProgression.text =
                                    resources.getText(R.string.persistence_succedded)
                                appSharedPreferences.edit().putBoolean(FIRST_LAUNCH, false)
                                    .apply()
                            }
                            WorkInfo.State.CANCELLED -> {
                                tvProgression.text =
                                    resources.getText(R.string.persistence_failed)
                                appSharedPreferences.edit().putBoolean(FIRST_LAUNCH, true)
                                    .apply()
                            }
                            WorkInfo.State.FAILED -> {
                                tvProgression.text =
                                    resources.getText(R.string.persistence_failed)
                                appSharedPreferences.edit().putBoolean(FIRST_LAUNCH, true)
                                    .apply()
                            }
                            else -> {
                                //No Action
                            }
                        }
                    })

            }
            createPeriodicCalendarWatcherService()
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent!!.extras != null && intent.getStringExtra(
                CalendarWatcher.NEW_FILE_NAME
            ) != null
        ) {
            downloadAndPersist()
        }
    }


    private fun toast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun createNotification(newFile: String?) {
        createNotificationChannel()
        // Create an explicit intent for an Activity in your app
        intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_TASK_ON_HOME
            if (newFile != null) {
                putExtra(CalendarWatcher.NEW_FILE_NAME, newFile)
            }
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext,
            cal.timeInMillis.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.bus_icon)
            .setContentTitle("Star : Nouveau calendrier disponible")
            .setContentText("Un nouveau calendrier est disponible. Cliquez pour télécharger.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setTimeoutAfter(60000)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            val notificationId = (0..9999).random()
            currentNotification = builder.build()
            notify(notificationId, currentNotification)
        }
    }

    private fun createPeriodicCalendarWatcherService() {
        periodicCalendarWatcher
            .setConstraints(constraints)
            .setInitialDelay(500, TimeUnit.MILLISECONDS)
        val watcher = periodicCalendarWatcher.build()
        // Enqueue periodic calendar watcher
        workManager.enqueue(watcher)
        workManager.getWorkInfoByIdLiveData(watcher.id).observe(
            this@MainActivity,
            { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> {
                        toast("Periodic watcher Enqueued")
                    }
                    WorkInfo.State.RUNNING -> toast("Periodic watcher Running")
                    WorkInfo.State.SUCCEEDED -> {
                        toast("Periodic watcher succeeded")
                        val isNewCalendar: Boolean = appSharedPreferences.getBoolean(
                            CalendarWatcher.NEW_DATA_AVAILABLE,
                            false
                        )
                        if (isNewCalendar) {
                            val newFile = appSharedPreferences.getString(
                                CalendarWatcher.NEW_FILE_NAME,
                                null
                            )
                            createNotification(newFile)
                        }
                    }
                    WorkInfo.State.BLOCKED -> toast("Periodic watcher Blocked")
                    WorkInfo.State.FAILED -> toast("Periodic watcher Failed")
                    WorkInfo.State.CANCELLED -> toast("Periodic watcher Canceled")
                }
            })
    }

    private fun downloadAndPersist() {
        downloadWorker.setConstraints(constraints)
        dataPersistenceWorker.setConstraints(constraints)
        val dataPersistenceWork = dataPersistenceWorker.build()
        val downloadWork = downloadWorker.build()

        // Enqueue download and persistence works
        workManager
            .beginWith(downloadWork)
            .then(dataPersistenceWork)
            .enqueue()

        workManager.getWorkInfoByIdLiveData(downloadWork.id).observe(
            this@MainActivity,
            { workInfo ->
                if (workInfo.state == WorkInfo.State.RUNNING) {
                    launchProgressBar(
                        downloadWork.id,
                        CalendarDownloaderWorker.TAG
                    )
                }
            })

        workManager.getWorkInfoByIdLiveData(dataPersistenceWork.id).observe(
            this@MainActivity,
            { workInfo ->
                if (workInfo.state == WorkInfo.State.RUNNING) {
                    launchProgressBar(
                        dataPersistenceWork.id,
                        DataPersistenceWorker.TAG
                    )
                }
            })
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun launchProgressBar(workId: UUID, work: String) {
        progressBar = findViewById(R.id.progressBar)
        progressBar.progress = 0
        // set up max value for progress bar
        progressBar.max = 100

        // val workManager = WorkManager.getInstance(this@MainActivity)

        workManager.getWorkInfoByIdLiveData(workId).observe(
            this@MainActivity,
            { workInfo ->
                if (workInfo != null) {
                    val progress: String = CalendarDownloaderWorker.Progress
                    val workProgress = workInfo.progress
                    val value = workProgress.getLong(progress, 0).toInt()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressBar.setProgress(value, false)
                    } else {
                        progressBar.progress = value
                    }
                    tvPercentage.text = value.toString()
                    tvProgression.text = "${work} en cours..........."
                }
            })
    }


}


