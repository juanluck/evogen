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
package geneura.org.factories;

import java.lang.reflect.InvocationTargetException;

import random.CommonState;

import geneura.org.Algorithm;
import geneura.org.Evaluator;
import geneura.org.config.Configuration;
import geneura.org.individuals.IChromosome;
import geneura.org.individuals.IComparator;
import geneura.org.individuals.Individual;
import geneura.org.individuals.binary.BinaryIndividual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.neighborhood.demes.FixedPopulation;
import geneura.org.operators.Crossover;
import geneura.org.operators.Mutation;
import geneura.org.replacement.IReplacement;
import geneura.org.selection.ISelection;
import geneura.org.ssGA.SSGA;
import geneura.org.statistics.IObservable;
import geneura.org.statistics.Observer;
import geneura.org.termination.ITermination;
import geneura.org.termination.NumberEvaluation;
import geneura.org.termination.SolutionNotImproved;
import geneura.org.termination.Termination;
import geneura.org.termination.ValueReached;

public class FactorySSGA {
	
	public static Algorithm createBinarySSGA(){
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Observer observer = new Observer(Configuration.statistic_sample);
		CommonState.setSeed(Configuration.seed);
		Individual._min = Configuration.minimization;
		
		Individual individual = new BinaryIndividual(Configuration.chromosome_size);
		
		Class[] evaluator_args = {};
		Object[] evaluator_obj = new Object[]{};
		Evaluator evaluator = null;
		
		try {
			evaluator = (Evaluator) loader.loadClass(Configuration.evaluation)
			.getConstructor(evaluator_args)
			.newInstance(evaluator_obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		observer.registerObservable(evaluator);	
		
		
		FixedPopulation population = new FixedPopulation(Configuration.population_size,individual, evaluator);
		
		
		observer.registerObservable(population);
		
		Class[] replacement_args = { };
		
		IReplacement replacement = null;
		try {
			replacement = (IReplacement) loader.loadClass(Configuration.replacement)
			.getConstructor(replacement_args)
			.newInstance(new Object[]{});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Class[] selection_args = {int.class};
		Object[] selection_obj = new Object[]{ new Integer(Configuration.selection_param) };
		ISelection selection = null;
		
		try {
			selection = (ISelection) loader.loadClass(Configuration.selection)
			.getConstructor(selection_args)
			.newInstance(selection_obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Class[] crossover_args = {double.class};
		Object[] crossover_obj = new Object[]{ new Double(Configuration.crossover_probability) };
		Crossover crossover = null;
		
		try {
			crossover = (Crossover) loader.loadClass(Configuration.crossover)
			.getConstructor(crossover_args)
			.newInstance(crossover_obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
				
		Class[] mutation_args = {double.class};
		Object[] mutation_obj = new Object[]{ new Double(Configuration.mutation_probability) };
		Mutation mutation = null;
		
		try {
			mutation = (Mutation) loader.loadClass(Configuration.mutation)
			.getConstructor(mutation_args)
			.newInstance(mutation_obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		ITermination [] termination_criteria;
		int n_criteria = 0;
		
		//TODO: Here we have to check all the termination criteria in Configuration
		if (Configuration.termination_max_evaluation != 0)
			n_criteria++;
		if (Configuration.termination_hit_value)
			n_criteria++;
		if (Configuration.termination_not_improved)
			n_criteria++;
		
		termination_criteria = new ITermination[n_criteria];
		
		int index_criteria = 0;
		
		if (Configuration.termination_max_evaluation != 0){
			NumberEvaluation aux = new NumberEvaluation(Configuration.termination_max_evaluation);
			termination_criteria[index_criteria] = (ITermination)aux; 
			observer.registerObservable((IObservable)aux);
			index_criteria++;
		}
		if (Configuration.termination_hit_value){
			ValueReached aux; 
			if (Configuration.termination_value_is_reached == Double.MIN_VALUE)
				aux = new ValueReached(evaluator.hit_value(),population);
			else
				aux = new ValueReached(Configuration.termination_value_is_reached,population);
			
			termination_criteria[index_criteria] = (ITermination)aux;
			observer.registerObservable((IObservable)aux);
			index_criteria++;
		}
		
		if (Configuration.termination_not_improved){
			SolutionNotImproved aux = new SolutionNotImproved(population);
			termination_criteria[index_criteria] = (ITermination)aux;
			observer.registerObservable((IObservable)aux);
			index_criteria++;
		}
		
		
		Termination termination = new Termination(termination_criteria);
		termination.attach(observer);
		
		SSGA ssGA = new SSGA();
		ssGA.setAlgorithm(population, evaluator, replacement, selection, crossover, mutation, termination);
		ssGA.attach(observer);
		Observer.restart();
		return ssGA;
	}
}
