package Gauss;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GaussService  {
    public double[] Gauss(double[][] A, double[] B, int precision) {

        int length = B.length;
        int i = 0;
        int j = 1;
        double[] scalling = new double[length];
        /** scalling array to put the biggest value of every row to make scalling**/

        while (i < length) {
            scalling[i] = Math.abs(A[i][0]);
            /**the intial value of scalling array is the first element in every row**/
            while (j < length) {
                if (Math.abs(A[i][j]) > scalling[i]) {
                    scalling[i] = Math.abs(A[i][j]);
                }
                j++;
            }
            j = 1;
            i++;

        }
        ForwardElimination(A, B, scalling, precision);
        double[] Sol = BackSubstitute(A, B, precision);
        return Sol;
    }

    /**
     * partial pivoting
     **/
    public void Partial_Pivoting(double[][] A, double[] B, double[] scaling, int k) {
        int pivot = k;
        double max = 0;
        double temp = 0;
        int length = B.length;
        int i = k + 1;
        max = Math.abs(A[k][k] / scaling[k]);
        /** make pivot as large as possible**/
        while (i < length) {
            temp = Math.abs(A[i][k] / scaling[i]);
            if (temp > max) {
                max = temp;
                pivot = i;
            }
            i++;
        }
        /**if we need to make make partial pivoting**/
        if (pivot != k) {
            int j = k;
            /**interchane rows of Matrix A**/
            while (j < length) {
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
/**Making forward Elemination**/
    /**
     * make the matrix upper triangular matrix
     **/
    public void ForwardElimination(double[][] A, double[] B, double[] scalling, int precision) {
        int length = B.length;
        int condtion = 0;
        double check = (double) Math.pow(10, -precision);
        for (int k = 0; k < length - 1; k++) {
            /**calling partial pivoting function**/
            Partial_Pivoting(A, B, scalling, k);
            for (int i = k + 1; i < length; i++) {
                /**calculate the factor by dividing the every element in the same row of the pivot by the pivot itself**/
                double factor = precision(A[i][k] / A[k][k], precision);
                for (int j = k; j < length; j++) {
                    /**making each element under the pivot equall zero**/
                    A[i][j] = precision(A[i][j] - precision(factor * A[k][j], precision), precision);
                }
                B[i] = precision(B[i] - precision(factor * B[k], precision), precision);
            }
            /**Check if the matrix is singular or not  and if it has a unique solution or many**/


        }
    }

    /**
     * Backward substitution to get the final answer
     **/
    public double[] BackSubstitute(double[][] A, double[] B, int precisionV) {
        int length = B.length;
        double[] Answer = new double[length];
        Answer[length - 1] = precision(B[length - 1] / A[length - 1][length - 1], precisionV);
        for (int i = length - 2; i >= 0; i--) {
            double sum = 0.0F;
            for (int j = 0; j < length; j++) {
                if (i != j) {
                    sum += precision(A[i][j] * Answer[j], precisionV);
                    precision(sum, precisionV);
                }
            }
            Answer[i] = precision((B[i] - sum) / A[i][i], precisionV);
        }
        for (int i = 0; i < length; i++) {
            System.out.println(Answer[i]);
        }
        return Answer;
    }

    public double[] Gauus_jorrdan(double[][] A, double[] B, int precisionV) {
        int length = B.length;
        int i = 0;
        int j = 1;
        double[] scalling = new double[length];
        /** scalling array to put the biggest value of every row to make scalling**/
        for (int k = 0; k < length; k++) {
            scalling[k] = Math.abs(A[k][0]);
        }
        while (i < length) {
            /**the intial value of scalling array is the first element in every row**/
            while (j < length) {
                if (Math.abs(A[i][j]) > scalling[i]) {
                    scalling[i] = Math.abs(A[i][j]);
                }
                j++;
            }
            i++;
            j = 1;
        }
        /**Check if the matrix is singular or not  and if it has a unique solution or many**/
        ForwardElimination(A, B, scalling, precisionV);
        BackwardElimination(A, B, precisionV);
        double[] Sol = Jordan_sub(A, B, precisionV);
        return Sol;
    }
    /**Backward Elimination**/
    /**
     * Make matrix  diagonal Matrix
     **/
    public void BackwardElimination(double[][] A, double[] B, int precisionV) {
        int length=B.length;
        double check=Math.pow(10,-precisionV);
        for (int k = length-1; k >0 ; k--) {
            for (int i = k -1; i >=0; i--) {
                /*Sme as Forward but it in the opposite side*/
                double factor = precision(A[i][k] / A[k][k],precisionV);
                for (int j = k ; j < length; j++) {
                    A[i][j] =precision( A[i][j] - precision(factor * A[k][j],precisionV),precisionV);
                }
                B[i] = precision(B[i] - precision(factor * B[k],precisionV),precisionV);
            }

        }


    }

    /**
     * substitution of Jordan
     **/
    public double[] Jordan_sub(double[][] A, double[] B, int precisinV) {
        /**no need any calculation**/
        int length = B.length;
        double[] Answer = new double[length];
        for (int i = 0; i < length; i++) {
            /**divide each pivot over the corresponding value in B array **/
            Answer[i] = precision(B[i] / A[i][i], precisinV);
        }
        return Answer;
    }

    public double precision(double num, int precision) {
        BigDecimal round = new BigDecimal(Double.toString(num));

        round = round.setScale(precision, RoundingMode.HALF_UP);

        return round.doubleValue();

    }

}
