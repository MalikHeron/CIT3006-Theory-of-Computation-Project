package ui

import Inventory
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JTextField

class SalesDialog : ActionListener {

    //JTextFields
    private var salesField = JTextField()
    private var forkField = JTextField()
    private var knifeField = JTextField()
    private var spoonField = JTextField()
    private var napkinField = JTextField()

    //JLabels
    private var salesLabel = JLabel("Total Sales: $")
    private var stockLabel = JLabel("Remaining stock")
    private var forkLabel = JLabel("Fork")
    private var knifeLabel = JLabel("Knife")
    private var spoonLabel = JLabel("Spoon")
    private var napkinLabel = JLabel("Napkin")

    //JButtons
    private var closeButton = JButton("Close")

    //JDialog
    private var dialog = JDialog()

    private fun initializeComponents() {
        val smlLabelFont = Font("SANS_SERIF", Font.PLAIN, 16)
        val lrgLabelFont = Font("SANS_SERIF", Font.PLAIN, 18)
        val fieldFont = Font("SANS_SERIF", Font.PLAIN, 16)
        val headerFont = Font("SANS_SERIF", Font.BOLD, 18)

        //JLabel properties
        salesLabel.font = lrgLabelFont
        salesLabel.setBounds(20, 20, 300, 30)

        stockLabel.font = headerFont
        stockLabel.setBounds(170, 70, 300, 30)

        forkLabel.font = smlLabelFont
        forkLabel.setBounds(20, 120, 300, 30)

        knifeLabel.font = smlLabelFont
        knifeLabel.setBounds(20, 170, 300, 30)

        napkinLabel.font = smlLabelFont
        napkinLabel.setBounds(300, 120, 300, 30)

        spoonLabel.font = smlLabelFont
        spoonLabel.setBounds(300, 170, 300, 30)

        //JTextFields
        salesField.font = fieldFont
        salesField.setBounds(150, 20, 90, 30)
        salesField.isEditable = false

        forkField.font = fieldFont
        forkField.setBounds(90, 120, 30, 30)
        forkField.isEditable = false

        knifeField.font = fieldFont
        knifeField.setBounds(90, 170, 30, 30)
        knifeField.isEditable = false

        napkinField.font = fieldFont
        napkinField.setBounds(390, 120, 30, 30)
        napkinField.isEditable = false

        spoonField.font = fieldFont
        spoonField.setBounds(390, 170, 30, 30)
        spoonField.isEditable = false

        //Button properties
        closeButton.font = smlLabelFont
        closeButton.setBounds(170, 270, 150, 30)
        closeButton.isFocusPainted = false
        closeButton.addActionListener(this)
    }

    private fun addComponentsToWindow() {
        dialog.add(salesLabel)
        dialog.add(salesField)
        dialog.add(stockLabel)
        dialog.add(knifeLabel)
        dialog.add(knifeField)
        dialog.add(forkLabel)
        dialog.add(forkField)
        dialog.add(spoonLabel)
        dialog.add(spoonField)
        dialog.add(napkinLabel)
        dialog.add(napkinField)
        dialog.add(closeButton)
    }

    //Setting Window Properties
    private fun setWindowProperties() {
        dialog.layout = null
        dialog.title = "System Information"
        dialog.setSize(500, 350)
        dialog.setLocationRelativeTo(null)
        dialog.isResizable = false
        dialog.isModal = true
        dialog.isVisible = true
    }

    fun showDialog() {
        initializeComponents()
        addComponentsToWindow()
        updateFields()
        setWindowProperties()
    }

    private fun updateFields() {
        salesField.text = Inventory.getFunds().toString()
        forkField.text = Inventory.getItemStock('F').toString()
        knifeField.text = Inventory.getItemStock('K').toString()
        napkinField.text = Inventory.getItemStock('N').toString()
        spoonField.text = Inventory.getItemStock('S').toString()
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (e?.source == closeButton) {
            //Store the results
            Inventory.storeTransaction()
            //Restock the inventory
            Inventory.restockInventory()
            //Empty the till
            Inventory.resetFunds()
            //Close the dialog
            dialog.dispose()
        }
    }
}