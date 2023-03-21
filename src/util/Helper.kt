package util

import models.inventory.Inventory.Companion.addItem
import models.inventory.Inventory.Companion.getFile
import models.turing.Register
import models.turing.Turing.Companion.tape

class Helper {
    companion object {

        fun getResults() {
            val registerMachine = Register()
            registerMachine.resetAllRegisters()

            tape.getData().forEach {
                when (it) {
                    // register 2 is used to track if there are insufficient funds
                    'F', 'K', 'N', 'S' -> registerMachine.run(arrayOf("INC 3"))
                    'ɑ', 'A' -> {
                        registerMachine.setRegisterValue(1, 5)
                        registerMachine.run(arrayOf("ADD 0 1")) // add 5 to register 0
                    }
                    'β', 'B' -> {
                        registerMachine.setRegisterValue(1, 10)
                        registerMachine.run(arrayOf("ADD 0 1")) // add 10 to register 0
                    }
                    'γ', 'Δ' -> {
                        registerMachine.setRegisterValue(1, 20)
                        registerMachine.run(arrayOf("ADD 0 1")) // add 20 to register 0
                    }
                }
            }

            if (registerMachine.getRegisterValue(3) != null && registerMachine.getRegisterValue(3)!! > 0) {
                println("Insufficient funds")
            }

            val total = registerMachine.getRegisterValue(0)
            if (total != null && total > 0) {
                println("Refund: $$total")
            }
        }

        fun addItems() {
            addItem("Fork", 15, 20)
            addItem("Knife", 25, 20)
            addItem("Napkin", 10, 20)
            addItem("Spoon", 20, 20)
            addItem("TotalSales", 0, 0)
        }

        fun closeFile() {
            getFile().close()
        }
    }
}