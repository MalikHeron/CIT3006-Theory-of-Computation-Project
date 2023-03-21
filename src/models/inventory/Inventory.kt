package models.inventory

import models.turing.Register
import java.io.IOException
import java.io.RandomAccessFile
import javax.swing.JOptionPane

class Inventory {
    companion object {
        private var file = RandomAccessFile("files//inventory.txt", "rw")
        private val items = mutableMapOf(
            'F' to "Fork",
            'N' to "Napkin",
            'S' to "Spoon",
            'K' to "Knife"
        )

        @Throws(IOException::class)
        fun addItem(name: String, price: Int, quantity: Int) {
            try {
                // Go to end of the file before adding a new record
                getFile().seek(getFile().length())
                getFile().writeUTF(name)
                getFile().writeInt(price)
                getFile().writeInt(quantity)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        @Throws(IOException::class)
        fun getItemStock(symbol: Char): Int {
            val name = items[symbol]
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val price = getFile().readInt()
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
            val name = items[symbol]
            // Seek to the beginning of the file
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val price = getFile().readInt()
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
            val items = arrayOf("Fork", "Knife", "Napkin", "Spoon")
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val price = getFile().readInt()
                val currentQuantity = getFile().readInt()
                if (items.contains(itemName)) {
                    // Found the item, update the quantity and write it back to the file
                    getFile().seek(getFile().filePointer - 4) // Move back to the quantity field
                    getFile().writeInt(20)
                } else {
                    return
                }
            }
        }

        @Throws(IOException::class)
        fun getPrice(symbol: Char): Double {
            val name = items[symbol]
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val price = getFile().readInt()
                val quantity = getFile().readInt()
                if (itemName == name) {
                    getFile().seek(0)
                    return price.toDouble()
                }
            }
            return 0.0
        }

        @Throws(IOException::class)
        fun getFunds(): Double {
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val price = getFile().readInt()
                val quantity = getFile().readInt()
                if (itemName == "TotalSales") {
                    getFile().seek(0)
                    return quantity.toDouble()
                }
            }
            return 0.0
        }

        fun resetFunds() {
            // Seek to the beginning of the file
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val price = getFile().readInt()
                val quantity = getFile().readInt()
                if (itemName == "TotalSales") {
                    // Found the item, update the quantity and write it back to the file
                    getFile().seek(getFile().filePointer - 4) // Move back to the quantity field
                    getFile().writeInt(0)
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

        fun setFunds(funds: Double) {
            val registerMachine = Register()
            // Seek to the beginning of the file
            getFile().seek(0)
            // Searching file for item specified
            while (getFile().filePointer < getFile().length()) {
                val itemName = getFile().readUTF()
                val price = getFile().readInt()
                val quantity = getFile().readInt()
                if (itemName == "TotalSales") {
                    // Found the item, update the quantity and write it back to the file
                    getFile().seek(getFile().filePointer - 4) // Move back to the quantity field

                    registerMachine.resetAllRegisters()
                    registerMachine.setRegisterValue(0, quantity)
                    registerMachine.setRegisterValue(1, funds.toInt())
                    registerMachine.run(arrayOf("ADD 0 1"))

                    val newQuantity = registerMachine.getRegisterValue(0) ?: 0

                    getFile().writeInt(newQuantity)
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

        //Reads entire inventory for all details if necessary
        @Throws(IOException::class)
        fun displayInventory(): String {
            val inventory = StringBuilder()
            file.seek(0)
            while (file.filePointer < file.length()) {
                val itemName = file.readUTF()
                val price = file.readDouble()
                val quantity = file.readInt()
                inventory.append("Name: ").append(itemName).append("\n")
                    .append("Price: $").append(price).append("\n")
                    .append("Quantity: ").append(quantity).append("\n\n")
            }
            file.close()
            return inventory.toString()
        }

        fun getFile(): RandomAccessFile {
            return file
        }
    }
}