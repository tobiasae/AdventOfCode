class Day19 : Solvable("19") {

    val cache = HashMap<List<String>, Pair<Set<Point>, List<Point>>>()

    override fun solveA(input: List<String>): String {
        val result = getPoints(input)
        cache[input] = result
        return result.first.size.toString()
    }

    override fun solveB(input: List<String>): String {
        val scannerPositions = cache[input]?.second ?: getPoints(input).second

        var max = Int.MIN_VALUE
        for (i in scannerPositions.indices) {
            for (j in i + 1 until scannerPositions.size) {
                val diff = scannerPositions[i] - scannerPositions[j]
                val distance = Math.abs(diff.x) + Math.abs(diff.y) + Math.abs(diff.z)
                if (distance > max) max = distance
            }
        }
        return max.toString()
    }

    private fun getPoints(input: List<String>): Pair<Set<Point>, List<Point>> {
        val scanners = getScannerViews(input)
        val first = scanners.first()
        val otherScanners = scanners.drop(1).toMutableList()
        val scannerPositions = mutableListOf<Point>()
        while (otherScanners.isNotEmpty()) {
            val other = otherScanners.removeAt(0)
            val (points, scannerPosition) = first.accumulatePoints(other)
            if (points.size > 0) {
                first.points.addAll(points)
                scannerPositions.add(scannerPosition)
            } else otherScanners.add(other)
        }
        return Pair(first.points, scannerPositions)
    }

    private fun getScannerViews(input: List<String>): List<Scanner> {
        return mutableListOf<Scanner>().apply {
            input.forEach {
                if (it.startsWith("---")) this.add(Scanner(mutableSetOf()))
                else if (it.length > 1) {
                    val (x, y, z) = it.split(",").map(String::toInt)
                    this.last().points.add(Point(x, y, z))
                }
            }
        }
    }
}

data class Scanner(val points: MutableSet<Point>) {

    fun accumulatePoints(other: Scanner): Pair<Set<Point>, Point> {
        for (r in 0..23) {
            val rotated = other.getRotation(r)
            points.forEach { p ->
                rotated.points.forEach { o ->
                    val diff = p - o
                    val translated = rotated.points.map { it + diff }
                    if (translated.intersect(points).size >= 12) {
                        return Pair(points.union(translated), diff)
                    }
                }
            }
        }

        return Pair(setOf(), Point(0, 0, 0))
    }

    fun getRotation(r: Int): Scanner = Scanner(points.map { it.getRotation(r) }.toMutableSet())
}

data class Point(val x: Int, val y: Int, val z: Int) {

    fun getRotation(r: Int): Point =
            when (r) {
                0 -> this
                1 -> Point(-y, x, z)
                2 -> Point(-x, -y, z)
                3 -> Point(y, -x, z)
                4 -> Point(-z, y, x)
                5 -> Point(-y, -z, x)
                6 -> Point(z, -y, x)
                7 -> Point(y, z, x)
                8 -> Point(z, y, -x)
                9 -> Point(-y, z, -x)
                10 -> Point(-z, -y, -x)
                11 -> Point(y, -z, -x)
                12 -> Point(-x, y, -z)
                13 -> Point(-y, -x, -z)
                14 -> Point(x, -y, -z)
                15 -> Point(y, x, -z)
                16 -> Point(x, -z, y)
                17 -> Point(z, x, y)
                18 -> Point(-x, z, y)
                19 -> Point(-z, -x, y)
                20 -> Point(-x, -z, -y)
                21 -> Point(z, -x, -y)
                22 -> Point(x, z, -y)
                23 -> Point(-z, x, -y)
                else -> throw Exception("Invalid rotation argument $r")
            }

    fun getAllRotations(): List<Point> = (0..23).map(this::getRotation)

    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Point): Point = Point(x - other.x, y - other.y, z - other.z)
}
