package com.tiagomissiato.playgrounddatastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.tiagomissiato.playgrounddatastore.Settings
import com.tiagomissiato.playgrounddatastore.serializer.ProtoSerializer
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
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("preference_file")
            }
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStore(@ApplicationContext context: Context): DataStore<Settings> {
        return DataStoreFactory.create(
            serializer = ProtoSerializer(),
            produceFile = {
                context.dataStoreFile("settings_proto")
            }
        )
    }

}