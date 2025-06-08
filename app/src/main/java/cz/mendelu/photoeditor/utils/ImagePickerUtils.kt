package cz.mendelu.photoeditor.utils

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.DrawableRes
import coil3.BitmapImage
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import java.io.FileOutputStream

class ImagePickerUtils {

    companion object {

        suspend fun loadBitmapFromUrl(context: Context, url: String):
                Bitmap? = withContext(Dispatchers.IO) {
            val loader = ImageLoader(context)

            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build()

            return@withContext when (val result = loader.execute(request)) {
                is SuccessResult -> {
                    val image = result.image
                    (image as? BitmapImage)?.bitmap
                }

                else -> null
            }
        }



    }
}