package fr.bowser.behaviortracker.pomodoro

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class PomodoroModule(private val view: PomodoroContract.View) {

    @GenericScope(component = PomodoroComponent::class)
    @Provides
    fun providePomodoroPresenter(pomodoroManager: PomodoroManager,
                                 timerListManager: TimerListManager): PomodoroPresenter {
        return PomodoroPresenter(view, pomodoroManager, timerListManager)
    }

}