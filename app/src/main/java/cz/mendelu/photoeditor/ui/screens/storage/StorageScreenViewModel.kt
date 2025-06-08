package cz.mendelu.photoeditor.ui.screens.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.mendelu.photoeditor.database.IPhotosLocalRepository
import cz.mendelu.photoeditor.database.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageScreenViewModel @Inject constructor
    (private val repository: IPhotosLocalRepository) : ViewModel(), StorageScreenActions
{

    private val _storageUIState: MutableStateFlow<StorageScreenUIState>
            = MutableStateFlow(value = StorageScreenUIState())

    val storageUIState = _storageUIState.asStateFlow()

    override fun loadPhotos() {
        viewModelScope.launch {
            repository.getAllPhotos().collect { photos ->
                _storageUIState.value = _storageUIState.value.copy(photos = photos, photosLoadedSuccesfuly = true)
            }
        }
    }

    override fun changePhotoState(id: Long, state: Boolean) {
        viewModelScope.launch {
            repository.changePhotoState(id, state)

        }

    }


    override fun removePhotos(photo: Photo) {
        viewModelScope.launch {
            repository.deletePhoto(photo)
        }
    }

    override fun applyFilter() {

        var photos = _storageUIState.value.photos
        var isoFilter =  _storageUIState.value.isoFilter
        var dateFilter = _storageUIState.value.dateFilter

        if (isoFilter.isNotBlank() || dateFilter != null) {
            val filteredList = photos.filter { photo ->
                val matchesIso = if (isoFilter.isNotBlank()) {
                    photo.iso.contains(isoFilter, ignoreCase = true)
                } else true

                val matchesDate = if (dateFilter != null) {
                    photo.date == dateFilter
                } else true

                matchesIso && matchesDate
            }

            _storageUIState.value = _storageUIState.value.copy(photos = filteredList)
        }else{
            loadPhotos()
        }
    }

    override fun onFilterDateChange(date: Long?) {
        _storageUIState.value = _storageUIState.value.copy(dateFilter = date)

    }


    override fun onFilterIsoChange(iso: String) {
        _storageUIState.value = _storageUIState.value.copy(isoFilter = iso)

    }
}