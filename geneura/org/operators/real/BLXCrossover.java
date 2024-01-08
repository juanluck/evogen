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
import geneura.org.operators.Crossover;



public class BLXCrossover extends Crossover{
	
	private double alpha = 0.5;
	
	/**
	 * Default alpha value 0.5
	 */
	public BLXCrossover(double _pc) {
		super(_pc);
	}
	

	public Individual cross(Individual father, Individual mother) {
		
		double[] child 	= new double[father.getChr().getLength()];
	
        for (int x = 0 ; x < child.length; x++){
        	double c_max = Math.max(father.getChr().asdouble()[x], mother.getChr().asdouble()[x]);
        	double c_min = Math.min(father.getChr().asdouble()[x], mother.getChr().asdouble()[x]);
        	double I = c_max - c_min;
        	double alphaI = alpha * I;
        	
        	double max = ((c_max+alphaI) > Configuration.range_max) ? Configuration.range_max : c_max+alphaI;
        	double min = ((c_min-alphaI) < Configuration.range_min) ? Configuration.range_min : c_min-alphaI;
        	
        	child [x] = CommonState.r.nextDouble()*(max-min)+min;
        				
        }
        Individual ind = (Individual) mother.clone();
		
        for (int i = 0;i<ind.getChr().getLength();i++){
        	ind.getChr().setGen(i,new Double(child[i]));
		}
                		
		return ind;
		
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

}

