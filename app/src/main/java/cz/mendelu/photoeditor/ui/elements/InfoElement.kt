package cz.mendelu.photoeditor.ui.elements

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import cz.mendelu.photoeditor.R
import cz.mendelu.photoeditor.ui.theme.lightTextColor

@Composable
fun InfoElement(
    value: String?,
    hint: String,
    leadingIcon: ImageVector,
    onClick: () -> Unit,
    onClearClick: () -> Unit){

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    // Je nutné kvůli správnému chování labelu. Jinak po kliknutí na křížek zůstane nahoře.
    val focusManager = LocalFocusManager.current

    if (isPressed) {
        LaunchedEffect(isPressed){
            onClick()
        }
    }

    OutlinedTextField(
        value = if (value != null) value else "",
        onValueChange = {},
        interactionSource = interactionSource,
        leadingIcon = {Icon(
            imageVector = leadingIcon,
            tint = Color.Black,
            contentDescription = null
        )}
        ,
        trailingIcon = if (value != null) {
            {
                IconButton(
                    onClick = {
                        onClearClick()
                        focusManager.clearFocus()
                }) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Filled.Clear),
                        tint = Color.Black,
                        contentDescription = stringResource(R.string.clear)
                    )
                }
            }

        } else {
            null
        },
        label = {Text(text = hint)},
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = lightTextColor,
            focusedContainerColor = lightTextColor
    )
    )
}