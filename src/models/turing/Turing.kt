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
        //Build tape from input
        Helper.buildTape(input)
        run()
    }

    private fun run() {
        //Start turing machine
        State.currentState = 0
        State.getState(null, null)

        //Loop while not at the end of the tape
        while (State.currentState != State.acceptState && State.currentState != State.rejectState) {
            val currentSymbol = head
            //read symbol at head
            read = head?.data!!
            when (read) {
                'ɑ' -> {
                    State.currentState = 2
                    State.getState(read as Char, currentSymbol)
                }

                'β' -> {
                    State.currentState = 11
                    State.getState(read as Char, currentSymbol)
                }

                'γ' -> {
                    State.currentState = 17
                    State.getState(read as Char, currentSymbol)
                }
            }
            if (head?.data == machine.blankSymbol) {
                //Check if only alphabet symbols remain
                State.currentState = 35
                State.getState(null, null)
                break
            }
            //Move one position to the right
            head = head?.next
        }
        return
    }
}