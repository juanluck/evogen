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

import edu.uci.ics.jung.exceptions.ConstraintViolationException;
import edu.uci.ics.jung.graph.ArchetypeGraph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseVertex;
import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.evoag.EVAG;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;

import random.CommonState;

public class NodeNeighborhoodNewscast implements INeighborhood{
	
//	The  +1 element is set for the self individual
	EVAG[] _neigbours = new EVAG[Configuration.topology_cache+1];
	int[] _timestamp = new int[Configuration.topology_cache+1];
	int _index=0;
	
	public SimpleNewscast _nws;

	public NodeNeighborhoodNewscast(SimpleNewscast nws, EVAG self) {
		_nws = nws;
		
		for(int i=0;i<_neigbours.length;i++){
			_neigbours[i] = null;
			_timestamp[i] = 0;
		}
		_neigbours[_neigbours.length-1] = self;
	}

	
	public boolean addEvAg(EVAG evag){
		if(_index < _neigbours.length-1){
			_neigbours[_index] = evag;
			if (Configuration.JUNG){
				try{
				_neigbours[_neigbours.length-1].getGraph().addEdge(new UndirectedSparseEdge(_neigbours[_neigbours.length-1].getVertex(),_neigbours[_index].getVertex()));
				}catch (Exception e) {
				}
			}
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
		//int pos_individual = CommonState.r.nextInt(_index);
		//if (pos_individual== _index){
			//return _neigbours[_neigbours.length-1].get_individual();
		//}else{
			return _neigbours[CommonState.r.nextInt(_index)].get_individual();
		//}
	}

	public int getSize() {
		return _index;
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
	
	private synchronized EVAG executeNewscast(){
		
		// We increment a cycle in the timestamp of the current node
		_timestamp[_neigbours.length-1]++; 
		
		// and get a random neighbour
		EVAG neighbor= _neigbours[CommonState.r.nextInt(_index)];
		
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
	}
	
	//It merges the current neighborhood with neighbor
	//As postcondition, both neighborhood are the same
	private void merge(EVAG neighbor){
		int[] ts = new int[_timestamp.length];
		EVAG[] nb = new EVAG[_neigbours.length];
		
		int auxindex = 0;

		NodeNeighborhoodNewscast nn = (NodeNeighborhoodNewscast)neighbor.getNeighborhood();
		
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
		
		//In case that the framework JUNG is active
		if (Configuration.JUNG){
	/*		UndirectedSparseGraph G1 = _neigbours[_neigbours.length-1].getGraph();
			synchronized (G1) {
				UndirectedSparseVertex V1 = _neigbours[_neigbours.length-1].getVertex();
				UndirectedSparseVertex Foreign = nn._neigbours[nn._neigbours.length-1].getVertex();
				
				 
				G1.removeEdges(V1.getOutEdges());
				G1.removeEdges(Foreign.getOutEdges());
				
				
				for(int i=0;i<_index;i++){
					try
                    {
						G1.addEdge(new UndirectedSparseEdge(V1,_neigbours[i].getVertex()));
                    } catch (ConstraintViolationException e)
                    {                   
                    }
					
				}

				for(int i=0;i<nn._index;i++){
					try
                    {
					G1.addEdge(new DirectedSparseEdge(Foreign,nn._neigbours[i].getVertex()));
                    } catch (ConstraintViolationException e)
                    {                   
                    }
				}
			}
		*/				
		}
		
	}


	public double getAvg() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void replace_altruistic(Individual ind){
		if (ind!=null){
			int pos1 = CommonState.r.nextInt(_index);
			//int pos2 = CommonState.r.nextInt(_index);
			
			//if (_neigbours[pos1].get_hitsize()>_neigbours[pos2].get_hitsize()){
				_neigbours[pos1].compareAndStore((Individual)ind.clone());	
			//}else{
			//	_neigbours[pos2].compareAndStore((Individual)ind.clone());
			//}
			
			
		}
	}
	
	public void replace(){
		_neigbours[_neigbours.length-1].replaceInvididuals();
	}


	public Individual getThisIndividual(int index) {
		// TODO Auto-generated method stub
		if(index<0){
			return _neigbours[CommonState.r.nextInt(_index)].this_individual();
		}else if(index < _index)
			return _neigbours[index].this_individual();
		else
			return null;
		
	}

	public INeighborhood getNeighborhood(int index) {
		
		return this;
	}

	public ArchetypeGraph getGraph() {
		return _nws.getGraph();
	}
	
	public EVAG getEvAg(){
		return _neigbours[_neigbours.length-1];
	}
	
	
}
