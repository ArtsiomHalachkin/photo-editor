package cz.mendelu.photoeditor.dbinjection

import cz.mendelu.photoeditor.database.IPhotosLocalRepository
import cz.mendelu.photoeditor.database.PhotosDao
import cz.mendelu.photoeditor.database.PhotosLocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(dao: PhotosDao, supabaseClient: SupabaseClient,): IPhotosLocalRepository {
        return PhotosLocalRepositoryImpl(
            supabaseClient = supabaseClient,
            photosDao = dao
        )
    }
}