package cz.mendelu.photoeditor.ui.screens.settings

import android.graphics.Bitmap
data class SettingsScreenUIState(

    val originalImage: Bitmap? = null,
    val processedImage: Bitmap? = null,
    val brightness: Float = 0f,
    val contrast: Float = 1f,
    val saturation: Float = 1f,
    val shadow: Float = 0f,

)
