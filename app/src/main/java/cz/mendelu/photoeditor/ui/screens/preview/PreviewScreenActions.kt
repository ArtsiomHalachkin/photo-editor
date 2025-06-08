package cz.mendelu.photoeditor.ui.screens.preview

interface PreviewScreenActions {

    fun loadPhoto(id: Long)
    fun onNameChanged(value: String)
    fun onDateChanged(value: Long?)
    fun onIsoChanged(value: String)
    fun onDescriptionChanged(value: String)
    fun savePhoto()

}