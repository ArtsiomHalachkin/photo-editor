package cz.mendelu.photoeditor.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var url: String,
    var name: String,
    var storageName: String,
    var date: Long? = null,
    var description: String = "",
    var iso: String = "",
    var photoState: Boolean = false
)

