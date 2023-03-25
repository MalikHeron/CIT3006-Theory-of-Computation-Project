import ui.MainScreen
import javax.swing.UIManager

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        MainScreen()
        //Dialog.summaryDialog()
        //println(Inventory.getFunds())
    }
}