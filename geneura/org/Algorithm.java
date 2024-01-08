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
package geneura.org;

import java.util.ArrayList;
import java.util.ListIterator;

import geneura.org.individuals.IChromosome;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.operators.Crossover;
import geneura.org.operators.Mutation;
import geneura.org.replacement.IReplacement;
import geneura.org.selection.ISelection;
import geneura.org.statistics.Observer;
import geneura.org.termination.Termination;

public abstract class Algorithm extends Thread{
	protected INeighborhood population;
	protected Evaluator evaluator;
	protected IReplacement replacement;
	protected ISelection selection;
	protected ISelection []selections;
	protected Crossover crossover;
	protected Mutation mutation;
	protected Termination termination;

	protected Observer observer;
	
	protected boolean _shouldEnd = false;
	
	
	public Algorithm() {
		super();
	}
	
	public Algorithm(String name){
		super(name);
	}

	public void setAlgorithm(INeighborhood population, Evaluator evaluator, IReplacement replacement, ISelection selection, Crossover crossover, Mutation mutation, Termination termination){
		this.population = population;
		this.evaluator = evaluator;
		this.replacement = replacement;
		this.selection = selection;
		this.crossover = crossover;
		this.mutation = mutation;
		this.termination = termination;		
	}
	
	public void setAlgorithmS(INeighborhood population, Evaluator evaluator, IReplacement replacement, ISelection []selections, Crossover crossover, Mutation mutation, Termination termination){
		this.population = population;
		this.evaluator = evaluator;
		this.replacement = replacement;
		this.selections = selections;
		this.crossover = crossover;
		this.mutation = mutation;
		this.termination = termination;		
	}

	
	public void attach(Observer obs){
		observer = obs;
	}
	
	public void dettach(Observer obs){
		observer = null;
	}
	
	public void notifyObserver(){
		observer.Update();
	}
	
	public void setNeighborhood (INeighborhood neighborhood){
		population = neighborhood;
	}
	
	public INeighborhood getNeighborhood (){
		return population;
	}
	
	public Evaluator getEvaluator(){
		return evaluator;
	}
	
	public void setTermination(Termination term){
		termination = term;
	}
	
	public void kill(){
		_shouldEnd = true;
	}
	
	public boolean shouldEnd(){
		return _shouldEnd;
	}
	
}
