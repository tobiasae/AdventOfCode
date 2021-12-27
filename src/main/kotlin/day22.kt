class Day22 : Solvable("22") {

    override fun solveA(input: List<String>): String {
        val cuboids = input.map { Cuboid.getFromString(it) }

        val grid = Array(101) { Array(101) { Array(101) { false } } }
        cuboids.forEach {
            for (x in it.xMin..it.xMax) {
                if (x < -50) continue
                if (x > 50) break
                for (y in it.yMin..it.yMax) {
                    if (y < -50) continue
                    if (y > 50) break
                    for (z in it.zMin..it.zMax) {
                        if (z < -50) continue
                        if (z > 50) break
                        grid[x + 50][y + 50][z + 50] = it.on
                    }
                }
            }
        }

        return grid.map { it.map { it.count { it } }.sum() }.sum().toString()
    }

    override fun solveB(input: List<String>): String {
        val cuboids = input.map { Cuboid.getFromString(it) }

        val xInter = cuboids.map { setOf(it.xMin, it.xMax + 1) }.reduce { l, r -> l + r }.sorted()
        val yInter = cuboids.map { setOf(it.yMin, it.yMax + 1) }.reduce { l, r -> l + r }.sorted()
        val zInter = cuboids.map { setOf(it.zMin, it.zMax + 1) }.reduce { l, r -> l + r }.sorted()

        val grid = Array(xInter.size) { Array(yInter.size) { Array(zInter.size) { false } } }
        cuboids.forEach {
            for (x in xInter.indexOf(it.xMin) until xInter.indexOf(it.xMax + 1)) {
                for (y in yInter.indexOf(it.yMin) until yInter.indexOf(it.yMax + 1)) {
                    for (z in zInter.indexOf(it.zMin) until zInter.indexOf(it.zMax + 1)) {
                        grid[x][y][z] = it.on
                    }
                }
            }
        }

        var sum = 0L

        for (i in 0 until xInter.size - 1) {
            for (j in 0 until yInter.size - 1) {
                for (k in 0 until zInter.size - 1) {
                    if (grid[i][j][k])
                            sum +=
                                    (xInter[i + 1].toLong() - xInter[i].toLong()) *
                                            (yInter[j + 1].toLong() - yInter[j].toLong()) *
                                            (zInter[k + 1].toLong() - zInter[k].toLong())
                }
            }
        }
        return sum.toString()
    }
}

data class Cuboid(
        val xMin: Int,
        val xMax: Int,
        val yMin: Int,
        val yMax: Int,
        val zMin: Int,
        val zMax: Int,
        val on: Boolean = true
) {
    companion object {
        fun getFromString(input: String): Cuboid {
            val (on, dim) = input.split(" ")

            val (x, y, z) = dim.split(",").map { it.drop(2) }

            val (x1, x2) = x.split("..").map(String::toInt)
            val (y1, y2) = y.split("..").map(String::toInt)
            val (z1, z2) = z.split("..").map(String::toInt)

            return Cuboid(x1, x2, y1, y2, z1, z2, on == "on")
        }
    }
}
