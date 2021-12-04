class Day01 : Solvable("01") {

    override fun solveA(input: List<String>): String {
        val measurements = input.map { it.toInt() }
        var count = 0
        for (i in 1 until measurements.size) {
            if (measurements[i] > measurements[i - 1]) count++
        }
        return count.toString()
    }

    override fun solveB(input: List<String>): String {
        val measurements = input.map { it.toInt() }
        var count = 0
        for (i in 1..measurements.size - 3) {
            if (getInterval(measurements, i) > getInterval(measurements, i - 1)) count++
        }
        return count.toString()
    }

    private fun getInterval(measurements: List<Int>, index: Int): Int {
        return measurements.subList(index, index + 3).sum()
    }
}
