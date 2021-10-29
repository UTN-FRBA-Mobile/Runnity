package com.utnfrbamobile.runnity.work

import android.app.Notification
import android.content.Context
import android.location.Location
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.domain.FetchLocationUseCase
import kotlinx.coroutines.delay

class RunnityWorker(
    private val fetchLocationUseCase: FetchLocationUseCase,
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private var notification: NotificationManagerCompat = NotificationManagerCompat.from(context)
    private var notificationBuilder: NotificationCompat.Builder = createNotification()

    override suspend fun doWork(): Result {
        while (true) {
            delay(1000)
            updatePush(fetchLocationUseCase())
        }
    }

    private fun updatePush(location: Location) {

    }

    private fun createNotification() = NotificationCompat.Builder(context, "id").apply {
        setContentTitle("Carrera en curso.")
        setOngoing(true)
        setShowWhen(false)
        setOnlyAlertOnce(true)
        setLargeIcon(ContextCompat.getDrawable(context, R.drawable.running)!!.toBitmap())
    }
}