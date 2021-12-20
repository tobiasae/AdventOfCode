import kotlin.comparisons.compareBy
import java.util.PriorityQueue

class Day15 : Solvable("15") {

    override fun solveA(input: List<String>): String {
        val grid = mutableListOf<List<Int>>()
        input.forEach { grid.add(it.map(Char::toInt).map { it - 48 }) }
        return dijkstra(grid).toString()
    }

    override fun solveB(input: List<String>): String {
        val grid = mutableListOf<List<Int>>()
        input.forEach {
            val base = it.map(Char::toInt).map { it - 48 }
            val concat = mutableListOf<Int>()
            (-1..3).forEach { i -> concat.addAll(base.map { (it + i) % 9 + 1 }) }
            grid.add(concat)
        }
        val n = grid.size
        (0 until 4 * n).forEach { grid.add(grid[grid.size - n].map { it % 9 + 1 }) }

        return dijkstra(grid).toString()
    }

    private fun dijkstra(grid: List<List<Int>>): Int {
        var queue = PriorityQueue<Vertex>(compareBy { it.distance })

        val spt = mutableSetOf<Vertex>()

        val distances = grid.map { it.map { Int.MAX_VALUE }.toMutableList() }
        distances[0][0] = 0

        queue.add(Vertex(Pair(0, 0), 0))

        fun updatePos(i: Int, j: Int, v: Int) {
            if (i < 0 || i >= grid.size || j < 0 || j >= grid.first().size) return
            val newVal = v + grid[i][j]
            if (newVal < distances[i][j]) {
                distances[i][j] = newVal
                queue.add(Vertex(Pair(i, j), newVal))
            }
        }

        while (queue.isNotEmpty()) {
            val el = queue.remove()
            spt.add(el)
            updatePos(el.pos.first - 1, el.pos.second, el.distance)
            updatePos(el.pos.first + 1, el.pos.second, el.distance)
            updatePos(el.pos.first, el.pos.second - 1, el.distance)
            updatePos(el.pos.first, el.pos.second + 1, el.distance)
        }
        return spt.maxBy { it.pos.first + it.pos.second }!!.distance
    }
}

class Vertex(val pos: Pair<Int, Int>, var distance: Int)
