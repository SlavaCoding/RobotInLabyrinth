package UI

import FieldMode
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FieldModeRadioButton(
    bindVal: MutableState<FieldMode>,
    mode: FieldMode,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable {
            bindVal.value = mode
        }
    ){
        RadioButton(
            selected = bindVal.value == mode,
            onClick = {
                bindVal.value = mode
            }
        )
        Text(text)
    }
}