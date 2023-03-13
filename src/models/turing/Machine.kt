package models.turing

data class Machine(
    var currentState: Int = 0,
    var nextState: Int = 0,
    // To halt the machine when reached, must not contain
    // any transitions
    var finalState: Int = 0,
    var initialState: Int = 0,
    val language: Array<Char> = arrayOf('F', 'K', 'N', 'S'),
    val alphabet: Array<Char> = arrayOf('K', 'F', 'S', 'N', 'ɑ', 'β', 'γ', '#', 'x', '$'),
    // Blank symbol defined for the machine in the input file
    var blankSymbol: Char = alphabet[7],
    var checkSymbol: Char = alphabet[8],
    var endSymbol: Char = alphabet[9]
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Machine

        if (!language.contentEquals(other.language)) return false
        if (!alphabet.contentEquals(other.alphabet)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = language.contentHashCode()
        result = 31 * result + alphabet.contentHashCode()
        return result
    }
}