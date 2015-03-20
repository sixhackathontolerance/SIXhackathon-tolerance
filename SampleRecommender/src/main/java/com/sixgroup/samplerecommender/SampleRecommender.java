
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sixgroup.samplerecommender;

import java.util.HashMap;
import java.util.Map;

import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;


class Point{
    public int x;
    public int y;

    public Point(int x,int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object arg0) {
        Point p = (Point)  arg0;
        return( (this.x == p.x) &&(this.y== p.y));
    }

    @Override
    public String toString() {
        return  this.x + " , " + this.y ; 
    }
}

public class SampleRecommender {



    public static void main(String[] args) {

            Map<Point,Integer> points = new HashMap<Point, Integer>();

            points.put(new Point(0,0), 0);
            points.put(new Point(1,1), 0);
            points.put(new Point(1,0), 0);
            points.put(new Point(0,1), 0);
            points.put(new Point(2,2), 0);

            points.put(new Point(8,8), 1);
            points.put(new Point(8,9), 1);
            points.put(new Point(9,8), 1);
            points.put(new Point(9,9), 1);


            OnlineLogisticRegression learningAlgo = new OnlineLogisticRegression();
            learningAlgo =  new OnlineLogisticRegression(2, 3, new L1());
            learningAlgo.lambda(0.1);
            learningAlgo.learningRate(10);

            System.out.println("training model  \n" );

            for(Point point : points.keySet()){

                Vector v = getVector(point);
                System.out.println(point  + " belongs to " + points.get(point));
                learningAlgo.train(points.get(point), v);
            }

            learningAlgo.close();

            Vector v = new RandomAccessSparseVector(3);
            v.set(0, 0.5);
            v.set(1, 0.5);
            v.set(2, 1);

            Vector r = learningAlgo.classifyFull(v);
            System.out.println(r);

            System.out.println("ans = " );
            System.out.println("no of categories = " + learningAlgo.numCategories());
            System.out.println("no of features = " + learningAlgo.numFeatures());
            System.out.println("Probability of cluster 0 = " + r.get(0));
            System.out.println("Probability of cluster 1 = " + r.get(1));

    }

    public static Vector getVector(Point point){
        Vector v = new DenseVector(3);
        v.set(0, point.x);
        v.set(1, point.y);
        v.set(2, 1);
        return v;
    }
}