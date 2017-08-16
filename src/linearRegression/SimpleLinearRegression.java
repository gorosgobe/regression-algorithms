package linearRegression;

import Utils.Point;
import Utils.StatisticUtils;

import java.util.List;

public class SimpleLinearRegression {

    /* Line of form y = ax + b, where a is the slope and b is the intercept */
    private final List<Point> points;
    private double slopeCoefficient;
    private double interceptCoefficient;

    public SimpleLinearRegression(List<Point> points) {
        this.points = points;
        this.slopeCoefficient = getSlopeCoefficient(points);
        this.interceptCoefficient = getInterceptCoefficient(points, slopeCoefficient);
    }

    public double getPrediction(double value) {
        return value * slopeCoefficient + interceptCoefficient;
    }

    public double getSlopeCoefficient() {
        return slopeCoefficient;
    }

    public double getInterceptCoefficient() {
        return interceptCoefficient;
    }

    public double getRootMeanSquareError() {

        double meanSquareError =
                points.stream()
                .map(i -> Math.pow(getPrediction(i.getX()) - i.getY(), 2.0))
                .reduce(0.0, (a, b) -> a + b) / points.size();

        return Math.sqrt(meanSquareError);
    }

    public static double getSlopeCoefficient(List<Point> pointList) {
        return StatisticUtils.covariance(pointList)
                / StatisticUtils.variance(StatisticUtils.getListOfX(pointList));
    }

    public static double getInterceptCoefficient(List<Point> pointList, double slopeCoefficient) {
        return StatisticUtils.mean(StatisticUtils.getListOfY(pointList))
                - slopeCoefficient * StatisticUtils.mean(StatisticUtils.getListOfX(pointList));
    }

}
