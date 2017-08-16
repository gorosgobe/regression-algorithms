package Utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticUtils {

    public static final double EPSILON = 0.00001;

    public static boolean isApproxEqual(double d1, double d2) {
        return Math.abs(d1 - d2) <= EPSILON;
    }

    public static double mean(List<Double> list) {

        double sum = list.stream().reduce(0.0, (a, b) -> a + b);
        return sum / list.size();

    }

    public static double mean(Double[] doubles) {
        return mean(Arrays.asList(doubles));
    }

    public static double variance(List<Double> list) {

        double mean = mean(list);

        return list.stream()
                .map(i -> (i - mean) * (i - mean))
                .reduce(0.0, (a, b) -> a + b);
    }

    public static double variance(Double[] doubles) {
        return variance(Arrays.asList(doubles));
    }

    public static double covariance(List<Double> xs, List<Double> ys) {

        double meanX = mean(xs);
        double meanY = mean(ys);

        double covariance = 0.0;

        for (int i = 0; i < xs.size(); i++) {
            covariance += (xs.get(i) - meanX) * (ys.get(i) - meanY);
        }

        return covariance;

    }

    public static double covariance(List<Point> points) {
        return covariance(getListOfX(points), getListOfY(points));
    }

    public static List<Double> getListOfX(List<Point> list) {
        return list.stream()
                .map(i -> i.getX())
                .collect(Collectors.toList());
    }

    public static List<Double> getListOfY(List<Point> list) {
        return list.stream()
                .map(i -> i.getY())
                .collect(Collectors.toList());
    }


}
