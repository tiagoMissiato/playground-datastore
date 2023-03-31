package com.tiagomissiato.playgrounddatastore

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import com.tiagomissiato.playgrounddatastore.ui.theme.PlaygroundDatastoreTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Settings> by dataStore(
    fileName = "settings_proto",
    serializer = SettingsSerializer,
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(
                context,
                "shared_preference"
            ) { sharedPrefs: SharedPreferencesView, currentData: Settings ->
                currentData
                    .toBuilder()
                    .setIntSharedPref(sharedPrefs.getInt("SharedIntValue", -1))
                    .setStringSharedPref(sharedPrefs.getString("SharedStringValue", ""))
                    .build()
            }
        )
    }
)

class MainActivity : ComponentActivity() {

    private val sharedPreference by lazy {
        getSharedPreferences("shared_preference", MODE_PRIVATE)
    }

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
                            saveSharedPreference()
                        },
                        {
                            scope.launch {
                                savePreference()
                            }
                        }, {
                            scope.launch {
                                readPreference()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun saveSharedPreference() {
        sharedPreference.edit {
            putInt("SharedIntValue", 199)
            putString("SharedStringValue", "Migrated from shared preference")
        }
    }

    private suspend fun savePreference() {
        dataStore.updateData { settings ->
            settings.toBuilder()
                .setIntSettings(7)
                .setStringSettings("Proto String Settings")
                .build()
        }
    }

    private suspend fun readPreference() {
        val settings = dataStore.data.first()
        Log.i("DEBUG", "-----")
        Log.i("DEBUG", "int original DataStore value: ${settings.intSettings}")
        Log.i("DEBUG", "string original DataStore value: ${settings.stringSettings}")
        Log.i("DEBUG", "-----")
        Log.i("DEBUG", "int migrated from SharedPref value: ${settings.intSharedPref}")
        Log.i("DEBUG", "string migrated from SharedPref value: ${settings.stringSharedPref}")
        Log.i("DEBUG", "-----")
    }
}

@Composable
fun Greeting(onSaveSharedPreference: () -> Unit, onSavePreference: () -> Unit, readPreference: () -> Unit) {
    Column(modifier = Modifier) {
        Button(onClick = { onSaveSharedPreference() }) {
            Text(text = "Save shared preference")
        }
        Button(onClick = { onSavePreference() }) {
            Text(text = "Save proto")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { readPreference() }) {
            Text(text = "Read from proto")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlaygroundDatastoreTheme {
        Greeting({}, { }, { })
    }
}