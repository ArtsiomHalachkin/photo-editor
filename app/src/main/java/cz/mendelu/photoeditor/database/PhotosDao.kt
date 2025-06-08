package cz.mendelu.photoeditor.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo)

    @Query("SELECT * FROM Photos ORDER BY date DESC")
    fun getAllPhotos(): Flow<List<Photo>>

    @Delete
    suspend fun deletePhoto(photo: Photo)

    @Query("SELECT * FROM Photos ORDER BY id DESC LIMIT 3")
    fun getLastPhotos(): Flow<List<Photo>>

    @Query("SELECT * FROM Photos WHERE id = :id")
    suspend fun getPhotoById(id: Long): Photo

    @Query("UPDATE photos SET photoState = :state WHERE id = :id")
    suspend fun changePhotoState(id: Long, state: Boolean)

}