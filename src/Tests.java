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
    public void matrixInverseTest() {

        double[][] matrix1 = new double[][] {
                {1, 3},
                {2, 7}
        };

        double[][] intermediateRightResult = new double[][] {
                {7, -3},
                {-2, 1}
        };

        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(intermediateRightResult, MatrixUtils.inverse(matrix1)));

        double[][] matrix2 = new double[][] {
                {1, 2, 3},
                {2, 5, 3},
                {1,  0, 8}
        };

        double[][] inverse2 = new double[][] {
                {-40, 16, 9},
                {13, -5, -3},
                {5,  -2, -1}
        };

        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(inverse2, MatrixUtils.inverse(matrix2)));

        double[][] matrix3 = new double[][] {
                {10, 2, 6},
                {12, 9, 7},
                {3,  11, 4}
        };

        double[][] inverse3 = new double[][] {
                {-0.247, 0.3494, -0.241},
                {-0.1627, 0.1325, 0.012},
                {0.6325, -0.6265, 0.3976}
        };

        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(inverse3, MatrixUtils.inverse(matrix3), 0.001));

        double[][] matrix4 = new double[][] {
                {4.0, 10.0, 5.0, 6.0, 7.0},
                {8.0, 9.0, 3.0, 11.0, 12.0},
                {13.0, 14.0, 15.0, 16.0, 17.0},
                {18.0, 2.0, 19.0, 20.0, 21.0},
                {22.0, 23.0, 24.0, 25.0, 26.0}
        };

        double[][] inverse4 = new double[][] {
                { 0.7273, 0, -1.6869, 0.2727, 0.6869},
                {0.0455, 0, -0.0707, -0.0455, 0.0707},
                {0, -0.1429, 0.2222, 0, -0.0794},
                {-3.0455, 0.2857, 3.6263, -0.9545, -0.912},
                {2.2727, -0.1429, -2.202, 0.7273, 0.3449}
        };

        System.out.println("hello");
        MatrixUtils.printMatrix(MatrixUtils.inverse(matrix4));
        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(inverse4, MatrixUtils.inverse(matrix4), 0.001));


    }

}
