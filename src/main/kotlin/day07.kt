class Day07 : Solvable("07") {

    override fun solveA(input: List<String>): String {
        val positions = input.first().split(",").map(String::toInt)
        return binarySearch(positions, this::getFuel).toString()
    }

    override fun solveB(input: List<String>): String {
        val positions = input.first().split(",").map(String::toInt)
        return binarySearch(positions, this::getExpensiveFuel).toString()
    }

    private fun binarySearch(positions: List<Int>, fuelFunc: (List<Int>, Int) -> Int): Int {
        var l = 0
        var r = positions.size - 1
        var m = r / 2
        while (true) {
            val m_left = if (m > 1) fuelFunc(positions, m - 1) else Int.MAX_VALUE
            val m_val = fuelFunc(positions, m)
            val m_right = if (m < positions.size - 1) fuelFunc(positions, m + 1) else Int.MAX_VALUE
            if (m_left >= m_val && m_right >= m_val) return m_val
            if (m_right < m_val) {
                l = m
                m = l + Math.max(1, (r - m) / 2)
            } else if (m_right > m_val) {
                r = m
                m = l + Math.max(1, (r - l) / 2)
            }
        }
    }

    private fun getFuel(positions: List<Int>, meetingPoint: Int): Int {
        return positions.map { Math.abs(it - meetingPoint) }.sum()
    }

    private fun getExpensiveFuel(positions: List<Int>, meetingPoint: Int): Int {
        return positions.map { Math.abs(it - meetingPoint).expensiveSum() }.sum()
    }
}

fun Int.expensiveSum() = (this * (this + 1)) / 2
