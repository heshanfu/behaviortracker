package fr.bowser.behaviortracker.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import androidx.annotation.StringRes
import android.widget.Toast
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject


class SettingFragment : PreferenceFragment() {

    @Inject
    lateinit var presenter: SettingPresenter

    private lateinit var sendCommentary: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference_list)

        setupGraph()

        presenter.start()

        sendCommentary = findPreference(R.string.pref_key_send_commentary)
        sendCommentary.onPreferenceClickListener = onPreferenceClickListener
    }

    override fun onDestroy() {
        presenter.stop()
        super.onDestroy()
    }

    private fun setupGraph() {
        val component = DaggerSettingComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(activity!!))
                .settingPresenterModule(SettingPresenterModule())
                .build()
        component.inject(this)
    }

    private fun sendCommentary() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$SUPPORT_EMAIL")
        intent.putExtra(Intent.EXTRA_SUBJECT,
                resources.getString(R.string.settings_send_email_subject))
        try {
            startActivity(Intent.createChooser(
                    intent,
                    resources.getString(R.string.settings_send_email_choose_app)))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(activity!!,
                    resources.getString(R.string.settings_send_email_no_application_available),
                    Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun findPreference(@StringRes prefKey: Int): Preference {
        return findPreference(resources.getString(prefKey))
    }

    private val onPreferenceClickListener = Preference.OnPreferenceClickListener { preference ->
        when (preference) {
            sendCommentary -> {
                sendCommentary()
                return@OnPreferenceClickListener true
            }
        }
        false
    }

    companion object {
        const val SUPPORT_EMAIL = "torcheux.frederic@gmail.com"
    }

}