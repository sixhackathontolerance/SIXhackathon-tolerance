package six.classifier;

	import java.io.BufferedReader;
	import java.io.BufferedWriter;
	import java.io.FileInputStream;
	import java.io.FileReader;
	import java.io.FileWriter;
	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import java.lang.Double;

	import org.apache.mahout.classifier.sgd.L1;
	import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
	import org.apache.mahout.math.DenseVector;
	import org.apache.mahout.math.RandomAccessSparseVector;
	import org.apache.mahout.math.Vector;


	public class App 
	{
				/*
	    	    public static class Double {
	    	        public int x;
	    	        public int y;

	    	        public Double(int x, int y) {
	    	            this.x = x;
	    	            this.y = y;
	    	        }

	    	        @Override
	    	        public boolean equals(Object arg0) {
	    	            Double p = (Double) arg0;
	    	            return ((this.x == p.x) && (this.y == p.y));
	    	        }

	    	        @Override
	    	        public String toString() {
	    	            // TODO Auto-generated method stub
	    	            return this.x + " , " + this.y;
	    	        }
	    	    }
				*/
	    	    public static void main(String[] args) {
	    	        Map<Double, Integer> prices = new HashMap<Double, Integer>();
	    	        String filePath = "/Users/risen/hackathon/workspace/manuel/tolerance-check/data";
	    	        String inputFile = "mdf_stream.csv";
	    	        BufferedReader in = null;
	    	        OnlineLogisticRegression learningAlgo = new OnlineLogisticRegression(2, 3, new L1());
	    	        try
	    	        {
	    	        	in = new BufferedReader(new FileReader(filePath + "/" + inputFile));
	        	        String line;
	        	        System.out.println("training model  \n");
	        	        int i = 0;
	        	        while((line = in.readLine())!= null && (i < 20 ))
	        	        {
	        	        	String[] fields = line.split(";");
	        	        	if(fields[4].equals("275191") && 
	    	        			(fields[5].equals("2") || fields[5].equals("3") || fields[5].equals("4")) &&
	    	        			fields[6].equals("3")
	        	        			)
	        	        	{
	            	        	Double price = Double.parseDouble(fields[8]);
	        	        		Vector v = getVector(price);
	        	        		learningAlgo.train(1, v);
	        	        		i++;
	        	        	}
	        	        }
	        	        learningAlgo.close();
	        	        BufferedWriter out = new BufferedWriter(new FileWriter(filePath +"/" +"275191.results.txt"));
	        	        out.write("P(0)|P(1)|input\n");
	        	        //now classify real data
	        	        while((line = in.readLine())!= null)
	        	        {
	        	        	String[] fields = line.split(";");
	        	        	if(fields[4].equals("275191") && 
	    	        			(fields[5].equals("2") || fields[5].equals("3") || fields[5].equals("4")) &&
	    	        			fields[6].equals("3"))
	        	        	{
			        	        Vector v = new RandomAccessSparseVector(3);
	            	        	Double price = Double.parseDouble(fields[8]);
	        	        		v.set(0, price);       	        		
			        	        v.set(1, price);
			        	        v.set(2, 1);
	        	        	
			        	        Vector r = learningAlgo.classify(v);
			        	        out.write(Double.toString(r.get(0)));
			        	        out.write("|");
			        	        out.write(Double.toString(1.0d - r.get(0)));
			        	        out.write("|");
			        	        out.write(line+"\n");
			        	        //System.out.println(r);
			        	        
			        	        //System.out.println("ans = ");
			        	        //System.out.println("no of categories = " + learningAlgo.numCategories());
			        	        //System.out.println("no of features = " + learningAlgo.numFeatures());
			        	        //System.out.println("Probability of cluster 0 = " + (1.0d - r.get(0)));
			        	        //System.out.println("Probability of cluster 1 = " + r.get(0));
	        	        	}
	        	        }
	    	        }
	    	        catch (Exception e)
	    	        {
	    	        	System.err.println("Error processing input from file: " + inputFile);
	    	        	e.printStackTrace();
	    	        }

	    	    }

	    	    public static Vector getVector(Double price)
		    	{
	    	        //Vector v = new DenseVector(2);
	    	        //Vector v = new DenseVector(1);
	    	        Vector v = new DenseVector(3);
	    	        v.set(0, price);
	    	        v.set(1, price);
	    	        v.set(2, 1);
	    	        return v;
	    	    }
	}

