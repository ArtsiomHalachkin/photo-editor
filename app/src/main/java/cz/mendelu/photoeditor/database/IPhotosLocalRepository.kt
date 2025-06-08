package cz.mendelu.photoeditor.database


import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface IPhotosLocalRepository {
    suspend fun uploadAndSavePhoto( photoObject: Photo,
                                    photoBytes: ByteArray)


    suspend fun insertPhoto(photo: Photo)

    suspend fun deletePhoto(photo: Photo)

    fun getAllPhotos(): Flow<List<Photo>>
    fun getLastPhotos(): Flow<List<Photo>>

    suspend fun getPhotoById(id: Long): Photo

    suspend fun changePhotoState(id: Long, state: Boolean)




}