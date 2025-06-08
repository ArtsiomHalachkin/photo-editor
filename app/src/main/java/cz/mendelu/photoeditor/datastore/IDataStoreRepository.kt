package cz.mendelu.photoeditor.datastore

interface IDataStoreRepository {
    suspend fun setContrast(contrast: Float)
    suspend fun setBrightness(brightness: Float)
    suspend fun setSaturation(saturation: Float)
    suspend fun setShadow(shadow: Float)

    suspend fun getContrast(): Float
    suspend fun getBrightness(): Float
    suspend fun getSaturation(): Float
    suspend fun getShadow(): Float

    /*
  suspend fun getSupabaseUrl(): String
  suspend fun getSupabaseKey(): String
  suspend fun setSupabaseKey(key: String)
  suspend fun setSupabaseUrl(url: String)\

   */

}