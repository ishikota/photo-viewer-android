package com.ishikota.photoviewerandroid.ui.userdetail

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.databinding.BindingAdapter
import java.util.regex.Pattern

@BindingAdapter("userDetailStatsStr")
fun TextView.setUserDetailStatsStr(str: String?) {
    val numberPosition = userDetailFindFirstNumberPositionFromString(str ?: "")
    text = if (numberPosition != null) {
        val (start, end) = numberPosition
        val sb = SpannableStringBuilder()
        sb.append(str)
        sb.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        sb
    } else {
        str
    }
}

@VisibleForTesting
fun userDetailFindFirstNumberPositionFromString(str: String): Pair<Int, Int>? {
    val p = Pattern.compile("\\d+")
    val m = p.matcher(str)
    return if (m.find()) {
        Pair(m.start(), m.end())
    } else {
        null
    }
}
