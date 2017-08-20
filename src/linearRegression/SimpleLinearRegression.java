package linearRegression;

import Utils.Point;
import Utils.StatisticUtils;

import java.util.List;

/**
 * Class representing a simple linear regression (one independent variable, one dependent variable). The line obtained
 * from the regression is of the form y = ax + b, where a is the slope and b is the intercept
 * @author gorosgobe
 */
public class SimpleLinearRegression {

    /* Line of form y = ax + b, where a is the slope and b is the intercept */
    /** The list of Points representing the training data*/
    private final List<Point> points;
    /** The slope coefficient, <em>a</em>x + b*/
    private double slopeCoefficient;
    /** The intercept coefficient, ax + <em>b</em>*/
    private double interceptCoefficient;

    /**
     * Constructor of a simple linear regression.
     * @param points the training points
     */
    public SimpleLinearRegression(List<Point> points) {
        this.points = points;
        this.slopeCoefficient = getSlopeCoefficient(points);
        this.interceptCoefficient = getInterceptCoefficient(points, slopeCoefficient);
    }

    /**
     * Gets the prediction for the supplied value, according to the trained model.
     * @param value the value to get the prediction of.
     * @return the prediction for the value supplied.
     */
    public double getPrediction(double value) {
        return value * slopeCoefficient + interceptCoefficient;
    }

    /**
     * Gets the slope coefficient, <em>a</em>x + b
     * @return the slope coefficient
     */
    public double getSlopeCoefficient() {
        return slopeCoefficient;
    }

    /**
     * Gets the intercept coefficient, ax + <em>b</em>
     * @return the intercept coefficient
     */
    public double getInterceptCoefficient() {
        return interceptCoefficient;
    }

    /**
     * Computes the Root Mean Square Error (RMSE) for the supplied training data,
     * @return
     */
    public double getRootMeanSquareError() {

        double meanSquareError =
                points.stream()
                .map(i -> Math.pow(getPrediction(i.getX()) - i.getY(), 2.0))
                .reduce(0.0, (a, b) -> a + b) / points.size();

        return Math.sqrt(meanSquareError);
    }

    /**
     * Computes the slope coefficient for the supplied list of points.
     * @param pointList the training data
     * @return the slope coefficient of the linear regression for the supplied data
     */
    public static double getSlopeCoefficient(List<Point> pointList) {
        return StatisticUtils.covariance(pointList)
                / StatisticUtils.variance(StatisticUtils.getListOfX(pointList));
    }

    /**
     * Computes the intercept coefficient for the supplied list of points and slope coefficient.
     * @param pointList the training data
     * @param slopeCoefficient the slope coefficient of the linear regression.
     * @return the intercept coefficient of the linear regression for the supplied data and the slope coefficient.
     */
    public static double getInterceptCoefficient(List<Point> pointList, double slopeCoefficient) {
        return StatisticUtils.mean(StatisticUtils.getListOfY(pointList))
                - slopeCoefficient * StatisticUtils.mean(StatisticUtils.getListOfX(pointList));
    }

}
