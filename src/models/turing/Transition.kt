package models.turing

import util.DoublyLinkedList
import util.Node

class Transition(private var input: String) {

    private var read: Any? = null
    private var current: Node? = null
    private var inputList: DoublyLinkedList = DoublyLinkedList()
    private var machine = Machine()

    init {
        //Get individual symbols from string
        getSymbols(input)
        //Begin the transition
        doTransitions()
    }

    private fun getSymbols(input: String) {
        //Iterate through the characters of the string
        input.forEach {
            //Add the characters as Nodes to the LinkedList
            inputList.addNewNode(it)
        }
        println(inputList.showData())
    }

    private fun doTransitions() {
        //Get the head of the LinkedList
        current = inputList.getCurrent()
        //Loop while not at the end of the LinkedList
        while (read != '$') {
            //read data at head
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

                '$' -> {
                    //End transition
                    return
                }

                else -> {
                    //Check if only alphabet symbols remain
                    if (machine.language.contains(read)) {
                        println("Insufficient funds")
                        //End transition
                        return
                    } else if (!machine.alphabet.contains(read)) {
                        println("Invalid input detected")
                        //End transition
                        return
                    }
                }
            }
            //Move to next position on tape
            current = current?.next
        }
    }

    private fun alphaTransitions(currentSymbol: Node?) {
        var found: Boolean
        //For storing the value at the front of the tape
        val tapeFront: Node?
        var nextSymbol: Node?
        var currSymbol: Node?

        //Check if already at the tape's front
        if (currentSymbol?.prev != null) {
            goToFront(inputList.getCurrent())
            //Set tape front
            tapeFront = inputList.getCurrent()
            //Set current node
            currSymbol = tapeFront
            //Get the next node
            nextSymbol = tapeFront?.next
        } else {
            currSymbol = currentSymbol?.next
            nextSymbol = currSymbol?.next
        }

        println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

        while (currSymbol?.data != '$') {
            //Get the next node
            nextSymbol = currSymbol?.next
            //Store current node symbol
            val prevSymbol = currSymbol?.data
            println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

            if (currSymbol != currentSymbol) {
                found = true
                //Read value under head
                read = currSymbol?.data
                when (read) {
                    'ɑ' -> {
                        //Set current node symbol to blankSymbol aka '#'
                        currSymbol?.data = machine.blankSymbol
                        println(inputList.showData())

                        //Check if Napkin is a requested item
                        while (nextSymbol?.data != 'N') {
                            //Check if end of linkedList is reached
                            if (nextSymbol?.data == '$') {
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
                        //Set current node symbol to blankSymbol aka '#'
                        currSymbol?.data = machine.blankSymbol
                        println(inputList.showData())

                        //Check if Fork is a requested item
                        while (nextSymbol?.data != 'F') {
                            //Check if end of linkedList is reached
                            if (nextSymbol?.data == '$') {
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
                        //Set current node symbol to blankSymbol aka '#'
                        currSymbol?.data = machine.blankSymbol
                        println(inputList.showData())

                        //Check if Knife is a requested item
                        while (nextSymbol?.data != 'K') {
                            //Check if end of linkedList is reached
                            if (nextSymbol?.data == '$') {
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
                        if (machine.language.contains(read)) {
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
        var tapeFront = inputList.getCurrent()
        //Get the next node
        var nextSymbol = currentSymbol?.next

        if (currentSymbol?.prev != null) {
            goToFront(inputList.getCurrent())
            tapeFront = inputList.getCurrent()
            //Get the next node
            nextSymbol = tapeFront?.next
        }

        println("Current: ${currentSymbol?.data}, Next: ${nextSymbol?.data}")
        var prevSymbol = currentSymbol?.data
        //Set to blankSymbol aka '#'
        currentSymbol?.data = machine.blankSymbol
        println(inputList.showData())

        //Check if Napkin is a requested item
        while (nextSymbol?.data != 'N') {
            //Check if end of linkedList is reached
            if (nextSymbol?.data == '$') {
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

            while (currSymbol?.data != '$') {
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
                            //Set current node symbol to blankSymbol aka '#'
                            currSymbol?.data = machine.blankSymbol
                            println(inputList.showData())

                            while (nextSymbol?.data != 'F') {
                                if (nextSymbol?.data == '$') {
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
                            //Set current node symbol to blankSymbol aka '#'
                            currSymbol?.data = machine.blankSymbol
                            println(inputList.showData())

                            while (nextSymbol?.data != 'S') {
                                if (nextSymbol?.data == '$') {
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
                            //Set current node symbol to blankSymbol aka '#'
                            currSymbol?.data = machine.blankSymbol
                            println(inputList.showData())

                            var count = 0
                            val symbolArray = ArrayList<Node>()

                            while (count != 2) {
                                if (nextSymbol?.data == '$') {
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
                            if (machine.language.contains(read)) {
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
        var tapeFront = inputList.getCurrent()
        //Get the next node
        var nextSymbol = currentSymbol?.next

        if (currentSymbol?.prev != null) {
            goToFront(inputList.getCurrent())
            tapeFront = inputList.getCurrent()
            //Get the next node
            nextSymbol = tapeFront?.next
        }

        println("Current: ${currentSymbol?.data}, Next: ${nextSymbol?.data}")
        var prevSymbol = currentSymbol?.data
        //Set to blankSymbol aka '#'
        currentSymbol?.data = machine.blankSymbol
        println(inputList.showData())

        //Check if Napkin is a requested item
        while (nextSymbol?.data != 'S') {
            //Check if end of linkedList is reached
            if (nextSymbol?.data == '$') {
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

            while (currSymbol?.data != '$') {
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
                            //Set current node symbol to blankSymbol aka '#'
                            currSymbol?.data = machine.blankSymbol
                            println(inputList.showData())

                            while (nextSymbol?.data != 'K') {
                                if (nextSymbol?.data == '$') {
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
                            //Set current node symbol to blankSymbol aka '#'
                            currSymbol?.data = machine.blankSymbol
                            println(inputList.showData())

                            var count = 0
                            val symbolArray = ArrayList<Node>()

                            while (count != 2) {
                                if (nextSymbol?.data == '$') {
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
                                    giveItem(it, currSymbol)
                                }
                                return
                            } else {
                                replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            }
                        }

                        'γ' -> {
                            //Set current node symbol to blankSymbol aka '#'
                            currSymbol?.data = machine.blankSymbol
                            println(inputList.showData())

                            while (nextSymbol?.data != 'F') {
                                if (nextSymbol?.data == '$') {
                                    found = false
                                    break
                                }
                                val current = nextSymbol
                                nextSymbol = nextSymbol?.next
                                println("Current: ${current?.data}, Next: ${nextSymbol?.data}")
                            }

                            if (found) {
                                giveItem(nextSymbol, currSymbol)
                                return
                            } else {
                                replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            }
                        }

                        else -> {
                            //Check if only alphabet symbols remain
                            if (machine.language.contains(read)) {
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
        println("Write: ${machine.checkSymbol} on '${currentSymbol?.data}'")
        //Check off item
        currentSymbol?.data = machine.checkSymbol
        println(inputList.showData())

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
                println("Write: ${machine.blankSymbol} on '${targetSymbol.data}'")
                previousSymbol?.data = machine.blankSymbol
            }
        }
        println(inputList.showData())
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
        println(inputList.showData())
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
        println(inputList.showData())
    }
}