package cz.mendelu.photoeditor.utils

import android.content.Context
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.util.Locale
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ImageUtils {

    companion object {

        suspend fun bitmapToByteArray(bitmap: Bitmap): ByteArray = withContext(
            Dispatchers.Default)   {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            return@withContext stream.toByteArray()
        }

        suspend fun getWebPBitmapFromDrawable(@DrawableRes drawableResId: Int, context: Context):
                Bitmap? = withContext(
            Dispatchers.Default)   {
            return@withContext BitmapFactory.decodeResource(context.resources, drawableResId)
        }

        suspend fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? = withContext(
        Dispatchers.Default) {
            return@withContext BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }





    }
}