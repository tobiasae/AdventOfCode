class Day17 : Solvable("17") {

    override fun solveA(input: List<String>): String {
        return maxHeight(getYRange(input.first()).first).toString()
    }

    override fun solveB(input: List<String>): String {
        val yStepMap =
                HashMap<Int, Set<YStep>>().also {
                    for (y in getYRange(input.first())) {
                        it[y] =
                                (y..(Math.abs(y) - 1))
                                        .map { numStepsY(it, y) }
                                        .filterNotNull()
                                        .toSet()
                    }
                }
        val xStepMap =
                HashMap<Int, Pair<Set<XStep>, XStep?>>().also {
                    for (x in getXRange(input.first())) {
                        val steps = (1..x).map { numStepsX(it, x) }.filterNotNull()
                        val lowestBound = steps.filter { it.unbounded }.minBy { it.steps }
                        val values =
                                steps
                                        .filter { it.steps < lowestBound?.steps ?: Int.MAX_VALUE }
                                        .toSet()
                        it[x] = Pair(values, lowestBound)
                    }
                }

        var positions = HashSet<Pair<Int, Int>>()
        for (y in getYRange(input.first())) {
            for (x in getXRange(input.first())) {
                val ySteps = yStepMap[y]!!
                val (xSteps, lowestBound) = xStepMap[x]!!
                for (ys in ySteps) {
                    for (xs in xSteps) {
                        if (ys.steps == xs.steps                        ) {
                            positions.add(Pair(xs.x, ys.y))
                        } else if (lowestBound is XStep && ys.steps >= lowestBound.steps) {
                            positions.add(Pair(lowestBound.x, ys.y))
                        }
                    }
                }
            }
        }
        return positions.size.toString()
    }

    private fun numStepsX(x: Int, targetX: Int): XStep? {
        var steps = 0
        var xStep = x
        var xPos = 0
        while (xPos < targetX && xStep > 0) {
            xPos += xStep--
            steps++
        }
        return if (xPos == targetX) XStep(steps, x, xStep == 0) else null
    }

    private fun numStepsY(y: Int, targetY: Int): YStep? {
        var steps = 0
        var yStep = y
        var yPos = 0
        while (yPos > targetY) {
            yPos += yStep--
            steps++
        }
        return if (yPos == targetY) YStep(steps, y) else null
    }

    private fun maxHeight(y: Int) = (Math.abs(y) - 1).let { it * (it + 1) / 2 }

    private fun getXRange(line: String): IntProgression {
        val start = line.indexOfFirst({ it == '=' })
        val end = line.indexOfFirst { it == ',' }
        return getRange(line, start + 1, end)
    }

    private fun getYRange(line: String): IntProgression {
        val start = line.lastIndexOf('=')
        return getRange(line, start + 1, line.length)
    }

    private fun getRange(line: String, start: Int, end: Int): IntProgression {
        val (l, r) = line.substring(start, end).split("..").map(String::toInt)
        return l..r
    }
}

abstract class Step(val steps: Int) {
    override fun equals(other: Any?): Boolean = if (other is Step) steps == other.steps else false

    override fun hashCode(): Int =steps.hashCode()
}

class XStep(steps: Int, val x: Int, val unbounded: Boolean) : Step(steps) {
    override fun toString(): String = "$steps; $x; $unbounded"
}

class YStep(steps: Int, val y: Int) : Step(steps) {
    override fun toString(): String = "$steps; $y"
}
