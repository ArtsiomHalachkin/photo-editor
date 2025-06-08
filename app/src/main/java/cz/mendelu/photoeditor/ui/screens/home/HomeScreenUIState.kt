package cz.mendelu.photoeditor.ui.screens.home

import cz.mendelu.photoeditor.database.Photo

data class HomeScreenUIState (
    var photo: Photo = Photo(
        url = "",
        name = "myphoto.jpg",
        storageName = "",
        description = "",
        iso = ""
    ),
    val photos: List<Photo> = emptyList(),
    var photoPicked: Boolean = false
)