package UI

import MainViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IntTextField(
    intValue: MutableState<Int>,
    range: ClosedRange<Int>,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(intValue.value.toString()) }
    var error by remember { mutableStateOf(false) }
    LaunchedEffect(intValue){
        text = intValue.value.toString()
    }
    TextField(
        value = text,
        isError = error,
        maxLines = 1,
        modifier = modifier,
        onValueChange = { newText ->
            text = newText
            try {
                val newVal = text.toInt()
                if (newVal in range) {
                    intValue.value = newVal
                    error = false
                }
                else error = true
            }
            catch (_: Exception) {
                error = true
            }
        }
    )
}