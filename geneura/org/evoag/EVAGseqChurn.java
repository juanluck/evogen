package geneura.org.evoag;

import random.CommonState;
import geneura.org.Algorithm;
import geneura.org.GPEvaluator;
import geneura.org.config.Configuration;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.individuals.binary.BinaryIndividual;
import geneura.org.individuals.tree.GPData;
import geneura.org.individuals.tree.SimpleGPIndividual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.neighborhood.graphs.NodeNeighborhoodNewscastChurn;
import geneura.org.neighborhood.graphs.SimpleNewscastChurn;

public class EVAGseqChurn extends Algorithm {

	boolean lock = false;
	int internalind = 0;
	int hash=0;
	
	public EVAGseqChurn() {
		super();
	}
	
	public EVAGseqChurn(String name) {
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
		
		while(!termination.isFinish()){
			
			int size = Configuration.population_size;//population.getSize();
			for (int i=0; i < size; i++){
				
					
				
				//Take the neighborhood for node i
				NodeNeighborhoodNewscastChurn neigh = (NodeNeighborhoodNewscastChurn) population.getNeighborhood(i);
				
				
				
				//if (Configuration.adaptive_selection)
					Configuration.selection_param = neigh.getEvAg().get_tournament_size();
				
				if (neigh.getEvAg().failure  || neigh.getSize()==0){ //&& neigh.getEvAg().isFailing())
					//neigh.getEvAg().failure = neigh.getEvAg().isFailing();
					//neigh.getEvAg().local_cycle++;

				}else if (Configuration.churnnewindividualpolicy.equals("random") && neigh.getEvAg().alive){//neigh.getEvAg().failure && !neigh.getEvAg().isFailing()){
					neigh.getEvAg().alive = false;
					aux[i] = new BinaryIndividual(Configuration.chromosome_size);
					evaluator.evaluate(aux[i]);
					
					
					//neigh.getEvAg().failure = neigh.getEvAg().isFailing();
					//population.setIndividual(i, aux[i]);
					//neigh.getEvAg().local_cycle++;
				}else if(Configuration.churnnewindividualpolicy.equals("breed") && neigh.getEvAg().alive){//neigh.getEvAg().failure && !neigh.getEvAg().isFailing()){
					neigh.getEvAg().alive = false;
					//Iteration for a single individual
					aux[i] = mutation.mutate(crossover.cross(selection.select(neigh),selection.select(neigh)));
					evaluator.evaluate(aux[i]);
					//neigh.getEvAg().failure = neigh.getEvAg().isFailing();
					//population.setIndividual(i, aux[i]);
					//neigh.getEvAg().local_cycle++;
					
				}else if(Configuration.churnnewindividualpolicy.equals("localsearch") && neigh.getEvAg().alive){//neigh.getEvAg().failure && !neigh.getEvAg().isFailing()){
					neigh.getEvAg().alive = false;
					//Iteration for a single individual
					Individual ind = selection.select(neigh);
					Individual auxind = mutation.mutate(ind);
					
					evaluator.evaluate(auxind);
					//neigh.getEvAg().failure = neigh.getEvAg().isFailing();
					if (auxind.better(ind)){
						aux[i]= auxind;
						
					}else{
						aux[i]=ind;
						
					}
					//neigh.getEvAg().local_cycle++;
					
				}else if(Configuration.churnnewindividualpolicy.equals("searcher") && neigh.getEvAg().alive){//neigh.getEvAg().failure && !neigh.getEvAg().isFailing()){
					neigh.getEvAg().alive = false;
					//Iteration for a single individual
					int oldtourn = Configuration.selection_param;
					Individual ind = selection.select(neigh);
					Individual auxind = mutation.mutate(ind);
					evaluator.evaluate(auxind);
					//neigh.getEvAg().failure = neigh.getEvAg().isFailing();
					if (auxind.better(ind)){
						aux[i]= auxind;
						//population.setIndividual(i, auxind);
					}else{
						//population.setIndividual(i, ind);
						aux[i]=ind;
					}
					//neigh.getEvAg().local_cycle++;
					neigh.getEvAg().searcher=true;
				}else{
					if(Configuration.churnnewindividualpolicy.equals("searcher") && neigh.getEvAg().searcher){
						Individual ind = neigh.getEvAg().get_individual();
						Individual auxind = mutation.mutate(ind);
						evaluator.evaluate(auxind);
						//neigh.getEvAg().failure = neigh.getEvAg().isFailing();
						if(neigh.getEvAg().failure)
							neigh.getEvAg().searcher=false;
						if (auxind.better(ind)){
							//population.setIndividual(i, auxind);
							aux[i]=auxind;
						}else{
							aux[i]=ind;
							//population.setIndividual(i, ind);
						}
						//neigh.getEvAg().local_cycle++;
				
					}else{
						//neigh.getEvAg().failure = neigh.getEvAg().isFailing();
						
						//Iteration for a single individual
						EVAGChurn[] selgroup = selection.selectgroup(neigh);
						Individual selected = selection.selectbest(selgroup);

						aux[i] = mutation.mutate(crossover.cross(selected,population.getIndividual(i)));//_selection.select(population)));
						evaluator.evaluate(aux[i]);
						//((EVAGChurn)neigh.getEvAg()).local_cycle++;
						
						
						
						//Replacement Uniform Choice (UC)
						Individual current_ind = population.getIndividual(i);
						if (Configuration.adaptive_selection){
							neigh.getEvAg().delta_tournament (selgroup,current_ind);
							//neigh.getEvAg().increase_g2i(selected.getFitness());
						}
						
						//neigh.getEvAg().increase_g2i(selected.getFitness());
						
						if (aux[i].better(current_ind)){
							//population.setIndividual(i, aux[i]);
							
							if (Configuration.adaptive_selection){
								
							}
						}else{
							if (Configuration.adaptive_selection){
								
							}
						}
						
					}
					
				}
					
				((EVAGChurn)neigh.getEvAg()).local_cycle++;
				boolean isdeath= neigh.getEvAg().failure;
				neigh.getEvAg().failure = neigh.getEvAg().isFailing();
				
				if(isdeath && !neigh.getEvAg().failure){
					neigh.getEvAg().alive = true;
				}
				
			}
			
			//Replacement
			for (int i=0; i < size; i++){
				
				if (!((NodeNeighborhoodNewscastChurn) population.getNeighborhood(i)).getEvAg().failure){
					Individual current_ind = population.getIndividual(i);
					if (aux[i].better(current_ind)){
						population.setIndividual(i, aux[i]);
					}	
				}
			}

			
			//for (int h=0;h<10;h++)
			for (int i=0; i < size; i++){
				//if (((EVAGChurn)neigh.getEvAg()).local_cycle >= ((EVAGChurn)neigh.getEvAg()).life){
				//	((EVAGChurn)neigh.getEvAg()).failure=true;
				//}

				//Execute newscast
				
				//if(neigh!=null && !((EVAGChurn)neigh.getEvAg()).failure)
				
				if(!((EVAGChurn)((NodeNeighborhoodNewscastChurn)population.getNeighborhood(i)).getEvAg()).failure){
					
				//	if(((EVAGChurn)((NodeNeighborhoodNewscastChurn)population.getNeighborhood(i)).getEvAg()).alive && !lock){
				//		if (internalind==10){
				//			lock=true;
				//			hash = ((EVAGChurn)((NodeNeighborhoodNewscastChurn)population.getNeighborhood(i)).getEvAg()).hashccode();
				//				System.out.println("Antes "+population.getNeighborhood(i).getSize());	
				//		}else
				//			internalind++;
				//	}
						
					population.getNeighborhood(i).executeNeighborhoodPolicy();
				//	if(((EVAGChurn)((NodeNeighborhoodNewscastChurn)population.getNeighborhood(i)).getEvAg()).hashccode()== hash){
				//		int count = numberofoccurrences(hash, size, population);
				//		System.out.println("Hash "+hash+" ocurrences "+count);
				//}
				}else{
					
					((NodeNeighborhoodNewscastChurn)population.getNeighborhood(i)).increasecycle();
				}
					
				
				
			}
			notifyObserver();	
			
			
			

		}
		notifyObserver();
	}
	
	private int numberofoccurrences(int hash, int size, INeighborhood pop){
		int count=0;
		for (int i=0; i < size; i++){
			if(!((EVAGChurn)((NodeNeighborhoodNewscastChurn)population.getNeighborhood(i)).getEvAg()).failure &&
				((EVAGChurn)((NodeNeighborhoodNewscastChurn)population.getNeighborhood(i)).getEvAg()).hashccode()!=hash){
				EVAGChurn[] neighbors = ((NodeNeighborhoodNewscastChurn)population.getNeighborhood(i)).get_neigbours();
				int index = ((NodeNeighborhoodNewscastChurn)population.getNeighborhood(i)).get_index();
				for (int j=0;j<index;j++){
					if (neighbors[j].hashccode()==hash)
						count++;
				}
			}
		}
		return count;
	}

}
