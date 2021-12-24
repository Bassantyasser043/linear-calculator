package Evaluation;

import java.util.ArrayList;
import java.util.Iterator;

public class CoefficientServices {
    String equations;
    int var;
    public boolean valid;
    double[] results;
    ValidationServices validationServices;

    public CoefficientServices(String equations, int var) {
        this.var = var;
        this.equations = equations;
        this.validationServices = new ValidationServices(this.equations, this.var);
        this.valid = validationServices.validation();
    }


    ////////////////////////////
    public boolean isnumber(String string) {
        try {
            double intValue = Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //////////////////take the coefficients
    public double extract_coeff(String variable, int var, String equl) {
        String[] templeft = new String[var];
        double ohh = 0;
        double sum = 0;
        double sumcon = 0;
        templeft = equl.split("\\+");
        for (int j = 0; j < templeft.length; j++) {
            if (equl.contains(variable)) {
                String number = templeft[j].replace(variable, "");
                if (templeft[j].contains(variable)) {
                    if (number.isEmpty()) {
                        number = "1";
                    } else if (number.equals("-")) {
                        number = "-1";
                    } else if (number.equals(".")) {
                        number = "0";
                    }
                    ohh = Double.parseDouble(number);
                    sum = sum + ohh;
                } else if (templeft[j].isEmpty()) {
                    return 0;
                } else if (isnumber(templeft[j])) {
                    sumcon = sumcon + Double.parseDouble(templeft[j].replace(variable, ""));
                }
            } else if (isnumber(templeft[j])) {
                sumcon = sumcon + Double.parseDouble(templeft[j].replace(variable, ""));
            }
        }
        validationServices.con.add(sumcon);
        return sum;
    }

    ///////////////////////////////////////////////////store coefficients in matrix
    public double[][] extract_coeff(int var) {
        double[] coeff = new double[var];
        double[][] coefficients = new double[var][var + 1];
        int count = 0;
        if (valid) {
            Iterator<String> itr = validationServices.data.iterator();
            while (itr.hasNext()) {
                String element = itr.next();
                for (int i = 0; i < validationServices.eq.length; i++) {
                    coeff[i] = extract_coeff(element, var, validationServices.eq[i]) - extract_coeff(element, var, validationServices.constants[i]);
                    coefficients[i][count] = coeff[i];
                }
                count++;
            }

            ArrayList<Double> sub_ArrayList = new ArrayList<Double>(validationServices.con.subList(0, 2 * var));
            double sum = 0;
            ////////row of the matrix to store the constants
            ////sum the constants
            int start = 0;
            for (int i = 0; i < var; i++) {
                sum = 0;
                for (int j = 0; j < 2; j++) {
                    if (j % 2 == 0) {
                        sum = sum - sub_ArrayList.get(start);
                    } else {
                        sum = sum + sub_ArrayList.get(start);
                    }
                    start++;
                }
                coefficients[i][var] = sum;
            }
            /////printing matrix
            for (int i = 0; i < var; i++) {
                for (int j = 0; j < var + 1; j++) {
                    System.out.print(" " + coefficients[i][j]);
                }
                System.out.print("\n");
            }
            //  }
            //////////////////////return error to the front gui
        } else {
            System.out.print("WARNING1!!!!!!!!!!!!!");

        }
        return coefficients;
    }

    public int Rank(double[][] A, double[] B, int precesion,double[] scale) {
        double[][] tempA;
        double[] tempB;
        tempA = A.clone();
        tempB = B.clone();
        for (int i = 0; i < A.length; i++) {
            tempA[i] = A[i].clone();
        }
        int length = tempB.length;
        int condition = 0;
        double check = Math.pow(10, -precesion);
        for (int k = 0; k < length - 1; k++) {
            Partial_Pivoting(tempA, tempB, scale, k);
            for (int i = k + 1; i < length; i++) {
                double factor = tempA[i][k] / tempA[k][k];
                for (int j = k; j < length; j++) {
                    tempA[i][j] = tempA[i][j] - factor * tempA[k][j];
                }
                tempB[i] = tempB[i] - factor * tempB[k];
            }
        }
        for (int i = 0; i < length; i++) {

            if (Math.abs(tempA[i][i]) <= check && Math.abs(tempB[i]) > check) {
                condition = -1;
                return condition;
            } else if (Math.abs(tempA[i][i]) <= check && Math.abs(tempB[i]) < check) {
                condition = 1;
                return condition;
            }

        }
        return condition;
    }
    public int scale(double [][]A,double[]B,int prec){
        int length=B.length;
        int i=0;
        int j=1;
        double[] scaling = new double[length];
        /** scalling array to put the biggest value of every row to make scalling**/

        while (i<length){
            scaling[i]=Math.abs(A[i][0]);
            /**the intial value of scalling array is the first element in every row**/
            while (j<length){
                if (Math.abs(A[i][j]) > scaling[i]) {
                    scaling[i] = Math.abs(A[i][j]);
                }
                j++;
            }
            j=1;
            i++;

        }
        return   Rank(A,B,prec,scaling);
    }
    public void Partial_Pivoting(double[][] A, double[] B,double[] scaling, int k) {
        int pivot = k;
        double max = 0;
        double temp = 0;
        int length= B.length;
        int i=k+1;
        max = Math.abs(A[k][k] / scaling[k]);
        /** make pivot as large as possible**/
        while (i<length){
            temp = Math.abs(A[i][k] / scaling[i]);
            if (temp > max) {
                max = temp;
                pivot = i;
            }
            i++;
        }
        /**if we need to make make partial pivoting**/
        if (pivot != k) {
            int j=k;
            /**interchane rows of Matrix A**/
            while (j<length){
                temp = A[pivot][j];
                A[pivot][j] = A[k][j];
                A[k][j] = temp;
                j++;
            }
            /**interchange scaling array**/
            temp = scaling[pivot];
            scaling[pivot] = scaling[k];
            scaling[k] = temp;
            /**interchange B array**/
            temp = B[pivot];
            B[pivot] = B[k];
            B[k] = temp;


        }
    }

}
