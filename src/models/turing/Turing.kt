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
        Helper.buildTape(input)
    }

    fun run(): String {
        //Start turing machine
        State.getState(1, null, null)
        return tape.getData()
    }
}