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

package geneura.org.cGA;

import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.individuals.IChromosome;
import geneura.org.individuals.Individual;
import geneura.org.individuals.binary.BinaryChromosome;
import geneura.org.individuals.binary.BinaryUMDA;

public class cGA extends Algorithm {

	public cGA() {
		super();
	}
	
	public cGA(String name){
		super(name);
	}
	
	
	public void run() {

		
		while(!termination.isFinish()){
			
			BinaryUMDA distribution = (BinaryUMDA) population.getIndividual(0);

			IChromosome a = distribution.create_an_instance();
			evaluator.evaluate(distribution);
			double af = distribution.getFitness();
			IChromosome b = distribution.create_an_instance();
			evaluator.evaluate(distribution);
			double bf = distribution.getFitness();
			
			if(Configuration.minimization){
				if(af<bf){
					distribution.update_distribution(a, b);
				}else{
					distribution.update_distribution(b, a);
				}
			}else{
				if(af>bf){
					distribution.update_distribution(a, b);
				}else{
					distribution.update_distribution(b, a);
				}				
			}
			notifyObserver();				
		}
	}


}
