package models.turing

import models.turing.Turing.Companion.head
import util.Node

class State {
    companion object {
        var initialState: Int = 1
        var finalState: Int = 31
        var currentState: Int = 0
        private var nextState: Int = 0
        private var result: Node? = null
        private var machine = Machine()
        private var read: Any? = null
        private var write: Any? = null
        private var right = 'R'
        private var left = 'L'

        fun getState(input: Char?, currentSymbol: Node?): Node? {
            when (currentState) {
                2 -> {
                    read = input
                    write = machine.alpha
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = machine.alpha
                    head = currentSymbol
                    //Check combinations for 'ɑ'
                    Transitions.alphaTransitions()
                }

                3 -> {
                    read = input
                    write = machine.theta
                    println("q$currentState: $read -> $write, $right")
                    //Write 'x' on current head position
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
                        //Write back the symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                5 -> {
                    read = input
                    write = machine.mu
                    println("q$currentState: $read -> $write, $right")
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
                        Transitions.giveItem(nextSymbol)
                        head
                    } else {
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                7 -> {
                    read = input
                    write = machine.omega
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = machine.omega
                    head = currentSymbol
                    nextState = 8
                    getNextState()
                }

                8 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Knife is a requested item
                    while (nextSymbol?.data != 'K') {
                        //Check if end of tape is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        if (nextSymbol?.data == 'F') {
                            nextState = 17
                            getNextState(currentSymbol?.data, currentSymbol)
                            //nextState = 6
                            //getNextState()
                            TODO("transition for N and F and custom give item function")
                        } else if (nextSymbol?.data == 'N') {
                            nextState = 18
                            getNextState(currentSymbol?.data, currentSymbol)
                            //nextState = 6
                            //getNextState()
                            TODO("transition for N and F and custom give item function")
                        }
                        read = "S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (found) {
                        Transitions.giveItem(nextSymbol)
                        head
                    } else {
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                9 -> {
                    read = input
                    write = machine.beta
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = machine.beta
                    head = currentSymbol
                    //Check combinations for 'β'
                    Transitions.betaTransitions()
                }

                10 -> {
                    read = input
                    write = machine.theta
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = machine.theta
                    head = currentSymbol
                    nextState = 6
                    getNextState()
                }

                11 -> {
                    read = input
                    write = machine.mu
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = machine.mu
                    head = currentSymbol
                    nextState = 12
                    getNextState()
                }

                12 -> {
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
                        Transitions.giveItem(nextSymbol)
                        head
                    } else {
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                13 -> {
                    read = input
                    write = machine.omega
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = machine.omega
                    head = currentSymbol
                    nextState = 14
                    getNextState()
                }

                14 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    var count = 0
                    val symbolArray = ArrayList<Node>()

                    while (count != 2) {
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        if (nextSymbol?.data == 'F') {
                            symbolArray.add(nextSymbol)
                            count++
                        }
                        read = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (found) {
                        symbolArray.forEach {
                            Transitions.giveItem(it)
                        }
                        head
                    } else {
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                15 -> {
                    read = input
                    write = machine.delta
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = machine.delta
                    head = currentSymbol
                    //Check combinations for 'γ'
                    Transitions.gammaTransitions()
                }

                16 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Napkin is a requested item
                    while (nextSymbol?.data != 'S') {
                        //Check if end of tape is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        if (nextSymbol?.data == 'F') {
                            nextState = 17
                            getNextState(currentSymbol?.data, currentSymbol)
                            Transitions.giveItem(nextSymbol)
                        }
                        if (nextSymbol?.data == 'N') {
                            nextState = 18
                            getNextState(currentSymbol?.data, currentSymbol)
                            Transitions.giveItem(nextSymbol)
                        }
                        read = "K, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        write = "K, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                        println("q$currentState: $read -> $right [${nextSymbol?.data}]")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol)
                        head
                    } else {
                        //Write back the symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                17 -> {
                    read = input
                    write = 'ɑ'
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = 'ɑ'
                    head = currentSymbol
                }

                18 -> {
                    read = input
                    write = 'β'
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = 'β'
                    head = currentSymbol
                }

                19 -> {
                    read = input
                    write = machine.theta
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = machine.theta
                    head = currentSymbol
                    nextState = 8
                    getNextState()
                }

                20 -> {
                    read = input
                    write = machine.mu
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = machine.mu
                    head = currentSymbol
                    nextState = 14
                    getNextState()
                }

                21 -> {
                    read = input
                    write = machine.omega
                    println("q$currentState: $read -> $write, $right")
                    currentSymbol?.data = machine.omega
                    head = currentSymbol
                    nextState = 6
                    getNextState()
                }

                22 -> {
                    read = input
                    write = machine.crossSymbol
                    println("q$currentState: $read -> $write, $left")
                    println("Dispense '$read'")
                    currentSymbol?.data = machine.crossSymbol
                    head = currentSymbol
                    nextState = 23
                    getNextState()
                }

                23 -> {
                    val symbolList =
                        arrayOf(machine.alpha, machine.beta, machine.delta, machine.theta, machine.mu, machine.omega)
                    var current: Node?
                    current = currentSymbol?.prev

                    while (current != null) {
                        val data = current.data
                        if (symbolList.contains(data)) {
                            nextState = 24
                            getNextState(data, current)
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

                24 -> {
                    read = input
                    write = machine.crossSymbol
                    println("q$currentState: $read -> $write, $left")
                    currentSymbol?.data = machine.crossSymbol
                }

                25 -> {
                    val symbolList = arrayOf(machine.theta, machine.mu, machine.omega)
                    var current = currentSymbol

                    while (current != null) {
                        val data = current.data
                        if (symbolList.contains(data)) {
                            read = data
                            if (read == machine.theta) {
                                nextState = 27
                                getNextState(current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.mu) {
                                nextState = 28
                                getNextState(current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.omega) {
                                nextState = 29
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

                26 -> {
                    val symbolList = arrayOf(machine.alpha, machine.beta, machine.delta)
                    var current = currentSymbol

                    while (current != null) {
                        val data = current.data
                        if (symbolList.contains(data)) {
                            read = data
                            if (read == machine.alpha) {
                                nextState = 27
                                getNextState(current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.beta) {
                                nextState = 28
                                getNextState(current.data, current)
                                head = current
                                return null
                            }
                            if (read == machine.delta) {
                                nextState = 29
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

                27 -> {
                    val replaceList = arrayOf('ɑ', 'β', 'γ')
                    write = replaceList[0]
                    println("q$currentState: $read -> $write, $left")
                    currentSymbol?.data = replaceList[0]
                }

                28 -> {
                    val replaceList = arrayOf('ɑ', 'β', 'γ')
                    write = replaceList[1]
                    println("q$currentState: $read -> $write, $left")
                    currentSymbol?.data = replaceList[1]
                }

                29 -> {
                    val replaceList = arrayOf('ɑ', 'β', 'γ')
                    write = replaceList[2]
                    println("q$currentState: $read -> $write, $left")
                    currentSymbol?.data = replaceList[2]
                }

                30 -> {
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

                31 -> {
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