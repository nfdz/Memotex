package io.github.nfdz.memotex.settings

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import io.github.nfdz.memotex.R
import io.github.nfdz.memotex.common.logAnalytics
import io.github.nfdz.memotex.common.reportException
import io.github.nfdz.memotex.common.showSnackbar

fun Context.startSettingsActivity() {
    startActivity(Intent(this, SettingsActivity::class.java))
}

class SettingsActivity : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onIsMultiPane(): Boolean {
        return isXLargeTablet(this)
    }

    private fun isXLargeTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
    }

    override fun onBuildHeaders(target: List<PreferenceActivity.Header>) {
        loadHeadersFromResource(R.xml.pref_headers, target)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (!super.onOptionsItemSelected(item) && (item.itemId == android.R.id.home)) {
            finish()
            true
        } else {
            false
        }
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    override fun isValidFragment(fragmentName: String): Boolean {
        return PreferenceFragment::class.java.name == fragmentName
                || LevelPreferenceFragment::class.java.name == fragmentName
                || AboutPreferenceFragment::class.java.name == fragmentName
    }

    class LevelPreferenceFragment : PreferenceFragment() {

        private val bindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            preference.summary = value.toString() + "%"
            true
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_level)
            setHasOptionsMenu(true)
            setupPreferences()
        }

        private fun setupPreferences() {
            val easyWordsKey = getString(R.string.pref_lvl_easy_words_key)
            val mediumWordsKey = getString(R.string.pref_lvl_medium_words_key)
            val hardWordsKey = getString(R.string.pref_lvl_hard_words_key)
            val easyWordsDefault = getString(R.string.pref_lvl_easy_words_default)
            val mediumWordsDefault = getString(R.string.pref_lvl_medium_words_default)
            val hardWordsDefault = getString(R.string.pref_lvl_hard_words_default)
            bindPreferences(easyWordsKey, easyWordsDefault, mediumWordsKey, mediumWordsDefault, hardWordsKey, hardWordsDefault)
            findPreference(getString(R.string.pref_lvl_restore_default_key)).setOnPreferenceClickListener {
                PreferenceManager.getDefaultSharedPreferences(it.context).edit()
                    .putString(easyWordsKey, easyWordsDefault)
                    .putString(mediumWordsKey, mediumWordsDefault)
                    .putString(hardWordsKey, hardWordsDefault)
                    .commit()
                bindPreferences(easyWordsKey, easyWordsDefault, mediumWordsKey, mediumWordsDefault, hardWordsKey, hardWordsDefault)
                true
            }
        }

        private fun bindPreferences(easyWordsKey: String, easyWordsDefault: String,
                                    mediumWordsKey: String, mediumWordsDefault: String,
                                    hardWordsKey: String, hardWordsDefault: String) {
            bindPreferenceSummaryToValue(findPreference(easyWordsKey), easyWordsDefault)
            bindPreferenceSummaryToValue(findPreference(mediumWordsKey), mediumWordsDefault)
            bindPreferenceSummaryToValue(findPreference(hardWordsKey), hardWordsDefault)
        }

        private fun bindPreferenceSummaryToValue(preference: Preference, defaultValue: String) {
            preference.onPreferenceChangeListener = bindPreferenceSummaryToValueListener
            bindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.context).getString(preference.key, defaultValue)
            )
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return if(handleOptionsItemSelected(item)) { true } else { super.onOptionsItemSelected(item) }
        }
    }

    class AboutPreferenceFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_about)
            setHasOptionsMenu(true)
            setupFeedbackPreference()
            setupRepoPreference()
        }

        private fun setupFeedbackPreference() {
            findPreference(getString(R.string.pref_about_feedback_key)).setOnPreferenceClickListener {
                val email = getString(R.string.email_feedback_address)
                try {
                    val subject = getString(R.string.email_feedback_subject)
//                    val starter = Intent(Intent.ACTION_SEND).apply {
//                        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
//                        data = Uri.parse(email)
//                        putExtra(Intent.EXTRA_SUBJECT, subject);
//                        type = "message/rfc822"
//                    }
                    val starter = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("mailto:?to=$email&subject=$subject")
                    }
                    startActivity(starter)
                } catch (e: Exception) {
                    reportException(e)
                    activity.findViewById<View>(android.R.id.content).showSnackbar(email)
                }
                activity.logAnalytics("FEEDBACK")
                true
            }
        }

        private fun setupRepoPreference() {
            findPreference(getString(R.string.pref_about_repo_key)).setOnPreferenceClickListener {
                val url = getString(R.string.url_repo_website)
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (e: Exception) {
                    reportException(e)
                    activity.findViewById<View>(android.R.id.content).showSnackbar(url)
                }
                activity.logAnalytics("REPOSITORY")
                true
            }
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return if(handleOptionsItemSelected(item)) { true } else { super.onOptionsItemSelected(item) }
        }
    }

}

fun PreferenceFragment.handleOptionsItemSelected(item: MenuItem): Boolean {
    return if (item.itemId == android.R.id.home) {
        fragmentManager.popBackStack()
        true
    } else {
        false
    }
}
