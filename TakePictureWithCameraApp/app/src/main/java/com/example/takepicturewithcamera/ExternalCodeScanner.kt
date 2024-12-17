package com.example.takepicturewithcamera

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun ExternalCodeScanner(modifier: Modifier = Modifier) {
    // #1: Create a configured scanning client
    val context = LocalContext.current
    val scanner = remember {
        val options = GmsBarcodeScannerOptions.Builder()
            .enableAutoZoom()
            .allowManualInput()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
        GmsBarcodeScanning.getClient(context, options)
    }

    // #2: Define how to launch external component
    var barcode by remember { mutableStateOf<Barcode?>(null) }
    val launchAction: () -> Unit = {
        scanner.startScan()
            .addOnSuccessListener { barcode = it }
            .addOnCanceledListener { /* Deal with cancellation */ }
            .addOnFailureListener { /* Deal with failure */ }
    }

    // #3: Show UI
    val barcodeValue = barcode?.rawValue
    if (barcodeValue != null) {
        Text("Scanned: $barcodeValue", modifier)
    } else {
        Button(onClick = launchAction, modifier) {
            Text("Start code scan!")
        }
    }
}