package cz.mendelu.photoeditor.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.mendelu.photoeditor.R
import cz.mendelu.photoeditor.ui.theme.halfMargin
import cz.mendelu.photoeditor.ui.theme.smallMargin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraGallerySheet(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.padding(halfMargin),
            verticalArrangement = Arrangement.spacedBy(halfMargin)
        ) {



            Button(onClick = {
                onCameraClick()
                onDismiss()
            }, modifier = Modifier.fillMaxWidth(),) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = stringResource(R.string.make_photo),
                )
                Spacer(modifier = Modifier.padding(smallMargin))
                Text(stringResource(R.string.make_photo))
            }

            Button(onClick = {
                onGalleryClick()
                onDismiss()
            },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Default.ImageSearch,
                    contentDescription = stringResource(R.string.gallery),
                )
                Spacer(modifier = Modifier.padding(smallMargin))

                Text(stringResource(R.string.choose_from_gallery))
            }
        }
    }
}

