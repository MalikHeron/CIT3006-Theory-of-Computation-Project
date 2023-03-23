import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TuringTest {
    @Test
    @Order(1)
    fun acceptState() {
        val result = Turing("ɑɑβββγβɑFKFS").run()
        assertEquals("xxxxxxxxFKFS⊔", result)
    }

    @Test
    @Order(2)
    fun refundAlone() {
        val result = Turing("ɑɑββγβɑFKF").run()
        assertEquals("xxxxxxxFKF⊔β", result)
    }

    @Test
    @Order(3)
    fun insufficientFunds() {
        val result = Turing("ɑɑβγFKF").run()
        assertEquals("xxxxFKx⊔", result)
    }

    @Test
    @Order(4)
    fun acceptState2() {
        val result = Turing("ɑβɑγFNF").run()
        assertEquals("xxxxFNF⊔", result)
    }

    @Test
    @Order(5)
    fun refundAndInsufficientFunds() {
        val result = Turing("γβɑSK").run()
        assertEquals("xxxSx⊔ɑβ", result)
    }

    @Test
    @Order(6)
    fun refundAndInsufficientFunds2() {
        val result = Turing("βγɑSK").run()
        assertEquals("xxxSx⊔ɑβ", result)
    }

    @Test
    @Order(7)
    fun invalidInput() {
        val result = Turing("δNKF").run()
        assertEquals("δNKF⊔", result)
    }

    @Test
    @Order(8)
    fun invalidInput2() {
        val result = Turing("βδNKF").run()
        assertEquals("βδNKF⊔", result)
    }

    @Test
    @Order(9)
    fun noStock() {
        val result = Turing("γN").run()
        assertEquals("xN⊔β", result)
    }

    @Test
    @Order(10)
    fun fiveDollarsOnly() {
        val result = Turing("ɑɑɑɑS").run()
        assertEquals("xxxxS⊔", result)
    }

    @Test
    @Order(11)
    fun restockAndCheckSale() {
        val result = Turing("NKSFFSKS").run()
        assertEquals("NKSFFSKS⊔", result)
    }

    @Test
    @Order(12)
    fun accept3() {
        val result = Turing("γɑK").run()
        assertEquals("xxK⊔", result)
    }

    @Test
    @Order(12)
    fun test() {
        val result = Turing("NKɑ").run()
        assertEquals("xxx⊔ɑ", result)
    }
}