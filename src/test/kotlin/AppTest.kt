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

    @Test
    fun testDay02() {
        val a = Day02().solveA(getExample("example02.txt"))
        assertEquals("150", a)

        val b = Day02().solveB(getExample("example02.txt"))
        assertEquals("900", b)
    }

    @Test
    fun testDay03() {
        val a = Day03().solveA(getExample("example03.txt"))
        assertEquals("198", a)

        val b = Day03().solveB(getExample("example03.txt"))
        assertEquals("230", b)
    }
}
