import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.RandomAccessFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.JOptionPane

class Inventory {
    companion object {
        private var file = RandomAccessFile("data//inventory.txt", "rw")
        private val items = mutableMapOf(
            'F' to "Fork",
            'N' to "Napkin",
            'S' to "Spoon",
            'K' to "Knife"
        )
        private val positions = mutableMapOf(
            'F' to 0L,
            'N' to 29L,
            'S' to 45L,
            'K' to 14L
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
            val position = positions[symbol]

            //Seek position of item
            getFile().seek(position!!)

            val itemName = getFile().readUTF()
            if (itemName == name) {
                // Found the item, update the quantity and write it back to the file
                getFile().seek(getFile().filePointer + 4) // Move back to the quantity field
                return getFile().readInt()
            } else {
                println("An error occurred while reading the file.")
                // If item was not found, throw an exception
                val errorMessage = "$name is not an item in the inventory"
                JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE)
                throw IllegalArgumentException(errorMessage)
            }
        }

        @Throws(IOException::class)
        fun setItemStock(symbol: Char, quantity: Int) {
            val name = items[symbol]
            val position = positions[symbol]

            //Seek position of item
            getFile().seek(position!!)

            val itemName = getFile().readUTF()
            if (itemName == name) {
                // Found the item, update the quantity and write it back to the file
                getFile().seek(getFile().filePointer + 4) // Move back to the quantity field
                getFile().writeInt(quantity)
                return
            } else {
                println("An error occurred while reading the file.")
                // If item was not found, throw an exception
                val errorMessage = "$name is not an item in the inventory"
                JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE)
                throw IllegalArgumentException(errorMessage)
            }
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
            val position = positions[symbol]

            //Seek position of item
            getFile().seek(position!!)

            val itemName = getFile().readUTF()
            if (itemName == name) {
                return getFile().readInt().toDouble()
            } else {
                println("An error occurred while reading the file.")
                // If item was not found, throw an exception
                val errorMessage = "$name is not an item in the inventory"
                JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE)
                throw IllegalArgumentException(errorMessage)
            }
        }

        @Throws(IOException::class)
        fun getFunds(): Double {
            // Seek to the quantity field of the file
            getFile().seek(76)
            return getFile().readInt().toDouble()
        }

        fun resetFunds() {
            // Seek to the quantity field of the file
            getFile().seek(76)
            getFile().writeInt(0)
            JOptionPane.showMessageDialog(
                null,
                "Till emptied and items restocked.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE
            )
        }

        fun setFunds(funds: Double) {
            val registerMachine = Register()
            // Seek to the quantity field of the file
            getFile().seek(76)

            val quantity = getFile().readInt()
            // Move back to the quantity field from the EOF
            getFile().seek(getFile().filePointer - 4)

            registerMachine.resetAllRegisters()
            registerMachine.CalculateSales(arrayOf("LOAD $quantity 1", "LOAD ${funds.toInt()} 2"))

            val newQuantity = registerMachine.getRegisterValue(0) ?: 0
            getFile().writeInt(newQuantity)

            return
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

        private fun getFile(): RandomAccessFile {
            return file
        }

        @Throws(IOException::class)
        fun storeTransaction(){
            val now = LocalDateTime.now()
            val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            val date = now.format(dateFormatter)
            val time = now.format(timeFormatter)
            val sales = getFunds()
            var outFileStream: FileWriter? = null
            try {
                outFileStream = FileWriter(File("data//sales.txt"), true)
                val newTransaction: String = (sales.toString() + "\t" + date + "\t" + time + "\n")
                outFileStream.write(newTransaction)
            } catch (e: Exception) {
                println("\nAn unexpected error occurred while saving the transaction.")
            } finally {
                if (outFileStream != null) {
                    try {
                        outFileStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}