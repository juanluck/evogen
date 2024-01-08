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
package geneura.org.evoag;



import random.CommonState;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseVertex;
import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.individuals.Individual;
import geneura.org.individuals.binary.BinaryUMDA;

public class EVAG extends Algorithm{
	
	protected Individual _individual;
	protected Individual _auxindividual=null;
	protected int _hitsize = 0;
	protected int _tournament_size;


	protected int _index;
	public static int DYNAMIC_COUNT = 0;
	private UndirectedSparseVertex V;
	
	public int information_exchange=0;
	public int n_processor;
	public int agent_exchange =0;
	


	
	public EVAG(Individual ind) {
		super();
		_individual = ind;
		_auxindividual=null;
		_tournament_size = Configuration.selection_param;
		V = new UndirectedSparseVertex();
	}
	
	public EVAG(String name, Individual ind){
		super(name);
		_individual = ind;
		_auxindividual=null;
		_tournament_size = Configuration.selection_param;
		V = new UndirectedSparseVertex();
	}
	
	public synchronized UndirectedSparseVertex getVertex(){
		return (UndirectedSparseVertex)V;
	}
	
	public synchronized UndirectedSparseGraph getGraph(){
		return (UndirectedSparseGraph)V.getGraph();
	}
	
	public void set_index (int index){
		n_processor = index % Configuration.n_processors;
		_index = index;
	}
	
	public int get_index(){
		return _index;
	}
	
	public void run() {
		while(!termination.isFinish() && !shouldEnd()){
			
			/*Individual a = selection.select(population);
			Individual b = selection.select(population);
			
			if (a.better(b) && _individual.better(b))
				b = _individual;
			else if ((b.better(a) && _individual.better(b)))
				a = _individual;
			*/

			
			
			
			if (Configuration.dynamic && Configuration.dynamic_detection){

					if(evaluator.dynamic_recalculation(_individual)){
					
						Individual aux = mutation.mutate(crossover.cross(selection.select(population),_individual));//_selection.select(population)));
						evaluator.evaluate(aux);

						if (aux.better(_individual)){
							set_individual(aux);
						}

						population.getBestIndividual();
					}
						
			}else if(Configuration.dynamic && !Configuration.dynamic_detection){
				Individual aux = mutation.mutate(crossover.cross(selection.select(population),_individual));//_selection.select(population)));
				evaluator.evaluate(aux);
				evaluator.evaluate(_individual);
				
				
				if (aux.better(_individual)){
					set_individual(aux);
				}
				
				population.getBestIndividual();
	
									
			}else{

				Individual aux = mutation.mutate(crossover.cross(selection.select(population),_individual));//_selection.select(population)));
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
			}
			
			
			
				
			Thread.yield();
			notifyObserver();
		}
		_shouldEnd = true;
	}

	public synchronized Individual get_individual() {
		if (_individual instanceof BinaryUMDA){
			return (BinaryUMDA) _individual.clone();
		}else{
			return (Individual) _individual.clone();
		}
	}
	
	public synchronized Individual this_individual() {
		return _individual;
	}
	
	public synchronized void set_individual (Individual ind){
		_individual = ind;
	}
	
	public void compareAndStore(Individual ind){
		
		
		if (_auxindividual == null){
			_auxindividual = ind;
			_hitsize ++;
		}else if(ind.better(_auxindividual)){
			_auxindividual = ind;
			_hitsize ++;
			//System.err.print("X");
		}

	}
	
	public void replaceInvididuals(){
		if(_auxindividual!=null && _auxindividual.better(_individual)){
			_individual = (Individual)_auxindividual.clone();
			_auxindividual = null;
			//System.err.print("X");
		}else if(_auxindividual == null){
			_auxindividual = null;
			//System.err.print("o");
		}else{
			//System.err.print("-");
		}
		
		_hitsize=0;

		_auxindividual = null;
		
	}
	
	public int get_hitsize(){
		return _hitsize;
	}

	public int get_tournament_size() {
		return _tournament_size;
	}

}
