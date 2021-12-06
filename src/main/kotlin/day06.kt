class Day06 : Solvable("06") {

    override fun solveA(input: List<String>): String {
        return simulateNDays(input, 80).toString()
    }

    override fun solveB(input: List<String>): String {
        return simulateNDays(input, 256).toString()
    }

    private fun simulateNDays(input: List<String>, n: Int): Long {
        val inputFishes = input.first().split(",").map { it.toInt() }
        val fishes = (0..8).map { FishState(it, 0) }
        inputFishes.forEach { fishes[it].numFishes += 1 }

        repeat(n) {
            fishes.forEach { it.timer-- }
            fishes.find { it.timer == -1 }?.let { f8 ->
                f8.timer = 8
                fishes.find { it.timer == 6 }?.let { f6 -> f6.numFishes += f8.numFishes }
            }
        }

        return fishes.map(FishState::numFishes).sum()
    }
}

data class FishState(var timer: Int, var numFishes: Long)
