package fr.bowser.behaviortracker.showmode

import android.support.v4.view.PagerAdapter
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import fr.bowser.behaviortracker.showmodeitem.ShowModeTimerView
import fr.bowser.behaviortracker.timer.Timer

class ShowModeAdapter : PagerAdapter() {

    private val timers: ArrayList<Timer> = ArrayList()
    private val list = SparseArray<View>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val showModeTimerView = ShowModeTimerView(container.context)
        showModeTimerView.setTimer(timers[position])
        container.addView(showModeTimerView)
        list.append(position, showModeTimerView)
        return showModeTimerView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `view`: Any) {
        container.removeView(view as View)
        list.remove(position)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return timers.size
    }

    fun getViewAtPosition(position: Int): View{
        return list.get(position)
    }

    fun setData(timers: List<Timer>) {
        this.timers.clear()
        this.timers.addAll(timers)
        notifyDataSetChanged()
    }
}