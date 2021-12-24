package seidelAndJacobi;

import java.util.Arrays;

public class Jacobi {
    public boolean isDivergent = false;

    public double[] JacobiMethod(double[][] equ, double[] initial, String choice, double num, int precision) {
        double temp[] = initial.clone();
        int ncol = equ[0].length; //no. of columns
        int nrow = equ.length; //no. of rows
        double sum;
        double[] V = new double[nrow];
        double[] E = new double[nrow];

        Seidel g = new Seidel();
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
            default:
                System.out.print("error..");
        }
        while (flag == false) {
            rounding = Math.pow(10, precision);
            for (int i = 0; i < nrow; ++i) {
                sum = equ[i][ncol - 1]; //last column
                for (int j = 0; j < nrow; ++j) {
                    if (i != j) {

                        sum = (Math.round(sum * rounding) / rounding) - ((Math.round(temp[j] * rounding) / rounding) * (Math.round(equ[i][j] * rounding) / rounding));
                    } else {
                        continue;
                    }
                }
                V[i] = (Math.round(((Math.round(sum * rounding) / rounding) / (Math.round(equ[i][i] * rounding) / rounding)) * rounding) / rounding);
                E[i] = Math.abs(Math.round(((((Math.round(V[i] * rounding) / rounding) - (Math.round(temp[i] * rounding) / rounding)) / (Math.round(V[i] * rounding) / rounding)) * 100) * rounding) / rounding);


            }
            System.out.print("After iteration=  ");
            System.out.println(Arrays.toString(V));
            System.out.print("relative error= ");
            System.out.println(Arrays.toString(E));

            for (int r = 0; r < nrow; ++r) {

                temp[r] = (Math.round(V[r] * rounding) / rounding); //to assign result from iteration
            }

            if (NoOfIterations == num) {
                ++iter;
                if (iter == 1) {
                    continue;
                }
                if (iter == NoOfIterations && Math.round(g.max_element(E) * rounding) / rounding > 100) {
                    System.out.println("diverge");
                    flag = true;
                    isDivergent = true;
                    break;
                } else if (iter == NoOfIterations && Math.round(g.max_element(E) * rounding) / rounding < 50) {
                    flag = true;
                    break;
                }
            } else if (Es == num) {
                if ((Math.round(g.max_element(E) * rounding) / rounding) < Es) {
                    flag = true;
                    break;
                } else {
                    ++iter;
                    if (Math.round(g.max_element(E) * rounding) / rounding > 100 && iter > 6) {
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

}
