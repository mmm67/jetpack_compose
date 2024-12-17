package com.example.takepicturewithcamera

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

@Composable
fun ScanDocumentScreen(modifier: Modifier = Modifier) {

    // #1: Create a configured scanning client
    val scanner = remember {
        val options = GmsDocumentScannerOptions.Builder()
            .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
            .setScannerMode(SCANNER_MODE_FULL)
            .setPageLimit(5)
            .setGalleryImportAllowed(true)
            .build()
        GmsDocumentScanning.getClient(options)
    }

    // #2: Define how to launch external component
    var scanResult by remember { mutableStateOf<GmsDocumentScanningResult?>(null) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        scanResult = GmsDocumentScanningResult
            .fromActivityResultIntent(result.data)
    }

    val activity = LocalContext.current.findActivity()
    val launchAction: () -> Unit = {
        scanner.getStartScanIntent(activity)
            .addOnSuccessListener {
                launcher.launch(IntentSenderRequest.Builder(it).build())
            }
            .addOnFailureListener { /* Deal with failure */ }
    }

    // #3: Show UI
    val firstPageImageUri = scanResult?.pages?.first()
    val pageCount = scanResult?.pdf?.pageCount
    if (firstPageImageUri != null) {
        Column(modifier) {
            Text("Page count: $pageCount")
            AsyncImage(firstPageImageUri, null)
        }
    } else {
        Button(onClick = launchAction, modifier) {
            Text("Start scan!")
        }
    }
}

private fun Context.findActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Cannot start scanning document without access to Activity")
}