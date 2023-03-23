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

    fun run(instructions: Array<String>) {
        var currentInstruction = 0
        while (currentInstruction < instructions.size) {
            val instruction = instructions[currentInstruction]
            println("\nExecuting instruction $currentInstruction: $instruction")
            when {
                instruction.startsWith("LOAD") -> {
                    val parts = instruction.split(" ")
                    val value = parts[1].toInt()
                    val register = parts[2].toInt()
                    setRegisterValue(register, value)
                }

                instruction.startsWith("INC") -> {
                    val register = instruction.split(" ")[1].toInt()
                    increment(register)
                }

                instruction.startsWith("DEC") -> {
                    val register = instruction.split(" ")[1].toInt()
                    decrement(register)
                }

                instruction.startsWith("ADD") -> {
                    val parts = instruction.split(" ")
                    val registerX = parts[1].toInt()
                    val registerY = parts[2].toInt()
                    add(registerX, registerY)
                }
            }
            currentInstruction++
        }
        println("Final configuration: ")
        registers.forEach {
            println("Register ${it.key} = ${it.value}")
        }
        println()
    }

    fun isRegisterEmpty(register: Int): Boolean {
        return registers[register] == null
    }

    fun resetRegister(register: Int) {
        registers[register] = 0
    }

    fun resetAllRegisters() {
        registers.keys.forEach { resetRegister(it) }
    }

    fun getRegisterValue(register: Int): Int? {
        return registers[register]
    }

    private fun setRegisterValue(register: Int, value: Int) {
        registers[register] = value
        println("Register $register = ${registers[register]}\n")
    }

    private fun increment(register: Int) {
        registers[register] = (registers[register] ?: 0) + 1
        println("Register $register = ${registers[register]}\n")
    }

    private fun decrement(register: Int) {
        if ((registers[register] ?: 0) > 0) {
            registers[register] = (registers[register] ?: 0) - 1
            println("Register $register = ${registers[register]}\n")
        } else {
            println("Register $register value is already 0 and cannot be decremented.\n")
        }
    }

    private fun add(registerX: Int, registerY: Int) {
        registers[0] = (registers[registerX] ?: 0) + (registers[registerY] ?: 0)
        println("Register $registerX = ${registers[registerX]}")
        println("Register $registerY = ${registers[registerY]}")
        println("Result stored in Register 0.\n")
    }
}