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
                            if (State.getState(3, read as Char, currentSymbol) != null)
                                return
                        }

                        'β' -> {
                            if (State.getState(5, read as Char, currentSymbol) != null)
                                return
                        }

                        'γ' -> {
                            if (State.getState(7, read as Char, currentSymbol) != null)
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

            if (State.getState(4, null, currentSymbol) == null) {
                currentSymbol = head
                while (head?.data != machine.blankSymbol) {
                    if (currentSymbol != previousSymbol) {
                        //Read value under head
                        read = currentSymbol?.data
                        when (read) {
                            'ɑ' -> {
                                if (State.getState(12, read as Char, currentSymbol) != null)
                                    return
                            }

                            'β' -> {
                                if (State.getState(13, read as Char, currentSymbol) != null)
                                    return
                            }

                            'γ' -> {
                                if (State.getState(15, read as Char, currentSymbol) != null)
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

            if (State.getState(18, null, currentSymbol) == null) {
                currentSymbol = head
                while (head?.data != machine.blankSymbol) {
                    if (currentSymbol != previousSymbol) {
                        //Read value under head
                        read = currentSymbol?.data
                        when (read) {
                            'ɑ' -> {
                                State.currentState = 21
                                if (State.getState(21, read as Char, currentSymbol) != null)
                                    return
                            }

                            'β' -> {
                                if (State.getState(22, read as Char, currentSymbol) != null)
                                    return
                            }

                            'γ' -> {
                                if (State.getState(23, read as Char, currentSymbol) != null)
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
            State.getState(24, currentSymbol?.data, currentSymbol)
        }

        fun giveItemAlt(currentSymbol: Node?) {
            State.getState(27, currentSymbol?.data, currentSymbol)
        }

        fun giveItemAlone(currentSymbol: Node?) {
            State.getState(29, currentSymbol?.data, currentSymbol)
        }

        fun revertComboSymbol(currentSymbol: Node?) {
            State.getState(30, currentSymbol?.data, currentSymbol)
            println("Tape: ${tape.getData()}\n")
        }

        private fun revertSymbol(currentSymbol: Node?) {
            State.getState(31, currentSymbol?.data, currentSymbol)
            println("Tape: ${tape.getData()}\n")
        }

        fun goToFront() {
            State.getState(42, null, null)
        }
    }
}