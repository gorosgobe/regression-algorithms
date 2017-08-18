package polynomialRegression;


import Utils.MatrixUtils;
import Utils.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

public class PolynomialRegression {

    private final List<Point> points;
    private final int polynomialDegree;
    private double[][] coefficients;

    /*
    Parameter column vector beta = (XTX)-1XTy where X is the design matrix.
    Polynomial degree is number of columns of matrix
     */

    public PolynomialRegression(List<Point> points, int polynomialDegree) {
        this.points = points;
        if (polynomialDegree < 0) {
            throw new IllegalArgumentException("Polynomial degree should be >= 0");
        }
        this.polynomialDegree = polynomialDegree;
    }

    public PolynomialRegression(List<Point> points) {
        this.points = points;
        this.polynomialDegree = 0;
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

    public int getOptimalPolynomialDegreeWithTestData(List<Point> testData) throws InterruptedException {

        long startTime = System.nanoTime();

        //gets max number of threads
        int threadNum = Runtime.getRuntime().availableProcessors() - 1;
        //creates an array of threads
        final Thread[] threads = new Thread[threadNum];
        //creates an array of polynomial degrees
        final int[] threadPolyDegrees = new int[threadNum];
        //creates an array of root mean squared errors
        final double[] threadRMSE = new double[threadNum];

        //for every available thread with index: threadIndex
        IntStream.range(0, threadNum).forEach(threadIndex -> {
            //create a new thread
            Thread t = new Thread(() -> {
                //with a mapping from RSMErrors to polynomial degrees
                Map<Double, Integer> errorsToDegree = new LinkedHashMap<>();

                //for the degrees corresponding to the thread index, compute the RSME errors and add it to the map
                for (int i = ((testData.size() - 1) * threadIndex) / threadNum;
                     i < (testData.size() - 1) * (threadIndex + 1)/ threadNum; i++) {

                    PolynomialRegression regression = new PolynomialRegression(getPoints(), i);
                    double error = regression.getTestDataRootMeanSquareError(testData);
                    System.out.println("Degree: " + i + ", Error: " + error);
                    errorsToDegree.put(error, i);

                }

                //for the degree section corresponding to the thread index, compute the minimum error and get its polynomial degree
                threadRMSE[threadIndex] = Collections.min(errorsToDegree.keySet());
                threadPolyDegrees[threadIndex] = errorsToDegree.get(threadRMSE[threadIndex]);

            });

            threads[threadIndex] = t;

        });

        //start threads
        for (Thread t : threads) {
            t.start();
        }
        //wait until the last thread to complete its process
        for (Thread t : threads) {
            t.join();
        }

        int minimalDegreeIndex = getIndexOfMinDouble(threadRMSE);

        long endTime = System.nanoTime();

        System.out.println("Time required: " + ((endTime - startTime) / 1000000000.0) + "s");
        return threadPolyDegrees[minimalDegreeIndex];

    }

    private int getIndexOfMinDouble(double[] doubles) {

        double current = Double.MAX_VALUE;
        int currentIndex = 0;

        for (int i = 0; i < doubles.length; i++) {
            if (doubles[i] < current) {
                current = doubles[i];
                currentIndex = i;
            }
        }

        return currentIndex;

    }

    public double getTrainingDataRootMeanSquareError() {
        double meanSquareError =
                points.stream()
                        .map(i -> Math.pow(getPrediction(i.getX()) - i.getY(), 2.0))
                        .reduce(0.0, (a, b) -> a + b) / points.size();

        return Math.sqrt(meanSquareError);
    }

    public double getTestDataRootMeanSquareError(List<Point> testData) {
        //model must be trained
        if (coefficients == null) {
            computeCoefficients();
        }

        double meanSquareError =
                testData.stream()
                        .map(i -> Math.pow(getPrediction(i.getX()) - i.getY(), 2.0))
                        .reduce(0.0, (a, b) -> a + b) / testData.size();

        return Math.sqrt(meanSquareError);
    }

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        File file = new File("src/testData2.txt");
        Scanner sc = new Scanner(file);
        sc.nextLine(); //ignores first line with comment
        List<Point> points = new ArrayList<>();
        List<Point> testData = new ArrayList<>();

        int count = 0;
        while (sc.hasNext()) {
            //assume number of tokens is multiple of 2
            String x = sc.next();
            String y = sc.next();
            if (count < 20) {
                points.add(new Point(Integer.parseInt(x), Integer.parseInt(y)));
            } else {
                testData.add(new Point(Integer.parseInt(x), Integer.parseInt(y)));
            }
            count++;
        }

        PolynomialRegression regression = new PolynomialRegression(points, 0);
        System.out.println(regression.getOptimalPolynomialDegreeWithTestData(testData));

    }


}
