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

import geneura.org.Algorithm;
import geneura.org.Evaluator;
import geneura.org.GPEvaluator;
import geneura.org.config.Configuration;
import geneura.org.evoag.EVAG;
import geneura.org.evoag.EVAGseqGP;
import geneura.org.gGA.GGA;
import geneura.org.gGA.GGAGP;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.individuals.binary.BinaryIndividual;
import geneura.org.individuals.real.DoubleIndividual;
import geneura.org.individuals.tree.GPData;
import geneura.org.individuals.tree.IGenomeValidator;
import geneura.org.individuals.tree.SimpleGPIndividual;
import geneura.org.individuals.tree.SimpleTreeGenomeValidator;
import geneura.org.neighborhood.demes.FixedPopulation;
import geneura.org.neighborhood.graphs.SimpleNewscast;
import geneura.org.operators.Crossover;
import geneura.org.operators.Mutation;
import geneura.org.replacement.IReplacement;
import geneura.org.selection.ISelection;
import geneura.org.statistics.IObservable;
import geneura.org.statistics.Observer;
import geneura.org.termination.ITermination;
import geneura.org.termination.NumberEvaluation;
import geneura.org.termination.SolutionNotImproved;
import geneura.org.termination.Termination;
import geneura.org.termination.ValueReached;
import random.CommonState;

public class FactoryGGAGP {
	
	public static Algorithm createGGAGP(){
		
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Observer observer = new Observer(Configuration.statistic_sample);
		CommonState.setSeed(Configuration.seed);
		Individual._min = Configuration.minimization;
		
		
		Class[] evaluator_args = {};
		Object[] evaluator_obj = new Object[]{};
		GPEvaluator evaluator = null;
		
		try {
			evaluator = (GPEvaluator) loader.loadClass(Configuration.evaluation)
			.getConstructor(evaluator_args)
			.newInstance(evaluator_obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		observer.registerObservable(evaluator);	
		
		Class[] data_args = {};
		Object[] data_obj = new Object[]{};
		GPData problemData = null;
		
		try {
			problemData = (GPData) loader.loadClass(Configuration.gp_data)
			.getConstructor(data_args)
			.newInstance(data_obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        FixedPopulation population = new FixedPopulation(Configuration.population_size,evaluator);
        for (int i=0;i<Configuration.population_size;i++){
			GPIndividual individual = new SimpleGPIndividual(evaluator.getPrimitives(), evaluator, problemData, i);
			evaluator.evaluate(individual);	
			population.setIndividual(i, individual);	
		}
		
        
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
		
	
		
		ISelection selection = null;
		ISelection []selections = null;
		if(Configuration.selection2.equals("null")){
			Class[] selection_args = {int.class};
			Object[] selection_obj = new Object[]{ new Integer(Configuration.selection_param) };
			
			
			try {
				selection = (ISelection) loader.loadClass(Configuration.selection)
				.getConstructor(selection_args)
				.newInstance(selection_obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			selections = new ISelection[2];
			Class[] selection_args = {int.class};
			Object[] selection_obj = new Object[]{ new Integer(Configuration.selection_param) };
			
			
			try {
				selections[0] = (ISelection) loader.loadClass(Configuration.selection)
				.getConstructor(selection_args)
				.newInstance(selection_obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				selections[1] = (ISelection) loader.loadClass(Configuration.selection2)
				.getConstructor(selection_args)
				.newInstance(selection_obj);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		
		
		IGenomeValidator genomeV = new SimpleTreeGenomeValidator(Configuration.gp_tree_depth);
				
		Class[] crossover_args = new Class[]{IGenomeValidator.class,double.class,double.class};
		Object[] crossover_obj = new Object[]{genomeV, new Double(Configuration.crossover_probability),new Double(Configuration.gp_internalXprobability) };
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
		
		GGAGP gGA = new GGAGP();
		if (selections==null)
			gGA.setAlgorithm(population, evaluator, replacement, selection, crossover, mutation, termination);
		else
			gGA.setAlgorithmS(population, evaluator, replacement, selections, crossover, mutation, termination);
		gGA.attach(observer);
		Observer.restart();
		return gGA;
	}	
}
