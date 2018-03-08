package fr.bowser.behaviortracker.home

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
internal class HomeModule(private val homeView: HomeContract.View) {

    @GenericScope(component = HomeComponent::class)
    @Provides
    fun provideHomePresenter(context: Context,
                             timerNotificationManager: TimerNotificationManager,
                             timerListManager: TimerListManager): HomePresenter {
        return HomePresenter(homeView, context, timerNotificationManager, timerListManager)
    }

}