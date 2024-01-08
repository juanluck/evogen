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

import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.demes.PanmicticResizing;
import geneura.org.termination.Termination;

public class GGAResized extends Algorithm{
	
	public GGAResized() {
		super();
	}
	
	public GGAResized(String name){
		super(name);
	}
	
	
	public void run() {
		
		while(!termination.isFinish()){

			//System.out.println("Population size: "+population.getSize());
			PanmicticResizing aux_pop = new PanmicticResizing( PanmicticResizing.resize(population.getSize()), Configuration.tau, Configuration.rho);	
			
			for (int i=0; i < aux_pop.getSize(); i++){
				Individual aux = mutation.mutate(crossover.cross(selection.select(population), selection.select(population)));
				evaluator.evaluate(aux);
				aux_pop.setIndividual(i, (Individual)aux.clone());					
			}
			
			//Elitism
			Individual best = population.getIndividual(population.getBest());
			evaluator.evaluate(best);
			replacement.replace(best, aux_pop);
			
			population.copyNeighborhood(aux_pop);
			Termination.increment_generation();
			
			notifyObserver();
		}
	}


}
