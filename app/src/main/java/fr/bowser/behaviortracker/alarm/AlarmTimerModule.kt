package fr.bowser.behaviortracker.alarm

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class AlarmTimerModule(private val view: AlarmTimerContract.View) {

    @GenericScope(component = AlarmTimerComponent::class)
    @Provides
    fun provideAlarmTimerPresenter(alarmTimerManager: AlarmTimerManager, eventManager: EventManager): AlarmDialogPresenter {
        return AlarmDialogPresenter(view, alarmTimerManager, eventManager)
    }

}