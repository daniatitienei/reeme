package com.atitienei_daniel.reeme.data.receiver

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkBuilder
import com.atitienei_daniel.reeme.MainActivity
import com.atitienei_daniel.reeme.R
import com.atitienei_daniel.reeme.ui.utils.Routes
import kotlin.random.Random

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val id = intent.getIntExtra("id", Random.nextInt())

        val notificationSound: Uri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val reminderDetailIntent = Intent(
            Intent.ACTION_VIEW,
            "https://example.com/reminderId=$id".toUri(),
            context,
            MainActivity::class.java
        )

        val pending: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(reminderDetailIntent)
            getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(context!!, "reminder")
            .setSmallIcon(R.drawable.ic_notifications_active)
            .setContentTitle(title)
            .setContentText(description)
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pending)

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(
            id,
            notification.build()
        )
    }
}