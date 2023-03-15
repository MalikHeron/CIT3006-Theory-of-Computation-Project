package models.turing

import util.DoublyLinkedList
import util.Helper

class Turing(input: String) {

    companion object {
        var tape: DoublyLinkedList = DoublyLinkedList()

        //Get the head of the tape
        var head = tape.getCurrent()
    }

    private var read: Any? = null
    private var machine = Machine()

    init {
        //Verify symbols from input
        if (Helper.verifySymbols(input))
            run()
    }

    private fun run() {
        //Loop while not at the end of the tape
        while (State.currentState != State.finalState) {
            val currentSymbol = head
            //read symbol at head
            read = head?.data!!
            when (read) {
                'ɑ' -> {
                    State.currentState = 2
                    State.getState(read as Char, currentSymbol)
                }

                'β' -> {
                    State.currentState = 9
                    State.getState(read as Char, currentSymbol)
                }

                'γ' -> {
                    State.currentState = 15
                    State.getState(read as Char, currentSymbol)
                }
            }
            if (head?.data == machine.blankSymbol) {
                //Check if only alphabet symbols remain
                Helper.checkInput()
                Helper.refund()
                println("Tape: ${tape.getData()}\n")
                State.currentState = 31
                State.getState(null, null)
                return
            }
            //Move one position to the right
            head = head?.next
        }
    }
}