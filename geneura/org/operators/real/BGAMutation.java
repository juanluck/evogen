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
package geneura.org.operators.real;

import random.CommonState;
import geneura.org.config.Configuration;
import geneura.org.individuals.Individual;
import geneura.org.operators.Mutation;

public class BGAMutation extends Mutation{

	public BGAMutation(double _pm) {
		super(_pm);
	}
	
	public Individual mutate(Individual ind) {

		Individual aux = (Individual)ind.clone();
		
		double rang = 0.1 * (Configuration.range_max-Configuration.range_min);

		if (CommonState.r.nextDouble() < _pm){
			for(int i=0;i< ind.getChr().getLength();i++){
    		    
	    		double sum = 0;
	    		for (int k=0;k<=15;k++){
	    			sum += (CommonState.r.nextDouble() < 0.0625) ? Math.pow(2, -k):0;
	    		}
	    		
	    		if (CommonState.r.nextDouble() < 0.5){
	    			double value = (ind.getChr().asdouble()[i]+(rang*sum) > Configuration.range_max) ? Configuration.range_max : (ind.getChr().asdouble()[i]+(rang*sum)) ;
	    			aux.getChr().setGen(i, new Double(value));
	    		}else{
	   				double value = (ind.getChr().asdouble()[i]-(rang*sum) < Configuration.range_min) ? Configuration.range_min : (ind.getChr().asdouble()[i]-(rang*sum)) ;
		    		aux.getChr().setGen(i, new Double(value));
	   			}
	    	}
		}
		return aux;
	}
	
}
