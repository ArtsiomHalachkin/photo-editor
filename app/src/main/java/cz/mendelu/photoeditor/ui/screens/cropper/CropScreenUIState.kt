package cz.mendelu.photoeditor.ui.screens.cropper

import android.graphics.Bitmap
import cz.mendelu.photoeditor.database.Photo

data class CropScreenUIState(
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
)
