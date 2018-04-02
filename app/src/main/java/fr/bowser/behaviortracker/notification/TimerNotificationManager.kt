package fr.bowser.behaviortracker.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.support.v4.app.NotificationCompat
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.home.HomeActivity
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.TimeConverter


class TimerNotificationManager(private val context: Context,
                               private val timeManager: TimeManager)
    : TimeManager.TimerCallback {

    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var timerNotificationBuilder: NotificationCompat.Builder? = null

    private var isNotificationDisplayed = false

    private var isAppInBackground = false

    var timer: Timer? = null
        private set

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            generateNotificationChannel(context.resources)
        }

        timeManager.registerUpdateTimerCallback(this)
    }

    fun displayTimerNotif(modifiedTimer: Timer) {
        if (timer == modifiedTimer) {
            resumeTimerNotif(modifiedTimer)
        } else {
            this.timer = modifiedTimer

            isNotificationDisplayed = true

            // action when click on notification
            val homeIntent = Intent(context, HomeActivity::class.java)
            homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val resultPendingIntent = PendingIntent.getActivity(
                    context, 0, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            timerNotificationBuilder = NotificationCompat.Builder(context, context.resources.getString(R.string.timer_notif_channel_id))
                    .setContentTitle(modifiedTimer.name)
                    .setContentText(TimeConverter.convertSecondsToHumanTime(
                            modifiedTimer.currentTime))
                    .setSmallIcon(R.drawable.ic_timer)
                    .setContentIntent(resultPendingIntent)
                    .setDeleteIntent(TimerReceiver.getDeletePendingIntent(context))
                    .addAction(R.drawable.ic_pause,
                            context.resources.getString(R.string.timer_notif_pause),
                            TimerReceiver.getPausePendingIntent(context))

            notificationManager.notify(
                    TIMER_NOTIFICATION_ID,
                    timerNotificationBuilder?.build())
        }
    }

    fun pauseTimerNotif(modifiedTimer: Timer) {
        if (!isNotificationDisplayed || timer != modifiedTimer) {
            return
        }

        // allow to remove notification when timer is not running
        timerNotificationBuilder?.setOngoing(false)

        timerNotificationBuilder?.mActions?.clear()
        timerNotificationBuilder?.addAction(R.drawable.ic_play,
                context.resources.getString(R.string.timer_notif_start),
                TimerReceiver.getPlayPendingIntent(context))

        timerNotificationBuilder?.let {
            notificationManager.notify(
                    TIMER_NOTIFICATION_ID,
                    timerNotificationBuilder?.build())
        }
    }

    fun changeNotifOngoing(isAppInBackground: Boolean) {
        if (!isNotificationDisplayed) {
            return
        }
        this.isAppInBackground = isAppInBackground
        timerNotificationBuilder?.setOngoing(isAppInBackground)
        timerNotificationBuilder?.let {
            notificationManager.notify(
                    TIMER_NOTIFICATION_ID,
                    timerNotificationBuilder?.build())
        }
    }

    fun renameTimerNotif(updatedTimer: Timer) {
        if (timer == updatedTimer) {
            timerNotificationBuilder?.setContentTitle(timer!!.name)
            timerNotificationBuilder?.let {
                notificationManager.notify(
                        TIMER_NOTIFICATION_ID,
                        timerNotificationBuilder?.build())
            }
        }
    }

    fun destroyNotif(removedTimer: Timer) {
        if (timer == removedTimer) {
            notificationManager.cancel(TIMER_NOTIFICATION_ID)
            notificationDismiss()
        }
    }

    fun notificationDismiss() {
        timer = null
        isNotificationDisplayed = false
    }

    override fun onTimerStateChanged(updatedTimer: Timer) {
        if (updatedTimer.isActivate) {
            displayTimerNotif(updatedTimer)
        } else {
            pauseTimerNotif(updatedTimer)
        }
    }

    override fun onTimerTimeChanged(updatedTimer: Timer) {
        if (timer == updatedTimer) {
            updateTimeNotif()
        }
    }

    private fun resumeTimerNotif(modifiedTimer: Timer) {
        if (!isNotificationDisplayed || timer != modifiedTimer) {
            return
        }

        timerNotificationBuilder?.setOngoing(isAppInBackground)

        timerNotificationBuilder?.mActions?.clear()
        timerNotificationBuilder?.addAction(R.drawable.ic_pause,
                context.resources.getString(R.string.timer_notif_pause),
                TimerReceiver.getPausePendingIntent(context))

        timerNotificationBuilder?.let {
            notificationManager.notify(
                    TIMER_NOTIFICATION_ID,
                    timerNotificationBuilder?.build())
        }
    }

    private fun updateTimeNotif() {
        timerNotificationBuilder?.setContentText(
                TimeConverter.convertSecondsToHumanTime(timer!!.currentTime))
        timerNotificationBuilder?.let {
            notificationManager.notify(
                    TIMER_NOTIFICATION_ID,
                    timerNotificationBuilder?.build())
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun generateNotificationChannel(res: Resources) {
        val channelId = res.getString(R.string.timer_notif_channel_id)
        val channelName = res.getString(R.string.timer_notif_channel_name)
        val channelDescription = res.getString(R.string.timer_notif_channel_description)
        val channelImportance = NotificationManager.IMPORTANCE_LOW
        val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                channelImportance)
        notificationChannel.description = channelDescription
        notificationChannel.enableVibration(false)
        notificationChannel.enableLights(false)
        notificationChannel.setShowBadge(false)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {

        private const val TIMER_NOTIFICATION_ID = 42

    }

}