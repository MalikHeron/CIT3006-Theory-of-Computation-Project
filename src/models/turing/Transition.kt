package models.turing

import com.sun.org.apache.xpath.internal.operations.Bool
import util.DoublyLinkedList
import util.Node

class Transition(input: String) {

    private var read: Any? = null
    private var current: Node? = null
    private var tape: DoublyLinkedList = DoublyLinkedList()
    private var machine = Machine()

    init {
        //Get individual symbols from string
        val valid = getSymbols(input)
        if (valid)
        //Begin the transition
            doTransitions()
    }

    private fun getSymbols(input: String): Boolean {
        //Iterate through the characters of the string
        input.forEach {
            //Invalid input
            if (!machine.tapeAlphabet.contains(it)) {
                println("Invalid input detected")
                refund()
                //End transition/Halt
                println("Halt")
                return false
            }
            //Add the characters as symbols to the tape
            tape.addNewNode(it)
        }
        println(tape.showData())
        return true
    }

    private fun doTransitions() {
        //Get the head of the tape
        current = tape.getCurrent()
        //Loop while not at the end of the tape
        while (read != machine.blankSymbol) {
            //read symbol at head
            read = current?.data!!
            println("Read: $read")
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
                    refund()
                    //End transition/Halt
                    println("Halt")
                    return
                }

                else -> {
                    //Check if only alphabet symbols remain
                    if (machine.inputAlphabet.contains(read)) {
                        println("Insufficient funds")
                        refund()
                        //End transition/Halt
                        println("Halt")
                        return
                    }
                }
            }
            //Move to next position on tape
            current = current?.next
        }
    }

    private fun refund() {
        var total: Int = 0
        tape.getData()?.forEach {
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
        //For storing the value at the front of the tape
        val tapeFront: Node?
        var nextSymbol: Node?
        var currSymbol: Node?

        //Check if already at the tape's front
        if (currentSymbol?.prev != null) {
            goToFront(tape.getCurrent())
            //Set tape front
            tapeFront = tape.getCurrent()
            //Set current node
            currSymbol = tapeFront
            //Get the next node
            nextSymbol = tapeFront?.next
        } else {
            //Set current node
            currSymbol = currentSymbol?.next
            //Get the next node
            nextSymbol = currSymbol?.next
        }

        println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

        while (currSymbol?.data != machine.blankSymbol) {
            //Get the next node
            nextSymbol = currSymbol?.next
            //Store current symbol
            val prevSymbol = currSymbol?.data
            println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

            if (currSymbol != currentSymbol) {
                found = true
                //Read value under head
                read = currSymbol?.data
                when (read) {
                    'ɑ' -> {
                        //Write 'x' on current head position
                        currSymbol?.data = machine.crossSymbol
                        println(tape.showData())

                        //Check if Napkin is a requested item
                        while (nextSymbol?.data != 'N') {
                            //Check if end of tape is reached
                            if (nextSymbol?.data == machine.blankSymbol) {
                                found = false
                                break
                            }
                            val current = nextSymbol
                            nextSymbol = nextSymbol?.next
                            println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
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
                        //Write 'x' on current head position
                        currSymbol?.data = machine.crossSymbol
                        println(tape.showData())

                        //Check if Fork is a requested item
                        while (nextSymbol?.data != 'F') {
                            //Check if end of tape is reached
                            if (nextSymbol?.data == machine.blankSymbol) {
                                found = false
                                break
                            }
                            val current = nextSymbol
                            nextSymbol = nextSymbol?.next
                            println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                        }

                        if (found) {
                            giveItem(nextSymbol, currentSymbol)
                            return
                        } else {
                            replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                        }
                    }

                    'γ' -> {
                        //Write x on current head position
                        currSymbol?.data = machine.crossSymbol
                        println(tape.showData())

                        //Check if Knife is a requested item
                        while (nextSymbol?.data != 'K') {
                            //Check if end of tape is reached
                            if (nextSymbol?.data == machine.blankSymbol) {
                                found = false
                                break
                            }
                            val current = nextSymbol
                            nextSymbol = nextSymbol?.next
                            println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
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
            goToFront(tape.getCurrent())
            tapeFront = tape.getCurrent()
            //Get the next node
            nextSymbol = tapeFront?.next
        }

        println("Current: ${currentSymbol?.data}, Next: ${nextSymbol?.data}")
        var prevSymbol = currentSymbol?.data
        //Set to blankSymbol aka '⊔'
        currentSymbol?.data = machine.crossSymbol
        println(tape.showData())

        //Check if Napkin is a requested item
        while (nextSymbol?.data != 'N') {
            //Check if end of tape is reached
            if (nextSymbol?.data == machine.blankSymbol) {
                found = false
                break
            }
            val current = nextSymbol
            nextSymbol = nextSymbol?.next
            println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
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
                println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

                if (currSymbol != currentSymbol) {
                    found = true
                    read = currSymbol?.data
                    when (read) {
                        'ɑ' -> {
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            println(tape.showData())

                            while (nextSymbol?.data != 'F') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                val current = nextSymbol
                                nextSymbol = nextSymbol?.next
                                println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                            }

                            if (found) {
                                giveItem(nextSymbol, currentSymbol)
                                return
                            } else {
                                replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            }
                        }

                        'β' -> {
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            println(tape.showData())

                            while (nextSymbol?.data != 'S') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                val current = nextSymbol
                                nextSymbol = nextSymbol?.next
                                println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                            }

                            if (found) {
                                giveItem(nextSymbol, currentSymbol)
                                return
                            } else {
                                replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            }
                        }

                        'γ' -> {
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            println(tape.showData())

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
                                println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
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
            goToFront(tape.getCurrent())
            tapeFront = tape.getCurrent()
            //Get the next node
            nextSymbol = tapeFront?.next
        }

        println("Current: ${currentSymbol?.data}, Next: ${nextSymbol?.data}")
        var prevSymbol = currentSymbol?.data
        println(tape.showData())

        //Check if Napkin is a requested item
        while (nextSymbol?.data != 'S') {
            //Check if end of tape is reached
            if (nextSymbol?.data == machine.blankSymbol) {
                found = false
                break
            }
            if (nextSymbol?.data == 'F') {
                println("Write: 'ɑ' on '${currentSymbol?.data}'")
                currentSymbol?.data = 'ɑ'
                giveItem(nextSymbol, null)
                alphaTransitions(currentSymbol)
                return
            }
            if (nextSymbol?.data == 'N') {
                println("Write: 'β' on '${currentSymbol?.data}'")
                //Set to blankSymbol aka '⊔'
                currentSymbol?.data = 'β'
                giveItem(nextSymbol, null)
                betaTransitions(currentSymbol)
                return
            }
            val current = nextSymbol
            nextSymbol = nextSymbol?.next
            println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
        }

        //Set to blankSymbol aka '⊔'
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
                println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

                if (currSymbol != currentSymbol) {
                    found = true
                    read = currSymbol?.data
                    when (read) {
                        'ɑ' -> {
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            println(tape.showData())

                            while (nextSymbol?.data != 'K') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                val current = nextSymbol
                                nextSymbol = nextSymbol?.next
                                println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                            }

                            if (found) {
                                giveItem(nextSymbol, currentSymbol)
                                return
                            } else {
                                replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            }
                        }

                        'β' -> {
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            println(tape.showData())

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
                                println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
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
                            //Write 'x' on current head position
                            currSymbol?.data = machine.crossSymbol
                            println(tape.showData())

                            while (nextSymbol?.data != 'F') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                val current = nextSymbol
                                nextSymbol = nextSymbol?.next
                                println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
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
        println("Write: ${machine.crossSymbol} on '${currentSymbol?.data}'")
        //Check off item
        currentSymbol?.data = machine.crossSymbol
        println(tape.showData())

        if (targetSymbol != null) {
            var previousSymbol = currentSymbol?.prev
            println("Current: ${currentSymbol?.data}, Previous: ${previousSymbol?.data}, Target: ${targetSymbol.data}")

            while (previousSymbol != targetSymbol) {
                if (previousSymbol == null) {
                    found = false
                    break
                }
                val current = previousSymbol
                previousSymbol = previousSymbol.prev
                println("Current: ${current.data}, Previous: ${previousSymbol?.data}")
            }

            if (found) {
                println("Write: ${machine.crossSymbol} on '${targetSymbol.data}'")
                previousSymbol?.data = machine.crossSymbol
            }
        }
        println(tape.showData())
    }

    private fun replaceSymbol(currentSymbol: Node?, substituteSymbol: Char?, targetNode: Node?) {
        var found = true
        var previousSymbol = currentSymbol?.prev
        println("Current: ${currentSymbol?.data}, Substitute: $substituteSymbol, Target Node: ${targetNode?.data}")

        while (previousSymbol != targetNode) {
            if (previousSymbol == null) {
                found = false
                break
            }
            val current = previousSymbol
            previousSymbol = previousSymbol.prev
            println("Current: ${current.data}, Previous: ${previousSymbol?.data}")
        }
        if (found) {
            targetNode?.data = substituteSymbol!!
        }
        println(tape.showData())
    }

    private fun goToFront(currentSymbol: Node?) {
        var previousSymbol = currentSymbol?.prev
        println("Current: ${currentSymbol?.data}, Previous: ${previousSymbol?.data}")

        while (previousSymbol != null) {
            val current = previousSymbol
            previousSymbol = previousSymbol.prev
            println("Current: ${current.data}, Previous: ${previousSymbol?.data}")
        }
        println("Front of tape reached")
        println(tape.showData())
    }
}