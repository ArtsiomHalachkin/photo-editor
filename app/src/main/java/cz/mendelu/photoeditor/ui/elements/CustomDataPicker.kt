package cz.mendelu.photoeditor.ui.elements

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.mendelu.photoeditor.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDataPicker(
    modifier: Modifier = Modifier,
    date: Long? = null,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date)

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(datePickerState.selectedDateMillis!!)
                onDismiss()
            }
            ) {
               Text(text = stringResource(R.string.select))
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}
