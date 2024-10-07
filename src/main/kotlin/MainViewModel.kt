import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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

    fun createVisited(): MutableList<MutableList<Boolean>> = MutableList(field.size){MutableList(field[0].size){false} }

    fun findPath(method: AlgorithmEnum) {
        // Подготовка к решению задачи
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

        //Выполняем поиск
        coroutineScope.launch {
            val generator = { state: Pair<Int, Int> -> stateGenerator(field, state, target, dynamicPriority) }
            val targetCheck = { state: Pair<Int, Int> -> isTarget(state, target) }
            visited = createVisited()
            visited[robot.first][robot.second] = true   // Клетка, в которой стоит робот уже посещена
            val visitCheck =  { state: Pair<Int, Int> ->
                val result = visited[state.first][state.second]
                visited[state.first][state.second] = true
                result
            }
            path = when (method){
                AlgorithmEnum.SearchInDepth -> searchInDepth(generator, targetCheck, visitCheck, robot) ?: listOf()
                AlgorithmEnum.SearchInWidth -> searchInWidth(generator, targetCheck, visitCheck, robot) ?: listOf()
                AlgorithmEnum.BranchBoundaryMethod -> {
                    val getEval = { state: Pair<Int, Int> -> evaluationFunction(state, target)}
                    branchBoundaryMethod(generator, getEval, visitCheck, robot) ?: listOf()
                }
            }
            status = "Длина пути: ${path.size-1} \n" +
                    "Число помеченных клеток: " + visited.flatten().count { it } + "\n" +
                    path.toString()
        }
    }
}

enum class AlgorithmEnum {
    SearchInDepth, SearchInWidth, BranchBoundaryMethod
}

enum class FieldMode {
    FieldEdit, StartEdit, TargetEdit
}