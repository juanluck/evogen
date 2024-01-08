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
/**
 * A fixed panmictic population of Individuals
 * 
 *
 */
public class FixedPopulation implements INeighborhood,IObservable{

	private Individual[] _pop;
	private int pos_worst = 0;
	private int pos_best = 0;
	private boolean bw_pos = false;
	private Evaluator _evaluator;
	public static boolean control_resizing=false;
	//TODO: Refactorizar estos constructores
	
	public FixedPopulation(int size){
		_pop = new Individual[size];
	}
	
	public FixedPopulation(int size, Evaluator evaluator){
		_pop = new Individual[size];
		_evaluator = evaluator;
	}
	
	public FixedPopulation(int size, Individual ind, Evaluator evaluator) {
		_evaluator = evaluator;
		_pop = new Individual[size];
		
		ResizingBisection.counter_ind = 0;
		if (!ResizingBisection.bisection){
			Class []args = {int.class};
			try {
				for (int i=0; i<size; i++){
					_pop[i] = (Individual) ind.getClass().getConstructor(args)
					.newInstance(
							new Object[]{
									new Integer(Configuration.chromosome_size)
									}
							);
					evaluator.evaluate(_pop[i]);
					pos_best = (_pop[i].better(_pop[pos_best])) ? i : pos_best;
					pos_worst = (!_pop[i].better(_pop[pos_worst])) ? i : pos_worst;
				}
				bw_pos = true;

			} catch (Exception e) {	e.printStackTrace(); System.exit(-1);} 
		}else{
			if (!control_resizing){
				Class []args = {int.class};
				try {
					for (int i=0; i<size; i++){
						_pop[i] = (Individual) ind.getClass().getConstructor(args)
						.newInstance(
								new Object[]{
										new Integer(Configuration.chromosome_size)
										}
								);
						evaluator.evaluate(_pop[i]);
						pos_best = (_pop[i].better(_pop[pos_best])) ? i : pos_best;
						pos_worst = (!_pop[i].better(_pop[pos_worst])) ? i : pos_worst;
						ResizingBisection.popaux[ResizingBisection.counter_exp][ResizingBisection.counter_ind] = (Individual)_pop[i].clone();
				    	ResizingBisection.counter_ind++;
					}
					bw_pos = true;

				} catch (Exception e) {	e.printStackTrace(); System.exit(-1);} 
			}else{
				for (int i=0; i<size; i++){
					_pop[i] = (Individual) ResizingBisection.pop[ResizingBisection.counter_exp][ResizingBisection.counter_ind].clone();
					Individual aux = (Individual) ResizingBisection.pop[ResizingBisection.counter_exp][ResizingBisection.counter_ind].clone();
					ResizingBisection.popaux[ResizingBisection.counter_exp][ResizingBisection.counter_ind] = aux;
		    		ResizingBisection.counter_ind++;	
		    		evaluator.evaluate(_pop[i]);
					pos_best = (_pop[i].better(_pop[pos_best])) ? i : pos_best;
					pos_worst = (!_pop[i].better(_pop[pos_worst])) ? i : pos_worst;
				}
				bw_pos = true;
			}
		}
				 
	}
	
	public Individual getIndividual(int index) {
		return _pop[index];
	}

	public Individual getRandomIndividual() {
		return _pop[CommonState.r.nextInt(_pop.length)];
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
		for (int i=0; i<_pop.length ; i++){
			pos_best = (_pop[i].better(_pop[pos_best])) ? i : pos_best;
			pos_worst = (!_pop[i].better(_pop[pos_worst])) ? i : pos_worst;
		}
		bw_pos = true;
		return pos_best;
	}

	public int getWorst() {
		pos_best=0;
		pos_worst=0;
		for (int i=0; i<_pop.length; i++){
			pos_best = (_pop[i].better(_pop[pos_best])) ? i : pos_best;
			pos_worst = (!_pop[i].better(_pop[pos_worst])) ? i : pos_worst;
		}
		bw_pos = true;
		//System.out.println("El peor es "+pos_worst+" y el tamano es "+_pop.length);
		return pos_worst;
	}

	public void copyNeighborhood(INeighborhood neighborhood) {
		_pop = new Individual[neighborhood.getSize()];
		for (int i=0;i<_pop.length;i++){
			_pop[i] = neighborhood.getIndividual(i);
		}
		bw_pos = false;	
	}
	
	public String getStateAsString() {
		double avg = 0;
		int pos_best = 0;
		for (int i=0; i<_pop.length; i++){
			avg += _pop[i].getFitness();
			if (_pop[i].better(_pop[pos_best]))
				pos_best = i;
		}
		avg /= _pop.length ;
		
		//Computing the shannon entropy based on hamming distance
		double entropy=0;
		if (Configuration.entropy && Configuration.representation.equals("binary")){
		
			
			//Frequencies of the hamming distance to a string of ones
			int[] frequencies = new int[Configuration.chromosome_size+1];
			int N=0; //N represents the number of different hamming distances
			for(int i=0;i<Configuration.chromosome_size+1;i++)
				frequencies[i] = 0;
			
			for (int i=0; i<_pop.length; i++){
				//Computing the hamming distance to a string of ones
				int hamming=0;
				for (int j=0;j<Configuration.chromosome_size;j++){
					if(!((Boolean)_pop[i].getChr().getGen(j)).booleanValue())
						hamming++;
				}
				if(frequencies[hamming]==0)
					N++;
				frequencies[hamming]++;
			}
			
			//Computing the shannon entropy based on hamming distance
			for (int i=0; i<Configuration.chromosome_size+1; i++){
				if(frequencies[i]!=0){
					double freq = frequencies[i]/(Configuration.population_size*1.0);
					entropy -= freq*Math.log(freq);
				}
			}
		}
		
		if (Configuration.entropy)
			return " P: "+_pop.length+" G: "+Termination.get_n_generation()+" BF: "+_pop[pos_best].getFitness()+" AF: "+avg+" NTROPY: "+entropy;
		else
			return " P: "+_pop.length+" G: "+Termination.get_n_generation()+" BF: "+_pop[pos_best].getFitness()+" AF: "+avg;
		
		
		
	}

	public void setBest(int index) {
		
	}

	public Individual getBestIndividual() {
		return getIndividual(getBest());
	}

	public double getAvg() {
		double avg = 0;
		for (int i=0; i<_pop.length; i++){
			avg += _pop[i].getFitness();
		}
		avg /= _pop.length ;
		return avg;
	}

	public Individual getThisIndividual(int index) {
		// TODO Auto-generated method stub
		return null;
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
