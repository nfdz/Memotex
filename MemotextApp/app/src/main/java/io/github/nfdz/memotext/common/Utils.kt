package io.github.nfdz.memotext.common

import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.showSnackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_LONG): Snackbar {
    val result = Snackbar.make(this, text, duration)
    result.show()
    return result
}

fun View.showSnackbarWithAction(text: CharSequence,
                                actionText: CharSequence,
                                duration: Int = Snackbar.LENGTH_LONG,
                                actionCallback: View.OnClickListener? = null): Snackbar {
    val result = Snackbar.make(this, text, duration).setAction(actionText, actionCallback)
    result.show()
    return result
}