package cz.mendelu.photoeditor.dbinjection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://ittpnpvhherohscrkgjd.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Iml0dHBucHZoaGVyb2hzY3JrZ2pkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDc2NjEyOTEsImV4cCI6MjA2MzIzNzI5MX0.62DaI3LWvLbzGOC9LBurcI9V8NTXlzTxutS8va6rugI"
        ){
            install(Storage)
        }

    }
}