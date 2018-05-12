package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroManager {

    fun startPomodoro(actionTimer: Timer,
                      actionDuration: Long,
                      restTimer: Timer,
                      restDuration: Long)

    fun resume()

    fun pause()

    fun stop()

    fun getPomodoroActionTimer(): Timer?

    fun getPomodoroRestTimer(): Timer?

    fun isPomodoroStarted(): Boolean

    fun isPomodoroRunning(): Boolean

    fun getPomodoroCurrentTimer(): Timer?

    fun getPomodoroTime(): Long

    fun setPomodoroCallback(callback: Callback?)

    interface Callback {
        fun onTimerStateChanged(updatedTimer: Timer)
        fun updateTime(timer: Timer, currentTime: Long)
        fun onCountFinished(newTimer: Timer)
    }

}