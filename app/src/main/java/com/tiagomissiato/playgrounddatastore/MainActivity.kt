package com.tiagomissiato.playgrounddatastore

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tiagomissiato.playgrounddatastore.customdata.Settings
import com.tiagomissiato.playgrounddatastore.customdata.SettingsDataSource
import com.tiagomissiato.playgrounddatastore.preferece.PreferenceDataSource
import com.tiagomissiato.playgrounddatastore.ui.theme.PlaygroundDatastoreTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    @Inject
    lateinit var dataStore: PreferenceDataSource

    @Inject
    lateinit var settingsStore: SettingsDataSource

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
        val newValues = Settings(
                5,
                "String",
                listOf("1", "2")
            )
        settingsStore.save(newValues)
        val settingsData = settingsStore.readSettings()
        Log.i("DEBUG", "intParam ${settingsData.intParam}")
        Log.i("DEBUG", "StringParam ${settingsData.stringParam}")
        settingsData.listParam.forEachIndexed{ index, value ->
            Log.i("DEBUG", "listParam[$index] $value")
        }
    }

    private suspend fun savePreference() {
        dataStore.saveInt("prefKeyInt", 5)
        dataStore.saveString("prefKeyString", "6")
        dataStore.saveBoolean("prefKeyBoolean", false)

        Log.i("DEBUG", "prefKeyInt ${dataStore.readInt("prefKeyInt")}")
        Log.i("DEBUG", "prefKeyInt ${dataStore.readString("prefKeyString")}")
        Log.i("DEBUG", "prefKeyInt ${dataStore.readBoolean("prefKeyBoolean")}")

//        dataStore.edit {
//            it[intPreferencesKey("prefKeyInt")] = 5
//            it[stringPreferencesKey("prefKeyString")] = "5"
//            it[booleanPreferencesKey("prefKeyBoolean")] = true
//        }
//        dataStore.data.collect {
//            Log.i("DEBUG", "prefKeyInt ${it[intPreferencesKey("prefKeyInt")]}")
//            Log.i("DEBUG", "prefKeyString ${it[stringPreferencesKey("prefKeyString")]}")
//            Log.i("DEBUG", "prefKeyBoolean ${it[booleanPreferencesKey("prefKeyBoolean")]}")
//        }
    }
}

@Composable
fun Greeting(onSavePreference: () -> Unit, onSaveSettings: () -> Unit) {
    Row(modifier = Modifier) {
        Button(onClick = { onSavePreference() }) {
            Text(text = "Save preference")
        }

        Button(onClick = { onSaveSettings() }) {
            Text(text = "Save settings")
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