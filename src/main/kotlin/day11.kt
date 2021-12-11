class Day11 : Solvable("11") {

    override fun solveA(input: List<String>): String {
        val grid = getGrid(input)

        var flashes = 0

        repeat(100) { flashes += simulateStep(grid) }

        return flashes.toString()
    }

    override fun solveB(input: List<String>): String {
        val grid = getGrid(input)
        var step = 0
        val n = grid.size * grid.first().size
        while (true) {
            step++
            if (simulateStep(grid) == n) {
                return step.toString()
            }
        }
    }

    private fun getGrid(input: List<String>): List<MutableList<Int>> {
        return input.map { it.toCharArray().map { it.toInt() - 48 }.toMutableList() }
    }

    private fun simulateStep(grid: List<MutableList<Int>>): Int {
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                increase(grid, row, col)
            }
        }

        return grid
                .mapIndexed { row, r ->
                    r
                            .filterIndexed { col, c ->
                                val flash = c >= 10
                                if (flash) grid[row][col] = 0
                                flash
                            }
                            .size
                }
                .sum()
    }

    private fun increase(grid: List<MutableList<Int>>, row: Int, col: Int): Int {
        if (row < 0 || row >= grid.size || col < 0 || col >= grid.first().size) return 0

        grid[row][col] += 1

        return if (grid[row][col] == 10)
                increase(grid, row - 1, col - 1) +
                        increase(grid, row - 1, col) +
                        increase(grid, row - 1, col + 1) +
                        increase(grid, row, col - 1) +
                        increase(grid, row, col + 1) +
                        increase(grid, row + 1, col - 1) +
                        increase(grid, row + 1, col) +
                        increase(grid, row + 1, col + 1) +
                        1
        else 0
    }
}
