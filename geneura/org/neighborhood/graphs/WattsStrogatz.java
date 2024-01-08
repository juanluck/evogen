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
import geneura.org.config.Configuration;
import geneura.org.evoag.EVAG;
import geneura.org.evoag.EVAGChurn;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.statistics.IObservable;

public class WattsStrogatz implements INeighborhood,IObservable{
	private EVAG[] _pop;
	private boolean[][] _graph;
	private static int pos_best = 0;
	private static Individual best;
	
	
	public WattsStrogatz(EVAG[] evag,double rewiring){
		_pop = evag;
		_graph = new boolean[_pop.length][_pop.length];
		
		int simetric_degree =0;
		
		if(_pop.length <= Configuration.topology_cache )
			simetric_degree = (Configuration.topology_cache /3)-1;
		else
			simetric_degree = Configuration.topology_cache/2;
		
		//Building a regular ring
		for(int i=0;i<_pop.length;i++)
			for (int j=0;j<simetric_degree;j++){
				_graph[i][(i+(j+1))%_pop.length] = true;
				_graph[i][((i-(j+1))%_pop.length > -1) ? (i-(j+1))%_pop.length : _pop.length+((i-(j+1))%_pop.length)] = true;
			}
		//for(int i=0;i<_pop.length;i++){
		//	_graph[i][(i+1)%_pop.length] = true;
		//	_graph[i][(i+2)%_pop.length] = true;
		//	_graph[i][((i-1)%_pop.length > -1) ? (i-1)%_pop.length : _pop.length+((i-1)%_pop.length)] = true;
		//	_graph[i][((i-2)%_pop.length > -1) ? (i-2)%_pop.length : _pop.length+((i-2)%_pop.length)] = true;
		//}
		
		//Rewiring
		for(int i=0;i<_pop.length;i++){
			for(int j=0;j<i;j++){
				if(_graph[i][j] && CommonState.r.nextDouble()<rewiring){ //We initialize with a watts strogatz graph
					int node = CommonState.r.nextInt(_pop.length);
					while(_graph[i][node] || node==i){
						node = (node+1)%_pop.length; 
					}
					_graph[i][j] = false;
					_graph[j][i] = false;
					_graph[i][node] =true;
					_graph[node][i] = true;
				}
			}
		}
		
//		for(int i=0;i<_pop.length;i++){
//			for(int j=0;j<i;j++){
//				if(_graph[i][j] && CommonState.r.nextDouble()<rewiring){
//					int node = CommonState.r.nextInt(_pop.length);
//					while(_graph[i][node] || node==i){
//						node = (node+1)%_pop.length; 
//					}
//					_graph[i][j] = false;
//					_graph[j][i] = false;
//					_graph[i][node] =true;
//					_graph[node][i] = true;
//				}
//			}
//		}
		
		
	}
	
	public INeighborhood getNeighborhoodforNode(int index){
		
		NodeNeighborhood nn= new NodeNeighborhood(this);
		
		for(int i=0;i<_pop.length;i++){
			if(_graph[index][i])
				nn.addEvAg(_pop[i]);
		}
		return nn;
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
	
	public static synchronized void set_pos_best(int index){
		WattsStrogatz.pos_best = index;
	}
	

	public int getBest() {
		int pos_best=0;
		for (int i=1; i<_pop.length; i++){
			pos_best = (_pop[i].this_individual().better(_pop[pos_best].this_individual())) ? i : pos_best;
		}
		return pos_best;
	}
	
	public static synchronized int best_pos(){
		return WattsStrogatz.pos_best;
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
					if(!((Boolean)_pop[i].get_individual().getChr().getGen(j)).booleanValue())
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
		
		if (number_of_best == Configuration.population_size && Configuration.Algorithm=="TAKEOVER")
			System.exit(1);
		if (Configuration.Algorithm == "TAKEOVER")
			return ""+_pop[getBest()].get_individual().getFitness()+" "+avg+" "+number_of_best;
		else if (Configuration.entropy)
			return ""+_pop[getBest()].get_individual().getFitness()+" "+avg+" "+entropy;
		else
			return ""+_pop[getBest()].get_individual().getFitness()+" "+avg;
	}
	
	public EVAG get_evag(int index){
		return _pop[index];
	}

	public void copyNeighborhood(INeighborhood neighborhood) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		System.out.println(""+((-1)%7)+" ");
	}
		

	public Individual getBestIndividual() {
		// TODO Auto-generated method stub
		return getIndividual(pos_best);
	}

	public double getAvg() {
		double avg=_pop[0].this_individual().getFitness();
		for (int i=1; i<_pop.length; i++){
			avg += _pop[i].this_individual().getFitness();
		}
		return (avg*1.0)/_pop.length;
	}

	public Individual getThisIndividual(int index) {
		// TODO Auto-generated method stub
		return _pop[index].this_individual();
	}

	public INeighborhood getNeighborhood(int index) {
		// TODO Auto-generated method stub
		return _pop[index].getNeighborhood();
	}
	
	public ArchetypeGraph getGraph() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Algorithm executeNeighborhoodPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	public EVAGChurn getRandomEVAG() {
		// TODO Auto-generated method stub
		return null;
	}
}
