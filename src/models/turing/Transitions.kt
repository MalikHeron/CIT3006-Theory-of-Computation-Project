package models.turing

import models.turing.Turing.Companion.head
import models.turing.Turing.Companion.tape
import util.Node

class Transitions {
    companion object {
        private var machine = Machine()
        private var read: Any? = null
        private var write: Any? = null
        private var right = 'R'
        private var left = 'L'

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
            while (head?.data != machine.blankSymbol) {
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
                                return
                            }
                        }
                    }
                }
                read = "F, K, N, S, A, B, Δ, θ, μ, Ω, x"
                write = "F, K, N, S, A, B, Δ, θ, μ, Ω, x"
                println("q$currentState: $read -> $right [${head?.data}]")

                previousSymbol = currentSymbol
                head = head?.next
                currentSymbol = head
            }
            revertSymbol(currentSymbol)
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

            if (State.getState(4, null, currentSymbol) == null) {
                currentSymbol = head
                while (head?.data != machine.blankSymbol) {
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
                                if (State.getState(9, read as Char, currentSymbol) != null)
                                    return
                            }

                            'γ' -> {
                                write = machine.omega
                                println("q$currentState: $read -> $write, $right")
                                //Write 'Ω' on head position
                                currentSymbol?.data = machine.omega
                                head = currentSymbol
                                if (State.getState(10, read as Char, currentSymbol) != null)
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
                    read = "F, K, N, S, A, B, Δ, θ, μ, Ω, x"
                    write = "F, K, N, S, A, B, Δ, θ, μ, Ω, x"
                    println("q$currentState: $read -> $right [${head?.data}]")

                    previousSymbol = currentSymbol
                    head = head?.next
                    currentSymbol = head
                }
                revertSymbol(currentSymbol)
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

            if (State.getState(12, null, currentSymbol) == null) {
                currentSymbol = head
                while (head?.data != machine.blankSymbol) {
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
                                if (State.getState(10, read as Char, currentSymbol) != null)
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
                                    return
                                }
                            }
                        }
                    }
                    read = "F, K, N, S, A, B, Δ, θ, μ, Ω, x"
                    write = "F, K, N, S, A, B, Δ, θ, μ, Ω, x"
                    println("q$currentState: $read -> $right [${head?.data}]")

                    previousSymbol = currentSymbol
                    head = head?.next
                    currentSymbol = head
                }
                revertSymbol(currentSymbol)
            }
        }

        fun giveItem(currentSymbol: Node?, currentState: Int) {
            read = currentSymbol?.data
            write = machine.crossSymbol
            println("q$currentState: $read -> $write, $left")
            println("Dispense '$read'")
            //Write 'x' on head position
            currentSymbol?.data = machine.crossSymbol
            head = currentSymbol
            State.getState(13, currentSymbol?.data, currentSymbol)
        }

        fun giveItemAlt(currentSymbol: Node?, currentState: Int) {
            read = currentSymbol?.data
            write = machine.crossSymbol
            println("q$currentState: $read -> $write, $left")
            println("Dispense '$read'")
            //Write 'x' on head position
            currentSymbol?.data = machine.crossSymbol
            head = currentSymbol
            State.getState(14, currentSymbol?.data, currentSymbol)
        }

        fun revertComboSymbol(currentSymbol: Node?) {
            State.getState(15, currentSymbol?.data, currentSymbol)
            println("Tape: ${tape.getData()}\n")
        }

        private fun revertSymbol(currentSymbol: Node?) {
            State.getState(16, currentSymbol?.data, currentSymbol)
            println("Tape: ${tape.getData()}\n")
        }

        fun replaceSymbol(symbol: Char, currentSymbol: Node?) {
            State.getState(7, symbol, currentSymbol)
        }

        fun goToFront() {
            State.getState(27, null, null)
        }
    }
}