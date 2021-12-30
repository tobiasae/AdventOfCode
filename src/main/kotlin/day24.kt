class Day24 : Solvable("24") {

    override fun solveA(input: List<String>): String {
        return solve(getInstructions(input), true)
    }

    override fun solveB(input: List<String>): String {
        return solve(getInstructions(input), false)
    }

    private fun getInstructions(input: List<String>): List<Pair<List<Instruction>, Int>> {
        return input
                .map { Instruction.get(it.split(" ")) }
                .chunked(18)
                .map { it to (it.filter { it is Div && it.from.get() == 26L }.size == 1) }
                .map {
                    if (it.second) {
                        it.first to (it.first[5] as Add).from.get().toInt()
                    } else {
                        it.first to Int.MAX_VALUE
                    }
                }
    }

    private fun solve(instrs: List<Pair<List<Instruction>, Int>>, max: Boolean): String {
        var count = if (max) 9999999 else 1111111
        var countStr: String = ""
        var nums: MutableList<Int> = mutableListOf()

        outer@ while (count >= 1111111 && count <= 9999999) {
            resetRegisters()
            countStr = count.toString()
            if (countStr.contains("0")) {
                count += if (max) -1 else 1
                continue
            }
            var i = 0
            nums = mutableListOf<Int>()
            for (instr in instrs) {
                if (instr.second != Int.MAX_VALUE) {
                    val n = registers.get("z")!!.get().toInt() % 26 + instr.second
                    if (n <= 0 || n > 9) break
                    run(instr.first, mutableListOf(n))
                    nums.add(n)
                } else {
                    run(instr.first, mutableListOf(countStr[i++].toInt() - 48))
                }
            }
            if (nums.size == 7 && registers.get("z")!!.get() == 0L) break
            count += if (max) -1 else 1
        }

        var cCount = 0
        var nCount = 0

        return instrs
                .map {
                    if (it.second == Int.MAX_VALUE) {
                        countStr[cCount++].toInt() - 48
                    } else {
                        nums[nCount++]
                    }
                }
                .map(Int::toString)
                .joinToString("")
    }
}

fun run(instructions: List<Instruction>, input: MutableList<Int>) {
    instructions.forEach { it.execute(input) }
}

val registers = listOf("w", "x", "y", "z").associateWith { Register() }.toMutableMap()

fun resetRegisters() {
    registers.values.forEach { it.set(0) }
}

open class Register(v: Long = 0) {
    var value = v

    fun get(): Long {
        return value
    }

    fun set(reg: Register) {
        value = reg.get()
    }

    fun set(i: Long) {
        value = i
    }

    companion object {
        fun get(str: String): Register {
            registers[str]?.let {
                return it
            }
            return Register(str.toLong())
        }
    }
}

abstract class Instruction() {
    abstract fun execute(input: MutableList<Int>)

    companion object {
        fun get(inp: List<String>): Instruction {
            return when (inp.first()) {
                "inp" -> Inp(Register.get(inp[1]))
                "add" -> Add(Register.get(inp[1]), Register.get(inp[2]))
                "mul" -> Mul(Register.get(inp[1]), Register.get(inp[2]))
                "div" -> Div(Register.get(inp[1]), Register.get(inp[2]))
                "mod" -> Mod(Register.get(inp[1]), Register.get(inp[2]))
                "eql" -> Eql(Register.get(inp[1]), Register.get(inp[2]))
                else -> throw Exception("invalid instruction ${inp.first()}")
            }
        }
    }
}

class Inp(val to: Register) : Instruction() {
    override fun execute(input: MutableList<Int>) {
        to.set(input.removeAt(input.size - 1).toLong())
    }
}

class Add(val to: Register, val from: Register) : Instruction() {
    override fun execute(input: MutableList<Int>) {
        to.set(to.get() + from.get())
    }
}

class Mul(val to: Register, val from: Register) : Instruction() {
    override fun execute(input: MutableList<Int>) {
        to.set(to.get() * from.get())
    }
}

class Div(val to: Register, val from: Register) : Instruction() {
    override fun execute(input: MutableList<Int>) {
        to.set(to.get() / from.get())
    }
}

class Mod(val to: Register, val from: Register) : Instruction() {
    override fun execute(input: MutableList<Int>) {
        to.set(to.get() % from.get())
    }
}

class Eql(val to: Register, val from: Register) : Instruction() {
    override fun execute(input: MutableList<Int>) {
        to.set(if (to.get() == from.get()) 1L else 0L)
    }
}
