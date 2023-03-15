package view.dialogs;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class InputDialog extends JDialog implements ActionListener {
    private JTextField inputField;
    private JLabel inputLabel;
    private JButton validateButton, cancelButton;
    private JPanel inputPanel;
    private String input = "";
    private boolean isValidInput;

    public InputDialog() {
        initializeComponents();
        addComponentsToWindow();
        registerListeners();
        setWindowProperties();
    }

    private void initializeComponents() {
        //Panel Properties
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //Increasing JOptionPane window size as JOptionPane display very small on my machine NTS: Revisit Later
            UIManager.put("OptionPane.minimumSize", new Dimension(350, 150));
            UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 25));
            UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 30));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        Border buttonBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

        //Label properties
        inputLabel = new JLabel("Input String:");
        inputLabel.setFont(new Font("times new roman", Font.PLAIN, 50)); //NTS: MIXING OF FONTS
        inputLabel.setPreferredSize(new Dimension(300, 65));
        inputLabel.setVerticalAlignment(SwingConstants.CENTER);

        //Field properties
        inputField = new JTextField(input);
        inputField.setFont(new Font("times new roman", Font.PLAIN, 50)); //NTS: MIXING OF FONTS,Keep this one
        inputField.setPreferredSize(new Dimension(380, 60));

        //Button properties
        validateButton = new JButton("CONFIRM");
        validateButton.setPreferredSize(new Dimension(300, 60));
        validateButton.setForeground(Color.GREEN);
        validateButton.setFont(new Font("arial", Font.BOLD, 30));
        validateButton.setBorder(buttonBorder);

        //Button properties
        cancelButton = new JButton("CANCEL");
        cancelButton.setPreferredSize(new Dimension(300, 60));
        cancelButton.setForeground(Color.RED);
        cancelButton.setFont(new Font("arial", Font.BOLD, 30));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(buttonBorder);
    }

    private void addComponentsToWindow() {
        add(inputLabel);
        add(inputField);
        add(validateButton);
        add(cancelButton);
    }

    //Registering listeners
    private void registerListeners() {
        validateButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    //validating input field
    private boolean validateFields() {
        return !(inputField.getText().isEmpty());
    }

    //Setting Window Properties
    private void setWindowProperties() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 30, 30));
        setTitle("Input Validation");
        setSize(820, 300);
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
        if (e.getSource() == validateButton) {
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
        if (e.getSource() == cancelButton) {
            isValidInput = false;
            dispose();
        }
    }
}
