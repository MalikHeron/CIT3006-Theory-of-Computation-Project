package models.turing

import util.DoublyLinkedList
import util.Node
import kotlin.system.exitProcess

class Transition(private var input: String) {

    private var read: Any? = null
    private var blankSymbol = '#'
    private var currentState = ""
    private var alphabet = arrayOf('F', 'K', 'N', 'S')
    private var current: Node? = null
    private var inputList: DoublyLinkedList = DoublyLinkedList()

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
        var output = 0
        //Loop while not at the end of the LinkedList
        while (read != '$') {
            read = current?.data!!
            println("Read: $read")
            when (read) {
                'ɑ' -> {
                    //Check combinations for 'ɑ'
                    alphaTransitions(current?.next)
                }

                'β' -> {
                    //Check combinations for 'β'
                    output = betaTransitions(current)
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
                    if (alphabet.contains(read)) {
                        println("Insufficient funds")
                        //End transition
                        return
                    }
                }
            }
            current = current?.next
        }
    }

    private fun alphaTransitions(currentSymbol: Node?) {
        var found = true
        var currSymbol = currentSymbol
        goToFront(inputList.getCurrent())
        val next = inputList.getCurrent()
        read = currentSymbol?.data

        while (currSymbol?.data != '$') {
            //Get the next node
            var nextSymbol = next?.next
            //Store current node symbol
            val prevSymbol = currSymbol?.data
            println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

            read = currSymbol?.data
            when (read) {
                'ɑ' -> {
                    //Set current node symbol to blankSymbol aka '#'
                    currSymbol?.data = blankSymbol
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
                        giveItem(nextSymbol, currSymbol?.prev)
                        return
                    } else {
                        replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                        alphaTransitions(currSymbol?.next)
                    }
                }

                'β' -> {
                    //Set current node symbol to blankSymbol aka '#'
                    currSymbol?.data = blankSymbol
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
                        giveItem(nextSymbol, currentSymbol?.prev)
                        return
                    } else {
                        replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                        alphaTransitions(currSymbol?.next)
                    }
                }

                'γ' -> {
                    //Set current node symbol to blankSymbol aka '#'
                    currSymbol?.data = blankSymbol
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
                        giveItem(nextSymbol, currSymbol?.prev)
                        return
                    } else {
                        replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                        alphaTransitions(currentSymbol?.next)
                    }
                }

                '$' -> {
                    replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                    return
                }

                else -> {
                    //Check if only alphabet symbols remain
                    if (alphabet.contains(read)) {
                        return
                    }
                }
            }
            currSymbol = currSymbol?.next
        }
    }

    private fun betaTransitions(currentSymbol: Node?): Int {
        var found = true

        //Get the next node
        var nextSymbol = currentSymbol?.next
        println("Current: ${currentSymbol?.data}, Next: ${nextSymbol?.data}")
        var prevSymbol = currentSymbol?.data
        //Set to blankSymbol aka '#'
        currentSymbol?.data = blankSymbol
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
            found = true
            var currSymbol = currentSymbol?.next

            while (currSymbol?.data != '$') {
                //Get the next node
                nextSymbol = currSymbol?.next
                //Store current node symbol
                prevSymbol = currSymbol?.data
                println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

                read = currSymbol?.data
                when (read) {
                    'ɑ' -> {
                        //Set current node symbol to blankSymbol aka '#'
                        currSymbol?.data = blankSymbol
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
                            giveItem(nextSymbol, currSymbol?.prev)
                            return 1
                        } else {
                            replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            betaTransitions(currSymbol?.next)
                        }
                    }

                    'β' -> {
                        //Set current node symbol to blankSymbol aka '#'
                        currSymbol?.data = blankSymbol
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
                            giveItem(nextSymbol, currSymbol?.prev)
                            return 1
                        } else {
                            replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            betaTransitions(currSymbol?.next)
                        }
                    }

                    'γ' -> {
                        //Set current node symbol to blankSymbol aka '#'
                        currSymbol?.data = blankSymbol
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
                                giveItem(it, currSymbol?.prev)
                            }
                            return 1
                        } else {
                            replaceSymbol(nextSymbol, prevSymbol, currSymbol)
                            betaTransitions(currSymbol?.next)
                        }
                    }

                    '$' -> {
                        replaceSymbol(nextSymbol, prevSymbol, currentSymbol)
                        return 1
                    }

                    else -> {
                        //Check if only alphabet symbols remain
                        if (alphabet.contains(read)) {
                            return 0
                        }
                    }
                }
                currSymbol = currSymbol?.next
            }
        }
        return 0
    }

    private fun gammaTransitions(currentSymbol: Node?) {
        var found = true

        //Get the next node
        var nextSymbol = currentSymbol?.next
        println("Current: ${currentSymbol?.data}, Next: ${nextSymbol?.data}")
        var prevSymbol = currentSymbol?.data
        //Set to blankSymbol aka '#'
        currentSymbol?.data = blankSymbol
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
            found = true
            var currSymbol = currentSymbol?.next

            while (currSymbol?.data != '$') {
                //Get the next node
                nextSymbol = currSymbol?.next
                //Store current node symbol
                prevSymbol = currSymbol?.data
                println("Current: ${currSymbol?.data}, Next: ${nextSymbol?.data}")

                read = currSymbol?.data
                when (read) {
                    'ɑ' -> {
                        //Set current node symbol to blankSymbol aka '#'
                        currSymbol?.data = blankSymbol
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
                            giveItem(nextSymbol, currSymbol?.prev)
                        } else {
                            replaceSymbol(nextSymbol, prevSymbol, currSymbol?.prev)
                        }
                    }

                    'β' -> {
                        //Set current node symbol to blankSymbol aka '#'
                        currSymbol?.data = blankSymbol
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
                                giveItem(it, currSymbol?.prev)
                            }
                        } else {
                            replaceSymbol(nextSymbol, prevSymbol, currSymbol?.prev)
                        }
                    }

                    'γ' -> {
                        //Set current node symbol to blankSymbol aka '#'
                        currSymbol?.data = blankSymbol
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
                            giveItem(nextSymbol, currSymbol?.prev)
                        } else {
                            replaceSymbol(nextSymbol, prevSymbol, currSymbol?.prev)
                        }
                    }

                    else -> {
                        //Check if only alphabet symbols remain
                        if (alphabet.contains(read)) {
                            return
                        }
                    }
                }
                currSymbol = currSymbol?.next
            }
        }
    }

    private fun giveItem(currentSymbol: Node?, targetSymbol: Node?) {
        var found = true
        currentSymbol?.data = 'x'
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
                previousSymbol?.data = '#'
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
        println(inputList.showData())
    }
}