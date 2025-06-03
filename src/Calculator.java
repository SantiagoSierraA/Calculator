import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Calculator {
    JFrame frame = new JFrame("Calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    String a = null;
    String b = null;
    String operator = null;
    boolean newNum = true;

    String[] buttonValues = {
            "C", "+/-", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", " ", "="
    };
    String[] rightOperators = {"÷", "×", "-", "+", "="};
    String[] topOperators = {"C", "+/-", "%"};

    Calculator(){
        // Frame
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360,540);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Display
        displayLabel.setLayout(new BorderLayout());
        displayLabel.setBackground(Color.BLACK);
        displayLabel.setForeground(Color.WHITE);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        frame.add(displayPanel,BorderLayout.NORTH);

        // Buttons
        buttonPanel.setLayout(new GridLayout(5,4));
        buttonPanel.setBackground(Color.GRAY);
        frame.add(buttonPanel,BorderLayout.CENTER);

        for (String buttonValue : buttonValues) {
            JButton button = new JButton(buttonValue);
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setFocusable(false);
            button.setBorderPainted(false);
            buttonPanel.add(button);
            if (buttonValue.equals(" ")) {
                button.setVisible(false);
            }
            button.addActionListener(new ButtonListener());
        }
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String buttonValue = button.getText();

            if (Arrays.asList(topOperators).contains(buttonValue)) {
                switch (buttonValue) {
                    case "C" -> {
                        clear();
                        displayLabel.setText("0");
                        newNum = true;
                    }
                    case "+/-" -> {
                        if (!displayLabel.getText().equals("Error")) {
                            if (a != null && operator != null && !newNum) {
                                calculate();
                            }
                            double value = Double.parseDouble(displayLabel.getText());
                            value = value * -1;
                            displayLabel.setText(removeZeroDec(value));
                            newNum = true;
                        }
                    }
                    case "%" -> {
                        if (!displayLabel.getText().equals("Error")) {
                            if (a != null && operator != null && !newNum) {
                                calculate();
                            }
                            double value = Double.parseDouble(displayLabel.getText());
                            value = value / 100;
                            displayLabel.setText(removeZeroDec(value));
                            newNum = true;
                        }
                    }
                }
            }
            else if (Arrays.asList(rightOperators).contains(buttonValue)) {
                if (buttonValue.equals("=")) {
                    if (a != null && operator != null && !newNum && !displayLabel.getText().equals("Error")) {
                        calculate();
                    }
                }
                else if ("+-×÷".contains(buttonValue)) {

                    if (displayLabel.getText().equals("Error")) {
                        clear();
                        displayLabel.setText("0");
                    }
                    else if (operator != null && !newNum) {
                        calculate();
                    }
                    else {
                        a = displayLabel.getText();
                    }
                    operator = buttonValue;
                    newNum = true;
                }
            }
            else {
                if (buttonValue.equals(".") && !displayLabel.getText().equals("Error")) {
                    if (newNum) {
                        displayLabel.setText("0.");
                        newNum = false;
                    }
                    else if (!displayLabel.getText().contains(buttonValue)) { //Avoid double dots
                        displayLabel.setText(displayLabel.getText() + buttonValue);
                    }

                }
                else if ("0123456789".contains(buttonValue)) {
                    if (newNum || displayLabel.getText().equals("0")) {
                        displayLabel.setText(buttonValue);
                        newNum = false;
                    }
                    else {
                        displayLabel.setText(displayLabel.getText() + buttonValue);
                    }
                }
            }
        }
    }


    void clear(){
        a = null;
        b = null;
        operator = null;
    }

    String removeZeroDec(double value){
        if(value % 1 == 0 && value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE){
            return String.valueOf((int)value);
        }
        else{
            return String.valueOf(value);
        }
    }

    void calculate(){
        b = displayLabel.getText();
        double numA = Double.parseDouble(a);
        double numB = Double.parseDouble(b);
        double result = 0;
        switch (operator) {
            case "+" -> result = numA + numB;
            case "-" -> result = numA - numB;
            case "×" -> result = numA * numB;
            case "÷" -> {
                if (numB == 0) {
                    displayLabel.setText("Error");
                    clear();
                    newNum = true;
                    return;
                } else {
                    result = numA / numB;
                }
            }
        }
        displayLabel.setText(removeZeroDec(result));
        a = removeZeroDec(result);
        operator = null;
        newNum = true;
    }
}
