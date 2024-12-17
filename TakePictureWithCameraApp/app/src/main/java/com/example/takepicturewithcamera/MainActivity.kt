package com.example.takepicturewithcamera

import android.graphics.Paint.Cap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import com.example.takepicturewithcamera.ui.theme.TakePictureWithCameraTheme
import java.io.File

class MainActivity : ComponentActivity() {
    companion object {
        const val AUTHORITY = "com.example.takepicturewithcamera.provider"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TakePictureWithCameraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExternalCodeScanner(Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun RecordVideo_WithCameraApp(modifier: Modifier = Modifier) {
        // #1: Set the location where the image will be stored
        val context = LocalContext.current
        val uri = remember {
            FileProvider.getUriForFile(
                context, AUTHORITY,
                File(context.filesDir, "video.MP4")
            )
        }

        // #2: Define how to launch external component
        var captureSuccess by remember { mutableStateOf(false) }
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.CaptureVideo()
        ) { captureSuccess = it }

        // #3: Show UI
        Box(modifier, contentAlignment = Alignment.Center) {
            if (captureSuccess) {
                Text("Successfully saved video")
            } else {
                Button({ launcher.launch(uri) }) {
                    Text("Record video")
                }
            }
        }
    }

    @Composable
    fun TakePicture_WithCameraApp(modifier: Modifier = Modifier) {
        // #1: Set the location where the image will be stored
        val context = LocalContext.current
        val uri = remember {
            FileProvider.getUriForFile(
                context,
                AUTHORITY,
                File(context.filesDir, "image.jpg")
            )
        }

        // #2: Define how to launch external component
        var captureSuccess by remember { mutableStateOf(false) }
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { captureSuccess = it }

        // #3: Show UI
        Box(modifier, contentAlignment = Alignment.Center) {
            if (captureSuccess) {
                AsyncImage(uri, contentDescription = null)
            } else {
                Button({ launcher.launch(uri) }) {
                    Text("Take picture")
                }
            }
        }
    }
}