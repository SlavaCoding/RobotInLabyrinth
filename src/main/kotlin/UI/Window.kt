package UI

import FieldMode
import MainViewModel
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Window() {
    val vm by remember { mutableStateOf(MainViewModel()) }
    vm.coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier.padding(10.dp).fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LabyrinthField(vm, Modifier.weight(1f))
        Column(
            modifier = Modifier.width(350.dp).fillMaxHeight()
                .background(Color.hsv(0f, 0.0f, 0.95f))
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Размер ячейки")
                Slider(
                    value = vm.cellSize.toFloat(),
                    modifier = Modifier.weight(1f).padding(8.dp),
                    valueRange = 8f..60f,
                    onValueChange = {
                        vm.cellSize = it.toInt()
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Размеры поля")
                IntTextField(vm.fieldRowsState, 3..100, Modifier.padding(8.dp).weight(1f))
                IntTextField(vm.fieldColsState, 3..100, Modifier.padding(8.dp).weight(1f))
            }
            Text("Заполненность лабиринта")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Slider(
                    value = vm.wallChance.toFloat(),
                    modifier = Modifier.weight(1f).padding(8.dp),
                    valueRange = 0f..100f,
                    onValueChange = {
                        vm.wallChance = it.toInt()
                    }
                )
                Text(vm.wallChance.toString()+"%", Modifier.width(50.dp))
            }
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    vm.coroutineScope.launch {
                        vm.field = vm.createField()
                        vm.visited = vm.createVisited()
                        vm.path = listOf()
                    }
                }
            ){
                Text("Сгенерировать поле")
            }
            FieldModeRadioButton(vm.fieldModeState, FieldMode.FieldEdit, "Редактирование поля")
            FieldModeRadioButton(vm.fieldModeState, FieldMode.StartEdit, "Выбор старта")
            FieldModeRadioButton(vm.fieldModeState, FieldMode.TargetEdit, "Выбор цели")
            Row(Modifier.clickable { vm.dynamicPriority = !vm.dynamicPriority }) {
                Checkbox(
                    checked = vm.dynamicPriority,
                    onCheckedChange = {
                        vm.dynamicPriority = it
                    })
                Text("Динамический приоритет направления")
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button({vm.findPath(AlgorithmEnum.SearchInDepth)}){
                    Text("Поиск в глубину")
                }
                Button({vm.findPath(AlgorithmEnum.SearchInWidth)}){
                    Text("Поиск в ширину")
                }
            }
            Button({vm.findPath(AlgorithmEnum.BranchBoundaryMethod)}, Modifier.fillMaxWidth()){
                Text("Поиск в методом ветвей и границ")
            }

            Box(Modifier.verticalScroll(ScrollState(0))) {
                Text(vm.status, Modifier.align(Alignment.BottomStart))
            }
        }
    }

}