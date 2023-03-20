package util

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

        private fun getFile(): RandomAccessFile {
            if (file == null) {
                throw IllegalStateException("File not properly initialized")
            }
            return file!!
        }

        @Throws(IOException::class)
        fun getItemStock(name: Char): Int {//Parameter less, how??
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val quantity = getFile().readInt()
                if (itemName == name.toString()) {
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
        fun setItemStock(name: Char, quantity: Int) {
            // Seek to the beginning of the file
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val currentQuantity = getFile().readInt()
                if (itemName == name.toString()) {
                    // Found the item, update the quantity and write it back to the file
                    getFile().seek(getFile().filePointer - 4) // Move back to the quantity field
                    getFile().writeInt(quantity)
                    getFile().close()
                    return
                }
            }
            getFile().close()
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
                if(itemName != "TotalSales") {
                    getFile().seek(getFile().filePointer - 4) // Move back to the quantity field
                    getFile().writeInt(20) //update to full quantity
                }
            }
            getFile().close()
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
                    getFile().close()
                    totalFunds = quantity.toDouble()
                }
            }
            getFile().close()
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
                    getFile().close()
                    JOptionPane.showMessageDialog(null, "Total Sales were successfully updated", "Update Successful", JOptionPane.ERROR_MESSAGE)
                    return
                }
            }
            getFile().close()
        }


    }
}