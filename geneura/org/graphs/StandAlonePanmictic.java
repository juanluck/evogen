package geneura.org.graphs;

import geneura.org.Algorithm;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;

public class StandAlonePanmictic extends Algorithm{
	
	public StandAlonePanmictic() {
		super();
	}
	
	public StandAlonePanmictic(String name) {
		super(name);
	}
	
	
	public void run() {
		
		Individual aux[] = new Individual[population.getSize()];
		while(!termination.isFinish()){
			
			for (int i=0; i < population.getSize(); i++){
				
				Individual current_ind = population.getIndividual(i);
			
					
				//Iteration for a single individual
				aux[i] = (Individual)selection.select(population).clone();
				evaluator.evaluate(aux[i]);
				
				notifyObserver();	
			}
			
			//Replacement
			for (int i=0; i < population.getSize(); i++){
				Individual current_ind = population.getIndividual(i);
				if (aux[i].better(current_ind)){
					population.setIndividual(i, aux[i]);
				}
				
			}
			


		}
		notifyObserver();
	}


}
