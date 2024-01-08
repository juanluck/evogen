package geneura.org.selection;

import random.CommonState;
import geneura.org.config.Configuration;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;

public class ProporcionateSelection implements ISelection {
	
	// Neighborhood size represents the number of individuals  
	// in a neighborhood to consider in the selection
	// -1 means that all the population is considered
	// 
protected int neighborhood_size = -1;
	
	public ProporcionateSelection() {
	
	}
	/**
	 * 
	 * @param size The size represents the number of individuals
	 * in a neighborhood to consider in the selection
	 * -1 means that all the population is considered
	 */
	public ProporcionateSelection(int size) {
		neighborhood_size = size;
	}
	
	public Individual select(INeighborhood neighboor) {
		int nsize;
		if(neighborhood_size<0 || neighborhood_size>Configuration.population_size)
			nsize = Configuration.population_size;
		else
			nsize = neighborhood_size;
		
		Individual toselect[] = new Individual[nsize];
		double fitness[] = new double[nsize];
		double cumulative = 0;
		
		//Creating the arrays
		for (int i=0;i<nsize;i++){
			
			if(nsize==Configuration.population_size){
				toselect[i] = neighboor.getIndividual(i);
				if (Configuration.minimization)
					fitness[i] = 1.0/toselect[i].getFitness();
				else
					fitness[i] = toselect[i].getFitness();
				cumulative += fitness[i];
			}else{
				toselect[i] = neighboor.getRandomIndividual();
				if (Configuration.minimization)
					fitness[i] = 1.0/toselect[i].getFitness();
				else
					fitness[i] = toselect[i].getFitness();
				cumulative += fitness[i];
			}
		}
		
		// Normalizing
		double[] normalized = new double[nsize];
		double increment = 0;
		for (int i=0;i<nsize;i++){
			normalized[i] = ((fitness[i]*1.0)/cumulative)+increment;
			increment = normalized[i];
		}
		
		// Selecting
		double random = CommonState.r.nextDouble();
		boolean found = false;
		int pos=0;
		for (int i=0;i<nsize &&!found;i++){
			if (random<normalized[i]){
				found = true;
				pos = i;
				
			}
		}	
		
		return toselect[pos];
	}
	public Individual selectbest(Individual[] individuals) {
		// TODO Auto-generated method stub
		return null;
	}
	public Individual[] selectgroup(INeighborhood neighboor) {
		// TODO Auto-generated method stub
		return null;
	}


}
