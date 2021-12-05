class Day05 : Solvable("05") {

    override fun solveA(input: List<String>): String {
        val lineCount = HashMap<Pair<Int, Int>, Int>()

        getLines(input).forEach {
            it.getVertHorPoints().forEach { lineCount[it] = 1 + (lineCount[it] ?: 0) }
        }

        return lineCount.filterValues { it >= 2 }.size.toString()
    }

    override fun solveB(input: List<String>): String {
        val lineCount = HashMap<Pair<Int, Int>, Int>()

        getLines(input).forEach {
            it.getAllPoints().forEach { lineCount[it] = (lineCount[it] ?: 0) + 1 }
        }

        return lineCount.filterValues { it >= 2 }.size.toString()
    }

    private fun getLines(input: List<String>): List<Line> {
        return input.map { Line(it.split(" -> ")) }
    }
}

class Line {
    val x1: Int
    val x2: Int
    val y1: Int
    val y2: Int

    constructor(input: List<String>) {
        val (x1, x2) = input.first().split(",").map(String::toInt)
        val (y1, y2) = input.last().split(",").map(String::toInt)
        this.x1 = x1
        this.x2 = x2
        this.y1 = y1
        this.y2 = y2
    }

    fun getVerticalPoints(): List<Pair<Int, Int>> {
        if (x1 == y1) {
            return (if (y2 > x2) (x2..y2) else (y2..x2)).map { Pair(x1, it) }
        }
        return listOf()
    }

    fun getHorizontalPoints(): List<Pair<Int, Int>> {
        if (x2 == y2) {
            return (if (y1 > x1) (x1..y1) else (y1..x1)).map { Pair(it, x2) }
        }
        return listOf()
    }

    fun getDiagonalPoints(): List<Pair<Int, Int>> {
        if ((x1 != y1) and (x2 != y2)) {
            val xrange = if (y1 > x1) x1..y1 else x1 downTo y1
            val yrange = if (y2 > x2) x2..y2 else x2 downTo y2
            return xrange.zip(yrange)
        }
        return listOf()
    }

    fun getVertHorPoints(): List<Pair<Int, Int>> {
        return getVerticalPoints() + getHorizontalPoints()
    }

    fun getAllPoints(): List<Pair<Int, Int>> {
        return getVerticalPoints() + getHorizontalPoints() + getDiagonalPoints()
    }
}
