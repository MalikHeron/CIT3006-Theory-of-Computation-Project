package models.turing

import models.turing.Turing.Companion.head
import util.Helper
import util.Node

class Transitions {
    companion object {
        private var machine = Machine()
        private var read: Any? = null
        private var write: Any? = null
        private var right = 'R'

        fun alphaTransitions(currentState: Int) {
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
            while (head?.data != machine.blankSymbol && head?.next != null) {
                if (currentSymbol != previousSymbol) {
                    //Read value under head
                    read = currentSymbol?.data
                    when (read) {
                        'ɑ' -> {
                            write = machine.theta
                            println("q$currentState: $read -> $write, $right")
                            //Write 'θ' on head position
                            currentSymbol?.data = machine.theta
                            head = currentSymbol
                            if (State.getState(4, read as Char, currentSymbol) != null)
                                return
                        }

                        'β' -> {
                            write = machine.mu
                            println("q$currentState: $read -> $write, $right")
                            //Write 'μ' on head position
                            currentSymbol?.data = machine.mu
                            head = currentSymbol
                            if (State.getState(5, read as Char, currentSymbol) != null)
                                return
                        }

                        'γ' -> {
                            write = machine.omega
                            println("q$currentState: $read -> $write, $right")
                            //Write 'Ω' on head position
                            currentSymbol?.data = machine.omega
                            head = currentSymbol
                            if (State.getState(6, read as Char, currentSymbol) != null)
                                return
                        }

                        else -> {
                            //Check if only alphabet symbols remain
                            if (machine.inputAlphabet.contains(read)) {
                                revertComboSymbol(currentSymbol)
                                break
                            }
                        }
                    }
                }
                if (head?.next != null) {
                    write = read
                    println("q$currentState: $read -> $write, $right")

                    previousSymbol = currentSymbol
                    head = head?.next
                    currentSymbol = head
                }
            }
            revertSymbol(head)
        }

        fun betaTransitions(currentState: Int) {
            var previousSymbol: Node? = null
            //Check if already at the tape's front
            var currentSymbol = if (head?.prev != null) {
                //Get the next head
                goToFront()
                head
            } else {
                head
            }

            read = machine.beta
            write = machine.beta
            println("q$currentState: $read -> $write, $right")
            //Write 'θ' on head position
            if (State.getState(4, null, currentSymbol) == null) {
                currentSymbol = head
                while (head?.data != machine.blankSymbol && head?.next != null) {
                    if (currentSymbol != previousSymbol) {
                        //Read value under head
                        read = currentSymbol?.data
                        when (read) {
                            'ɑ' -> {
                                write = machine.theta
                                println("q$currentState: $read -> $write, $right")
                                //Write 'θ' on head position
                                currentSymbol?.data = machine.theta
                                head = currentSymbol
                                if (State.getState(5, read as Char, currentSymbol) != null)
                                    return
                            }

                            'β' -> {
                                write = machine.mu
                                println("q$currentState: $read -> $write, $right")
                                //Write 'μ' on head position
                                currentSymbol?.data = machine.mu
                                head = currentSymbol
                                if (State.getState(10, read as Char, currentSymbol) != null)
                                    return
                            }

                            'γ' -> {
                                write = machine.omega
                                println("q$currentState: $read -> $write, $right")
                                //Write 'Ω' on head position
                                currentSymbol?.data = machine.omega
                                head = currentSymbol
                                if (State.getState(11, read as Char, currentSymbol) != null)
                                    return
                            }

                            else -> {
                                //Check if only alphabet symbols remain
                                if (machine.inputAlphabet.contains(read)) {
                                    revertComboSymbol(currentSymbol)
                                    break
                                }
                            }
                        }
                    }
                    if (head?.next != null) {
                        write = read
                        println("q$currentState: $read -> $write, $right")

                        previousSymbol = currentSymbol
                        head = head?.next
                        currentSymbol = head
                    }
                }
                revertSymbol(head)
            }
        }

        fun gammaTransitions(currentState: Int) {
            var previousSymbol: Node? = null
            //Check if already at the tape's front
            var currentSymbol = if (head?.prev != null) {
                //Get the next head
                goToFront()
                head
            } else {
                head
            }

            read = machine.delta
            write = machine.delta
            println("q$currentState: $read -> $write, $right")
            if (State.getState(13, null, currentSymbol) == null) {
                currentSymbol = head
                while (head?.data != machine.blankSymbol && head?.next != null) {
                    if (currentSymbol != previousSymbol) {
                        //Read value under head
                        read = currentSymbol?.data
                        when (read) {
                            'ɑ' -> {
                                write = machine.theta
                                println("q$currentState: $read -> $write, $right")
                                //Write 'θ' on head position
                                currentSymbol?.data = machine.theta
                                head = currentSymbol
                                if (State.getState(6, read as Char, currentSymbol) != null)
                                    return
                            }

                            'β' -> {
                                write = machine.mu
                                println("q$currentState: $read -> $write, $right")
                                //Write 'μ' on head position
                                currentSymbol?.data = machine.mu
                                head = currentSymbol
                                if (State.getState(11, read as Char, currentSymbol) != null)
                                    return
                            }

                            'γ' -> {
                                write = machine.omega
                                println("q$currentState: $read -> $write, $right")
                                //Write 'Ω' on head position
                                currentSymbol?.data = machine.omega
                                head = currentSymbol
                                if (State.getState(5, read as Char, currentSymbol) != null)
                                    return
                            }

                            else -> {
                                //Check if only alphabet symbols remain
                                if (machine.inputAlphabet.contains(read)) {
                                    revertComboSymbol(currentSymbol)
                                    break
                                }
                            }
                        }
                    }
                    if (head?.next != null) {
                        write = read
                        println("q$currentState: $read -> $write, $right")

                        previousSymbol = currentSymbol
                        head = head?.next
                        currentSymbol = head
                    }
                }
                revertSymbol(head)
            }
        }

        fun giveItem(item: Char, currentSymbol: Node?) {
            println("Dispense '$item'")
            Helper.setItemStock(item, Helper.getItemStock(item) - 1)
            head = currentSymbol
            State.getState(14, currentSymbol?.data, currentSymbol)
        }

        fun giveItemAlt(item: Char, currentSymbol: Node?) {
            println("Dispense '$item'")
            Helper.setItemStock(item, Helper.getItemStock(item) - 1)
            head = currentSymbol
            State.getState(15, currentSymbol?.data, currentSymbol)
        }

        fun giveItemEnd(item: Char, currentSymbol: Node?) {
            println("Dispense '$item'")
            Helper.setItemStock(item, Helper.getItemStock(item) - 1)
            head = currentSymbol
            State.getState(29, currentSymbol?.data, currentSymbol)
        }

        fun revertComboSymbol(currentSymbol: Node?) {
            State.getState(16, currentSymbol?.data, currentSymbol)
        }

        private fun revertSymbol(currentSymbol: Node?) {
            State.getState(17, currentSymbol?.data, currentSymbol)
        }

        fun goToFront() {
            State.getState(27, null, null)
        }
    }
}