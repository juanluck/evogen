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
import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.evoag.EVAG;
import geneura.org.evoag.EVAGChurn;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;

import random.CommonState;

public class NodeNeighborhoodNewscastChurn implements INeighborhood{
	
//	The  +1 element is set for the self individual
	
	EVAGChurn[] _neigbours = new EVAGChurn[Configuration.topology_cache+1];
	int[] _timestamp = new int[Configuration.topology_cache+1];
	int _index=0;
	
	public SimpleNewscastChurn _nws;

	public NodeNeighborhoodNewscastChurn(SimpleNewscastChurn nws, EVAGChurn self) {
		_nws = nws;
		
		for(int i=0;i<_neigbours.length;i++){
			_neigbours[i] = null;
			_timestamp[i] = 0;
		}
		_neigbours[_neigbours.length-1] = self;
	}

	
	public boolean addEvAg(EVAGChurn evag){
		if(_index < _neigbours.length-1){
			_neigbours[_index] = evag;

			_index++;
			return true;
		}else{
			return false;
		}
	}
	

	
	public void copyNeighborhood(INeighborhood neighborhood) {
		
	}

	
	public int getBest() {
		return 0;
	}
	
	

	public Individual getIndividual(int index) {
		if(index < _index)
			return _neigbours[index].get_individual();
		else
			return null;
	}

	public Individual getRandomIndividual() {
		int size = getSize();
		if (size<=0){
			System.err.println("Size es: "+size);
			return null;
		}
		int rindex = CommonState.r.nextInt(size);
		
		int count =-1;
		int pos = -1;
		boolean found=false;
		for(int i=0;i<_index && !found;i++){
			pos++;
			if(!_neigbours[i].failure){
				count++;
				if(count==rindex)
					found=true;
			}
		}
		
		return _neigbours[pos].get_individual();
	}

	public int getSize() {
		int count = 0;
		for (int i=0;i<_index;i++)
			if (!_neigbours[i].failure)
				count++;
		return count;

	}

	public int getWorst() {
		return 0;
	}

	public void setBest(int index) {
		//SimpleNewscast.set_pos_best(index);
	}

	public void setIndividual(int index, Individual ind) {
	}


	public Individual getBestIndividual() {
		executeNewscast(); //Once per cycle it executes the newscast algorithm
		return null;//_nws.getBestIndividual();
	}
	
	public Algorithm executeNeighborhoodPolicy() {
		return executeNewscast(); //Once per cycle it executes the newscast algorithm
	}
	
	public int getRandomNeighborhood() {
		int size = getSize();
		int rindex = CommonState.r.nextInt(size);
		
		int count =-1;
		int pos = -1;
		boolean found=false;
		for(int i=0;i<_neigbours.length && !found;i++){
			pos++;
			if(!_neigbours[i].failure){
				count++;
				if(count==rindex)
					found=true;
			}
		}
		
		return pos;
		
	}
	
	public void increasecycle(){
		_timestamp[_neigbours.length-1]++;	
	}
	 
	
	private synchronized EVAG executeNewscast(){
		
		// We increment a cycle in the timestamp of the current node
		_timestamp[_neigbours.length-1]++; 

		//-- we erase failures from the cache
		compact(this);
		//--- Procedure to  get a random neighbour
		if(_index>0){
			EVAGChurn neighbor= _neigbours[getRandomNeighborhood()];
				
				//_neigbours[CommonState.r.nextInt(_index)];

			
			// -- we erase failures from neighbour cache 
			compact((NodeNeighborhoodNewscastChurn)neighbor.getNeighborhood());
			
			
			// for statistics on the overhead, in the case that both nodes are allocated on different processors
			if(Configuration.overhead){
				if (neighbor.n_processor != _neigbours[_neigbours.length-1].n_processor){
					_neigbours[_neigbours.length-1].information_exchange++;
					_neigbours[_neigbours.length-1].agent_exchange++;
					if (CommonState.r.nextDouble() < 0.5)
						neighbor.n_processor = _neigbours[_neigbours.length-1].n_processor;
				}
			}
			
			//We merge both neighborhoods 
			merge(neighbor);
			return neighbor;
	
		}else
			return null;
	}
	
	public void compact(NodeNeighborhoodNewscastChurn nn){
		EVAGChurn[] auxneigbours = new EVAGChurn[Configuration.topology_cache+1];
		int[] auxtimestamp = new int[Configuration.topology_cache+1];
		int auxindex=0;
		
		for(int i=0;i<nn._index;i++){
			if(!nn._neigbours[i].failure){
				auxneigbours[auxindex]=nn._neigbours[i];
				auxtimestamp[auxindex]=nn._timestamp[i];
				auxindex++;
			}
		}
		auxneigbours[_neigbours.length-1]=nn._neigbours[_neigbours.length-1];
		auxtimestamp[_neigbours.length-1]=nn._timestamp[_neigbours.length-1];
		
		
		nn._index = auxindex;
		nn._timestamp=auxtimestamp;
		nn._neigbours=auxneigbours;
	}
	
	
	//It merges the current neighborhood with neighbor
	//As postcondition, both neighborhood are the same
	private void merge(EVAGChurn neighbor){
		int[] ts = new int[_timestamp.length];
		EVAGChurn[] nb = new EVAGChurn[_neigbours.length];
		
		int auxindex = 0;

		NodeNeighborhoodNewscastChurn nn = (NodeNeighborhoodNewscastChurn)neighbor.getNeighborhood();
		
		int d1 = _index; // Number of elements in the first cache
		int d2 = nn._index; // Number of elements in the second cache

		
		//Copy the second cache without references to the first one and selfreferences
		for(int i=0;i<d2;i++){
			if (nn._neigbours[i].get_index() != _neigbours[_neigbours.length-1].get_index() &&
				nn._neigbours[i].get_index() != nn._neigbours[_neigbours.length-1].get_index()	){
				ts[auxindex] = nn._timestamp[i];
				nb[auxindex] = nn._neigbours[i];
				auxindex++;	
			}
		}
//		System.out.println(d2+" "+auxindex);
		
		int indexfirstvector = auxindex;
		//Check for duplicates. It keep the freshest.
		for(int i=0;i<d1;i++){

			
			//Here we guarantee no copy of the reference to the second cache and selfreferences
			if(_neigbours[i].get_index() != nn._neigbours[nn._neigbours.length-1].get_index()&&
			   _neigbours[i].get_index() != _neigbours[nn._neigbours.length-1].get_index()	){
				boolean found = false;
				for(int j=0;j<indexfirstvector && !found;j++){
					// If we find a duplicate, we keep the freshest reference
					if(_neigbours[i].get_index() == nb[j].get_index()){  
						found = true;									 
						if(_timestamp[i]>ts[j]){ 		 
							ts[j] = _timestamp[i];		
						}
					}
				}
				// If we don't find a duplicate
				if (!found){
					//If there is enough capacity in cache, we insert the item
					if (auxindex < _neigbours.length-1){
						nb[auxindex] = _neigbours[i];
						ts[auxindex] = _timestamp[i];
						auxindex++;
					}else{ // If not, we replace an item for the freshest
						boolean replace = false;
						for (int j=0;j<auxindex && !replace;j++){
							if (ts[j]<_timestamp[i]){
								ts[j] = _timestamp[i];
								nb[j] = _neigbours[i];
								replace = true;
							}
							
						}
					}
				}
			}
			
			
			
		}
		
		//If cache is not full
		if (auxindex < _neigbours.length-1){
			//We create space for the reference to the neighbour
			for (int i=1;i<=auxindex;i++){
				_neigbours[i] = nb[i-1];
				_timestamp[i] = ts[i-1];
				nn._neigbours[i] = nb[i-1];
				nn._timestamp[i] = ts[i-1];
			}
			_index = auxindex++;
			nn._index = auxindex++;
		//If it is full
		}else{
			//We'll overwrite the first element afterwards
			for (int i=0;i<auxindex;i++){
				_neigbours[i] = nb[i];
				_timestamp[i] = ts[i];
				nn._neigbours[i] = nb[i];
				nn._timestamp[i] = ts[i];
			}
			_index = auxindex;
			nn._index = auxindex;
		}

		//The first position of both caches is a link to the other 
		nn._neigbours[0] = _neigbours[_neigbours.length-1];
		nn._timestamp[0] = _timestamp[_timestamp.length-1];
		
		_neigbours[0] = nn._neigbours[nn._neigbours.length-1];
		_timestamp[0] = nn._timestamp[nn._timestamp.length-1];
		
	}


	public double getAvg() {
		// TODO Auto-generated method stub
		return 0;
	}


	public Individual getThisIndividual(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	public INeighborhood getNeighborhood(int index) {
		// TODO Auto-generated method stub
		return this;
	}

	public ArchetypeGraph getGraph() {
		return _nws.getGraph();
	}
	
	public EVAGChurn getEvAg(){
		return _neigbours[_neigbours.length-1];
	}
	
	
	public EVAGChurn[] get_neigbours(){
		return _neigbours;
	}
	
	public int get_index(){
		return _index;
	}


	public EVAGChurn getRandomEVAG() {
		
		int size = getSize();
		if (size<=0){
			System.err.println("Size es: "+size);
			return null;
		}
		int rindex = CommonState.r.nextInt(size);
		
		int count =-1;
		int pos = -1;
		boolean found=false;
		for(int i=0;i<_index && !found;i++){
			pos++;
			if(!_neigbours[i].failure){
				count++;
				if(count==rindex)
					found=true;
			}
		}
		
		return _neigbours[pos];
		
	}
	
	
}
