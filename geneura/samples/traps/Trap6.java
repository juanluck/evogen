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

public class Trap6 extends Evaluator{

	
    public Trap6() {
       	
     }
    
    
    public void evaluate(Individual ind){
        super.evaluate(ind);
                
        
        boolean[] x = ind.getChr().asboolean();      
        
        int m = Configuration.chromosome_size/6;    //number of subproblems

        double fitnessx = 0.0;
    	for (int i = 0;	i < m;	i++) {
    		int xs = 0;
    		for (int j = 0;	j < 6;	j++) { // Count the number of ones
    			if (x[i*4+j])
    				xs = xs+1;
    		}
    		
    		if (xs== 6)
    			fitnessx += 6;
    		else
    			fitnessx += 6-1-xs;
    		
    	}
 
    		ind.setFitness(fitnessx);

    }

    public double hit_value() {
    	return Configuration.chromosome_size;
    }
    
    
    public Object clone() {
    	
    	Trap6 pp = new Trap6();
    	 
    	
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