package cz.mendelu.photoeditor.dbinjection

import android.content.Context
import cz.mendelu.photoeditor.database.PhotosDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PhotosDatabase {
        return PhotosDatabase.getDatabase(context)
    }
}