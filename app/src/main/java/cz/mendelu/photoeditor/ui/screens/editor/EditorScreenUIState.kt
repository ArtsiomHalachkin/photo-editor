package cz.mendelu.photoeditor.ui.screens.editor

import android.graphics.Bitmap
import cz.mendelu.photoeditor.database.Photo

data class EditorScreenUIState(
    var photo: Photo = Photo(
        url = "",
        name = "myphoto.jpg",
        storageName = "",
        description = "",
        iso = ""
    ),

    var photoLoadedSuccesfuly: Boolean = false,
    var newPhotoSaved: Boolean = false,


    val originalImage: Bitmap? = null,
    val processedImage: Bitmap? = null,
    val brightness: Float = 0f,
    val contrast: Float = 1f,
    val saturation: Float = 1f,
    val shadow: Float = 0f
)
