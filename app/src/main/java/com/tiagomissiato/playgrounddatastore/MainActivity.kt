package com.tiagomissiato.playgrounddatastore

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
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
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.tiagomissiato.playgrounddatastore.ui.theme.PlaygroundDatastoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "datastore_preferences",
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, "shared_preference"))
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
            putBoolean("SharedBooleanValue", false)
        }
    }

    private suspend fun savePreference() {
        val intKey = intPreferencesKey("intKey")
        val stringKey = stringPreferencesKey("stringKey")

        dataStore.edit {
            it[intKey] = 10
            it[stringKey] = "My preference string"
        }
    }

    private suspend fun readPreference() {
        val intKey = intPreferencesKey("intKey")
        val stringKey = stringPreferencesKey("stringKey")

        val intSharedPref = intPreferencesKey("SharedIntValue")
        val stringSharedPref = stringPreferencesKey("SharedStringValue")
        val booleanSharedPref = booleanPreferencesKey("SharedBooleanValue")

        val pref = dataStore.data.first()
        Log.i("DEBUG", "-----")
        Log.i("DEBUG", "int original DataStore value: ${pref[intKey]}")
        Log.i("DEBUG", "string original DataStore value: ${pref[stringKey]}")
        Log.i("DEBUG", "-----")
        Log.i("DEBUG", "int migrated from SharedPref value: ${pref[intSharedPref]}")
        Log.i("DEBUG", "string migrated from SharedPref value: ${pref[stringSharedPref]}")
        Log.i("DEBUG", "boolean migrated from SharedPref value: ${pref[booleanSharedPref]}")
        Log.i("DEBUG", "-----")
    }
}

@Composable
fun Greeting(onSavePreference: () -> Unit, readPreference: () -> Unit) {
    Column(modifier = Modifier) {
        Button(onClick = { onSavePreference() }) {
            Text(text = "Save preference")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { readPreference() }) {
            Text(text = "Read from preference")
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