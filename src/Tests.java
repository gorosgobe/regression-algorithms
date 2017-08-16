import Utils.StatisticUtils;
import Utils.Point;
import linearRegression.SimpleLinearRegression;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public class Tests {

    @Test
    public void meanTest() {
        List<Double> list1 = Arrays.asList(1.0, 3.0, 2.0, 4.0, 5.0);
        List<Double> list2 = Arrays.asList(1.0, 3.0, 3.0, 2.0, 5.0);
        Assert.assertTrue(StatisticUtils.isApproxEqual(StatisticUtils.mean(list1), 3.0));
        Assert.assertTrue(StatisticUtils.isApproxEqual(StatisticUtils.mean(list2), 2.8));

    }

    @Test
    public void varianceTest() {
        List<Double> list1 = Arrays.asList(1.0, 3.0, 2.0, 4.0, 5.0);
        List<Double> list2 = Arrays.asList(1.0, 3.0, 3.0, 2.0, 5.0);
        Assert.assertTrue(StatisticUtils.isApproxEqual(StatisticUtils.variance(list1), 10.0));
        Assert.assertTrue(StatisticUtils.isApproxEqual(StatisticUtils.variance(list2), 8.8));
    }

    @Test
    public void covarianceTest() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(2, 3), new Point(4, 3),
                new Point(3, 2), new Point(5, 5));
        Assert.assertTrue(StatisticUtils.isApproxEqual(StatisticUtils.covariance(points), 8.0));
    }

    @Test
    public void simpleLinearRegressionTest() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(2, 3), new Point(4, 3),
                new Point(3, 2), new Point(5, 5));
        SimpleLinearRegression regression = new SimpleLinearRegression(points);

        System.out.println(regression.getRootMeanSquareError());
        Assert.assertTrue(StatisticUtils.isApproxEqual(regression.getRootMeanSquareError(), 0.6928, 0.0001));
        Assert.assertTrue(StatisticUtils.isApproxEqual(regression.getInterceptCoefficient(), 0.4));
        Assert.assertTrue(StatisticUtils.isApproxEqual(regression.getSlopeCoefficient(), 0.8));


    }

}
