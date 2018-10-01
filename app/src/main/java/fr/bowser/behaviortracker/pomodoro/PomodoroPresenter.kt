package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

class PomodoroPresenter(private val screen: PomodoroContract.Screen,
                        private val pomodoroManager: PomodoroManager,
                        override val timerList: List<Timer>) : PomodoroContract.Presenter {

    private val pomodoroListener = createPomodoroManagerListener()

    override fun start() {
        pomodoroManager.listener = pomodoroListener

        if (pomodoroManager.currentTimer != null) {
            screen.updatePomodoroTimer(pomodoroManager.currentTimer!!, pomodoroManager.pomodoroTime)
        }

        updateFabIcon()
    }

    override fun stop() {
        pomodoroManager.listener = null
    }

    override fun onClickFab() {
        if (!pomodoroManager.isStarted) {
            screen.displayChoosePomodoroTimer()
            return
        }

        if (pomodoroManager.isRunning) {
            pomodoroManager.pause()
        } else if (!pomodoroManager.isRunning) {
            pomodoroManager.resume()
        }

        updateFabIcon()
    }

    override fun onClickStopPomodoro() {
        pomodoroManager.stop()
        screen.displayEmptyView()
        updateFabIcon()
    }

    private fun updateFabIcon() {
        if (!pomodoroManager.isStarted) {
            screen.displayStartIcon()
            return
        }

        if (pomodoroManager.isRunning) {
            screen.displayPauseIcon()
        } else {
            screen.displayPlayIcon()
        }
    }

    private fun createPomodoroManagerListener(): PomodoroManager.Listener {
        return object : PomodoroManager.Listener {

            override fun onPomodoroSessionStarted() {
                screen.displayPauseIcon()
            }

            override fun onTimerStateChanged(updatedTimer: Timer) {
                // nothing to do
            }

            override fun updateTime(timer: Timer, currentTime: Long) {
                screen.updateTime(timer, currentTime)
            }

            override fun onCountFinished(newTimer: Timer, duration: Long) {
                screen.updatePomodoroTimer(newTimer, duration)
            }

        }
    }

}