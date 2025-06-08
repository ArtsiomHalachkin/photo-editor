package cz.mendelu.photoeditor.ui.screens.settings

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.mendelu.photoeditor.database.IPhotosLocalRepository
import cz.mendelu.photoeditor.database.Photo
import cz.mendelu.photoeditor.datastore.DataStoreRepositoryImpl
import cz.mendelu.photoeditor.datastore.IDataStoreRepository
import cz.mendelu.photoeditor.utils.ImageProcessor
import cz.mendelu.photoeditor.utils.ImageUtils.Companion.bitmapToByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val dataStoreRepository: IDataStoreRepository
) : ViewModel(), SettingsScreenActions {

    private val _settingsUIState: MutableStateFlow<SettingsScreenUIState> =
        MutableStateFlow(value = SettingsScreenUIState())

    val settingsUIState = _settingsUIState.asStateFlow()



    override fun updateBrightness(value: Float) {
        _settingsUIState.value = _settingsUIState.value.copy(brightness = value)
        applyBrightness()
    }

    override fun updateContrast(value: Float) {
        _settingsUIState.value = _settingsUIState.value.copy(contrast = value)
        applyContrast()
    }

    override fun updateSaturation(value: Float) {
        _settingsUIState.value = _settingsUIState.value.copy(saturation = value)
        applySaturation()

    }

    override fun updateShadow(value: Float) {
        _settingsUIState.value = _settingsUIState.value.copy(shadow = value)
        applyShadow()
    }

    override fun setSampleImage(bitmap: Bitmap) {
        _settingsUIState.value = _settingsUIState.value.copy(originalImage = bitmap, processedImage = bitmap)
    }

    override fun initFilers() {
        viewModelScope.launch {
            updateBrightness(dataStoreRepository.getBrightness())
            updateShadow(dataStoreRepository.getShadow())
            updateContrast(dataStoreRepository.getContrast())
            updateSaturation(dataStoreRepository.getSaturation())
        }
    }

    override fun accept() {
        viewModelScope.launch {
            dataStoreRepository.setBrightness(
                _settingsUIState.value.brightness
            )
            dataStoreRepository.setSaturation(
                _settingsUIState.value.saturation
            )
            dataStoreRepository.setContrast(
                _settingsUIState.value.contrast
            )
            dataStoreRepository.setShadow(
                _settingsUIState.value.shadow
            )
        }
    }

    override fun loadBack() {
        _settingsUIState.value = _settingsUIState.value.copy(
            originalImage = _settingsUIState.value.originalImage,
            processedImage = _settingsUIState.value.originalImage,
            brightness = 0f,
            saturation = 1f,
            contrast = 1f,
            shadow = 0f
        )
    }


    private fun applyContrast() {
        val original = _settingsUIState.value.originalImage

        viewModelScope.launch {
            val processed = ImageProcessor.applyContrast(
                original!!,
                contrast = _settingsUIState.value.contrast,

                )
            _settingsUIState.value = _settingsUIState.value.copy(processedImage = processed)
        }
    }

    private fun applyBrightness(){
        val original = _settingsUIState.value.originalImage

        viewModelScope.launch {
            val processed = ImageProcessor.applyBrightness(
                original!!,
                brightness = _settingsUIState.value.brightness,

                )
            _settingsUIState.value = _settingsUIState.value.copy(processedImage = processed)
        }
    }
    private fun applyShadow(){
        val original = _settingsUIState.value.originalImage

        viewModelScope.launch {
            val processed = ImageProcessor.applyShadow(
                original!!,
                shadow = _settingsUIState.value.shadow,

                )
            _settingsUIState.value = _settingsUIState.value.copy(processedImage = processed)
        }
    }

    private fun applySaturation(){
        val original = _settingsUIState.value.originalImage

        viewModelScope.launch {
            val processed = ImageProcessor.applySaturation(
                original!!,
                saturation = _settingsUIState.value.saturation,

                )
            _settingsUIState.value = _settingsUIState.value.copy(processedImage = processed)
        }
    }

}