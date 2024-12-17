package com.example.providephotowithcameraandgallery

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.providephotowithcameraandgallery.ui.theme.ProvidePhotoWithCameraAndGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProvidePhotoWithCameraAndGalleryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(Modifier.padding(innerPadding))
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainContent(modifier: Modifier = Modifier) {
        var selectedPhoto by remember {
            mutableStateOf<Uri?>(null)
        }
        var showBottomSheet by remember { mutableStateOf(false) }
        var showCameraScreen by remember { mutableStateOf(false) }
        var pickFromGallery by remember { mutableStateOf(false) }


        var isGranted by remember { mutableStateOf(false) }
        CheckCameraPermission { granted -> isGranted = granted }

        if (isGranted) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (showCameraScreen) {
                    CameraPreviewScreen(modifier) { uri ->
                        showCameraScreen = false
                        selectedPhoto = uri

                    }
                } else if (pickFromGallery) {
                    PickImageFromGallery {
                        pickFromGallery = false
                        selectedPhoto = it
                    }
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(selectedPhoto ?: R.drawable.defaultphoto)
                            .crossfade(true)
                            .build(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .size(200.dp)
                            .clip(shape = CircleShape)
                            .clickable { showBottomSheet = true },
                    )
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Take Photo Option
                    TextButton(onClick = {
                        showBottomSheet = false
                        showCameraScreen = true
                    }) {
                        Text(text = "Take Photo")
                    }

                    // Select from Gallery Option
                    TextButton(onClick = {
                        showBottomSheet = false
                        pickFromGallery = true

                    }) {
                        Text(text = "Select from Gallery")
                    }
                }
            }
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
