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
package geneura.org.takeover;

import geneura.org.evoag.EVAG;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;

public class TAKEOVER extends EVAG{
	
	

	
	public TAKEOVER(Individual ind) {
		super(ind);
	}
	
	public TAKEOVER(String name, Individual ind){
		super(name,ind);
	}
	
	public void set_index (int index){
		_index = index;
	}
	
	public int get_index(){
		return _index;
	}
	
	public void run() {
		
		//We wait 20 cycles for convergence to SW
		for (int i=0;i<20;i++){
			population.getBestIndividual();
			Thread.yield();
		}
		while(!termination.isFinish() && !shouldEnd()){
			

			Individual aux = (Individual)selection.select(population).clone();
			evaluator.evaluate(aux);
			//synchronized (this) {
			if (aux.better(_individual)){
				set_individual(aux);
			}
			//}
		

			population.getBestIndividual();
			//if (_individual.better(population.getBestIndividual())){
			//	population.setBest(_index);
			//}
		
			Thread.yield();
			notifyObserver();
		}
		_shouldEnd = true;
	}

	public synchronized Individual get_individual() {
		return (Individual)_individual.clone();
	}
	
	public synchronized Individual this_individual() {
		return _individual;
	}
	
	public synchronized void set_individual (Individual ind){
		_individual = ind;
	}
	
	public synchronized INeighborhood getNeighborhood(){
		return population;
	}
}
	

