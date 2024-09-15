package UI

import MainViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.onClick
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabyrinthField(
    vm: MainViewModel,
    modifier: Modifier
){
    Box(modifier){
        LazyColumn {
            itemsIndexed(vm.field) { rowIndex, row->
                LazyRow {
                    itemsIndexed(row) {colIndex, item ->
                        val coordinate = Pair(rowIndex, colIndex)
                        val color = if (coordinate == vm.robot) Color.Green
                        else if (coordinate == vm.target) Color.Red
                        else if (!item) Color.Black
                        else if (coordinate in vm.path) Color.Yellow
                        else if (vm.visited[rowIndex][colIndex]) Color.Cyan
                        else Color.White
                        Box(
                            modifier = Modifier.width(vm.cellSize.dp).height(vm.cellSize.dp)
                                .border(1.dp, Color.Gray).background(color)
                                .onClick {
                                    when (vm.fieldModeState.value) {
                                        FieldMode.FieldEdit -> {
                                            if (coordinate != vm.robot || coordinate != vm.target)
                                                vm.field[rowIndex][colIndex] = !item
                                        }
                                        FieldMode.StartEdit -> {
                                            vm.field[rowIndex][colIndex] = true
                                            vm.robot = coordinate
                                        }
                                        FieldMode.TargetEdit -> {
                                            vm.field[rowIndex][colIndex] = true
                                            vm.target = coordinate
                                        }
                                    }
                                }
                        ) {}
                    }
                }
            }
        }
    }

}
