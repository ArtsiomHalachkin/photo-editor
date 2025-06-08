package cz.mendelu.photoeditor.ui.screens.preview

import cz.mendelu.photoeditor.database.Photo

data class PreviewScreenUIState(
    var photo: Photo = Photo(
        url = "",
        name = "",
        storageName = "",
        description = "",
        iso = ""
    ),
    var textError: Boolean? = null,
    var photoSaved: Boolean = false
)