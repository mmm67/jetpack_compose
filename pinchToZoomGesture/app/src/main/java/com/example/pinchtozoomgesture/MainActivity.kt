package com.example.pinchtozoomgesture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import com.example.pinchtozoomgesture.ui.theme.PinchToZoomGestureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PinchToZoomGestureTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PinchToZoomView(Modifier.padding(innerPadding))
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PinchToZoomView(modifier: Modifier = Modifier) {
        var scale by remember { mutableFloatStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }

        fun calculateNewOffset(maxWidth: Int, maxHeight: Int, pan: Offset) {
            val extraWidth = (scale - 1) * maxWidth
            val extraHeight = (scale - 1) * maxHeight

            val maxX = extraWidth / 2
            val maxY = extraHeight / 2

            offset = Offset(
                x = (offset.x + scale * pan.x).coerceIn(-maxX, maxX),
                y = (offset.y + scale * pan.y).coerceIn(-maxY, maxY),
            )
        }

        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .aspectRatio(1f)
        ) {
            Image(
                Icons.Filled.Face,
                contentDescription = "Face",
                modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(onDoubleClick = {
                        scale = 1f
                        offset = Offset.Zero
                    }) {}
                    .transformable(
                        state = rememberTransformableState { zoom, pan, _ ->
                            scale = (scale * zoom).coerceIn(1f, 3f)
                            calculateNewOffset(constraints.maxWidth, constraints.maxHeight, pan)
                        })
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            )
        }

    }
}
