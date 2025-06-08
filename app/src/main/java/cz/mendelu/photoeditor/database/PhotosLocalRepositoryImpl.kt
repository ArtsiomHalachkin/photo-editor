package cz.mendelu.photoeditor.database

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class PhotosLocalRepositoryImpl @Inject constructor
    (private val photosDao: PhotosDao,
     private val supabaseClient: SupabaseClient,) : IPhotosLocalRepository {

    override suspend fun uploadAndSavePhoto(
        photoObject: Photo,
        photoBytes: ByteArray
    ) {
        val storage = supabaseClient.storage.from("photo-editor-storage")

        try {

            val storageName = appendTimestampToFilename(photoObject.name)

            storage.upload(storageName, photoBytes) {
                upsert = false
            }

            val publicUrl = storage.publicUrl(storageName)
            val updatedPhoto = photoObject.copy(
                url = publicUrl,
                storageName = storageName,
                date = System.currentTimeMillis()
            )

            photosDao.insertPhoto(updatedPhoto)

        } catch (e: Exception) {
            Log.e("Supabase", "Upload or save failed", e)
        }
    }


    override suspend fun insertPhoto(photo: Photo) {
        photosDao.insertPhoto(photo)
    }

    override suspend fun deletePhoto(photo: Photo) {
        val storage = supabaseClient.storage.from("photo-editor-storage")

        try {
            storage.delete(photo.storageName)
            photosDao.deletePhoto(photo)

            Log.d("Supabase", "Deleted photo: ${photo.storageName}")
        } catch (e: Exception) {
            Log.e("Supabase", "Failed to delete photo: ${photo.storageName}", e)
        }
    }

    override fun getAllPhotos(): Flow<List<Photo>> {
        return photosDao.getAllPhotos()
    }

    override fun getLastPhotos(): Flow<List<Photo>> {
        return photosDao.getLastPhotos()
    }

    override suspend fun getPhotoById(id: Long): Photo {
        return photosDao.getPhotoById(id)

    }

    override suspend fun changePhotoState(id: Long, state: Boolean) {
        photosDao.changePhotoState(id, state)
    }

    private fun appendTimestampToFilename(filename: String): String {
        val timestamp = System.currentTimeMillis()
        val dotIndex = filename.lastIndexOf('.')
        return if (dotIndex != -1) {
            val name = filename.substring(0, dotIndex)
            val extension = filename.substring(dotIndex)
            "${name}_$timestamp$extension"
        } else {
            "${filename}_$timestamp"
        }
    }
}
