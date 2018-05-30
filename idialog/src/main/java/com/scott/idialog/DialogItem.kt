@file:Suppress("MemberVisibilityCanBePrivate")

package com.scott.idialog

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.widget.TextView

/**
 * @author scott
 * @version 1.0
 */
class DialogItem {
    private lateinit var context: Context

    var textFromResource = false
    var text: String = ""
    var textColor: ColorStateList? = null

    var backgroud: Drawable? = null
    var backgroundResource: Int = 0
        set(@DrawableRes resId) {
            if (resId != 0 && resId == backgroundResource) {
                return
            }

            var d: Drawable? = null
            if (resId != 0) {
                d = ContextCompat.getDrawable(context, resId)
            }
            backgroud = d

            field = resId
        }

    /**
     * Sets the typeface and style in which the text should be displayed.
     * Note that not all Typeface families actually have bold and italic
     * variants, so you may need to use
     * [.setTypeface] to get the appearance
     * that you actually want.
     *
     * @see .getTypeface
     * @attr ref android.R.styleable#TextView_fontFamily
     * @attr ref android.R.styleable#TextView_typeface
     * @attr ref android.R.styleable#TextView_textStyle
     */
    var typeface: Typeface = Typeface.DEFAULT

    fun setTextColor(@ColorInt color: Int) {
        textColor = ColorStateList.valueOf(color)
    }

    fun setText(@StringRes resId: Int) {
        textFromResource = true
        context.getString(resId)
    }

    fun setBackgroundColor(@ColorInt color: Int) {
        if (backgroud is ColorDrawable) {
            ((backgroud as ColorDrawable).mutate() as ColorDrawable).color = color
            backgroundResource = 0
        } else {
            backgroud = ColorDrawable(color)
        }
    }


    companion object {
        const val RED = -0x2b5d2
        const val BLUE = -0xfc8401
    }
}