package models.turing

import models.turing.Turing.Companion.head
import util.Node

class State {
    companion object {
        private var initialState: Int = 1
        var finalState: Int = 36
        var currentState: Int = 0
        private var nextState: Int = 0
        private var result: Node? = null
        private var machine = Machine()
        private val lowercaseSymbolList =
            arrayOf(machine.theta, machine.mu, machine.omega)
        private val uppercaseSymbolList =
            arrayOf(machine.alpha, machine.beta, machine.delta)
        private val replaceList = arrayOf('ɑ', 'β', 'γ')
        private var read: Any? = null
        private var write: Any? = null
        private var right = 'R'
        private var left = 'L'

        fun getState(input: Char?, currentSymbol: Node?): Node? {
            when (currentState) {
                initialState -> {
                    println("q${currentState}: Turing Machine started...")
                }

                2 -> {
                    read = input
                    write = machine.alpha
                    println("q$currentState: $read -> $write, $right")
                    //Write 'A' on head position
                    currentSymbol?.data = machine.alpha
                    head = currentSymbol
                    //Check combinations for 'ɑ'
                    Transitions.alphaTransitions()
                }

                3 -> {
                    read = input
                    write = machine.theta
                    println("q$currentState: $read -> $write, $right")
                    //Write 'θ' on head position
                    currentSymbol?.data = machine.theta
                    head = currentSymbol
                    nextState = 4
                    getNextState()
                }

                4 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Napkin is a requested item
                    while (nextSymbol?.data != 'N') {
                        //Check if end of input is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = "F, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "F, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol)
                        head
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                5 -> {
                    read = input
                    write = machine.mu
                    println("q$currentState: $read -> $write, $right")
                    //Write 'μ' on head position
                    currentSymbol?.data = machine.mu
                    head = currentSymbol
                    nextState = 6
                    getNextState()
                }

                6 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Fork is a requested item
                    while (nextSymbol?.data != 'F') {
                        //Check if end of tape is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol)
                        head
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                7 -> {
                    read = input
                    write = machine.omega
                    println("q$currentState: $read -> $write, $right")
                    //Write 'Ω' on head position
                    currentSymbol?.data = machine.omega
                    head = currentSymbol
                    nextState = 8
                    getNextState()
                }

                8 -> {
                    var found = true
                    var alternate = false
                    var nextSymbol = currentSymbol?.next
                    //Check if Knife is a requested item
                    while (nextSymbol?.data != 'K') {
                        //Check if end of tape is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        if (nextSymbol?.data == 'F') {
                            //Give the item and check it off
                            nextState = 19
                            getNextState(currentSymbol?.data, currentSymbol)
                            nextState = 9
                            getNextState()
                            alternate = true
                            break
                        } else if (nextSymbol?.data == 'N') {
                            //Give the item and check it off
                            nextState = 20
                            getNextState(currentSymbol?.data, currentSymbol)
                            nextState = 10
                            getNextState()
                            alternate = true
                            break
                        }
                        read = "S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (alternate) {
                        // Go to tape front
                        Transitions.goToFront()
                        null
                    } else if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol)
                        head
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                9 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Fork is a requested item
                    while (nextSymbol?.data != 'F') {
                        //Check if end of tape is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (found) {
                        //Give the item and check it off
                        Transitions.giveItemAlone(nextSymbol)
                        head
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                10 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Napkin is a requested item
                    while (nextSymbol?.data != 'N') {
                        //Check if end of input is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = "F, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "F, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (found) {
                        //Give the item and check it off
                        Transitions.giveItemAlone(nextSymbol)
                        head
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                11 -> {
                    read = input
                    write = machine.beta
                    println("q$currentState: $read -> $write, $right")
                    //Write 'B' on head position
                    currentSymbol?.data = machine.beta
                    head = currentSymbol
                    //Check combinations for 'β'
                    Transitions.betaTransitions()
                }

                12 -> {
                    read = input
                    write = machine.theta
                    println("q$currentState: $read -> $write, $right")
                    //Write 'θ' on head position
                    currentSymbol?.data = machine.theta
                    head = currentSymbol
                    nextState = 6
                    getNextState()
                }

                13 -> {
                    read = input
                    write = machine.mu
                    println("q$currentState: $read -> $write, $right")
                    //Write 'μ' on head position
                    currentSymbol?.data = machine.mu
                    head = currentSymbol
                    nextState = 14
                    getNextState()
                }

                14 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    while (nextSymbol?.data != 'S') {
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = "F, K, N, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "F, K, N, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol)
                        head
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                15 -> {
                    read = input
                    write = machine.omega
                    println("q$currentState: $read -> $write, $right")
                    //Write 'Ω' on head position
                    currentSymbol?.data = machine.omega
                    head = currentSymbol
                    nextState = 16
                    getNextState()
                }

                16 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    var alternate = false

                    while (nextSymbol?.data != 'S') {
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        if (nextSymbol?.data == 'F') {
                            //Give the item and check it off
                            nextState = 19
                            getNextState(currentSymbol?.data, currentSymbol)
                            nextState = 9
                            getNextState()
                            alternate = true
                            break
                        } else if (nextSymbol?.data == 'N') {
                            //Give the item and check it off
                            nextState = 20
                            getNextState(currentSymbol?.data, currentSymbol)
                            nextState = 10
                            getNextState()
                            alternate = true
                            break
                        }
                        read = "K, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "K, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (alternate) {
                        // Go to tape front
                        Transitions.goToFront()
                        null
                    } else if (found) {
                        //Give the item and check it off
                        Transitions.giveItemAlt(nextSymbol)
                        null
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                17 -> {
                    read = input
                    write = machine.delta
                    println("q$currentState: $read -> $write, $right")
                    //Write 'Δ' on head position
                    currentSymbol?.data = machine.delta
                    head = currentSymbol
                    //Check combinations for 'γ'
                    Transitions.gammaTransitions()
                }

                18 -> {
                    var found = true
                    var alternate = false
                    var nextSymbol = currentSymbol?.next
                    //Check if Napkin is a requested item
                    while (nextSymbol?.data != 'S') {
                        //Check if end of tape is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        if (nextSymbol?.data == 'F') {
                            //Give the item and check it off
                            nextState = 19
                            getNextState(currentSymbol?.data, currentSymbol)
                            nextState = 9
                            getNextState()
                            alternate = true
                            break
                        }
                        if (nextSymbol?.data == 'N') {
                            //Give the item and check it off
                            nextState = 20
                            getNextState(currentSymbol?.data, currentSymbol)
                            nextState = 10
                            getNextState()
                            alternate = true
                            break
                        }
                        read = "K, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "K, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (alternate) {
                        // Go to tape front
                        Transitions.goToFront()
                        null
                    } else if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol)
                        head
                    } else {
                        //Write back the symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                19 -> {
                    read = input
                    write = 'ɑ'
                    println("q$currentState: $read -> $write, $right")
                    //Write 'ɑ' on head position
                    currentSymbol?.data = 'ɑ'
                    head = currentSymbol
                }

                20 -> {
                    read = input
                    write = 'β'
                    println("q$currentState: $read -> $write, $right")
                    //Write 'β' on head position
                    currentSymbol?.data = 'β'
                    head = currentSymbol
                }

                21 -> {
                    read = input
                    write = machine.theta
                    println("q$currentState: $read -> $write, $right")
                    //Write 'θ' on head position
                    currentSymbol?.data = machine.theta
                    head = currentSymbol
                    nextState = 8
                    getNextState()
                }

                22 -> {
                    read = input
                    write = machine.mu
                    println("q$currentState: $read -> $write, $right")
                    //Write 'μ' on head position
                    currentSymbol?.data = machine.mu
                    head = currentSymbol
                    nextState = 16
                    getNextState()
                }

                23 -> {
                    read = input
                    write = machine.omega
                    println("q$currentState: $read -> $write, $right")
                    //Write 'Ω' on head position
                    currentSymbol?.data = machine.omega
                    head = currentSymbol
                    nextState = 6
                    getNextState()
                }

                24 -> {
                    read = input
                    write = machine.crossSymbol
                    println("q$currentState: $read -> $write, $left")
                    println("Dispense '$read'")
                    //Write 'x' on head position
                    currentSymbol?.data = machine.crossSymbol
                    head = currentSymbol
                    nextState = 25
                    getNextState()
                }

                25 -> {
                    var current: Node?
                    current = currentSymbol?.prev

                    while (current != null) {
                        val data = current.data
                        if (lowercaseSymbolList.contains(data) || uppercaseSymbolList.contains(data)) {
                            nextState = 26
                            getNextState(data, current)
                            currentState = 25
                        }
                        if (current.prev == null) {
                            head = current
                        }
                        read = "F, K, N, S, ɑ, β, γ, x"
                        write = "F, K, N, S, ɑ, β, γ, x"
                        println("q$currentState: $read -> $left [${current.data}]")
                        current = current.prev
                    }
                }

                26 -> {
                    read = input
                    write = machine.crossSymbol
                    println("q$currentState: $read -> $write, $left")
                    //Write 'x' on head position
                    currentSymbol?.data = machine.crossSymbol
                }

                27 -> {
                    read = input
                    write = machine.crossSymbol
                    println("q$currentState: $read -> $write, $left")
                    println("Dispense '$read'")
                    //Write 'x' on head position
                    currentSymbol?.data = machine.crossSymbol
                    head = currentSymbol
                    nextState = 28
                    getNextState()
                }

                28 -> {
                    var current: Node?
                    current = currentSymbol?.prev

                    while (current != null) {
                        val data = current.data
                        if (lowercaseSymbolList.contains(data)) {
                            nextState = 26
                            getNextState(data, current)
                            currentState = 28
                        }
                        if (current.prev == null) {
                            head = current
                        }
                        read = "F, K, N, S, ɑ, β, γ, x"
                        write = "F, K, N, S, ɑ, β, γ, x"
                        println("q$currentState: $read -> $left [${current.data}]")
                        current = current.prev
                    }
                }

                29 -> {
                    read = input
                    write = machine.crossSymbol
                    println("q$currentState: $read -> $write, $left")
                    println("Dispense '$read'")
                    //Write 'x' on head position
                    currentSymbol?.data = machine.crossSymbol
                    head = currentSymbol
                }

                30 -> {
                    var current = currentSymbol

                    while (current != null) {
                        val data = current.data
                        if (lowercaseSymbolList.contains(data)) {
                            read = data
                            if (read == machine.theta) {
                                nextState = 32
                                getNextState(current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.mu) {
                                nextState = 33
                                getNextState(current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.omega) {
                                nextState = 34
                                getNextState(current.data, current)
                                head = current
                                return null
                            }
                        }
                        if (current.prev == null) {
                            head = current
                        }
                        read = "F, K, N, S, ɑ, β, γ, A, B, Δ, x"
                        write = "F, K, N, S, ɑ, β, γ, A, B, Δ, x"
                        println("q$currentState: $read -> $left [${current.data}]")
                        current = current.prev
                    }
                }

                31 -> {
                    var current = currentSymbol

                    while (current != null) {
                        val data = current.data
                        if (uppercaseSymbolList.contains(data)) {
                            read = data
                            if (read == machine.alpha) {
                                nextState = 32
                                getNextState(current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.beta) {
                                nextState = 33
                                getNextState(current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.delta) {
                                nextState = 34
                                getNextState(current.data, current)
                                head = current
                                return null
                            }
                        }
                        if (current.prev == null) {
                            head = current
                        }
                        read = "F, K, N, S, ɑ, β, γ, A, B, Δ, x"
                        write = "F, K, N, S, ɑ, β, γ, A, B, Δ, x"
                        println("q$currentState: $read -> $left [${current.data}]")
                        current = current.prev
                    }
                }

                32 -> {
                    write = replaceList[0]
                    println("q$currentState: $read -> $write, $right")
                    //Write 'ɑ' on head position
                    currentSymbol?.data = replaceList[0]
                }

                33 -> {
                    write = replaceList[1]
                    println("q$currentState: $read -> $write, $right")
                    //Write 'β' on head position
                    currentSymbol?.data = replaceList[1]
                }

                34 -> {
                    write = replaceList[2]
                    println("q$currentState: $read -> $write, $right")
                    //Write 'γ' on head position
                    currentSymbol?.data = replaceList[2]
                }

                35 -> {
                    head = head?.prev

                    if (head != null) {
                        while (head?.prev != null) {
                            head = head!!.prev
                            read = "F, K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                            write = "F, K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                            println("q$currentState: $read -> $left [${head?.data}]")
                        }
                        println("Tape: ${Turing.tape.getData()}\n")
                    }
                }

                finalState -> {
                    //End transition/Halt
                    println("Halt")
                }
            }
            return result
        }

        private fun getNextState() {
            currentState = nextState
            getState(null, head)
        }

        private fun getNextState(read: Char?, currentSymbol: Node?) {
            currentState = nextState
            getState(read, currentSymbol)
        }
    }
}