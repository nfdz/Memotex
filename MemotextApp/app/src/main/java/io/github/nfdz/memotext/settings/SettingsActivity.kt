package io.github.nfdz.memotext.settings

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
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.showSnackbar
import timber.log.Timber

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
            val bronzeWordsKey = getString(R.string.pref_lvl_bronze_words_key)
            val silverWordsKey = getString(R.string.pref_lvl_silver_words_key)
            val goldWordsKey = getString(R.string.pref_lvl_gold_words_key)
            val bronzeWordsDefault = getString(R.string.pref_lvl_bronze_words_default)
            val silverWordsDefault = getString(R.string.pref_lvl_silver_words_default)
            val goldWordsDefault = getString(R.string.pref_lvl_gold_words_default)
            bindPreferences(bronzeWordsKey, bronzeWordsDefault, silverWordsKey, silverWordsDefault, goldWordsKey, goldWordsDefault)
            findPreference(getString(R.string.pref_lvl_restore_default_key)).setOnPreferenceClickListener {
                PreferenceManager.getDefaultSharedPreferences(it.context).edit()
                    .putString(bronzeWordsKey, bronzeWordsDefault)
                    .putString(silverWordsKey, silverWordsDefault)
                    .putString(goldWordsKey, goldWordsDefault)
                    .commit()
                bindPreferences(bronzeWordsKey, bronzeWordsDefault, silverWordsKey, silverWordsDefault, goldWordsKey, goldWordsDefault)
                true
            }
        }

        private fun bindPreferences(bronzeWordsKey: String, bronzeWordsDefault: String,
                                    silverWordsKey: String, silverWordsDefault: String,
                                    goldWordsKey: String, goldWordsDefault: String) {
            bindPreferenceSummaryToValue(findPreference(bronzeWordsKey), bronzeWordsDefault)
            bindPreferenceSummaryToValue(findPreference(silverWordsKey), silverWordsDefault)
            bindPreferenceSummaryToValue(findPreference(goldWordsKey), goldWordsDefault)
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
                try {
                    val email = getString(R.string.email_feedback_address)
                    val starter = Intent(Intent.ACTION_SEND)
                    starter.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                    starter.data = Uri.parse(email)
                    starter.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_feedback_subject));
                    starter.type = "plain/content"
                    startActivity(starter)
                } catch (e: Exception) {
                    Timber.e(e)
                    activity.findViewById<View>(android.R.id.content).showSnackbar(getString(R.string.email_feedback_error))
                }
                true
            }
        }

        private fun setupRepoPreference() {
            findPreference(getString(R.string.pref_about_repo_key)).setOnPreferenceClickListener {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_repo_website))))
                } catch (e: Exception) {
                    Timber.e(e)
                    activity.findViewById<View>(android.R.id.content).showSnackbar(getString(R.string.url_repo_error))
                }
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
