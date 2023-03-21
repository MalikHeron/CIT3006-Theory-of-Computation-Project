package util

import models.turing.State
import models.turing.Turing.Companion.head
import models.turing.Turing.Companion.tape
import java.io.IOException
import java.io.RandomAccessFile
import javax.swing.JOptionPane
import kotlin.system.exitProcess

class Helper {
    companion object {

        private var file = RandomAccessFile("files//inventory.txt", "rw")

        fun checkCount(alphaCounter: Int, betaCounter: Int): Node? {
            when (alphaCounter) {
                1 -> {
                    if (betaCounter == 2) {
                        return State.getState(20, null, head)
                    }
                }

                2 -> {
                    if (betaCounter == 1) {
                        return State.getState(21, null, head)
                    }
                }

                3 -> {
                    if (betaCounter == 1) {
                        return State.getState(20, null, head)
                    } else if (betaCounter == 0) {
                        return State.getState(22, null, head)
                    }
                }

                4 -> return State.getState(21, null, head)

                5 -> return State.getState(20, null, head)
            }
            return null
        }

        fun isRemaining(): Boolean {
            var total = 0
            var insufficient = false
            var refund = false
            tape.getData().forEach {
                if (it == 'F' || it == 'K' || it == 'N' || it == 'S')
                    insufficient = true
                if (it == 'ɑ' || it == 'A')
                    total += 5
                if (it == 'β' || it == 'B')
                    total += 10
                if (it == 'γ' || it == 'Δ')
                    total += 20
            }
            if (insufficient)
                println("Insufficient funds")
            if (total != 0)
                refund = true
            println("Refund: $$total")

            return refund || insufficient
        }

        private fun getFile(): RandomAccessFile {
            return file
        }

        fun addItems() {
            addItem("Fork", 20)
            addItem("Knife", 20)
            addItem("Napkin", 20)
            addItem("Spoon", 20)
        }

        @Throws(IOException::class)
        fun addItem(name: String, quantity: Int) {//use this same method to add TotalSales during file creation
            try {
                // Go to end of the file before adding a new record
                getFile().seek(getFile().length())
                getFile().writeUTF(name)
                getFile().writeInt(quantity)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        @Throws(IOException::class)
        fun getItemStock(symbol: Char): Int {
            val name = when (symbol) {
                'F' -> "Fork"
                'K' -> "Knife"
                'N' -> "Napkin"
                'S' -> "Spoon"
                else -> {
                    println("An error occurred")
                    exitProcess(1)
                }
            }
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val quantity = getFile().readInt()
                if (itemName == name) {
                    return quantity
                }
            }
            // If item was not found, throw exception
            val errorMessage = "$name is not an item in the inventory"
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE)
            throw IllegalArgumentException(errorMessage)
        }

        @Throws(IOException::class)
        fun setItemStock(symbol: Char, quantity: Int) {
            val name = when (symbol) {
                'F' -> "Fork"
                'K' -> "Knife"
                'N' -> "Napkin"
                'S' -> "Spoon"
                else -> {
                    println("An error occurred")
                    exitProcess(1)
                }
            }
            // Seek to the beginning of the file
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val currentQuantity = getFile().readInt()
                if (itemName == name) {
                    // Found the item, update the quantity and write it back to the file
                    getFile().seek(getFile().filePointer - 4) // Move back to the quantity field
                    getFile().writeInt(quantity)
                    return
                }
            }
            // If item was not found, throw an exception
            val errorMessage = "$name is not an item in the inventory"
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE)
            throw IllegalArgumentException(errorMessage)
        }

        @Throws(IOException::class)
        fun restockInventory() {
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName: String = getFile().readUTF()
                val currentQuantity: Int = getFile().readInt()
                if (itemName != "TotalSales") {
                    getFile().seek(getFile().filePointer - 4) // Move back to the quantity field
                    getFile().writeInt(20) //update to full quantity
                }
            }
        }

        @Throws(IOException::class)
        fun getFunds(): Double {
            var totalFunds = 0.0
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val quantity = getFile().readInt()
                if (itemName == "TotalSale") {
                    totalFunds = quantity.toDouble()
                }
            }
            return totalFunds
        }

        fun setFunds(funds: Double) {
            // Seek to the beginning of the file
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val quantity = getFile().readInt()
                if (itemName == "TotalSales") {
                    // Found the item, update the quantity and write it back to the file
                    getFile().seek(getFile().filePointer - 4) // Move back to the quantity field
                    getFile().writeInt(funds.toInt())
                    JOptionPane.showMessageDialog(
                            null,
                            "Total Sales were successfully updated",
                            "Update Successful",
                            JOptionPane.ERROR_MESSAGE
                    )
                    return
                }
            }
        }

        fun closeFile() {
            getFile().close()
        }
    }
}