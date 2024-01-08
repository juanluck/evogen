/*
EvoGen, Evolutionary Geneura is a framework for simulating distributed evolutionary computation experiments
Copyright (C) 2008 Junta de Andalucia CICE project P06-TIC-02025

This file is part of EvoGen.

EvoGen is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

EvoGen is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with EvoGen; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

contact:  http://geneura.ugr.es, https://forja.rediris.es/svn/geneura/evogen
*/
package geneura.samples.traps;


import geneura.org.Evaluator;
import geneura.org.config.Configuration;
import geneura.org.evoag.EVAG;
import geneura.org.individuals.Individual;
import geneura.org.termination.Termination;
import geneura.org.termination.ValueReached;
import random.CommonState;

public class Trap4 extends Evaluator{
	boolean[][] mask = new boolean[11][Configuration.chromosome_size]; 
	boolean[][] t = new boolean[11][Configuration.chromosome_size];
	int change_steps;
	int mask_flag = 0;
	boolean should_recalculate = false;
	boolean trigger_mask = false;

	
    public Trap4() {
       	
    	change_steps = (int) Configuration.termination_max_evaluation / 10;
    	
    	int ones;
    	
    	
    	
    	for (int i = 0;	i < 10;	i++) {//Initialize first Mask with all 0s
    		for (int j = 0;	j < Configuration.chromosome_size;	j++) { 
    			mask[i][j] = false;
    			t[i][j] = false;
    		}
    	}
    	for (int i = 1;	i < 10;	i++) {//Creates remaining masks
    		if (Configuration.rho == 0)
    			ones = CommonState.r.nextInt(Configuration.chromosome_size);
    		else                 
    			ones = (int)(Configuration.rho*Configuration.chromosome_size+0.5);
    				
    		for (int j = 0;	j < ones;	j++) { //defines intermediate binary template with ro*genome_dimension ones
    			int position;
    			do {
    				position = CommonState.r.nextInt(Configuration.chromosome_size);
    			} while (t[i][position]);
    			t[i][position] = true;
    		}
    		for (int j = 0;	j < Configuration.chromosome_size;	j++) {
    			mask[i][j] = (mask[i-1][j])^(t[i][j]);
    		}
    	}
    	
    	EVAG.DYNAMIC_COUNT = 0;
    	mask_flag = (int)(Termination.get_n_evaluation())/change_steps;
    	
    }
    
    public boolean dynamic_recalculation(Individual ind) {

    	if(mask_flag != (int)(Termination.get_n_evaluation())/change_steps){
    		should_recalculate = true;
    	}
    	
    	if (should_recalculate)
    	{
    		mask_flag = (int)(Termination.get_n_evaluation())/change_steps;
    		
    		
    		if (!trigger_mask){
    			trigger_mask = true;
    			EVAG.DYNAMIC_COUNT++;
    			recalculate(ind);
    			return false;
    		}

    		if (EVAG.DYNAMIC_COUNT == Configuration.population_size || EVAG.DYNAMIC_COUNT == 0){
    		    EVAG.DYNAMIC_COUNT = 0;
        		should_recalculate = false;
        		trigger_mask = false;
    		    return true;
    		}
    		//System.err.println("Espero");
    		return false;
    	}else{
    		EVAG.DYNAMIC_COUNT = 0;
    		return true;	
    	}
    	
    	
    }
    
    public void check_triggers(){
    	if(mask_flag != (int)(Termination.get_n_evaluation())/change_steps){
    		should_recalculate = true;
    	}
    	mask_flag = (int)(Termination.get_n_evaluation())/change_steps;
    }
    
    public void evaluate(Individual ind){
        super.evaluate(ind);
        
        check_triggers();
        
        
        boolean[] x = ind.getChr().asboolean();
        boolean[] maskedx = new boolean[x.length];
        
        for (int i = 0;	i < x.length;	i++)
    	     maskedx[i] = x[i]^mask[mask_flag][i]; //Mask for DOPs
        
        
        
        int m = Configuration.chromosome_size/4;    //number of subproblems
        double fitness = 0.0;
        double fitnessx = 0.0;
    	for (int i = 0;	i < m;	i++) {
    		int ones = 0;
    		int xs = 0;
    		for (int j = 0;	j < 4;	j++) { // Count the number of ones
    			if (maskedx[i*4+j])
    				ones = ones+1;
    			if (x[i*4+j])
    				xs = xs+1;
    		}
    		if (ones == 4)
    			fitness += 4;
    		else
    			fitness += 4-1-ones;
    		
    		if (xs== 4)
    			fitnessx += 4;
    		else
    			fitnessx += 4-1-xs;
    		
    	}
 
    	if (Configuration.dynamic)
    		ind.setFitness(fitness);
    	else
    		ind.setFitness(fitnessx);

    }

    public double hit_value() {
    	return Configuration.chromosome_size;
    }
    
    public void recalculate(Individual ind){
    	boolean[] x = ind.getChr().asboolean();
        boolean[] maskedx = new boolean[x.length];
        
        for (int i = 0;	i < x.length;	i++)
    	     maskedx[i] = x[i]^mask[mask_flag][i]; //Mask for DOPs
        
        
        
        int m = Configuration.chromosome_size/4;    //number of subproblems
        double fitness = 0.0;
        double fitnessx = 0.0;
    	for (int i = 0;	i < m;	i++) {
    		int ones = 0;
    		int xs = 0;
    		for (int j = 0;	j < 4;	j++) { // Count the number of ones
    			if (maskedx[i*4+j])
    				ones = ones+1;
    			if (x[i*4+j])
    				xs = xs+1;
    		}
    		if (ones == 4)
    			fitness += 4;
    		else
    			fitness += 4-1-ones;
    		
    		if (xs== 4)
    			fitnessx += 4;
    		else
    			fitnessx += 4-1-xs;
    		
    	}
 
    	if (Configuration.dynamic)
    		ind.setFitness(fitness);
    	else
    		ind.setFitness(fitnessx);
    }
    
    public Object clone() {
    	
    	Trap4 pp = new Trap4();
    	for (int i = 0;	i < 10;	i++) {//Initialize first Mask with all 0s
    		for (int j = 0;	j < Configuration.chromosome_size;	j++) {
    			pp.mask[i][j] = mask[i][j];
    		}
    	}
    	pp.change_steps = change_steps;
    	 
    	
    	return pp;
    }
    
    
    
    public static void main(String[] args) {
		boolean a = false;
		boolean b = true;
		
		if(a^b)
			System.out.println("true");
		else
			System.out.print("false");
		
	}
    
    
}