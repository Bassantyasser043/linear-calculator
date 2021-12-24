package LU;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LUServices implements LUServicesI {

    public LUResult luResult;
    int n, prec;
    double[][] matrix;
    double[] results;

    public LUServices(int n, double[][] matrix, double[] results, int prec) {
        this.n = n;
        this.matrix = matrix;
        luResult = new LUResult(n);
        this.results = results;
        this.prec = prec;
    }

    @Override
    public double[] Doolittle() {
        double[] z;
        double fraction;
        luResult.U = matrix;
        for (int i = 0; i < n; i++) {
            luResult.L[i][i] = 1;
            if (luResult.U[i][i] == 0) {
                //NO LU Decomposition exist
                return null;
            }
            luResult.L[i][i] = 1;
            for (int k = 1; i + k < n; k++) {
                if (luResult.U[i + k][i] != 0) {
                    fraction = precision(luResult.U[i + k][i] / luResult.U[i][i], prec);
                    luResult.L[i + k][i] = fraction;
                    luResult.U[i + k][i] = 0;
                    for (int j = i + 1; j < n; j++) {
                        luResult.U[i + k][j] = precision((precision(fraction * luResult.U[i][j], prec) * -1) + luResult.U[i + k][j], prec);
                        luResult.L[i][j] = 0;

                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            System.out.println();
            for (int j = 0; j < n; j++) {
                System.out.print(luResult.L[i][j] + "   ");
            }
        }
        System.out.println("\n----------------------");
        for (int i = 0; i < n; i++) {
            System.out.println();
            for (int j = 0; j < n; j++) {
                System.out.print(luResult.U[i][j] + "   ");
            }
        }
        System.out.println("\n----------------------");

        z = forwardSub(luResult.L, results, prec);
        return BackSubstitute(luResult.U, z, prec);
    }

    @Override
    public double[] Crout() {
        double z[];
        for (int i = 0; i < n; i++) {
            luResult.L[i][0] = matrix[i][0];
        }
        for (int j = 1; j < n; j++) {
            luResult.U[0][j] = matrix[0][j] / luResult.L[0][0];
        }
        for (int i = 0; i < n; i++) {
            luResult.U[i][i] = 1;
        }
        for (int i = 1; i < n; i++) {
            for (int j = i; j < n; j++) {
                luResult.L[j][i] = matrix[j][i];
                if (j != i) luResult.U[i][j] = matrix[i][j];
                for (int k = 0; k < i; k++) {
                    luResult.L[j][i] -= precision((luResult.L[j][k] * luResult.U[k][i]), prec);
                    precision(luResult.L[j][i], prec);
                    if (j != i) {
                        luResult.U[i][j] -= precision((luResult.L[i][k] * luResult.U[k][j]), prec);
                        precision(luResult.U[i][j], prec);

                    }

                }
                if (j != i) {
                    luResult.U[i][j] /= luResult.L[i][i];
                    precision(luResult.U[i][j], prec);
                }

            }
        }

        for (int i = 0; i < n; i++) {
            System.out.println();
            for (int j = 0; j < n; j++) {
                System.out.print(luResult.L[i][j] + "   ");
            }
        }
        System.out.println("\n----------------------");
        for (int i = 0; i < n; i++) {
            System.out.println();
            for (int j = 0; j < n; j++) {
                System.out.print(luResult.U[i][j] + "   ");
            }
        }
        System.out.println("\n----------------------");

        z = forwardSub(luResult.L, results, prec);
        System.out.println("\n----------------------");

        return BackSubstitute(luResult.U, z, prec);
    }

    public double precision(double num, int precision) {
        BigDecimal round = new BigDecimal(Double.toString(num));

        round = round.setScale(precision, RoundingMode.HALF_UP);

        return round.doubleValue();

    }

    public double[] BackSubstitute(double[][] A, double[] B, int precisionV) {
        int length = B.length;
        double[] Answer = new double[length];
        Answer[length - 1] = precision(B[length - 1] / A[length - 1][length - 1], precisionV);
        for (int i = length - 2; i >= 0; i--) {
            double sum = 0.0F;
            for (int j = 0; j < length; j++) {
                if (i != j) {
                    sum += precision(A[i][j] * Answer[j], precisionV);
                    precision(sum, prec);
                }
            }
            Answer[i] = precision((B[i] - sum) / A[i][i], precisionV);
        }
        for (int i = 0; i < length; i++) {
            System.out.println(Answer[i]);
        }
        return Answer;
    }

    public double[] forwardSub(double[][] A, double[] B, int precisionV) {
        int length = B.length;
        double[] Answer = new double[length];
        Answer[0] = precision(B[0] / A[0][0], precisionV);
        for (int i = 1; i < length; i++) {
            double sum = 0.0F;
            for (int j = 0; j < length; j++) {
                if (i != j) {
                    sum += precision(A[i][j] * Answer[j], precisionV);
                    precision(sum, prec);
                }
            }
            Answer[i] = precision((B[i] - sum) / A[i][i], precisionV);
        }
        for (int i = 0; i < length; i++) {
            System.out.println(Answer[i]);
        }
        return Answer;
    }


}
