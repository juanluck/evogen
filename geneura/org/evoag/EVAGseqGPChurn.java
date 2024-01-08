package geneura.org.evoag;

import random.CommonState;
import geneura.org.Algorithm;
import geneura.org.GPEvaluator;
import geneura.org.config.Configuration;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.individuals.tree.GPData;
import geneura.org.individuals.tree.SimpleGPIndividual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.neighborhood.graphs.NodeNeighborhoodNewscastChurn;
import geneura.org.neighborhood.graphs.SimpleNewscastChurn;

public class EVAGseqGPChurn extends Algorithm {
	
	
	public EVAGseqGPChurn() {
		super();
	}
	
	public EVAGseqGPChurn(String name) {
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
			for (int i=0; i < Configuration.population_size; i++){
				
				//Take the neighborhood for node i
				NodeNeighborhoodNewscastChurn neigh = (NodeNeighborhoodNewscastChurn) population.getNeighborhood(i);
				
				//If the node is rejoining the system we initialize its chromosome at random
				if ((neigh.getEvAg().failure && neigh.getEvAg().isFailing()) || neigh.getSize()==0){
					neigh.getEvAg().failure = neigh.getEvAg().isFailing();
					neigh.getEvAg().local_cycle++;
				}else if (Configuration.churnnewindividualpolicy.equals("random") && neigh.getEvAg().failure && !neigh.getEvAg().isFailing()){
					
					GPData data = ((GPIndividual)neigh.getEvAg()._individual).problem_data;
					individuals[i] = new SimpleGPIndividual(((GPEvaluator)evaluator).getPrimitives(), (GPEvaluator)evaluator, data, i);
					evaluator.evaluate(individuals[i]);
					
					neigh.getEvAg().failure = neigh.getEvAg().isFailing();
					neigh.getEvAg().local_cycle++;
				}else if(Configuration.churnnewindividualpolicy.equals("breed") && neigh.getEvAg().failure && !neigh.getEvAg().isFailing()){
					if (CommonState.r.nextDouble() < Configuration.crossover_probability)
						individuals[i] = (GPIndividual)crossover.cross(selections[0].select(neigh),selections[0].select(neigh));//population.getIndividual(i));
					else
						individuals[i] = (GPIndividual)selections[1].select(neigh);
					
					if(individuals[i]!=null){
						evaluator.evaluate(individuals[i]);	
						
					}else{
						GPIndividual current_ind = (GPIndividual)selections[1].select(neigh);
						evaluator.evaluate(current_ind);
						individuals[i]= current_ind;
					}
					
					neigh.getEvAg().failure = neigh.getEvAg().isFailing();
					neigh.getEvAg().local_cycle++;
				}else if(Configuration.churnnewindividualpolicy.equals("localsearch") && neigh.getEvAg().failure && !neigh.getEvAg().isFailing()){
					
					Individual ind = selections[0].select(neigh);//neigh.getRandomIndividual();
					Individual auxind = mutation.mutate(ind);
					evaluator.evaluate(auxind);
					neigh.getEvAg().failure = neigh.getEvAg().isFailing();
					if (auxind.better(ind)){
						individuals[i]=(GPIndividual)auxind;
						//System.out.println("mejor");
					}
					else{
						individuals[i]=(GPIndividual)ind;
						//System.out.println("peor");
					}
					neigh.getEvAg().local_cycle++;
					
				}else{
					//GPIndividual individual;
					neigh.getEvAg().failure = neigh.getEvAg().isFailing();
					//Iteration for a single individual
					if (CommonState.r.nextDouble() < Configuration.crossover_probability)
						individuals[i] = (GPIndividual)crossover.cross(selections[0].select(neigh),selections[0].select(neigh));//population.getIndividual(i));
					else
						individuals[i] = (GPIndividual)selections[1].select(neigh);
					
					if(individuals[i]!=null){
						evaluator.evaluate(individuals[i]);	
						((EVAGChurn)neigh.getEvAg()).local_cycle++;
						GPIndividual current_ind = (GPIndividual)population.getIndividual(i);
						
						if (current_ind.better(individuals[i])){
							individuals[i]= current_ind;
						}
					}else{
						GPIndividual current_ind = (GPIndividual)population.getIndividual(i);
						evaluator.evaluate(current_ind);
						((EVAGChurn)neigh.getEvAg()).local_cycle++;
						individuals[i]= current_ind;
					}
					
					//if (((EVAGChurn)neigh.getEvAg()).local_cycle >= ((EVAGChurn)neigh.getEvAg()).life){
					//	((EVAGChurn)neigh.getEvAg()).failure=true;
					//}
				}
				
//				for(;i< Configuration.population_size-1 && (neigh.getEvAg().failure || neigh.getSize()==0);i++){
//					if (neigh.getSize()==0 && !neigh.getEvAg().failure){
//						neigh.getEvAg().local_cycle++;
//					}
//					//individuals[i] = null;
//					neigh = (NodeNeighborhoodNewscastChurn) population.getNeighborhood(i);
//				}
//				
//				
//				//GPIndividual individual;
//				//Iteration for a single individual
//				if (CommonState.r.nextDouble() < Configuration.crossover_probability)
//					individuals[i] = (GPIndividual)crossover.cross(selections[0].select(neigh),selections[0].select(neigh));//population.getIndividual(i));
//				else
//					individuals[i] = (GPIndividual)selections[1].select(neigh);
//				
//				if(individuals[i]!=null){
//					evaluator.evaluate(individuals[i]);	
//					((EVAGChurn)neigh.getEvAg()).local_cycle++;
//					GPIndividual current_ind = (GPIndividual)population.getIndividual(i);
//					
//					if (current_ind.better(individuals[i])){
//						individuals[i]= current_ind;
//					}
//				}else{
//					GPIndividual current_ind = (GPIndividual)population.getIndividual(i);
//					evaluator.evaluate(current_ind);
//					((EVAGChurn)neigh.getEvAg()).local_cycle++;
//					individuals[i]= current_ind;
//				}
//				
//				if (((EVAGChurn)neigh.getEvAg()).local_cycle >= ((EVAGChurn)neigh.getEvAg()).life){
//					((EVAGChurn)neigh.getEvAg()).failure=true;
//				}
				
				
				//Execute newscast
				if(!((EVAGChurn)((NodeNeighborhoodNewscastChurn)population.getNeighborhood(i)).getEvAg()).failure)
					population.getNeighborhood(i).executeNeighborhoodPolicy();
				else
					((NodeNeighborhoodNewscastChurn)population.getNeighborhood(i)).increasecycle();

			}
		
			for (int i=0; i < Configuration.population_size; i++){
				population.setIndividual(i, individuals[i]);
			}
			notifyObserver();	
			
		}
		notifyObserver();
	}


}
