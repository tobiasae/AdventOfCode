class Day25 : Solvable("25") {

    override fun solveA(input: List<String>): String {
        val grid = input.map { it.toCharArray() }

        var count = 1
        while (grid.moveEast() or grid.moveSouth()) {
            count++
        }

        return count.toString()
    }

    override fun solveB(input: List<String>) = "Merry Christmas!"
}

fun List<CharArray>.moveEast(): Boolean {
    var moved = false
    for (i in indices) {
        var j = 0
        var firstFree = this[i][j] == '.'
        while (j < first().size) {
            if (this[i][j] != '>') {
                j++
                continue
            }
            val nextPos = (j + 1) % this[i].size
            if (this[i][nextPos] == '.' && (nextPos > 0 || firstFree)) {
                this[i][j] = '.'
                this[i][nextPos] = '>'
                j++
                moved = true
            }
            j++
        }
    }
    return moved
}

fun List<CharArray>.moveSouth(): Boolean {
    var moved = false
    for (i in first().indices) {
        var j = 0
        var firstFree = this[j][i] == '.'
        while (j < size) {
            if (this[j][i] != 'v') {
                j++
                continue
            }
            val nextPos = (j + 1) % size
            if (this[nextPos][i] == '.' && (nextPos > 0 || firstFree)) {
                this[j][i] = '.'
                this[nextPos][i] = 'v'
                j++
                moved = true
            }
            j++
        }
    }
    return moved
}
