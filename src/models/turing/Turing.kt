package models.turing

import util.DoublyLinkedList
import util.Helper

class Turing(input: String) {

    companion object {
        var tape: DoublyLinkedList = DoublyLinkedList()

        //Get the head of the tape
        var head = tape.getCurrent()
    }

    private var machine = Machine()

    init {
        //Build tape from input
        Helper.buildTape(input)
        run()
    }

    private fun run() {
        //Start turing machine
        State.getState(1, null, null)

        //Loop while not at the end of the tape
        while (State.currentState != State.acceptState && State.currentState != State.rejectState) {
            State.getState(2, head?.data, head)
            if (head?.data == machine.blankSymbol) {
                State.getState(18, null, null)
                break
            }
            //Move one position to the right
            head = head?.next
        }
        return
    }
}