package fr.bowser.behaviortracker.showmodeitem

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.RippleDrawable
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.TimeConverter
import javax.inject.Inject

class ShowModeTimerView(context: Context) : ConstraintLayout(context),
        ShowModeTimerViewContract.View {

    @Inject
    lateinit var presenter: ShowModeTimerViewPresenter

    private val chrono: TextView
    private val timerName: TextView
    private val timerState: ImageView
    private val bg: View

    init {
        setupGraph()

        inflate(context, R.layout.item_show_mode, this)

        setOnClickListener { presenter.onClickView() }

        chrono = findViewById(R.id.show_mode_timer_time)
        timerName = findViewById(R.id.show_mode_timer_name)
        timerState = findViewById(R.id.show_mode_timer_status)
        bg = findViewById(R.id.animated_background)

        bg.setBackgroundResource(R.drawable.bg_show_mode_item)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.start()
    }

    override fun onDetachedFromWindow() {
        presenter.stop()
        super.onDetachedFromWindow()
    }

    override fun statusUpdated(activate: Boolean) {
        if (activate) {
            timerState.setImageResource(R.drawable.ic_pause)
        } else {
            timerState.setImageResource(R.drawable.ic_play)
        }
    }

    override fun timerUpdated(newTime: Long) {
        chrono.text = TimeConverter.convertSecondsToHumanTime(newTime)
    }

    fun setTimer(timer: Timer) {
        presenter.setTimer(timer)

        val drawable = bg.background as RippleDrawable
        drawable.setColorFilter(ColorUtils.getColor(context, timer.color), PorterDuff.Mode.SRC_ATOP)

        chrono.text = TimeConverter.convertSecondsToHumanTime(timer.time.toLong())
        timerName.text = timer.name

        chrono.transitionName = "time:" + timer.id
        timerName.transitionName = "name:" + timer.id
        bg.transitionName = "background:" + timer.id
        timerState.transitionName = "status:" + timer.id

        /*viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                ActivityCompat.startPostponedEnterTransition(context as Activity)
                return true
            }
        })*/
    }

    private fun setupGraph() {
        val component = DaggerShowModeTimerViewComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
                .showModeTimerViewModule(ShowModeTimerViewModule(this))
                .build()
        component.inject(this)
    }

}