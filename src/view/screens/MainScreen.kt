package view.screens

import java.awt.*
import java.awt.FlowLayout.CENTER
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import javax.swing.*
import javax.swing.border.BevelBorder


const val WINDOW_WIDTH = 900
const val WINDOW_HEIGHT = 700
const val HALF_WINDOW = WINDOW_WIDTH / 2

class MainScreen() : JFrame(), ActionListener {
    private var leftPanel = JPanel(FlowLayout(CENTER, 0, 0))
    private var rightPanel = JPanel(FlowLayout(CENTER, 10, 10))
    private lateinit var machineImage: JLabel
    private lateinit var inputDisplay: JTextField
    private lateinit var alphaButton: JButton
    private lateinit var betaButton: JButton
    private lateinit var gammaButton: JButton
    private lateinit var deltaButton: JButton
    private lateinit var epsilonButton: JButton
    private lateinit var nButton: JButton
    private lateinit var kButton: JButton
    private lateinit var sButton: JButton
    private lateinit var fButton: JButton
    private lateinit var zButton: JButton
    private lateinit var wButton: JButton
    private lateinit var aButton: JButton
    private lateinit var deleteButton: JButton
    private lateinit var enterButton: JButton

    private var buttons = arrayListOf<JButton>()

    init {
        setupComponents()
        addComponentsToPanels()
        setupWindow()
    }

    private fun setupComponents(){
        leftPanel.size = Dimension(HALF_WINDOW, WINDOW_HEIGHT)
        leftPanel.background = Color.DARK_GRAY
        leftPanel.alignmentY = CENTER_ALIGNMENT

        rightPanel.size = Dimension(HALF_WINDOW, WINDOW_HEIGHT)
        rightPanel.background = Color.GRAY


        val image = ImageIcon(Objects.requireNonNull(javaClass.getResource("/res/vendingMachineWindow_900.png")))
        machineImage = JLabel(image)
        machineImage.verticalAlignment = JLabel.CENTER
        machineImage.size = Dimension(HALF_WINDOW, WINDOW_HEIGHT)

        inputDisplay = JTextField("")
        inputDisplay.preferredSize = Dimension(HALF_WINDOW - 30, 70)
        inputDisplay.background = Color.decode("#c8d96a")
        inputDisplay.isEditable = false
        inputDisplay.border = BevelBorder(BevelBorder.LOWERED)
        inputDisplay.font = Font(Font.SANS_SERIF, Font.BOLD, 30)

        alphaButton = JButton("$5")
        betaButton = JButton("$10")
        gammaButton = JButton("$20")
        deltaButton = JButton("$50")
        epsilonButton = JButton("$100")

        nButton = JButton("N")
        kButton = JButton("K")
        sButton = JButton("S")
        fButton = JButton("F")
        zButton = JButton("Z")
        wButton = JButton("W")
        aButton = JButton("A")

        deleteButton = JButton("Delete")
        enterButton= JButton("ENTER")

        buttons = arrayListOf(
            alphaButton,
            betaButton,
            gammaButton,
            deltaButton,
            epsilonButton,
            nButton,
            kButton,
            sButton,
            fButton,
            zButton,
            wButton,
            aButton,
            deleteButton,
            enterButton,
        )
        // loop to shorten code
        for (btn in buttons){
            btn.preferredSize = Dimension(130, 70)
            btn.background = Color.LIGHT_GRAY
            btn.isFocusPainted = false
            btn.border = BevelBorder(BevelBorder.RAISED)
            btn.font = Font(Font.SANS_SERIF, Font.BOLD, 30)
            btn.addActionListener(this)
        }

        deleteButton.preferredSize = Dimension(200, 70)
        deleteButton.background = Color.decode("#d60f34")
        enterButton.preferredSize = Dimension(200, 70)
        enterButton.background = Color.decode("#289946")
    }

    private fun addComponentsToPanels(){
        leftPanel.add(machineImage)

        val space = JLabel(" ")
        space.preferredSize = Dimension(WINDOW_WIDTH, 60)
        rightPanel.add(space)
        rightPanel.add(inputDisplay)

        for(btn in buttons){
            rightPanel.add(btn)
        }

        // add panels to jframe
        this.add(leftPanel)
        this.add(rightPanel)
    }

    private fun setupWindow(){
        this.title = "Turing Vending Machine"
        this.defaultCloseOperation = EXIT_ON_CLOSE
//        this.size = Dimension(WINDOW_WIDTH, WINDOW_HEIGHT + 100)
        this.contentPane.preferredSize = Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)
        this.pack()
        this.layout = GridLayout(1,2)
        this.isResizable = false
        this.isVisible = true
    }

    override fun actionPerformed(e: ActionEvent?) {
        // clear error
        if(inputDisplay.text == "NO INPUT")
            inputDisplay.text = ""

        if(e?.source == alphaButton){
            inputDisplay.text = inputDisplay.text + "α"
        }
        else if(e?.source == betaButton){
            inputDisplay.text = inputDisplay.text + "β"
        }
        else if(e?.source == gammaButton){
            inputDisplay.text = inputDisplay.text + "γ"
        }
        else if(e?.source == deltaButton){
            inputDisplay.text = inputDisplay.text + "δ"
        }
        else if(e?.source == epsilonButton){
            inputDisplay.text = inputDisplay.text + "ε"
        }
        else if(e?.source == deleteButton){
            inputDisplay.text = inputDisplay.text.dropLast(1)
        }
        else if(e?.source == enterButton){
            if(inputDisplay.text.isEmpty()){
                inputDisplay.text = "NO INPUT"
                return
            }

            // send input to machine
        }
        else{
            inputDisplay.text = inputDisplay.text + (e?.source as? JButton)?.text
        }
    }

}