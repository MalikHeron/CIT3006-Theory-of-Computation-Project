package models.turing

import models.inventory.Inventory
import models.turing.Turing.Companion.head
import util.Node

class State {
    companion object {
        private var initialState: Int = 1
        private var acceptState: Int = 40
        private var rejectState: Int = 39
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
        private var none = 'N'
        private var alphaCounter = 0
        private var betaCounter = 0

        fun getState(currentState: Int, input: Char?, currentSymbol: Node?): Node? {
            when (currentState) {
                initialState -> {
                    println("q${currentState}: Turing Machine started...")
                    head = head?.next
                    while (head != null) {
                        //Check for invalid input
                        if (!machine.tapeAlphabet.contains(head?.data)) {
                            println("Invalid input detected!")
                            getNextState(rejectState)
                            return null
                        }
                        if (head?.next == null) {
                            read = head?.data
                            write = head?.data
                            println("q$currentState: $read -> $write, $left")
                            break
                        }
                        read = head?.data
                        write = head?.data
                        println("q$currentState: $read -> $write, $right")
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
                            println("q$currentState: $read -> $write, $none")
                            //Write 'B' on head position
                            currentSymbol?.data = machine.beta
                            head = currentSymbol
                            getNextState(9, currentSymbol?.data, currentSymbol)
                        }

                        'γ' -> {
                            write = machine.delta
                            println("q$currentState: $read -> $write, $none")
                            //Write 'Δ' on head position
                            currentSymbol?.data = machine.delta
                            head = currentSymbol
                            getNextState(12, currentSymbol?.data, currentSymbol)
                        }

                        machine.blankSymbol -> {
                            //Move one position to the right
                            write = machine.blankSymbol
                            println("q$currentState: $read -> $write, $none")
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

                    result = if (found) {
                        read = nextSymbol?.data
                        write = machine.crossSymbol
                        println("q$currentState: $read -> $write, $left")
                        //Write 'x' on head position
                        nextSymbol?.data = machine.crossSymbol
                        if (Inventory.getItemStock(read as Char) > 0) {
                            //Give the item and check it off
                            Transitions.giveItem(read as Char, nextSymbol)
                            head
                        } else {
                            //Revert symbols
                            Transitions.revertComboSymbol(nextSymbol)
                            null
                        }
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
                            nextSymbol = nextSymbol.prev
                            found = false
                            break
                        }
                        if (nextSymbol?.next?.data == machine.blankSymbol) {
                            read = nextSymbol.next?.data
                            write = nextSymbol.next?.data
                            println("q$currentState: $read -> $write, $left")
                        } else {
                            read = nextSymbol?.data
                            write = nextSymbol?.data
                            println("q$currentState: $read -> $write, $right")
                        }
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (found) {
                        read = nextSymbol?.data
                        write = machine.crossSymbol
                        println("q$currentState: $read -> $write, $left")
                        //Write 'x' on head position
                        nextSymbol?.data = machine.crossSymbol
                        if (Inventory.getItemStock(read as Char) > 0) {
                            //Give the item and check it off
                            Transitions.giveItem(read as Char, nextSymbol)
                            head
                        } else {
                            //Revert symbols
                            Transitions.revertComboSymbol(nextSymbol)
                            null
                        }
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
                            current = current.prev
                            found = false
                            break
                        }
                        if (current?.data == 'F') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            if (Inventory.getItemStock(read as Char) > 0) {
                                println("Dispense '$read'")
                                Inventory.setItemStock(read as Char, Inventory.getItemStock(read as Char) - 1)
                                getNextState(7, 'ɑ', head)
                            }
                            alternate = true
                            break
                        } else if (current?.data == 'N') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            if (Inventory.getItemStock(read as Char) > 0) {
                                println("Dispense '$read'")
                                Inventory.setItemStock(read as Char, Inventory.getItemStock(read as Char) - 1)
                                getNextState(8, 'β', head)
                            }
                            alternate = true
                            break
                        }
                        if (current?.next?.data == machine.blankSymbol) {
                            read = current.next?.data
                            write = current.next?.data
                            println("q$currentState: $read -> $write, $left")
                        } else {
                            read = current?.data
                            write = current?.data
                            println("q$currentState: $read -> $write, $right")
                        }
                        //Move one position to the right
                        current = current?.next
                    }

                    result = if (alternate) {
                        read = head?.data
                        write = head?.data
                        println("q$currentState: $read -> $write, $none")
                        // Go to tape front
                        Transitions.goToFront()
                        null
                    } else if (found) {
                        read = current?.data
                        write = machine.crossSymbol
                        println("q$currentState: $read -> $write, $left")
                        //Write 'x' on head position
                        current?.data = machine.crossSymbol
                        if (Inventory.getItemStock(read as Char) > 0) {
                            //Give the item and check it off
                            Transitions.giveItem(read as Char, current)
                            head
                        } else {
                            //Revert symbols
                            Transitions.revertComboSymbol(current)
                            null
                        }
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
                            nextSymbol = nextSymbol.prev
                            found = false
                            break
                        }
                        if (nextSymbol?.next?.data == machine.blankSymbol) {
                            read = nextSymbol.next?.data
                            write = nextSymbol.next?.data
                            println("q$currentState: $read -> $write, $left")
                        } else {
                            read = nextSymbol?.data
                            write = nextSymbol?.data
                            println("q$currentState: $read -> $write, $right")
                        }
                        //Move one position to the right
                        nextSymbol = nextSymbol?.next
                    }

                    result = if (found) {
                        read = nextSymbol?.data
                        write = machine.crossSymbol
                        println("q$currentState: $read -> $write, $left")
                        //Write 'x' on head position
                        nextSymbol?.data = machine.crossSymbol
                        if (Inventory.getItemStock(read as Char) > 0) {
                            //Give the item and check it off
                            Transitions.giveItem(read as Char, nextSymbol)
                            head
                        } else {
                            //Revert symbols
                            Transitions.revertComboSymbol(nextSymbol)
                            null
                        }
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(nextSymbol)
                        null
                    }
                }

                11 -> {
                    var found = true
                    var current = currentSymbol?.next
                    var alternate = false

                    while (current?.data != 'S') {
                        if (current?.data == machine.blankSymbol) {
                            current = current.prev
                            found = false
                            break
                        }
                        if (current?.data == 'F') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            if (Inventory.getItemStock(read as Char) > 0) {
                                println("Dispense '$read'")
                                Inventory.setItemStock(read as Char, Inventory.getItemStock(read as Char) - 1)
                                getNextState(7, 'ɑ', head)
                            }
                            alternate = true
                            break
                        } else if (current?.data == 'N') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            if (Inventory.getItemStock(read as Char) > 0) {
                                println("Dispense '$read'")
                                Inventory.setItemStock(read as Char, Inventory.getItemStock(read as Char) - 1)
                                getNextState(8, 'β', head)
                            }
                            alternate = true
                            break
                        }
                        if (current?.next?.data == machine.blankSymbol) {
                            read = current.next?.data
                            write = current.next?.data
                            println("q$currentState: $read -> $write, $left")
                        } else {
                            read = current?.data
                            write = current?.data
                            println("q$currentState: $read -> $write, $right")
                        }
                        //Move one position to the right
                        current = current?.next
                    }

                    result = if (alternate) {
                        // Go to tape front
                        Transitions.goToFront()
                        null
                    } else if (found) {
                        read = current?.data
                        write = machine.crossSymbol
                        println("q$currentState: $read -> $write, $left")
                        //Write 'x' on head position
                        current?.data = machine.crossSymbol
                        if (Inventory.getItemStock(read as Char) > 0) {
                            //Give the item and check it off
                            Transitions.giveItemAlt(read as Char, current)
                            null
                        } else {
                            //Revert symbols
                            Transitions.revertComboSymbol(current)
                            null
                        }
                    } else {
                        //Revert symbols
                        Transitions.revertComboSymbol(current)
                        null
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
                            current = current.prev
                            found = false
                            break
                        }
                        if (current?.data == 'F') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            if (Inventory.getItemStock(read as Char) > 0) {
                                println("Dispense '$read'")
                                Inventory.setItemStock(read as Char, Inventory.getItemStock(read as Char) - 1)
                                getNextState(7, 'ɑ', head)
                            }
                            alternate = true
                            break
                        } else if (current?.data == 'N') {
                            read = current.data
                            write = machine.crossSymbol
                            println("q$currentState: $read -> $write, $left")
                            //Write 'x' on head position
                            current.data = machine.crossSymbol
                            head = current
                            if (Inventory.getItemStock(read as Char) > 0) {
                                println("Dispense '$read'")
                                Inventory.setItemStock(read as Char, Inventory.getItemStock(read as Char) - 1)
                                getNextState(8, 'β', head)
                            }
                            alternate = true
                            break
                        }
                        if (current?.next?.data == machine.blankSymbol) {
                            read = current.next?.data
                            write = current.next?.data
                            println("q$currentState: $read -> $write, $left")
                        } else {
                            read = current?.data
                            write = current?.data
                            println("q$currentState: $read -> $write, $right")
                        }
                        //Move one position to the right
                        current = current?.next
                    }

                    result = if (alternate) {
                        // Go to tape front
                        Transitions.goToFront()
                        null
                    } else if (found) {
                        read = current?.data
                        write = machine.crossSymbol
                        println("q$currentState: $read -> $write, $left")
                        //Write 'x' on head position
                        current?.data = machine.crossSymbol
                        if (Inventory.getItemStock(read as Char) > 0) {
                            //Give the item and check it off
                            Transitions.giveItem(read as Char, current)
                            head
                        } else {
                            //Write back the symbols
                            Transitions.revertComboSymbol(current)
                            null
                        }
                    } else {
                        //Write back the symbols
                        Transitions.revertComboSymbol(current)
                        null
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
                    getNextState(28)
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
                                head = current
                                return null
                            }
                            if (read == machine.mu) {
                                write = replaceList[1]
                                println("q$currentState: $read -> $write, $right")
                                //Write 'β' on head position
                                current.data = replaceList[1]
                                head = current
                                return null
                            }
                            if (read == machine.omega) {
                                write = replaceList[2]
                                println("q$currentState: $read -> $write, $right")
                                //Write 'γ' on head position
                                current.data = replaceList[2]
                                head = current
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
                    println("Tape: ${Turing.tape.getData()}\n")
                }

                17 -> {
                    var current = currentSymbol?.next

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
                        if (current.next == null) {
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $left")
                            //Move one position to the right
                            head = current.prev
                        } else {
                            read = current.data
                            write = current.data
                            println("q$currentState: $read -> $write, $right")
                        }
                        //Move one position to the right
                        current = current.next
                    }
                    println("Tape: ${Turing.tape.getData()}\n")
                    getNextState(18)
                }

                18 -> {
                    var current = head

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
                    while (head != null) {
                        when (head?.data) {
                            'ɑ' -> {
                                alphaCounter++
                                read = head?.data
                                write = machine.alpha
                                println("q$currentState: $read -> $write, $none")
                                //Write 'A' on head position
                                head?.data = machine.alpha
                            }

                            'β' -> {
                                betaCounter++
                                read = head?.data
                                write = machine.beta
                                println("q$currentState: $read -> $write, $none")
                                //Write 'B' on head position
                                head?.data = machine.beta
                            }
                        }
                        if ((alphaCounter == 1 && betaCounter == 2)
                            || (alphaCounter == 3 && betaCounter == 1) || alphaCounter == 5
                        ) {
                            getNextState(20)
                            return null
                        } else if (alphaCounter == 4 || (alphaCounter == 2 && betaCounter == 1)) {
                            getNextState(21)
                            return null
                        } else if (alphaCounter == 3 && betaCounter == 0) {
                            getNextState(22)
                            return null
                        }
                        if (head?.next != null) {
                            read = head?.data
                            write = head?.data
                            println("q$currentState: $read -> $write, $right")
                            //Move one position to the right
                            head = head!!.next
                        } else {
                            read = head?.data
                            write = head?.data
                            println("q$currentState: $read -> $write, $left")
                            break
                        }
                    }
                    println("Tape: ${Turing.tape.getData()}\n")
                    getNextState(23)
                }

                20 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Fork is a requested item
                    while (nextSymbol?.data != 'K' && nextSymbol != null) {
                        //Check if end of tape is reached
                        if (nextSymbol.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = nextSymbol.data
                        write = nextSymbol.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        nextSymbol = nextSymbol.next
                    }

                    if (found) {
                        alphaCounter = 0
                        betaCounter = 0
                        read = nextSymbol?.data
                        write = machine.crossSymbol
                        println("q$currentState: $read -> $write, $left")
                        //Write 'x' on head position
                        nextSymbol?.data = machine.crossSymbol
                        if (Inventory.getItemStock(read as Char) > 0) {
                            //Give the item and check it off
                            Transitions.giveItemEnd(read as Char, nextSymbol)
                        }
                    } else {
                        head = nextSymbol
                        getNextState(30)
                    }
                }

                21 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Fork is a requested item
                    while (nextSymbol?.data != 'S' && nextSymbol != null) {
                        //Check if end of tape is reached
                        if (nextSymbol.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = nextSymbol.data
                        write = nextSymbol.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        nextSymbol = nextSymbol.next
                    }

                    if (found) {
                        alphaCounter = 0
                        betaCounter = 0
                        read = nextSymbol?.data
                        write = machine.crossSymbol
                        println("q$currentState: $read -> $write, $left")
                        //Write 'x' on head position
                        nextSymbol?.data = machine.crossSymbol
                        if (Inventory.getItemStock(read as Char) > 0) {
                            //Give the item and check it off
                            Transitions.giveItemEnd(read as Char, nextSymbol)
                        }
                    } else {
                        head = nextSymbol
                        getNextState(30)
                    }
                }

                22 -> {
                    var found = true
                    var nextSymbol = currentSymbol?.next
                    //Check if Fork is a requested item
                    while (nextSymbol?.data != 'F' && nextSymbol != null) {
                        //Check if end of tape is reached
                        if (nextSymbol.data == machine.blankSymbol) {
                            found = false
                            break
                        }
                        read = nextSymbol.data
                        write = nextSymbol.data
                        println("q$currentState: $read -> $write, $right")
                        //Move one position to the right
                        nextSymbol = nextSymbol.next
                    }

                    if (found) {
                        alphaCounter = 0
                        betaCounter = 0
                        read = nextSymbol?.data
                        write = machine.crossSymbol
                        println("q$currentState: $read -> $write, $left")
                        //Write 'x' on head position
                        nextSymbol?.data = machine.crossSymbol
                        if (Inventory.getItemStock(read as Char) > 0) {
                            //Give the item and check it off
                            Transitions.giveItemEnd(read as Char, nextSymbol)
                        }
                    } else {
                        head = nextSymbol
                        getNextState(30)
                    }
                }

                23 -> {
                    val current = head
                    if (current != null) {
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
                            if (machine.inputAlphabet.contains(head?.data) || lowercaseSymbolList.contains(head?.data)
                                || uppercaseSymbolList.contains(head?.data)
                            )
                                result = getNextState(rejectState)
                        }
                        head = head?.next
                        println("Tape: ${Turing.tape.getData()}\n")
                    }
                    result = getNextState(acceptState)
                }

                24 -> {
                    val current = head?.prev
                    if (current != null) {
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
                    getNextState(25, head?.data, head)
                }

                25 -> {
                    read = input
                    write = currentSymbol?.data
                    if (read == 'N') {
                        println("q$currentState: $read -> $write, $right")
                        getNextState(31)
                    } else {
                        println("q$currentState: $read -> $write, $left")
                        getNextState(26)
                    }
                }

                26 -> {
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
                    getNextState(28)
                }

                27 -> {
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

                28 -> {
                    //Loop while not at the end of the tape
                    while (head != null) {
                        if (head?.data == 'ɑ' || head?.data == 'β' || head?.data == 'γ'
                            || head?.data == machine.blankSymbol
                        ) {
                            read = head?.prev?.data
                            write = head?.prev?.data
                            println("q$currentState: $read -> $write, $right")
                            if (getNextState(2, head?.data, head) != null) {
                                return null
                            }
                        }
                        if (head?.next == null) {
                            read = head?.data
                            write = head?.data
                            println("q$currentState: $read -> $write, $right")
                        }
                        //Move one position to the right
                        head = head?.next
                    }
                }

                29 -> {
                    var current: Node?
                    current = currentSymbol?.prev

                    while (current != null) {
                        val data = current.data
                        if (uppercaseSymbolList.contains(data)) {
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
                    getNextState(19)
                }

                30 -> {
                    if (head?.prev != null) {
                        while (head?.data != machine.alpha && head?.data != machine.beta) {
                            read = head?.data
                            write = head?.data
                            if (head?.prev == null) {
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                println("q$currentState: $read -> $write, $left")
                            }
                            //Move one position to the left
                            head = head!!.prev
                        }
                        head = head?.next
                        println("Tape: ${Turing.tape.getData()}\n")
                    }
                    getNextState(19)
                }

                31 -> {
                    head = currentSymbol?.next
                    read = head?.data
                    write = head?.data
                    if (read == 'K') {
                        println("q$currentState: $read -> $write, $right")
                        getNextState(32)
                    } else {
                        println("q$currentState: $read -> $write, $left")
                        getNextState(26)
                    }
                }

                32 -> {
                    head = currentSymbol?.next
                    read = head?.data
                    write = head?.data
                    if (read == 'S') {
                        println("q$currentState: $read -> $write, $right")
                        getNextState(33)
                    } else {
                        println("q$currentState: $read -> $write, $left")
                        getNextState(26)
                    }
                }

                33 -> {
                    head = currentSymbol?.next
                    read = head?.data
                    write = head?.data
                    if (read == 'F') {
                        println("q$currentState: $read -> $write, $right")
                        getNextState(34)
                    } else {
                        println("q$currentState: $read -> $write, $left")
                        getNextState(26)
                    }
                }

                34 -> {
                    head = currentSymbol?.next
                    read = head?.data
                    write = head?.data
                    if (read == 'F') {
                        println("q$currentState: $read -> $write, $right")
                        getNextState(35)
                    } else {
                        println("q$currentState: $read -> $write, $left")
                        getNextState(26)
                    }
                }

                35 -> {
                    head = currentSymbol?.next
                    read = head?.data
                    write = head?.data
                    if (read == 'S') {
                        println("q$currentState: $read -> $write, $right")
                        getNextState(36)
                    } else {
                        println("q$currentState: $read -> $write, $left")
                        getNextState(26)
                    }
                }

                36 -> {
                    head = currentSymbol?.next
                    read = head?.data
                    write = head?.data
                    if (read == 'K') {
                        println("q$currentState: $read -> $write, $right")
                        getNextState(37)
                    } else {
                        println("q$currentState: $read -> $write, $left")
                        getNextState(26)
                    }
                }

                37 -> {
                    head = currentSymbol?.next
                    read = head?.data
                    write = head?.data
                    if (read == 'S') {
                        println("q$currentState: $read -> $write, $right")
                        getNextState(38)
                    } else {
                        println("q$currentState: $read -> $write, $left")
                        getNextState(26)
                    }
                }

                38 -> {
                    head = currentSymbol?.next
                    read = head?.data
                    write = head?.data
                    if (read == machine.blankSymbol) {
                        println("q$currentState: $read -> $write, $right")
                        println("Funds: $${Inventory.getFunds()}")
                        Inventory.restockInventory()
                        Inventory.resetFunds()
                        getNextState(acceptState)
                        getNextState(acceptState)
                    } else {
                        println("q$currentState: $read -> $write, $left")
                        getNextState(26)
                    }
                }

                rejectState -> {
                    //Stop machine
                    println("qr: Halt")
                    return head
                }

                acceptState -> {
                    //Stop machine
                    println("qa: Halt")
                    return head
                }
            }
            return result
        }

        private fun getNextState(nextState: Int): Node? {
            return getState(nextState, null, head)
        }

        private fun getNextState(nextState: Int, read: Char?, currentSymbol: Node?): Node? {
            return getState(nextState, read, currentSymbol)
        }
    }
}