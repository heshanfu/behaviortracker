package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer

class PomodoroManagerImpl(private val timeManager: TimeManager) : PomodoroManager {

    private var actionTimer: Timer? = null

    private var restTimer: Timer? = null

    private var pomodoroTime = 0L

    private var currentTimer: Timer? = null

    private var isRunning = false

    private var isStarted = false

    private var callback: PomodoroManager.Callback? = null

    private var actionDuration = 0L
    private var restDuration = 0L

    override fun startPomodoro(actionTimer: Timer,
                      actionDuration: Long,
                      restTimer: Timer,
                      restDuration: Long) {
        this.actionTimer = actionTimer
        this.restTimer = restTimer
        this.actionDuration = actionDuration
        this.restDuration = restDuration

        currentTimer = actionTimer
        pomodoroTime = actionDuration

        isStarted = true
        isRunning = true

        timeManager.registerUpdateTimerCallback(timeManagerCallback)

        timeManager.startTimer(currentTimer!!)
    }

    override fun resume() {
        if (!isStarted) {
            return
        }
        isRunning = true
        timeManager.startTimer(currentTimer!!)
    }

    override fun pause() {
        if (!isStarted) {
            return
        }
        isRunning = false
        timeManager.stopTimer(currentTimer!!)
    }

    override fun stop() {
        isStarted = false
        actionTimer = null
        restTimer = null
        timeManager.unregisterUpdateTimerCallback(timeManagerCallback)
    }

    override fun getPomodoroActionTimer(): Timer? {
        return actionTimer
    }

    override fun getPomodoroRestTimer(): Timer? {
        return restTimer
    }

    override fun isPomodoroStarted(): Boolean {
        return isStarted
    }

    override fun isPomodoroRunning(): Boolean {
        return isRunning
    }

    override fun getPomodoroCurrentTimer(): Timer? {
        return currentTimer
    }

    override fun getPomodoroTime(): Long {
        return pomodoroTime
    }

    override fun setPomodoroCallback(callback: PomodoroManager.Callback?) {
        this.callback = callback
    }

    private val timeManagerCallback = object : TimeManager.TimerCallback {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            if (actionTimer == updatedTimer || updatedTimer == restTimer) {
                callback?.onTimerStateChanged(updatedTimer)
            }
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            if (updatedTimer != currentTimer) {
                return
            }

            pomodoroTime--
            callback?.updateTime(currentTimer!!, pomodoroTime)

            if (pomodoroTime > 0L) {
                return
            }

            if (currentTimer == actionTimer) {
                currentTimer = restTimer
            } else if (currentTimer == restTimer) {
                currentTimer = actionTimer
            }
            callback?.onCountFinished(currentTimer!!)
        }
    }

}