package cz.mendelu.photoeditor.utils

import java.util.*

object LanguageUtils {

    private val CZECH = "cs"
    private val ENGLISH = "en"

    fun isLanguageCzech(): Boolean {
        val language = Locale.getDefault().language
        return language.equals(CZECH) || language.equals(ENGLISH)
    }

}

