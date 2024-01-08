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
package geneura.org.evoag;



import java.util.Random;

import random.CommonState;
import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.config.distributions.SVPS;
import geneura.org.config.distributions.Weibull;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.selection.TournamentSelection;
import geneura.org.termination.Churn;
import geneura.org.termination.Termination;
import geneura.org.termination.Traces;

public class EVAGChurn extends EVAG{
	
	protected long life=100000;
	protected int local_cycle = 0;
	public boolean failure = false;
	public boolean alive = false;
	public boolean searcher = false;
	private boolean[] _trace;
	
	private static int selectedcode;
	
	private int hash;
	
	
	protected Churn _churn= null;
	protected int _tournament_size;
	
	
	private boolean firstgeneration = true;
	private boolean secondgeneration = true;
	
	private double fsel_tend=0;
	private int tendence=0;
	
	private int g2i=0;
	
	private double s_t;
	private double s_max=0;
	
	public double hamm_t=0;
	public double hamm_t1=0;
	public double hamm_t2=0;
	public double hamm_t3=0;
	public double hamm_t4=0;
	public double hamm_t5=0;
	public double hamm_t6=0;
	public double hamm_t7=0;
	public double hamm_t8=0;
	public double hamm_t9=0;
	
	public double inhamm_t=0;
	public double inhamm_t1=0;
	public double inhamm_t2=0;
	public double inhamm_t3=0;
	public double inhamm_t4=0;
	public double inhamm_t5=0;
	public double inhamm_t6=0;
	public double inhamm_t7=0;
	public double inhamm_t8=0;
	public double inhamm_t9=0;
	
	
	public int Nk=0;
	
	public double hamm_smooth=0;
	public double inhamm_smooth=0;
	public double hamm_smooth_t1=0;
	
	
	
	public EVAGChurn(Individual ind,Traces traces) {
		super(ind);
		_trace=traces.get();
		hash = CommonState.r.nextInt();
		_tournament_size = Configuration.selection_param;
		
		if (CommonState.r.nextDouble()< Configuration.selection2_prob)
			_tournament_size = Configuration.selection_param2;
		//life = (long) traces.get(local_cycle);
		//life = (long) SVPS.get(Configuration.churn_rho, Configuration.churn_tau,Configuration.termination_max_cycles);
	}
	
	public EVAGChurn(Individual ind) {
		super(ind);
		life = (long) Weibull.get(Configuration.weibull_lambda, Configuration.weibull_k);
		//life = (long) SVPS.get(Configuration.churn_rho, Configuration.churn_tau,Configuration.termination_max_cycles);
		hash = CommonState.r.nextInt();
		_tournament_size = Configuration.selection_param;
		if (CommonState.r.nextDouble()< Configuration.selection2_prob)
			_tournament_size = Configuration.selection_param2;

	}
	


	public EVAGChurn(String name, Individual ind) {
		super(name, ind);
		life = (long) Weibull.get(Configuration.weibull_lambda, Configuration.weibull_k);
		//life = (long) SVPS.get(Configuration.churn_rho, Configuration.churn_tau,Configuration.termination_max_cycles);
		// TODO Auto-generated constructor stub
		hash = CommonState.r.nextInt();
		_tournament_size = Configuration.selection_param;
		if (CommonState.r.nextDouble()< Configuration.selection2_prob)
			_tournament_size = Configuration.selection_param2;

	}

		
	public void register_churn_monitor(Churn churn){
		_churn = churn;
	}
	
	
	public long get_life(){
		return life;
	}
	
	public long get_local_cycle(){
		return local_cycle;
	}
	
	public boolean isFailing(){
		return !_trace[local_cycle];
	}
	
	public void run() {
		while(!termination.isFinish() && !shouldEnd()){
			
	
				Individual aux = mutation.mutate(crossover.cross(selection.select(population),_individual));//_selection.select(population)));
				evaluator.evaluate(aux);
				local_cycle++;

				//synchronized (this) {
				if (aux.better(_individual)){
					set_individual(aux);
				}
				//}
			

				population.getBestIndividual();
				//if (_individual.better(population.getBestIndividual())){
				//	population.setBest(_index);
				//}			
			
				
			Thread.yield();
			notifyObserver();
			if (local_cycle >= life)
				_shouldEnd = true;
		}
		_shouldEnd = true;
	}

	public int hashccode(){
		return hash;
	}

	public int get_tournament_size() {
		return _tournament_size;
	}
	

	public void increase_g2i(double sel_t_1){
		int old_size = _tournament_size;
		if (fsel_tend == 0){
			fsel_tend = sel_t_1;
			selectedcode = hash;
		}else{
			if (selectedcode == hash)
					System.out.println(local_cycle+" "+" H "+hamm_smooth+" "+inhamm_smooth+" "+_tournament_size);

			if(sel_t_1>fsel_tend){
				tendence++;
				//_tournament_size--;
			}else if (sel_t_1<fsel_tend){
				tendence/=2;
				//_tournament_size++;
			}
			fsel_tend=sel_t_1;
		}
		
		if (_tournament_size < 2 || _tournament_size > 32){
			_tournament_size = old_size;
		}
		g2i++;
	}
	

		
	//public void delta_tournament(double nt){
	//	_tournament_size = (int)((nt+_tournament_size)/2.0);
	//}
	
	public void delta_tournament (EVAGChurn[] sel_t_1,Individual curr_t_1){
		int old_size = _tournament_size;
		/*if (firstgeneration){
			if (Nk==0){
				hamm_t = estimated_hamming(sel_t_1, curr_t_1);
				Nk++;
			}else{
				
				hamm_smooth = kernelaveragesmoother(sel_t_1, curr_t_1);
				if (Nk==9){
					Nk=0;
					firstgeneration = false;
					secondgeneration = true;	
				}else{
					Nk++;
				}
			}
			
		}else*//* if(secondgeneration){
			if (Nk==0){
				hamm_t = estimated_hamming(sel_t_1, curr_t_1);
				inhamm_t = estimated_hamming_to_individual(sel_t_1, curr_t_1);
				Nk++;
			}else{
				
				hamm_smooth = kernelaveragesmoother(sel_t_1, curr_t_1);
				inhamm_smooth = kernelaveragesmootherin(sel_t_1, curr_t_1);
				if (Nk==9){					
					secondgeneration = false;
				}else{
					Nk++;
				}
			}
		}else{*/
			
			//hamm_smooth = kernelaveragesmoother(sel_t_1, curr_t_1);
			//inhamm_smooth = kernelaveragesmootherin(sel_t_1, curr_t_1);
		
			hamm_t = estimated_hamming(sel_t_1, curr_t_1);
			hamm_smooth = hamm_t;
			inhamm_smooth = estimated_hamming_to_individual(sel_t_1, curr_t_1);
		
			/*if(CommonState.r.nextDouble()< (hamm_smooth/(1.0*Configuration.chromosome_size))){
				_tournament_size++;
			}else{
				_tournament_size--;
			}*/
			
			if(inhamm_smooth<hamm_smooth){
				_tournament_size--;
			}else{
				_tournament_size++;
			}
			
			
			
			/*double slope = hamm_smooth_t1-hamm_smooth;
			
			if (Math.abs(hamm_smooth_t1-hamm_smooth)>s_max){
				s_max = Math.abs(slope);	
			}
			
			if (hamm_smooth_t1<hamm_smooth ){
				if(CommonState.r.nextDouble() < (Math.abs(slope)/(s_max*1.0)))
					_tournament_size=2;
				else
					_tournament_size--;
			}else if (hamm_smooth_t1>hamm_smooth ){
				if (CommonState.r.nextDouble() < (Math.abs(slope)/(s_max*1.0)))
					_tournament_size=30;
				else
					_tournament_size++;
			}
			
			
			hamm_smooth = hamm_smooth_t1;
			s_max = slope;*/
			
			/*double hamm_t1 = estimated_hamming(sel_t_1, curr_t_1);
			double s_t1 = hamm_t1 - hamm_t;
			
			if (Math.abs(s_t1)>s_max)
				s_max = Math.abs(s_t1);
			
			if (s_t1 < 0){ // Decrease in diversity
				if(CommonState.r.nextDouble() > (Math.abs(s_t1)/(s_max*1.0))){
					_tournament_size=2;
				}
			}else if (s_t1 > 0){
				if(CommonState.r.nextDouble() > (Math.abs(s_t1)/(s_max*1.0))){
					_tournament_size=30;
				}
			}
			
			
			
			hamm_t = hamm_t1;*/
		//}
		
		if (_tournament_size < 2 || _tournament_size > 32){
			_tournament_size = old_size;
		}
		
	}
	
	private double kernelaveragesmoother(EVAGChurn[] sel_t_1,Individual curr_t_1){
		if (Nk==1){
			hamm_t1 = hamm_t;
			hamm_t = estimated_hamming(sel_t_1, curr_t_1);
		}else if (Nk==2){
			hamm_t2 = hamm_t1;
			hamm_t1 = hamm_t;
			hamm_t = estimated_hamming(sel_t_1, curr_t_1);
		}else if (Nk==3){
			hamm_t3 = hamm_t2;
			hamm_t2 = hamm_t1;
			hamm_t1 = hamm_t;
			hamm_t = estimated_hamming(sel_t_1, curr_t_1);
		}else if (Nk==4){
			hamm_t4 = hamm_t3;
			hamm_t3 = hamm_t2;
			hamm_t2 = hamm_t1;
			hamm_t1 = hamm_t;
			hamm_t = estimated_hamming(sel_t_1, curr_t_1);
		}else if (Nk==5){
			hamm_t5 = hamm_t4;
			hamm_t4 = hamm_t3;
			hamm_t3 = hamm_t2;
			hamm_t2 = hamm_t1;
			hamm_t1 = hamm_t;
			hamm_t = estimated_hamming(sel_t_1, curr_t_1);
		}else if (Nk==6){
			hamm_t6 = hamm_t5;
			hamm_t5 = hamm_t4;
			hamm_t4 = hamm_t3;
			hamm_t3 = hamm_t2;
			hamm_t2 = hamm_t1;
			hamm_t1 = hamm_t;
			hamm_t = estimated_hamming(sel_t_1, curr_t_1);
		}else if (Nk==7){
			hamm_t7 = hamm_t6;
			hamm_t6 = hamm_t5;
			hamm_t5 = hamm_t4;
			hamm_t4 = hamm_t3;
			hamm_t3 = hamm_t2;
			hamm_t2 = hamm_t1;
			hamm_t1 = hamm_t;
			hamm_t = estimated_hamming(sel_t_1, curr_t_1);
		}else if (Nk==8){
			hamm_t8 = hamm_t7;
			hamm_t7 = hamm_t6;
			hamm_t6 = hamm_t5;
			hamm_t5 = hamm_t4;
			hamm_t4 = hamm_t3;
			hamm_t3 = hamm_t2;
			hamm_t2 = hamm_t1;
			hamm_t1 = hamm_t;
			hamm_t = estimated_hamming(sel_t_1, curr_t_1);
		}else if (Nk==9){
			hamm_t9 = hamm_t8;
			hamm_t8 = hamm_t7;
			hamm_t7 = hamm_t6;
			hamm_t6 = hamm_t5;
			hamm_t5 = hamm_t4;
			hamm_t4 = hamm_t3;
			hamm_t3 = hamm_t2;
			hamm_t2 = hamm_t1;
			hamm_t1 = hamm_t;
			hamm_t = estimated_hamming(sel_t_1, curr_t_1);
		}
		
		return (hamm_t9+hamm_t8+hamm_t7+hamm_t6+hamm_t5+hamm_t4+hamm_t3+hamm_t2+hamm_t1+hamm_t)/10.0;
		
	}
	
	private double kernelaveragesmootherin(EVAGChurn[] sel_t_1,Individual curr_t_1){
		if (Nk==1){
			inhamm_t1 = inhamm_t;
			inhamm_t = estimated_hamming_to_individual(sel_t_1, curr_t_1);
		}else if (Nk==2){
			inhamm_t2 = inhamm_t1;
			inhamm_t1 = inhamm_t;
			inhamm_t = estimated_hamming_to_individual(sel_t_1, curr_t_1);
		}else if (Nk==3){
			inhamm_t3 = inhamm_t2;
			inhamm_t2 = inhamm_t1;
			inhamm_t1 = inhamm_t;
			inhamm_t = estimated_hamming_to_individual(sel_t_1, curr_t_1);
		}else if (Nk==4){
			inhamm_t4 = inhamm_t3;
			inhamm_t3 = inhamm_t2;
			inhamm_t2 = inhamm_t1;
			inhamm_t1 = inhamm_t;
			inhamm_t = estimated_hamming_to_individual(sel_t_1, curr_t_1);
		}else if (Nk==5){
			inhamm_t5 = inhamm_t4;
			inhamm_t4 = inhamm_t3;
			inhamm_t3 = inhamm_t2;
			inhamm_t2 = inhamm_t1;
			inhamm_t1 = inhamm_t;
			inhamm_t = estimated_hamming_to_individual(sel_t_1, curr_t_1);
		}else if (Nk==6){
			inhamm_t6 = inhamm_t5;
			inhamm_t5 = inhamm_t4;
			inhamm_t4 = inhamm_t3;
			inhamm_t3 = inhamm_t2;
			inhamm_t2 = inhamm_t1;
			inhamm_t1 = inhamm_t;
			inhamm_t = estimated_hamming_to_individual(sel_t_1, curr_t_1);
		}else if (Nk==7){
			inhamm_t7 = inhamm_t6;
			inhamm_t6 = inhamm_t5;
			inhamm_t5 = inhamm_t4;
			inhamm_t4 = inhamm_t3;
			inhamm_t3 = inhamm_t2;
			inhamm_t2 = inhamm_t1;
			inhamm_t1 = inhamm_t;
			inhamm_t = estimated_hamming_to_individual(sel_t_1, curr_t_1);
		}else if (Nk==8){
			inhamm_t8 = inhamm_t7;
			inhamm_t7 = inhamm_t6;
			inhamm_t6 = inhamm_t5;
			inhamm_t5 = inhamm_t4;
			inhamm_t4 = inhamm_t3;
			inhamm_t3 = inhamm_t2;
			inhamm_t2 = inhamm_t1;
			inhamm_t1 = inhamm_t;
			inhamm_t = estimated_hamming_to_individual(sel_t_1, curr_t_1);
		}else if (Nk==9){
			inhamm_t9 = inhamm_t8;
			inhamm_t8 = inhamm_t7;
			inhamm_t7 = inhamm_t6;
			inhamm_t6 = inhamm_t5;
			inhamm_t5 = inhamm_t4;
			inhamm_t4 = inhamm_t3;
			inhamm_t3 = inhamm_t2;
			inhamm_t2 = inhamm_t1;
			inhamm_t1 = inhamm_t;
			inhamm_t = estimated_hamming_to_individual(sel_t_1, curr_t_1);
		}
		
		return (inhamm_t9+inhamm_t8+inhamm_t7+inhamm_t6+inhamm_t5+inhamm_t4+inhamm_t3+inhamm_t2+inhamm_t1+inhamm_t)/10.0;
		
	}
	
	private double estimated_hamming(EVAGChurn[] sel,Individual curr){
		//Frequencies of zeros
		int[] frequencies = new int[Configuration.chromosome_size];
		for(int i=0;i<Configuration.chromosome_size;i++)
			frequencies[i] = 0;
		int count=0;
		double hamm=0;
		for (int i=0; i<sel.length; i++){
			//Computing the hamming distance to a string of ones
			for (int j=0;j<Configuration.chromosome_size;j++){
				if(!((Boolean)sel[i].this_individual().getChr().getGen(j)).booleanValue())
					frequencies[j]++;
			}
			count++;
		}
		
		/*for (int j=0;j<Configuration.chromosome_size;j++){
			if(!((Boolean)curr.getChr().getGen(j)).booleanValue())
				frequencies[j]++;
		}
		count++;*/
		
		
		for (int j=0;j<Configuration.chromosome_size;j++){
			int zeros = frequencies[j];
			int ones = count - frequencies[j];
			hamm += 2* zeros * ones; 				
		}
		hamm /= count *(count-1)*1.0;
		
		
		count=1;
		for (int i=0; i<sel.length; i++){
			if (sel[i].hamm_t!=0){
				hamm += sel[i].hamm_t;
				count++;
			}
			
		}
		
		hamm /= (count*1.0);
		
	
		return hamm;
	}
	
	
	private double estimated_hamming_to_individual(EVAGChurn[] sel,Individual curr){
		//Frequencies of zeros

		int count=0;
		double hamm=0;
		for (int i=0; i<sel.length; i++){
			//Computing the hamming distance to a string of ones
			for (int j=0;j<Configuration.chromosome_size;j++){
				if (((Boolean)sel[i].this_individual().getChr().getGen(j)).booleanValue()!=((Boolean)curr.getChr().getGen(j)).booleanValue())
					hamm++;
			}
			count++;
		}
		

		hamm /=(count*1.0);
		
	
		return hamm;
	}
	
	
	



}
