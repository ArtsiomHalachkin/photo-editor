package cz.mendelu.photoeditor.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ImagesearchRoller
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.media3.effect.Crop
import cz.mendelu.photoeditor.R
import cz.mendelu.photoeditor.ui.theme.basicMargin
import cz.mendelu.photoeditor.ui.theme.smallMargin

@Composable
fun EditorBottomBar(
    onCropClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onPreviewClick: () -> Unit,
    onEditorClick: () -> Unit
){
    Row(
        modifier =
        Modifier.fillMaxWidth()
            .padding(vertical = smallMargin),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(
            onClick = onEditorClick,
            modifier = Modifier.padding(horizontal = smallMargin)
        ) {
            Icon(
                imageVector = Icons.Default.ImagesearchRoller,
                contentDescription = stringResource(R.string.Editor),
            )
        }
        IconButton(
            onClick = onPreviewClick,
            modifier = Modifier.padding(horizontal = smallMargin)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription =  stringResource(R.string.Preview),
            )
        }
        IconButton(
            onClick = onCropClick,
            modifier = Modifier.padding(horizontal = smallMargin)
        ) {
            Icon(
                imageVector = Icons.Default.ContentCut,
                contentDescription =  stringResource(R.string.Crop),
            )
        }


        Spacer(
            modifier = Modifier.padding(basicMargin)
        )


        IconButton(
            onClick = onGalleryClick,
            modifier = Modifier.padding(horizontal = smallMargin)
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = stringResource(R.string.Gallery),
            )
        }
    }
}