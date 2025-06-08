package cz.mendelu.photoeditor.ui.screens.storage

import cz.mendelu.photoeditor.database.Photo

interface StorageScreenActions {
    fun loadPhotos()
    fun changePhotoState(id: Long, state: Boolean)
    fun removePhotos(photo: Photo)
    fun applyFilter()
    fun onFilterDateChange(date: Long?)
    fun onFilterIsoChange(iso: String)

}