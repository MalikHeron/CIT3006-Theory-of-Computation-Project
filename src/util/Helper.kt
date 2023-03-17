package util

import models.turing.Machine
import models.turing.Turing.Companion.head
import models.turing.Turing.Companion.tape

class Helper {
    companion object {
        private var machine = Machine()
        fun verifySymbols(input: String): Boolean {
            //Iterate through the symbols in the input
            input.forEach {
                //Check for invalid input
                if (!machine.tapeAlphabet.contains(it)) {
                    println("Invalid input detected")
                    refund()
                    //End transition/Halt
                    println("Halt")
                    return false
                }
                //Add the characters as symbols to the tape
                tape.addNewNode(it)
            }
            head = tape.getCurrent()
            println("Tape: ${tape.getData()}\n")
            return true
        }

        fun checkInput() {
            tape.getData().forEach {
                if (machine.inputAlphabet.contains(it)) {
                    if (it == 'F' || it == 'K' || it == 'N' || it == 'S') {
                        println("Insufficient funds")
                        return
                    }
                }
            }
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
                println("Refund: $$total")
        }
    }
}