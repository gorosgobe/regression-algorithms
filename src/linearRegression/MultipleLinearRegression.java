package linearRegression;

import Utils.MatrixUtils;
import Utils.MultiplePoint;
import Utils.QRDecomposition;

import java.util.List;
import java.util.stream.Collectors;

import static Utils.QRDecomposition.solveByBackSubstitution;


/**
 * Class representing a multiple (two or more independent variables) linear regression.This is of the form
 * <em>ŷ i=b0+b1xi,1+b2xi,2+…+bp−1xi,p−1</em>
 * @author gorosgobe
 */
public class MultipleLinearRegression {

    /** The list of MultiplePoint representing the training data*/
    private final List<MultiplePoint> points;
    /** The coefficients to compute*/
    private double[][] coefficients;

    /**
     * Constructs a MultipleLinearRegression. Coefficients are computed upon creation of the object.
     * @param points the list containing the points with multiple independent variables and one dependent variable.
     */
    public MultipleLinearRegression(List<MultiplePoint> points) {
        this.points = points;
        checkAllPointsHaveSameNumberXs();
        computeCoefficients();
    }

    /**
     * Checks all points in the training data have the same number of independent variables
     */
    private void checkAllPointsHaveSameNumberXs() {
        int size = points.get(0).getXs().size();

        boolean haveSameNumber = points.stream()
                .filter(i -> i.getXs().size() == size)
                .collect(Collectors.toList()).size() == points.size();

        if (!haveSameNumber) {
            throw new IllegalArgumentException("All points supplied must have the same number of independent variables");
        }
    }

    /**
     * Computes the coefficients with the training data through QR decomposition and back substitution.
     */
    private void computeCoefficients() {
        double[][] designMatrix = generateDesignMatrix();
        double[][] responseMatrix = generateResponseMatrix();

        QRDecomposition decomp = new QRDecomposition(designMatrix);
        this.coefficients = solveByBackSubstitution(decomp.getR(), MatrixUtils.multiply(MatrixUtils.transpose(decomp.getQ()), responseMatrix));

    }

    /**
     * Gets a prediction given the independent variables.
     * @param independentVars the independent variables
     * @return the prediction of the trained model
     */
    public double getPrediction(double... independentVars) {
        if (independentVars.length != points.get(0).getXs().size()) {
            throw new IllegalArgumentException();
        }

        double result = 0.0;

        for (double i = 0.0; i < coefficients.length; i++) {
            result += coefficients[(int) i][0] * (i == 0.0 ? 1 : independentVars[(int) i - 1]);
        }

        return result;
    }


    /**
     * Generates the design matrix with the training data.
     * @return the design matrix of the training data supplied in the constructor.
     */
    // Matrix with the x of the points
    private double[][] generateDesignMatrix() {

        //width is points.get(0).getXs().size() + 1 as column 0 is a column of 1s
        double[][] designMatrix = new double[points.size()][points.get(0).getXs().size() + 1];

        //sets first column to be 1
        for (int i = 0; i < designMatrix.length; i++) {
            designMatrix[i][0] = 1;
        }

        //fills up the matrix with the points
        for (int i = 0; i < designMatrix.length; i++) {
            for (int j = 1; j < designMatrix[0].length; j++) {
                designMatrix[i][j] = points.get(i).getXs().get(j - 1);
            }
        }

        return designMatrix;
    }

    /**
     * Generates the response matrix with the training data.
     * @return the response matrix with the training data.
     */
    //Matrix with the y of the points
    private double[][] generateResponseMatrix() {
        double[][] responseMatrix = new double[points.size()][1];

        for (int i = 0; i < responseMatrix.length; i++) {
            responseMatrix[i][0] = points.get(i).getY();
        }

        return responseMatrix;
    }

    /**
     * Gets the points used as training data.
     * @return a list with the points used as training data.
     */
    public List<MultiplePoint> getPoints() {
        return points;
    }

    /**
     * Gets the coefficients computed with the training data.
     * @return the coefficients computed with the training data.
     */
    public double[][] getCoefficients() {
        return coefficients;
    }


}
