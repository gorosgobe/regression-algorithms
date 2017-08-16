package Utils;


public class MatrixUtils {

    public static final double EPSILON = StatisticUtils.EPSILON;

    public static double[][] multiply(double[][] matrix1, double[][] matrix2) {
        assert matrix1[0].length == matrix2.length : "Dimensions must match";

        double[][] result = new double[matrix1.length][matrix2[0].length];

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    result[i][j] = result[i][j] + matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return result;
    }

    public static double[][] transpose(double[][] matrix) {
        double[][] transposed = new double[matrix[0].length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }

        return transposed;
    }

    public static double[][] inverse(double[][] matrix) {
        //naive and not most efficient method for computing an inverse of a matrix
        double[][] workingMatrix = new double[matrix.length][2 * matrix[0].length];

        return null;
    }

    public static double[][] subtractRows(double[][] matrix, int firstRow, int secondRow, double factor) {
        assert !(firstRow == secondRow) : "both rows must not be the same";
        double[][] result = new double[matrix.length][matrix[0].length];

        //initialises result matrix except target row to modify from given matrix
        for (int i = 0; i < matrix.length; i++) {

            if (i == firstRow) {
                continue;
            }

            for (int j = 0; j < matrix[0].length; j++) {
                result[i][j] = matrix[i][j];
            }
        }

        for (int j = 0; j < matrix[0].length; j++) {
            result[firstRow][j] = matrix[firstRow][j] - matrix[secondRow][j] * factor;
        }

        return result;
    }

    public static boolean areMatricesApproximatelyEqual(double[][] matrix1, double[][] matrix2, double epsilon) {
        assert matrix1.length == matrix2.length && matrix1[0].length == matrix2[0].length : "Dimensions must be equal";

        for (int i = 0; i < matrix1.length; i++)  {
            for (int j = 0; j < matrix1[0].length; j++) {
                if (!StatisticUtils.isApproxEqual(matrix1[i][j], matrix2[i][j], epsilon)) {
                    //not equal therefore return false
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean areMatricesApproximatelyEqual(double[][] matrix1, double[][] matrix2) {
        return areMatricesApproximatelyEqual(matrix1, matrix2, EPSILON);
    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++)  {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

}
