package cz.mendelu.photoeditor.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.opencv.android.OpenCVLoader

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {

    private val viewModel: SplashScreenActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        if (!OpenCVLoader.initLocal()) {
            Toast.makeText(this, "OpenCV init failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "OpenCV loaded", Toast.LENGTH_SHORT).show()
        }

        splashScreen.setKeepOnScreenCondition { true }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.splashScreenState.collect { value ->
                    if (value.runForAFirstTime) {
                        runAppIntro()
                    }
                }
            }
        }

    }

    private fun runAppIntro(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
