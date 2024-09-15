import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel {
    lateinit var coroutineScope: CoroutineScope

    var cellSize by mutableStateOf(25)
    val fieldRowsState = mutableStateOf(10)
    var fieldColsState = mutableStateOf(10)
    var fieldModeState = mutableStateOf(FieldMode.FieldEdit)

    // Параметры генерации поля
    var fieldRows by fieldRowsState
    var fieldCols by fieldColsState
    var wallChance by mutableStateOf(25)

    var field by mutableStateOf(createField())
    var robot by mutableStateOf(Pair(0, 0))
    var target by mutableStateOf(Pair(0, 0))
    var dynamicPriority by mutableStateOf(true)

    var path by mutableStateOf<List<Pair<Int, Int>>>(listOf())
    var visited by mutableStateOf(createVisited())

    var status by mutableStateOf("")

    fun createField(): MutableList<MutableList<Boolean>> {
        val matrix = mutableStateListOf<MutableList<Boolean>>()
        repeat(fieldRows) { row ->
            matrix.add(mutableStateListOf())
            repeat(fieldCols){ col ->
                val random = Random.nextInt(0, 100)
                matrix[row].add(random >= wallChance)
            }
        }
        return matrix
    }

    fun createVisited(): MutableList<MutableList<Boolean>> {
        val matrix = mutableStateListOf<MutableList<Boolean>>()
        repeat(field.size) { row ->
            matrix.add(mutableStateListOf())
            repeat(field[0].size){
                matrix[row].add(false)
            }
        }
        return matrix
    }

    fun findPath(method: ((Pair<Int, Int>) -> List<Pair<Int, Int>>,
                          (Pair<Int, Int>) -> Boolean,
                          (Pair<Int, Int>) -> Boolean,
                          Pair<Int, Int>) -> List<Pair<Int, Int>>?) {
        if (robot.first > field.size || robot.second > field[0].size || robot.first < 0 || robot.second < 0){
            status = "Робот находится за пределом поля"
            return
        }
        field[robot.first][robot.second] = true
        if (target.first > field.size || target.second > field[0].size || target.first < 0 || target.second < 0){
            status = "Цель находится за пределом поля"
            return
        }
        field[target.first][target.second] = true
        coroutineScope.launch {
            val generator = { state: Pair<Int, Int> ->
                stateGenerator(field, state, target, dynamicPriority)
            }
            val targetCheck = { state: Pair<Int, Int> ->
                isTarget(state, target)
            }
            visited = createVisited()
            visited[robot.first][robot.second] = true
            val visit =  { state: Pair<Int, Int> ->
                val result = visited[state.first][state.second]
                visited[state.first][state.second] = true
                result
            }
            path = method(generator, targetCheck, visit, robot) ?: listOf()
            status = "Длина пути: ${path.size-1} \n" + path.toString()
        }
    }
}

enum class FieldMode {
    FieldEdit, StartEdit, TargetEdit
}