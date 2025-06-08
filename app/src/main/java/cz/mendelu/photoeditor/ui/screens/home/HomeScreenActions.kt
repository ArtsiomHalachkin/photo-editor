package cz.mendelu.photoeditor.ui.screens.home




interface HomeScreenActions {
    fun uploadPickedPhoto(imageBytes: ByteArray)
    fun loadRecentPhotos()
}