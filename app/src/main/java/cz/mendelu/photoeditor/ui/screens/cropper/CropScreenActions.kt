package cz.mendelu.photoeditor.ui.screens.cropper

import android.graphics.Bitmap


interface CropScreenActions {

    fun setImage(bitmap: Bitmap)
    fun savePhoto()
    fun loadBack()
    fun loadPhoto(id: Long)
    fun apply(bitmap: Bitmap)

}