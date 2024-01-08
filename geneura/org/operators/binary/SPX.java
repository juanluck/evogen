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
package geneura.org.operators.binary;

import random.CommonState;
import geneura.org.individuals.Individual;
import geneura.org.operators.Crossover;

public class SPX extends Crossover{

	public SPX(double pc) {
		super(pc);
	}
	public Individual cross(Individual father, Individual mother) {
		if (CommonState.r.nextDouble()<_pc){
			int pos1 = CommonState.r.nextInt(father.getChr().getLength());

			Individual ind = (Individual) mother.clone();
			
			for (int i = 0;i<pos1;i++){
				ind.getChr().setGen(i, father.getChr().getGen(i));
			}
			
			return ind; 
	
		}else{
			return (CommonState.r.nextDouble()<0.5) ? (Individual)father.clone(): (Individual)mother.clone();
		}
	}

}
