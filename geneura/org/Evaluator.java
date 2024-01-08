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
package geneura.org;

import geneura.org.config.Configuration;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.individuals.tree.ADFGenome;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.statistics.IObservable;
import geneura.org.termination.Termination;

public abstract class Evaluator implements IObservable{
	
	//static GPIndividual individual = null;
		
	/**
	 * The subclass has to override this method
	 * @param ind The individual to be evaluated
	 * 
	 */
	public void evaluate(Individual ind){
		
		//individual = (GPIndividual)ind;
		Termination.increment_evaluation();
	}
	
	public boolean dynamic_recalculation(Individual ind){
		return false;
	}
	
	public abstract double hit_value();
	
	public long get_n_evaluation(){
		return Termination.get_n_evaluation();
	}
	

	public String getStateAsString() {
		String aux=" ";
		
		if (Configuration.evaluation.contains("Trap2"))
			aux += Configuration.chromosome_size/2+" ";
		else if (Configuration.evaluation.contains("Trap3"))
			aux += Configuration.chromosome_size/3+" ";
		else if (Configuration.evaluation.contains("Trap4"))
			aux += Configuration.chromosome_size/4+" ";
		
		//if (Configuration.gp_print_best && Termination.get_n_evaluation() > 10000)
			//return "\n\n"+((ADFGenome)individual.getTreeGenome()).getDefRootNode().toString()+"\n\n";

		return aux+Termination.get_n_evaluation()+" "+Configuration.population_size;
	}
	
	public abstract Object clone();
	
	
	
}
