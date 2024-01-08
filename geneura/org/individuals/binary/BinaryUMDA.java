package geneura.org.individuals.binary;

import random.CommonState;
import geneura.org.config.Configuration;
import geneura.org.individuals.IChromosome;
import geneura.org.individuals.Individual;

public class BinaryUMDA extends Individual{
	
	private double[] _distribution_gens;
	
	private static double INCREASE=0.01;
		
	public BinaryUMDA(){
		
	}
	
	public BinaryUMDA(int bits) {
					
		_distribution_gens = new double [bits];
		for(int i=0; i<bits; i++){
			_distribution_gens[i] = 0.5;
		}
		
		BinaryChromosome crm = new BinaryChromosome(bits);
		setChr(crm);
	}
	
	
	
	public Object clone() {
		BinaryUMDA ind = new BinaryUMDA();
		ind.setChr((IChromosome)this.chr.clone());
		ind.fitness = this.fitness;
		
		ind._distribution_gens = new double [this._distribution_gens.length];
		
		for (int i=0;i<this._distribution_gens.length;i++){
			ind._distribution_gens[i]=this._distribution_gens[i];
		}
		
		
		return ind;
	}
	
		
	public double[] get_distribution_gens() {
		return _distribution_gens;
	}

	public void set_distribution_gens(double[] distributionGens) {
		_distribution_gens = distributionGens;
	}
	

	public void update_distribution(IChromosome x){
		boolean[] xgen = x.asboolean(); 

		for(int i=0;i<x.getLength();i++)
			if(xgen[i])
				_distribution_gens[i] += 1.0/(Configuration.population_size);
			else
				_distribution_gens[i] -= 1.0/(Configuration.population_size);
	}


	public void update_distribution(boolean[][] chromosomes){
		
		boolean[] current = getChr().asboolean();
		
		for(int i=0;i<Configuration.chromosome_size;i++){
			double i_value=0;
			for (int j=0; j< Configuration.UMDAf*Configuration.population_size;j++){
				if(chromosomes[j][i])
					i_value++;
			}	
			_distribution_gens[i] = i_value/(Configuration.UMDAf*Configuration.population_size);
		}
		
		
	}
	
	public void update_distribution(IChromosome winner,IChromosome loser){
		boolean[] wingen = winner.asboolean(); 
		boolean[] losgen = loser.asboolean();
		
		for(int i=0;i<winner.getLength();i++)
			if(wingen[i]!=losgen[i])
				if(wingen[i] && _distribution_gens[i]<1)
					_distribution_gens[i] += 1.0/(Configuration.population_size);
				else if(_distribution_gens[i]>0)
					_distribution_gens[i] -= 1.0/(Configuration.population_size);
	}
	public void update_distribution(double[] distri){

		for(int i=0;i<distri.length;i++){
			_distribution_gens[i] += distri[i];
			_distribution_gens[i] /= 2.0;
			
		}
	}

	public void update_distribution(IChromosome winner,double[] distri,IChromosome loser){
		boolean[] wingen = winner.asboolean(); 
		boolean[] losgen = loser.asboolean();
		
		for(int i=0;i<winner.getLength();i++)
			if(wingen[i]!=losgen[i])
				if(wingen[i])
					_distribution_gens[i] += 1.0/(Configuration.population_size);
				else
					_distribution_gens[i] -= 1.0/(Configuration.population_size);
			else{
				_distribution_gens[i] += distri[i];
				_distribution_gens[i] /= 2.0;
			}
	}
	
	
	
	public void update_distribution(Individual[] aux){
		
		for(int i=0;i<Configuration.chromosome_size;i++){
			double distri=0;
			for(int j=0;j<aux.length;j++){
				if (aux[j].getChr().asboolean()[i])
					distri++;
			}
			_distribution_gens[i] = distri/(1.0*aux.length);
		}
		
	}

	
	public IChromosome create_an_instance(){
		for(int i=0;i<_distribution_gens.length;i++){
			//System.out.println("Anterior: "+((Boolean)getChr().getGen(i)).booleanValue());
			if(CommonState.r.nextDouble()<_distribution_gens[i])
				getChr().setGen(i, new Boolean(true));
			else
				getChr().setGen(i, new Boolean(false));
			//System.out.println("Posterior: "+((Boolean)getChr().getGen(i)).booleanValue());
		}
		return (IChromosome)(getChr().clone());
		
	}

}
