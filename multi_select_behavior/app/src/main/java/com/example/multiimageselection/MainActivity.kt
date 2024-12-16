package com.example.multiimageselection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.multiimageselection.ui.theme.MultiImageSelectionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiImageSelectionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    val images = listOf(
        Image(imageId = R.drawable.add),
        Image(imageId = R.drawable.art),
        Image(imageId = R.drawable.warn),
        Image(imageId = R.drawable.category),
        Image(imageId = R.drawable.changepass),
        Image(imageId = R.drawable.dailyhabit),
        Image(imageId = R.drawable.deleteaccount),
        Image(imageId = R.drawable.signup),
        Image(imageId = R.drawable.notification),
        Image(imageId = R.drawable.lifestyle),
        Image(imageId = R.drawable.home),
        Image(imageId = R.drawable.entertainment),
        Image(imageId = R.drawable.health),
        Image(imageId = R.drawable.outdoor),
        Image(imageId = R.drawable.timer),
        Image(imageId = R.drawable.other)
    )

    ImageGrid(modifier, images)
}


@Composable
fun ImageGrid(modifier: Modifier = Modifier, images: List<Image>) {
    var selectedIds by rememberSaveable { mutableStateOf(emptySet<String>()) }
    val inSelectionMode by remember { derivedStateOf { selectedIds.isNotEmpty() } }

    LazyVerticalGrid(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp),
        columns = GridCells.Adaptive(128.dp)
    ) {
        items(images, key = { it.id }) { image ->
            ImageItem(
                image, inSelectionMode, selected = selectedIds.contains(image.id),
                onClick = { id ->
                    selectedIds = if (selectedIds.contains(id)) {
                        selectedIds.minus(id)
                    } else {
                        selectedIds.plus(id)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageItem(
    image: Image,
    inSelectionMode: Boolean,
    selected: Boolean,
    onClick: (String) -> Unit
) {
    Surface(
        shadowElevation = 10.dp,
        modifier = Modifier
            .aspectRatio(1f)
            .combinedClickable(onLongClick = {
                if (!inSelectionMode) {
                    onClick(image.id)
                }
            }) {
                if (inSelectionMode) {
                    onClick(image.id)
                }
            }
            .border(
                width = 2.dp,
                shape = RoundedCornerShape(4.dp),
                color = if (selected) Color.Black else Color.Transparent
            )
    ) {
        Image(
            painter = painterResource(image.imageId),
            contentDescription = "image"
        )
    }
}
