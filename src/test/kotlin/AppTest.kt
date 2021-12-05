import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {

    @Test
    fun testDay01() {
        assertDay(Day01(), "7", "5")
    }

    @Test
    fun testDay02() {
        assertDay(Day02(), "150", "900")
    }

    @Test
    fun testDay03() {
        assertDay(Day03(), "198", "230")
    }

    @Test
    fun testDay04() {
        assertDay(Day04(), "4512", "1924")
    }

    @Test
    fun testDay05() {
        assertDay(Day05(), "5", "12")
    }

    private fun assertDay(day: Solvable, expectedA: String, expectedB: String) {
        assertEquals(expectedA, day.debugA())
        assertEquals(expectedB, day.debugB())
    }
}
