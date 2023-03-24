package ui

import Turing
import java.awt.*
import java.awt.FlowLayout.CENTER
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import javax.swing.*
import javax.swing.border.BevelBorder
import javax.swing.border.LineBorder
import javax.swing.border.SoftBevelBorder
import javax.swing.border.TitledBorder

const val WINDOW_WIDTH = 900
const val WINDOW_HEIGHT = 700
const val HALF_WINDOW = WINDOW_WIDTH / 2

class MainScreen : JFrame(), ActionListener {
    private var leftPanel = JPanel(null)
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
    private lateinit var inventoryDisplay: JTextArea
    private var buttons = arrayListOf<JButton>()
    private var timesEnterPressed = 0

    init {
        setupComponents()
        addComponentsToPanels()
        setupWindow()
    }

    private fun setupComponents(){
        leftPanel.size = Dimension(HALF_WINDOW, WINDOW_HEIGHT)
        leftPanel.background = Color.DARK_GRAY
        rightPanel.size = Dimension(HALF_WINDOW, WINDOW_HEIGHT)
        rightPanel.background = Color.GRAY

        val image = ImageIcon(Objects.requireNonNull(javaClass.getResource("/vendingMachineWindow_900.png")))
        machineImage = JLabel(image)
        machineImage.size = Dimension(HALF_WINDOW, WINDOW_HEIGHT)
        machineImage.border = LineBorder(Color.BLACK, 12, true)

        inputDisplay = JTextField("")
        inputDisplay.preferredSize = Dimension(HALF_WINDOW - 40, 70)
        inputDisplay.background = Color.decode("#c8d96a")
        inputDisplay.isEditable = false
        inputDisplay.horizontalAlignment = JLabel.CENTER
        inputDisplay.border = SoftBevelBorder(SoftBevelBorder.LOWERED)
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
        deleteButton = JButton("Clear")
        enterButton= JButton("NEXT")

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
            btn.foreground = Color.BLACK
            btn.isFocusPainted = false
            btn.border = BevelBorder(BevelBorder.RAISED)
            btn.font = Font(Font.SANS_SERIF, Font.BOLD, 30)
            btn.addActionListener(this)
        }

        deleteButton.preferredSize = Dimension(200, 70)
        deleteButton.background = Color.decode("#d60f34")
        enterButton.preferredSize = Dimension(200, 70)
        enterButton.background = Color.decode("#289946")

        inventoryDisplay = JTextArea("N - 20\tK - 20\tS - 20\tF - 20")
        inventoryDisplay.preferredSize = Dimension(HALF_WINDOW-40, 60)
        inventoryDisplay.background = Color.GRAY
        inventoryDisplay.isEditable = false
        inventoryDisplay.font = Font(Font.SANS_SERIF, Font.BOLD, 15)
        inventoryDisplay.border = TitledBorder(
            SoftBevelBorder(SoftBevelBorder.LOWERED),
            "ITEMS AVAILABLE",
            TitledBorder.DEFAULT_POSITION,
            TitledBorder.DEFAULT_JUSTIFICATION,
            Font(Font.SANS_SERIF, Font.BOLD, 16)
        )

        setActiveInputType("prices")
    }

    private fun addComponentsToPanels(){
        leftPanel.add(machineImage)

        rightPanel.add(verticalSpace(10))
        rightPanel.add(inventoryDisplay)
        rightPanel.add(verticalSpace(30))
        rightPanel.add(inputDisplay)

        for(btn in buttons){
            rightPanel.add(btn)
        }

        // add panels to frame
        this.add(leftPanel)
        this.add(rightPanel)
    }

    private fun setupWindow(){
        this.title = "Vending Machine"
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.contentPane.preferredSize = Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)
        this.layout = GridLayout(1,2)
        this.pack()
        this.isResizable = false
        this.isVisible = true
    }

    // {type} is "prices" or "items"
    private fun setActiveInputType(type: String) {
        // separate the types of buttons to enable or disable them
        val priceButtons = arrayListOf(alphaButton, betaButton, gammaButton, deltaButton, epsilonButton)
        val itemButtons = arrayListOf(nButton, fButton, sButton, kButton, wButton, zButton, aButton)

        priceButtons.forEach { it.isEnabled = type == "prices" }
        itemButtons.forEach { it.isEnabled = type == "items" }

        enterButton.text = if(type == "prices") "NEXT" else "ENTER"
    }

    override fun actionPerformed(e: ActionEvent?) {
        // clear any errors
        if(inputDisplay.text == "NO INPUT")
            inputDisplay.text = ""

        when(e?.source){
            alphaButton -> inputDisplay.text = inputDisplay.text + "ɑ"
            betaButton -> inputDisplay.text = inputDisplay.text + "β"
            gammaButton -> inputDisplay.text = inputDisplay.text + "γ"
            deltaButton -> inputDisplay.text = inputDisplay.text + "δ"
            epsilonButton -> inputDisplay.text = inputDisplay.text + "ε"
            deleteButton -> {
                setActiveInputType("prices")
                inputDisplay.text = ""
                timesEnterPressed = 0
            }
            enterButton -> {
                if(inputDisplay.text.isEmpty()){
                    Toolkit.getDefaultToolkit().beep()
                    inputDisplay.text = "NO INPUT"
                    return
                }

                // change functionality
                if(timesEnterPressed == 0){
                    // lock price selectors and enable item selectors
                    setActiveInputType("items")
                    timesEnterPressed ++
                }
                else if(timesEnterPressed == 1){
                    handleTuringRequest()
                    setActiveInputType("prices")
                    inputDisplay.text = ""
                    timesEnterPressed = 0
                }
                // process
            }
            else -> inputDisplay.text = inputDisplay.text + (e?.source as? JButton)?.text
        }
    }

    private fun handleTuringRequest(){
        Turing("⊔" + inputDisplay.text + "⊔")

        // display results

        // update stock amounts
        // inventoryDisplay.text = "N - 20\tK - 20\tS - 20\tF - 20"
    }

    private fun verticalSpace(height: Int): JLabel {
        val space = JLabel(" ")
        space.preferredSize = Dimension(WINDOW_WIDTH, height)

        return space
    }

}