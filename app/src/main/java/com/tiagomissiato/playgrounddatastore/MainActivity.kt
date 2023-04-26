package com.tiagomissiato.playgrounddatastore

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tiagomissiato.playgrounddatastore.ui.theme.PlaygroundDatastoreTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesDataStore: DataStore<Preferences>

    @Inject
    lateinit var protoDataStore: DataStore<Settings>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundDatastoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scope = rememberCoroutineScope()

                    Greeting(
                        {
                            scope.launch {
                                savePreference()
                            }
                        }, {
                            scope.launch {
                                saveSettings()
                            }
                        }
                    )
                }
            }
        }
    }

    private suspend fun saveSettings() {
        protoDataStore.updateData { settings ->
            settings
                .toBuilder()
                .setIntSettings(7)
                .setStringSettings("Proto String Settings")
                .build()
        }

        val data = protoDataStore.data.first()

        Log.i("DEBUG", "proto int value ${data.intSettings}")
        Log.i("DEBUG", "proto string value ${data.stringSettings}")
    }

    private suspend fun savePreference() {
        val intKey = intPreferencesKey("prefKeyInt")
        val stringKey = stringPreferencesKey("prefKeyString")
        val booleanKey = booleanPreferencesKey("prefKeyBoolean")

        preferencesDataStore.edit { pref ->
            pref[intKey] = 5
            pref[stringKey] = "6"
            pref[booleanKey] = false
        }

        val data = preferencesDataStore.data.first()

        Log.i("DEBUG", "prefKeyInt ${data[intKey]}")
        Log.i("DEBUG", "prefKeyInt ${data[stringKey]}")
        Log.i("DEBUG", "prefKeyInt ${data[booleanKey]}")
    }
}

@Composable
fun Greeting(onSavePreference: () -> Unit, onSaveSettings: () -> Unit) {
    Column(modifier = Modifier) {
        Button(onClick = { onSavePreference() }) {
            Text(text = "Save data store preferences")
        }

        Button(onClick = { onSaveSettings() }) {
            Text(text = "Save proto settings")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlaygroundDatastoreTheme {
        Greeting({ }, { })
    }
}