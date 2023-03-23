class State {

    private var inputHead = 0
    private var itemHead = 0
    private var stockHead = 0
    private var inputTape = arrayListOf<Char>()
    private var itemTape = arrayListOf<Char>()
    private var stockTape = arrayListOf<Char>()
    private var machine = Machine()
    private var initialState: Int = 1
    private var acceptState: Int = 21
    private var rejectState: Int = 20
    private val items = mutableMapOf(
        'F' to "Fork",
        'N' to "Napkin",
        'S' to "Spoon",
        'K' to "Knife"
    )
    private val itemList =
        arrayOf('F', 'K', 'N', 'S')
    private val currencyList =
        mutableMapOf('ɑ' to 1, 'β' to 2, 'γ' to 3)
    private var read: Char = '\u0000'
    private var write: Char = '\u0000'
    private var right = 'R'
    private var left = 'L'
    private var none = 'N'
    private var register = Register()

    fun doTransitions(inputTape: ArrayList<Char>): ArrayList<Char> {
        this.inputTape = inputTape
        getState(1)
        return this.inputTape
    }

    private fun getState(currentState: Int) {
        when (currentState) {
            initialState -> {
                while (inputHead != (inputTape.lastIndex + 1)) {
                    read = inputTape[inputHead]
                    write = read

                    //Check for invalid input
                    if (!machine.tapeAlphabet.contains(inputTape[inputHead])) {
                        println("Invalid input detected!")
                        getNextState(rejectState)
                        return
                    }
                    if (inputHead != inputTape.lastIndex) {
                        println("q$currentState: $read -> $write, $right")
                    } else {
                        println("q$currentState: $read -> $write, $left")
                        inputHead--
                        break
                    }
                    inputHead++
                }
                getNextState(2)
            }

            2 -> {
                while (inputHead != 0) {
                    read = inputTape[inputHead]
                    write = read
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                }
                getNextState(11)
            }

            3 -> {
                while (inputHead != 0) {
                    read = inputTape[inputHead]
                    write = read
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                }
                getNextState(4)
            }

            4 -> {
                while (inputHead != (inputTape.lastIndex + 1)) {
                    read = inputTape[inputHead]
                    if (itemList.contains(read)) {
                        write = read
                        itemTape.add(read)
                    }
                    if (currencyList.contains(read)) {
                        write = machine.crossSymbol
                        inputTape[inputHead] = write
                        register.run(arrayOf("INC ${currencyList[read]}"))
                    }
                    if (inputHead != inputTape.lastIndex) {
                        println("q$currentState: $read -> $write, $right")
                    } else {
                        write = read
                        println("q$currentState: $read -> $write, $left")
                        itemTape.add(machine.blankSymbol)
                        inputHead--
                        break
                    }
                    inputHead++
                }
                println("Input tape: $inputTape")
                println("Item tape: $itemTape\n")
                getNextState(5)
            }

            5 -> {
                while (inputHead != 0) {
                    read = inputTape[inputHead]
                    write = read
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                    if (inputHead == 0 || inputTape[inputHead] == machine.crossSymbol) {
                        inputHead++
                        break
                    }
                }
                getNextState(6)
            }

            6 -> {
                while (itemHead != (itemTape.lastIndex + 1)) {
                    read = itemTape[itemHead]
                    if (read == 'F') {
                        if ((register.getRegisterValue(1) ?: 0) >= 3) {
                            if (inStock(read)) {
                                repeat(3) { register.run(arrayOf("DEC 1", "INC 6")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(1) ?: 0) >= 1 &&
                            (register.getRegisterValue(2) ?: 0) >= 1
                        ) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 1", "DEC 2", "INC 6", "INC 7"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(3) ?: 0) >= 1) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 3", "INC 1", "INC 8", "DEC 6"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else {
                            write = machine.crossSymbol
                            inputTape[inputHead] = write
                            println("q$currentState: $read -> $write, $right")
                        }
                    }
                    if (read == 'K') {
                        if ((register.getRegisterValue(1) ?: 0) >= 3 &&
                            (register.getRegisterValue(2) ?: 0) >= 1
                        ) {
                            if (inStock(read)) {
                                repeat(3) { register.run(arrayOf("DEC 1", "INC 6")) }
                                register.run(arrayOf("DEC 2", "INC 7"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(1) ?: 0) >= 1 &&
                            (register.getRegisterValue(2) ?: 0) >= 2
                        ) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 1"))
                                repeat(2) { register.run(arrayOf("DEC 2", "INC 7")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(1) ?: 0) >= 5) {
                            if (inStock(read)) {
                                repeat(5) { register.run(arrayOf("DEC 1", "INC 6")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(1) ?: 0) >= 1 &&
                            (register.getRegisterValue(3) ?: 0) >= 1
                        ) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 1", "INC 6", "DEC 3", "INC 8"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else {
                            write = machine.crossSymbol
                            inputTape[inputHead] = write
                            println("q$currentState: $read -> $write, $right")
                        }
                    }
                    if (read == 'N') {
                        if ((register.getRegisterValue(1) ?: 0) >= 2) {
                            if (inStock(read)) {
                                repeat(2) { register.run(arrayOf("DEC 1", "INC 6")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(2) ?: 0) >= 1
                        ) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 2", "INC 7"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(3) ?: 0) >= 1) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 3", "INC 8", "INC 2", "DEC 7"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else {
                            write = machine.crossSymbol
                            inputTape[inputHead] = write
                            println("q$currentState: $read -> $write, $right")
                        }
                    }
                    if (read == 'S') {
                        if ((register.getRegisterValue(1) ?: 0) >= 4) {
                            if (inStock(read)) {
                                repeat(4) { register.run(arrayOf("DEC 1", "INC 6")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(2) ?: 0) >= 2
                        ) {
                            if (inStock(read)) {
                                repeat(2) { register.run(arrayOf("DEC 2", "INC 7")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(3) ?: 0) >= 1
                        ) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 3", "INC 8"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else {
                            write = machine.crossSymbol
                            inputTape[inputHead] = write
                            inputTape[inputHead] = read
                            println("q$currentState: $read -> $write, $right")
                        }
                    }
                    if (itemHead == itemTape.lastIndex) {
                        write = read
                        println("q$currentState: $read -> $write, $left")
                        itemHead--
                        inputHead--
                        break
                    }
                    itemHead++
                    inputHead++
                }
                println("Input tape: $inputTape")
                println("Item tape: $itemTape\n")
                getNextState(7)
            }

            7 -> {
                while (itemHead != 0) {
                    if (itemList.contains(itemTape[itemHead])) {
                        register.run(arrayOf("INC 4"))
                    }
                    read = itemTape[itemHead]
                    write = read
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                    itemHead--
                }
                if ((register.getRegisterValue(4) ?: 0) > 0) {
                    val count = (register.getRegisterValue(4)) ?: 0
                    repeat(count) { register.run(arrayOf("DEC 4")) }
                    getNextState(8)
                } else {
                    getNextState(9)
                }
            }

            8 -> {
                while (itemHead != (itemTape.lastIndex + 1)) {
                    read = itemTape[itemHead]
                    if (read == 'F') {
                        if ((register.getRegisterValue(1) ?: 0) >= 3) {
                            if (inStock(read)) {
                                repeat(3) { register.run(arrayOf("DEC 1", "INC 6")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(1) ?: 0) >= 1 &&
                            (register.getRegisterValue(2) ?: 0) >= 1
                        ) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 1", "DEC 2", "INC 6", "INC 7"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(3) ?: 0) >= 1) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 3", "INC 1", "INC 8", "DEC 6"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else {
                            write = machine.crossSymbol
                            inputTape[inputHead] = write
                            println("q$currentState: $read -> $write, $right")
                        }
                    }
                    if (read == 'K') {
                        if ((register.getRegisterValue(1) ?: 0) >= 3 &&
                            (register.getRegisterValue(2) ?: 0) >= 1
                        ) {
                            if (inStock(read)) {
                                repeat(3) { register.run(arrayOf("DEC 1", "INC 6")) }
                                register.run(arrayOf("DEC 2", "INC 7"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(1) ?: 0) >= 1 &&
                            (register.getRegisterValue(2) ?: 0) >= 2
                        ) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 1"))
                                repeat(2) { register.run(arrayOf("DEC 2", "INC 7")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(1) ?: 0) >= 5) {
                            if (inStock(read)) {
                                repeat(5) { register.run(arrayOf("DEC 1", "INC 6")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(1) ?: 0) >= 1 &&
                            (register.getRegisterValue(3) ?: 0) >= 1
                        ) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 1", "INC 6", "DEC 3", "INC 8"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else {
                            write = machine.crossSymbol
                            inputTape[inputHead] = write
                            println("q$currentState: $read -> $write, $right")
                        }
                    }
                    if (read == 'N') {
                        if ((register.getRegisterValue(1) ?: 0) >= 2) {
                            if (inStock(read)) {
                                repeat(2) { register.run(arrayOf("DEC 1", "INC 6")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(2) ?: 0) >= 1
                        ) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 2", "INC 7"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(3) ?: 0) >= 1) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 3", "INC 8", "INC 2", "DEC 7"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else {
                            write = machine.crossSymbol
                            inputTape[inputHead] = write
                            println("q$currentState: $read -> $write, $right")
                        }
                    }
                    if (read == 'S') {
                        if ((register.getRegisterValue(1) ?: 0) >= 4) {
                            if (inStock(read)) {
                                repeat(4) { register.run(arrayOf("DEC 1", "INC 6")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(2) ?: 0) >= 2
                        ) {
                            if (inStock(read)) {
                                repeat(2) { register.run(arrayOf("DEC 2", "INC 7")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else if ((register.getRegisterValue(3) ?: 0) >= 1
                        ) {
                            if (inStock(read)) {
                                register.run(arrayOf("DEC 3", "INC 8"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = write
                                println("q$currentState: $read -> $write, $right")
                                stockTape.add(read)
                            }
                        } else {
                            write = machine.crossSymbol
                            inputTape[inputHead] = write
                            inputTape[inputHead] = read
                            println("q$currentState: $read -> $write, $right")
                        }
                    }
                    if (itemHead == itemTape.lastIndex) {
                        write = read
                        println("q$currentState: $read -> $write, $left")
                        itemHead--
                        inputHead--
                        break
                    }
                    itemHead++
                    inputHead++
                }
                println("Input tape: $inputTape")
                println("Item tape: $itemTape\n")
                getNextState(9)
            }

            9 -> {
                while (itemHead != (itemTape.lastIndex + 1)) {
                    if (inputHead == inputTape.lastIndex || itemHead == itemTape.lastIndex) {
                        inputHead--
                        itemHead--
                        break
                    }
                    if (itemList.contains(itemTape[itemHead])) {
                        register.run(arrayOf("INC 4"))
                        println("Insufficient funds to purchase ${items[itemTape[itemHead]]}")
                    }
                    inputHead++
                    itemHead++
                }
                getNextState(10)
            }

            10 -> {
                if (stockTape.isNotEmpty()) {
                    while (stockHead != stockTape.lastIndex) {
                        read = stockTape[stockHead]
                        write = machine.crossSymbol
                        println("q$currentState: $read -> $write, $left")

                        if (itemList.contains(read)) {
                            println("${items[read]} is out of stock.")
                        }
                        stockHead++
                    }
                }

                val reg1 = (register.getRegisterValue(1) ?: 0)
                val reg2 = (register.getRegisterValue(2) ?: 0)
                val reg3 = (register.getRegisterValue(3) ?: 0)
                val reg4 = (register.getRegisterValue(4) ?: 0)
                val reg6 = (register.getRegisterValue(6) ?: 0)
                val reg7 = (register.getRegisterValue(7) ?: 0)
                val reg8 = (register.getRegisterValue(8) ?: 0)

                if (reg1 > 0 || reg2 > 0 || reg3 > 0) {
                    repeat(reg1) {
                        // decrease register 1, load 5 on register 5 and add value on register 5 to register 0
                        register.run(arrayOf("DEC 1", "LOAD 5 5", "ADD 0 5"))
                        inputTape.add('ɑ')
                    }

                    repeat(reg2) {
                        // decrease register 2, load 10 on register 5 and add value on register 5 to register 0
                        register.run(arrayOf("DEC 2", "LOAD 10 5", "ADD 0 5"))
                        inputTape.add('β')
                    }

                    repeat(reg3) {
                        // decrease register 3, load 20 on register 5 and add value on register 5 to register 0
                        register.run(arrayOf("DEC 3", "LOAD 20 5", "ADD 0 5"))
                        inputTape.add('γ')
                    }

                    //Get value on register 0
                    val total = register.getRegisterValue(0)
                    if (total != null && total > 0) {
                        println("Refund: $$total")
                    }

                    //Reset register 0
                    register.resetRegister(0)
                    //Empty the till and calculate to total sales
                    repeat(reg6) { register.run(arrayOf("DEC 6", "LOAD 5 5", "ADD 0 5")) }
                    repeat(reg7) { register.run(arrayOf("DEC 7", "LOAD 10 5", "ADD 0 5")) }
                    repeat(reg8) { register.run(arrayOf("DEC 8", "LOAD 20 5", "ADD 0 5")) }

                    //Get value on register 0
                    val totalSales = register.getRegisterValue(0)
                    Inventory.setFunds(totalSales!!.toDouble())

                    getNextState(rejectState)
                } else if (reg4 > 0) {
                    getNextState(rejectState)
                } else {
                    getNextState(acceptState)
                }
            }

            11 -> {
                read = inputTape[inputHead]
                write = read
                if (read == 'N') {
                    println("q$currentState: $read -> $write, $right")
                    inputHead++
                    getNextState(12)
                } else {
                    println("q$currentState: $read -> $write, $none")
                    getNextState(4)
                }
            }

            12 -> {
                read = inputTape[inputHead]
                write = read
                if (read == 'K') {
                    println("q$currentState: $read -> $write, $right")
                    inputHead++
                    getNextState(13)
                } else {
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                    getNextState(4)
                }
            }

            13 -> {
                read = inputTape[inputHead]
                write = read
                if (read == 'S') {
                    println("q$currentState: $read -> $write, $right")
                    inputHead++
                    getNextState(14)
                } else {
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                    getNextState(4)
                }
            }

            14 -> {
                read = inputTape[inputHead]
                write = read
                if (read == 'F') {
                    println("q$currentState: $read -> $write, $right")
                    inputHead++
                    getNextState(15)
                } else {
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                    getNextState(4)
                }
            }

            15 -> {
                read = inputTape[inputHead]
                write = read
                if (read == 'F') {
                    println("q$currentState: $read -> $write, $right")
                    inputHead++
                    getNextState(16)
                } else {
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                    getNextState(4)
                }
            }

            16 -> {
                read = inputTape[inputHead]
                write = read
                if (read == 'S') {
                    println("q$currentState: $read -> $write, $right")
                    inputHead++
                    getNextState(17)
                } else {
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                    getNextState(4)
                }
            }

            17 -> {
                read = inputTape[inputHead]
                write = read
                if (read == 'K') {
                    println("q$currentState: $read -> $write, $right")
                    inputHead++
                    getNextState(18)
                } else {
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                    getNextState(4)
                }
            }

            18 -> {
                read = inputTape[inputHead]
                write = read
                if (read == 'S') {
                    println("q$currentState: $read -> $write, $right")
                    inputHead++
                    getNextState(19)
                } else {
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                    getNextState(4)
                }
            }

            19 -> {
                read = inputTape[inputHead]
                write = read
                if (read == machine.blankSymbol) {
                    println("q$currentState: $read -> $write, $right")
                    println("Funds: $${Inventory.getFunds()}")
                    Inventory.restockInventory()
                    Inventory.resetFunds()
                    getNextState(acceptState)
                } else {
                    println("q$currentState: $read -> $write, $left")
                    inputHead--
                    getNextState(4)
                }
            }

            rejectState -> {
                //Halt machine
                println("qr: Halt\n")
                return
            }

            acceptState -> {
                //Halt machine
                println("qa: Halt\n")
                return
            }
        }
    }

    private fun getNextState(nextState: Int) {
        getState(nextState)
    }

    private fun inStock(symbol: Char): Boolean {
        if (itemList.contains(symbol)) {
            val itemStock = Inventory.getItemStock(symbol)
            if (itemStock > 0) {
                println("Dispense '${items[symbol]}'")

                val registerM = Register()
                registerM.run(arrayOf("LOAD $itemStock 1", "DEC 1"))
                Inventory.setItemStock(symbol, (registerM.getRegisterValue(1) ?: 0))
                return true
            }
        }
        return false
    }
}