package ui

import java.awt.Component
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import java.awt.event.ActionEvent
import javax.swing.*


class Dialog {
    companion object {
        private lateinit var inputField: JTextField
        private lateinit var refundField: JTextField
        private lateinit var qforkField: JTextField
        private lateinit var qknifeField: JTextField
        private lateinit var qspoonField: JTextField
        private lateinit var qNapkinField: JTextField
        private lateinit var inputLabel: JLabel
        private lateinit var refundLabel: JLabel
        private lateinit var dispenseLabel: JLabel
        private  lateinit var stockLabel:JLabel
        private  lateinit var stocks:JLabel
        private lateinit var forkLabel: JLabel
        private lateinit var knifeLabel: JLabel
        private lateinit var spoonLabel: JLabel
        private lateinit var napkinLabel: JLabel
        private lateinit var displayLabel: JLabel
        private lateinit var okButton: JButton
        private lateinit var retryButton: JButton
        private var input = ""
        private lateinit var summaryDialog: JDialog
        private var refund = 0.0
        private var isValid = true
        private val SOLD = hashMapOf(
            'N' to 0,
            'K' to 0,
            'F' to 0,
            'S' to 0
        )
        private val NO_STOCK = hashMapOf<Char, String>()

        private fun initializeComponents() {
            val smlLabelFont = Font("SANS_SERIF", Font.PLAIN, 34)
            val lrgLabelFont = Font("SANS_SERIF", Font.PLAIN, 40)
            val fieldFont = Font("SANS_SERIF", Font.PLAIN, 34)//Remove this
            // Components Configuration
            summaryDialog = JDialog()
            displayLabel = JLabel("Machine Output", SwingConstants.CENTER)
            displayLabel.font = lrgLabelFont
            displayLabel.preferredSize = Dimension(700, 65)
            displayLabel.verticalAlignment = SwingConstants.CENTER

            inputLabel = JLabel("Input String")
            inputLabel.font = lrgLabelFont
            inputLabel.preferredSize = Dimension(300, 65)
            inputLabel.verticalAlignment = SwingConstants.CENTER

            inputField = JTextField(input)
            inputField.font = fieldFont
            inputField.preferredSize = Dimension(380, 60)

            dispenseLabel = JLabel("Dispensing...", SwingConstants.CENTER)
            dispenseLabel.font = lrgLabelFont
            dispenseLabel.preferredSize = Dimension(700, 65)
            dispenseLabel.verticalAlignment = SwingConstants.CENTER

            knifeLabel = JLabel("Knife", SwingConstants.CENTER)
            knifeLabel.font = fieldFont
            knifeLabel.preferredSize = Dimension(325, 60)

            qknifeField = JTextField(SOLD['K'].toString())
            qknifeField.font = fieldFont
            qknifeField.preferredSize = Dimension(150, 60)
            qknifeField.horizontalAlignment = SwingConstants.CENTER

            forkLabel = JLabel("Fork", SwingConstants.CENTER)
            forkLabel.font = smlLabelFont
            forkLabel.preferredSize = Dimension(325, 60)

            qforkField = JTextField(SOLD['F'].toString())
            qforkField.font = fieldFont
            qforkField.preferredSize = Dimension(150, 60)
            qforkField.horizontalAlignment = SwingConstants.CENTER

            spoonLabel = JLabel("Spoon", SwingConstants.CENTER)
            spoonLabel.font = smlLabelFont
            spoonLabel.preferredSize = Dimension(325, 60)

            qspoonField = JTextField(SOLD['S'].toString())
            qspoonField.font = fieldFont
            qspoonField.preferredSize = Dimension(150, 60)
            qspoonField.horizontalAlignment = SwingConstants.CENTER

            napkinLabel = JLabel("Napkin", SwingConstants.CENTER)
            napkinLabel.font = smlLabelFont
            napkinLabel.preferredSize = Dimension(325, 60)

            qNapkinField = JTextField(SOLD['N'].toString())
            qNapkinField.font = fieldFont //NTS: MIXING OF FONTS,Keep this one
            qNapkinField.preferredSize = Dimension(150, 60)
            qNapkinField.horizontalAlignment = SwingConstants.CENTER

            stockLabel = JLabel("Out of Stock") // Change Text to 'change' based on payment processing
            stockLabel.font = lrgLabelFont
            stockLabel.preferredSize = Dimension(300, 65)
            stockLabel.verticalAlignment = SwingConstants.CENTER

            stocks = JLabel() // Change Text to 'change' based on payment processing
            stocks.font = lrgLabelFont
            stocks.preferredSize = Dimension(300, 65)
            stocks.verticalAlignment = SwingConstants.CENTER

            refundLabel = JLabel("Refund/Change")
            refundLabel.font = lrgLabelFont
            refundLabel.preferredSize = Dimension(300, 65)
            refundLabel.verticalAlignment = SwingConstants.CENTER

            refundField = JTextField(getRefund().toString())
            refundField.font = fieldFont
            refundField.preferredSize = Dimension(380, 60)

            //Button properties
            okButton = JButton("OK")
            okButton.preferredSize = Dimension(300, 60)
            okButton.font = smlLabelFont
            okButton.alignmentX = Component.CENTER_ALIGNMENT
            okButton.horizontalAlignment = SwingConstants.CENTER

            val allFields: Array<JTextField> = arrayOf(inputField, refundField, qforkField, qknifeField, qspoonField, qNapkinField)
            for (field in allFields) {
                field.isEnabled = false
            }
            // Use a timer to update the values of the sold items
            val timer = Timer(200) { e ->
            // Increment the values and update the text fields
                var soldKnife = SOLD['K']!!
                var knives = Integer.valueOf(qknifeField.text)
                if(soldKnife > knives) {
                    knives += 1
                    qknifeField.text = knives.toString()
                }

                var soldFork = SOLD['F']!!
                var fork = Integer.valueOf(qforkField.text)
                if(soldFork > fork) {
                    fork += 1
                    qforkField.text = fork.toString()
                }
                var soldSpoon = SOLD['S']!!
                var spoon = Integer.valueOf(qspoonField.text)
                if(soldSpoon > spoon) {
                    spoon += 1
                    qspoonField.text = spoon.toString()
                }

                var soldNapkin = SOLD['N']!!
                var napkin = Integer.valueOf(qNapkinField.text)
                if(soldNapkin > napkin) {
                    napkin += 1
                    qNapkinField.text = napkin.toString()
                }
                // Check if all items are sold out and stop the timer
                if (soldKnife == knives && soldFork == fork &&  soldSpoon == spoon && soldNapkin == napkin) {
                    (e.source as Timer).stop()
                    var OoS = ""
                    for ((_, value) in NO_STOCK) {
                        OoS += "$value, "
                    }
                    stocks.text = OoS
                }
            }
            timer.start()


        }

        private fun addComponentsToWindow() {
            summaryDialog.add(displayLabel)
            summaryDialog.add(inputLabel)
            summaryDialog.add(inputField)
            summaryDialog.add(dispenseLabel)
            summaryDialog.add(knifeLabel)
            summaryDialog.add(qknifeField)
            summaryDialog.add(forkLabel)
            summaryDialog.add(qforkField)
            summaryDialog.add(spoonLabel)
            summaryDialog.add(qspoonField)
            summaryDialog.add(napkinLabel)
            summaryDialog.add(qNapkinField)
            summaryDialog.add(stockLabel)
            summaryDialog.add(stocks)
            summaryDialog.add(refundLabel)
            summaryDialog.add(refundField)
            summaryDialog.add(okButton)
        }


        fun validateInput(item: String) {
            // Check if the input char is invalid
            val validChars = "FKNSɑβγ⊔x"
            for (i in 0 until item.length) {
                if (!validChars.contains(item[i].toString())) {
                    val errorMessage = "Invalid input '$item' detected. Machine Halted and Input rejected"
                    JOptionPane.showMessageDialog(null, errorMessage, "Invalid Input", JOptionPane.ERROR_MESSAGE)
                }
            }
        }

        fun validateInput(item: Char) {
            // Check if the input char is invalid
            val tapeAlphabet = charArrayOf('F', 'K', 'N', 'S', 'ɑ', 'β', 'γ', '⊔', 'x')
            if (!tapeAlphabet.contains(item)) {
                val errorMessage = "Invalid input '$item' detected. Machine Halted and Input rejected"
                JOptionPane.showMessageDialog(null, errorMessage, "Invalid Input", JOptionPane.ERROR_MESSAGE)
            }
        }

        fun setOutOfStock(item: Char) {
            var value = ""
            when (item.toUpperCase()) {
                'N' -> value = "Napkin"
                'F' -> value = "Fork"
                'K' -> value = "Knifes"
                'S' -> value = "Spoon"
            }
            NO_STOCK[item] = value
        }

        fun setRefund(funds: Double) {
            refund += funds
        }

        private fun getRefund(): Double {
            return refund
        }

        fun setDispense(item: Char) {
            val count = SOLD[item] ?: 0
            SOLD[item] = count + 1
        }

        fun summaryDialog() {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
                //Increasing JOptionPane window size as JOptionPane display very small on my machine NTS: Revisit Later
                UIManager.put("OptionPane.minimumSize", Dimension(350, 150))
                UIManager.put("OptionPane.buttonFont", Font("Arial", Font.PLAIN, 25))
                UIManager.put("OptionPane.messageFont", Font("times new roman", Font.PLAIN, 34))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            initializeComponents()
            addComponentsToWindow()
            //registerListeners()
            setWindowProperties()
        }

        fun displayFunds(totalSale: Double) {
            val message = "Vending Machine current total sales is $totalSale"
            JOptionPane.showMessageDialog(null, message, "Restocking Inventory", JOptionPane.INFORMATION_MESSAGE)
        }

        /*Registering listeners
        private fun registerListeners() {
            okButton.addActionListener(this)
        }*/


        //Setting Window Properties
        private fun setWindowProperties() {
            summaryDialog.layout = FlowLayout(FlowLayout.LEFT, 30, 30)
            summaryDialog.title = "TRANSACTION COMPLETE"
            summaryDialog.setSize(820, 850) //820 [500 or 950]
            summaryDialog.setLocationRelativeTo(null)
            summaryDialog.isResizable = false
            summaryDialog.isModal = true
            summaryDialog.isVisible = true
        }

        fun actionPerformed(e: ActionEvent) {
            if (e.source === okButton) {
                summaryDialog.dispose()
            }
        }

    }

}