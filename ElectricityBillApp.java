import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class ElectricityBillApp {
    private static HashMap<String, ArrayList<String>> history = new HashMap<>();
    private static boolean isDarkMode = false; // Track the current mode

    public static void main(String[] args) {
        JFrame frame = new JFrame("Electricity Bill Calculator");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        // Theme setup
        JPanel contentPane = new JPanel(new GridBagLayout());
        frame.setContentPane(contentPane);
        setTheme(contentPane);

        // Icons
        ImageIcon calculateIcon = resizeIcon(new ImageIcon("calculator.png"), 20, 20);
        ImageIcon clearIcon = resizeIcon(new ImageIcon("delete.png"), 20, 20);
        ImageIcon historyIcon = resizeIcon(new ImageIcon("history.png"), 20, 20);
        ImageIcon printIcon = resizeIcon(new ImageIcon("printer.png"), 20, 20);
        ImageIcon exportIcon = resizeIcon(new ImageIcon("export.png"), 20, 20);

        // Components
        JLabel nameLabel = new JLabel("Customer Name:");
        RoundedTextField nameField = new RoundedTextField(20);

        JLabel meterLabel = new JLabel("Meter Number:");
        RoundedTextField meterField = new RoundedTextField(20);

        JLabel unitsLabel = new JLabel("Units Consumed:");
        RoundedTextField unitsField = new RoundedTextField(20);

        JButton calculateBtn = new JButton("Calc", calculateIcon);
        JButton clearBtn = new JButton("Clear", clearIcon);
        JButton historyBtn = new JButton("History", historyIcon);
        JButton printBtn = new JButton("Print", printIcon);
        JButton exportBtn = new JButton("Export", exportIcon);
        JButton toggleThemeBtn = new JButton("Toggle Theme");

        // Button hover effects
        addButtonHoverEffect(calculateBtn, new Color(144, 238, 144), new Color(50, 205, 50));
        addButtonHoverEffect(clearBtn, new Color(255, 160, 122), new Color(255, 69, 0));
        addButtonHoverEffect(historyBtn, new Color(135, 206, 235), new Color(70, 130, 180));
        addButtonHoverEffect(printBtn, new Color(255, 228, 196), new Color(255, 215, 0));
        addButtonHoverEffect(exportBtn, new Color(221, 160, 221), new Color(186, 85, 211));
        addButtonHoverEffect(toggleThemeBtn, new Color(192, 192, 192), new Color(105, 105, 105));

        JTextArea outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        contentPane.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        contentPane.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        contentPane.add(meterLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        contentPane.add(meterField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        contentPane.add(unitsLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        contentPane.add(unitsField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        contentPane.add(calculateBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        contentPane.add(clearBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        contentPane.add(historyBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        contentPane.add(printBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        contentPane.add(exportBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        contentPane.add(toggleThemeBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        contentPane.add(outputArea, gbc);

        // Button Actions
        calculateBtn.addActionListener(e -> {
            String name = nameField.getText();
            String meter = meterField.getText();
            String unitsStr = unitsField.getText();

            if (name.isEmpty() || meter.isEmpty() || unitsStr.isEmpty()) {
                outputArea.setText("Please fill all fields.");
                return;
            }

            try {
                int units = Integer.parseInt(unitsStr);
                double bill = calculateBill(units);

                String result = String.format("Customer: %s\nMeter: %s\nUnits: %d\nTotal Bill: ₹%.2f",
                        name, meter, units, bill);
                outputArea.setText(result);

                // Store in history
                history.computeIfAbsent(meter, k -> new ArrayList<>()).add(result);

            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid input for units.");
            }
        });

        clearBtn.addActionListener(e -> {
            nameField.setText("");
            meterField.setText("");
            unitsField.setText("");
            outputArea.setText("");
        });

        historyBtn.addActionListener(e -> {
            String meter = meterField.getText();
            if (meter.isEmpty() || !history.containsKey(meter)) {
                outputArea.setText("No history found for the given meter number.");
                return;
            }
            ArrayList<String> records = history.get(meter);
            StringBuilder sb = new StringBuilder("Bill History:\n");
            for (String record : records) {
                sb.append(record).append("\n\n");
            }
            outputArea.setText(sb.toString());
        });

        printBtn.addActionListener(e -> {
            String output = outputArea.getText();
            if (output.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nothing to print!");
                return;
            }
            JOptionPane.showMessageDialog(frame, "Simulating print... \n" + output);
        });

        exportBtn.addActionListener(e -> {
            String output = outputArea.getText();
            if (output.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nothing to export!");
                return;
            }
            try (FileWriter writer = new FileWriter("bill.txt")) {
                writer.write(output);
                JOptionPane.showMessageDialog(frame, "Bill exported successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error exporting the bill!");
            }
        });

        toggleThemeBtn.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            setTheme(contentPane);
        });

        // Display Frame
        frame.setVisible(true);
    }

    // Function to calculate bill
    private static double calculateBill(int units) {
        double bill = 0;
        if (units <= 100) {
            bill = units * 3;
        } else if (units <= 300) {
            bill = (100 * 3) + (units - 100) * 5;
        } else {
            bill = (100 * 3) + (200 * 5) + (units - 300) * 7;
        }
        return bill;
    }

    // Resize icons
    private static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    // Set theme
    private static void setTheme(JPanel contentPane) {
        if (isDarkMode) {
            contentPane.setBackground(new Color(45, 45, 45));

        } else {
            contentPane.setBackground(new Color(240, 240, 245));
        }
        for (Component comp : contentPane.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                label.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
            }
        }
    
        // Repaint the content panel to apply changes
        contentPane.repaint();
    }

    // Add button hover effect
    private static void addButtonHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.setBackground(normalColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }
}

// Rounded TextField
class RoundedTextField extends JTextField {
    private Shape shape;

    public RoundedTextField(int size) {
        super(size);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        }
        return shape.contains(x, y);
    }
}


