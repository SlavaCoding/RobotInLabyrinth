import java.util.Collections
import java.util.LinkedList
import java.util.Queue
import kotlin.math.abs

fun <T> searchInDepth(generator: (T)->List<T>, targetCheck: (T)->Boolean, visit: (T)->Boolean,  state: T): MutableList<T>? {
    if (targetCheck(state)) return mutableListOf(state)
    val nextStates = generator(state)
    nextStates.forEach{ newState ->
        if (!visit(newState)) {
            val result = searchInDepth(generator, targetCheck, visit, newState)
            result?.let {
                it.add(0, state)
                return it
            }
        }
    }
    return null
}

class ParentTree<T>(val parent: ParentTree<T>?, val value: T){
    fun getPath(): MutableList<T> {
        val list = parent?.getPath() ?: mutableListOf()
        list.add(value)
        return list
    }
}

fun <T> searchInWidth(generator: (T) -> List<T>, targetCheck: (T) -> Boolean, visit: (T)->Boolean,  state: T): MutableList<T>? {
    val queue: Queue<ParentTree<T>> = LinkedList(mutableListOf(ParentTree(null, state)))
    while (!queue.isEmpty()){
        val currentState = queue.poll()
        if (targetCheck(currentState.value)) return currentState.getPath()
        val nextStates = generator(currentState.value)
        nextStates.forEach{ newState ->
            if (!visit(newState)) {
                queue.add(ParentTree(currentState, newState))
            }
        }
    }
    return null
}

fun isTarget(robot: Pair<Int, Int>, target: Pair<Int, Int>):Boolean = robot == target
fun isCorrect(field: List<List<Boolean>>, robot: Pair<Int, Int>):Boolean {
    return robot.first >= 0 && robot.second >= 0
            && robot.first < field.size && robot.second < field[0].size
            && field[robot.first][robot.second]
}

fun stateGenerator(field: List<List<Boolean>>, robot: Pair<Int, Int>, target: Pair<Int, Int>, isDynamic: Boolean): List<Pair<Int, Int>>{
    // Изначальный приоритет: вниз, вправо, влево, вверх
    val movePriority = mutableListOf(Pair(1,0), Pair(0,1), Pair(0,-1), Pair(-1,0))
    if (isDynamic){
        val vector = target-robot
        if (vector.first < 0) Collections.swap(movePriority, 0,3) //Приоритет вверх-вниз
        if (vector.second < 0) Collections.swap(movePriority, 1,2) //Приоритет влево-вправо
        if (abs(vector.first) < abs(vector.second)) { //Приоритет вертикаль-горизонталь
            Collections.swap(movePriority, 0,1)
            Collections.swap(movePriority, 2,3)
        }
    }
    val result: MutableList<Pair<Int, Int>> = mutableListOf()
    movePriority.forEach{
        val newPos = robot+it
        if (isCorrect(field, newPos)) result.add(newPos)
    }
    return result
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first+other.first, this.second+other.second)
}
operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first-other.first, this.second-other.second)
}