package geneura.org.gGA;

import geneura.org.Algorithm;
import geneura.org.GPEvaluator;
import geneura.org.config.Configuration;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.individuals.tree.GPData;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.SimpleGPIndividual;
import geneura.org.neighborhood.demes.FixedPopulation;
import geneura.org.operators.tree.GPIniter;
import geneura.org.termination.Termination;
import geneura.org.termination.Traces;
import random.CommonState;

public class GGAGPChurn extends Algorithm{

	Traces _traces;
	public GGAGPChurn(Traces traces) {
		super();
		_traces=traces;
	}
	
	public GGAGPChurn(String name, Traces traces){
		super(name);
		_traces=traces;
	}
	
	
	public void run() {
		
		while(!termination.isFinish()){
			int popatnextgen = _traces.popatgeneration((int)Termination.get_n_generation());
			FixedPopulation aux_pop = new FixedPopulation(popatnextgen);
			int increaseinpop = aux_pop.getSize() - population.getSize(); 
			
			for (int i=0; i < popatnextgen; i++){
				if(Configuration.churnnewindividualpolicy.endsWith("random") && increaseinpop > 0){	// Policy: if the number of computing nodes increases, Individuals are added randomly

					GPIndividual ind = (GPIndividual)selections[0].select(population); // Just for java reflection
				
					ClassLoader loader = ClassLoader.getSystemClassLoader();
					Class[] args = new Class[] {IPrimitive[].class,GPEvaluator.class,GPData.class,int.class};
					IPrimitive[] pPrimitives = ind.pPrimitives;
					GPEvaluator problem = (GPEvaluator)this.evaluator;
					GPData gpdata = ind.problem_data;
					Integer popIndex = new Integer(CommonState.r.nextInt(Configuration.population_size)); // To determine which inizialization process is used.
					Object[] obj = new Object[]{pPrimitives,(GPEvaluator)problem,gpdata,popIndex};
					
					
					try {
						GPIndividual aux = (GPIndividual) loader.loadClass("geneura.org.individuals.tree.SimpleGPIndividual")
						.getConstructor(args)
							.newInstance(obj);
						evaluator.evaluate(aux);
						aux_pop.setIndividual(i, aux);
						increaseinpop--;
					}catch (Exception e) {	e.printStackTrace(); System.exit(-1);} 
					
				}else if(Configuration.churnnewindividualpolicy.endsWith("breed") && increaseinpop > 0){
					Individual aux=null;
					
					if (CommonState.r.nextDouble() < Configuration.crossover_probability)
						aux = (GPIndividual)crossover.cross(selections[0].select(population),selections[0].select(population));//population.getIndividual(i));
					else
						aux = (GPIndividual)selections[1].select(population);
					
			
					if (aux!=null){
						evaluator.evaluate(aux);
						aux_pop.setIndividual(i, aux);	
					}else{
						aux_pop.setIndividual(i,selections[1].select(population));
					}
					increaseinpop--;

				}else if(Configuration.churnnewindividualpolicy.endsWith("localsearch") && increaseinpop > 0){
					
					Individual ind = selections[0].select(population);
					Individual auxind = mutation.mutate(ind);
					evaluator.evaluate(auxind);
					
					if (auxind.better(ind)){
						aux_pop.setIndividual(i, auxind);
						//System.out.println("mejor");
					}
					else{
						aux_pop.setIndividual(i, ind);
						//System.out.println("peor");
					}
										
					increaseinpop--;

				}else{
					Individual aux=null;
					
					if (CommonState.r.nextDouble() < Configuration.crossover_probability)
						aux = (GPIndividual)crossover.cross(selections[0].select(population),selections[0].select(population));//population.getIndividual(i));
					else
						aux = (GPIndividual)selections[1].select(population);
					
			
					if (aux!=null){
						evaluator.evaluate(aux);
						aux_pop.setIndividual(i, aux);	
					}else{

						aux_pop.setIndividual(i,selections[1].select(population));
					}
			
					
				}
											
			}
			//Elitism
			Individual best = population.getIndividual(population.getBest());
			//evaluator.evaluate(best);
			replacement.replace(best, aux_pop);
			population.copyNeighborhood(aux_pop);
			Termination.increment_generation();
			notifyObserver();
		}
	}
}