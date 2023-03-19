package models.turing

import models.turing.Turing.Companion.head
import util.Helper
import util.Node

class State {
    companion object {
        private var initialState: Int = 1
        var acceptState: Int = 29
        var rejectState: Int = 28
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
                    getNextState(24)
                }

                2 -> {
                    read = input
                    when (read) {
                        'ɑ' -> {
                            write = machine.alpha
                            println("q$currentState: $read -> $write, $right")
                            //Write 'A' on head position
                            currentSymbol?.data = machine.alpha
                            head = currentSymbol
                            getNextState(3, currentSymbol?.data, currentSymbol)
                        }

                        'β' -> {
                            write = machine.beta
                            println("q$currentState: $read -> $write, $right")
                            //Write 'B' on head position
                            currentSymbol?.data = machine.beta
                            head = currentSymbol
                            getNextState(8, currentSymbol?.data, currentSymbol)
                        }

                        'γ' -> {
                            write = machine.delta
                            println("q$currentState: $read -> $write, $right")
                            //Write 'Δ' on head position
                            currentSymbol?.data = machine.delta
                            head = currentSymbol
                            getNextState(11, currentSymbol?.data, currentSymbol)
                        }

                        machine.blankSymbol -> {
                            getNextState(17)
                        }
                    }
                }

                3 -> {
                    //Check combinations for 'ɑ'
                    Transitions.alphaTransitions(3)
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
                        Transitions.giveItem(nextSymbol, currentState)
                        head
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                5 -> {
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
                        Transitions.giveItem(nextSymbol, currentState)
                        head
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                6 -> {
                    var found = true
                    var alternate = false
                    var current = currentSymbol?.next
                    //Check if Knife is a requested item
                    while (current?.data != 'K') {
                        //Check if end of tape is reached
                        if (current?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        if (current?.data == 'F') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            println("Dispense '$read'")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            Transitions.replaceSymbol('ɑ', head)
                            alternate = true
                            break
                        } else if (current?.data == 'N') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            println("Dispense '$read'")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            Transitions.replaceSymbol('β', head)
                            alternate = true
                            break
                        }
                        read = "S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${current?.data}]")
                        //Move one position to the right
                        current = current?.next
                    }

                    result = if (alternate) {
                        // Go to tape front
                        Transitions.goToFront()
                        null
                    } else if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(current, currentState)
                        head
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(current)
                        null
                    }
                }

                7 -> {
                    var current = currentSymbol?.prev
                    while (current != null) {
                        if (current.data == machine.delta || current.data == machine.omega) {
                            read = current.data
                            write = input
                            println("q$currentState: $read -> $write, $left")
                            //Write the replacement symbol on head position
                            current.data = input!!
                            head = current
                            break
                        }
                        if (current.prev == null) {
                            head = current
                        }
                        read = "F, K, N, S, ɑ, β, γ, A, B, θ, μ, Ω, x"
                        write = "F, K, N, ɑ, β, γ, A, B, θ, μ, Ω, x"
                        println("q$currentState: $read -> $left [${current.data}]")
                        //Move one position to the right
                        current = current.prev
                    }
                }

                8 -> {
                    //Check combinations for 'β'
                    Transitions.betaTransitions(8)
                }

                9 -> {
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
                        Transitions.giveItem(nextSymbol, currentState)
                        head
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                10 -> {
                    var found = true
                    var current = currentSymbol?.next
                    var alternate = false

                    while (current?.data != 'S') {
                        if (current?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        if (current?.data == 'F') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            println("Dispense '$read'")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            Transitions.replaceSymbol('ɑ', head)
                            alternate = true
                            break
                        } else if (current?.data == 'N') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            println("Dispense '$read'")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            Transitions.replaceSymbol('β', head)
                            alternate = true
                            break
                        }
                        read = "K, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "K, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${current?.data}]")
                        //Move one position to the right
                        current = current?.next
                    }

                    result = if (alternate) {
                        // Go to tape front
                        Transitions.goToFront()
                        null
                    } else if (found) {
                        //Give the item and check it off
                        Transitions.giveItemAlt(current, currentState)
                        null
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(current)
                        null
                    }
                }

                11 -> {
                    //Check combinations for 'γ'
                    Transitions.gammaTransitions(11)
                }

                12 -> {
                    var found = true
                    var alternate = false
                    var current = currentSymbol?.next
                    //Check if Napkin is a requested item
                    while (current?.data != 'S') {
                        //Check if end of tape is reached
                        if (current?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        if (current?.data == 'F') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            println("Dispense '$read'")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            Transitions.replaceSymbol('ɑ', head)
                            alternate = true
                            break
                        }
                        if (current?.data == 'N') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            println("Dispense '$read'")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            Transitions.replaceSymbol('β', head)
                            alternate = true
                            break
                        }
                        read = "K, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "K, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${current?.data}]")
                        //Move one position to the right
                        current = current?.next
                    }

                    result = if (alternate) {
                        // Go to tape front
                        Transitions.goToFront()
                        null
                    } else if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(current, currentState)
                        head
                    } else {
                        //Write back the symbols
                        Transitions.revertComboSymbol(current)
                        null
                    }
                }

                13 -> {
                    var current: Node?
                    current = currentSymbol?.prev

                    while (current != null) {
                        val data = current.data
                        if (lowercaseSymbolList.contains(data) || uppercaseSymbolList.contains(data)) {
                            read = data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
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

                14 -> {
                    var current: Node?
                    current = currentSymbol?.prev

                    while (current != null) {
                        val data = current.data
                        if (lowercaseSymbolList.contains(data)) {
                            read = data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
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

                15 -> {
                    var current = currentSymbol

                    while (current != null) {
                        val data = current.data
                        if (lowercaseSymbolList.contains(data)) {
                            read = data
                            if (read == machine.theta) {
                                write = replaceList[0]
                                println("q$currentState: $read -> $write, $right")
                                //Write 'ɑ' on head position
                                current.data = replaceList[0]
                                return null
                            }
                            if (read == machine.mu) {
                                write = replaceList[1]
                                println("q$currentState: $read -> $write, $right")
                                //Write 'β' on head position
                                current.data = replaceList[1]
                                return null
                            }
                            if (read == machine.omega) {
                                write = replaceList[2]
                                println("q$currentState: $read -> $write, $right")
                                //Write 'γ' on head position
                                current.data = replaceList[2]
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

                16 -> {
                    var current = currentSymbol

                    while (current != null) {
                        val data = current.data
                        if (uppercaseSymbolList.contains(data)) {
                            read = data
                            if (read == machine.alpha) {
                                write = replaceList[0]
                                println("q$currentState: $read -> $write, $right")
                                //Write 'ɑ' on head position
                                current.data = replaceList[0]
                                return null
                            }
                            if (read == machine.beta) {
                                write = replaceList[1]
                                println("q$currentState: $read -> $write, $right")
                                //Write 'β' on head position
                                current.data = replaceList[1]
                                return null
                            }
                            if (read == machine.delta) {
                                write = replaceList[2]
                                println("q$currentState: $read -> $write, $right")
                                //Write 'γ' on head position
                                current.data = replaceList[2]
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

                17 -> {
                    var current = currentSymbol

                    while (current != null) {
                        val data = current.data
                        if (uppercaseSymbolList.contains(data)) {
                            read = data
                            if (read == machine.alpha) {
                                write = replaceList[0]
                                println("q$currentState: $read -> $write, $right")
                                //Write 'ɑ' on head position
                                current.data = replaceList[0]
                            }
                            if (read == machine.beta) {
                                write = replaceList[1]
                                println("q$currentState: $read -> $write, $right")
                                //Write 'β' on head position
                                current.data = replaceList[1]
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

                    getNextState(18)
                }

                18 -> {
                    var alphaCounter = 0
                    var betaCounter = 0

                    while (head?.data != machine.blankSymbol) {
                        when (head?.data) {
                            'ɑ' -> {
                                alphaCounter++
                                read = currentSymbol?.data
                                write = machine.alpha
                                println("q$currentState: $read -> $write, $right")
                                //Write 'A' on head position
                                head?.data = machine.alpha
                            }

                            'β' -> {
                                betaCounter++
                                read = currentSymbol?.data
                                write = machine.beta
                                println("q$currentState: $read -> $write, $right")
                                //Write 'B' on head position
                                head?.data = machine.beta
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
                    getNextState(22)
                }

                19 -> {
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
                        Transitions.giveItem(nextSymbol, currentState)
                        head
                    } else {
                        null
                    }
                }

                20 -> {
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
                        Transitions.giveItem(nextSymbol, currentState)
                        head
                    } else {
                        null
                    }
                }

                21 -> {
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
                        Transitions.giveItem(nextSymbol, currentState)
                        head
                    } else {
                        null
                    }
                }

                22 -> {
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
                    getNextState(23)
                }

                23 -> {
                    while (head?.data != machine.blankSymbol) {
                        if (head?.data == 'F' || head?.data == 'K' || head?.data == 'N' || head?.data == 'S') {
                            println("\nInsufficient funds")
                            getNextState(rejectState)
                            return null
                        }
                        if (lowercaseSymbolList.contains(head?.data) || uppercaseSymbolList.contains(head?.data)
                                    || head?.data == 'ɑ' || head?.data == 'β' || head?.data == 'γ') {
                            getNextState(rejectState)
                            return null
                        }
                        read = "x"
                        write = "x"
                        println("q$currentState: $read -> $write, $right [${head?.data}]")
                        head = head?.next
                    }
                    getNextState(acceptState)
                }

                24 -> {
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
                    getNextState(25)
                }

                25 -> {
                    restockString.forEach {
                        if (it == head?.data) {
                            head = head?.next
                            read = "F, K, N, S"
                            write = "F, K, N, S"
                            println("q$currentState: $read -> $write, $right [${head?.data}]")
                        } else {
                            getNextState(26)
                            return null
                        }
                    }
                    getNextState(acceptState)
                }

                26 -> {
                    val current = head?.prev
                    if (current?.data != null) {
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
                    getNextState(2)
                }

                27 -> {
                    if (head?.prev != null) {
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

                rejectState -> {
                    Helper.refund()
                    //Stop machine
                    this.currentState = rejectState
                    println("qr: Halt")
                }

                acceptState -> {
                    //Stop machine
                    this.currentState = acceptState
                    println("\nqa: Halt")
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