package cz.mendelu.photoeditor.ui.screens.cropper

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.photoeditor.database.IPhotosLocalRepository
import cz.mendelu.photoeditor.database.Photo
import cz.mendelu.photoeditor.utils.ImageProcessor
import cz.mendelu.photoeditor.utils.ImageUtils.Companion.bitmapToByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CropScreenViewModel @Inject constructor(
    private val repository: IPhotosLocalRepository,
) : ViewModel(), CropScreenActions {

    private val _cropUIState: MutableStateFlow<CropScreenUIState> =
        MutableStateFlow(value = CropScreenUIState())

    val cropUIState = _cropUIState.asStateFlow()

    override fun setImage(bitmap: Bitmap) {
        _cropUIState.value = _cropUIState.value.copy(originalImage = bitmap, processedImage = bitmap)

    }



    override fun savePhoto() {
        viewModelScope.launch {
            _cropUIState.value.processedImage?.let { bitmap ->
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

                _cropUIState.value = _cropUIState.value.copy(
                    newPhotoSaved = true
                )
            }
        }

    }

    override fun loadBack() {
        _cropUIState.value = _cropUIState.value.copy(
                originalImage = _cropUIState.value.originalImage,
                processedImage = _cropUIState.value.originalImage
        )
    }


    override fun loadPhoto(id: Long) {
        viewModelScope.launch {
            _cropUIState.value = _cropUIState.value.copy(
                photo = repository.getPhotoById(id),
                photoLoadedSuccesfuly = true
            )
        }
    }

    override fun apply(bitmap: Bitmap) {
        _cropUIState.value = _cropUIState.value.copy( processedImage = bitmap)
    }
}