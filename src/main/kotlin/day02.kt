class Day02 : Solvable() {

    override fun day() = "02"

    override fun solveA(input: List<String>): String {
        var pos = 0
        var depth = 0
        input.forEach {
            val line = it.split(" ")
            val command = line[0]
            val amount = line[1].toInt()
            when (command) {
                "forward" -> pos += amount
                "up" -> depth -= amount
                "down" -> depth += amount
            }
        }
        return (pos * depth).toString()
    }

    override fun solveB(input: List<String>): String {
        var pos = 0
        var depth = 0
        var aim = 0
        input.forEach {
            val line = it.split(" ")
            val command = line[0]
            val amount = line[1].toInt()
            when (command) {
                "forward" -> {
                    pos += amount
                    depth += aim * amount
                }
                "up" -> aim -= amount
                "down" -> aim += amount
            }
        }
        return (pos * depth).toString()
    }
}
