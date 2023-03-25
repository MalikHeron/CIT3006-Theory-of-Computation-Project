package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class dialogs extends JDialog implements ActionListener {
    private JTextField inputField, refundField, qforkField, qknifeField, qspoonField, qNapkinField;
    private JLabel inputLabel, refundLabel, forkLabel, knifeLabel, spoonLabel,
            napkinLabel, displayLabel, dispenseLabel, stockLabel, stocks;
    private JButton okButton;
    private String input = "";
    private JDialog summaryDialog;
    private double refund = 0;
    private boolean isValid = true;
    private static final Map<Character, Integer> SOLD = new HashMap<Character, Integer>() {{
        put('N', 0);
        put('K', 0);
        put('F', 0);
        put('S', 0);
    }};

    private static final Map<Character, String> NO_STOCK = new HashMap<Character, String>();


    private void initializeComponents() {
        //Panel Properties
        Font smlLabelFont = new Font("SANS_SERIF", Font.PLAIN, 34);
        Font lrgLabelFont = new Font("SANS_SERIF", Font.PLAIN, 40);
        Font fieldFont = new Font("SANS_SERIF", Font.PLAIN, 34);
        //Components Configuration
        summaryDialog = new JDialog();

        displayLabel = new JLabel("Machine Output", SwingConstants.CENTER);
        displayLabel.setFont(lrgLabelFont);
        displayLabel.setPreferredSize(new Dimension(700, 65));
        displayLabel.setVerticalAlignment(SwingConstants.CENTER);

        inputLabel = new JLabel("Input");
        inputLabel.setFont(lrgLabelFont);
        inputLabel.setPreferredSize(new Dimension(300, 65));
        inputLabel.setVerticalAlignment(SwingConstants.CENTER);

        inputField = new JTextField(input);
        inputField.setFont(fieldFont);
        inputField.setPreferredSize(new Dimension(380, 60));

        dispenseLabel = new JLabel("Dispensing...", SwingConstants.CENTER);
        dispenseLabel.setFont(lrgLabelFont);
        dispenseLabel.setPreferredSize(new Dimension(700, 65));
        dispenseLabel.setVerticalAlignment(SwingConstants.CENTER);

        knifeLabel = new JLabel("Knife", SwingConstants.CENTER);
        knifeLabel.setFont(fieldFont);
        knifeLabel.setPreferredSize(new Dimension(200, 60));

        qknifeField = new JTextField(String.valueOf("0"));//SOLD.get('K')));
        qknifeField.setFont(fieldFont); //NTS: MIXING OF FONTS,Keep this one
        qknifeField.setPreferredSize(new Dimension(100, 60));
        qknifeField.setHorizontalAlignment(SwingConstants.CENTER);

        forkLabel = new JLabel("Fork", SwingConstants.CENTER);
        forkLabel.setFont(smlLabelFont);
        forkLabel.setPreferredSize(new Dimension(200, 60));

        qforkField = new JTextField(String.valueOf("0"));//SOLD.get('F')));
        qforkField.setFont(fieldFont); //NTS: MIXING OF FONTS,Keep this one
        qforkField.setPreferredSize(new Dimension(100, 60));
        qforkField.setHorizontalAlignment(SwingConstants.CENTER);

        spoonLabel = new JLabel("Spoon", SwingConstants.CENTER);
        spoonLabel.setFont(smlLabelFont);
        spoonLabel.setPreferredSize(new Dimension(200, 60));

        qspoonField = new JTextField(String.valueOf("0"));//SOLD.get('S')));
        qspoonField.setFont(fieldFont);
        qspoonField.setPreferredSize(new Dimension(100, 60));
        qspoonField.setHorizontalAlignment(SwingConstants.CENTER);

        napkinLabel = new JLabel("Napkin", SwingConstants.CENTER);
        napkinLabel.setFont(smlLabelFont);
        napkinLabel.setPreferredSize(new Dimension(200, 60));

        qNapkinField = new JTextField(String.valueOf("0"));//SOLD.get('N')));
        qNapkinField.setFont(fieldFont); //NTS: MIXING OF FONTS,Keep this one
        qNapkinField.setPreferredSize(new Dimension(100, 60));
        qNapkinField.setHorizontalAlignment(SwingConstants.CENTER);

        stockLabel = new JLabel("Out of Stock");
        stockLabel.setFont(lrgLabelFont);
        stockLabel.setPreferredSize(new Dimension(300, 65));
        stockLabel.setVerticalAlignment(SwingConstants.CENTER);

        stocks = new JLabel();
        stocks.setFont(lrgLabelFont);
        stocks.setPreferredSize(new Dimension(300, 65));
        stocks.setVerticalAlignment(SwingConstants.CENTER);


        refundLabel = new JLabel("Refund/Change"); // Change Text to 'change' based on payment processing
        refundLabel.setFont(lrgLabelFont);
        refundLabel.setPreferredSize(new Dimension(300, 65));
        refundLabel.setVerticalAlignment(SwingConstants.CENTER);

        refundField = new JTextField((String.valueOf(getRefund())));
        refundField.setFont(fieldFont); //NTS: MIXING OF FONTS,Keep this one
        refundField.setPreferredSize(new Dimension(380, 60));
        refundField.setHorizontalAlignment(SwingConstants.CENTER);
        //Button properties
        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(250, 60));
        okButton.setFont(smlLabelFont);
        okButton.setAlignmentX(SwingConstants.CENTER);
        okButton.setAlignmentY(SwingConstants.CENTER);

        JTextField[] allFields = {inputField, refundField, qforkField, qknifeField, qspoonField, qNapkinField};
        for (JTextField field : allFields) {
            field.setEnabled(false);
        }

        // Use a timer to update the values of the sold items
        Timer timer = new Timer(200, e -> {
            // Increment the values and update the text fields
            int soldKnife = SOLD.get('K');
            int knives = Integer.valueOf(qknifeField.getText());
            if (soldKnife > knives) {
                knives += 1;
                qknifeField.setText(String.valueOf(knives));
            }

            int soldFork = SOLD.get('F');
            int fork = Integer.valueOf(qforkField.getText());
            if (soldFork > fork) {
                fork += 1;
                qforkField.setText(String.valueOf(fork));
            }
            int soldSpoon = SOLD.get('S');
            int spoon = Integer.valueOf(qspoonField.getText());
            if (soldSpoon > spoon) {
                spoon += 1;
                qspoonField.setText(String.valueOf(spoon));
            }

            int soldNapkin = SOLD.get('N');
            int napkin = Integer.valueOf(qNapkinField.getText());
            if (soldNapkin > napkin) {
                napkin += 1;
                qNapkinField.setText(String.valueOf(napkin));
            }
            // Check if all items are sold out and stop the timer
            if (soldKnife == knives && soldFork == fork && soldSpoon == spoon && soldNapkin == napkin) {
                ((Timer) e.getSource()).stop();
                String OoS = "";
                for (Map.Entry<Character, String> entry : NO_STOCK.entrySet()) {
                    OoS += entry.getValue() + ", ";
                }
                if(OoS.length() > 3) {
                    stocks.setText(OoS.substring(0, OoS.length() - 2));
                }
            }
        });
        timer.start();

    }

    private void addComponentsToWindow() {
        summaryDialog.add(displayLabel);
        summaryDialog.add(inputLabel);
        summaryDialog.add(inputField);
        summaryDialog.add(dispenseLabel);
        summaryDialog.add(knifeLabel);
        summaryDialog.add(qknifeField);
        summaryDialog.add(forkLabel);
        summaryDialog.add(qforkField);
        summaryDialog.add(spoonLabel);
        summaryDialog.add(qspoonField);
        summaryDialog.add(napkinLabel);
        summaryDialog.add(qNapkinField);
        summaryDialog.add(stockLabel);
        summaryDialog.add(stocks);
        summaryDialog.add(refundLabel);
        summaryDialog.add(refundField);
        summaryDialog.add(okButton);
    }

    public void validateInput(String item) {
        // Check if the input char is invalid
        char[] tapeAlphabet = {'F', 'K', 'N', 'S', 'ɑ', 'β', 'γ', '⊔', 'x'};
        String validChars = "FKNSɑβγ⊔x";
        for (int i = 0; i < item.length(); i++) {
            if (!validChars.contains(String.valueOf(item.charAt(i)))) {//(!Arrays.asList(tapeAlphabet).contains(item)) {
                String errorMessage = "Invalid input '" + item + "' detected. Machine Halted and Input rejected";
                JOptionPane.showMessageDialog(null, errorMessage, "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void validateInput(char item) {
        // Check if the input char is invalid
        char[] tapeAlphabet = {'F', 'K', 'N', 'S', 'ɑ', 'β', 'γ', '⊔', 'x'};
        if (!Arrays.asList(tapeAlphabet).contains(item)) {
            String errorMessage = "Invalid input '" + item + "' detected. Machine Halted and Input rejected";
            JOptionPane.showMessageDialog(null, errorMessage, "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setOutOfStock(char item) {
        String value = "";
        switch (item) {
            case 'N':
            case 'n':
                value = "Napkin";
                break;
            case 'F':
            case 'f':
                value = "Fork";
                break;
            case 'K':
            case 'k':
                value = "Knifes";
                break;
            case 'S':
            case 's':
                value = "Spoon";
                break;
        }
        NO_STOCK.put(item, value);
    }

    public void setRefund(Double funds) {
        this.refund += funds;
    }

    public double getRefund() {
        return this.refund;
    }

    public void setDispense(char item) {
        int count = SOLD.get(item);
        SOLD.put(item, count + 1);
    }

    public void summaryDialog(String inputString) {
        input = inputString;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //Increasing JOptionPane window size as JOptionPane display very small on my machine NTS: Revisit Later
            UIManager.put("OptionPane.minimumSize", new Dimension(350, 150));
            UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 25));
            UIManager.put("OptionPane.messageFont", new Font("times new roman", Font.PLAIN, 34));
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }

        initializeComponents();
        addComponentsToWindow();
        registerListeners();
        setWindowProperties();
    }

    public void displayFunds(Double totalSale) {
        String message = "Vending Machine current total sales is " + totalSale;
        JOptionPane.showMessageDialog(null, message, "Restocking Inventory", JOptionPane.INFORMATION_MESSAGE);
    }

    //Registering listeners
    private void registerListeners() {
        okButton.addActionListener(this);
    }

    //Setting Window Properties
    private void setWindowProperties() {
        summaryDialog.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 30));
        summaryDialog.setTitle("TRANSACTION COMPLETE");
        summaryDialog.setSize(820, 850); //820 [500 or 950]
        summaryDialog.setLocationRelativeTo(null);
        summaryDialog.setResizable(false);
        summaryDialog.setModal(true);
        summaryDialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            summaryDialog.dispose();
        }
    }
}
/*private void initializeComponents() {
        //Panel Properties
        Font smlLabelFont = new Font("SANS_SERIF", Font.PLAIN, 34);
        Font lrgLabelFont = new Font("SANS_SERIF", Font.PLAIN, 40);
        Font fieldFont = new Font("SANS_SERIF", Font.PLAIN, 34);
        //Components Configuration
        summaryDialog = new JDialog();

        displayLabel = new JLabel("Machine Output", SwingConstants.CENTER); //String Accepted! Transaction Complete.
        displayLabel.setFont(lrgLabelFont); //NTS: MIXING OF FONTS
        displayLabel.setPreferredSize(new Dimension(700, 65));
        displayLabel.setVerticalAlignment(SwingConstants.CENTER);

        inputLabel = new JLabel("Input String");
        inputLabel.setFont(lrgLabelFont);
        inputLabel.setPreferredSize(new Dimension(300, 65));
        inputLabel.setVerticalAlignment(SwingConstants.CENTER);

        inputField = new JTextField(input);
        inputField.setFont(fieldFont); //NTS: MIXING OF FONTS,Keep this one
        inputField.setPreferredSize(new Dimension(380, 60));

        dispenseLabel = new JLabel("Dispensing...", SwingConstants.CENTER);
        dispenseLabel.setFont(lrgLabelFont);
        dispenseLabel.setPreferredSize(new Dimension(650, 65));
        dispenseLabel.setVerticalAlignment(SwingConstants.CENTER);

        quantityLabel = new JLabel("Quantity");
        quantityLabel.setFont(lrgLabelFont); //NTS: MIXING OF FONTS
        quantityLabel.setPreferredSize(new Dimension(300, 65));
        quantityLabel.setVerticalAlignment(SwingConstants.CENTER);
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);

        itemLabel = new JLabel("Item");
        itemLabel.setFont(lrgLabelFont); //NTS: MIXING OF FONTS
        itemLabel.setPreferredSize(new Dimension(250, 65));
        itemLabel.setVerticalAlignment(SwingConstants.CENTER);
        itemLabel.setHorizontalAlignment(SwingConstants.CENTER);

        knifeLabel = new JLabel("Knife", SwingConstants.CENTER);
        knifeLabel.setFont(fieldFont); //NTS: MIXING OF FONTS,Keep this one
        knifeLabel.setPreferredSize(new Dimension(325, 60));

        qknifeField = new JTextField(String.valueOf(SOLD.get('K')));
        qknifeField.setFont(fieldFont); //NTS: MIXING OF FONTS,Keep this one
        qknifeField.setPreferredSize(new Dimension(150, 60));
        qknifeField.setHorizontalAlignment(SwingConstants.CENTER);

        forkLabel = new JLabel("Fork", SwingConstants.CENTER);
        forkLabel.setFont(smlLabelFont);
        forkLabel.setPreferredSize(new Dimension(325, 60));

        qforkField = new JTextField(String.valueOf(SOLD.get('F')));
        qforkField.setFont(fieldFont); //NTS: MIXING OF FONTS,Keep this one
        qforkField.setPreferredSize(new Dimension(150, 60));
        qforkField.setHorizontalAlignment(SwingConstants.CENTER);

        spoonLabel = new JLabel("Spoon", SwingConstants.CENTER);
        spoonLabel.setFont(smlLabelFont);
        spoonLabel.setPreferredSize(new Dimension(325, 60));

        qspoonField = new JTextField(String.valueOf(SOLD.get('S')));
        qspoonField.setFont(fieldFont); //NTS: MIXING OF FONTS,Keep this one
        qspoonField.setPreferredSize(new Dimension(150, 60));
        qspoonField.setHorizontalAlignment(SwingConstants.CENTER);

        napkinLabel = new JLabel("Napkin", SwingConstants.CENTER);
        napkinLabel.setFont(smlLabelFont); //NTS: MIXING OF FONTS,Keep this one
        napkinLabel.setPreferredSize(new Dimension(325, 60));

        qNapkinField = new JTextField(String.valueOf(SOLD.get('N')));
        qNapkinField.setFont(fieldFont); //NTS: MIXING OF FONTS,Keep this one
        qNapkinField.setPreferredSize(new Dimension(150, 60));
        qNapkinField.setHorizontalAlignment(SwingConstants.CENTER);

        refundLabel = new JLabel("Refund/Change"); // Change Text to 'change' based on payment processing
        refundLabel.setFont(lrgLabelFont);
        refundLabel.setPreferredSize(new Dimension(300, 65));
        refundLabel.setVerticalAlignment(SwingConstants.CENTER);

        refundField = new JTextField((String.valueOf(getRefund())));
        refundField.setFont(fieldFont); //NTS: MIXING OF FONTS,Keep this one
        refundField.setPreferredSize(new Dimension(380, 60));
        refundField.setHorizontalAlignment(SwingConstants.CENTER);
        //Button properties
        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(250, 60));
        okButton.setFont(smlLabelFont);
        okButton.setHorizontalAlignment(SwingConstants.CENTER);
        okButton.setVerticalAlignment(SwingConstants.CENTER);

        JTextField[] allFields = {inputField, refundField, qforkField, qknifeField, qspoonField, qNapkinField};
        for (JTextField field : allFields) {
            field.setEnabled(false);
        }
    }*/