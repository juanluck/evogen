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
package geneura.org.neighborhood.demes;

import edu.uci.ics.jung.graph.ArchetypeGraph;
import random.CommonState;
import geneura.org.Algorithm;
import geneura.org.Evaluator;
import geneura.org.ResizingBisection;
import geneura.org.config.Configuration;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.statistics.IObservable;
import geneura.org.termination.Termination;

public class PanmicticResizing implements INeighborhood,IObservable{
	
	private Individual[] _pop;
	private int pos_worst = 0;
	private int pos_best = 0;
	private boolean bw_pos = false;
	private Evaluator _evaluator;
	private static double _tau,_rho;
	//TODO: Refactorizar estos constructores
	private static String POLICY_RANDOM="random";
	private static String POLICY_WORST="worst";
	private static String POLICY_DIFFERENT="different";
	
	public PanmicticResizing(int size,double tau,double rho){
		_pop = new Individual[size];
		_tau = tau;
		_rho = rho;
	}
	
	public PanmicticResizing(int size, Individual ind, Evaluator evaluator) {
		_evaluator = evaluator;
		_pop = new Individual[size];
		
		ResizingBisection.counter_ind = 0;
		for (int i=0; i<size; i++){
			_pop[i] = ResizingBisection.pop[ResizingBisection.counter_exp][ResizingBisection.counter_ind];
    		ResizingBisection.counter_ind++;	
    		evaluator.evaluate(_pop[i]);
			pos_best = (_pop[i].better(_pop[pos_best])) ? i : pos_best;
			pos_worst = (!_pop[i].better(_pop[pos_worst])) ? i : pos_worst;
		}
		bw_pos = true;
	}
	
	public Individual getIndividual(int index) {
		return _pop[index];
	}

	public Individual getRandomIndividual() {
		return _pop[CommonState.r.nextInt(getSize())];
	}

	public int getSize() {
		return _pop.length;
	}

	public void setIndividual(int index, Individual ind) {
		_pop [index] = ind;
		bw_pos = false;
	}

	public int getBest() {
		pos_best=0;
		pos_worst=0;
		for (int i=0; i<getSize() ; i++){
			pos_best = (_pop[i].better(_pop[pos_best])) ? i : pos_best;
			pos_worst = (!_pop[i].better(_pop[pos_worst])) ? i : pos_worst;
		}
		bw_pos = true;
		return pos_best;
	}

	public int getWorst() {
		pos_best=0;
		pos_worst=0;
		for (int i=0; i<getSize(); i++){
			pos_best = (_pop[i].better(_pop[pos_best])) ? i : pos_best;
			pos_worst = (!_pop[i].better(_pop[pos_worst])) ? i : pos_worst;
		}
		bw_pos = true;
		return pos_worst;
	}

	public void copyNeighborhood(INeighborhood neighborhood) {
		_pop=null;
		_pop = new Individual[neighborhood.getSize()];
		
		for (int i=0;i<neighborhood.getSize();i++){
			_pop[i] = neighborhood.getIndividual(i);
		}
		bw_pos = false;	
	}
	
	public String getStateAsString() {
		double avg = 0;
		int pos_best = 0;
		for (int i=0; i<getSize(); i++){
			avg += _pop[i].getFitness();
			if (_pop[i].better(_pop[pos_best]))
				pos_best = i;
		}
		avg /= getSize() ;
		return " "+getSize()+" "+Termination.get_n_generation()+" "+_pop[pos_best].getFitness()+" "+avg;
	}

	public void setBest(int index) {
		
	}

	public Individual getBestIndividual() {
		return getIndividual(getBest());
	}

	public double getAvg() {
		double avg = 0;
		for (int i=0; i<getSize(); i++){
			avg += _pop[i].getFitness();
		}
		avg /= getSize() ;
		// TODO Auto-generated method stub
		return avg;
	}

	public Individual getThisIndividual(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public static int resize(int current_pop){
		
		if (Termination.get_n_generation()<ResizingBisection.n_gen[ResizingBisection.counter_exp]){
			double a = Math.sqrt(Termination.get_n_generation()/(ResizingBisection.n_gen[ResizingBisection.counter_exp]*1.0));
			double b = Math.pow(a, Configuration.tau)*(1-Configuration.rho);
			//System.out.println("Population: " + (int) (current_pop * (1-b)));
			if ((int) (current_pop * (1-b)) < 5)
				return 4;
			else
				return (int) (current_pop * (1-b));	
		}else{
			//System.out.println("Population: " + current_pop);
			return current_pop;
		}
		
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
