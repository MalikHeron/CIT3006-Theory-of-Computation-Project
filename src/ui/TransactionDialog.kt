package ui

import Inventory
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class TransactionDialog : ActionListener {

    //JTextFields
    private var inputField = JTextField()
    private var outputField = JTextField()
    private var refundField =JTextField()
    private var forkField = JTextField()
    private var knifeField = JTextField()
    private var spoonField = JTextField()
    private var napkinField = JTextField()

    //JLabels
    private var inputLabel = JLabel("Input: ")
    private var outputLabel = JLabel("Output: ")
    private var refundLabel = JLabel("Refund: $")
    private var dispenseLabel = JLabel("Items Dispensed")
    private var stockLabel = JLabel("Items in stock")
    private var forkLabel = JLabel("Fork")
    private var knifeLabel = JLabel("Knife")
    private var spoonLabel = JLabel("Spoon")
    private var napkinLabel = JLabel("Napkin")

    //JCheckBox
    private val forkCheckBox = JCheckBox("Fork")
    private val napkinCheckBox = JCheckBox("Napkin")
    private val spoonCheckBox = JCheckBox("Spoon")
    private val knifeCheckBox = JCheckBox("Knife")

    //JButtons
    private var closeButton = JButton("Close")

    //JDialog
    private var dialog = JDialog()

    private var input = ""
    private var output = ""
    private var refund = 0
    private val items = mutableMapOf(
        'F' to "Fork",
        'N' to "Napkin",
        'S' to "Spoon",
        'K' to "Knife"
    )
    private val dispenseList = hashMapOf(
        'F' to 0,
        'K' to 0,
        'N' to 0,
        'S' to 0
    )
    private val insufficientList = hashMapOf(
        'F' to false,
        'K' to false,
        'N' to false,
        'S' to false
    )

    private fun initializeComponents() {
        val smlLabelFont = Font("SANS_SERIF", Font.PLAIN, 16)
        val lrgLabelFont = Font("SANS_SERIF", Font.PLAIN, 18)
        val fieldFont = Font("SANS_SERIF", Font.PLAIN, 16)
        val headerFont = Font("SANS_SERIF", Font.BOLD, 18)

        //JLabel properties
        inputLabel.font = lrgLabelFont
        inputLabel.setBounds(20, 20, 300, 30)

        outputLabel.font = lrgLabelFont
        outputLabel.setBounds(20, 70, 300, 30)

        dispenseLabel.font = headerFont
        dispenseLabel.setBounds(170, 120, 300, 30)

        forkLabel.font = smlLabelFont
        forkLabel.setBounds(20, 170, 300, 30)

        knifeLabel.font = smlLabelFont
        knifeLabel.setBounds(20, 220, 300, 30)

        napkinLabel.font = smlLabelFont
        napkinLabel.setBounds(300, 170, 300, 30)

        spoonLabel.font = smlLabelFont
        spoonLabel.setBounds(300, 220, 300, 30)

        stockLabel.font = headerFont
        stockLabel.setBounds(170, 260, 300, 30)

        refundLabel.font = lrgLabelFont
        refundLabel.setBounds(20, 410, 300, 30)

        //JCheckBox
        forkCheckBox.font = smlLabelFont
        forkCheckBox.setBounds(20, 300, 300, 30)
        forkCheckBox.isEnabled = false

        knifeCheckBox.font = smlLabelFont
        knifeCheckBox.setBounds(20, 350, 300, 30)
        knifeCheckBox.isEnabled = false

        napkinCheckBox.font = smlLabelFont
        napkinCheckBox.setBounds(350, 300, 300, 30)
        napkinCheckBox.isEnabled = false

        spoonCheckBox.font = smlLabelFont
        spoonCheckBox.setBounds(350, 350, 300, 30)
        spoonCheckBox.isEnabled = false

        //JTextFields
        inputField.font = fieldFont
        inputField.setBounds(90, 20, 200, 30)
        inputField.isEditable = false

        outputField.font = fieldFont
        outputField.setBounds(90, 70, 200, 30)
        outputField.isEditable = false

        forkField.font = fieldFont
        forkField.setBounds(90, 170, 30, 30)
        forkField.isEditable = false

        knifeField.font = fieldFont
        knifeField.setBounds(90, 220, 30, 30)
        knifeField.isEditable = false

        napkinField.font = fieldFont
        napkinField.setBounds(390, 170, 30, 30)
        napkinField.isEditable = false

        spoonField.font = fieldFont
        spoonField.setBounds(390, 220, 30, 30)
        spoonField.isEditable = false

        refundField.font = fieldFont
        refundField.setBounds(120, 410, 60, 30)
        refundField.isEditable = false

        //Button properties
        closeButton.font = smlLabelFont
        closeButton.setBounds(170, 470, 150, 30)
        closeButton.isFocusPainted = false
        closeButton.addActionListener(this)
    }

    private fun addComponentsToWindow() {
        dialog.add(outputLabel)
        dialog.add(outputField)
        dialog.add(inputLabel)
        dialog.add(inputField)
        dialog.add(dispenseLabel)
        dialog.add(knifeLabel)
        dialog.add(knifeField)
        dialog.add(forkLabel)
        dialog.add(forkField)
        dialog.add(spoonLabel)
        dialog.add(spoonField)
        dialog.add(napkinLabel)
        dialog.add(napkinField)
        dialog.add(refundLabel)
        dialog.add(stockLabel)
        dialog.add(forkCheckBox)
        dialog.add(knifeCheckBox)
        dialog.add(napkinCheckBox)
        dialog.add(spoonCheckBox)
        dialog.add(refundField)
        dialog.add(closeButton)
    }

    //Setting Window Properties
    private fun setWindowProperties() {
        dialog.layout = null
        dialog.title = "Transaction Results"
        dialog.setSize(500, 550)
        dialog.setLocationRelativeTo(null)
        dialog.isResizable = false
        dialog.isModal = true
        dialog.isVisible = true
    }

    fun setRefund(refund: Int) {
        this.refund = refund
    }

    private fun getRefund(): Int {
        return refund
    }

    fun setDispense(item: Char) {
        val count = dispenseList[item] ?: 0
        dispenseList[item] = count + 1
    }

    fun setInput(input: String) {
        this.input = input
    }

    private fun getInput(): String {
        return input
    }

    fun setOutput(output: String) {
        this.output = output
    }

    private fun getOutput(): String {
        return output
    }

    fun setInsufficient(symbol: Char) {
        insufficientList[symbol] = true
    }

    fun showDialog() {
        initializeComponents()
        addComponentsToWindow()
        setWindowProperties()
    }

    fun showInvalidInputDialog(symbol: Char) {
        val message = "Invalid input '$symbol' detected"
        JOptionPane.showMessageDialog(
            null,
            message,
            "Invalid Input",
            JOptionPane.WARNING_MESSAGE
        )
    }

    private fun showInsufficientFundsDialog() {
        insufficientList.forEach {
            if (it.value) {
                val message = "Insufficient funds to purchase ${items[it.key]}"
                JOptionPane.showMessageDialog(
                    null,
                    message,
                    "Insufficient Funds",
                    JOptionPane.WARNING_MESSAGE
                )
            }
        }
    }

    fun updateFields() {
        outputField.text = getOutput()
        inputField.text = getInput()
        refundField.text = getRefund().toString()
        forkField.text = dispenseList['F'].toString()
        knifeField.text = dispenseList['K'].toString()
        napkinField.text = dispenseList['N'].toString()
        spoonField.text = dispenseList['S'].toString()
        forkCheckBox.isSelected = Inventory.getItemStock('F') > 0
        knifeCheckBox.isSelected = Inventory.getItemStock('K') > 0
        napkinCheckBox.isSelected = Inventory.getItemStock('N') > 0
        spoonCheckBox.isSelected = Inventory.getItemStock('S') > 0
        showInsufficientFundsDialog()
    }

    private fun resetFields() {
        setRefund(0)
        setOutput("")
        setInput("")
        dispenseList['F'] = 0
        dispenseList['K'] = 0
        dispenseList['N'] = 0
        dispenseList['S'] = 0
        forkCheckBox.isSelected = Inventory.getItemStock('F') > 0
        knifeCheckBox.isSelected = Inventory.getItemStock('K') > 0
        napkinCheckBox.isSelected = Inventory.getItemStock('N') > 0
        spoonCheckBox.isSelected = Inventory.getItemStock('S') > 0
        insufficientList['F'] = false
        insufficientList['K'] = false
        insufficientList['N'] = false
        insufficientList['S'] = false
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (e?.source === closeButton) {
            resetFields()
            dialog.dispose()
        }
    }
}