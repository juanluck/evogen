package geneura.org.evoag;

import random.CommonState;
import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.neighborhood.graphs.NodeNeighborhoodNewscast;

public class EVAGseq extends Algorithm {

	public EVAGseq() {
		super();
	}
	
	public EVAGseq(String name) {
		super(name);
	}
	
	
	public void run() {
		
		Individual aux[] = new Individual[population.getSize()];

		//We wait 20 cycles for convergence to SW
		if(Configuration.nwconverged)
			for (int j=0;j<20;j++){
				for (int i=0; i < population.getSize(); i++){
					INeighborhood neigh = population.getNeighborhood(i);
					neigh.executeNeighborhoodPolicy();	
				}
			}
		
		int size = Configuration.population_size;
		while(!termination.isFinish()){
			
			
			for (int i=0; i < size; i++){
				
				int j = CommonState.r.nextInt(size);
				//Take the neighborhood for node i
				INeighborhood neigh = population.getNeighborhood(i);
				
				//Iteration for a single individual
				aux[i] = mutation.mutate(crossover.cross(selection.select(neigh),population.getIndividual(i)));//selection.select(neigh)));
				evaluator.evaluate(aux[i]);
				
				//Replacement Uniform Choice (UC)
				//Individual current_ind = population.getIndividual(j);
				//if (aux[j].better(current_ind)){
				//	population.setIndividual(j, aux[j]);
				//}
				
				//Execute newscast
				if(neigh!=null)
					population.getNeighborhood(i).executeNeighborhoodPolicy();
					
			}
			
			
			Individual aux_replace[] = new Individual[population.getSize()];
			// Replacement
			for (int i=0; i < size; i++){

				Individual current_ind = population.getIndividual(i);
				if(aux[i].better(current_ind)){
					population.setIndividual(i, aux[i]);
					//aux_replace[i]=(Individual)aux[i].clone();
				}else{
					//aux_replace[i]=(Individual)aux[i].clone();
					//population.setIndividual(i, aux[i]);
				}
				
				//Individual current_ind = population.getIndividual(i);
				//if (current_ind.better(aux[i])){
				//	aux_replace[i] = (Individual)current_ind.clone();
				//}else{
				//	aux_replace[i] = null;
				//}
				//population.setIndividual(i, aux[i]);

			}
			
			for (int i=0; i < size; i++){
				INeighborhood neigh = population.getNeighborhood(i);
			//	((NodeNeighborhoodNewscast)neigh).replace_altruistic(aux_replace[i]);
			}
			
			for (int i=0; i < size; i++){
				INeighborhood neigh = population.getNeighborhood(i);
			//	((NodeNeighborhoodNewscast)neigh).replace();
			}
			

	
			
			
			notifyObserver();
			
			

		}
		notifyObserver();
	}

}
