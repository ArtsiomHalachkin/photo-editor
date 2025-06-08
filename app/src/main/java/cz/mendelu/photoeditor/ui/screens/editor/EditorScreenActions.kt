package cz.mendelu.photoeditor.ui.screens.editor

import android.graphics.Bitmap


interface EditorScreenActions {
    fun updateBrightness(value: Float)
    fun updateContrast(value: Float)
    fun updateSaturation(value: Float)
    fun updateShadow(value: Float)

    fun setImage(bitmap: Bitmap)
    fun savePhoto()
    fun loadBack()
    fun loadPhoto(id: Long)

}