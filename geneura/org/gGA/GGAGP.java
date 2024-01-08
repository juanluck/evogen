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
package geneura.org.gGA;

import random.CommonState;
import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.demes.FixedPopulation;
import geneura.org.termination.Termination;

public class GGAGP extends Algorithm{

	public GGAGP() {
		super();
	}
	
	public GGAGP(String name){
		super(name);
	}
	
	
	public void run() {
		
		FixedPopulation aux_pop = new FixedPopulation(population.getSize());
		while(!termination.isFinish()){
			
				
			for (int i=0; i < population.getSize(); i++){
				Individual aux=null;
				
				if (CommonState.r.nextDouble() < Configuration.crossover_probability)
					aux = (GPIndividual)crossover.cross(selections[0].select(population),selections[0].select(population));//population.getIndividual(i));
				else
					aux = (GPIndividual)selections[1].select(population);
				
		
				if (aux!=null){
					evaluator.evaluate(aux);
					aux_pop.setIndividual(i, aux);	
				}else{
					
					aux_pop.setIndividual(i, population.getIndividual(i));
				}
									
			}
			//Elitism
			Individual best = population.getIndividual(population.getBest());
			//evaluator.evaluate(best);
			replacement.replace(best, aux_pop);
			
			population.copyNeighborhood(aux_pop);
			Termination.increment_generation();
			notifyObserver();
		}
	}
}
