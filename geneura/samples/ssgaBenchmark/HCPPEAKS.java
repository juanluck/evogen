package geneura.samples.ssgaBenchmark;

import geneura.org.config.Configuration;
import geneura.org.individuals.Individual;


import random.CommonState;

public class HCPPEAKS extends geneura.org.Evaluator{
	
	private static int P;
	private static int L;
	private boolean peak[][];
	private double heigth[];

	
	public HCPPEAKS() {
		this.L = Configuration.chromosome_size;
		this.P = Configuration.auxint;
		peak = new boolean[P][L];
		heigth = new double[P];
		generatePeaks();
	}
	
	public HCPPEAKS(int L, int P) {
		this.L = L;
		this.P = P;
		peak = new boolean[P][L];
		heigth = new double[P];
	}
	
	  public void generatePeaks(){
		  double decr = 1.0/P;
		  double value = 1+decr;
	      for(int p=0;p<P;p++)
	      {
	    	  heigth[p] = value - decr;
	    	  value -= decr;
	        for(int j=0;j<L;j++)
	        if(CommonState.r.nextDouble()<0.5)	peak[p][j] = true;
	        else	peak[p][j] = false;
	      }
	  }
	  
	  public void evaluate(Individual ind){
			super.evaluate(ind);
			
			boolean[] x = ind.getChr().asboolean();
			double fitness = (double)hamming(x) / (double)L;
			ind.setFitness(fitness);
		}
		
		
		  public double hamming(boolean[] x){
		      int nearest_peak = 0;
		      int n_peak_index=0;

		      for(int p=0; p<P; p++)	// For every peak do
			    {
			      // Compute Hamming distance
			    	int hd = 0;
			    	
			      for(int j=0;j<L;j++) 
			    	  if(peak[p][j]!=x[j])	
			    		  hd++;

			      if ((L-hd)>nearest_peak){
			    	  nearest_peak = L-hd;
			    	  n_peak_index = p;
			      }
			    }
		      		return nearest_peak*heigth[n_peak_index]*heigth[n_peak_index]*heigth[n_peak_index];
		  }
		  
		  public double hit_value() {
				return 1.0;
			}
			
			public Object clone() {
				HCPPEAKS pp = new HCPPEAKS(L,P);
			      for(int p=0;p<P;p++)
			      {
			    	  pp.heigth[p] = this.heigth[p];
			        for(int j=0;j<L;j++)
			        	pp.peak[p][j] = this.peak[p][j];
			      }

				return pp;
			}

}
