package view.screens

import java.awt.Dimension
import javax.swing.JFrame

class MainScreen: JFrame {

    constructor(){
        setupWindow()
    }

    fun setupWindow(){
        this.title = "Turing Vending Machine"
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.size = Dimension(800, 600)
        this.isVisible = true
    }

}