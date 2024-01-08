
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

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;

import random.CommonState;
import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.config.Logger;
import geneura.org.evoag.EVAG;
import geneura.org.evoag.EVAGChurn;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.statistics.IObservable;
import geneura.org.termination.Termination;

public class SimpleNewscastChurn implements INeighborhood,IObservable{
	
	private EVAGChurn[] _pop;
	private boolean[][] _graph;
	private static Individual best;
	
	private int counting = 0;
	private int printing = 0;
	
	
	//Singleton
	
	public SimpleNewscastChurn(EVAGChurn[] evag){
		
		
		_pop = evag;
		_graph = new boolean[_pop.length][_pop.length];
		
		
		//Bootstrapping
		if (Configuration.neighborhood_init.equals("lattice")){
			for(int i=0;i<_pop.length;i++){
				for (int j=0;j<Configuration.topology_cache;j++){
					_graph[i][(i+(j+1))%_pop.length] = true;
					_graph[i][((i-(j+1))%_pop.length > -1) ? (i-(j+1))%_pop.length : _pop.length+((i-(j+1))%_pop.length)] = true;
				}
			}
		}else if (Configuration.neighborhood_init.equals("wattsstrogatz")){
			//Building a regular ring
			for(int i=0;i<_pop.length;i++){
				for (int j=0;j<Configuration.topology_cache;j++){
					_graph[i][(i+(j+1))%_pop.length] = true;
					_graph[i][((i-(j+1))%_pop.length > -1) ? (i-(j+1))%_pop.length : _pop.length+((i-(j+1))%_pop.length)] = true;
				}
				/*_graph[i][(i+1)%_pop.length] = true;
				_graph[i][(i+2)%_pop.length] = true;
				_graph[i][(i+3)%_pop.length] = true;
				_graph[i][(i+4)%_pop.length] = true;
				_graph[i][((i-1)%_pop.length > -1) ? (i-1)%_pop.length : _pop.length+((i-1)%_pop.length)] = true;
				_graph[i][((i-2)%_pop.length > -1) ? (i-2)%_pop.length : _pop.length+((i-2)%_pop.length)] = true;
				_graph[i][((i-3)%_pop.length > -1) ? (i-3)%_pop.length : _pop.length+((i-3)%_pop.length)] = true;
				_graph[i][((i-4)%_pop.length > -1) ? (i-4)%_pop.length : _pop.length+((i-4)%_pop.length)] = true;*/
			}
			
			//Rewiring
			for(int i=0;i<_pop.length;i++){
				for(int j=0;j<i;j++){
					if(_graph[i][j] && CommonState.r.nextDouble()<0.1){ //We initialize with a watts strogatz graph
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
		
			
		}else{
			//Building a random graph
			for(int i=0;i<_pop.length;i++){
				for(int j=0;j<Configuration.topology_cache;j++){
					int pos = (int)(CommonState.r.nextDouble()*_pop.length);
					boolean set=false;
					while(!set){
						if(!_graph[i][pos]){
							_graph[i][pos]=true;
							set=true;
						}else{
							pos = (int)(CommonState.r.nextDouble()*_pop.length);
						}
					}
				}
			}
	
		}

		
	}
	
	public INeighborhood getNeighborhoodforNode(int index){
		
		NodeNeighborhoodNewscastChurn nn= new NodeNeighborhoodNewscastChurn(this,_pop[index]);
		
		for(int i=0;i<_pop.length;i++){
			if(_graph[index][i])
				nn.addEvAg(_pop[i]);
		}
		return nn;
	}
	
	public void set_evag(int index, EVAGChurn evag){
		_pop[index] = evag;
	}
	
	public Individual getIndividual(int index) {
		return _pop[index].get_individual();
	}

	public Individual getRandomIndividual() {
		int size = getSize();
		int rindex = CommonState.r.nextInt(size);
		
		int count =-1;
		int pos = -1;
		boolean found=false;
		for(int i=0;i<_pop.length && !found;i++){
			pos++;
			if(!_pop[i].failure){
				count++;
				if(count==rindex)
					found=true;
			}
		}
		return _pop[pos].get_individual();
	}

	public int getSize() {
		int count = 0;
		for (int i=0;i<_pop.length;i++)
			if (!_pop[i].failure)
				count++;
		return count;
	}

	public void setIndividual(int index, Individual ind) {
		_pop [index].set_individual(ind);
	}
	
	public void setBest(int index) {
		
		
	}
	

	

	public int getBest() {
		int pos_best=-1;

		for (int i=0; i<_pop.length; i++){
			if(pos_best==-1 && !_pop[i].failure)
				pos_best=i;
			else if(!_pop[i].failure)
				pos_best = (_pop[i].this_individual().better(_pop[pos_best].this_individual())) ? i : pos_best;
		}
		return pos_best;
	}
	
	

	public int getWorst() {
		int pos_worst=0;
		for (int i=1; i<_pop.length; i++){
			if(!_pop[i].failure)
				pos_worst = (!_pop[i].get_individual().better(_pop[pos_worst].get_individual())) ? i : pos_worst;
		}
		return pos_worst;
	}

	public void repaintgraph(){
	}
	
	public String getStateAsString() {
		double avg = 0;
		//int pos_best = 0;
		int number_of_best = 0;
		
		double avgSelection = 0;

		for (int i=0; i<_pop.length; i++){
			if(!_pop[i].failure){
				avg += _pop[i].get_individual().getFitness();
				
				avgSelection += _pop[i].get_tournament_size();
			//	if (_pop[i].get_individual().better(_pop[pos_best].get_individual()))
				//	pos_best = i;
				if (_pop[i].get_individual().getFitness() == _pop[0].getEvaluator().hit_value())
					number_of_best ++;	
			}
		}
		int size = getSize();
		avg /= size ;
		avgSelection /= size ;
		
		
		//Computing hamming distance
		double hamm = 0;
		double estimated = 0;
		int count=0;
		if (Configuration.adaptive_selection && Configuration.representation.equals("binary")){
			
			//Frequencies of zeros
			int[] frequencies = new int[Configuration.chromosome_size];
			for(int i=0;i<Configuration.chromosome_size;i++)
				frequencies[i] = 0;
			
			for (int i=0; i<_pop.length; i++){
				//Computing the hamming distance to a string of ones
				if(!_pop[i].failure){
					int hamming=0;
					for (int j=0;j<Configuration.chromosome_size;j++){
						if(!((Boolean)_pop[i].get_individual().getChr().getGen(j)).booleanValue())
							frequencies[j]++;
					}
					count++;
					
					estimated += _pop[i].hamm_smooth;
				}
			}
			
			
			for (int j=0;j<Configuration.chromosome_size;j++){
				int zeros = frequencies[j];
				int ones = count - frequencies[j];
				hamm += 2* zeros * ones; 				
			}
			hamm /= count *(count-1)*1.0;
			estimated /= count*1.0;
			
						
		}

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
				if(!_pop[i].failure){
					int hamming=0;
					for (int j=0;j<Configuration.chromosome_size;j++){
						if(!((Boolean)_pop[i].get_individual().getChr().getGen(j)).booleanValue())
							hamming++;
					}
					if(frequencies[hamming]==0)
						N++;
					frequencies[hamming]++;	
				}
			}
			
			//Computing the shannon entropy based on hamming distance
			for (int i=0; i<Configuration.chromosome_size+1; i++){
				if(frequencies[i]!=0){
					double freq = frequencies[i]/(size*1.0);
					entropy -= freq*Math.log(freq);
				}
			}
		}
		
		
		//Painting the JUNG graph
		
		String toreturn = ""+_pop[getBest()].get_individual().getFitness()+" "+avg;
		
		if (Configuration.entropy)
			toreturn += " "+entropy;
		
		if (Configuration.adaptive_selection)
			toreturn += " "+avgSelection+" "+hamm+" "+estimated;
		
		return toreturn;
		
	}
	
	//public EVAG get_evag(int index){
	//	return _pop[index];
	//}

	public void copyNeighborhood(INeighborhood neighborhood) {
		// TODO Auto-generated method stub
		
	}
			

	public Individual getBestIndividual() {
		return getIndividual(getBest());
	}
	
	public Algorithm executeNeighborhoodPolicy() {
		return null;
	}

	public double getAvg() {
		// TODO Auto-generated method stub
		double avg = 0;
		int count = 0;
		for (int i=0; i<_pop.length; i++){
			if(!_pop[i].failure){
				avg += _pop[i].this_individual().getFitness();
				count++;
			}
		}
		return (avg*1.0)/count;
		
	}

	public Individual getThisIndividual(int index) {
		// TODO Auto-generated method stub
		return _pop[index].this_individual();
	}

	public int getRandomNeighborhood() {
		int size = getSize();
		int rindex = CommonState.r.nextInt(size);
		
		int count =-1;
		int pos = -1;
		boolean found=false;
		for(int i=0;i<_pop.length && !found;i++){
			pos++;
			if(!_pop[i].failure){
				count++;
				if(count==rindex)
					found=true;
			}
		}
		
		return pos;
		
	}
	
	public INeighborhood getNeighborhood(int index) {
		
		return _pop[index].getNeighborhood();
		
//		int size = getSize();
//		int rindex = index;
//		
//		int count =-1;
//		int pos = -1;
//		boolean found=false;
//		for(int i=0;i<_pop.length && !found;i++){
//			pos++;
//			if(!_pop[i].failure){
//				count++;
//				if(count==rindex)
//					found=true;
//			}
//		}
//		
//		
//		
//		
//		return _pop[rindex].getNeighborhood();
//		
	}
	
	public UndirectedSparseGraph getGraph(){
		return null;
	}
	
	public void reset_information_exchange(){
		for (int i=0; i<_pop.length; i++){
			_pop[i].information_exchange = 0;
			_pop[i].agent_exchange = 0;
		}
	}

	public EVAGChurn getRandomEVAG() {
		// TODO Auto-generated method stub
		return null;
	}

}
