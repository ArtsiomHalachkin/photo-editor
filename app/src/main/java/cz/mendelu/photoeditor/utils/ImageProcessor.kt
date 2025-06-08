package cz.mendelu.photoeditor.utils


import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc


class ImageProcessor {
    companion object {

        suspend fun applyBrightness(bitmap: Bitmap, brightness: Float):Bitmap = withContext(Dispatchers.Default)
        {
            val mat = Mat()
            Utils.bitmapToMat(bitmap, mat) // mat has 4 channels: BGRA

            // Split channels into list
            val channels = ArrayList<Mat>(4)
            Core.split(mat, channels)

            // channels[0] = B, channels[1] = G, channels[2] = R, channels[3] = A

            // Convert BGR channels to float
            for (i in 0..2) {
                channels[i].convertTo(channels[i], CvType.CV_32F)
                Core.add(channels[i], Scalar((brightness * 50).toDouble()), channels[i])
                channels[i].convertTo(channels[i], CvType.CV_8U)
            }

            // Merge B,G,R + original alpha
            Core.merge(channels, mat)

            return@withContext toBitmap(mat)
        }

           suspend fun applyContrast(bitmap: Bitmap, contrast: Float):Bitmap = withContext(
               Dispatchers.Default)
           {
               val mat = Mat()
               Utils.bitmapToMat(bitmap, mat) // mat now likely has 4 channels: BGRA

               // Split channels (B, G, R, A)
               val channels = ArrayList<Mat>()
               Core.split(mat, channels)

               // Convert BGR channels (0,1,2) to float and apply contrast
               for (i in 0..2) {
                   channels[i].convertTo(channels[i], CvType.CV_32F)
                   Core.multiply(channels[i], Scalar(contrast.toDouble()), channels[i])
                   channels[i].convertTo(channels[i], CvType.CV_8UC1)
               }

               // Alpha channel stays untouched (channels[3])

               // Merge back BGR + Alpha
               Core.merge(channels, mat)

               return@withContext toBitmap(mat)
            }

        suspend fun applyShadow(bitmap: Bitmap, shadow: Float): Bitmap = withContext(
            Dispatchers.Default)  {
            val mat = Mat()
            Utils.bitmapToMat(bitmap, mat)  // BGRA

            // Split into channels B, G, R, A
            val channels = ArrayList<Mat>(4)
            Core.split(mat, channels)

            // Convert BGR channels to CV_32F for processing
            for (i in 0..2) {
                channels[i].convertTo(channels[i], CvType.CV_32F)
            }

            if (shadow > 0f) {
                val shadowScalar = Scalar(
                    (-shadow * 30).toDouble(),
                    (-shadow * 30).toDouble(),
                    (-shadow * 30).toDouble()
                )
                val shadowMat = Mat(channels[0].size(), channels[0].type(), shadowScalar)

                // Subtract shadow (darken)
                for (i in 0..2) {
                    Core.add(channels[i], shadowMat, channels[i])  // Core.add because shadowScalar is negative, so add = subtract shadow
                }
            }

            // Convert back to CV_8U
            for (i in 0..2) {
                channels[i].convertTo(channels[i], CvType.CV_8U)
            }

            // Merge BGR + original alpha back
            Core.merge(channels, mat)

            return@withContext toBitmap(mat)
        }

        suspend fun applySaturation(bitmap: Bitmap, saturation: Float): Bitmap = withContext(
            Dispatchers.Default)  {
            val mat = Mat()
            Utils.bitmapToMat(bitmap, mat)  // BGRA expected

            // Split into B,G,R,A
            val channels = ArrayList<Mat>(4)
            Core.split(mat, channels)

            // Merge BGR channels only for HSV conversion
            val bgrMat = Mat()
            Core.merge(listOf(channels[0], channels[1], channels[2]), bgrMat)

            // Convert BGR to HSV
            Imgproc.cvtColor(bgrMat, bgrMat, Imgproc.COLOR_BGR2HSV)

            // Split HSV channels
            val hsvChannels = ArrayList<Mat>(3)
            Core.split(bgrMat, hsvChannels)

            // Multiply saturation channel by saturation factor (clamp if needed)
            Core.multiply(hsvChannels[1], Scalar(saturation.toDouble()), hsvChannels[1])

            // Merge HSV back
            Core.merge(hsvChannels, bgrMat)

            // Convert HSV back to BGR
            Imgproc.cvtColor(bgrMat, bgrMat, Imgproc.COLOR_HSV2BGR)

            // Extract channels back
            val b = Mat()
            val g = Mat()
            val r = Mat()
            Core.extractChannel(bgrMat, b, 0)
            Core.extractChannel(bgrMat, g, 1)
            Core.extractChannel(bgrMat, r, 2)

            // Merge processed BGR with original alpha channel
            val resultMat = Mat()
            Core.merge(listOf(b, g, r, channels[3]), resultMat)

            return@withContext toBitmap(resultMat)
        }

        suspend fun applyAllFilters(
            bitmap: Bitmap,
            brightness: Float,
            contrast: Float,
            saturation: Float,
            shadow: Float
        ): Bitmap = withContext(
            Dispatchers.Default)  {
            val mat = Mat()
            Utils.bitmapToMat(bitmap, mat) // BGRA expected

            // Split channels: B, G, R, A
            val channels = ArrayList<Mat>(4)
            Core.split(mat, channels)

            // ---- 1. Apply brightness, contrast and shadow to B, G, R ----
            for (i in 0..2) {
                channels[i].convertTo(channels[i], CvType.CV_32F)

                // Contrast
                Core.multiply(channels[i], Scalar(contrast.toDouble()), channels[i])

                // Brightness
                Core.add(channels[i], Scalar((brightness * 50).toDouble()), channels[i])

                // Shadow (darken if shadow > 0)
                if (shadow > 0f) {
                    Core.add(
                        channels[i],
                        Mat(channels[i].size(), channels[i].type(), Scalar(-shadow.toDouble() * 30)),
                        channels[i]
                    )
                }

                channels[i].convertTo(channels[i], CvType.CV_8U)
            }

            // ---- 2. Merge BGR for saturation ----
            val bgrMat = Mat()
            Core.merge(listOf(channels[0], channels[1], channels[2]), bgrMat)

            // Convert to HSV
            Imgproc.cvtColor(bgrMat, bgrMat, Imgproc.COLOR_BGR2HSV)
            val hsvChannels = ArrayList<Mat>(3)
            Core.split(bgrMat, hsvChannels)

            // Apply saturation
            Core.multiply(hsvChannels[1], Scalar(saturation.toDouble()), hsvChannels[1])

            // Merge back HSV and convert to BGR
            Core.merge(hsvChannels, bgrMat)
            Imgproc.cvtColor(bgrMat, bgrMat, Imgproc.COLOR_HSV2BGR)

            // Extract final BGR channels
            val b = Mat()
            val g = Mat()
            val r = Mat()
            Core.extractChannel(bgrMat, b, 0)
            Core.extractChannel(bgrMat, g, 1)
            Core.extractChannel(bgrMat, r, 2)

            // Merge with original alpha channel
            val resultMat = Mat()
            Core.merge(listOf(b, g, r, channels[3]), resultMat)

            return@withContext toBitmap(resultMat)
        }


        private fun toBitmap(mat: Mat): Bitmap {
            val result = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(mat, result)
            return result
        }
    }
}
