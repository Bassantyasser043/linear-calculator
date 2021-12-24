package seidelAndJacobi;

import java.util.Arrays;

public class Seidel {
    public  boolean isDivergent = false;

    public double[] seidelMethod(double[][] equ, double[] initial, String choice, double num, int precision) {

        double temp[] = initial.clone();
        int ncol = equ[0].length; //no. of columns
        int nrow = equ.length; //no. of rows
        double sum;
        double[] V = new double[nrow];
        double[] E = new double[nrow];

        double iter = 0;
        double NoOfIterations = 0;
        double Es = 0;
        boolean flag = false;

        double rounding;
        switch (choice) {
            case "Number of Iterations":
                NoOfIterations = num;

                break;
            case "Absolute Relative Error":
                Es = num;
                break;
        }
        rounding = Math.pow(10, precision);
        while (flag == false) {

            for (int i = 0; i < nrow; ++i) {
                sum = equ[i][ncol - 1]; //last col
                for (int j = 0; j < nrow; ++j) {
                    if (i != j) {
                        sum = (Math.round(sum * rounding) / rounding) - ((Math.round(temp[j] * rounding) / rounding) * (Math.round(equ[i][j] * rounding) / rounding));
                    }
                }
                V[i] = (Math.round(((Math.round(sum * rounding) / rounding) / (Math.round(equ[i][i] * rounding) / rounding)) * rounding) / rounding);
                E[i] = Math.abs(Math.round(((((Math.round(V[i] * rounding) / rounding) - (Math.round(temp[i] * rounding) / rounding)) / (Math.round(V[i] * rounding) / rounding)) * 100) * rounding) / rounding);

                temp[i] = V[i];

            }
            System.out.print("after iteration= ");
            System.out.println(Arrays.toString(temp));
            System.out.print("relative error= ");
            System.out.println(Arrays.toString(E));

            if (NoOfIterations == num) {
                ++iter;
                if (iter == 1) {
                    continue;
                }
                if (iter == NoOfIterations && Math.round(max_element(E) * rounding) / rounding > 100) {
                    System.out.println("diverge");
                    flag = true;
                    isDivergent = true;
                    break;
                } else if (iter == NoOfIterations && Math.round(max_element(E) * rounding) / rounding < 50) {
                    flag = true;
                    break;
                }
            } else if (Es == num) {
                if (Math.round(max_element(E) * rounding) / rounding < Es) {
                    flag = true;
                    break;
                } else {
                    ++iter;
                    if (Math.round(max_element(E) * rounding) / rounding > 100 && iter > 6) {
                        System.out.print("diverage..");
                        isDivergent = true;
                        break;
                    }

                }
            }
        }

        System.out.printf("\n");

        return temp;
    }

    public double max_element(double[] s) {
        double max = s[0];
        for (int y = 1; y < s.length; ++y) {
            if (s[y] > max) {
                max = s[y];
            }
        }
        return max;
    }
}
