class Day12 : Solvable("12") {

    override fun solveA(input: List<String>): String {
        return countWays(getMap(input), setOf("start"), "start").toString()
    }

    override fun solveB(input: List<String>): String {
        return countWaysDouble(getMap(input), setOf("start"), false, "start").toString()
    }

    private fun countWays(
            map: Map<String, MutableSet<String>>,
            visited: Set<String>,
            current: String
    ): Int {
        var count = 0
        map[current]?.forEach {
            if (it == "end") count++
            else if (it.first().isUpperCase() || !visited.contains(it))
                    count += countWays(map, setOf(current) + visited, it)
        }
        return count
    }

    private fun countWaysDouble(
            map: Map<String, MutableSet<String>>,
            visited: Set<String>,
            doubleTaken: Boolean,
            current: String
    ): Int {
        var count = 0
        map[current]?.forEach {
            if (it == "end") count++
            else if (it.first().isLowerCase() && visited.contains(it)) {
                if (!doubleTaken && it != "start") {
                    count += countWaysDouble(map, visited, true, it)
                }
            } else count += countWaysDouble(map, visited + setOf(it), doubleTaken, it)
        }
        return count
    }

    fun getMap(input: List<String>): Map<String, MutableSet<String>> {
        val map = mutableMapOf<String, MutableSet<String>>()
        input.forEach {
            val (a, b) = it.split("-")
            map[a] = (map[a] ?: mutableSetOf()).also { it.add(b) }
            map[b] = (map[b] ?: mutableSetOf()).also { it.add(a) }
        }
        return map
    }
}
