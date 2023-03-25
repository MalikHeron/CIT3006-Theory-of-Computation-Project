package ui

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
    private var stockLabel = JLabel("Items out of stock")
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
    private var summaryDialog = JDialog()

    private var input = ""
    private var output = ""
    private var refund = 0
    private val items = hashMapOf(
        'N' to 0,
        'K' to 0,
        'F' to 0,
        'S' to 0
    )
    private val isOutOfStock = hashMapOf(
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
        summaryDialog.add(outputLabel)
        summaryDialog.add(outputField)
        summaryDialog.add(inputLabel)
        summaryDialog.add(inputField)
        summaryDialog.add(dispenseLabel)
        summaryDialog.add(knifeLabel)
        summaryDialog.add(knifeField)
        summaryDialog.add(forkLabel)
        summaryDialog.add(forkField)
        summaryDialog.add(spoonLabel)
        summaryDialog.add(spoonField)
        summaryDialog.add(napkinLabel)
        summaryDialog.add(napkinField)
        summaryDialog.add(refundLabel)
        summaryDialog.add(stockLabel)
        summaryDialog.add(forkCheckBox)
        summaryDialog.add(knifeCheckBox)
        summaryDialog.add(napkinCheckBox)
        summaryDialog.add(spoonCheckBox)
        summaryDialog.add(refundField)
        summaryDialog.add(closeButton)
    }

    //Setting Window Properties
    private fun setWindowProperties() {
        summaryDialog.layout = null
        summaryDialog.title = "Transaction Results"
        summaryDialog.setSize(500, 550)
        summaryDialog.setLocationRelativeTo(null)
        summaryDialog.isResizable = false
        summaryDialog.isModal = true
        summaryDialog.isVisible = true
    }

    fun setOutOfStock(item: Char) {
        isOutOfStock[item] = true
    }

    fun setRefund(refund: Int) {
        this.refund = refund
    }

    private fun getRefund(): Int {
        return refund
    }

    fun setDispense(item: Char) {
        val count = items[item] ?: 0
        items[item] = count + 1
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

    fun updateFields() {
        outputField.text = getOutput()
        inputField.text = getInput()
        refundField.text = getRefund().toString()
        forkField.text = items['F'].toString()
        knifeField.text = items['K'].toString()
        napkinField.text = items['N'].toString()
        spoonField.text = items['S'].toString()
        forkCheckBox.isSelected = isOutOfStock['F']!!
        knifeCheckBox.isSelected = isOutOfStock['K']!!
        napkinCheckBox.isSelected = isOutOfStock['N']!!
        spoonCheckBox.isSelected = isOutOfStock['S']!!
    }

    private fun resetFields() {
        setRefund(0)
        setOutput("")
        setInput("")
        items['F'] = 0
        items['K'] = 0
        items['N'] = 0
        items['S'] = 0
        forkCheckBox.isSelected = false
        knifeCheckBox.isSelected = false
        napkinCheckBox.isSelected = false
        spoonCheckBox.isSelected = false
        isOutOfStock['F'] = false
        isOutOfStock['K'] = false
        isOutOfStock['N'] = false
        isOutOfStock['M'] = false
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (e?.source === closeButton) {
            resetFields()
            summaryDialog.dispose()
        }
    }
}