# regression-algorithms
Implementation of different regression algorithms in Java.

# How to use the library
Currently, the library supports three types of regression: Simple linear regression, multiple linear regression and simple polynomial regression.

- Simple linear regression:
```java
//create a List of points (here manually inputed, the training data)
List<Point> points = Arrays.asList(new Point(1, 1), new Point(2, 3), new Point(4, 3),
                new Point(3, 2), new Point(5, 5));
//create the linear regression object passing the training data as the argument
SimpleLinearRegression regression = new SimpleLinearRegression(points);
//get slope coefficient
double slopeCoefficient = regression.getSlopeCoefficient();
//get intercept coefficient
double intercept coefficient = regression.getInterceptCoefficient();
//get a prediction for the training data supplied
double valueToPredict = 3.4;
double predictedValue = regression.getPrediction(valueToPredict);
//get root mean square error of training data
double rmse = regression.getRootMeanSquareError();
```

- Multiple Linear Regression
```java
//example using the data in testData3.txt
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
        
//creates the multiple linear regression object and computes the coefficients
MultipleLinearRegression mlr = new MultipleLinearRegression(points);
//get the coefficients
double[][] coefficients = mlr.getCoefficients();
//get the List of Multiple Points
List<MultiplePoint> list = mlr.getPoints();
//get a prediction for the given independent variables
double prediction = mlr.getPrediction(1.3, 2.78);
double[] values = new double[] {1.3, 2.78};
double prediction2 = mlr.getPrediction(values);
```

- Simple polynomial regression
```java
//as with Simple Linear Regression, supply a List of Points and the polynomial degree desired for the regression
int polynomialDegreeDesired = 3;
PolynomialRegression plr = new PolynomialRegression(points, polynomialDegreeDesired);
//get back the list with points
List<Point> list = plr.getPoints();
//get the polynomial degree
int pd = plr.getPolynomialDegree();
//get the computed coefficients
double[][] coefficients = plr.getCoefficients();
//get a prediction
double value = 3.54;
double prediction = plr.getPrediction(value);
//get RMSE error for training data:
double rmse = plr.getTrainingDataRootMeanSquareError();
//get RMSE error for test data (used in computation of optimal polynomial regression)
double rmse = plr.getTestDataRootMeanSquareError(testData);
```
  
 - Given test data to optimise the polynomial regression:
  ```java
  //computes the optimal polynomial regression for the supplied training data and test data
  //if we want to have standard output
  boolean terminalOutput = true;
  PolynomialRegression optimalPLR = PolynomialRegression.getOptimalPolynomialRegression(trainingData, testData, terminalOutput);
  ```
  Produced output for example in PolynomialRegression: 
  ```
  Points to analyse: 201
Thread: 0, Degree: 0, Error: 2449.8470901997493
Thread: 1, Degree: 4, Error: 1191.6880922848914
Thread: 0, Degree: 28, Error: 693792.2722850717
Thread: 1, Degree: 32, Error: 825061.6243167162
Thread: 2, Degree: 36, Error: 1291602.8773571763
Thread: 3, Degree: 40, Error: 3179747.571711865
...
Thread: 1, Degree: 7, Error: 169.2523672185814
...
Thread: 5, Degree: 52, Error: 1.9553379536494124E8
Thread: 5, Degree: 80, Error: 9.498507018861318E12
Thread: 6, Degree: 195, Error: 1.1208697729287957E33
Thread: 6, Degree: 196, Error: 1.6800940740814055E33
Thread: 6, Degree: 197, Error: 2.5183654098890806E33
Thread: 6, Degree: 198, Error: 3.774943548418527E33
Thread: 6, Degree: 199, Error: 5.658595080232076E33
Time required: 24.880461799s
Optimal degree: 7
  ```
