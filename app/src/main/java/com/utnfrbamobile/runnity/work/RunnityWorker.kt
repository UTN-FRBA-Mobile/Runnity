package com.utnfrbamobile.runnity.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.location.Location
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.work.*
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.data.LocationEntity
import com.utnfrbamobile.runnity.data.RunnityDaoSingleton
import com.utnfrbamobile.runnity.domain.FetchLocationUseCase
import kotlinx.coroutines.delay
import java.util.*

class RunnityWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private var notification: NotificationManagerCompat = NotificationManagerCompat.from(context)
    private var notificationBuilder: NotificationCompat.Builder = createNotification()
    private val fetchLocationUseCase : FetchLocationUseCase = FetchLocationUseCase(context.applicationContext)
    private val runnityDao = RunnityDaoSingleton.getInstance(context)

    override suspend fun doWork(): Result {
        setForeground(ForegroundInfo(NOTIFICATION_ID, notificationBuilder.build()))
        createChannelIfShould()
        while (true) {
            if (isStopped) {
                return Result.success()
            }
            delay(10_000)
            updatePush(fetchLocationUseCase())
        }
    }

    private suspend fun updatePush(location: Location?) = location?.let {
        runnityDao.insert(LocationEntity(latitude = location.latitude, longitude = location.longitude))
        setForeground(ForegroundInfo(NOTIFICATION_ID, notificationBuilder.build()))
    }

    private fun createNotification() = NotificationCompat.Builder(context, CHANNEL_ID).apply {
        setContentTitle("Carrera en curso.")
        setSmallIcon(R.drawable.running)
        setOngoing(true)
        setShowWhen(false)
        setOnlyAlertOnce(true)
        setLargeIcon(ContextCompat.getDrawable(context, R.drawable.running)!!.toBitmap())
        addAction(
            android.R.drawable.ic_delete,
            "CANCELAR",
            WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)
        )
    }

    private fun createChannelIfShould() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Runnity", NotificationManager.IMPORTANCE_HIGH)
            notification.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 839239
        private const val CHANNEL_ID = "1478492352459234"

        fun enqueue(context: Context): UUID {
            val uploadWork = OneTimeWorkRequestBuilder<RunnityWorker>().build()
            WorkManager.getInstance(context).enqueue(uploadWork)
            return uploadWork.id
        }
    }
}