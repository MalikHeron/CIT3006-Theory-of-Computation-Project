package models.filing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JOptionPane;

public class Inventory {
    private RandomAccessFile file;

    public Inventory() {
        try {
            setFile(new RandomAccessFile("inventory.txt", "rw")); //NTS: revisit for specifying paths based on project structure
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Getters and Setters
    public RandomAccessFile getFile() {
        return file;
    }

    public void setFile(RandomAccessFile file) {
        this.file = file;
    }

    public void addItem(String name, double price, int quantity) {
        try {
            // Go to end of the file before adding a new record
            getFile().seek(getFile().length());
            getFile().writeUTF(name);
            getFile().writeDouble(price);
            getFile().writeInt(quantity);
            getFile().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method Used to update quantity
    public void updateQuantity(String name, int quantity) throws IOException {
        // Seek to the beginning of the file
        getFile().seek(0);
        // Searching file for item specified
        while (getFile().getFilePointer() < getFile().length()) {
            String itemName = getFile().readUTF();
            double price = getFile().readDouble();
            int currentQuantity = getFile().readInt();
            if (itemName.equals(name)) {
                // Found the item, update the quantity and write it back to the file
                getFile().seek(getFile().getFilePointer() - 4); // Move back to the quantity field
                getFile().writeInt(quantity); //decrementing by 1 may need to change for restocking
                getFile().close();
                return;
            }
        }
        getFile().close();
        // If item was not found, throw an exception
        String errorMessage = name + " is not an item in the inventory";
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        throw new IllegalArgumentException(errorMessage);
    }

    //Method Used for restocking a single item NTS: could do a restockAll() instead. Find out if full stock is same
    // quantity for all items
    public void restockAll() throws IOException {
        getFile().seek(0);
        // Searching file for item specified
        while (getFile().getFilePointer() < getFile().length()) {
            String itemName = getFile().readUTF();
            double price = getFile().readDouble();
            int currentQuantity = getFile().readInt();
            // Found the item, update the quantity and write it back to the file
            getFile().seek(getFile().getFilePointer() - 4); // Move back to the quantity field
            getFile().writeInt(20); //update to full quantity
        }
        getFile().close();
    }

    //Method used to retrieve quantity of remaining items
    public int getQuantity(String name) throws IOException {
        getFile().seek(0);
        // Searching file for item specified
        while (getFile().getFilePointer() < getFile().length()) {
            String itemName = getFile().readUTF();
            double price = getFile().readDouble();
            int quantity = getFile().readInt();
            if (itemName.equals(name)) {
                getFile().close();
                return quantity;
            }
        }
        getFile().close();
        // If item was not found, throw exception
        String errorMessage = name + " is not an item in the inventory";
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        throw new IllegalArgumentException(errorMessage);
    }

    //Reads entire inventory for all details if necessary
    public String displayInventory() throws IOException {
        StringBuilder inventory = new StringBuilder();
        getFile().seek(0);
        while (getFile().getFilePointer() < getFile().length()) {
            String itemName = getFile().readUTF();
            double price = getFile().readDouble();
            int quantity = getFile().readInt();
            inventory.append("Name: ").append(itemName).append("\n")
                    .append("Price: $").append(price).append("\n")
                    .append("Quantity: ").append(quantity).append("\n\n");
        }
        getFile().close();
        return inventory.toString();
    }
}
