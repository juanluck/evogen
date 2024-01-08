package geneura.org.factories;

import geneura.org.Algorithm;
import geneura.org.Evaluator;
import geneura.org.cGA.cGA;
import geneura.org.config.Configuration;
import geneura.org.gGA.GGA;
import geneura.org.individuals.Individual;
import geneura.org.individuals.binary.BinaryIndividual;
import geneura.org.individuals.binary.BinaryUMDA;
import geneura.org.neighborhood.demes.FixedPopulation;
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

public class FactorycGA {
	public static Algorithm createBinarycGA(){
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Observer observer = new Observer(Configuration.statistic_sample);
		CommonState.setSeed(Configuration.seed);
		Individual._min = Configuration.minimization;
		Individual individual = new BinaryUMDA(Configuration.chromosome_size);
		
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
		
		FixedPopulation population = new FixedPopulation(1,individual, evaluator);
		
		
		observer.registerObservable(population);

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
		
		cGA GA = new cGA();
		GA.setAlgorithm(population, evaluator, null, null, null, null, termination);
		GA.attach(observer);
		Observer.restart();
		return GA;
	}

}
