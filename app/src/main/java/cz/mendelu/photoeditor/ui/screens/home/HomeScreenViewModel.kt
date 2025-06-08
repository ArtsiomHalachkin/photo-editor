package cz.mendelu.photoeditor.ui.screens.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.mendelu.photoeditor.database.IPhotosLocalRepository
import cz.mendelu.photoeditor.database.Photo
import cz.mendelu.photoeditor.datastore.IDataStoreRepository
import cz.mendelu.photoeditor.utils.ImageProcessor
import cz.mendelu.photoeditor.utils.ImageUtils.Companion.bitmapToByteArray
import cz.mendelu.photoeditor.utils.ImageUtils.Companion.byteArrayToBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor
    (private val repository: IPhotosLocalRepository,
     private val dataStoreRepository: IDataStoreRepository) : ViewModel(), HomeScreenActions
{

    private val _homeScreenUIState: MutableStateFlow<HomeScreenUIState>
            = MutableStateFlow(value = HomeScreenUIState())

    val homeScreenUIState = _homeScreenUIState.asStateFlow()

    override fun uploadPickedPhoto(imageBytes: ByteArray) {
        viewModelScope.launch {
            val processedBitmap = applyFilters(imageBytes)
            val processedByteArray = bitmapToByteArray(processedBitmap)
            repository.uploadAndSavePhoto(
                photoObject = _homeScreenUIState.value.photo,
                photoBytes = processedByteArray
            )
            _homeScreenUIState.value = _homeScreenUIState.value.copy(photoPicked = true)
        }
    }

    override fun loadRecentPhotos() {
        viewModelScope.launch {
            repository.getLastPhotos().collect { photos ->
                _homeScreenUIState.value = _homeScreenUIState.value.copy(
                    photos = photos)
            }
        }
    }



    @SuppressLint("SuspiciousIndentation")
    private suspend fun applyFilters(imageBytes: ByteArray): Bitmap {
        val bitmap = byteArrayToBitmap(imageBytes)
            return ImageProcessor.applyAllFilters(
                bitmap!!,
                dataStoreRepository.getBrightness(),
                dataStoreRepository.getContrast(),
                dataStoreRepository.getSaturation(),
                dataStoreRepository.getShadow()
            )
    }

}

