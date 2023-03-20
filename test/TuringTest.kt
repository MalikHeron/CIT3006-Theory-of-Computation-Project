import models.turing.Turing
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TuringTest {
    @Test
    fun acceptState() {
        val result = Turing("⊔ɑɑβββγβɑFKFS⊔").run()
        assertEquals("⊔xxxxxxxxxxxx⊔", result)
    }

    @Test
    fun refundAlone() {
        val result = Turing("⊔ɑɑββγβɑFKF⊔").run()
        assertEquals("⊔xxxxxBxxxx⊔", result)
    }

    @Test
    fun insufficientFunds() {
        val result = Turing("⊔ɑɑβγFKF⊔").run()
        assertEquals("⊔xxxxxxF⊔", result)
    }

    @Test
    fun acceptState2() {
        val result = Turing("⊔ɑβɑγFNF⊔").run()
        assertEquals("⊔xxxxxxx⊔", result)
    }

    @Test
    fun refundAndInsufficientFunds() {
        val result = Turing("⊔γβɑSK⊔").run()
        assertEquals("⊔xBAxK⊔", result)
    }

    @Test
    fun refundAndInsufficientFunds2() {
        val result = Turing("⊔βγɑSK⊔").run()
        assertEquals("⊔BxAxK⊔", result)
    }

    @Test
    fun invalidInput() {
        val result = Turing("⊔δNKF⊔").run()
        assertEquals("⊔δNKF⊔", result)
    }

    @Test
    fun invalidInput2() {
        val result = Turing("⊔βδNKF⊔").run()
        assertEquals("⊔βδNKF⊔", result)
    }

    @Test
    fun noStock() {
        val result = Turing("⊔γN⊔").run()
        assertEquals("⊔Bx⊔", result)
    }

    @Test
    fun fiveDollarsOnly() {
        val result = Turing("⊔ɑɑɑɑS⊔").run()
        assertEquals("⊔xxxxx⊔", result)
    }

    @Test
    fun restockAndCheckSale() {
        val result = Turing("⊔NKSFFSKS⊔").run()
        assertEquals("⊔NKSFFSKS⊔", result)
    }
}