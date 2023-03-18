package models.turing

import models.registers.Register
import models.turing.Turing.Companion.head
import util.Helper
import util.Node

class State {
    companion object {
        private var initialState: Int = 0
        var acceptState: Int = 46
        var rejectState: Int = 45
        var currentState: Int = 0
        private var nextState: Int = 0
        private var result: Node? = null
        private var machine = Machine()
        private val lowercaseSymbolList =
            arrayOf(machine.theta, machine.mu, machine.omega)
        private val uppercaseSymbolList =
            arrayOf(machine.alpha, machine.beta, machine.delta)
        private val replaceList = arrayOf('ɑ', 'β', 'γ')
        private val restockString = arrayOf('N', 'K', 'S', 'F', 'F', 'S', 'K', 'S')
        private var read: Any? = null
        private var write: Any? = null
        private var right = 'R'
        private var left = 'L'

        fun getState(input: Char?, currentSymbol: Node?): Node? {
            when (currentState) {
                initialState -> {
                    println("q${currentState}: Turing Machine started...")
                    nextState = 1
                    getNextState()
                }

                1 -> {
                    while (head?.data != machine.blankSymbol) {
                        //Check for invalid input
                        if (!machine.tapeAlphabet.contains(head?.data)) {
                            println("Invalid input detected!")
                            nextState = rejectState
                            getNextState()
                            return null
                        }
                        read = "F, N, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "F, N, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${head?.data}]")
                        head = head?.next
                    }
                    nextState = 44
                    getNextState()
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
                        //Move one position to the left
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
                        //Move one position to the left
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
                        //Move one position to the left
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
                        read = "F, K, N, S, ɑ, β, γ, θ, μ, Ω, x"
                        write = "F, K, N, S, ɑ, β, γ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $left [${current.data}]")
                        //Move one position to the left
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
                    nextState = 41
                    getNextState()
                    currentState = 35

                    var alphaCounter = 0
                    var betaCounter = 0

                    while (head?.data != machine.blankSymbol) {
                        when (head?.data) {
                            'ɑ' -> {
                                alphaCounter++
                                nextState = 36
                                getNextState()
                            }

                            'β' -> {
                                betaCounter++
                                nextState = 37
                                getNextState()
                            }
                        }
                        //Check for symbols
                        if (Helper.checkCount(alphaCounter, betaCounter) != null) {
                            betaCounter = 0
                            alphaCounter = 0
                        }
                        //Move one position to the right
                        head = head!!.next
                        read = "F, K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "F, K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${head?.data}]")
                    }
                    println("Tape: ${Turing.tape.getData()}\n")

                    nextState = 43
                    getNextState()
                }

                36 -> {
                    read = currentSymbol?.data
                    write = machine.alpha
                    println("q$currentState: $read -> $write, $right")
                    //Write 'A' on head position
                    currentSymbol?.data = machine.alpha
                }

                37 -> {
                    read = currentSymbol?.data
                    write = machine.beta
                    println("q$currentState: $read -> $write, $right")
                    //Write 'B' on head position
                    currentSymbol?.data = machine.beta
                }

                38 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Fork is a requested item
                    while (nextSymbol?.data != 'K') {
                        //Check if end of tape is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = "F, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "F, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol)
                        head
                    } else {
                        null
                    }
                }

                39 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Fork is a requested item
                    while (nextSymbol?.data != 'S') {
                        //Check if end of tape is reached
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
                        null
                    }
                }

                40 -> {
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
                        null
                    }
                }

                41 -> {
                    var current = currentSymbol

                    while (current != null) {
                        val data = current.data
                        if (uppercaseSymbolList.contains(data)) {
                            read = data
                            if (read == machine.alpha) {
                                nextState = 32
                                getNextState(current.data, current)
                                currentState = 41
                            }
                            if (read == machine.beta) {
                                nextState = 33
                                getNextState(current.data, current)
                                currentState = 41
                            }
                        }
                        if (current.prev == null) {
                            head = current
                        }
                        read = "F, K, N, S, ɑ, β, γ, θ, μ, Ω, x"
                        write = "F, K, N, S, ɑ, β, γ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $left [${current.data}]")
                        //Move one position to the left
                        current = current.prev
                    }
                }

                42 -> {
                    val current = head?.prev
                    if (current != null) {
                        head = head?.prev
                        while (head?.prev != null) {
                            //Move one position to the left
                            head = head!!.prev
                            read = "F, K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                            write = "F, K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                            println("q$currentState: $read -> $left [${head?.data}]")
                        }
                        println("Tape: ${Turing.tape.getData()}\n")
                    }
                }

                43 -> {
                    nextState = 42
                    getNextState()
                    currentState = 43

                    while (head?.data != machine.blankSymbol) {
                        if (head?.data == 'F' || head?.data == 'K' || head?.data == 'N' || head?.data == 'S') {
                            println("\nInsufficient funds")
                            nextState = rejectState
                            getNextState()
                            return null
                        }
                        if (head?.data == 'A' || head?.data == 'B' || head?.data == 'Δ') {
                            nextState = rejectState
                            getNextState()
                            return null
                        }
                        read = "ɑ, β, γ, θ, μ, Ω, x"
                        write = "ɑ, β, γ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${head?.data}]")
                        head = head?.next
                    }
                    nextState = acceptState
                    getNextState()
                }

                44 -> {
                    nextState = 42
                    getNextState()

                    restockString.forEach {
                        if (it == head?.data) {
                            head = head?.next
                            read = "F, K, N, S"
                            write = "F, K, N, S"
                            println("q$currentState: $read -> $write, $right [${head?.data}]")
                        } else {
                            nextState = 42
                            getNextState()
                            return null
                        }
                    }
                    nextState = acceptState
                    getNextState()
                }

                rejectState -> {
                    Helper.refund()
                    //Stop machine
                    currentState = rejectState
                    println("q$currentState: Reject and Halt")
                }

                acceptState -> {
                    Helper.refund()
                    //Stop machine
                    currentState = acceptState
                    println("\nq${currentState}: Accept and Halt")
                }
            }
            return result
        }

        private fun getNextState(): Node? {
            currentState = nextState
            return getState(null, head)
        }

        private fun getNextState(read: Char?, currentSymbol: Node?) {
            currentState = nextState
            getState(read, currentSymbol)
        }

    }
}