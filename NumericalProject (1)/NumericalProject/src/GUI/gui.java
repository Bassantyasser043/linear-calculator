package GUI;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.net.URL;

import Evaluation.*;
import Gauss.*;
import seidelAndJacobi.*;
import LU.*;

class gui {

    public static void main(String args[]) {
        GuiServices guiServices = new GuiServices();


        JFrame frame = new JFrame();
        JTextPane Variables = new JTextPane();
        JLabel label = new JLabel();
        JLabel method_of_solving = new JLabel();
        JLabel Variables_label = new JLabel();
        JLabel equations_label = new JLabel();
        JLabel output_label = new JLabel();
        JLabel runTime_label = new JLabel();
        JLabel matrix_label = new JLabel();
        JTextPane input = new JTextPane();
        JTextField output = new JTextField();
        JTextField runTime = new JTextField();
        JTextPane matrix = new JTextPane();
        JComboBox methods = new JComboBox(guiServices.manner);
        JButton solve = new JButton("Solve");
        JButton setAttributes = new JButton("Set Attributes");
        Border border = BorderFactory.createLineBorder(Color.green, 3);
        JButton clear = new JButton("Clear");
        URL url = gui.class.getResource("Logo.png");
        ImageIcon image = new ImageIcon(url);


        // setting a label
        label.setText("Solve Equation");
        label.setForeground(Color.decode("#3e4444"));
        label.setFont(new Font("MV Boli", Font.PLAIN, 50));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);

        // adding text field for the input
        equations_label.setText("Equations");
        equations_label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        equations_label.setBounds(20, 230, 150, 50);
        input.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        input.setBounds(150, 200, 725, 100);


        // adding text field for the output
        output_label.setText("Output");
        output_label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        output_label.setBounds(20, 450, 150, 50);
        output.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        output.setBounds(150, 450, 725, 50);
        output.setEditable(false);

        // adding text field for matrix
        matrix_label.setText("Matrix");
        matrix_label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        matrix_label.setBounds(900, 100, 150, 50);
        matrix.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        matrix.setBounds(900, 150, 400, 500);
        matrix.setEditable(false);


        // adding text field for the runtime
        runTime_label.setText("Run Time");
        runTime_label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        runTime_label.setBounds(20, 525, 150, 50);
        runTime.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        runTime.setBounds(150, 525, 725, 50);
        runTime.setEditable(false);

        // adding text field for the Variables
        Variables_label.setText("Variables");
        Variables_label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        Variables_label.setBounds(20, 350, 150, 50);
        Variables.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        Variables.setBounds(150, 350, 725, 50);

        // adding dropdown list
        method_of_solving.setText("Method");
        method_of_solving.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        method_of_solving.setBounds(20, 100, 150, 50);
        methods.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        methods.setBounds(150, 100, 300, 50);
        methods.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guiServices.text = (String) methods.getSelectedItem();
            }
        });

        // setting a button solve
        solve.setBounds(650, 600, 100, 50);
        solve.setBackground(Color.green);
        solve.setFont(new Font("Arial", Font.PLAIN, 20));
        solve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int numOfvariables = 0, rank;
                long startTime;

                boolean flag = true;
                try {
                    numOfvariables = Integer.parseInt(Variables.getText());
                } catch (NumberFormatException e1) {
                    guiServices.showError("Please, Enter a valid Integer");
                    flag = false;
                }
                if (flag) {
                    if (input.getText().equals("")) {
                        guiServices.showError("  Please, Enter The Equations");
                    } else if (Variables.getText().equals("")) {
                        guiServices.showError("Enter the NUMBER of variables");
                    } else {
                        CoefficientServices coefficientServices = new CoefficientServices(input.getText(), numOfvariables);
                        if (coefficientServices.valid) {
                            double[][] augmentedMatrix = coefficientServices.extract_coeff(Integer.parseInt(Variables.getText()));
                            double[][] coefficientsMatrix = guiServices.getCoefficientMatrix(augmentedMatrix, numOfvariables);
                            double[] resultsArray = guiServices.getResultsArray(augmentedMatrix, numOfvariables);
                            double[] solution = {};
                            rank = coefficientServices.scale(coefficientsMatrix, resultsArray, guiServices.g_precision);
                            if (rank == -1) {
                                output.setText("No solution");
                            } else if (rank == 1) {
                                output.setText("Infinite number of solutions");
                            } else {
                                if (guiServices.text.equals("Gauss Elimination")) {
                                    GaussService gaussService = new GaussService();
                                    startTime = System.nanoTime();
                                    solution = gaussService.Gauss(coefficientsMatrix, resultsArray, guiServices.g_precision);
                                    String steps = guiServices.setMatrix(coefficientsMatrix);
                                    matrix.setText(steps);
                                } else if (guiServices.text.equals("Gauss-Jordan")) {
                                    GaussService gaussService = new GaussService();
                                    startTime = System.nanoTime();
                                    solution = gaussService.Gauus_jorrdan(coefficientsMatrix, resultsArray, guiServices.g_precision);
                                    String steps = guiServices.setMatrix(coefficientsMatrix);
                                    matrix.setText(steps);
                                } else if (guiServices.text.equals("LU Decomposition")) {
                                    LUServices luServices = new LUServices(numOfvariables, coefficientsMatrix, resultsArray, guiServices.g_precision);
                                    if (guiServices.lu_form == "Doolittle Form") {
                                        startTime = System.nanoTime();
                                        solution = luServices.Doolittle();
                                    } else {
                                        startTime = System.nanoTime();
                                        solution = luServices.Crout();
                                    }
                                    String steps = "L:\n" + guiServices.setMatrix(luServices.luResult.getL());
                                    steps += "U:\n";
                                    steps += guiServices.setMatrix(luServices.luResult.getU());
                                    matrix.setText(steps);


                                } else if (guiServices.text.equals("Gauss-Seidil")) {

                                    Seidel seidel = new Seidel();
                                    startTime = System.nanoTime();
                                    solution = seidel.seidelMethod(augmentedMatrix, guiServices.g_initial_guess_values, guiServices.g_stop_condition, guiServices.g_stop_value, guiServices.g_precision);
                                    matrix.setText("");
                                    if (seidel.isDivergent) {
                                        output.setText(guiServices.turnIntoString(solution));
                                        guiServices.showError("       Diverges");
                                    }
                                } else { //jacobi{
                                    Jacobi jacobi = new Jacobi();
                                    startTime = System.nanoTime();
                                    solution = jacobi.JacobiMethod(augmentedMatrix, guiServices.g_initial_guess_values, guiServices.g_stop_condition, guiServices.g_stop_value, guiServices.g_precision);
                                    matrix.setText("");
                                    if (jacobi.isDivergent) {
                                        output.setText(guiServices.turnIntoString(solution));
                                        guiServices.showError("       Diverges");
                                    }
                                }
                                long endTime = System.nanoTime();
                                long totalTime = endTime - startTime;
                                runTime.setText(totalTime + "");
                                output.setText(guiServices.turnIntoString(solution));
                            }
                        } else {
                            output.setText("");
                            guiServices.showError("Enter a proper format");

                        }
                    }
                }
            }
        });

        // setting button setAttributes
        setAttributes.setBounds(450, 600, 200, 50);
        setAttributes.setBackground(Color.green);
        setAttributes.setFont(new

                Font("Arial", Font.PLAIN, 20));
        setAttributes.addActionListener(new

                                                ActionListener() {
                                                    public void actionPerformed(ActionEvent e) {
                                                        guiServices.showParameters(guiServices.text);
                                                    }
                                                });

        // setting button clear
        clear.setBounds(750, 600, 100, 50);
        clear.setBackground(Color.green);
        clear.setFont(new

                Font("Arial", Font.PLAIN, 20));
        clear.addActionListener(new

                                        ActionListener() {
                                            public void actionPerformed(ActionEvent e) {
                                                methods.setSelectedItem(guiServices.manner[0]);
                                                input.setText("");
                                                Variables.setText("");
                                                output.setText("");
                                                runTime.setText("");
                                                matrix.setText("");
                                            }
                                        });

        // adding elements to the frame
        frame.add(methods);
        frame.add(method_of_solving);
        frame.add(input);
        frame.add(equations_label);
        frame.add(output);
        frame.add(output_label);
        frame.add(runTime);
        frame.add(runTime_label);
        frame.add(matrix);
        frame.add(matrix_label);
        frame.add(Variables);
        frame.add(Variables_label);
        frame.add(setAttributes);
        frame.add(solve);
        frame.add(clear);
        frame.add(label);


        // setting border
        label.setBorder(border);

        // to set a logo
        frame.setIconImage(image.getImage());

        // Setting the frame
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setTitle("Equation Solver");
        frame.setSize(1000, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().

                setBackground(Color.decode("#878f99"));
    }


}
