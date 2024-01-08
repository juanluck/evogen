package geneura.org.evoag;

import random.CommonState;
import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;

public class EVAGseqGP extends Algorithm {
	
	public EVAGseqGP() {
		super();
	}
	
	public EVAGseqGP(String name) {
		super(name);
	}
	
	
	public void run() {
		
		//We wait 20 cycles for convergence to SW
		if(Configuration.nwconverged)
			for (int j=0;j<20;j++){
				for (int i=0; i < population.getSize(); i++){
					INeighborhood neigh = population.getNeighborhood(i);
					neigh.executeNeighborhoodPolicy();	
				}
			}
		
		
		GPIndividual []individuals = new GPIndividual[Configuration.population_size];
		
		while(!termination.isFinish()){
			
			for (int i=0; i < population.getSize(); i++){
				
				int j = CommonState.r.nextInt(population.getSize());
				//Take the neighborhood for node i
				INeighborhood neigh = population.getNeighborhood(i);
				
				//GPIndividual individual;
				//Iteration for a single individual
				if (CommonState.r.nextDouble() < Configuration.crossover_probability)
					individuals[i] = (GPIndividual)crossover.cross(selections[0].select(neigh),selections[0].select(neigh));//population.getIndividual(i));
				else
					individuals[i] = (GPIndividual)selections[1].select(neigh);
				
				if(individuals[i]!=null){
					evaluator.evaluate(individuals[i]);	
					GPIndividual current_ind = (GPIndividual)population.getIndividual(i);
					
					if (current_ind.better(individuals[i])){
						individuals[i]= current_ind;
					}
				}else{
					GPIndividual current_ind = (GPIndividual)population.getIndividual(i);
					evaluator.evaluate(current_ind);
					individuals[i]= current_ind;
				}
				
				
				//if(individual!=null){
					
				//	evaluator.evaluate(individual);
					
				//Individual current_ind = population.getIndividual(i);
				//	if (individual.better(current_ind)){
				//		population.setIndividual(i, individual);
				//	}
				//}
					
				
				//Execute newscast
				if(neigh!=null)
					population.getNeighborhood(i).executeNeighborhoodPolicy();
				notifyObserver();	
			}
			
			for (int i=0; i < population.getSize(); i++){
				population.setIndividual(i, individuals[i]);
			}
			
			//Replacement
			//for (int i=0; i < population.getSize(); i++){
			//	Individual current_ind = population.getIndividual(i);
			//	if (aux[i].better(current_ind)){
			//		population.setIndividual(i, aux[i]);
			//	}
				
			//}
			
			

		}
		notifyObserver();
	}


}
