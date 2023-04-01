import ui.MainScreen
import javax.swing.UIManager

fun main() {
    try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())
    } catch (e: Exception) {
        e.printStackTrace()
    }
    Inventory.initializeFiles()
    MainScreen()
}