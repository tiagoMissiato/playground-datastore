package com.tiagomissiato.playgrounddatastore.customdata

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SettingsDataSource @Inject constructor(private val dataSource: DataStore<Settings>) {

    suspend fun save(setting: Settings) {
        dataSource.updateData {
            setting
        }
    }

    suspend fun readSettings(): Settings =
        dataSource.data.first()

}