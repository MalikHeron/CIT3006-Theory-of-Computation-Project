package models.turing

import models.turing.Turing.Companion.head
import models.turing.Turing.Companion.tape
import util.Node

class Transitions {
    companion object {
        private var machine = Machine()
        private var read: Any? = null

        fun alphaTransitions() {
            var previousSymbol: Node? = null

            //Check if already at the tape's front
            var currentSymbol = if (head?.prev != null) {
                //Move to the leftmost position on the tape
                goToFront()
                head
            } else {
                //Move one position to the right
                head?.next
            }

            //While not end of input
            while (currentSymbol?.data != machine.blankSymbol) {
                if (currentSymbol != previousSymbol) {
                    //Read value under head
                    read = currentSymbol?.data
                    when (read) {
                        'ɑ' -> {
                            State.currentState = 3
                            if (State.getState(read as Char, currentSymbol) != null)
                                return
                        }

                        'β' -> {
                            State.currentState = 5
                            if (State.getState(read as Char, currentSymbol) != null)
                                return
                        }

                        'γ' -> {
                            State.currentState = 7
                            if (State.getState(read as Char, currentSymbol) != null)
                                return
                        }

                        else -> {
                            //Check if only alphabet symbols remain
                            if (machine.inputAlphabet.contains(read)) {
                                revertComboSymbol(currentSymbol)
                                return
                            }
                        }
                    }
                }
                previousSymbol = currentSymbol
                currentSymbol = currentSymbol?.next
            }
            revertSymbol(currentSymbol)
        }

        fun betaTransitions() {
            var previousSymbol: Node? = null
            //Check if already at the tape's front
            var currentSymbol = if (head?.prev != null) {
                //Get the next head
                goToFront()
                head
            } else {
                head
            }

            State.currentState = 4
            State.getState(null, currentSymbol)
            if (head == currentSymbol) {
                currentSymbol = head
                while (currentSymbol?.data != machine.blankSymbol) {
                    if (currentSymbol != previousSymbol) {
                        //Read value under head
                        read = currentSymbol?.data
                        when (read) {
                            'ɑ' -> {
                                State.currentState = 10
                                if (State.getState(read as Char, currentSymbol) != null)
                                    return
                            }

                            'β' -> {
                                State.currentState = 11
                                if (State.getState(read as Char, currentSymbol) != null)
                                    return
                            }

                            'γ' -> {
                                State.currentState = 13
                                if (State.getState(read as Char, currentSymbol) != null)
                                    return
                            }

                            else -> {
                                //Check if only alphabet symbols remain
                                if (machine.inputAlphabet.contains(read)) {
                                    revertComboSymbol(currentSymbol)
                                    return
                                }
                            }
                        }
                    }
                    previousSymbol = currentSymbol
                    currentSymbol = currentSymbol?.next
                }
                revertSymbol(currentSymbol)
            }
        }

        fun gammaTransitions() {
            var previousSymbol: Node? = null
            //Check if already at the tape's front
            var currentSymbol = if (head?.prev != null) {
                //Get the next head
                goToFront()
                head
            } else {
                head
            }

            State.currentState = 16
            State.getState(null, currentSymbol)
            if (head == currentSymbol) {
                currentSymbol = head
                while (currentSymbol?.data != machine.blankSymbol) {
                    if (currentSymbol != previousSymbol) {
                        //Read value under head
                        read = currentSymbol?.data
                        when (read) {
                            'ɑ' -> {
                                State.currentState = 19
                                if (State.getState(read as Char, currentSymbol) != null)
                                    return
                            }

                            'β' -> {
                                State.currentState = 20
                                if (State.getState(read as Char, currentSymbol) != null)
                                    return
                            }

                            'γ' -> {
                                State.currentState = 21
                                if (State.getState(read as Char, currentSymbol) != null)
                                    return
                            }

                            else -> {
                                //Check if only alphabet symbols remain
                                if (machine.inputAlphabet.contains(read)) {
                                    revertComboSymbol(currentSymbol)
                                    return
                                }
                            }
                        }
                    }
                    previousSymbol = currentSymbol
                    currentSymbol = currentSymbol?.next
                }
                revertSymbol(currentSymbol)
            }
        }

        fun giveItem(currentSymbol: Node?) {
            State.currentState = 22
            State.getState(currentSymbol?.data, currentSymbol)
        }

        fun revertComboSymbol(currentSymbol: Node?) {
            State.currentState = 25
            State.getState(currentSymbol?.data, currentSymbol)
            println("Tape: ${tape.getData()}\n")
        }

        private fun revertSymbol(currentSymbol: Node?) {
            State.currentState = 26
            State.getState(currentSymbol?.data, currentSymbol)
            println("Tape: ${tape.getData()}\n")
        }

        private fun goToFront() {
            State.currentState = 30
            State.getState(null, null)
            println("Tape: ${tape.getData()}\n")
        }
    }
}