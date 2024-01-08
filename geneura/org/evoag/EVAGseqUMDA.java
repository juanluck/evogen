package geneura.org.evoag;

import random.CommonState;
import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.individuals.IChromosome;
import geneura.org.individuals.Individual;
import geneura.org.individuals.binary.BinaryIndividual;
import geneura.org.individuals.binary.BinaryUMDA;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.utils.MergeSort;

public class EVAGseqUMDA extends Algorithm {
	
	public EVAGseqUMDA() {
		super();
	}
	
	public EVAGseqUMDA(String name) {
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
		
		BinaryIndividual[] _population = new BinaryIndividual[Configuration.population_size];
		
		Individual[] _selected = new BinaryIndividual[(int)(Configuration.population_size*Configuration.UMDAf)];
				
		
		while(!termination.isFinish()){
			
			
			for (int i=0; i < population.getSize();i++){// population.getSize(); i++){
				
				INeighborhood neigh = population.getNeighborhood(i);
				BinaryUMDA current = (BinaryUMDA)population.getThisIndividual(i);
	
//				System.out.println("Unsorted:");
//				for (int h=0;h<current.get_distribution_gens().length;h++){
//					System.out.print(current.get_distribution_gens()[h]+" ");
//					
//				}
//				System.out.println();
	
				
				for (int j=0;j<Configuration.population_size;j++){
					_population[j] = new BinaryIndividual(Configuration.chromosome_size);
					_population[j].setChr(current.create_an_instance());						
					evaluator.evaluate(_population[j]);	
					if(_population[j].better(current)){
						current.setChr(_population[j].getChr());
						current.setFitness(_population[j].getFitness());
					}
				}
				
				
//				System.out.println("Unsorted:");
//				for (int h=0;h<_population.length;h++){
//					System.out.print(_population[h].getFitness()+" ");
//					
//				}
//				System.out.println();
				
				MergeSort _popToSort = new MergeSort(_population);
				
				_popToSort.sort();
				
				_selected = _popToSort.getFirstList((int)(Configuration.population_size*Configuration.UMDAf));
				
				current.update_distribution(_selected);
				
//				System.out.println("Sorted:");
//				for (int h=0;h<_selected.length;h++){
//					System.out.print(_selected[h].getFitness()+" ");
//				}
//				System.out.println();
//
//				System.exit(-1);
				
				BinaryUMDA aux = (BinaryUMDA) neigh.getThisIndividual(-1);
				//neigh.getRandomIndividual();
				//selection.select(neigh);
				
				
				
				aux.update_distribution(current.get_distribution_gens());
				current.update_distribution(aux.get_distribution_gens());
				
				//for (int h=0;h<current.get_distribution_gens().length;h++)
				//	System.out.print(current.get_distribution_gens()[h]+" ");
				//System.out.println();
				
				//Updating the distribution
				//current.update_distribution(aux);//current.getChr(), aux.getChr());
//				if (aux2.better(current))
//					current.update_distribution(aux2.getChr());
//				else
//					current.update_distribution(aux2.getChr());
				
				
//				current.update_distribution(aux2.get_distribution_gens());
//				
//				if (aux2.better(current))
//					current.update_distribution(aux2.getChr(),current.getChr());
//				else
//					current.update_distribution(current.getChr(),aux2.getChr());
//				
//				aux2.update_distribution(current.get_distribution_gens());
//				
//				if (aux2.better(current))
//					aux2.update_distribution(aux2.getChr(),current.getChr());
//				else
//					aux2.update_distribution(current.getChr(),aux2.getChr());
				
				
				
				
				//for (int h=0;h<current.get_distribution_gens().length;h++)
				//	System.out.print(current.get_distribution_gens()[h]+" ");
				//System.out.println();
				//System.out.println();
				
//				boolean[] x = current.getChr().asboolean();
//				for (int h=0;h<x.length;h++)
//					System.out.print(x[h]+" ");
//				System.out.println();
				
				//Creating a new individual instance
				
//				x = current.getChr().asboolean();
//				for (int h=0;h<x.length;h++)
//					System.out.print(x[h]+" ");
//				System.out.println();
//				System.out.println();
				
				//Evaluating the new individual
				//current.create_an_instance();
				//evaluator.evaluate(current);
				
				
				
				//Execute newscast
				if(neigh!=null)
					population.getNeighborhood(i).executeNeighborhoodPolicy();
				notifyObserver();	
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
