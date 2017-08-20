package Utils;


import java.util.HashMap;
import java.util.Map;

public class QRDecomposition {

    private double[][] matrix;
    private double[][] Q;
    private double[][] R;
    private Map<Integer, double[][]> EMap;
    private Map<Integer, double[][]> UMap;

    public QRDecomposition(double[][] matrix) {
        //pre: matrix is invertible
        this.matrix = matrix;
        allocateMatrices();
        this.EMap = new HashMap<>();
        this.UMap = new HashMap<>();
        computeDecomposition();
    }

    private void allocateMatrices() {
        this.Q = new double[matrix.length][matrix[0].length];
        this.R = new double[matrix[0].length][matrix[0].length];
    }

    private void computeDecomposition() {

        for (int i = 0; i < Q[0].length; i++) {
            putQColumnVectorAtIndex(getEVector(i), i);
        }

        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[0].length; j++) {
                putATimesEValueAtIndices(multiplyDotProductVectors(getEVector(i), getColumnVector(matrix, j)), i, j);
            }
        }

    }

    private double computeNorm(double[][] columnVector) {
        double value = 0.0;
        for (int i = 0; i < columnVector.length; i++) {
            value += Math.pow(columnVector[i][0], 2);
        }

        return Math.sqrt(value);
    }

    private static double[][] getColumnVector(double[][] matrix, int index) {
        double[][] columnVector = new double[matrix.length][1];

        for (int i = 0; i < matrix.length; i++) {
            columnVector[i][0] = matrix[i][index];
        }

        return columnVector;
    }

    private static double[][] divideColumnVector(double[][] vector, double divisor) {

        double[][] result = new double[vector.length][vector[0].length];

        for (int i = 0; i < vector.length; i++) {
            result[i][0] = vector[i][0] / divisor;
        }
        return result;
    }

    private static double multiplyDotProductVectors(double[][] vector1, double[][] vector2) {
        assert vector1.length == vector2.length && vector1[0].length == vector2[0].length : "Dimensions must be equal";
        double result = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            result += vector1[i][0] * vector2[i][0];
        }

        return result;
    }

    private static double[][] multiplyVectorByScalar(double[][] vector, double scalar) {
        double[][] result = new double[vector.length][vector[0].length];

        for (int i = 0; i < vector.length; i++) {
            result[i][0] = vector[i][0] * scalar;
        }
        return result;
    }

    private static double[][] addVectors(double[][] vector1, double[][] vector2) {
        assert vector1.length == vector2.length && vector1[0].length == vector2[0].length : "Dimensions must be equal";
        double[][] result = new double[vector1.length][vector1[0].length];

        for (int i = 0; i < vector1.length; i++) {
            result[i][0] = vector1[i][0] + vector2[i][0];
        }

        return result;
    }

    private static double[][] subtractVectors(double[][] vector1, double[][] vector2) {
        assert vector1.length == vector2.length && vector1[0].length == vector2[0].length : "Dimensions must be equal";
        double[][] result = new double[vector1.length][vector1[0].length];

        for (int i = 0; i < vector1.length; i++) {
            result[i][0] = vector1[i][0] - vector2[i][0];
        }

        return result;
    }

    private double[][] getEVector(int index) {
        if (EMap.containsKey(index)) {
            return copyOf(EMap.get(index));
        } else {
            double[][] UVector = getIntermediateUVector(index);
            double[][] EVector = divideColumnVector(UVector, computeNorm(UVector));
            EMap.put(index, EVector);
            return EVector;
        }
    }

    private double[][] getIntermediateUVector(int index) {
        if (index == 0) {
            //return a1
            if (UMap.containsKey(index)) {
                return copyOf(UMap.get(index));
            }

            return getColumnVector(matrix, index);

        } else {

            if (UMap.containsKey(index)) {
                return copyOf(UMap.get(index));
            }

            double[][] uVector = getCompleteUSection(index);
            UMap.put(index, uVector);
            return uVector;
        }

    }

    private double[][] getUSection(int aIndex, int eIndex) {
        double[][] eVector = getEVector(eIndex);
        return multiplyVectorByScalar(eVector, multiplyDotProductVectors(getColumnVector(matrix, aIndex), eVector));
    }

    private double[][] getCompleteUSection(int uIndex) {

        double[][] result = getColumnVector(matrix, uIndex);

        for (int i = 0; i < uIndex; i++) {
            result = subtractVectors(result, getUSection(uIndex, i));
        }

        return result;
    }

    private double[][] copyOf(double[][] vector) {
        double[][] result = new double[vector.length][vector[0].length];

        for (int i = 0; i < vector.length; i++) {
            for (int j = 0; j < vector[0].length; j++) {
                result[i][j] = vector[i][j];
            }
        }

        return result;
    }

    private void putQColumnVectorAtIndex(double[][] vector, int index) {

        for (int i = 0; i < Q.length; i++) {
            Q[i][index] = vector[i][0];
        }

    }

    private void putATimesEValueAtIndices(double value, int i, int j) {
        R[i][j] = value;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public double[][] getQ() {
        return Q;
    }

    public double[][] getR() {
        return R;
    }
}
