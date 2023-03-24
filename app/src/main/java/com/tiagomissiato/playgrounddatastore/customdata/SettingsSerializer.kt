package com.tiagomissiato.playgrounddatastore.customdata

import androidx.datastore.core.Serializer
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

class SettingsSerializer(override val defaultValue: Settings = Settings()) : Serializer<Settings> {

    val moshi = Moshi
        .Builder()
        .build()
    val adapter: JsonAdapter<Settings> = moshi.adapter(Settings::class.java).lenient()

    override suspend fun readFrom(input: InputStream): Settings {
        return try {
            adapter.fromJson(input.toString()) ?: Settings()
        } catch (e: Exception) {
            Settings()
        }
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                adapter.toJson(t).encodeToByteArray()
            )
        }
    }

}


data class Settings(
    val intParam: Int = 0,
    val stringParam: String = "",
    val listParam: List<String> = emptyList()
)