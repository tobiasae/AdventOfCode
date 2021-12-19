class Day13 : Solvable("13") {

    override fun solveA(input: List<String>): String {
        val (positions, foldInstrs) = getGridFolds(input)

        return doFold(positions.toSet(), foldInstrs.first()).size.toString()
    }

    override fun solveB(input: List<String>): String {
        val (positions, foldInstrs) = getGridFolds(input)

        var folded = positions.toSet()

        foldInstrs.forEach { folded = doFold(folded, it) }

        var maxX = (folded.map { it.first }.max() ?:0) + 1
        var maxY = (folded.map { it.second }.max() ?:0) + 1

        val grid = Array<Array<Char>>(maxY) { Array<Char>(maxX) { ' ' } }

        folded.forEach { grid[it.second][it.first] = '#' }

        return grid.joinToString(separator = "\n") { it.joinToString(separator = "") }
    }

    private fun doFold(
            positions: Set<Pair<Int, Int>>,
            fold: Pair<Boolean, Int>
    ): Set<Pair<Int, Int>> {
        return positions
                .map {
                    val (x, y) = it

                    if (fold.first && x > fold.second) {
                        Pair(fold.second - (x - fold.second), y)
                    } else if (!fold.first && y > fold.second)
                            Pair(x, fold.second - (y - fold.second))
                    else it
                }
                .toSet()
    }

    private fun getGridFolds(
            input: List<String>
    ): Pair<List<Pair<Int, Int>>, List<Pair<Boolean, Int>>> {
        val emptyLineIndex = input.indexOf("")

        val positions =
                input.subList(0, emptyLineIndex).map {
                    val (x, y) = it.split(",")
                    Pair(x.toInt(), y.toInt())
                }

        val foldInstrs =
                input.subList(emptyLineIndex + 1, input.size).map {
                    val (text, pos) = it.split("=")
                    Pair(text.last() == 'x', pos.toInt())
                }

        return Pair(positions, foldInstrs)
    }
}
