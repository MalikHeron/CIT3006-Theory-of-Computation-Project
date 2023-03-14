package models.turing

import util.DoublyLinkedList
import util.Node

class Transition(input: String) {

    private var read: Any? = null
    private var write: Any? = null
    private var right = 'R'
    private var left = 'L'
    private var current: Node? = null
    private var tape: DoublyLinkedList = DoublyLinkedList()
    private var machine = Machine()

    init {
        //Verify symbols from input
        verifySymbols(input)
    }

    private fun verifySymbols(input: String) {
        //Iterate through the symbols in the input
        input.forEach {
            //Check for invalid input
            if (!machine.tapeAlphabet.contains(it)) {
                println("Invalid input detected")
                refund()
                //End transition/Halt
                println("Halt")
                return
            }
            //Add the characters as symbols to the tape
            tape.addNewNode(it)
        }
        println("Tape: ${tape.getData()}\n")
        //Begin the transition
        doTransitions()
    }

    private fun doTransitions() {
        //Get the head of the tape
        current = tape.getCurrent()
        //Loop while not at the end of the tape
        while (read != machine.blankSymbol) {
            //read symbol at head
            read = current?.data!!
            write = current?.data
            println("$read -> $write, $right")
            when (read) {
                'ɑ' -> {
                    //Check combinations for 'ɑ'
                    alphaTransitions(current)
                }

                'β' -> {
                    //Check combinations for 'β'
                    betaTransitions(current)
                }

                'γ' -> {
                    //Check combinations for 'γ'
                    gammaTransitions(current)
                }

                machine.blankSymbol -> {
                    //Check if only alphabet symbols remain
                    checkInput()
                    refund()
                    //End transition/Halt
                    println("Halt")
                    println("Tape: ${tape.getData()}\n")
                    return
                }
            }
            //Move one position to the right
            current = current?.next
        }
    }

    private fun checkInput() {
        tape.getData().forEach {
            if (machine.inputAlphabet.contains(it)) {
                if (it == 'F' || it == 'K' || it == 'N' || it == 'S')
                    println("Insufficient funds")
                    return
            }
        }
    }

    private fun refund() {
        var total = 0
        tape.getData().forEach {
            if (it == 'ɑ')
                total += 5
            if (it == 'β')
                total += 10
            if (it == 'γ')
                total += 20
        }
        if (total != 0)
            println("Refund: $$total")
    }

    private fun alphaTransitions(currentSymbol: Node?) {
        var found: Boolean
        var nextSymbol: Node?
        var currSymbol: Node?

        //Check if already at the tape's front
        currSymbol = if (currentSymbol?.prev != null) {
            //Move to the leftmost position on the tap
            //goToFront(tape.getCurrent())
            //Get current symbol under head
            tape.getCurrent()
        } else {
            //Move one position to the right
            currentSymbol?.next
        }

        //println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

        //While not end of input
        while (currSymbol?.data != machine.blankSymbol) {
            //Get symbol to the right of head
            nextSymbol = currSymbol?.next
            //Store current symbol
            val prevSymbol = currSymbol?.data
            //println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

            if (currSymbol != currentSymbol) {
                found = true
                //Read value under head
                read = currSymbol?.data
                when (read) {
                    'ɑ' -> {
                        write = machine.crossSymbol
                        println("$read -> $write, $right")
                        //Write 'x' on current head position
                        currSymbol?.data = machine.crossSymbol
                        //println("Tape: ${tape.getData()}\n")

                        //Check if Napkin is a requested item
                        while (nextSymbol?.data != 'N') {
                            //Check if end of input is reached
                            if (nextSymbol?.data == machine.blankSymbol) {
                                found = false
                                break
                            }
                            val current = nextSymbol
                            //Move one position to the right
                            nextSymbol = nextSymbol?.next
                            //println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                            read = current?.data
                            write = current?.data
                            println("$read -> $write, $right")
                        }

                        if (found) {
                            //Give the item and check it off
                            giveItem(nextSymbol, currentSymbol)
                            return
                        } else {
                            //Write back the symbols
                            replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                        }
                    }

                    'β' -> {
                        write = machine.crossSymbol
                        println("$read -> $write, $right")
                        //Write 'x' on current head position
                        currSymbol?.data = machine.crossSymbol
                        //println("Tape: ${tape.getData()}\n")

                        //Check if Fork is a requested item
                        while (nextSymbol?.data != 'F') {
                            //Check if end of tape is reached
                            if (nextSymbol?.data == machine.blankSymbol) {
                                found = false
                                break
                            }
                            val current = nextSymbol
                            //Move one position to the right
                            nextSymbol = nextSymbol?.next
                            //println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                            read = current?.data
                            write = current?.data
                            println("$read -> $write, $right")
                        }

                        if (found) {
                            giveItem(nextSymbol, currentSymbol)
                            return
                        } else {
                            replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                        }
                    }

                    'γ' -> {
                        write = machine.crossSymbol
                        println("$read -> $write, $right")
                        //Write x on current head position
                        currSymbol?.data = machine.crossSymbol
                        //println("Tape: ${tape.getData()}\n")

                        //Check if Knife is a requested item
                        while (nextSymbol?.data != 'K') {
                            //Check if end of tape is reached
                            if (nextSymbol?.data == machine.blankSymbol) {
                                found = false
                                break
                            }
                            val current = nextSymbol
                            nextSymbol = nextSymbol?.next
                            //println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                            read = current?.data
                            write = current?.data
                            println("$read -> $write, $right")
                        }

                        if (found) {
                            giveItem(nextSymbol, currentSymbol)
                            return
                        } else {
                            replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                        }
                    }

                    else -> {
                        //Check if only alphabet symbols remain
                        if (machine.inputAlphabet.contains(read)) {
                            return
                        }
                    }
                }
            }
            currSymbol = currSymbol?.next
        }
    }

    private fun betaTransitions(currentSymbol: Node?) {
        var found = true
        var tapeFront = tape.getCurrent()
        //Get the next node
        var nextSymbol = currentSymbol?.next

        if (currentSymbol?.prev != null) {
            //goToFront(tape.getCurrent())
            tapeFront = tape.getCurrent()
            //Get the next node
            nextSymbol = tapeFront?.next
        }

        //println("Current: ${currentSymbol?.data}, Next: ${nextSymbol?.data}")
        var prevSymbol = currentSymbol?.data
        //Write crossSymbol aka 'x'
        currentSymbol?.data = machine.crossSymbol
        println("Tape: ${tape.getData()}\n")

        //Check if Napkin is a requested item
        while (nextSymbol?.data != 'N') {
            //Check if end of tape is reached
            if (nextSymbol?.data == machine.blankSymbol) {
                found = false
                break
            }
            val current = nextSymbol
            nextSymbol = nextSymbol?.next
            //println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
            read = current?.data
            write = current?.data
            println("$read -> $write, $right")
        }

        if (found) {
            giveItem(nextSymbol, null)
        } else {
            replaceSymbol(nextSymbol, prevSymbol, currentSymbol)
            var currSymbol = tapeFront

            while (currSymbol?.data != machine.blankSymbol) {
                //Get the next node
                nextSymbol = currSymbol?.next
                //Store current node symbol
                prevSymbol = currSymbol?.data
                //println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

                if (currSymbol != currentSymbol) {
                    found = true
                    read = currSymbol?.data
                    when (read) {
                        'ɑ' -> {
                            write = machine.crossSymbol
                            println("$read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            //println("Tape: ${tape.getData()}\n")

                            while (nextSymbol?.data != 'F') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                val current = nextSymbol
                                nextSymbol = nextSymbol?.next
                                //println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                                read = current?.data
                                write = current?.data
                                println("$read -> $write, $right")
                            }

                            if (found) {
                                giveItem(nextSymbol, currentSymbol)
                                return
                            } else {
                                replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            }
                        }

                        'β' -> {
                            write = machine.crossSymbol
                            println("$read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            //println("Tape: ${tape.getData()}\n")

                            while (nextSymbol?.data != 'S') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                val current = nextSymbol
                                nextSymbol = nextSymbol?.next
                                //println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                                read = current?.data
                                write = current?.data
                                println("$read -> $write, $right")
                            }

                            if (found) {
                                giveItem(nextSymbol, currentSymbol)
                                return
                            } else {
                                replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            }
                        }

                        'γ' -> {
                            write = machine.crossSymbol
                            println("$read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            //println("Tape: ${tape.getData()}\n")

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
                                val current = nextSymbol
                                nextSymbol = nextSymbol?.next
                                //println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                                read = current?.data
                                write = current?.data
                                println("$read -> $write, $right")
                            }

                            if (found) {
                                symbolArray.forEach {
                                    giveItem(it, currentSymbol)
                                }
                                return
                            } else {
                                replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            }
                        }

                        else -> {
                            //Check if only alphabet symbols remain
                            if (machine.inputAlphabet.contains(read)) {
                                return
                            }
                        }
                    }
                }
                currSymbol = currSymbol?.next
            }
        }
    }

    private fun gammaTransitions(currentSymbol: Node?) {
        var found = true
        var tapeFront = tape.getCurrent()
        //Get the next node
        var nextSymbol = currentSymbol?.next

        if (currentSymbol?.prev != null) {
            //goToFront(tape.getCurrent())
            tapeFront = tape.getCurrent()
            //Get the next node
            nextSymbol = tapeFront?.next
        }

        //println("Current: ${currentSymbol?.data}, Next: ${nextSymbol?.data}")
        var prevSymbol = currentSymbol?.data
        //println("Tape: ${tape.getData()}\n")

        //Check if Napkin is a requested item
        while (nextSymbol?.data != 'S') {
            //Check if end of tape is reached
            if (nextSymbol?.data == machine.blankSymbol) {
                found = false
                break
            }
            if (nextSymbol?.data == 'F') {
                //println("Write: 'ɑ' on '${currentSymbol?.data}'")
                read = currentSymbol?.data
                write = 'ɑ'
                println("$read -> $write, $right")
                //Write crossSymbol aka 'x'
                currentSymbol?.data = 'ɑ'
                giveItem(nextSymbol, null)
                read = currentSymbol?.data
                write = 'x'
                println("$read -> $write, $right")
                alphaTransitions(currentSymbol)
                return
            }
            if (nextSymbol?.data == 'N') {
                //println("Write: 'β' on '${currentSymbol?.data}'")
                read = currentSymbol?.data
                write = 'β'
                println("$read -> $write, $right")
                //Write crossSymbol aka 'x'
                currentSymbol?.data = 'β'
                giveItem(nextSymbol, null)
                read = currentSymbol?.data
                write = 'x'
                println("$read -> $write, $right")
                betaTransitions(currentSymbol)
                return
            }
            val current = nextSymbol
            nextSymbol = nextSymbol?.next
            //println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
            read = current?.data
            write = current?.data
            println("$read -> $write, $right")
        }

        //Write crossSymbol aka 'x'
        currentSymbol?.data = machine.crossSymbol

        if (found) {
            giveItem(nextSymbol, null)
        } else {
            replaceSymbol(nextSymbol, prevSymbol, currentSymbol)
            var currSymbol = tapeFront

            while (currSymbol?.data != machine.blankSymbol) {
                //Get the next node
                nextSymbol = currSymbol?.next
                //Store current node symbol
                prevSymbol = currSymbol?.data
                //println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

                if (currSymbol != currentSymbol) {
                    found = true
                    read = currSymbol?.data
                    when (read) {
                        'ɑ' -> {
                            write = machine.crossSymbol
                            println("$read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            //println("Tape: ${tape.getData()}\n")

                            while (nextSymbol?.data != 'K') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                val current = nextSymbol
                                nextSymbol = nextSymbol?.next
                                //println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                                read = current?.data
                                write = current?.data
                                println("$read -> $write, $right")
                            }

                            if (found) {
                                giveItem(nextSymbol, currentSymbol)
                                return
                            } else {
                                replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            }
                        }

                        'β' -> {
                            write = machine.crossSymbol
                            println("$read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            //println("Tape: ${tape.getData()}\n")

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
                                val current = nextSymbol
                                nextSymbol = nextSymbol?.next
                                //println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                                read = current?.data
                                write = current?.data
                                println("$read -> $write, $right")
                            }

                            if (found) {
                                symbolArray.forEach {
                                    println("Element: $it")
                                    giveItem(it, currentSymbol)
                                }
                                return
                            } else {
                                replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            }
                        }

                        'γ' -> {
                            write = machine.crossSymbol
                            println("$read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            //println("Tape: ${tape.getData()}\n")

                            while (nextSymbol?.data != 'F') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                val current = nextSymbol
                                nextSymbol = nextSymbol?.next
                                //println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                                read = current?.data
                                write = current?.data
                                println("$read -> $write, $right")
                            }

                            if (found) {
                                giveItem(nextSymbol, currentSymbol)
                                return
                            } else {
                                replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            }
                        }

                        else -> {
                            //Check if only alphabet symbols remain
                            if (machine.inputAlphabet.contains(read)) {
                                return
                            }
                        }
                    }
                }
                currSymbol = currSymbol?.next
            }
        }
    }

    private fun giveItem(currentSymbol: Node?, targetSymbol: Node?) {
        var found = true
        //println("Write: ${machine.crossSymbol} on '${currentSymbol?.data}'")
        read = currentSymbol?.data
        write = machine.crossSymbol
        println("$read -> $write, $left")

        if (targetSymbol != null) {
            var previousSymbol = currentSymbol?.prev
            //println("Current: ${currentSymbol?.data}, Previous: ${previousSymbol?.data}, Target: ${targetSymbol.data}")

            while (previousSymbol != targetSymbol) {
                if (previousSymbol == null) {
                    found = false
                    break
                }
                val current = previousSymbol
                previousSymbol = previousSymbol.prev
                //println("Current: ${current.data}, Previous: ${previousSymbol?.data}")
                read = current.data
                write = current.data
                println("$read -> $write, $left")
            }

            if (found) {
                //println("Write: ${machine.crossSymbol} on '${targetSymbol.data}'")
                read = targetSymbol.data
                write = machine.crossSymbol
                println("$read -> $write, $left")
                previousSymbol?.data = machine.crossSymbol
            }
        }
        println("Dispense '${currentSymbol?.data}'")
        //Check off item
        currentSymbol?.data = machine.crossSymbol
        println("Tape: ${tape.getData()}\n")
    }

    private fun replaceSymbol(currentSymbol: Node?, substituteSymbol: Char?, targetNode: Node?) {
        var found = true
        var previousSymbol = currentSymbol?.prev
        //println("Current: ${currentSymbol?.data}, Substitute: $substituteSymbol, Target Node: ${targetNode?.data}\n")

        while (previousSymbol != targetNode) {
            if (previousSymbol == null) {
                found = false
                break
            }
            val current = previousSymbol
            previousSymbol = previousSymbol.prev
            //println("Current: ${current.data}, Previous: ${previousSymbol?.data}")
            read = current.data
            write = current.data
            println("$read -> $write, $left")
        }
        if (found) {
            read = targetNode?.data
            write = substituteSymbol
            println("$read -> $write, $right")
            targetNode?.data = substituteSymbol!!
        }
        println("Tape: ${tape.getData()}\n")
    }

    private fun goToFront(currentSymbol: Node?) {
        var previousSymbol = currentSymbol?.prev
        //println("Current: ${currentSymbol?.data}, Previous: ${previousSymbol?.data}")

        println("Moving to tape front")
        while (previousSymbol != null) {
            val current = previousSymbol
            previousSymbol = previousSymbol.prev
            //println("Current: ${current.data}, Previous: ${previousSymbol?.data}")
            read = current.data
            write = current.data
            println("$read -> $write, $left")
        }
        println("Front of tape reached")
        println("Tape: ${tape.getData()}\n")
    }
}