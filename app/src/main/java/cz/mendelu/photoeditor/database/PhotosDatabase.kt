package cz.mendelu.photoeditor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Photo::class], version = 13, exportSchema = true)
abstract class PhotosDatabase : RoomDatabase() {

    abstract fun photosDao(): PhotosDao

    companion object {
        private var INSTANCE: PhotosDatabase? = null
        fun getDatabase(context: Context): PhotosDatabase {
            if (INSTANCE == null) {
                synchronized(PhotosDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            PhotosDatabase::class.java,
                            "photos_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }

    }
}