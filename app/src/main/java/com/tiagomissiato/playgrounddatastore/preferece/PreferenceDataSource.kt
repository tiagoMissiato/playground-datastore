package com.tiagomissiato.playgrounddatastore.preferece

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferenceDataSource @Inject constructor(private val dataSource: DataStore<Preferences>) {

    suspend fun saveInt(key: String, value: Int) {
        dataSource.edit {
            it[intPreferencesKey(key)] = value
        }
    }

    suspend fun saveString(key: String, value: String) {
        dataSource.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    suspend fun saveBoolean(key: String, value: Boolean) {
        dataSource.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun readInt(key: String): Int =
        dataSource.data.first()[intPreferencesKey(key)] ?: 0

    suspend fun readString(key: String): String =
        dataSource.data.first()[stringPreferencesKey(key)] ?: ""

    suspend fun readBoolean(key: String): Boolean =
        dataSource.data.first()[booleanPreferencesKey(key)] ?: true
}