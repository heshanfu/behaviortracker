package fr.bowser.behaviortracker.showmodeitem

import fr.bowser.behaviortracker.timer.Timer


interface ShowModeTimerViewContract {

    interface View {

        fun timerUpdated(newTime: Long)

    }

    interface Presenter {

        fun setTimer(timer: Timer)

        fun start()

        fun stop()

        fun onClickView()

    }

}