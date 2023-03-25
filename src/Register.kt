class Register {
    private val registers = mutableMapOf(
        0 to 0,
        1 to 0,
        2 to 0,
        3 to 0,
        4 to 0,
        5 to 0,
        6 to 0,
        7 to 0,
        8 to 0
    )

    inner class RunInstructions(private var instructions: Array<String>) {
        private var currentInstruction = 0

        init {
            //Start running the instructions
            execute()
        }

        private fun execute() {
            while (currentInstruction < instructions.size) {
                val instruction = instructions[currentInstruction]
                if (currentInstruction == 0) {
                    println("\nExecuting instruction 0: $instruction")
                } else {
                    println("Executing instruction $currentInstruction: $instruction")
                }
                when {
                    //Check if instruction is to increment
                    instruction.startsWith("INC") -> {
                        val register = instruction.split(" ")[1].toInt()
                        increment(register)
                    }
                    //Check if instruction is to decrement
                    instruction.startsWith("DEC") -> {
                        val register = instruction.split(" ")[1].toInt()
                        decrement(register)
                    }
                }
                //Go to next instruction
                currentInstruction++
            }
            println("Final configuration: ")
            registers.forEach {
                println("Register ${it.key} = ${it.value}")
            }
            println()
        }

        private fun increment(register: Int) {
            //Increment the register value
            registers[register] = (registers[register] ?: 0) + 1
        }
    }

    inner class CalculateSales(instructions: Array<String>) {

        init {
            //Get registerX and the value
            val partsX = instructions[0].split(" ")
            val valueX = partsX[1].toInt()
            val registerX = partsX[2].toInt()

            //Get registerY and the value
            val partsY = instructions[1].split(" ")
            val valueY = partsY[1].toInt()
            val registerY = partsY[2].toInt()

            //Execute instructions
            InstructionSet(registerX, registerY, valueX, valueY).execute()

            println("\nFinal configuration: ")
            registers.forEach {
                println("Register ${it.key} = ${it.value}")
            }
        }

        inner class InstructionSet(
            private var registerX: Int,
            private var registerY: Int,
            private var valueX: Int,
            private var valueY: Int
        ) {
            fun execute() {
                //Load value on registerX
                setRegisterValue(registerX, valueX)
                //Load value on registerY
                setRegisterValue(registerY, valueY)
                //Go to instruction 1
                instruction1()
            }

            private fun instruction1() {
                if ((registers[registerX] ?: 0) > 0) {
                    //Decrement register value
                    decrement(registerX)
                    //Go to instruction 2
                    instruction2()
                } else {
                    instruction3()
                }
            }

            private fun instruction2() {
                //Increment register value
                increment()
                //Go to instruction 1
                instruction1()
            }

            private fun instruction3() {
                if ((registers[registerY] ?: 0) > 0) {
                    //Decrement register value
                    decrement(registerY)
                    //Go to instruction 4
                    instruction4()
                } else {
                    //Go to instruction 5
                    halt()
                }
            }

            private fun instruction4() {
                //Increment register value
                increment()
                //Go to instruction 3
                instruction3()
            }

            private fun halt() {
                //End of instructions
                return
            }
        }
    }

    inner class CalculateFunds(instructions: Array<String>) {

        init {
            //Get register to decrement
            val registerX = instructions[0].split(" ")[1].toInt()
            //Get register to load and value to load
            val parts = instructions[1].split(" ")
            val value = parts[1].toInt()
            val registerY = parts[2].toInt()
            //Execute instructions
            InstructionSet(registerX, registerY, value).execute()

            println("\nFinal configuration: ")
            registers.forEach {
                println("Register ${it.key} = ${it.value}")
            }
        }

        inner class InstructionSet(
            private var registerX: Int,
            private var registerY: Int,
            private var value: Int
        ) {
            fun execute() {
                //Go to instruction 1
                instruction1()
            }

            private fun instruction1() {
                if ((registers[registerX] ?: 0) > 0) {
                    //Decrement register value
                    decrement(registerX)
                    //Load value on register
                    setRegisterValue(registerY, value)
                    //Go to instruction 2
                    instruction2()
                } else {
                    halt()
                }
            }

            private fun instruction2() {
                if ((registers[registerY] ?: 0) > 0) {
                    //Decrement register value
                    decrement(registerY)
                    //Go to instruction 3
                    instruction3()
                } else {
                    instruction1()
                }
            }

            private fun instruction3() {
                //Increment register 0
                increment()
                //Go to instruction 2
                instruction2()
            }

            private fun halt() {
                return
            }
        }
    }

    private fun checkIfZero(register: Int): Boolean {
        return registers[register] == 0
    }

    fun resetRegister(register: Int) {
        registers[register] = 0
    }

    fun resetAllRegisters() {
        //Reset each register to 0
        registers.keys.forEach { resetRegister(it) }
    }

    fun getRegisterValue(register: Int): Int? {
        return registers[register]
    }

    private fun setRegisterValue(register: Int, value: Int) {
        registers[register] = value
    }

    private fun increment() {
        //Increment register 0 value
        registers[0] = (registers[0] ?: 0) + 1
    }

    private fun decrement(register: Int) {
        //Decrement register if the value is greater than zero
        if (!checkIfZero(register)) {
            registers[register] = registers[register]!! - 1
        }
    }
}