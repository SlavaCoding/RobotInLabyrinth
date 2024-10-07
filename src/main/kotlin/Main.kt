import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application


fun main() = application {
    Window(onCloseRequest = ::exitApplication,
        title = "Робот в лабиринте", state = WindowState(position = WindowPosition(Alignment.Center), size = DpSize(1100.dp, 700.dp))
    ) {
        MaterialTheme {
            UI.Window()
        }
    }
}
