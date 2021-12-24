package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class GuiServices {
    String lu_form;
    double[] g_initial_guess_values;
    double g_stop_value; // value
    String g_stop_condition; //coice
    int g_precision;
    String text;
    String[] manner;
    boolean attributesFlag = false;

    public GuiServices() {
        lu_form = "Doolittle Form";
        g_stop_value = 10;
        g_stop_condition = "Number of Iterations";
        g_precision = 5;
        text = "Gauss Elimination";
        manner = new String[]{"Gauss Elimination", "Gauss-Jordan", "LU Decomposition", "Gauss-Seidil", "Jacobi-Iteration"};
    }


    public double[][] getCoefficientMatrix(double[][] matrix, int n) {
        if (!attributesFlag) {
            g_initial_guess_values = new double[n];
            for (int i = 0; i < n; i++) {
                g_initial_guess_values[i] = 0;
            }
            attributesFlag = false;
        }
        double[][] coefficient = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                coefficient[i][j] = matrix[i][j];
            }
        }
        return coefficient;
    }

    public double[] getResultsArray(double[][] matrix, int n) {
        double[] results = new double[n];
        for (int i = 0; i < n; i++) {
            results[i] = matrix[i][n];
        }
        return results;
    }

    public double[] separate(String n) {
        try {
            String[] array = n.split(", ");
            double[] values = new double[array.length];
            for (int i = 0; i < array.length; i++) {
                values[i] = checkNumber_double(array[i]);
            }
            return values;
        } catch (Exception e) {
            showError("Enter numbers Separated by \", \"");
        }
        return null;
    }

    public double checkNumber_double(String n) {
        try {
            return Double.parseDouble(n);
        } catch (NumberFormatException e) {
            showError("Please, Enter a valid number");
        }
        return 0;
    }

    public void showError(String message) {
        JDialog error = new JDialog();
        URL url1 = gui.class.getResource("Logo.png");
        ImageIcon image = new ImageIcon(url1);

        JLabel label = new JLabel();
        JButton b = new JButton("OK");
        URL url2 = gui.class.getResource("Error.png");

        ImageIcon icon = new ImageIcon(url2);
        JLabel icon_label = new JLabel(icon);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                error.setVisible(false);
            }
        });
        error.setIconImage(image.getImage());
        label.setText(message);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        label.setBounds(80, 150, 400, 50);
        b.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        b.setBounds(200, 200, 100, 50);
        b.setBackground(Color.green);
        icon_label.setHorizontalAlignment(JLabel.CENTER);
        icon_label.setVerticalAlignment(JLabel.TOP);
        error.add(b);
        error.add(label);
        error.add(icon_label);
        error.setSize(500, 300);
        error.setVisible(true);
        error.getContentPane().setBackground(Color.decode("#878f99"));
        error.setLocationRelativeTo(null);
    }

    public int checkNumber_int(String n) {
        try {
            return Integer.parseInt(n);
        } catch (NumberFormatException e) {
            showError("Please, Enter a valid Integer");
        }
        return 0;
    }

    public String turnIntoString(double[] solution) {
        String res = "";
        for (double i : solution) {
            res = res + i + " ";
        }
        return res;
    }

    public void showParameters(String method) {
        String[] lu = {"Doolittle Form", "Crout Form"};
        String[] iterations = {"Number of Iterations", "Absolute Relative Error"};
        JTextPane precision = new JTextPane();
        JLabel precision_label = new JLabel();
        JLabel l_Dialog = new JLabel();
        JLabel initial_guess_label = new JLabel();
        JButton ok = new JButton("OK");
        JTextField in_dialog = new JTextField();
        JTextField initial_guess = new JTextField();
        JDialog parameter = new JDialog();
        URL url1 = gui.class.getResource("Logo.png");
        ImageIcon image = new ImageIcon(url1);
        JComboBox lu_dialog = new JComboBox(lu);
        JComboBox iterat = new JComboBox(iterations);

        parameter.setIconImage(image.getImage());
        parameter.setTitle("Parameters");
        parameter.setSize(400, 400);

        l_Dialog.setText("Parameter");
        l_Dialog.setForeground(Color.decode("#3e4444"));
        l_Dialog.setFont(new Font("MV Boli", Font.PLAIN, 50));
        l_Dialog.setHorizontalAlignment(JLabel.CENTER);
        l_Dialog.setVerticalAlignment(JLabel.TOP);

        ok.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        ok.setBounds(150, 300, 100, 50);
        ok.setBackground(Color.green);

        lu_dialog.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        lu_dialog.setBounds(100, 200, 200, 50);

        initial_guess_label.setText("Initial Guess");
        initial_guess_label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        initial_guess_label.setBounds(40, 170, 200, 50);

        iterat.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        iterat.setBounds(40, 240, 200, 50);

        in_dialog.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        in_dialog.setBounds(250, 240, 100, 50);
        in_dialog.setText("10");

        initial_guess.setText("0, 0, 0");
        initial_guess.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        initial_guess.setBounds(250, 170, 100, 50);

        precision_label.setText("Precision");
        precision_label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        precision_label.setBounds(40, 100, 150, 50);

        precision.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        precision.setBounds(160, 100, 200, 50);
        precision.setText("5");

        l_Dialog.setVisible(true);
        lu_dialog.setVisible(false);
        initial_guess_label.setVisible(false);
        iterat.setVisible(false);
        in_dialog.setVisible(false);
        initial_guess.setVisible(false);
        precision_label.setVisible(true);
        precision.setVisible(true);

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                if (precision.getText().equals("")) {
                    g_precision = checkNumber_int((String) precision.getText());
                    flag = false;
                } else if (initial_guess.getText() == "") {
                    flag = false;
                } else if (in_dialog.getText().equals("")) {
                    flag = false;
                }
                if (flag) {
                    parameter.setVisible(false);
                    g_stop_condition = (String) iterat.getSelectedItem();
                    if (g_stop_condition.equals("Number of Iterations")) {
                        g_stop_value = checkNumber_int((String) in_dialog.getText());
                    } else if (g_stop_condition.equals("Absolute Relative Error")) {
                        g_stop_value = checkNumber_double((String) in_dialog.getText());
                        System.out.println(g_stop_value);
                    }
                    g_initial_guess_values = separate(initial_guess.getText());
                    g_stop_condition = (String) iterat.getSelectedItem();
                    attributesFlag = true;
                }
            }
        });

        if (method.equals("LU Decomposition")) {
            lu_dialog.setVisible(true);
            lu_form = (String) lu_dialog.getSelectedItem();
        } else if (method.equals("Gauss-Seidil") || method.equals("Jacobi-Iteration")) {
            iterat.setVisible(true);
            in_dialog.setVisible(true);
            initial_guess.setVisible(true);
            initial_guess_label.setVisible(true);
            //g_initial_guess_values = separate(initial_guess.getText());
            // g_stop_condition = (String) iterat.getSelectedItem();
        } else if (method.equals("Gauss Elimination") || method.equals("Gauss-Jordan")) {
            precision.setBounds(160, 150, 200, 50);
            precision_label.setBounds(40, 150, 150, 50);
        }
        parameter.add(lu_dialog);
        parameter.add(iterat);
        parameter.add(in_dialog);
        parameter.add(precision);
        parameter.add(precision_label);
        parameter.add(initial_guess);
        parameter.add(initial_guess_label);
        parameter.add(ok);
        parameter.add(l_Dialog);
        parameter.getContentPane().setBackground(Color.decode("#878f99"));
        parameter.setVisible(true);
        parameter.setLocationRelativeTo(null);
    }

    public String setMatrix(double[][] array) {
        String matrix = "";
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (j == array[i].length - 1) {
                    matrix = matrix + Double.toString(array[i][j]);
                } else {
                    matrix = matrix + Double.toString(array[i][j]) + " | ";
                }
            }
            matrix = matrix + "\n" + "-----".repeat(array[i].length) + "\n";
        }
        return matrix;
    }

}
