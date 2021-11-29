package fr.istic.coulibaly.fazul.horairesbus

import android.app.*
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import fr.istic.coulibaly.fazul.horairesbus.api.contract.StarContract
import fr.istic.coulibaly.fazul.horairesbus.api.core.watchers.CalendarWatcher
import fr.istic.coulibaly.fazul.horairesbus.api.core.workers.CalendarDownloaderWorker
import fr.istic.coulibaly.fazul.horairesbus.api.core.workers.DataPersistenceWorker
import fr.istic.coulibaly.fazul.horairesbus.api.database.BusScheduleApplication
import fr.istic.coulibaly.fazul.horairesbus.api.database.entity.BusRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.*
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val NOTIFICATION_CHANNEL_ID = StarContract.AUTHORITY

    private lateinit var workManager: WorkManager

    private val appSharedPreferences: SharedPreferences by lazy {
        this.getSharedPreferences(StarContract.AUTHORITY, Context.MODE_PRIVATE)
    }

    private lateinit var workerConstraints: Constraints

    private lateinit var dataBuilder: Data.Builder

    private lateinit var calendarWatcherRequestOnceBuilder: OneTimeWorkRequest.Builder

    private lateinit var calendarWatcherPeriodicRequest: PeriodicWorkRequest.Builder

    private lateinit var downloadCalendarRequestBuilder: OneTimeWorkRequest.Builder

    private lateinit var dataPersistenceWorkerBuilder: OneTimeWorkRequest.Builder

    private lateinit var textViewProgressBotton: TextView
    private lateinit var textViewProgressCenter: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutProgressBar: RelativeLayout

    private var progressStatus = 0
    private var handlerProgressBar: Handler? = null

    private val busScheduleApplication: BusScheduleApplication by lazy {
        BusScheduleApplication(this)
    }
    private var busRoutes: List<BusRoute> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        textViewProgressBotton = findViewById(R.id.textViewSecondary)
        textViewProgressCenter = findViewById(R.id.textViewPrimary)
        layoutProgressBar = findViewById(R.id.layoutProgressBar)

        init()
        var calendarWatcherOnceId = UUID.randomUUID()
        var calendarWatcherPeriodicId = UUID.randomUUID()
        var downloadCalendarWorkerId = UUID.randomUUID()
        var dataPersistenceWorkerId = UUID.randomUUID()

        lifecycleScope.launch(Dispatchers.IO) {
            busRoutes = busScheduleApplication.busRouteRepository.allBusRoutes()
            if (busRoutes.isEmpty()) {
                val calendarWatcherOnce = calendarWatcherRequestOnceBuilder.build()
                calendarWatcherOnceId = calendarWatcherOnce.id
                workManager.enqueue(calendarWatcherOnce)
                downloadCalendarWorkerId = launchCalendarDownloadWorker()
                dataPersistenceWorkerId = launchCalendarPersistenceWorker()
            }
            calendarWatcherPeriodicId = launchCalendarWatcherPeriodic()


            lifecycleScope.launch(Dispatchers.Main) {

                textViewProgressBotton.text =
                    "Il y a ${busRoutes.size} élément(s) dans la base de données"

                workManager.getWorkInfoByIdLiveData(calendarWatcherOnceId)
                    .observe(this@MainActivity, { workInfo ->
                        when (workInfo.state) {
                            WorkInfo.State.ENQUEUED -> {
                                Log.d(CalendarWatcher.TAG, "Enqueued")
                            }
                            WorkInfo.State.BLOCKED -> {
                                Log.e(CalendarWatcher.TAG, "Blocked")
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Log.d(CalendarWatcher.TAG, "Success")
                            }
                            WorkInfo.State.FAILED -> {
                                Log.e(CalendarWatcher.TAG, "Failed")
                            }
                            WorkInfo.State.CANCELLED -> {
                                Log.d(CalendarWatcher.TAG, "Cancelled")
                            }
                            WorkInfo.State.RUNNING -> {
                                Log.d(CalendarWatcher.TAG, "Cancelled")
                            }
                        }
                    })

                workManager.getWorkInfoByIdLiveData(calendarWatcherPeriodicId)
                    .observe(this@MainActivity, { workInfo ->
                        when (workInfo.state) {
                            WorkInfo.State.ENQUEUED -> {
                                Log.d(CalendarWatcher.TAG, "Enqueued")
                            }
                            WorkInfo.State.BLOCKED -> {
                                Log.e(CalendarWatcher.TAG, "Blocked")
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Log.d(CalendarWatcher.TAG, "Success")
                                alert()
                            }
                            WorkInfo.State.FAILED -> {
                                Log.e(CalendarWatcher.TAG, "Failed")
                            }
                            WorkInfo.State.CANCELLED -> {
                                Log.d(CalendarWatcher.TAG, "Cancelled")
                            }
                            WorkInfo.State.RUNNING -> {
                                Log.d(CalendarWatcher.TAG, "Cancelled")
                            }
                        }
                    })

                workManager.getWorkInfoByIdLiveData(downloadCalendarWorkerId)
                    .observe(this@MainActivity, { workInfo ->
                        when (workInfo.state) {
                            WorkInfo.State.ENQUEUED -> {
                                Log.d(CalendarDownloaderWorker.TAG, "Enqueued")
                            }
                            WorkInfo.State.BLOCKED -> {
                                Log.d(CalendarDownloaderWorker.TAG, "Blocked")
                                textViewProgressBotton.text = "Calendar Downloader blocked."
                            }
                            WorkInfo.State.FAILED -> {
                                Log.d(CalendarDownloaderWorker.TAG, "Failed")
                                textViewProgressBotton.text = "Calendar Downloader Failed."
                            }
                            WorkInfo.State.CANCELLED -> {
                                Log.d(CalendarDownloaderWorker.TAG, "Cancelled")
                                textViewProgressBotton.text = "Calendar Downloader canceled."
                            }
                            WorkInfo.State.RUNNING -> {
                                Log.d(CalendarDownloaderWorker.TAG, "Running")
                                launchProgressBar(CalendarDownloaderWorker.TAG)
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Log.d(CalendarDownloaderWorker.TAG, "Succeed")
                            }
                        }
                    })

                workManager.getWorkInfoByIdLiveData(dataPersistenceWorkerId)
                    .observe(this@MainActivity, { workInfo ->
                        when (workInfo.state) {
                            WorkInfo.State.ENQUEUED -> {
                                Log.d(DataPersistenceWorker.TAG, "Enqueued")
                            }
                            WorkInfo.State.BLOCKED -> {
                                Log.d(DataPersistenceWorker.TAG, "Blocked")
                                textViewProgressBotton.text = "Persistence blocked."
                            }
                            WorkInfo.State.FAILED -> {
                                Log.d(DataPersistenceWorker.TAG, "Failed")
                                textViewProgressBotton.text = "Persistence Failed."
                            }
                            WorkInfo.State.CANCELLED -> {
                                Log.d(DataPersistenceWorker.TAG, "Cancelled")
                                textViewProgressBotton.text = "Persistence canceled."
                            }
                            WorkInfo.State.RUNNING -> {
                                Log.d(DataPersistenceWorker.TAG, "Persistence Running")
                                launchProgressBar(CalendarDownloaderWorker.TAG)
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Log.d(DataPersistenceWorker.TAG, "Persistence Succeeded")
                            }
                        }
                    })
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent!!.extras != null) {
            launchCalendarDownloadWorker()
        }
        finish()
    }


    private fun init() {
        workManager = WorkManager.getInstance(this@MainActivity)

        workerConstraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        dataBuilder = Data.Builder()

        dataPersistenceWorkerBuilder = OneTimeWorkRequest
            .Builder(DataPersistenceWorker::class.java)
            .setInputData(dataBuilder.build())
            .setConstraints(workerConstraints)

        downloadCalendarRequestBuilder = OneTimeWorkRequest
            .Builder(CalendarDownloaderWorker::class.java)
            .setInputData(dataBuilder.build())
            .setConstraints(workerConstraints)

        calendarWatcherPeriodicRequest = PeriodicWorkRequest
            .Builder(CalendarWatcher::class.java, 30, TimeUnit.MINUTES)
            .setInputData(dataBuilder.build())
            .setInitialDelay(500, TimeUnit.MILLISECONDS)
            .setConstraints(workerConstraints)

        calendarWatcherRequestOnceBuilder = OneTimeWorkRequest
            .Builder(CalendarWatcher::class.java)
            .setInputData(dataBuilder.build())
            .setConstraints(workerConstraints)

        handlerProgressBar = Handler()
        hideProgressBar()
    }


    private fun launchCalendarWatcherPeriodic(): UUID {
        val calendarWatcherRequestPeriodic = calendarWatcherPeriodicRequest.build()
        workManager.enqueue(calendarWatcherRequestPeriodic)
        return calendarWatcherRequestPeriodic.id
    }

    private fun launchCalendarDownloadWorker(): UUID {
        val calendarDownloaderWorker = downloadCalendarRequestBuilder.build()
        workManager.enqueue(calendarDownloaderWorker)
        launchProgressBar(CalendarDownloaderWorker.TAG)
        return calendarDownloaderWorker.id
    }

    private fun launchCalendarPersistenceWorker(): UUID {
        val dataPersistenceWorker = dataPersistenceWorkerBuilder.build()
        workManager.enqueue(dataPersistenceWorker)
        launchProgressBar(DataPersistenceWorker.TAG)
        return dataPersistenceWorker.id
    }

    private fun launchProgressBar(taskName: String) {
        layoutProgressBar.isVisible = true
        progressBar.isVisible = true
        textViewProgressBotton.isVisible = true

        progressStatus = 0

        Thread {
            while (progressStatus < 100) {
                progressStatus += 1
                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                handlerProgressBar?.post {
                    progressBar.secondaryProgress = progressStatus
                    textViewProgressBotton.text = taskName +
                            " task progress\n$progressStatus% of 100"
                    textViewProgressCenter.text = progressStatus.toString()
                    if (progressStatus == 100) {
                        textViewProgressBotton.text = taskName + " task complete."
                        hideProgressBar()
                    }
                }
            }
        }.start()
    }

/*
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Nouveaux Horaires de Bus"
            val descriptionText = "De nouveaux horaires disponibles"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.notif)
            .setContentTitle("Nouveaux Horaires de Bus")
            .setContentText("De nouveaux horaires sont disponibles ! Veuillez les télécharger")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(101, builder.build())
        }
    }
*/

    private fun alert(): AlertDialog {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setIcon(R.drawable.notif_icon)
            setTitle("Nouveaux Horaires")
            setMessage("Nouveaux horaires de bus disponibles. Cliquez sur OK  pour télécharger !")
            setPositiveButton("OK") { _, _ ->
                launchCalendarDownloadWorker()
                launchCalendarPersistenceWorker()
            }
            setNegativeButton("Non") { _, _ ->
                toast("Téléchargement annulé")
            }
        }
            .create()
        return alertDialog.show()
    }

    private fun toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

    private fun hideProgressBar() {
        progressBar.isVisible = false
        textViewProgressCenter.isVisible = false
    }
}