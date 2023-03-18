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
                        return State.getState(38, null, head)
                    }
                }

                2 -> {
                    if (betaCounter == 1) {
                        return State.getState(39, null, head)
                    }
                }

                3 -> {
                    if (betaCounter == 1) {
                        return State.getState(38, null, head)
                    } else if (betaCounter == 0) {
                        return State.getState(40, null, head)
                    }
                }

                4 -> {
                    return State.getState(39, null, head)
                }

                5 -> {
                    return State.getState(38, null, head)
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