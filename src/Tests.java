import Utils.*;
import linearRegression.MultipleLinearRegression;
import linearRegression.SimpleLinearRegression;
import org.junit.Assert;
import org.junit.Test;
import polynomialRegression.PolynomialRegression;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static Utils.QRDecomposition.solveByBackSubstitution;
import static org.junit.Assert.assertTrue;


public class Tests {

    @Test
    public void meanTest() {
        List<Double> list1 = Arrays.asList(1.0, 3.0, 2.0, 4.0, 5.0);
        List<Double> list2 = Arrays.asList(1.0, 3.0, 3.0, 2.0, 5.0);
        assertTrue(StatisticUtils.isApproxEqual(StatisticUtils.mean(list1), 3.0));
        assertTrue(StatisticUtils.isApproxEqual(StatisticUtils.mean(list2), 2.8));

    }

    @Test
    public void varianceTest() {
        List<Double> list1 = Arrays.asList(1.0, 3.0, 2.0, 4.0, 5.0);
        List<Double> list2 = Arrays.asList(1.0, 3.0, 3.0, 2.0, 5.0);
        assertTrue(StatisticUtils.isApproxEqual(StatisticUtils.variance(list1), 10.0));
        assertTrue(StatisticUtils.isApproxEqual(StatisticUtils.variance(list2), 8.8));
    }

    @Test
    public void covarianceTest() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(2, 3), new Point(4, 3),
                new Point(3, 2), new Point(5, 5));
        assertTrue(StatisticUtils.isApproxEqual(StatisticUtils.covariance(points), 8.0));
    }

    @Test
    public void simpleLinearRegressionTest() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(2, 3), new Point(4, 3),
                new Point(3, 2), new Point(5, 5));
        SimpleLinearRegression regression = new SimpleLinearRegression(points);

        assertTrue(StatisticUtils.isApproxEqual(regression.getRootMeanSquareError(), 0.6928, 0.0001));
        assertTrue(StatisticUtils.isApproxEqual(regression.getInterceptCoefficient(), 0.4));
        assertTrue(StatisticUtils.isApproxEqual(regression.getSlopeCoefficient(), 0.8));
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

        assertTrue(MatrixUtils.areMatricesApproximatelyEqual(
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

        assertTrue(MatrixUtils.areMatricesApproximatelyEqual(
                MatrixUtils.multiply(multiplicand, multiplier), result1, 0.00001));

        double[][] a = {
                {5.0, -4.0},
                {3.0, 1.0},
                {4.0, 6.0},
                {7.0, 8.0}
        };

        double[][] b = {
                {-1.0, 9.0, 5.0, -3.0},
                {2.0, -2.0, 10.0, -4.0}
        };

        double[][] c = {
                {-13.0, 53.0, -15.0, 1.0},
                {-1.0, 25.0, 25.0, -13.0},
                {8.0, 24.0, 80.0, -36.0},
                {9.0, 47.0, 115.0, -53.0}
        };

        assertTrue(MatrixUtils.areMatricesApproximatelyEqual(
                MatrixUtils.multiply(a, b), c, 0.00001));

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

        assertTrue(MatrixUtils.areMatricesApproximatelyEqual
                (MatrixUtils.transpose(matrix1), result1, 0.0001));

        double[][] matrix2 = new double[][] {
                {0.0},
                {-4.0},
                {7.0}
        };

        double[][] result2 = new double[][] {
                {0.0, -4.0, 7.0}
        };

        assertTrue(MatrixUtils.areMatricesApproximatelyEqual
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

        assertTrue(MatrixUtils.areMatricesApproximatelyEqual(result1,
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

        assertTrue(MatrixUtils.areMatricesApproximatelyEqual(intermediateRightResult, MatrixUtils.inverse(matrix1)));

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

        assertTrue(MatrixUtils.areMatricesApproximatelyEqual(inverse2, MatrixUtils.inverse(matrix2)));

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

        assertTrue(MatrixUtils.areMatricesApproximatelyEqual(inverse3, MatrixUtils.inverse(matrix3), 0.001));

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

        assertTrue(MatrixUtils.areMatricesApproximatelyEqual(inverse4, MatrixUtils.inverse(matrix4), 0.001));
    }

    @Test
    public void testInverse() throws Exception {
        double[][] matrix = new double[][]{{1,2,3},{0,4,5},{1,0,6}};
        double[][] inverse = MatrixUtils.inverse(matrix);
        assertTrue(Math.abs(12.0/11.0-inverse[0][0]) < 1e-5);
        assertTrue(Math.abs(-6.0/11.0-inverse[0][1]) < 1e-5);
        assertTrue(Math.abs(-1.0/11.0-inverse[0][2]) < 1e-5);
        assertTrue(Math.abs(5.0/22.0-inverse[1][0]) < 1e-5);
        assertTrue(Math.abs(3.0/22.0-inverse[1][1]) < 1e-5);
        assertTrue(Math.abs(-5.0/22.0-inverse[1][2]) < 1e-5);
        assertTrue(Math.abs(-2.0/11.0-inverse[2][0]) < 1e-5);
        assertTrue(Math.abs(1.0/11.0-inverse[2][1]) < 1e-5);
        assertTrue(Math.abs(2.0/11.0-inverse[2][2]) < 1e-5);

    }

    @Test
    public void polynomialRegressionTest1() throws FileNotFoundException {

        File file = new File("src/testData1.txt");
        Scanner sc = new Scanner(file);
        sc.nextLine(); //ignores first line with comment
        List<Point> points = new ArrayList<>();

        while (sc.hasNext()) {
            //assume number of tokens is multiple of 2
            String x = sc.next();
            String y = sc.next();
            points.add(new Point(Integer.parseInt(x), Integer.parseInt(y)));
        }

        PolynomialRegression polynomialRegression = new PolynomialRegression(points, 2);
        //polynomialRegression.computeCoefficients();
        double[][] coefficients = polynomialRegression.getCoefficients();

        assertTrue(StatisticUtils.isApproxEqual(coefficients[0][0], -1216.143887));
        assertTrue(StatisticUtils.isApproxEqual(coefficients[1][0],  2.39893));
        assertTrue(StatisticUtils.isApproxEqual(coefficients[2][0], -0.00045));

    }

    @Test
    public void polynomialRegressionTest2() throws FileNotFoundException {
        File file = new File("src/testData2.txt");
        Scanner sc = new Scanner(file);
        sc.nextLine(); //ignores first line with comment
        List<Point> points = new ArrayList<>();

        while (sc.hasNext()) {
            //assume number of tokens is multiple of 2
            String x = sc.next();
            String y = sc.next();
            points.add(new Point(Integer.parseInt(x), Integer.parseInt(y)));
        }

        PolynomialRegression polynomialRegression = new PolynomialRegression(points, 2);
        //polynomialRegression.computeCoefficients();
        double[][] coefficients = polynomialRegression.getCoefficients();

        //coefficient real data has been rounded in the website that supplied the data, hence the big epsilon
        assertTrue(StatisticUtils.isApproxEqual(coefficients[0][0], 13.6, 0.1));
        assertTrue(StatisticUtils.isApproxEqual(coefficients[1][0],  54.05, 0.01));
        assertTrue(StatisticUtils.isApproxEqual(coefficients[2][0], -4.719, 0.01));
    }

    @Test
    public void QRDecompositionTest() {
        double[][] matrix = new double[][] {
                {1, 1, 0},
                {1, 0, 1},
                {0,  1, 1}
        };

        double[][] Q = new double[][] {
                {1 / Math.sqrt(2), 1 / Math.sqrt(6), -1 / Math.sqrt(3)},
                {1 / Math.sqrt(2), -1 / Math.sqrt(6), 1 / Math.sqrt(3)},
                {0,  2 / Math.sqrt(6), 1 / Math.sqrt(3)}
        };

        double[][] R = new double[][] {
                {2 / Math.sqrt(2), 1 / Math.sqrt(2), 1 / Math.sqrt(2)},
                {0, 3 / Math.sqrt(6), 1 / Math.sqrt(6)},
                {0,  0, 2 / Math.sqrt(3)}
        };

        QRDecomposition decomposition = new QRDecomposition(matrix);

        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(Q, decomposition.getQ()));
        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(R, decomposition.getR()));
    }

    @Test
    public void QRDecompositionTest2() {
        double[][] matrix = new double[][] {
                {12, -51, 4},
                {6, 167, -68},
                {-4,  24, -41}
        };

        double[][] Q = new double[][] {
                {6/7.0, -69/175.0, -58/175.0},
                {3/7.0, 158/175.0, 6/175.0},
                {-2/7.0,  6/35.0, -33/35.0}
        };

        double[][] R = new double[][] {
                {14, 21, -14},
                {0, 175, -70},
                {0,  0, 35}
        };

        QRDecomposition decomposition = new QRDecomposition(matrix);

        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(Q, decomposition.getQ()));
        Assert.assertTrue(MatrixUtils.areMatricesApproximatelyEqual(R, decomposition.getR()));
    }

    @Test
    public void substitutionTest() {
        double[][] R = new double[][] {
                {1, 2, 1},
                {0, -4, 1},
                {0,  0, -2}
        };

        double[][] y = new double[][] {
                {5},
                {2},
                {4}
        };

        double[][] result = solveByBackSubstitution(R, y);
        Assert.assertTrue(StatisticUtils.isApproxEqual(result[0][0], 9.0));
        Assert.assertTrue(StatisticUtils.isApproxEqual(result[1][0], -1));
        Assert.assertTrue(StatisticUtils.isApproxEqual(result[2][0], -2));


    }

    @Test
    public void multipleLinearRegressionTest() throws FileNotFoundException {
        File file = new File("src/testData3.txt");
        Scanner sc = new Scanner(file);
        sc.nextLine(); //ignores first line with comment
        List<MultiplePoint> points = new ArrayList<>();

        while (sc.hasNext()) {
            //assume number of tokens is multiple of 3
            String y = sc.next();
            String x1 = sc.next();
            String x2 = sc.next();
            List<Double> list = new ArrayList<>();
            list.add(Double.parseDouble(x1));
            list.add(Double.parseDouble(x2));
            points.add(new MultiplePoint(list, Double.parseDouble(y)));
        }

        MultipleLinearRegression mlr = new MultipleLinearRegression(points);
        double[][] coeffs = mlr.getCoefficients();

        //coefficient real data has been rounded in the website that supplied the data, hence the big epsilons
        Assert.assertTrue(StatisticUtils.isApproxEqual(coeffs[0][0], 86.0, 0.1));
        Assert.assertTrue(StatisticUtils.isApproxEqual(coeffs[1][0], -5.33, 0.001));
        Assert.assertTrue(StatisticUtils.isApproxEqual(coeffs[2][0], 31.10, 0.01));
        Assert.assertTrue(StatisticUtils.isApproxEqual(mlr.getPrediction(2, 5), 230.84, 0.1));

    }

    @Test(expected = IllegalArgumentException.class)
    public void multipleLinearRegressionTest2() throws FileNotFoundException {
        File file = new File("src/testData3.txt");
        Scanner sc = new Scanner(file);
        sc.nextLine(); //ignores first line with comment
        List<MultiplePoint> points = new ArrayList<>();
        int count = 0;
        while (sc.hasNext()) {
            String x1 = null;
            //assume number of tokens is multiple of 3
            String y = sc.next();
            if (count != 2) {
                x1 = sc.next();
            } else {
                sc.next();
            }
            String x2 = sc.next();
            List<Double> list = new ArrayList<>();
            if (count != 2) {
                list.add(Double.parseDouble(x1));
            }
            list.add(Double.parseDouble(x2));
            points.add(new MultiplePoint(list, Double.parseDouble(y)));
            count++;
        }

        MultipleLinearRegression mlr = new MultipleLinearRegression(points);

    }

}
