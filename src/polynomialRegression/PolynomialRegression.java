package polynomialRegression;


import Utils.MatrixUtils;
import Utils.Point;

import java.util.List;

public class PolynomialRegression {

    private final List<Point> points;
    private final int polynomialDegree;
    private double[][] coefficients;

    /*
    Parameter column vector beta = (XTX)-1XTy where X is the design matrix.
    Polynomial grade is number of columns of matrix
     */

    public PolynomialRegression(List<Point> points, int polynomialDegree) {
        this.points = points;
        if (polynomialDegree < 1) {
            throw new IllegalArgumentException("Polynomial degree should be >= 1");
        }
        this.polynomialDegree = polynomialDegree;
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getPolynomialDegree() {
        return polynomialDegree;
    }

    public double[][] getCoefficients() {
        return coefficients;
    }

    // Matrix with the x of the points
    public double[][] generateDesignMatrix() {

        //width is polynomial grade + 1 as column 0 is a column of 1s
        double[][] designMatrix = new double[points.size()][polynomialDegree + 1];

        //sets first column to be 1
        for (int i = 0; i < designMatrix.length; i++) {
            designMatrix[i][0] = 1;
        }

        //fills up the matrix with the points
        for (int i = 0; i < designMatrix.length; i++) {
            for (int j = 1; j < designMatrix[0].length; j++) {
                designMatrix[i][j] = Math.pow(points.get(i).getX(), j);
            }
        }

        return designMatrix;
    }

    //Matrix with the y of the points
    public double[][] generateResponseMatrix() {
        double[][] responseMatrix = new double[points.size()][1];

        for (int i = 0; i < responseMatrix.length; i++) {
            responseMatrix[i][0] = points.get(i).getY();
        }

        return responseMatrix;
    }

    public void computeCoefficients() {
        double[][] coefficients;
        double[][] designMatrix = generateDesignMatrix();
        double[][] responseMatrix = generateResponseMatrix();
        double[][] designMatrixTranspose = MatrixUtils.transpose(designMatrix);

        //coefficient matrix is given by the equation described above
        coefficients = MatrixUtils.multiply(MatrixUtils.multiply(MatrixUtils.inverse(
                MatrixUtils.multiply(designMatrixTranspose, designMatrix)), designMatrixTranspose), responseMatrix);

        this.coefficients = coefficients;
    }

    public double getPrediction(double value) {

        double result = 0.0;

        for (double i = 0.0; i < coefficients.length; i++) {
            result += coefficients[(int) i][0] * Math.pow(value, i);
        }

        return result;
    }
    

}
