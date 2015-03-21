package com.six.hack;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.six.hack.model.Outlier;
import com.six.hack.model.Price;
import com.six.hack.model.PriceStream;

public class ToleranceChecker extends Thread {

    @Autowired
    private WebController controller;
    private SimpleMahout simpleMahout;
    public ToleranceChecker() {
    	simpleMahout = new SimpleMahout(Arrays.asList("897789","275191"));
    }

    @PostConstruct
    public void startChecker() {
        start();
    }

    @Override
    public void run() {
        while (true) {
        	Outlier outlier = simpleMahout.processNextLine();
        	if(outlier != null)
        	{
        		controller.toQueue(outlier);
        	}
            //readData();
            //break;
        }
    }

    public static Vector getVector(Point point) {
        Vector v = new DenseVector(3);
        v.set(0, point.x);
        v.set(1, point.y);
        v.set(2, 1);
        return v;
    }

    public void readData() {
        Map<Point, Integer> points = new HashMap<Point, Integer>();

        int i = 0;
        OnlineLogisticRegression learningAlgo = new OnlineLogisticRegression();
        Vector vt = new RandomAccessSparseVector(3);
        double multiplier = 2;
        double divider = 2;
        for (Price price : new PriceStream()) {
            if (price.getMarketCode() == 2979 && price.getCurrencyCode() == 272 && price.getValorNumber() == 275191
                    && (price.getValueType() == 3 || price.getValueType() == 2) && price.getStatisticType() == 3) {
                double priceValue = price.getValue();
                points.put(new Point(priceValue, priceValue), 0);

                if (i == 21) {

                    points.put(new Point(priceValue * multiplier, priceValue * multiplier), 1);
                    points.put(new Point(priceValue * multiplier, priceValue * multiplier), 1);
                    points.put(new Point(priceValue * multiplier, priceValue * multiplier), 1);
                    points.put(new Point(priceValue * multiplier, priceValue * multiplier), 1);

                    // points.put(new Point(999, 999), 1);
                    // points.put(new Point(5/100, 5/100), 2);
                    points.put(new Point(priceValue / divider, priceValue / divider), 2);
                    points.put(new Point(priceValue / divider, priceValue / divider), 2);
                    points.put(new Point(priceValue / divider, priceValue / divider), 2);
                    points.put(new Point(priceValue / divider, priceValue / divider), 2);

                    System.out.println("check num = " + priceValue / divider + "<>" + priceValue * multiplier);

                    learningAlgo = new OnlineLogisticRegression(3, 3, new L1());
                    learningAlgo.lambda(0.01);
                    learningAlgo.learningRate(10);

                    System.out.println("training model  \n");

                    final List<Point> keys = Lists.newArrayList(points.keySet());
                    // 200 times through the training data is probably
                    // over-kill. It doesn't matter
                    // for tiny data. The key here is total number of points
                    // seen, not number of passes

                    for (int j = 0; j < 200; j++) {
                        // randomize training data on each iteration
                        Collections.shuffle(keys);
                        for (Point point : keys) {
                            Vector v = getVector(point);
                            learningAlgo.train(points.get(point), v);
                        }
                    }
                    learningAlgo.close();
                    System.out.println("ans = ");
                    System.out.println("no of categories = " + learningAlgo.numCategories());
                    System.out.println("no of features = " + learningAlgo.numFeatures());

                } else if (i > 21) {
                    vt.set(0, priceValue);
                    vt.set(1, priceValue);
                    vt.set(2, 1);

                    Vector r = learningAlgo.classifyFull(vt);
                    // System.out.println(r);
                    if (r.get(1) > .6 || r.get(2) > .6) {
                        // if (r.get(1) > .1 ) {
                        controller.toQueue(new Outlier(price));

                        System.out.println("GSN = " + price.getGsn() + " price = " + priceValue);
                        System.out.println("Probability of cluster 0 = " + r.get(0));
                        System.out.println("Probability of cluster 1 = " + r.get(1));
                        System.out.println("Probability of cluster 2 = " + r.get(2));
                    }
                }
                i++;

            }
        }

    }

    class Point {

        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object arg0) {
            Point p = (Point) arg0;
            return ((this.x == p.x) && (this.y == p.y));
        }

        @Override
        public String toString() {
            return this.x + " , " + this.y;
        }
    }

}
