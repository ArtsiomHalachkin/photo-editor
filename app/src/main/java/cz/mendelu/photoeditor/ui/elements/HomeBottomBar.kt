package cz.mendelu.photoeditor.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.mendelu.photoeditor.R
import cz.mendelu.photoeditor.ui.theme.basicMargin
import cz.mendelu.photoeditor.ui.theme.smallMargin

@Composable
fun HomeBottomBar(
    onHomeClick: () -> Unit,
    onGalleryClick: () -> Unit
){
    Row(
        modifier =
        Modifier.fillMaxWidth()
        .padding(vertical = smallMargin),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(
            onClick = onHomeClick,
            modifier = Modifier.padding(horizontal = basicMargin)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = stringResource(R.string.home),
            )
        }

        IconButton(
            onClick = onGalleryClick,
            modifier = Modifier.padding(horizontal = basicMargin)
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = stringResource(R.string.gallery),
            )
        }
    }
}