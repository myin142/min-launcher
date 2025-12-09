package io.github.myin.phone.theme

import io.github.myin.phone.R

/**
 * Enum representing available theme identifiers
 */
enum class ThemeId(val id: String, val displayName: String, val styleResId: Int) {
    DEFAULT("default", "Default", R.style.AppTheme),
    OCEAN("ocean", "Ocean", R.style.AppTheme_Ocean),
    ;

    companion object {
        fun fromId(id: String): ThemeId {
            return ThemeId.entries.find { it.id == id } ?: DEFAULT
        }
    }
}
