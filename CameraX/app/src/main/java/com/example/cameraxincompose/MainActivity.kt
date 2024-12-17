package com.example.cameraxincompose

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.cameraxincompose.ui.theme.CameraXInComposeTheme
import java.security.Permission

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CameraXInComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun MainContent(modifier: Modifier = Modifier) {
        var isGranted by remember { mutableStateOf(false) }
        CheckCameraPermission { granted ->
            isGranted = granted
        }

        if (isGranted) {
            CameraPreviewScreen()
        }
    }

    @Composable
    fun CheckCameraPermission(onGranted: (Boolean) -> Unit) {
        val cameraPermissionRequest =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    onGranted(true)
                } else {
                    onGranted(false)
                }
            }
        val cameraPermissionsAlreadyGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        LaunchedEffect(cameraPermissionsAlreadyGranted) {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }
}

