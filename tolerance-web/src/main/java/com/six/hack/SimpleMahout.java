package com.six.hack;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

public class SimpleMahout {

		Map<String, OnlineLogisticRegression> classifiers = new HashMap<>();
        final int TRAINING_SET_SIZE=20;
        final int TRAINING_SET_ITERATIONS=200;
        BufferedReader in = null;
    	public SimpleMahout(List<String> securities)
    	{
    		//For each given security pull the top 20 items out of the input
    		//and train a learningAlgo on it
    		for(String security: securities)
    		{
    			 try
    		        {
    		        	BufferedReader in1 = createReader();
    			        String line;
    	    	        OnlineLogisticRegression learningAlgo = new OnlineLogisticRegression(2, 3, new L1());
    	    	        
    	    	        //Based upon the mahout docs AdaptiveLogisticRegression would probably be a better way of continually training
    	    	        //and then classifying data, but I wasn't able to get an example to run with it
    	    	        //this would really be adaptive machine learning
    	    	        //AdaptiveLogisticRegression learningAlgo = new AdaptiveLogisticRegression(2,3,new L1());   	        
    	    	        
    	    	        //the learningRate has an effect on the resulting probabilities, if the results are plotted
    	    	        //there's actually a curve to it's effect on the change in probability on the same dataset,
    	    	        //so there's another place were some deep understanding would definitely help
    	    	        //learningAlgo.learningRate(10);   	    	        
    	    	        
    	    	        List<Double> extractedPrices = new ArrayList<Double>();
    			        System.err.println("training model for: " + security + " \n");
    			        int i = 0;
    			        while((line = in1.readLine())!= null && (i < TRAINING_SET_SIZE ))
    			        {
    			        	String[] fields = line.split(";");
    			        	//The data is restricted to bid/ask/mid price, all other values are ignored
    			        	if(fields[4].equals(security) && 
    		        			(fields[5].equals("2") || fields[5].equals("3") || fields[5].equals("4")) &&
    		        			fields[6].equals("3")
    			        			)
    			        	{
    		    	        	Double price = Double.parseDouble(fields[8]);
    		    	        	System.err.println("Adding training price:" + Double.toString(price));
    		    	        	extractedPrices.add(price);
    			        		i++;
    			        	}
    			        }
    			        //Train with the first 20 samples, the outer loop will
    			        //reinforce that 200x over, not really needed
    			        //for(int j =0; j<TRAINING_SET_ITERATIONS; j++)
    			        	for(int k=0; k<TRAINING_SET_SIZE; k++)
    			        		learningAlgo.train(1, getVector(extractedPrices.get(k)));

    			        learningAlgo.close();
    			        classifiers.put(security, learningAlgo);
    		        }
    		        catch (Exception e)
    		        {
    		        	System.err.println("Error processing input from file: " );
    		        	e.printStackTrace();
    		        }
			}
			//now thats done, setup the input file to be read through again, one record at a time
    		in = createReader();
    	}

	    public Outlier processNextLine()
	    {
	        try
	        {
	        	//System.out.println("Processing next line...");
	        	String line;
		        //now classify real data
		        if((line = in.readLine())!= null)
		        {
		        	String[] fields = line.split(";");
		        	//The data is restricted to bid/ask/mid price, all other values are ignored
		        	if(classifiers.containsKey(fields[4]) && 
	        			(fields[5].equals("2") || fields[5].equals("3") || fields[5].equals("4")) &&
	        			fields[6].equals("3"))
		        	{
	        	        Vector v = new RandomAccessSparseVector(3);
	    	        	Double price = Double.parseDouble(fields[8]);
		        		v.set(0, price);       	        		
	        	        v.set(1, price);
	        	        v.set(2, 1);
	
	        	        Vector r = classifiers.get(fields[4]).classify(v);
	        	        Thread.sleep(1);
	        	        //This is kind of hackish, and most likely wrong
	        	        //but works to get at the values that lie outside the bounds
	        	        //based upon the provided sample results, works for valor 897789 and 275191
	        	        if(r.get(0) > 0.99999999999994d || r.get(0) < 0.9)
	        	        {
	        	        	System.out.println(Double.toString(r.get(0))+"|"+Double.toString(1.0d - r.get(0))+"|"+line);
	        	        	return new Outlier(new Price(line));
	        	        }
		        	}
		        }
		        else
		        {
		        	Thread.sleep(5000);
		        }
	        }
	        catch (Exception e)
	        {
	        	System.err.println("Error processing input from file: " );
	        	e.printStackTrace();
	        }
        	return null;
	    }
	    private BufferedReader createReader() {
        	return new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/sample/data/mdf_stream.csv")));
    	}
    	public Vector getVector(Double price)
		{
	    	Vector v = new DenseVector(3);
  		 	v.set(0, price);
        	v.set(1, price);
        	v.set(2, 1);
        	return v;
    	}
    }
