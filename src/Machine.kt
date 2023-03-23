data class Machine(
    val inputAlphabet: Array<Char> = arrayOf('F', 'K', 'N', 'S', 'ɑ', 'β', 'γ'),
    val tapeAlphabet: Array<Char> = arrayOf('F', 'K', 'N', 'S', 'ɑ', 'β', 'γ', 'A', 'B', 'Δ', '⊔', 'x'),
    var alpha: Char = tapeAlphabet[7],
    var beta: Char = tapeAlphabet[8],
    var delta: Char = tapeAlphabet[9],
    var blankSymbol: Char = tapeAlphabet[10],
    var crossSymbol: Char = tapeAlphabet[11]
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