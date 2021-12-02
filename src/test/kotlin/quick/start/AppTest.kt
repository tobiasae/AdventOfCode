package quick.start

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {

    private fun getExample(fileName: String): List<String> {
        return File("examples/$fileName").readLines()
    }

    @Test
    fun testDay01() {
        val a = Day01().solveA(getExample("example01.txt"))
        assertEquals("7", a)

        val b = Day01().solveB(getExample("example01.txt"))
        assertEquals("5", b)
    }
}
