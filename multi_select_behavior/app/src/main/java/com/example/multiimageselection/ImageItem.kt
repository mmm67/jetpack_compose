package com.example.multiimageselection

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.UUID

@Stable
data class Image(val id: String = UUID.randomUUID().toString(), val imageId: ImageVector)
