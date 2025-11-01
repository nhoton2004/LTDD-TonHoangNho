package com.example.uthsmarttasks

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.uthsmarttasks.navigation.NavGraph
import com.example.uthsmarttasks.screens.NotificationDetailScreen
import com.example.uthsmarttasks.ui.theme.UthsmarttasksTheme

class MainActivity : androidx.fragment.app.FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }


        val title = intent.getStringExtra("title")
        val message = intent.getStringExtra("message")

        setContent {
            UthsmarttasksTheme {
                if (title != null && message != null) {
                    NotificationDetailScreen(title, message)
                } else {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }

    //Xin quyền thông báo
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {

            }
        }
}
