package fr.bowser.behaviortracker.update

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import android.util.Log


object UpdateUtils {

    private const val TAG = "UpdateUtils"

    /**
     * Preference key to detect if it's the first app launch since last update.
     * We concatenate version code to get the final key.
     */
    private const val PREF_IS_FIRST_LAUNCH_SINCE_UPDATE = "PlatineActivity.PREF_IS_FIRST_LAUNCH_SINCE_UPDATE_"

    fun checkIfFirstLaunchSinceUpdate(context: Context): Boolean {
        var info: PackageInfo? = null
        try {
            info = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Couldn't get package info to check if update")
        }

        if (info == null) {
            return false
        }

        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val isFirstLaunch = pref.getBoolean(PREF_IS_FIRST_LAUNCH_SINCE_UPDATE + info.versionCode, true)

        if (isFirstLaunch) {
            if (info.firstInstallTime != info.lastUpdateTime) {
                val edit = pref.edit()
                edit.putBoolean(PREF_IS_FIRST_LAUNCH_SINCE_UPDATE + info.versionCode, false)
                edit.apply()
                return true
            }
        }

        return false
    }
}
