package models.turing

import util.DoublyLinkedList
import util.Helper

class Turing(input: String) {

    companion object {
        var tape: DoublyLinkedList = DoublyLinkedList()

        //Get the head of the tape
        var head = tape.getCurrent()
    }

    init {
        //Build tape from input
        buildTape(input)
    }

    private fun buildTape(input: String) {
        //Iterate through the symbols in the input
        input.forEach {
            //Add the characters as symbols to the tape
            tape.addNewNode(it)
        }
        head = tape.getCurrent()
        println("Tape: ${tape.getData()}\n")
    }

    fun run(): String {
        //Start turing machine
        State.getState(1, null, null)
        Helper.getResults()
        return tape.getData()
    }
}