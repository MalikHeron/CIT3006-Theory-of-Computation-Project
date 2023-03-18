package util

import models.filing.Item
import models.turing.State
import models.turing.Turing.Companion.head
import models.turing.Turing.Companion.tape
import java.io.IOException
import java.io.RandomAccessFile
import javax.swing.JOptionPane

class Helper {
    companion object {

        fun buildTape(input: String) {
            //Iterate through the symbols in the input
            input.forEach {
                //Add the characters as symbols to the tape
                tape.addNewNode(it)
            }
            head = tape.getCurrent()
            println("Tape: ${tape.getData()}\n")
        }

        fun checkCount(alphaCounter: Int, betaCounter: Int): Node? {
            when (alphaCounter) {
                1 -> {
                    if (betaCounter == 2) {
                        State.currentState = 38
                        return State.getState(null, head)
                    }
                }

                2 -> {
                    if (betaCounter == 1) {
                        State.currentState = 39
                        return State.getState(null, head)
                    }
                }

                3 -> {
                    if (betaCounter == 1) {
                        State.currentState = 38
                        return State.getState(null, head)
                    } else if (betaCounter == 0) {
                        State.currentState = 40
                        return State.getState(null, head)
                    }
                }

                4 -> {
                    State.currentState = 39
                    return State.getState(null, head)
                }

                5 -> {
                    State.currentState = 38
                    return State.getState(null, head)
                }
            }
            return null
        }

        fun refund() {
            var total = 0
            tape.getData().forEach {
                if (it == 'ɑ' || it == 'A')
                    total += 5
                if (it == 'β' || it == 'B')
                    total += 10
                if (it == 'γ' || it == 'Δ')
                    total += 20
            }
            if (total != 0)
                println("Refund: $$total\n")
        }

        private var file: RandomAccessFile? = null

        private val ITEM_PRICES = mapOf(
                'N' to 10,
                'K' to 25,
                'F' to 15,
                'S' to 20
        )

        private fun getFile(): RandomAccessFile {
            if (file == null) {
                throw IllegalStateException("File not properly initialized")
            }
            return file!!
        }

        @Throws(IOException::class)
        fun getItemStock(name: String): Int {//Parameter less, how??
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val price = getFile().readDouble()
                val quantity = getFile().readInt()
                if (itemName == name) {
                    getFile().close()
                    return quantity
                }
            }
            getFile().close()
            // If item was not found, throw exception
            val errorMessage = "$name is not an item in the inventory"
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE)
            throw IllegalArgumentException(errorMessage)
        }

        @Throws(IOException::class)
        fun setItemStock(item: Item) {
            // Seek to the beginning of the file
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val price = getFile().readDouble()
                val currentQuantity = getFile().readInt()
                if (itemName == item.name) {
                    // Found the item, update the quantity and write it back to the file
                    getFile().seek(getFile().filePointer - 4) // Move back to the quantity field
                    getFile().writeInt(item.quantity)
                    getFile().close()
                    return
                }
            }
            getFile().close()
            // If item was not found, throw an exception
            val errorMessage = "${item.name} is not an item in the inventory"
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE)
            throw IllegalArgumentException(errorMessage)
        }

        @Throws(IOException::class)
        fun restockInventory() {
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().getFilePointer() < getFile().length()) {
                val itemName: String = getFile().readUTF()
                val price: Double = getFile().readDouble()
                val currentQuantity: Int = getFile().readInt()
                getFile().seek(getFile().getFilePointer() - 4) // Move back to the quantity field
                getFile().writeInt(20) //update to full quantity
            }
            getFile().close()
        }

        @Throws(IOException::class)
        fun getFunds(): Double {
            var totalFunds = 0.0
            // Seek to the beginning of the file
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val price = getFile().readDouble()
                val currentQuantity = getFile().readInt()
                if (ITEM_PRICES.containsKey(itemName[0])) {
                    totalFunds += ITEM_PRICES[itemName[0]]!! * (20 - currentQuantity)
                }
            }
            getFile().close()
            return totalFunds
        }

        fun setFunds(funds: Double) {}


    }
}