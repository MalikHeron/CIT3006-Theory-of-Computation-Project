package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class InputDialog extends JDialog implements ActionListener {
    private JTextField inputField, refundField, qforkField, qknifeField, qspoonField, qNapkinField;
    private JLabel inputLabel, refundLabel, quantityLabel, itemLabel, forkLabel, knifeLabel, spoonLabel,
            napkinLabel, displayLabel;
    private JButton okButton, retryButton;
    private String input = "";
    private boolean isValidInput;

    public InputDialog() {
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

    private void initializeComponents() {
        //Panel Properties
        Font smlLabelFont = new Font("times new roman", Font.PLAIN, 34);
        Font lrgLabelFont = new Font("times new roman", Font.PLAIN, 40);
        Border buttonBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

        //Components Configuration
        displayLabel = new JLabel("Invalid input!", SwingConstants.CENTER); //String Accepted! Transaction Complete.
        displayLabel.setFont(lrgLabelFont); //NTS: MIXING OF FONTS
        displayLabel.setPreferredSize(new Dimension(700, 65));
        displayLabel.setVerticalAlignment(SwingConstants.CENTER);

        inputLabel = new JLabel("Input String");
        inputLabel.setFont(lrgLabelFont); //NTS: MIXING OF FONTS
        inputLabel.setPreferredSize(new Dimension(300, 65));
        inputLabel.setVerticalAlignment(SwingConstants.CENTER);

        inputField = new JTextField(input);
        inputField.setFont(new Font("arial", Font.PLAIN, 45)); //NTS: MIXING OF FONTS,Keep this one
        inputField.setPreferredSize(new Dimension(380, 60));

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
        knifeLabel.setFont(smlLabelFont); //NTS: MIXING OF FONTS,Keep this one
        knifeLabel.setPreferredSize(new Dimension(325, 60));

        qknifeField = new JTextField(SwingConstants.CENTER);
        qknifeField.setFont(smlLabelFont); //NTS: MIXING OF FONTS,Keep this one
        qknifeField.setPreferredSize(new Dimension(150, 60));
        qknifeField.setHorizontalAlignment(SwingConstants.CENTER);

        forkLabel = new JLabel("Fork", SwingConstants.CENTER);
        forkLabel.setFont(smlLabelFont); //NTS: MIXING OF FONTS,Keep this one
        forkLabel.setPreferredSize(new Dimension(325, 60));

        qforkField = new JTextField();
        qforkField.setFont(smlLabelFont); //NTS: MIXING OF FONTS,Keep this one
        qforkField.setPreferredSize(new Dimension(150, 60));
        qforkField.setHorizontalAlignment(SwingConstants.CENTER);

        spoonLabel = new JLabel("Spoon", SwingConstants.CENTER);
        spoonLabel.setFont(smlLabelFont); //NTS: MIXING OF FONTS,Keep this one
        spoonLabel.setPreferredSize(new Dimension(325, 60));

        qspoonField = new JTextField();
        qspoonField.setFont(smlLabelFont); //NTS: MIXING OF FONTS,Keep this one
        qspoonField.setPreferredSize(new Dimension(150, 60));
        qspoonField.setHorizontalAlignment(SwingConstants.CENTER);

        napkinLabel = new JLabel("Napkin", SwingConstants.CENTER);
        napkinLabel.setFont(smlLabelFont); //NTS: MIXING OF FONTS,Keep this one
        napkinLabel.setPreferredSize(new Dimension(325, 60));

        qNapkinField = new JTextField();
        qNapkinField.setFont(smlLabelFont); //NTS: MIXING OF FONTS,Keep this one
        qNapkinField.setPreferredSize(new Dimension(150, 60));
        qNapkinField.setHorizontalAlignment(SwingConstants.CENTER);

        refundLabel = new JLabel("Refund"); // Change Text to 'change' based on payment processing
        refundLabel.setFont(lrgLabelFont);
        refundLabel.setPreferredSize(new Dimension(300, 65));
        refundLabel.setVerticalAlignment(SwingConstants.CENTER);

        refundField = new JTextField(input);
        refundField.setFont(new Font("arial", Font.PLAIN, 45)); //NTS: MIXING OF FONTS,Keep this one
        refundField.setPreferredSize(new Dimension(380, 60));

        //Button properties
        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(300, 60));
        okButton.setFont(new Font("arial", Font.BOLD, 30));
        okButton.setBorder(buttonBorder);

        //Button properties
        retryButton = new JButton("RETRY");
        retryButton.setPreferredSize(new Dimension(300, 60));
        retryButton.setForeground(Color.RED);
        retryButton.setFont(new Font("arial", Font.BOLD, 30));
        retryButton.setFocusPainted(false);
        retryButton.setBorder(buttonBorder);
        retryButton.setVisible(true);

        JTextField[] allFields = {inputField, refundField, qforkField, qknifeField, qspoonField, qNapkinField};
        for (JTextField field : allFields) {
            field.setEnabled(false);
        }
    }

    private void addComponentsToWindow() {
        add(displayLabel);
        add(inputLabel);
        add(inputField);
        /*add(itemLabel);
        add(quantityLabel);
        add(knifeLabel);
        add(qknifeField);
        add(forkLabel);
        add(qforkField);
        add(spoonLabel);
        add(qspoonField);
        add(napkinLabel);
        add(qNapkinField);
        */
        add(refundLabel);
        add(refundField);
        add(okButton);
        add(retryButton);
    }

    //Registering listeners
    private void registerListeners() {
        okButton.addActionListener(this);
        retryButton.addActionListener(this);
    }

    //validating input field
    private boolean validateFields() {
        return !(inputField.getText().isEmpty());
    }

    //Setting Window Properties
    private void setWindowProperties() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 30, 30));
        setTitle("Input Validation");
        setSize(820, 500); //820 [500 or 950]
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        setVisible(true);
    }

    private boolean isValidInput(String input) {
        input = input.toUpperCase();
        String[] alphabet = {"N", "F", "K", "S"};
        for (int i = 0; i < input.length(); i++) {
            if (!Arrays.asList(alphabet).contains(String.valueOf(input.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public boolean showDialog() {
        return isValidInput;
    }

    //For use in retrieving validated string
    public String getInput() {
        return inputField.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            input = inputField.getText().trim();
            if (isValidInput(input)) {
                isValidInput = true;
                JOptionPane.showMessageDialog(this, "Input Valid!",
                        "Valid Input", JOptionPane.INFORMATION_MESSAGE, null);
                dispose();
            } else if (inputField.getText().isEmpty() || !isValidInput(input)) {
                JOptionPane.showMessageDialog(this, "Invalid input!",
                        "Error", JOptionPane.ERROR_MESSAGE, null);
                Border border = BorderFactory.createLineBorder(Color.RED, 2);
                inputField.setBorder(border);
            }
        }
        if (e.getSource() == retryButton) {
            isValidInput = false;
            inputLabel.setText("Enter String");
            okButton.setText("CONFIRM");
            retryButton.setText("Cancel");
            inputField.setEnabled(true);
            refundField.setVisible(false);
            refundLabel.setVisible(false);
        }
    }
}
