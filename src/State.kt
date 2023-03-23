import Turing.Companion.inputHead
import Turing.Companion.inputTape
import Turing.Companion.itemHead
import Turing.Companion.itemTape
import Turing.Companion.machine

class State {
    companion object {
        private var initialState: Int = 1
        private var acceptState: Int = 20
        private var rejectState: Int = 19
        private val items = mutableMapOf(
            'F' to "Fork",
            'N' to "Napkin",
            'S' to "Spoon",
            'K' to "Knife"
        )
        private val itemList =
            arrayOf('F', 'K', 'N', 'S')
        private val currencyList =
            mutableMapOf('ɑ' to 0, 'β' to 1, 'γ' to 2)
        private var read: Char = '\u0000'
        private var write: Char = '\u0000'
        private var right = 'R'
        private var left = 'L'
        private var none = 'N'
        private var register = Register()

        fun getState(currentState: Int) {
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
                    getNextState(10)
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
                            println("q$currentState: $read -> $write, $left")
                            inputHead--
                            break
                        }
                        inputHead++
                    }
                    println("Input tape: $inputTape")
                    println("Item tape: $itemTape")
                    getNextState(5)
                }

                5 -> {
                    while (inputHead != 0) {
                        read = inputTape[inputHead]
                        write = read
                        if (inputTape[inputHead] == machine.crossSymbol) {
                            println("q$currentState: $read -> $write, $right")
                            inputHead++
                            break
                        } else {
                            println("q$currentState: $read -> $write, $left")
                        }
                        inputHead--
                    }
                    getNextState(6)
                }

                6 -> {
                    while (itemHead != (itemTape.lastIndex + 1)) {
                        read = itemTape[itemHead]
                        if (read == 'F') {
                            if ((register.getRegisterValue(0) ?: 0) >= 3) {
                                repeat(3) { register.run(arrayOf("DEC 0")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else if ((register.getRegisterValue(0) ?: 0) >= 1 &&
                                (register.getRegisterValue(1) ?: 0) >= 1
                            ) {
                                register.run(arrayOf("DEC 0", "DEC 1"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else if ((register.getRegisterValue(2) ?: 0) >= 1) {
                                register.run(arrayOf("DEC 2", "INC 0"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                inputTape[inputHead] = write
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            }
                        }
                        if (read == 'K') {
                            if ((register.getRegisterValue(0) ?: 0) >= 3 &&
                                (register.getRegisterValue(1) ?: 0) >= 1
                            ) {
                                register.run(arrayOf("DEC 0", "DEC 0", "DEC 0", "DEC 1"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else if ((register.getRegisterValue(0) ?: 0) >= 1 &&
                                (register.getRegisterValue(1) ?: 0) >= 2
                            ) {
                                register.run(arrayOf("DEC 0", "DEC 1", "DEC 1"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else if ((register.getRegisterValue(0) ?: 0) >= 5) {
                                repeat(5) { register.run(arrayOf("DEC 0")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else if ((register.getRegisterValue(0) ?: 0) >= 1 &&
                                (register.getRegisterValue(2) ?: 0) >= 1
                            ) {
                                register.run(arrayOf("DEC 0", "DEC 2"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                inputTape[inputHead] = write
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            }
                        }
                        if (read == 'N') {
                            if ((register.getRegisterValue(0) ?: 0) >= 2) {
                                repeat(2) { register.run(arrayOf("DEC 0")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else if ((register.getRegisterValue(1) ?: 0) >= 1
                            ) {
                                register.run(arrayOf("DEC 1"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else if ((register.getRegisterValue(2) ?: 0) >= 1) {
                                register.run(arrayOf("DEC 2", "INC 1"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                inputTape[inputHead] = write
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            }
                        }
                        if (read == 'S') {
                            if ((register.getRegisterValue(0) ?: 0) >= 4) {
                                repeat(4) { register.run(arrayOf("DEC 0")) }
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else if ((register.getRegisterValue(1) ?: 0) >= 2
                            ) {
                                register.run(arrayOf("DEC 1", "DEC 1"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else if ((register.getRegisterValue(2) ?: 0) >= 1
                            ) {
                                register.run(arrayOf("DEC 2"))
                                write = machine.crossSymbol
                                itemTape[itemHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            } else {
                                write = machine.crossSymbol
                                inputTape[inputHead] = write
                                inputTape[inputHead] = read
                                inputHead++
                                println("q$currentState: $read -> $write, $right")
                            }
                        }
                        if (itemHead == itemTape.lastIndex) {
                            println("q$currentState: $read -> $write, $left")
                            inputHead--
                            itemHead--
                            break
                        }
                        itemHead++
                    }
                    println("Input tape: $inputTape")
                    println("Item tape: $itemTape")
                    getNextState(7)
                }

                7 -> {
                    while (itemHead != 0) {
                        if (itemList.contains(itemTape[itemHead])) {
                            register.run(arrayOf("INC 3"))
                        }
                        read = itemTape[itemHead]
                        write = read
                        println("q$currentState: $read -> $write, $left")
                        inputHead--
                        itemHead--
                    }
                    if ((register.getRegisterValue(3) ?: 0) > 0) {
                        val count = (register.getRegisterValue(3)) ?: 0
                        repeat(count) { register.run(arrayOf("DEC 3")) }
                        getNextState(6)
                    } else {
                        getNextState(8)
                    }
                }

                8 -> {
                    while (itemHead != inputTape.lastIndex) {
                        if (inputHead > inputTape.lastIndex) {
                            break
                        }
                        if (itemList.contains(inputTape[inputHead])) {
                            println("Dispense '${items[inputTape[inputHead]]}'")
                        }
                        if (itemHead > itemTape.lastIndex) {
                            break
                        }
                        if (itemList.contains(itemTape[itemHead])) {
                            register.run(arrayOf("INC 3"))
                            println("Insufficient funds to purchase '${items[itemTape[itemHead]]}'")
                        }
                        inputHead++
                        itemHead++
                    }
                    getNextState(9)
                }

                9 -> {
                    val reg0 = (register.getRegisterValue(0) ?: 0)
                    val reg1 = (register.getRegisterValue(1) ?: 0)
                    val reg2 = (register.getRegisterValue(2) ?: 0)
                    val reg3 = (register.getRegisterValue(3) ?: 0)

                    if (reg0 > 0 || reg1 > 0 || reg2 > 0) {
                        //Refund
                        getNextState(rejectState)
                    } else if (reg3 > 0) {
                        getNextState(rejectState)
                    } else {
                        getNextState(acceptState)
                    }
                }

                10 -> {
                    read = inputTape[inputHead]
                    write = read
                    if (read == 'N') {
                        println("q$currentState: $read -> $write, $right")
                        inputHead++
                        getNextState(11)
                    } else {
                        println("q$currentState: $read -> $write, $none")
                        getNextState(4)
                    }
                }

                11 -> {
                    read = inputTape[inputHead]
                    write = read
                    if (read == 'K') {
                        println("q$currentState: $read -> $write, $right")
                        inputHead++
                        getNextState(12)
                    } else {
                        println("q$currentState: $read -> $write, $left")
                        inputHead--
                        getNextState(4)
                    }
                }

                12 -> {
                    read = inputTape[inputHead]
                    write = read
                    if (read == 'S') {
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
                    if (read == 'F') {
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
                    if (read == 'S') {
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
                    if (read == 'K') {
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
                    if (read == 'S') {
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
                    //Stop machine
                    println("qr: Halt")
                    return
                }

                acceptState -> {
                    //Stop machine
                    println("qa: Halt")
                    return
                }
            }
        }

        private fun getNextState(nextState: Int) {
            getState(nextState)
        }
    }
}