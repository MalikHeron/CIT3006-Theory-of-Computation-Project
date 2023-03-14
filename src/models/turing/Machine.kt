package models.turing

data class Machine(
    var currentState: Int = 0,
    var nextState: Int = 0,
    // To halt the machine when reached, must not contain
    // any transitions
    var finalState: Int = 0,
    var initialState: Int = 0,
    val inputAlphabet: Array<Char> = arrayOf('F', 'K', 'N', 'S', 'ɑ', 'β', 'γ'),
    val tapeAlphabet: Array<Char> = arrayOf('K', 'F', 'S', 'N', 'ɑ', 'β', 'γ', '⊔', 'x'),
    // Blank symbol defined for the machine in the input file
    var blankSymbol: Char = tapeAlphabet[7],
    var crossSymbol: Char = tapeAlphabet[8]
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Machine

        if (!inputAlphabet.contentEquals(other.inputAlphabet)) return false
        if (!tapeAlphabet.contentEquals(other.tapeAlphabet)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = inputAlphabet.contentHashCode()
        result = 31 * result + tapeAlphabet.contentHashCode()
        return result
    }
}