import ui.MainScreen
import javax.swing.UIManager

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Inventory.initializeFiles()
        MainScreen()
    }
}