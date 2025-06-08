package cz.mendelu.photoeditor.ui.screens.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.photoeditor.database.IPhotosLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class PreviewScreenViewModel @Inject constructor(
    private val repository: IPhotosLocalRepository): ViewModel(), PreviewScreenActions
{

    private val _previewUIState: MutableStateFlow<PreviewScreenUIState> =
        MutableStateFlow(value = PreviewScreenUIState())

    val previewUIState = _previewUIState.asStateFlow()

    override fun loadPhoto(id: Long) {
        viewModelScope.launch {
            _previewUIState.value = _previewUIState.value.copy(
                photo = repository.getPhotoById(id)
            //loading = false
            )
        }
    }

    override fun onNameChanged(value: String) {
        _previewUIState.value =_previewUIState.value.copy(
            photo =_previewUIState.value.photo.copy(name = value)
        )

    }

    override fun onDateChanged(value: Long?) {
        _previewUIState.value =_previewUIState.value.copy(
            photo =_previewUIState.value.photo.copy(date = value)
        )
    }


    override fun onIsoChanged(value: String) {
        _previewUIState.value =_previewUIState.value.copy(
            photo =_previewUIState.value.photo.copy(iso = value)

        )


    }

    override fun onDescriptionChanged(value: String) {
        _previewUIState.value =_previewUIState.value.copy(
            photo =_previewUIState.value.photo.copy(description = value)
        )
    }

    override fun savePhoto() {
        val photo = _previewUIState.value.photo

        val isValid = photo.name.isNotBlank()

        if(isValid){
            viewModelScope.launch {
                repository.insertPhoto(_previewUIState.value.photo)
                _previewUIState.value = _previewUIState.value.copy(photoSaved = true)
            }
        }else {
            _previewUIState.value =_previewUIState.value.copy(textError = true)
        }


    }


}