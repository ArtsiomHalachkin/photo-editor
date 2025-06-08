package cz.mendelu.photoeditor.ui.screens.storage

import cz.mendelu.photoeditor.database.Photo

data class StorageScreenUIState(
    val photos: List<Photo> = emptyList(),
    //sval notFilteredPhoto: List<Photo> = emptyList(),

    var photosLoadedSuccesfuly: Boolean = false,
    var loadingError: Int? = null,
    var dateFilter: Long? = null,
    var isoFilter: String = "",
)