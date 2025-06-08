package cz.mendelu.photoeditor.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cz.mendelu.photoeditor.navigation.Destination
import cz.mendelu.photoeditor.navigation.NavGraph
import cz.mendelu.photoeditor.ui.theme.PhotoEditorTheme
import dagger.hilt.android.AndroidEntryPoint
import org.opencv.android.OpenCVLoader

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoEditorTheme {
                NavGraph(startDestination = Destination.HomeScreen.route)
            }
        }
    }
}

