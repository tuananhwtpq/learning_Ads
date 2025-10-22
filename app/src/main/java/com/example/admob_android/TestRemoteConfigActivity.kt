package com.example.admob_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.admob_android.databinding.ActivityTestRemoteConfigBinding
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class TestRemoteConfigActivity : AppCompatActivity() {

    private companion object {
        const val TAG = "TestRemoteConfigActivity"
    }

    private lateinit var binding: ActivityTestRemoteConfigBinding

    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestRemoteConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        remoteConfig = Firebase.remoteConfig

        val configSetting = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 30
        }
        remoteConfig.setConfigSettingsAsync(configSetting)

        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Fetch and active succeeded")
                    val content = remoteConfig.getString("content")
                    binding.tvAfter.text = content
                } else {
                    Log.d(TAG, "Fetch failed")
                }
            }

    }
}