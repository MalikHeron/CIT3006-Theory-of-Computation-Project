package view.screens

import java.awt.Dimension
import javax.swing.JFrame

class MainScreen : JFrame() {

    init {
        setupWindow()
    }

    private fun setupWindow(){
        this.title = "Vending Machine"
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.size = Dimension(800, 600)
        this.isVisible = true
    }
}