package io.github.nfdz.memotex.common

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import androidx.annotation.LayoutRes
import androidx.annotation.Size
import androidx.annotation.StringRes
import androidx.annotation.WorkerThread
import com.google.android.material.snackbar.Snackbar
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
import androidx.appcompat.app.AlertDialog
import android.text.Spanned
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import io.github.nfdz.memotex.BuildConfig
import io.github.nfdz.memotex.R
import timber.log.Timber

//region View/ViewGroup utils

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.showSnackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_LONG): Snackbar {
    val result = Snackbar.make(this, processSnackbarText(text), duration)
    result.show()
    return result
}

fun View.showSnackbarWithAction(text: CharSequence,
                                actionText: CharSequence,
                                duration: Int = Snackbar.LENGTH_LONG,
                                actionCallback: View.OnClickListener? = null): Snackbar {
    val result = Snackbar.make(this, processSnackbarText(text), duration).setAction(actionText, actionCallback)
    result.show()
    return result
}

private fun processSnackbarText(text: CharSequence): Spanned {
    return HtmlCompat.fromHtml("<font color=\"#FAFAFA\">$text</font>", FROM_HTML_OPTION_USE_CSS_COLORS)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    this.requestFocus()
    imm?.showSoftInput(this, 0)
}

//endregion

//region Context utils

fun Context?.toast(@StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) = this?.let { Toast.makeText(it, textId, duration).show() }

fun Context.getStringFromPreferences(@StringRes key: Int, @StringRes default: Int): String {
    val defaultString = getString(default)
    val result: String? = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(key), defaultString)
    return result ?: defaultString
}

@WorkerThread
fun Context.setStringInPreferences(@StringRes key: Int, value: String) {
    PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(key), value).commit()
}

fun Intent.getStringExtra(name: String, defaultValue: String): String {
    return getStringExtra(name) ?: defaultValue
}

//endregion

//region Threading utils

class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    init {
        execute()
    }

    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}

fun doMainThread(handler: () -> Unit) {
    Handler(Looper.getMainLooper()).post(handler)
}

//endregion

//region Others utils

fun EditText.onNextOrEnterListener(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, event ->
        if (event == null) {
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    // Capture soft enters in a singleLine EditText that is the last EditText.
                    callback()
                    true;
                }
                EditorInfo.IME_ACTION_NEXT -> {
                    // Capture soft enters in other singleLine EditTexts
                    callback()
                    true;
                }
                else -> false  // Let system handle all other null KeyEvents
            }
        } else if (actionId == EditorInfo.IME_NULL) {
            // Capture most soft enters in multi-line EditTexts and all hard enters.
            // They supply a zero actionId and a valid KeyEvent rather than
            // a non-zero actionId and a null event like the previous cases.
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                // We capture the event when key is first pressed.
                true;
            } else {
                callback()
                true;   // We consume the event when the key is released.
            }
        } else {
            // We let the system handle it when the listener
            // is triggered by something that wasn't an enter.
            false;
        }
    }
}

fun Context.showAskLevelDialog(level: Level, callback: (level: Level) -> Unit) {
    val options = listOf<String>(getString(R.string.level_easy),
        getString(R.string.level_medium),
        getString(R.string.level_hard))
    val checkedItem = when(level) {
        Level.EASY -> 0
        Level.MEDIUM -> 1
        Level.HARD -> 2
    }
    AlertDialog.Builder(this).apply {
        setTitle(R.string.text_change_level_title)
    }.setSingleChoiceItems(options.toTypedArray(), checkedItem) { dialog, which ->
        callback(when(which) {
            1 -> Level.MEDIUM
            2 ->Level.HARD
            else -> Level.EASY
        })
        dialog.dismiss()
    }.show()
}


fun Int.bound(fromInclusive: Int, toInclusive: Int): Int {
    return Math.min(Math.max(this, fromInclusive), toInclusive)
}

fun Context.logAnalytics(@Size(min = 1L,max = 40L) event: String) {
    if (BuildConfig.DEBUG) {
        Timber.i("AnalyticsDebug: $event")
    } else {
        FirebaseAnalytics.getInstance(this).logEvent(event, null)
    }
}

fun reportException(ex: Exception) {
    if (BuildConfig.DEBUG) {
        Timber.w(ex, "CrashlyticsReportDebug")
    } else {
        Crashlytics.logException(ex)
    }
}

//endregion
