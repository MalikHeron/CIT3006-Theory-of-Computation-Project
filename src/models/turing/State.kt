package models.turing

import models.turing.Turing.Companion.head
import util.Helper
import util.Node
import kotlin.system.exitProcess

class State {
    companion object {
        private var initialState: Int = 1
        var acceptState: Int = 31
        var rejectState: Int = 30
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
                    head = head?.next
                    while (head?.data != machine.blankSymbol) {
                        //Check for invalid input
                        if (!machine.tapeAlphabet.contains(head?.data)) {
                            println("Invalid input detected!")
                            getNextState(rejectState)
                            return null
                        }
                        read = head?.data
                        write = head?.data
                        println("q$currentState: $read -> $right")
                        head = head?.next
                    }
                    getNextState(25)
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
                            getNextState(9, currentSymbol?.data, currentSymbol)
                        }

                        'γ' -> {
                            write = machine.delta
                            println("q$currentState: $read -> $write, $right")
                            //Write 'Δ' on head position
                            currentSymbol?.data = machine.delta
                            head = currentSymbol
                            getNextState(12, currentSymbol?.data, currentSymbol)
                        }

                        machine.blankSymbol -> {
                            //Move one position to the right
                            write = machine.blankSymbol
                            println("q$currentState: $read -> $write, $left")
                            getNextState(18)
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
                        read = nextSymbol?.data
                        write = nextSymbol?.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol, currentState)
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
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
                        read = nextSymbol?.data
                        write = nextSymbol?.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol, currentState)
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
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
                            getNextState(7, 'ɑ', head)
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
                            getNextState(8, 'β', head)
                            alternate = true
                            break
                        }
                        read = current?.data
                        write = current?.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        current = current?.next
                    }

                    if (alternate) {
                        // Go to tape front
                        Transitions.goToFront()
                    } else if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(current, currentState)
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(current)
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
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $right")
                            //Move one position to the right
                            head = current.next
                        } else {
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $left")
                        }
                        //Move one position to the right
                        current = current.prev
                    }
                }

                8 -> {
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
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $right")
                            //Move one position to the right
                            head = current.next
                        } else {
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $left")
                        }
                        //Move one position to the right
                        current = current.prev
                    }
                }

                9 -> {
                    //Check combinations for 'β'
                    Transitions.betaTransitions(9)
                }

                10 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    while (nextSymbol?.data != 'S') {
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = nextSymbol?.data
                        write = nextSymbol?.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol, currentState)
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                    }
                }

                11 -> {
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
                            getNextState(7, 'ɑ', head)
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
                            getNextState(8, 'β', head)
                            alternate = true
                            break
                        }
                        read = current?.data
                        write = current?.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        current = current?.next
                    }

                    if (alternate) {
                        // Go to tape front
                        Transitions.goToFront()
                    } else if (found) {
                        //Give the item and check it off
                        Transitions.giveItemAlt(current, currentState)
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(current)
                    }
                }

                12 -> {
                    //Check combinations for 'γ'
                    Transitions.gammaTransitions(12)
                }

                13 -> {
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
                            getNextState(7, 'ɑ', head)
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
                            getNextState(8, 'β', head)
                            alternate = true
                            break
                        }
                        read = current?.data
                        write = current?.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        current = current?.next
                    }

                    if (alternate) {
                        // Go to tape front
                        Transitions.goToFront()
                    } else if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(current, currentState)
                    } else {
                        //Write back the symbols
                        Transitions.revertComboSymbol(current)
                    }
                }

                14 -> {
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
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $right")
                            //Move one position to the right
                            head = current.next
                        } else {
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $left")
                        }
                        //Move one position to the left
                        current = current.prev
                    }
                    getNextState(29)
                }

                15 -> {
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
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $right")
                            //Move one position to the right
                            head = current.next
                        } else {
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $left")
                        }
                        //Move one position to the left
                        current = current.prev
                    }
                }

                16 -> {
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
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $right")
                            //Move one position to the right
                            head = current.next
                        } else {
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $left")
                        }
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
                            if (read == machine.delta) {
                                write = replaceList[2]
                                println("q$currentState: $read -> $write, $right")
                                //Write 'γ' on head position
                                current.data = replaceList[2]
                            }
                        }
                        if (current.prev == null) {
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $right")
                            //Move one position to the right
                            head = current.next
                        } else {
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $left")
                        }
                        //Move one position to the left
                        current = current.prev
                    }
                    getNextState(18)
                }

                18 -> {
                    var current = currentSymbol?.prev

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
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $right")
                            //Move one position to the right
                            head = current.next
                        } else {
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $left")
                        }
                        //Move one position to the left
                        current = current.prev
                    }

                    getNextState(19)
                }

                19 -> {
                    var alphaCounter = 0
                    var betaCounter = 0

                    while (head?.data != machine.blankSymbol) {
                        when (head?.data) {
                            'ɑ' -> {
                                alphaCounter++
                                read = head?.data
                                write = machine.alpha
                                println("q$currentState: $read -> $write, $right")
                                //Write 'A' on head position
                                head?.data = machine.alpha
                            }

                            'β' -> {
                                betaCounter++
                                read = head?.data
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
                        if (head?.next != null) {
                            read = head?.data
                            write = head?.data
                            println("q$currentState: $read -> $write, $right")
                            //Move one position to the right
                            head = head!!.next
                        } else {
                            return null
                        }
                    }
                    println("Tape: ${Turing.tape.getData()}\n")
                    getNextState(23)
                }

                20 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Fork is a requested item
                    while (nextSymbol?.data != 'K') {
                        //Check if end of tape is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = nextSymbol?.data
                        write = nextSymbol?.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol, currentState)
                    }
                }

                21 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Fork is a requested item
                    while (nextSymbol?.data != 'S') {
                        //Check if end of tape is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = nextSymbol?.data
                        write = nextSymbol?.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol, currentState)
                    }
                }

                22 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Fork is a requested item
                    while (nextSymbol?.data != 'F') {
                        //Check if end of tape is reached
                        if (nextSymbol?.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = nextSymbol?.data
                        write = nextSymbol?.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    if (found) {
                        //Give the item and check it off
                        Transitions.giveItem(nextSymbol, currentState)
                    }
                }

                23 -> {
                    val current = head?.prev
                    if (current != null) {
                        head = head?.prev
                        while (head?.prev != null) {
                            //Move one position to the left
                            head = head!!.prev
                            read = head?.data
                            write = head?.data
                            println("q$currentState: $read -> $write, $left")
                        }
                        head = head?.next
                        println("Tape: ${Turing.tape.getData()}\n")
                    }
                    getNextState(24)
                }

                24 -> {
                    while (head?.data != machine.blankSymbol) {
                        if (head?.data == 'F' || head?.data == 'K' || head?.data == 'N' || head?.data == 'S') {
                            println("\nInsufficient funds")
                            getNextState(rejectState)
                            return null
                        }
                        if (lowercaseSymbolList.contains(head?.data) || uppercaseSymbolList.contains(head?.data)
                            || head?.data == 'ɑ' || head?.data == 'β' || head?.data == 'γ'
                        ) {
                            getNextState(rejectState)
                            return null
                        }
                        read = head?.data
                        write = head?.data
                        println("q$currentState: $read -> $write, $right")
                        head = head?.next
                    }
                    getNextState(acceptState)
                }

                25 -> {
                    val current = head?.prev
                    if (current != null) {
                        head = head?.prev
                        while (head?.prev != null) {
                            //Move one position to the left
                            head = head!!.prev
                            read = head?.data
                            write = head?.data
                            if (head?.prev == null) {
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                println("q$currentState: $read -> $write, $left")
                            }
                        }
                        head = head?.next
                        println("Tape: ${Turing.tape.getData()}\n")
                    }
                    getNextState(26)
                }

                26 -> {
                    restockString.forEach {
                        if (it == head?.data) {
                            head = head?.next
                            read = head?.data
                            write = head?.data
                            if (head?.prev == null) {
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                println("q$currentState: $read -> $write, $left")
                            }
                        } else {
                            getNextState(27)
                            return null
                        }
                    }
                    getNextState(acceptState)
                }

                27 -> {
                    val current = head?.prev
                    if (current != null) {
                        head = head?.prev
                        while (head?.prev != null) {
                            //Move one position to the left
                            head = head!!.prev
                            read = head?.data
                            write = head?.data
                            if (head?.prev == null) {
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                println("q$currentState: $read -> $write, $left")
                            }
                        }
                        head = head?.next
                        println("Tape: ${Turing.tape.getData()}\n")
                    }
                    getNextState(29)
                }

                28 -> {
                    if (head?.prev != null) {
                        while (head?.prev != null) {
                            //Move one position to the left
                            head = head!!.prev
                            read = head?.data
                            write = head?.data
                            if (head?.prev == null) {
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                println("q$currentState: $read -> $write, $left")
                            }
                        }
                        head = head?.next
                        println("Tape: ${Turing.tape.getData()}\n")
                    }
                }

                29 -> {
                    //Loop while not at the end of the tape
                    while (head != null && head?.data != machine.blankSymbol) {
                        getNextState(2, head?.data, head)
                        if (head?.next != null) {
                            read = head?.data
                            write = head?.data
                            println("q$currentState: $read -> $write, $right")
                        }
                        //Move one position to the right
                        head = head?.next
                    }
                    Helper.refund()
                }

                rejectState -> {
                    //Stop machine
                    println("qr: Halt")
                }

                acceptState -> {
                    //Stop machine
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