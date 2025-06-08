package cz.mendelu.photoeditor.ui.screens.settings

import android.graphics.Bitmap


interface SettingsScreenActions {
    fun updateBrightness(value: Float)
    fun updateContrast(value: Float)
    fun updateSaturation(value: Float)
    fun updateShadow(value: Float)

    fun setSampleImage(bitmap: Bitmap)

    fun initFilers()
    fun accept()
    fun loadBack()

}