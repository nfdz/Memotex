package io.github.nfdz.memotext.common

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.annotation.WorkerThread
import android.support.design.widget.Snackbar
import android.support.v4.text.HtmlCompat
import android.support.v4.text.HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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

fun Intent.getStringExtra(name: String, defaultValue: String): String {
    val result: String? = getStringExtra(name)
    return result ?: defaultValue
}

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

fun Context.getStringFromPreferences(@StringRes key: Int, @StringRes default: Int): String {
    val defaultString = getString(default)
    val result: String? = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(key), defaultString)
    return if (result != null) result else defaultString
}

@WorkerThread
fun Context.setStringInPreferences(@StringRes key: Int, value: String) {
    PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(key), value).commit()
}