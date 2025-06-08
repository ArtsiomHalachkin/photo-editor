package cz.mendelu.photoeditor.ui.screens.editor

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.mendelu.photoeditor.database.IPhotosLocalRepository
import cz.mendelu.photoeditor.database.Photo
import cz.mendelu.photoeditor.ui.screens.preview.PreviewScreenUIState
import cz.mendelu.photoeditor.utils.ImageProcessor
import cz.mendelu.photoeditor.utils.ImageUtils.Companion.bitmapToByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorScreenViewModel @Inject constructor(
    private val repository: IPhotosLocalRepository,
) : ViewModel(), EditorScreenActions {

    private val _editorUIState: MutableStateFlow<EditorScreenUIState> =
        MutableStateFlow(value = EditorScreenUIState())

    val editorUIState = _editorUIState.asStateFlow()



    override fun updateBrightness(value: Float) {
        _editorUIState.value = _editorUIState.value.copy(brightness = value)
        applyBrightness()
    }

    override fun updateContrast(value: Float) {
        _editorUIState.value = _editorUIState.value.copy(contrast = value)
        applyContrast()
    }

    override fun updateSaturation(value: Float) {
        _editorUIState.value = _editorUIState.value.copy(saturation = value)
        applySaturation()

    }

    override fun updateShadow(value: Float) {
        _editorUIState.value = _editorUIState.value.copy(shadow = value)
        applyShadow()
    }


    override fun setImage(bitmap: Bitmap) {
        _editorUIState.value = _editorUIState.value.copy(originalImage = bitmap, processedImage = bitmap)

    }

    private fun applyContrast() {
        val original = _editorUIState.value.originalImage

        viewModelScope.launch {
            val processed = ImageProcessor.applyContrast(
                original!!,
                contrast = _editorUIState.value.contrast,

                )
            _editorUIState.value = _editorUIState.value.copy(processedImage = processed)
        }
    }

    private fun applyBrightness(){
        val original = _editorUIState.value.originalImage

        viewModelScope.launch {
            val processed = ImageProcessor.applyBrightness(
                original!!,
                brightness = _editorUIState.value.brightness,

                )
            _editorUIState.value = _editorUIState.value.copy(processedImage = processed)

        }
    }
    private fun applyShadow(){
        val original = _editorUIState.value.originalImage

        viewModelScope.launch {
            val processed = ImageProcessor.applyShadow(
                original!!,
                shadow = _editorUIState.value.shadow,

                )
            _editorUIState.value = _editorUIState.value.copy(processedImage = processed)

        }
    }

    private fun applySaturation(){
        val original = _editorUIState.value.originalImage

        viewModelScope.launch {
            val processed = ImageProcessor.applySaturation(
                original!!,
                saturation = _editorUIState.value.saturation,

                )
            _editorUIState.value = _editorUIState.value.copy(processedImage = processed)
        }
    }



    override fun savePhoto() {
        viewModelScope.launch {
            _editorUIState.value.processedImage?.let { bitmap ->
                val byteArray = bitmapToByteArray(bitmap)
                val photo = Photo(
                    url = "",
                    name = "myphoto.jpg",
                    storageName = "",
                    description = "",
                    iso = ""
                )

                repository.uploadAndSavePhoto(
                    photoObject = photo,
                    photoBytes = byteArray
                )

                _editorUIState.value = _editorUIState.value.copy(
                    newPhotoSaved = true
                )
            }
        }

    }

    override fun loadBack() {
        _editorUIState.value = _editorUIState.value.copy(
                originalImage = _editorUIState.value.originalImage,
                processedImage = _editorUIState.value.originalImage,
            brightness = 0f,
            saturation = 1f,
            contrast = 1f,
            shadow = 0f

        )
    }


    override fun loadPhoto(id: Long) {
        viewModelScope.launch {
            _editorUIState.value = _editorUIState.value.copy(
                photo = repository.getPhotoById(id),
                photoLoadedSuccesfuly = true
            )
        }
    }

}