package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ui.theme.ThemePreset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "brokkobi_settings")

class ThemePreferences(private val context: Context) {

    private val KEY_THEME_PRESET = stringPreferencesKey("theme_preset")
    private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
    private val KEY_HIDDEN_PASSWORD_HASH = stringPreferencesKey("hidden_notes_password_hash")
    private val KEY_LOCKED_PASSWORD_HASH = stringPreferencesKey("locked_notes_password_hash")
    private val KEY_DEFAULT_FONT = stringPreferencesKey("default_font")

    val themePresetFlow: Flow<ThemePreset> = context.dataStore.data.map { preferences ->
        val name = preferences[KEY_THEME_PRESET] ?: ThemePreset.GOLDEN_CLASSIC.name
        try {
            ThemePreset.valueOf(name)
        } catch (e: Exception) {
            ThemePreset.GOLDEN_CLASSIC
        }
    }

    val darkModeFlow: Flow<Boolean?> = context.dataStore.data.map { preferences ->
        preferences[KEY_DARK_MODE]
    }

    val hiddenPasswordHashFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_HIDDEN_PASSWORD_HASH]
    }

    val lockedPasswordHashFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_LOCKED_PASSWORD_HASH]
    }

    val defaultFontFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_DEFAULT_FONT] ?: "সোনার তরী"
    }

    suspend fun setThemePreset(preset: ThemePreset) {
        context.dataStore.edit { preferences ->
            preferences[KEY_THEME_PRESET] = preset.name
        }
    }

    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DARK_MODE] = isDark
        }
    }

    suspend fun setHiddenNotesPasswordHash(hash: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_HIDDEN_PASSWORD_HASH] = hash
        }
    }

    suspend fun setLockedNotesPasswordHash(hash: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LOCKED_PASSWORD_HASH] = hash
        }
    }

    suspend fun setDefaultFont(fontName: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DEFAULT_FONT] = fontName
        }
    }
}
