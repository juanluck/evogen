package geneura.org.factories;

import geneura.org.Algorithm;
import geneura.org.Evaluator;
import geneura.org.config.Configuration;
import geneura.org.evoag.EVAG;
import geneura.org.evoag.EVAGseq;
import geneura.org.graphs.StandAloneNewscast;
import geneura.org.individuals.Individual;
import geneura.org.individuals.binary.BinaryIndividual;
import geneura.org.neighborhood.graphs.SimpleNewscast;
import geneura.org.neighborhood.graphs.WattsStrogatz;
import geneura.org.operators.Crossover;
import geneura.org.operators.Mutation;
import geneura.org.selection.ISelection;
import geneura.org.statistics.IObservable;
import geneura.org.statistics.Observer;
import geneura.org.termination.ITermination;
import geneura.org.termination.NumberEvaluation;
import geneura.org.termination.SolutionNotImproved;
import geneura.org.termination.Termination;
import geneura.org.termination.ValueReached;
import random.CommonState;

public class FactoryEVAGseq {

	public static Algorithm createNewscastEVAGseq(){
		
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Observer observer = new Observer(Configuration.statistic_sample);
		CommonState.setSeed(Configuration.seed);
		Individual._min = Configuration.minimization;
		
		
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
		
		EVAG evag[] = new EVAG[Configuration.population_size];
		for (int i=0;i<Configuration.population_size;i++){
			Individual individual = new BinaryIndividual(Configuration.chromosome_size);
			evag[i] = new EVAG(individual);
			evag[i].set_index(i);
			evaluator.evaluate(evag[i].this_individual());
		}
		
		
		evag[0].setAlgorithm(null, evaluator, null, null, null, null, null );
		
		//Creating the topology
		SimpleNewscast sn = new SimpleNewscast(evag);
		
		for (int i=0;i<Configuration.population_size;i++){
			evag[i].setNeighborhood(sn.getNeighborhoodforNode(i));
		}
		
		observer.registerObservable(sn);
		
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
				aux = new ValueReached(evaluator.hit_value(),sn);
			else
				aux = new ValueReached(Configuration.termination_value_is_reached,sn);
			
			termination_criteria[index_criteria] = (ITermination)aux;
			observer.registerObservable((IObservable)aux);
			index_criteria++;
		}
		
		if (Configuration.termination_not_improved){
			SolutionNotImproved aux = new SolutionNotImproved(sn);
			termination_criteria[index_criteria] = (ITermination)aux;
			observer.registerObservable((IObservable)aux);
			index_criteria++;
		}
		
		
		Termination termination = new Termination(termination_criteria);
		termination.attach(observer);
		
		
		
		EVAGseq evagseq = new EVAGseq();
		evagseq.setAlgorithm(sn, evaluator, null, selection, crossover, mutation, termination);
	
		evagseq.attach(observer);
		Observer.restart();
		return evagseq;
		
	}

	public static Algorithm createWattsStrogatzEVAGseq(){
		
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Observer observer = new Observer(Configuration.statistic_sample);
		CommonState.setSeed(Configuration.seed);
		Individual._min = Configuration.minimization;
		
		
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
		
		EVAG evag[] = new EVAG[Configuration.population_size];
		for (int i=0;i<Configuration.population_size;i++){
			Individual individual = new BinaryIndividual(Configuration.chromosome_size);
			evag[i] = new EVAG(individual);
			evag[i].set_index(i);
			evaluator.evaluate(evag[i].this_individual());
		}
		
		
		evag[0].setAlgorithm(null, evaluator, null, null, null, null, null );
		
		//Creating the topology
		WattsStrogatz ws = new WattsStrogatz(evag,Configuration.topology_rewiring);
		
		for (int i=0;i<Configuration.population_size;i++){
			evag[i].setNeighborhood(ws.getNeighborhoodforNode(i));
		}
		
		observer.registerObservable(ws);
		
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
				aux = new ValueReached(evaluator.hit_value(),ws);
			else
				aux = new ValueReached(Configuration.termination_value_is_reached,ws);
			
			termination_criteria[index_criteria] = (ITermination)aux;
			observer.registerObservable((IObservable)aux);
			index_criteria++;
		}
		
		if (Configuration.termination_not_improved){
			SolutionNotImproved aux = new SolutionNotImproved(ws);
			termination_criteria[index_criteria] = (ITermination)aux;
			observer.registerObservable((IObservable)aux);
			index_criteria++;
		}
		
		
		Termination termination = new Termination(termination_criteria);
		termination.attach(observer);
		
		
		
		EVAGseq evagseq = new EVAGseq();
		evagseq.setAlgorithm(ws, evaluator, null, selection, crossover, mutation, termination);
	
		evagseq.attach(observer);
		Observer.restart();
		return evagseq;
		
	}

	
}
