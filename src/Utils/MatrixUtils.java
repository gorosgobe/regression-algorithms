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
        assert matrix.length == matrix[0].length : "Matrix has to have nxn entries";

        if (matrix.length == 1) {
            double[][] result = new double[1][1];
            result[0][0] = 1 / matrix[0][0];
            return result;
        }

        //all inverses dealt with are 2x2 or bigger

        //naive and not most efficient method for computing an inverse of a matrix
        double[][] workingMatrix = new double[matrix.length][2 * matrix[0].length];

        //initialises identity matrix in right part of working matrix
        for (int i = 0; i < workingMatrix.length; i++) {
                workingMatrix[i][i + workingMatrix[0].length / 2] = 1;
        }

        //initialises left part of working matrix to be the given matrix
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, workingMatrix[i], 0, matrix[0].length);
        }

        //converts matrix in upper triangular with 1s in diagonal
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                workingMatrix = subtractRows(workingMatrix, j, i, workingMatrix[j][i] / workingMatrix[i][i]);
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            workingMatrix = multiplyRow(workingMatrix, i, 1 / workingMatrix[i][i]);
        }

        //takes care of last (higher row and column index from matrix) number, and converts it to 1 by
        // dividing the whole row
        workingMatrix = multiplyRow(workingMatrix, matrix.length - 1,
                1 / workingMatrix[matrix.length - 1][matrix.length - 1]);

        //puts the left part of working matrix in RREF (reduced row echelos form)
        //starts by ignoring the first column as that one is already in RREF (matrix inputted was at least 2x2)
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                workingMatrix = subtractRows(workingMatrix, i, j, workingMatrix[i][j]);
            }
        }



        //selects right part of working matrix
        double[][] result = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = workingMatrix[0].length / 2; j < workingMatrix[0].length; j++) {
                result[i][j - workingMatrix[0].length / 2] = workingMatrix[i][j];
            }
        }

        return result;
    }

    public static double[][] subtractRows(double[][] matrix, int firstRow, int secondRow, double factor) {
        assert !(firstRow == secondRow) : "both rows must not be the same";
        double[][] result = new double[matrix.length][matrix[0].length];

        //initialises result matrix except target row to modify from given matrix
        for (int i = 0; i < matrix.length; i++) {

            if (i == firstRow) {
                continue;
            }

            System.arraycopy(matrix[i], 0, result[i], 0, matrix[0].length);
        }

        for (int j = 0; j < matrix[0].length; j++) {
            result[firstRow][j] = matrix[firstRow][j] - matrix[secondRow][j] * factor;
        }

        return result;
    }

    public static double[][] multiplyRow(double[][] matrix, int row, double factor) {
        double[][] result = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            if (i == row) {
                continue;
            }

            System.arraycopy(matrix[i], 0, result[i], 0, matrix[0].length);
        }

        for (int j = 0; j < matrix[0].length; j++) {
            result[row][j] = matrix[row][j] * factor;
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
