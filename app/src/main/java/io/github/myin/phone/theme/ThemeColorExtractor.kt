package io.github.myin.phone.theme

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import io.github.myin.phone.R

/**
 * Helper object to extract colors from Android theme resources
 */
object ThemeColorExtractor {

    /**
     * Get a color from a theme attribute
     *
     * @param context The context to use
     * @param themeResId The theme resource ID
     * @param attr The attribute to extract
     * @return The color value, or 0 if not found
     */
    @ColorInt
    fun getColorFromTheme(context: Context, @StyleRes themeResId: Int, @AttrRes attr: Int): Int {
        val typedValue = TypedValue()
        val contextThemeWrapper = android.view.ContextThemeWrapper(context, themeResId)
        val theme = contextThemeWrapper.theme

        return if (theme.resolveAttribute(attr, typedValue, true)) {
            if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT &&
                typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                // It's a direct color
                typedValue.data
            } else if (typedValue.type == TypedValue.TYPE_STRING) {
                // It's a reference to a color resource
                contextThemeWrapper.resources.getColor(typedValue.resourceId, theme)
            } else {
                0
            }
        } else {
            0
        }
    }

    /**
     * Get multiple colors from a theme
     *
     * @param context The context to use
     * @param themeResId The theme resource ID
     * @return Map of attribute names to color values
     */
    fun getThemeColors(context: Context, @StyleRes themeResId: Int): ThemeColors {
        return ThemeColors(
            primary = getColorFromTheme(context, themeResId, R.attr.colorPrimary),
            primaryDark = getColorFromTheme(context, themeResId, R.attr.colorPrimaryDark),
            accent = getColorFromTheme(context, themeResId, R.attr.colorAccent),
        )
    }
}

data class ThemeColors(
    @ColorInt val primary: Int,
    @ColorInt val primaryDark: Int,
    @ColorInt val accent: Int,
)
