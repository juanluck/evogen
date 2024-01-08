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
package geneura.org.neighborhood;

import edu.uci.ics.jung.graph.ArchetypeGraph;
import geneura.org.Algorithm;
import geneura.org.evoag.EVAGChurn;
import geneura.org.individuals.Individual;

public interface INeighborhood {

	
	public Individual getRandomIndividual();
	public EVAGChurn getRandomEVAG();
	public Individual getIndividual(int index);
	public Individual getThisIndividual(int index);
	public void setIndividual (int index, Individual ind);
	public abstract int getSize();
	
	public void copyNeighborhood(INeighborhood neighborhood);
	
	/**
	 * @return The index of the worst individual
	 */
	public int getWorst();
	
	/**
	 * @return The index of the best individual
	 */
	public int getBest();
	
	public Individual getBestIndividual();
	
	public void setBest(int index);
	
	/**
	 * 
	 * @return the Average of the fitness
	 */
	//TODO: This method is currently implemented just in FixedPopulation
	//TODO: In order to allow SolutionNotImproved it has to be implemented in graphs.*
	public double getAvg();
	
	public INeighborhood getNeighborhood(int index);
	
	public ArchetypeGraph getGraph();
	
	public Algorithm executeNeighborhoodPolicy();
	
}
