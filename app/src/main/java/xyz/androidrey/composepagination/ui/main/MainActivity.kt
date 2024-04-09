package xyz.androidrey.composepagination.ui.main

import MyAppTheme
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import xyz.androidrey.composepagination.domain.service.TheForegroundService
import xyz.androidrey.composepagination.ui.chat.ChatScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val serviceIntent by lazy {
        Intent(this, TheForegroundService::class.java)
    }

    private fun startService() {
        startService(serviceIntent)
    }

    private fun stopService() {
        stopService(serviceIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                100
            )
        }
        setContent {
            MyAppTheme {
                ChatScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startService()
    }

    override fun onPause() {
        super.onPause()
        stopService()
    }
}