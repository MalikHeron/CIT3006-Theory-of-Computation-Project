import ui.MainScreen.Companion.transactionDialog

class Turing(input: String) {

    private var inputTape = arrayListOf<Char>()
    private var machine = Machine()

    init {
        //Build tape from input
        buildTape(input)
    }

    private fun buildTape(input: String) {
        //Store input
        transactionDialog.setInput(input)
        //Iterate through the symbols in the input
        input.forEach {
            //Add the characters as symbols to the tape
            inputTape.add(it)
        }
        inputTape.add(machine.blankSymbol)
        println("Tape: $inputTape\n")
    }

    fun run(): String {
        //Start turing machine
        val output = State().doTransitions(inputTape)
        var result = ""
        output.forEach {
            result += it
        }
        println("Result: $result")
        //Store output
        transactionDialog.setOutput(result)
        return result
    }
}