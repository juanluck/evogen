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

import java.util.ArrayList;

import edu.uci.ics.jung.graph.ArchetypeGraph;

import random.CommonState;

import geneura.org.Algorithm;
import geneura.org.evoag.EVAG;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;

public class NodeNeighborhood implements INeighborhood{
	ArrayList<EVAG> _neigbours;
	WattsStrogatz _ws;

	public NodeNeighborhood(WattsStrogatz ws) {
		_neigbours = new ArrayList<EVAG>();
		_ws = ws;
	}

	
	public void addEvAg(EVAG evag){
		_neigbours.add(evag);
	}
	
	
	public void copyNeighborhood(INeighborhood neighborhood) {
		// TODO Auto-generated method stub
		
	}

	
	public int getBest() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

	public Individual getIndividual(int index) {
		return _neigbours.get(index).get_individual();
	}

	public Individual getRandomIndividual() {
		return _neigbours.get(CommonState.r.nextInt(_neigbours.size())).get_individual();
	}

	public int getSize() {
		return _neigbours.size();
	}

	public int getWorst() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setBest(int index) {
		WattsStrogatz.set_pos_best(index);
	}

	public void setIndividual(int index, Individual ind) {
		// TODO Auto-generated method stub
		
	}


	public Individual getBestIndividual() {
		// TODO Auto-generated method stub
		return _ws.getBestIndividual();
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
