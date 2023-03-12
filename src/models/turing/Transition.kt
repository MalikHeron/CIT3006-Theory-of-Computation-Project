package models.turing

import util.DoublyLinkedList
import util.Node
import kotlin.properties.Delegates

class Transition(private var input: String) {

    private var read: Any? = null
    private var blankSymbol = '#'
    private var currentState = ""
    private var current: Node? = null
    private var inputList: DoublyLinkedList = DoublyLinkedList()
    private var length by Delegates.notNull<Int>()

    init {
        length = input.length
        getSymbols(input)
        doTransitions()
    }

    private fun getSymbols(input: String) {
        input.forEach {
            inputList.addNewNode(it)
        }
        println(inputList.showData())
    }

    private fun doTransitions() {
        current = inputList.getCurrent()
        while (read != '$') {
            read = current?.data!!
            println("Read: $read")
            when (read) {
                'ɑ' -> {
                    alphaTransitions(current?.next)
                }

                'β' -> {
                    betaTransitions(current?.next)
                }

                'γ' -> {
                    gammaTransitions(current?.next)
                }

                '#' -> {
                    current = current?.next
                }
            }
        }
    }

    private fun alphaTransitions(currentSymbol: Node?) {
        var found = true
        read = currentSymbol?.data
        when (read) {
            'ɑ' -> {
                currentSymbol?.data = blankSymbol
                var nextSymbol = currentSymbol?.next
                println("Next: ${nextSymbol?.data}")

                while (nextSymbol?.data != 'N') {
                    if (nextSymbol?.data == '$') {
                        found = false
                        break
                    }
                    nextSymbol = nextSymbol?.next
                    println("Next: ${nextSymbol?.data}")
                }

                if (found) {
                    giveItem(nextSymbol, 'ɑ')
                } else {
                    replaceSymbol(nextSymbol, 'ɑ', 'ɑ')
                }
            }

            'β' -> {
                currentSymbol?.data = blankSymbol
                var nextSymbol = currentSymbol?.next
                println("Next: ${nextSymbol?.data}")

                while (nextSymbol?.data != 'F') {
                    if (nextSymbol?.data == '$') {
                        found = false
                        break
                    }
                    nextSymbol = nextSymbol?.next
                    println("Next: ${nextSymbol?.data}")
                }

                if (found) {
                    giveItem(nextSymbol, 'ɑ')
                } else {
                    replaceSymbol(nextSymbol, 'β', 'ɑ')
                }
            }

            'γ' -> {
                currentSymbol?.data = blankSymbol
                var nextSymbol = currentSymbol?.next
                println("Next: ${nextSymbol?.data}")

                while (nextSymbol?.data != 'K') {
                    if (nextSymbol?.data == '$') {
                        found = false
                        break
                    }
                    nextSymbol = nextSymbol?.next
                    println("Next: ${nextSymbol?.data}")
                }

                if (found) {
                    giveItem(nextSymbol, 'ɑ')
                } else {
                    replaceSymbol(nextSymbol, 'γ', 'ɑ')
                }
            }
        }
    }

    private fun betaTransitions(currentSymbol: Node?) {
        var found = true
        read = currentSymbol?.data
        when (read) {
            'ɑ' -> {
                currentSymbol?.data = blankSymbol
                var nextSymbol = currentSymbol?.next
                println("Next: ${nextSymbol?.data}")

                while (nextSymbol?.data != 'N') {
                    if (nextSymbol?.data == '$') {
                        found = false
                        break
                    }
                    nextSymbol = nextSymbol?.next
                    println("Next: ${nextSymbol?.data}")
                }

                if (found) {
                    giveItem(nextSymbol, 'ɑ')
                } else {
                    replaceSymbol(nextSymbol, 'ɑ', 'ɑ')
                }
            }

            'β' -> {
                currentSymbol?.data = blankSymbol
                var nextSymbol = currentSymbol?.next
                println("Next: ${nextSymbol?.data}")

                while (nextSymbol?.data != 'F') {
                    if (nextSymbol?.data == '$') {
                        found = false
                        break
                    }
                    nextSymbol = nextSymbol?.next
                    println("Next: ${nextSymbol?.data}")
                }

                if (found) {
                    giveItem(nextSymbol, 'ɑ')
                } else {
                    replaceSymbol(nextSymbol, 'β', 'ɑ')
                }
            }

            'γ' -> {
                currentSymbol?.data = blankSymbol
                var nextSymbol = currentSymbol?.next
                println("Next: ${nextSymbol?.data}")

                while (nextSymbol?.data != 'K') {
                    if (nextSymbol?.data == '$') {
                        found = false
                        break
                    }
                    nextSymbol = nextSymbol?.next
                    println("Next: ${nextSymbol?.data}")
                }

                if (found) {
                    giveItem(nextSymbol, 'ɑ')
                } else {
                    replaceSymbol(nextSymbol, 'γ', 'ɑ')
                }
            }
        }
    }

    private fun gammaTransitions(currentSymbol: Node?) {

    }

    private fun giveItem(currentSymbol: Node?, targetSymbol: Char) {
        var found = true
        currentSymbol?.data = 'x'

        var previousSymbol = currentSymbol?.prev
        println("Previous: ${previousSymbol?.data}")

        while (previousSymbol?.data != '#') {
            if (previousSymbol?.data == '$') {
                found = false
                break
            }
            previousSymbol = previousSymbol?.prev
            println("Previous: ${previousSymbol?.data}")
        }

        if (found) {
            previousSymbol = previousSymbol?.prev
            while (previousSymbol?.data != targetSymbol) {
                previousSymbol = previousSymbol?.prev
                println("Previous: ${previousSymbol?.data}")
            }
            previousSymbol.data = '#'
        }
        println(inputList.showData())
    }

    private fun replaceSymbol(currentSymbol: Node?, subSymbol: Char, predecessor: Char) {
        var previousSymbol = currentSymbol?.prev
        println("Previous: ${previousSymbol?.data}")

        while (previousSymbol?.data != '#') {
            if (previousSymbol == null) {
                return
            }
            previousSymbol = previousSymbol.prev
            println("Previous: ${previousSymbol?.data}")
        }

        if (previousSymbol.prev?.data == predecessor) {
            previousSymbol.data = subSymbol
        }

        println(inputList.showData())
    }
}