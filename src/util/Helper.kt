package util

import models.turing.State
import models.turing.Turing.Companion.head
import models.turing.Turing.Companion.tape

class Helper {
    companion object {

        fun buildTape(input: String) {
            //Iterate through the symbols in the input
            input.forEach {
                //Add the characters as symbols to the tape
                tape.addNewNode(it)
            }
            head = tape.getCurrent()
            println("Tape: ${tape.getData()}\n")
        }

        fun checkCount(alphaCounter: Int, betaCounter: Int): Node? {
            when (alphaCounter) {
                1 -> {
                    if (betaCounter == 2) {
                        State.currentState = 38
                        return State.getState(null, head)
                    }
                }

                2 -> {
                    if (betaCounter == 1) {
                        State.currentState = 39
                        return State.getState(null, head)
                    }
                }

                3 -> {
                    if (betaCounter == 1) {
                        State.currentState = 38
                        return State.getState(null, head)
                    } else if (betaCounter == 0) {
                        State.currentState = 40
                        return State.getState(null, head)
                    }
                }

                4 -> {
                    State.currentState = 39
                    return State.getState(null, head)
                }

                5 -> {
                    State.currentState = 38
                    return State.getState(null, head)
                }
            }
            return null
        }

        fun refund() {
            var total = 0
            tape.getData().forEach {
                if (it == 'ɑ' || it == 'A')
                    total += 5
                if (it == 'β' || it == 'B')
                    total += 10
                if (it == 'γ' || it == 'Δ')
                    total += 20
            }
            if (total != 0)
                println("Refund: $$total\n")
        }
    }
}