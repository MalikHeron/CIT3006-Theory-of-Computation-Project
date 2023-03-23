class Register {
    private val registers = mutableMapOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)

    fun run(instructions: Array<String>) {
        var currentInstruction = 0
        while (currentInstruction < instructions.size) {
            val instruction = instructions[currentInstruction]
            println("\nExecuting instruction $currentInstruction: $instruction")
            when {
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
        println(
            "Final configuration: Register 0 = ${registers[0]}, " +
                    "Register 1 = ${registers[1]}, Register 2 = ${registers[2]}, Register 3 = ${registers[3]}\n"
        )
    }

    fun isRegisterEmpty(register: Int): Boolean {
        return registers[register] == null
    }

    private fun resetRegister(register: Int) {
        registers[register] = 0
    }

    fun resetAllRegisters() {
        registers.keys.forEach { resetRegister(it) }
    }

    fun getRegisterValue(register: Int): Int? {
        return registers[register]
    }

    fun setRegisterValue(register: Int, value: Int) {
        registers[register] = value
    }

    private fun increment(register: Int) {
        registers[register] = (registers[register] ?: 0) + 1
        println("Register $register value: ${registers[register]}")
    }

    private fun decrement(register: Int) {
        if ((registers[register] ?: 0) > 0) {
            registers[register] = (registers[register] ?: 0) - 1
            println("Register $register value: ${registers[register]}")
        } else {
            println("Register $register value is already 0 and cannot be decremented")
        }
    }

    private fun add(registerX: Int, registerY: Int) {
        registers[0] = (registers[registerX] ?: 0) + (registers[registerY] ?: 0)
        println("Register $registerX value: ${registers[registerX]}")
        println("Register $registerY value: ${registers[registerY]}")
        println("Result stored in Register 0. Register 0 value: ${registers[0]}")
    }
}