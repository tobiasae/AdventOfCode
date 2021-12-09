class Day09 : Solvable("09") {

    override fun solveA(input: List<String>): String {
        val grid = getGrid(input)

        val lowPoints = getLowPoints(grid).map { grid[it.first][it.second] }

        return (lowPoints.size + lowPoints.sum()).toString()
    }

    override fun solveB(input: List<String>): String {
        val grid = getGrid(input)

        return getLowPoints(grid)
                .map { expandBasin(grid, it).size }
                .sortedDescending()
                .take(3)
                .reduce { l, r -> l * r }
                .toString()
    }

    private fun getGrid(input: List<String>): List<List<Int>> {
        return input.map { it.split("").let { it.subList(1, it.size - 1) }.map(String::toInt) }
    }

    private fun getLowPoints(grid: List<List<Int>>): List<Pair<Int, Int>> {
        val lowPoints = mutableListOf<Pair<Int, Int>>()

        grid.forEachIndexed { row, r ->
            r.forEachIndexed { col, c ->
                val left = if (col > 0) r[col - 1] else 10
                val right = if (col + 1 < r.size) r[col + 1] else 10
                val upper = if (row > 0) grid[row - 1][col] else 10
                val lower = if (row + 1 < grid.size) grid[row + 1][col] else 10
                if (c < left && c < right && c < upper && c < lower) lowPoints.add(Pair(row, col))
            }
        }

        return lowPoints
    }

    private fun expandBasin(grid: List<List<Int>>, point: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val basin = mutableSetOf<Pair<Int, Int>>(point)

        listOf(
                Pair(point.first - 1, point.second),
                Pair(point.first + 1, point.second),
                Pair(point.first, point.second - 1),
                Pair(point.first, point.second + 1)
        )
                .forEach {
                    if (!basin.contains(it) && flowsTo(grid, it, point)) {
                        basin.addAll(expandBasin(grid, it))
                    }
                }

        return basin
    }

    private fun flowsTo(grid: List<List<Int>>, from: Pair<Int, Int>, to: Pair<Int, Int>): Boolean {
        val fromY = from.first
        val fromX = from.second

        if (fromY < 0 || fromY >= grid.size || fromX < 0 || fromX >= grid.first().size) return false
        if (grid[fromY][fromX] == 9) return false

        return grid[fromY][fromX] > grid[to.first][to.second]
    }
}
