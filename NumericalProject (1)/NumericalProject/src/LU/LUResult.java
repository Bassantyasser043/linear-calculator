package LU;

public class LUResult {

    int n;
    double[][] L, U, P;
    double[] solution;


    public LUResult(int n) {
        this.n = n;
        L = new double[n][n];
        U = new double[n][n];
        P = new double[n][n];
        solution = new double[n];
        for (int i = 0; i < n; i++) {
            P[i][i] = 1;
        }
    }

    public double[][] getL() {
        return L;
    }

    public void setL(double[][] l) {
        L = l;
    }

    public double[][] getU() {
        return U;
    }

    public void setU(double[][] u) {
        U = u;
    }
}