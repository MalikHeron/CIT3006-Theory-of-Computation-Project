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
            while (head?.data != machine.blankSymbol) {
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
                head = head?.next
                currentSymbol = head
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
            if (State.getState(null, currentSymbol) == null) {
                currentSymbol = head
                while (head?.data != machine.blankSymbol) {
                    if (currentSymbol != previousSymbol) {
                        //Read value under head
                        read = currentSymbol?.data
                        when (read) {
                            'ɑ' -> {
                                State.currentState = 12
                                if (State.getState(read as Char, currentSymbol) != null)
                                    return
                            }

                            'β' -> {
                                State.currentState = 13
                                if (State.getState(read as Char, currentSymbol) != null)
                                    return
                            }

                            'γ' -> {
                                State.currentState = 15
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
                    head = head?.next
                    currentSymbol = head
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

            State.currentState = 18
            if (State.getState(null, currentSymbol) == null) {
                currentSymbol = head
                while (head?.data != machine.blankSymbol) {
                    if (currentSymbol != previousSymbol) {
                        //Read value under head
                        read = currentSymbol?.data
                        when (read) {
                            'ɑ' -> {
                                State.currentState = 21
                                if (State.getState(read as Char, currentSymbol) != null)
                                    return
                            }

                            'β' -> {
                                State.currentState = 22
                                if (State.getState(read as Char, currentSymbol) != null)
                                    return
                            }

                            'γ' -> {
                                State.currentState = 23
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
                    head = head?.next
                    currentSymbol = head
                }
                revertSymbol(currentSymbol)
            }
        }

        fun giveItem(currentSymbol: Node?) {
            State.currentState = 24
            State.getState(currentSymbol?.data, currentSymbol)
        }

        fun giveItemAlt(currentSymbol: Node?) {
            State.currentState = 27
            State.getState(currentSymbol?.data, currentSymbol)
        }

        fun giveItemAlone(currentSymbol: Node?) {
            State.currentState = 29
            State.getState(currentSymbol?.data, currentSymbol)
        }

        fun revertComboSymbol(currentSymbol: Node?) {
            State.currentState = 30
            State.getState(currentSymbol?.data, currentSymbol)
            println("Tape: ${tape.getData()}\n")
        }

        private fun revertSymbol(currentSymbol: Node?) {
            State.currentState = 31
            State.getState(currentSymbol?.data, currentSymbol)
            println("Tape: ${tape.getData()}\n")
        }

        fun goToFront() {
            State.currentState = 35
            State.getState(null, null)
            println("Tape: ${tape.getData()}\n")
        }
    }
}