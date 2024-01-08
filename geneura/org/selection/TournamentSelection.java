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
package geneura.org.selection;

import geneura.org.config.Configuration;
import geneura.org.evoag.EVAGChurn;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;


public class TournamentSelection implements ISelection{
	
	protected int tour_size = 2;
	
	/**
	 * The default tournament size is set to 2
	 *
	 */
	public TournamentSelection(){
		
	}
	/**
	 * 
	 * @param size The size of the tournament
	 */
	public TournamentSelection(int size) {
		tour_size = size;
	}
	
	public Individual select(INeighborhood neighboor) {
		Individual tourn[] = new Individual[Configuration.selection_param];
		
		int pos_best = 0;
		//System.out.println();
		for (int i=0;i<Configuration.selection_param;i++){
			
			tourn[i] = neighboor.getRandomIndividual();
			//System.out.print(tourn[i].getFitness()+" ");
			//for (int j=0;j<i;j++)
			//	if(tourn[i].equals(tourn[j])){
			//		tourn[i] = neighboor.getRandomIndividual();
			//		j = 0;
			//	}

			pos_best = (tourn[i].better(tourn[pos_best])) ? i:pos_best;
		}
		
		//System.out.println(": "+pos_best);
		return tourn[pos_best];
	}
	public Individual selectbest(EVAGChurn[] individuals) {
		int pos_best = 0;
		//System.out.println();
		for (int i=0;i<Configuration.selection_param;i++){
			
			pos_best = (individuals[i].get_individual().better(individuals[pos_best].get_individual())) ? i:pos_best;
		}
		return individuals[pos_best].get_individual();
	}
	
	public EVAGChurn[] selectgroup(INeighborhood neighboor) {
		
		EVAGChurn tourn[] = new EVAGChurn[Configuration.selection_param];
		
		for (int i=0;i<Configuration.selection_param;i++){
			tourn[i] = neighboor.getRandomEVAG();
		}
		return tourn;
	}

}
