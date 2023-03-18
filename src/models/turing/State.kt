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

        fun getState(currentState: Int, input: Char?, currentSymbol: Node?): Node? {
            when (currentState) {
                initialState -> {
                    println("q${currentState}: Turing Machine started...")
                    getNextState(1)
                }

                1 -> {
                    while (head?.data != machine.blankSymbol) {
                        //Check for invalid input
                        if (!machine.tapeAlphabet.contains(head?.data)) {
                            println("Invalid input detected!")
                            getNextState(rejectState)
                            return null
                        }
                        read = "F, N, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "F, N, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${head?.data}]")
                        head = head?.next
                    }
                    getNextState(44)
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
                    getNextState(4)
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
                    getNextState(6)
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
                    getNextState(8)
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
                            getNextState(19, currentSymbol?.data, currentSymbol)
                            getNextState(9)
                            alternate = true
                            break
                        } else if (nextSymbol?.data == 'N') {
                            //Give the item and check it off
                            getNextState(20, currentSymbol?.data, currentSymbol)
                            getNextState(10)
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
                    getNextState(6)
                }

                13 -> {
                    read = input
                    write = machine.mu
                    println("q$currentState: $read -> $write, $right")
                    //Write 'μ' on head position
                    currentSymbol?.data = machine.mu
                    head = currentSymbol
                    getNextState(14)
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
                    getNextState(16)
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
                            getNextState(19, currentSymbol?.data, currentSymbol)
                            getNextState(9)
                            alternate = true
                            break
                        } else if (nextSymbol?.data == 'N') {
                            //Give the item and check it off
                            getNextState(20, currentSymbol?.data, currentSymbol)
                            getNextState(10)
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
                            getNextState(19, currentSymbol?.data, currentSymbol)
                            getNextState(9)
                            alternate = true
                            break
                        }
                        if (nextSymbol?.data == 'N') {
                            //Give the item and check it off
                            getNextState(20, currentSymbol?.data, currentSymbol)
                            getNextState(10)
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
                    getNextState(8)
                }

                22 -> {
                    read = input
                    write = machine.mu
                    println("q$currentState: $read -> $write, $right")
                    //Write 'μ' on head position
                    currentSymbol?.data = machine.mu
                    head = currentSymbol
                    getNextState(16)
                }

                23 -> {
                    read = input
                    write = machine.omega
                    println("q$currentState: $read -> $write, $right")
                    //Write 'Ω' on head position
                    currentSymbol?.data = machine.omega
                    head = currentSymbol
                    getNextState(6)
                }

                24 -> {
                    read = input
                    write = machine.crossSymbol
                    println("q$currentState: $read -> $write, $left")
                    println("Dispense '$read'")
                    //Write 'x' on head position
                    currentSymbol?.data = machine.crossSymbol
                    head = currentSymbol
                    getNextState(25)
                }

                25 -> {
                    var current: Node?
                    current = currentSymbol?.prev

                    while (current != null) {
                        val data = current.data
                        if (lowercaseSymbolList.contains(data) || uppercaseSymbolList.contains(data)) {
                            getNextState(26, data, current)
                            this.currentState = 25
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
                    getNextState(28)
                }

                28 -> {
                    var current: Node?
                    current = currentSymbol?.prev

                    while (current != null) {
                        val data = current.data
                        if (lowercaseSymbolList.contains(data)) {
                            getNextState(26, data, current)
                            this.currentState = 28
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
                                getNextState(32, current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.mu) {
                                getNextState(33, current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.omega) {
                                getNextState(34, current.data, current)
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
                                getNextState(32, current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.beta) {
                                getNextState(33, current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.delta) {
                                getNextState(34, current.data, current)
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
                    getNextState(41)
                    this.currentState = 35

                    var alphaCounter = 0
                    var betaCounter = 0

                    while (head?.data != machine.blankSymbol) {
                        when (head?.data) {
                            'ɑ' -> {
                                alphaCounter++
                                getNextState(36)
                            }

                            'β' -> {
                                betaCounter++
                                getNextState(37)
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
                    getNextState(43)
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
                                getNextState(32, current.data, current)
                                this.currentState = 41
                            }
                            if (read == machine.beta) {
                                getNextState(33, current.data, current)
                                this.currentState = 41
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
                    getNextState(42)
                    this.currentState = 43

                    while (head?.data != machine.blankSymbol) {
                        if (head?.data == 'F' || head?.data == 'K' || head?.data == 'N' || head?.data == 'S') {
                            println("\nInsufficient funds")
                            getNextState(rejectState)
                            return null
                        }
                        if (head?.data == 'A' || head?.data == 'B' || head?.data == 'Δ') {
                            getNextState(rejectState)
                            return null
                        }
                        read = "ɑ, β, γ, θ, μ, Ω, x"
                        write = "ɑ, β, γ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${head?.data}]")
                        head = head?.next
                    }
                    getNextState(acceptState)
                }

                44 -> {
                    getNextState(42)

                    restockString.forEach {
                        if (it == head?.data) {
                            head = head?.next
                            read = "F, K, N, S"
                            write = "F, K, N, S"
                            println("q$currentState: $read -> $write, $right [${head?.data}]")
                        } else {
                            getNextState(42)
                            return null
                        }
                    }
                    getNextState(acceptState)
                }

                rejectState -> {
                    Helper.refund()
                    //Stop machine
                    this.currentState = rejectState
                    println("q$currentState: Reject and Halt")
                }

                acceptState -> {
                    Helper.refund()
                    //Stop machine
                    this.currentState = acceptState
                    println("\nq${currentState}: Accept and Halt")
                }
            }
            return result
        }

        private fun getNextState(nextState: Int): Node? {
            return getState(nextState, null, head)
        }

        private fun getNextState(nextState: Int, read: Char?, currentSymbol: Node?) {
            getState(nextState, read, currentSymbol)
        }
    }
}