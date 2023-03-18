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
        State.getState(0, null, null)

        //Loop while not at the end of the tape
        while (State.currentState != State.acceptState && State.currentState != State.rejectState) {
            val currentSymbol = head
            //read symbol at head
            read = head?.data!!
            when (read) {
                'ɑ' -> {
                    State.getState(2, read as Char, currentSymbol)
                }

                'β' -> {
                    State.getState(11, read as Char, currentSymbol)
                }

                'γ' -> {
                    State.getState(17, read as Char, currentSymbol)
                }
            }
            if (head?.data == machine.blankSymbol) {
                State.getState(35, null, null)
                break
            }
            //Move one position to the right
            head = head?.next
        }
        return
    }
}