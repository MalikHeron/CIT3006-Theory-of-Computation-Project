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
            when (read) {
                'ɑ' -> {
                    write = machine.alpha
                    println("q2: $read -> $write, $right")
                    current?.data = machine.alpha
                    //Check combinations for 'ɑ'
                    alphaTransitions(current)
                }

                'β' -> {
                    write = machine.beta
                    println("q9: $read -> $write, $right")
                    current?.data = machine.beta
                    //Check combinations for 'β'
                    betaTransitions(current)
                }

                'γ' -> {
                    write = machine.delta
                    println("q14: $read -> $write, $right")
                    current?.data = machine.delta
                    //Check combinations for 'γ'
                    val node = gammaTransitions(current) as Node?
                    if (node != null) {
                        current = node
                    }
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
                if (it == 'F' || it == 'K' || it == 'N' || it == 'S') {
                    println("Insufficient funds")
                    return
                }
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
            //Move to the leftmost position on the tape
            goToFront(currentSymbol)
        } else {
            //Move one position to the right
            currentSymbol?.next
        }

        //While not end of input
        while (currSymbol?.data != machine.blankSymbol) {
            //Get symbol to the right of head
            nextSymbol = currSymbol?.next

            if (currSymbol != currentSymbol) {
                found = true
                //Read value under head
                read = currSymbol?.data
                when (read) {
                    'ɑ' -> {
                        write = machine.theta
                        println("q3: $read -> $write, $right")
                        //Write 'x' on current head position
                        currSymbol?.data = machine.theta

                        //Check if Napkin is a requested item
                        while (nextSymbol?.data != 'N') {
                            //Check if end of input is reached
                            if (nextSymbol?.data == machine.blankSymbol) {
                                found = false
                                break
                            }
                            //Move one position to the right
                            nextSymbol = nextSymbol?.next
                            read = "F, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                            write = "F, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                            println("q4: $read -> $right")
                        }

                        if (found) {
                            //Give the item and check it off
                            giveItem(nextSymbol)
                            return
                        } else {
                            //Write back the symbols
                            revertComboSymbol(nextSymbol)
                        }
                    }

                    'β' -> {
                        write = machine.mu
                        println("q5: $read -> $write, $right")
                        //Write 'x' on current head position
                        currSymbol?.data = machine.mu

                        //Check if Fork is a requested item
                        while (nextSymbol?.data != 'F') {
                            //Check if end of tape is reached
                            if (nextSymbol?.data == machine.blankSymbol) {
                                found = false
                                break
                            }
                            //Move one position to the right
                            nextSymbol = nextSymbol?.next
                            read = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                            write = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                            println("q6: $read -> $right")
                        }

                        if (found) {
                            giveItem(nextSymbol)
                            return
                        } else {
                            revertComboSymbol(nextSymbol)
                        }
                    }

                    'γ' -> {
                        write = machine.omega
                        println("q7: $read -> $write, $right")
                        //Write x on current head position
                        currSymbol?.data = machine.omega

                        //Check if Knife is a requested item
                        while (nextSymbol?.data != 'K') {
                            //Check if end of tape is reached
                            if (nextSymbol?.data == machine.blankSymbol) {
                                found = false
                                break
                            }
                            nextSymbol = nextSymbol?.next
                            read = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                            write = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                            println("q8: $read -> $right")
                        }

                        if (found) {
                            giveItem(nextSymbol)
                            return
                        } else {
                            revertComboSymbol(nextSymbol)
                        }
                    }

                    else -> {
                        //Check if only alphabet symbols remain
                        if (machine.inputAlphabet.contains(read)) {
                            revertSymbol(currSymbol)
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
        var tapeFront = goToFront(currentSymbol)
        //Get the next node
        var nextSymbol = currentSymbol?.next
        var currSymbol = currentSymbol

        if (currentSymbol?.prev != null) {
            //Get the next node
            tapeFront = goToFront(currentSymbol)?.next
            nextSymbol = tapeFront?.next
        }

        //Check if Napkin is a requested item
        while (nextSymbol?.data != 'N') {
            //Check if end of tape is reached
            if (nextSymbol?.data == machine.blankSymbol) {
                found = false
                break
            }
            nextSymbol = nextSymbol?.next
            read = "F, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
            write = "F, K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
            println("q4: $read -> $right")
        }

        if (found) {
            giveItem(nextSymbol)
        } else {
            revertComboSymbol(nextSymbol)
            currSymbol = tapeFront

            while (currSymbol?.data != machine.blankSymbol) {
                //Get the next node
                nextSymbol = currSymbol?.next

                if (currSymbol != currentSymbol) {
                    found = true
                    read = currSymbol?.data
                    when (read) {
                        'ɑ' -> {
                            write = machine.theta
                            println("q10: $read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.theta

                            while (nextSymbol?.data != 'F') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                nextSymbol = nextSymbol?.next
                                read = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                write = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                println("q6: $read -> $right")
                            }

                            if (found) {
                                giveItem(nextSymbol)
                                return
                            } else {
                                revertComboSymbol(nextSymbol)
                            }
                        }

                        'β' -> {
                            write = machine.mu
                            println("p11: $read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.mu

                            while (nextSymbol?.data != 'S') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                nextSymbol = nextSymbol?.next
                                read = "F, K, N, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                write = "F, K, N, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                println("q12: $read -> $right")
                            }

                            if (found) {
                                giveItem(nextSymbol)
                                return
                            } else {
                                revertComboSymbol(nextSymbol)
                            }
                        }

                        'γ' -> {
                            write = machine.omega
                            println("q13: $read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.omega

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
                                nextSymbol = nextSymbol?.next
                                read = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                write = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                println("q6: $read -> $right")
                            }

                            if (found) {
                                symbolArray.forEach {
                                    giveItem(it)
                                }
                                return
                            } else {
                                revertComboSymbol(nextSymbol)
                            }
                        }

                        else -> {
                            //Check if only alphabet symbols remain
                            if (machine.inputAlphabet.contains(read)) {
                                revertSymbol(currSymbol)
                                return
                            }
                        }
                    }
                }
                currSymbol = currSymbol?.next
            }
        }
        revertSymbol(currSymbol)
    }

    private fun gammaTransitions(currentSymbol: Node?): Any? {
        var found = true
        var tapeFront = goToFront(currentSymbol)
        //Get the next node
        var nextSymbol = currentSymbol?.next
        var currSymbol = currentSymbol

        if (currentSymbol?.prev != null) {
            //Get the next node
            tapeFront = goToFront(currentSymbol)?.next
            nextSymbol = tapeFront?.next
        }

        //Check if Napkin is a requested item
        while (nextSymbol?.data != 'S') {
            //Check if end of tape is reached
            if (nextSymbol?.data == machine.blankSymbol) {
                found = false
                break
            }
            if (nextSymbol?.data == 'F') {
                read = currentSymbol?.data
                write = 'ɑ'
                println("q16: $read -> $write, $right")
                currentSymbol?.data = 'ɑ'
                return giveItem(nextSymbol)
            }
            if (nextSymbol?.data == 'N') {
                read = currentSymbol?.data
                write = 'β'
                println("q18: $read -> $write, $right")
                currentSymbol?.data = 'β'
                return giveItem(nextSymbol)
            }
            nextSymbol = nextSymbol?.next
            read = "K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
            write = "K, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
            println("q15: $read -> $right")
        }

        if (found) {
            giveItem(nextSymbol)
        } else {
            revertComboSymbol(nextSymbol)
            currSymbol = tapeFront

            while (currSymbol?.data != machine.blankSymbol) {
                //Get the next node
                nextSymbol = currSymbol?.next

                if (currSymbol != currentSymbol) {
                    found = true
                    read = currSymbol?.data
                    when (read) {
                        'ɑ' -> {
                            write = machine.theta
                            println("q20: $read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.theta

                            while (nextSymbol?.data != 'K') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                nextSymbol = nextSymbol?.next
                                read = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                write = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                println("q8: $read -> $right")
                            }

                            if (found) {
                                giveItem(nextSymbol)
                                return null
                            } else {
                                revertComboSymbol(nextSymbol)
                            }
                        }

                        'β' -> {
                            write = machine.mu
                            println("q21: $read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.mu

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
                                nextSymbol = nextSymbol?.next
                                nextSymbol = nextSymbol?.next
                                read = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                write = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                println("q6: $read -> $right")
                            }

                            if (found) {
                                symbolArray.forEach {
                                    giveItem(it)
                                }
                                return null
                            } else {
                                revertComboSymbol(nextSymbol)
                            }
                        }

                        'γ' -> {
                            write = machine.omega
                            println("q22: $read -> $write, $right")
                            //Write 'x' on current head position
                            currSymbol?.data = machine.omega

                            while (nextSymbol?.data != 'F') {
                                if (nextSymbol?.data == machine.blankSymbol) {
                                    found = false
                                    break
                                }
                                //Move one position to the right
                                nextSymbol = nextSymbol?.next
                                read = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                write = "K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                                println("q6: $read -> $right")
                            }

                            if (found) {
                                giveItem(nextSymbol)
                                return null
                            } else {
                                revertComboSymbol(nextSymbol)
                            }
                        }

                        else -> {
                            //Check if only alphabet symbols remain
                            if (machine.inputAlphabet.contains(read)) {
                                revertSymbol(currSymbol)
                                return null
                            }
                        }
                    }
                }
                currSymbol = currSymbol?.next
            }
        }
        revertSymbol(currSymbol)
        return null
    }

    private fun giveItem(currentSymbol: Node?): Node? {
        var previousSymbol = currentSymbol
        val symbolList = arrayOf(machine.alpha, machine.beta, machine.delta, machine.theta, machine.mu, machine.omega)
        var tapeFront: Node? = null

        read = currentSymbol?.data
        write = machine.crossSymbol
        println("q23: $read -> $write, $left")
        println("Dispense '${currentSymbol?.data}'")
        //Check off item
        currentSymbol?.data = machine.crossSymbol

        while (previousSymbol != null) {
            val current = previousSymbol.data
            if (symbolList.contains(current)) {
                read = current
                write = machine.crossSymbol
                println("q25: $read -> $write, $left")
                previousSymbol.data = machine.crossSymbol
            }
            if (previousSymbol.prev == null) {
                tapeFront = previousSymbol
            }
            previousSymbol = previousSymbol.prev
            read = "F, K, N, S, ɑ, β, γ, x"
            write = "F, K, N, S, ɑ, β, γ, x"
            println("q24: $read -> $left")
        }

        println("Tape: ${tape.getData()}\n")
        return tapeFront
    }

    private fun revertComboSymbol(currentSymbol: Node?) {
        var previousSymbol = currentSymbol?.prev
        val symbolList = arrayOf(machine.theta, machine.mu, machine.omega)
        val replaceList = arrayOf('ɑ', 'β', 'γ')

        while (previousSymbol != null) {
            val current = previousSymbol.data
            if (symbolList.contains(current)) {
                read = current
                if (read == machine.theta) {
                    write = replaceList[0]
                    println("q27: $read -> $write, $left")
                    previousSymbol.data = replaceList[0]
                }
                if (read == machine.mu) {
                    write = replaceList[1]
                    println("q28: $read -> $write, $left")
                    previousSymbol.data = replaceList[1]
                }
                if (read == machine.omega) {
                    write = replaceList[2]
                    println("q29: $read -> $write, $left")
                    previousSymbol.data = replaceList[2]
                }
            }
            previousSymbol = previousSymbol.prev
            read = "F, K, N, S, ɑ, β, γ, A, B, Δ, x"
            write = "F, K, N, S, ɑ, β, γ, A, B, Δ, x"
            println("q26: $read -> $left")
        }

        println("Tape: ${tape.getData()}\n")
    }

    private fun revertSymbol(currentSymbol: Node?) {
        var previousSymbol = currentSymbol?.prev
        val symbolList = arrayOf(machine.alpha, machine.beta, machine.delta)
        val replaceList = arrayOf('ɑ', 'β', 'γ')

        while (previousSymbol != null) {
            val current = previousSymbol.data
            if (symbolList.contains(current)) {
                read = current
                if (read == machine.alpha) {
                    write = replaceList[0]
                    println("q31: $read -> $write, $left")
                    previousSymbol.data = replaceList[0]
                }
                if (read == machine.beta) {
                    write = replaceList[1]
                    println("q32: $read -> $write, $left")
                    previousSymbol.data = replaceList[1]
                }
                if (read == machine.delta) {
                    write = replaceList[2]
                    println("q33: $read -> $write, $left")
                    previousSymbol.data = replaceList[2]
                }
            }
            previousSymbol = previousSymbol.prev
            read = "F, K, N, S, ɑ, β, γ, θ, μ, Ω, x"
            write = "F, K, N, S, ɑ, β, γ, θ, μ, Ω, x"
            println("q30: $read -> $left")
        }

        println("Tape: ${tape.getData()}\n")
    }

    private fun goToFront(currentSymbol: Node?): Node? {
        var previousSymbol = currentSymbol?.prev

        if (previousSymbol != null) {
            while (previousSymbol?.prev != null) {
                previousSymbol = previousSymbol.prev
                read = "F, K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                write = "F, K, N, S, ɑ, β, γ, A, B, Δ, θ, μ, Ω, x"
                println("q34: $read -> $left")
            }
            println("Tape: ${tape.getData()}\n")
        }
        return previousSymbol
    }
}