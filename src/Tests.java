import Utils.MatrixUtils;
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

        Assert.assertTrue(StatisticUtils.isApproxEqual(regression.getRootMeanSquareError(), 0.6928, 0.0001));
        Assert.assertTrue(StatisticUtils.isApproxEqual(regression.getInterceptCoefficient(), 0.4));
        Assert.assertTrue(StatisticUtils.isApproxEqual(regression.getSlopeCoefficient(), 0.8));
    }

    @Test
    public void matrixMultiplicationTest1() {
        double[][] multiplicand = new double[][] {
                {3, -1, 2},
                {2,  0, 1},
                {1,  2, 1}
        };
        double[][] multiplier = new double[][] {
                {2, -1, 1},
                {0, -2, 3},
                {3,  0, 1}
        };

        double[][] result1 = new double[][] {
                {12.0, -1.0, 2.0},
                {7.0, -2.0, 3.0},
                {5.0,  -5.0, 8.0}
        };

        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(
                        MatrixUtils.multiply(multiplicand, multiplier), result1, 0.00001));

    }

    @Test
    public void matrixMultiplicationTest2() {
        double[][] multiplicand = new double[][] {
                {1, 2, 0},
                {-1, 3, 1},
                {2, -2, 1}
        };
        double[][] multiplier = new double[][] {
                {2},
                {-1},
                {1}
        };

        double[][] result1 = new double[][] {
                {0.0},
                {-4.0},
                {7.0}
        };

        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(
                MatrixUtils.multiply(multiplicand, multiplier), result1, 0.00001));

    }

    @Test
    public void matrixTransposeTest() {
        double[][] matrix1 = new double[][] {
                {1, 2, 0},
                {-1, 3, 1},
                {2, -2, 1}
        };
        double[][] result1 = new double[][] {
                {1, -1, 2},
                {2, 3, -2},
                {0, 1, 1}
        };

        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual
                (MatrixUtils.transpose(matrix1), result1, 0.0001));

        double[][] matrix2 = new double[][] {
                {0.0},
                {-4.0},
                {7.0}
        };

        double[][] result2 = new double[][] {
                {0.0, -4.0, 7.0}
        };

        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual
                (MatrixUtils.transpose(matrix2), result2, 0.0001));
    }

    @Test
    public void matrixSubtractRowsTest() {
        double[][] matrix1 = new double[][] {
                {1, 2, 0},
                {-1, 3, 1},
                {2, -2, 1}
        };

        double[][] result1 = new double[][] {
                {2, -1, -1},
                {-1, 3, 1},
                {2, -2, 1}
        };

        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(result1,
                MatrixUtils.subtractRows(matrix1, 0, 1, 1), 0.0001));
    }

    @Test
    public void matrixIntermediateInverseTest() {
        double[][] matrix1 = new double[][] {
                {1, 3},
                {2, 7}
        };

        double[][] intermediateRightResult = new double[][] {
                {1, 0},
                {-2, 1}
        };

        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(intermediateRightResult, MatrixUtils.inverse(matrix1)));

        double[][] matrix2 = new double[][] {
                {1, 2, 3},
                {2, 5, 3},
                {1,  0, 8}
        };

        double[][] intermediateRightResult2 = new double[][] {
                {2, -1, 1},
                {0, -2, 3},
                {3,  0, 1}
        };

    }

}
