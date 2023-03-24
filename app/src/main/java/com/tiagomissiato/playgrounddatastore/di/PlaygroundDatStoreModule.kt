package com.tiagomissiato.playgrounddatastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.tiagomissiato.playgrounddatastore.customdata.Settings
import com.tiagomissiato.playgrounddatastore.customdata.SettingsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PlaygroundDatStoreModule {

    @Singleton
    @Provides
    fun providePreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("preference_file")
            }
        )
    }

    @Singleton
    @Provides
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Settings> {
        return DataStoreFactory.create(
            serializer = SettingsSerializer(),
            produceFile = {
                context.preferencesDataStoreFile("preference_file_settings")
            }
        )
    }

}