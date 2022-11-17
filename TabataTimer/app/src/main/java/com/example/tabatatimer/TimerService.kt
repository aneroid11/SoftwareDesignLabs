package com.example.tabatatimer

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.*

class TimerService : Service() {
    companion object {
        // Channel ID for notifications
        const val CHANNEL_ID = "Timer_Notifications"

        // Service Actions
        const val START = "START"
        const val PAUSE = "PAUSE"
        const val RESET = "RESET"
        const val GET_STATUS = "GET_STATUS"
        const val MOVE_TO_FOREGROUND = "MOVE_TO_FOREGROUND"
        const val MOVE_TO_BACKGROUND = "MOVE_TO_BACKGROUND"

        // Intent Extras
        const val TIMER_ACTION = "TIMER_ACTION"
        const val TIME_REMAINING = "TIME_REMAINING"
        const val IS_TIMER_RUNNING = "IS_TIMER_RUNNING"

        // Intent Actions
        const val TIMER_TICK = "TIMER_TICK"
        const val TIMER_STATUS = "TIMER_STATUS"
    }

    private lateinit var notificationManager: NotificationManager
    private var timer = Timer()
    private var updateTimer = Timer()
    private var isTimerRunning: Boolean = false
    private var timeRemaining: Int = 0

    private var currRepetition: Int = 0
    private var numRepetitions: Int = 1

    private lateinit var phasesDurations: ArrayList<Int>
    private var currPhaseIndex = -1

    private var trainingFinished: Boolean = false

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("TimerService", "TimerService onBind()")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannel()
        getNotificationManager()

        val action = intent?.getStringExtra(TIMER_ACTION)
        Log.d("TimerService", "onStartCommand Action: $action")

        val oldNumRepetitions = numRepetitions
        numRepetitions = intent!!.getIntExtra("numRepetitions", -1)
        if (numRepetitions < 0) { numRepetitions = oldNumRepetitions }

        val durations = intent.getIntegerArrayListExtra("phasesDurations")
        if (durations != null) {
            phasesDurations = durations

            if (currPhaseIndex == -1) {
                timeRemaining = phasesDurations[0]
            }
        }

        when (action) {
            START -> startTimer()
            PAUSE -> pauseTimer()
            RESET -> resetTimer()
            GET_STATUS -> sendStatus()
            MOVE_TO_FOREGROUND -> moveToForeground()
            MOVE_TO_BACKGROUND -> moveToBackground()
        }

        return START_STICKY
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Timer",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(true)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationManager() {
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }

    private fun sendStatus() {
        val statusIntent = Intent()
        statusIntent.action = TIMER_STATUS
        statusIntent.putExtra(IS_TIMER_RUNNING, isTimerRunning)
        statusIntent.putExtra(TIME_REMAINING, timeRemaining)
        sendBroadcast(statusIntent)
    }

    private fun startTimer() {
        isTimerRunning = true

        sendStatus()

        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (trainingFinished) { return }

                if (currPhaseIndex < 0) {
                    currPhaseIndex++
                    timeRemaining = phasesDurations[currPhaseIndex]
                }
                else {
                    timeRemaining--

                    if (timeRemaining < 1) {
                        currPhaseIndex++
                        if (currPhaseIndex >= phasesDurations.size) {
                            currRepetition++

                            if (currRepetition >= numRepetitions) {
                                trainingFinished = true
                            }
                            else {
                                currPhaseIndex = 0
                                timeRemaining = phasesDurations[currPhaseIndex]
                            }
                        }
                        else {
                            timeRemaining = phasesDurations[currPhaseIndex]
                        }
                    }

                    /*if (timeRemaining < 1 && currRepetition < numRepetitions) {
                        currRepetition++
                        timeRemaining = 10
                    }
                    else if (timeRemaining < 1) {
                        timeRemaining = 0
                    }*/
                }

                val timerIntent = Intent()
                timerIntent.action = TIMER_TICK
                timerIntent.putExtra(TIME_REMAINING, timeRemaining)
                sendBroadcast(timerIntent)
            }
        }, 0, 1000)
    }

    private fun pauseTimer() {
        timer.cancel()
        isTimerRunning = false
        sendStatus()
    }

    private fun resetTimer() {
        pauseTimer()
        timeRemaining = 10
        sendStatus()
    }

    private fun buildNotification(): Notification {
        val title = if (isTimerRunning) {
            "Timer is running!"
        } else {
            "Timer is paused!"
        }

        /*val hours: Int = timeElapsed.div(60).div(60)
        val minutes: Int = timeElapsed.div(60)
        val seconds: Int = timeElapsed.rem(60)*/

        val intent = Intent(this, TimerActivity::class.java)
        //val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        //clickPendingIntent()

        return NotificationCompat.Builder(this, CHANNEL_ID)
            //.setContentTitle(title)
            .setContentTitle("$timeRemaining")
            .setOngoing(true)
            //.setContentText("${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}")
            .setContentText("")
            .setColorized(true)
            .setColor(Color.parseColor("#BEAEE2"))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun updateNotification() {
        notificationManager.notify(
            1,
            buildNotification()
        )
    }

    private fun moveToForeground() {
        if (isTimerRunning) {
            startForeground(1, buildNotification())

            updateTimer = Timer()
            updateTimer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    updateNotification()
                }
            }, 0, 1000)
        }
    }

    private fun moveToBackground() {
        updateTimer.cancel()
        stopForeground(true)
    }
}