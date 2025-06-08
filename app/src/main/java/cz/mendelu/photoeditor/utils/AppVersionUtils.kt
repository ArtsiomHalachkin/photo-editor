package cz.mendelu.photoeditor.utils

import android.content.Context

class AppVersionUtils {
    companion object {
        fun getAppVersion(context: Context): String {
            return try {
                val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                packageInfo.versionName ?: "N/A"
            } catch (e: Exception) {
                "N/A"
            }
        }
    }
}