package geneura.samples.ssgaBenchmark;

import java.util.Random;

import geneura.org.individuals.Individual;

public class PPEAKSFROMFILE extends geneura.org.Evaluator{

	private static int P;
	private static int N;
	private boolean peak[][];
	
	
	public PPEAKSFROMFILE() {
		
		this.N = N;
		this.P = P;
		peak = new boolean[P][N];


	}
	
	public void evaluate(Individual ind){
		super.evaluate(ind);
		
		boolean[] x = ind.getChr().asboolean();
		double fitness = (double)hamming(x) / (double)N;
		ind.setFitness(fitness);
	}
	
	
	  public double hamming(boolean[] x){
	      int nearest_peak = 0;

		    for(int p=0; p<P; p++)	// For every peak do
		    {
		      // Compute Hamming distance
		    	int hd = 0;
		      for(int j=0;j<N;j++) 
		    	  if(peak[p][j]!=x[j])	
		    		  hd++;

		      if ((N-hd)>nearest_peak){
		    	  nearest_peak = N-hd;

		      }
		    }

			  return nearest_peak;
	  }
	
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}


	public double hit_value() {
		return 1.0;
	}

	
	
}
