package cz.mendelu.photoeditor.dbinjection

import cz.mendelu.photoeditor.database.PhotosDao
import cz.mendelu.photoeditor.database.PhotosDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideDao(database: PhotosDatabase): PhotosDao {
        return database.photosDao()
    }
}