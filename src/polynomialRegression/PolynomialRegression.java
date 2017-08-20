package polynomialRegression;


import Utils.MatrixUtils;
import Utils.Point;
import Utils.QRDecomposition;

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
        //NAIVE AND INEFFICIENT WAY:
//        coefficients = MatrixUtils.multiply(MatrixUtils.multiply(MatrixUtils.inverse(
//                MatrixUtils.multiply(designMatrixTranspose, designMatrix)), designMatrixTranspose), responseMatrix);

        //QR DECOMPOSITION AND BACK SUBSTITUTION
        QRDecomposition decomp = new QRDecomposition(designMatrix);
        coefficients = solveByBackSubstitution(decomp.getR(), MatrixUtils.multiply(MatrixUtils.transpose(decomp.getQ()), responseMatrix));

        this.coefficients = coefficients;
    }

    public static double[][] solveByBackSubstitution(double[][] r, double[][] qty) {
        double[][] result = new double[r[0].length][1];

        for (int i = r[0].length - 1; i >= 0; i--) {
            result[i][0] = qty[i][0];
            for (int j = i + 1; j < r[0].length; j++) {
                result[i][0] = result[i][0] - (r[i][j] * result[j][0]);
            }
            result[i][0] = result[i][0] / r[i][i];
        }

        return result;
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
        final Integer[] sequence = new Integer[testData.size() - 1];

        for (int i = 0; i < testData.size() - 1; i++) {
            sequence[i] = i;
        }

        //creates an even distribution of polynomial degrees so each thread does approximately the same amount of work
        distribute(sequence, threadNum);
        for (int i : sequence) {
            System.out.print(i + " ");
        }
        System.out.println();

        //for every available thread with index: threadIndex
        IntStream.range(0, threadNum).forEach(threadIndex -> {
            //create a new thread
            Thread t = new Thread(() -> {
                //with a mapping from RSMErrors to polynomial degrees
                Map<Double, Integer> errorsToDegree = new LinkedHashMap<>();

                for (int i = (sequence.length * threadIndex) / threadNum;
                     i < (sequence.length * (threadIndex + 1)) / threadNum; i++) {

                    PolynomialRegression regression = new PolynomialRegression(getPoints(), sequence[i]);
                    double error = regression.getTestDataRootMeanSquareError(testData);
                    System.out.println("Thread: " + threadIndex + ", Degree: " +sequence[i] + ", Error: " + error);
                    errorsToDegree.put(error, sequence[i]);

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

    private static List<Integer[]> splitArray(Integer[] items, int maxSubArraySize) {
        List<Integer[]> result = new ArrayList<>();

        if (items == null || items.length == 0) {
            return result;
        }

        int from = 0;
        int to = 0;
        int slicedItems = 0;
        while (slicedItems < items.length) {
            to = from + Math.min(maxSubArraySize, items.length - to);
            Integer[] slice = Arrays.copyOfRange(items, from, to);
            result.add(slice);
            slicedItems += slice.length;
            from = to;
        }

        return result;
    }

    private void distribute(Integer[] array, int numThreads) {

        List<Integer[]> list = splitArray(array, array.length / numThreads);
        int count = 0;

        for (int i = 0; i < array.length / numThreads; i++) {
            for (int j = 0; j < numThreads; j++) {
                array[count] = list.get(j)[count / numThreads];
                count++;
            }
        }


    }
    public static Integer[] concat(Integer[] first, Integer[] second) {
        Integer[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    private void swap(int[] array, int i, int j) {

        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
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
        for (double i = -2.0; i < 2.0; i += 0.01) {
            //assume number of tokens is multiple of 2

            Point point = new Point(i, 0.7483924 * Math.pow(i, 7)
                    + 13.431 * Math.pow(i, 6)
                    + -12.35161212 * Math.pow(i, 5)
                    + 0.0000012 * Math.pow(i, 4)
                    + -9.99991212 * Math.pow(i, 3)
                    + -34.4300009 * Math.pow(i, 2)
                    + 0.7483924 * i
                    + Math.random());

            if (count < 200) {
                points.add(point);
            } else {
                testData.add(point);
            }
            count++;
        }
//        int count = 0;
//        while (sc.hasNext()) {
//            //assume number of tokens is multiple of 2
//            String x = sc.next();
//            String y = sc.next();
//            if (count < 60) {
//                points.add(new Point(Integer.parseInt(x), Integer.parseInt(y)));
//            } else {
//                testData.add(new Point(Integer.parseInt(x), Integer.parseInt(y)));
//            }
//            count++;
//        }

        System.out.println("Points to analyse: " + testData.size());

        PolynomialRegression regression = new PolynomialRegression(points, 0);
        System.out.println(regression.getOptimalPolynomialDegreeWithTestData(testData));

    }


//    int count = 0;
//        for (double i = -2.0; i < 2.0; i += 0.01) {
//        //assume number of tokens is multiple of 2
//
//        Point point = new Point(i, 0.7483924 * Math.pow(i, 7)
//                + 13.431 * Math.pow(i, 6)
//                + -12.35161212 * Math.pow(i, 5)
//                + 0.0000012 * Math.pow(i, 4)
//                + -9.99991212 * Math.pow(i, 3)
//                + -34.4300009 * Math.pow(i, 2)
//                + 0.7483924 * i
//                + Math.random());
//
//        if (count < 200) {
//            points.add(point);
//        } else {
//            testData.add(point);
//        }
//        count++;
//    }

    /* EFFICIENCY ANALYSIS WITH DIFFERENT IMPLEMENTATIONS */
    //with this configuration:
    //naive inverse and naive normal equations: 129.705
    //qr inverse and naive normal equations: 79.82s
    //with qr decomposition and back substitution: 20.8814s
    //with qr decomposition, back substitution and distribution amongs threads: 14.1829s


}
