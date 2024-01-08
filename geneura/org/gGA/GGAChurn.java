package geneura.org.gGA;

import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.demes.FixedPopulation;
import geneura.org.termination.Termination;
import geneura.org.termination.Traces;

public class GGAChurn extends Algorithm{

	Traces _traces;
	public GGAChurn(Traces traces) {
		super();
		_traces=traces;
	}
	
	public GGAChurn(String name, Traces traces){
		super(name);
		_traces=traces;
	}
	
	
	public void run() {
		
		while(!termination.isFinish()){
			int popatnextgen = _traces.popatgeneration((int)Termination.get_n_generation());
			FixedPopulation aux_pop = new FixedPopulation(popatnextgen);
			
			int increaseinpop = aux_pop.getSize() - population.getSize(); 
			 
			for (int i=0; i < popatnextgen; i++){
 
				if(Configuration.churnnewindividualpolicy.equals("random") && increaseinpop > 0){	// Policy: if the number of computing nodes increases, Individuals are added randomly
					Class []args = {int.class};
					Individual ind = selection.select(population); // Just for java reflection
					try {
						Individual aux = (Individual) ind.getClass().getConstructor(args)
							.newInstance(
									new Object[]{
											new Integer(Configuration.chromosome_size)
											}
									);
						evaluator.evaluate(aux);
						aux_pop.setIndividual(i, aux);
						increaseinpop--;
					}catch (Exception e) {	e.printStackTrace(); System.exit(-1);} 
					
				}else if(Configuration.churnnewindividualpolicy.equals("breed") && increaseinpop > 0){
					Individual aux = mutation.mutate(crossover.cross(selection.select(population), selection.select(population)));
					evaluator.evaluate(aux);
					aux_pop.setIndividual(i, (Individual)aux.clone());	
					increaseinpop--;
				}else if(Configuration.churnnewindividualpolicy.equals("localsearch") && increaseinpop > 0){
					Individual ind = selection.select(population);
					Individual aux = mutation.mutate(ind);
					evaluator.evaluate(aux);
					if(ind.better(aux))
						aux_pop.setIndividual(i, (Individual)ind.clone());
					else
						aux_pop.setIndividual(i, (Individual)aux.clone());
					increaseinpop--;
				}else{
					Individual aux = mutation.mutate(crossover.cross(selection.select(population), selection.select(population)));
					evaluator.evaluate(aux);
					aux_pop.setIndividual(i, (Individual)aux.clone());	
				}
									
			}
			//Elitism
			Individual best = population.getIndividual(population.getBest());
			evaluator.evaluate(best);
			replacement.replace(best, aux_pop);
			population.copyNeighborhood(aux_pop);
			Termination.increment_generation();
			notifyObserver();
			
		}
	}
}
