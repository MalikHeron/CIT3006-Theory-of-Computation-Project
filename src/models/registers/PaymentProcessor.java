package models.registers;

import models.filing.Inventory;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PaymentProcessor {//NTS: You need to tie function together
    private static final Map<Character, Integer> ITEM_PRICES = new HashMap<Character, Integer>() {{
        put('N', 10);
        put('K', 25);
        put('F', 15);
        put('S', 20);
    }};
    private static final Map<Character, Integer> SOLD = new HashMap<Character, Integer>() {{
        put('N', 0);
        put('K', 0);
        put('F', 0);
        put('S', 0);
    }};

    private static final Map<Character, Integer> Requested_ITEMS = new HashMap<Character, Integer>() {{
        put('N', 0);
        put('K', 0);
        put('F', 0);
        put('S', 0);
    }};

    private static final Map<Character, Integer> MONEY_VALUES = new HashMap<Character, Integer>() {{
        put('α', 5);
        put('β', 10);
        put('γ', 20);
    }};
    private int refund = 0;
    private String itemsNotSold = "";
    private String itemsSold = "";
    private int totalSpent = 0;
    private String input = ""; //where input is the user input string
    private Inventory inventory;



    //NTS: Input validation is being done in "receipt" dialog. it must be taken into account
    public PaymentProcessor(String input, Inventory inventory){
        this.input = input;
        this.inventory = inventory;
    }

    //Getters and Setters
    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public String getItemsNotSold() {
        return itemsNotSold;
    }

    public void setItemsNotSold(String itemsNotSold) {
        this.itemsNotSold = itemsNotSold;
    }

    public String getItemsSold() {
        return itemsSold;
    }

    public void setItemsSold(String itemsSold) {
        this.itemsSold = itemsSold;
    }

    public int getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(int totalSpent) {
        this.totalSpent = totalSpent;
    }

    //NTS: Inputs must be validated before using this method. No validation is done in this method.
    //This method calculates the refund amount and items sold based on the input given to the machine and the
    //output string produced when processing is complete.
    public void turingCalculation(String output) { //NTS: You need to tie stock checking into this method
            for (int i = 0; i < output.length(); i++) {
                char outputChar = output.charAt(i);
                //if output string contains money that wasn't used add it to thr refund amount
                if (MONEY_VALUES.containsKey(outputChar)) {
                    setRefund(refund += MONEY_VALUES.get(outputChar));
                    //if output string contained values that were not crossed out in the output, add it to items not sold
                } else if (ITEM_PRICES.containsKey(outputChar)) {
                    setItemsNotSold(itemsNotSold += outputChar);
                }
                //Where a character is marked as X in the output calculate the value in input
                if (i < input.length() && output.charAt(i) == 'X') {
                    char inputChar = input.charAt(i);
                    if (MONEY_VALUES.containsKey(inputChar)) {
                        setTotalSpent(totalSpent += MONEY_VALUES.get(inputChar));
                    } else if (ITEM_PRICES.containsKey(inputChar)) {
                        setItemsSold(itemsSold += inputChar);
                    }
                }
            }
            getSoldItems(); //NTS: Not storing data returned
            System.out.println("Refund: " + refund); //Prints statements for testing purposes
            System.out.println("Items not sold: " + itemsNotSold);
            System.out.println("Items sold: " + itemsSold);
            System.out.println("Total spent: " + totalSpent);
    }

    //This function updates the quantity of items that are sold
    public Map<Character, Integer> getSoldItems() {
        for (int i = 0; i < itemsSold.length(); i++) {
            char c = itemsSold.charAt(i);
            if (SOLD.containsKey(c)) {
                int count = SOLD.get(c);
                SOLD.put(c, count + 1);
            }
        }
        return SOLD;
    }

    //This method check if inventory contains enough items for sales.
    // It should only be called after input validation no validation is done inside
    public boolean checkAvailableStock() {
        char inputChar;
        for (int i = 0; i < input.length(); i++) {//first find which items are being attempted to purchase
            inputChar = input.charAt(i);
            if (Requested_ITEMS.containsKey(inputChar)) {
                int count = Requested_ITEMS.get(inputChar);
                Requested_ITEMS.put(inputChar, count + 1);
            }
        }
        for (int i = 0; i < input.length(); i++) {//then determine if we have that in stock
            inputChar = input.charAt(i);
            if (Requested_ITEMS.containsKey(inputChar)) {
                try {
                    if (inventory.getQuantity(String.valueOf(inputChar)) < Requested_ITEMS.get(inputChar)) {
                        JOptionPane.showMessageDialog(null, "Inventory contain less items than requested!", "Error", JOptionPane.WARNING_MESSAGE, null);
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return true;
    }



}

