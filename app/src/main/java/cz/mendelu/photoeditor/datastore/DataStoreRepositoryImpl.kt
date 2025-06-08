package cz.mendelu.photoeditor.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class DataStoreRepositoryImpl(private val context: Context) : IDataStoreRepository {

    override suspend fun setContrast(contrast: Float) {
        val preferencesKey = floatPreferencesKey(DataStoreConstants.CONTRAST)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = contrast
        }
    }

    override suspend fun setBrightness(brightness: Float) {
        val preferencesKey = floatPreferencesKey(DataStoreConstants.BRIGHTNESS  )
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = brightness
        }
    }

    override suspend fun setSaturation(saturation: Float) {
        val preferencesKey = floatPreferencesKey(DataStoreConstants.SATURATION)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = saturation
        }
    }

    override suspend fun setShadow(shadow: Float) {
        val preferencesKey = floatPreferencesKey(DataStoreConstants.SHADOW)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = shadow
        }
    }

    override suspend fun getContrast(): Float {
        return try {
            val preferencesKey = floatPreferencesKey(DataStoreConstants.CONTRAST)
            val preferences = context.dataStore.data.first()
            if (!preferences.contains(preferencesKey))
                1f
            else
                preferences[preferencesKey]!!
        } catch (e: Exception) {
            e.printStackTrace()
            1f
        }
    }

    override suspend fun getBrightness(): Float {
        return try {
            val preferencesKey = floatPreferencesKey(DataStoreConstants.BRIGHTNESS)
            val preferences = context.dataStore.data.first()
            if (!preferences.contains(preferencesKey))
                1f
            else
                preferences[preferencesKey]!!
        } catch (e: Exception) {
            e.printStackTrace()
            1f
        }
    }

    override suspend fun getSaturation(): Float {
        return try {
            val preferencesKey = floatPreferencesKey(DataStoreConstants.SATURATION)
            val preferences = context.dataStore.data.first()
            if (!preferences.contains(preferencesKey))
                1f
            else
                preferences[preferencesKey]!!
        } catch (e: Exception) {
            e.printStackTrace()
            1f
        }
    }

    override suspend fun getShadow(): Float {
        return try {
            val preferencesKey = floatPreferencesKey(DataStoreConstants.SHADOW)
            val preferences = context.dataStore.data.first()
            if (!preferences.contains(preferencesKey))
                0f
            else
                preferences[preferencesKey]!!
        } catch (e: Exception) {
            e.printStackTrace()
            0f
        }
    }
    /*

    override suspend fun getSupabaseUrl(): String {
        val preferencesKey = stringPreferencesKey(DataStoreConstants.SUPABASE_URL)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey]!!
    }

    override suspend fun getSupabaseKey(): String {
        val preferencesKey = stringPreferencesKey(DataStoreConstants.SUPABASE_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey]!!
    }

    override suspend fun setSupabaseKey(key: String) {
        val preferencesKey = stringPreferencesKey(DataStoreConstants.SUPABASE_KEY)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = key
        }
    }

    override suspend fun setSupabaseUrl(url: String) {
        val preferencesKey = stringPreferencesKey(DataStoreConstants.SUPABASE_URL)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = url
        }
    }

     */
}
