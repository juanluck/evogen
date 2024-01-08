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
package geneura.org.neighborhood.graphs;

import edu.uci.ics.jung.graph.ArchetypeGraph;
import random.CommonState;
import geneura.org.Algorithm;
import geneura.org.Evaluator;
import geneura.org.config.Configuration;
import geneura.org.evoag.EVAG;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.statistics.IObservable;
import geneura.org.termination.Termination;

public class PanmicticGraph implements INeighborhood,IObservable{
	
	private EVAG[] _pop;
	private static int pos_best = 0;
	private int printing = 0;
	//TODO: Refactorizar este constructor
	

	
	public PanmicticGraph(EVAG[] evag){
		_pop = evag;
	}
	
	public void set_evag(int index, EVAG evag){
		_pop[index] = evag;
	}
	
	public Individual getIndividual(int index) {
		return _pop[index].get_individual();
	}

	public Individual getRandomIndividual() {
		return _pop[CommonState.r.nextInt(_pop.length)].get_individual();
	}

	public int getSize() {
		return _pop.length;
	}

	public void setIndividual(int index, Individual ind) {
		_pop [index].set_individual(ind);
	}
	
	public void setBest(int index) {
		set_pos_best(index);
	}
	
	public synchronized void set_pos_best(int index){
		PanmicticGraph.pos_best = index;
	}

	public int getBest() {
		return best_pos();
	}
	
	private synchronized int best_pos(){
		return PanmicticGraph.pos_best;
	}

	public int getWorst() {
		int pos_worst=0;
		for (int i=1; i<_pop.length; i++){
			pos_worst = (!_pop[i].get_individual().better(_pop[pos_worst].get_individual())) ? i : pos_worst;
		}
		return pos_worst;
	}

	public String getStateAsString() {
		double avg = 0;
		int number_of_best = 0;
		for (int i=0; i<_pop.length; i++){
			avg += _pop[i].get_individual().getFitness();
			if (_pop[i].get_individual().getFitness() == _pop[0].getEvaluator().hit_value())
				number_of_best ++;
		}
		avg /= _pop.length ;
		if (!Configuration.JUNG && number_of_best == Configuration.population_size && (Configuration.Algorithm=="TAKEOVER"|| Configuration.Algorithm == "GraphStatistics")){
			if(printing == 10)
				Termination.kill();
			else
				printing++;

		}


		if (Configuration.Algorithm == "TAKEOVER" || Configuration.Algorithm == "GraphStatistics")
			return ""+_pop[getBest()].get_individual().getFitness()+" "+avg+" "+number_of_best;
		else
			return ""+_pop[getBest()].get_individual().getFitness()+" "+avg;
		
	}
	
	public EVAG get_evag(int index){
		return _pop[index];
	}

	public void copyNeighborhood(INeighborhood neighborhood) {
		// TODO Auto-generated method stub
		
	}

	public Individual getBestIndividual() {
		return getIndividual(getBest());
	}

	public double getAvg() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Individual getThisIndividual(int index) {
		// TODO Auto-generated method stub
		return _pop[index].this_individual();
	}

	public INeighborhood getNeighborhood(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArchetypeGraph getGraph() {
		// TODO Auto-generated method stub
		return null;
	}
	public Algorithm executeNeighborhoodPolicy() {
		// TODO Auto-generated method stub
		return null;
	}
}
