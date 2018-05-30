package fr.bowser.behaviortracker.showmode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.TransitionHelper
import javax.inject.Inject


class ShowModeActivity : AppCompatActivity(), ShowModeContract.View {

    private lateinit var adapter: ShowModeAdapter
    private lateinit var viewPager: ViewPager

    @Inject
    lateinit var presenter: ShowModePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_mode)

        prepareSharedElementTransition()
        postponeEnterTransition()

        setupGraph()

        initUI()

        val extras = intent.extras
        val selectedTimerID = extras.getLong(EXTRA_SELECTED_TIMER_ID)
        presenter.start(selectedTimerID)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFinishAfterTransition()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_showmode, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val menuScreenOff = menu?.findItem(R.id.menu_keep_screen_off)
        val menuScreenOn = menu?.findItem(R.id.menu_keep_screen_on)

        if (presenter.keepScreenOn()) {
            menuScreenOff?.isVisible = true
            menuScreenOn?.isVisible = false
        } else {
            menuScreenOff?.isVisible = false
            menuScreenOn?.isVisible = true
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_keep_screen_on -> {
                presenter.onClickScreeOn()
                return true
            }
            R.id.menu_keep_screen_off -> {
                presenter.onClickScreeOff()
                return true
            }
        }
        return false
    }

    private fun setupGraph() {
        val build = DaggerShowModeComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(this))
                .showModeModule(ShowModeModule(this))
                .build()
        build.inject(this)
    }

    private fun initUI() {
        viewPager = findViewById(R.id.show_mode_pager)
        adapter = ShowModeAdapter()
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // nothing to do
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // nothing to do
            }

            override fun onPageSelected(position: Int) {
                TransitionHelper.INDEX = position
            }
        })

        initToolbar()
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.show_mode_toolbar)
        setSupportActionBar(toolbar)
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setDisplayShowHomeEnabled(true)
        supportActionBar.title = ""
    }

    override fun displayTimerList(timers: List<Timer>, selectedTimerPosition: Int) {
        adapter.setData(timers)
        viewPager.currentItem = selectedTimerPosition


        TransitionHelper.INDEX = selectedTimerPosition
    }

    override fun keepScreeOn(keepScreenOn: Boolean) {
        if (keepScreenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        invalidateOptionsMenu()
    }

    private fun prepareSharedElementTransition() {
        setEnterSharedElementCallback(
                object : SharedElementCallback() {
                    override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                        val view = adapter.getViewAtPosition(TransitionHelper.INDEX)
                        sharedElements[names[0]] = view.findViewById(R.id.show_mode_timer_time)
                    }
                })
    }

    companion object {

        private const val EXTRA_SELECTED_TIMER_ID = "show_mode_activity.key.extra_selected_timer_id"

        fun startActivity(activity: Activity,
                          time: View,
                          name: View,
                          background: View,
                          status: View,
                          state: Long) {
            val intent = Intent(activity, ShowModeActivity::class.java)
            intent.putExtra(EXTRA_SELECTED_TIMER_ID, state)
            val p1 = android.support.v4.util.Pair.create(time, time.transitionName)
            //val p2 = android.support.v4.util.Pair.create(name, name.transitionName)
            //val p3 = android.support.v4.util.Pair.create(background, background.transitionName)
            //val p4 = android.support.v4.util.Pair.create(status, status.transitionName)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, p1/*, p2, p3, p4*/)
            activity.startActivity(intent, options.toBundle())
        }

    }

}