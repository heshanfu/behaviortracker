package fr.bowser.behaviortracker.update

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import fr.bowser.behaviortracker.R

class UpdateDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = LayoutInflater.from(context).inflate(R.layout.view_whatsnew_dialog,
                null, false) as ViewGroup

        val okButton = root.findViewById<TextView>(R.id.view_whats_new_dialog_ok)
        okButton.setOnClickListener { dismiss() }

        val rateUsButton = root.findViewById<TextView>(R.id.view_whats_new_dialog_rate_us)
        rateUsButton.setOnClickListener {
            dismiss()
            //TODO display play store page
        }

        val builder = AlertDialog.Builder(context!!)
        builder.setView(root)
        return builder.create()
    }

    companion object {

        private const val TAG = "UpdateDialogFragment"

        fun newInstance(): UpdateDialogFragment {
            return UpdateDialogFragment()
        }

        fun showUpdateDialog(activity: AppCompatActivity) {
            val fragment = newInstance()
            fragment.show(activity.supportFragmentManager, TAG)
        }
    }
}
