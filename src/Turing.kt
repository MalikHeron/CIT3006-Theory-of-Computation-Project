import util.Helper

class Turing(input: String) {

    companion object {
        //Get the head of the tape
        var inputHead = 0
        var itemHead = 0
        var inputTape = arrayListOf<Char>()
        var itemTape = arrayListOf<Char>()
        var machine = Machine()
    }

    init {
        //Build tape from input
        buildTape(input)
    }

    private fun buildTape(input: String) {
        //Iterate through the symbols in the input
        input.forEach {
            //Add the characters as symbols to the tape
            inputTape.add(it)
        }
        inputTape.add(machine.blankSymbol)
        println("Tape: $inputTape\n")
    }

    fun run() {
        //Start turing machine
        State.getState(1)
        //Helper.getResults()
        //return inputTape
    }
}