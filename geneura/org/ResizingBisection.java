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

import geneura.org.config.Configuration;
import geneura.org.config.LoadProperties;
import geneura.org.config.Logger;
import geneura.org.factories.FactoryEVAG;
import geneura.org.factories.FactoryGGA;
import geneura.org.factories.FactoryGGAResized;
import geneura.org.factories.FactorySSGA;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.demes.FixedPopulation;
import geneura.org.termination.Termination;

import java.io.File;

public class ResizingBisection {
	static String algorithm="";
	
	public static Individual[][] pop;//
	public static Individual[][] popaux;//
	public static int[] n_gen;
	public static int[] n_genaux;
	public static int counter_exp = 0;
	public static int counter_ind = 0;
	public static boolean bisection = false;
	static int population_size = 80;
	static int max_population_size;
	static int min_population_size;
	static long seed;
	static String problem_name="";

	
	
	public static void main(String[] args) {
		FixedPopulation.control_resizing = false;
		bisection = true;
		
		int problem_size = 12;

		//TODO: Descomentar , comentado solo para el PPEAKS
		int[] problems ={64};
		
		LoadProperties lp = new LoadProperties(args);
		Termination.reset();
		Configuration.setConfiguration(lp);
		problem_name = Configuration.evaluation;
		algorithm = (Configuration.Algorithm.equals("GGA"))?"gga":"ssga";
		
		for(int i=0;i<problems.length;i++){
			bisection = true;
			if(problem_name.contains("Trap2"))
				problem_size = problems[i]*2;
			else if (problem_name.contains("Trap3"))
				problem_size = problems[i]*3;
			else if (problem_name.contains("Trap4"))
				problem_size = problems[i]*4;
			
			population_size = 80;
			max_population_size = population_size;
			min_population_size = population_size;
			seed = System.currentTimeMillis();

			
			String[] parameters = new String[args.length + 4];
			System.arraycopy(args, 0, parameters, 0, args.length);
			
			parameters[parameters.length-1] = "seed="+seed;
			parameters[parameters.length-2] = "populationsize="+population_size;
			parameters[parameters.length-3] = "chromosomesize="+problem_size;
			if (problem_name.contains("Trap2"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap2_"+(problem_size/2)+"_"+population_size+".txt";
			else if (problem_name.contains("Trap3"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap3_"+(problem_size/3)+"_"+population_size+".txt";
			else if (problem_name.contains("Trap4"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap4_"+(problem_size/4)+"_"+population_size+".txt";
			
			bisection_method(args, parameters, problem_size);
			FixedPopulation.control_resizing = true;
			optimize_bisection( args, parameters, problem_size);
			FixedPopulation.control_resizing = false;
			bisection = false;
			loop_tau_rho(args, problem_size);
			
		}
			
	}
	
	public static void loop_tau_rho(String[] args, int problem_size){
		
		String[] parameters = new String[args.length + 6];
		System.arraycopy(args, 0, parameters, 0, args.length);

		for(double tau=0.25;tau<64;tau*=1.5){
			for(double rho=0.25;rho<1;rho+=0.05){
				
				
				parameters[parameters.length-1] = "seed="+seed;
				parameters[parameters.length-2] = "populationsize="+population_size;
				parameters[parameters.length-3] = "chromosomesize="+problem_size;
				parameters[parameters.length-4] = "rho="+rho;
				parameters[parameters.length-5] = "tau="+tau;
				
				if (problem_name.contains("Trap2"))
					parameters[parameters.length-6] = "logfile=r"+algorithm+"_trap2_"+(problem_size/2)+"_"+rho+"_"+tau+"_"+population_size+".txt";
				else if (problem_name.contains("Trap3"))
					parameters[parameters.length-6] = "logfile=r"+algorithm+"_trap3_"+(problem_size/3)+"_"+rho+"_"+tau+"_"+population_size+".txt";
				else if (problem_name.contains("Trap4"))
					parameters[parameters.length-6] = "logfile=r"+algorithm+"_trap4_"+(problem_size/4)+"_"+rho+"_"+tau+"_"+population_size+".txt";
				
				if(!reliable_resizing(args,parameters,population_size,problem_size,tau,rho)){
					File outputFile;
					if (Configuration.evaluation.contains("Trap2"))
						outputFile = new File("r"+algorithm+"_trap2_"+(problem_size/2)+"_"+rho+"_"+tau+"_"+population_size+".txt");
					else if (Configuration.evaluation.contains("Trap3"))
						outputFile = new File("r"+algorithm+"_trap3_"+(problem_size/3)+"_"+rho+"_"+tau+"_"+population_size+".txt");
					else //if (problem_name.contains("Trap4"))
						outputFile = new File("r"+algorithm+"_trap4_"+(problem_size/4)+"_"+rho+"_"+tau+"_"+population_size+".txt");
					
					outputFile.delete();
				}else{
					rho=1;
				}
			}
		}
	}

	
	public static void optimize_bisection(String[] args, String [] parameters, int problem_size){

		int first_population = population_size;
		double initial_perc = 0.98;
		int new_population = (int) (first_population * initial_perc);
		
		counter_exp=0;
		counter_ind=0;
		
		System.arraycopy(args, 0, parameters, 0, args.length);
		parameters[parameters.length-1] = "seed="+System.currentTimeMillis();
		parameters[parameters.length-2] = "populationsize="+new_population;
		parameters[parameters.length-3] = "chromosomesize="+problem_size;
		
		if (problem_name.contains("Trap2"))
			parameters[parameters.length-4] = "logfile="+algorithm+"_trap2_"+(problem_size/2)+"_"+new_population+".txt";
		else if (problem_name.contains("Trap3"))
			parameters[parameters.length-4] = "logfile="+algorithm+"_trap3_"+(problem_size/3)+"_"+new_population+".txt";
		else if (problem_name.contains("Trap4"))
			parameters[parameters.length-4] = "logfile="+algorithm+"_trap4_"+(problem_size/4)+"_"+new_population+".txt";
		
		while (reliable_success(args,parameters,new_population,problem_size)){
			File outputFile;
			
			if (Configuration.evaluation.contains("Trap2"))
				outputFile = new File(algorithm+"_trap2_"+(problem_size/2)+"_"+population_size+".txt");
			else if (Configuration.evaluation.contains("Trap3"))
				outputFile = new File(algorithm+"_trap3_"+(problem_size/3)+"_"+population_size+".txt");
			else //if (problem_name.contains("Trap4"))
				outputFile = new File(algorithm+"_trap4_"+(problem_size/4)+"_"+population_size+".txt");
			
			outputFile.delete();
			
						
			initial_perc -= 0.02;
			population_size = new_population;
			new_population = (int) (first_population * initial_perc);
			
			System.arraycopy(args, 0, parameters, 0, args.length);
			parameters[parameters.length-1] = "seed="+System.currentTimeMillis();
			parameters[parameters.length-2] = "populationsize="+new_population;
			parameters[parameters.length-3] = "chromosomesize="+problem_size;
			
			if (problem_name.contains("Trap2"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap2_"+(problem_size/2)+"_"+new_population+".txt";
			else if (problem_name.contains("Trap3"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap3_"+(problem_size/3)+"_"+new_population+".txt";
			else if (problem_name.contains("Trap4"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap4_"+(problem_size/4)+"_"+new_population+".txt";
			
			counter_exp=0;
			counter_ind=0;
		}
		
	}
	
	public static void bisection_method(String[] args, String [] parameters, int problem_size){
		
		
		counter_exp=0;
		counter_ind=0;
		while (!reliable_success(args,parameters,population_size,problem_size)){
			min_population_size = population_size;
			population_size *= 2;
			//if (population_size>328000)
			//	System.exit(-1);
			System.arraycopy(args, 0, parameters, 0, args.length);
			parameters[parameters.length-1] = "seed="+System.currentTimeMillis();
			parameters[parameters.length-2] = "populationsize="+population_size;
			parameters[parameters.length-3] = "chromosomesize="+problem_size;
			
			if (problem_name.contains("Trap2"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap2_"+(problem_size/2)+"_"+population_size+".txt";
			else if (problem_name.contains("Trap3"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap3_"+(problem_size/3)+"_"+population_size+".txt";
			else if (problem_name.contains("Trap4"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap4_"+(problem_size/4)+"_"+population_size+".txt";
			
			counter_exp=0;
			counter_ind=0;
		}
		
		int oldsize=population_size;
		max_population_size = population_size;
		population_size = (max_population_size+min_population_size)/2;
		System.arraycopy(args, 0, parameters, 0, args.length);
		parameters[parameters.length-1] = "seed="+System.currentTimeMillis();
		parameters[parameters.length-2] = "populationsize="+population_size;
		parameters[parameters.length-3] = "chromosomesize="+problem_size;
		if (problem_name.contains("Trap2"))
			parameters[parameters.length-4] = "logfile="+algorithm+"_trap2_"+(problem_size/2)+"_"+population_size+".txt";
		else if (problem_name.contains("Trap3"))
			parameters[parameters.length-4] = "logfile="+algorithm+"_trap3_"+(problem_size/3)+"_"+population_size+".txt";
		else if (problem_name.contains("Trap4"))
			parameters[parameters.length-4] = "logfile="+algorithm+"_trap4_"+(problem_size/4)+"_"+population_size+".txt";
		
		
		while((max_population_size-min_population_size)/(min_population_size*1.0) > (1/16.0) ){ // 1/8
			if (reliable_success(args,parameters,population_size,problem_size)){
				File outputFile;
				if (problem_name.contains("Trap2"))
					outputFile = new File(algorithm+"_trap2_"+(problem_size/2)+"_"+max_population_size+".txt");
				else if (problem_name.contains("Trap3"))
					outputFile = new File(algorithm+"_trap3_"+(problem_size/3)+"_"+max_population_size+".txt");
				else //if (problem_name.contains("Trap4"))
					outputFile = new File(algorithm+"_trap4_"+(problem_size/4)+"_"+max_population_size+".txt");
				
				outputFile.delete();
				max_population_size = population_size;
				oldsize = population_size;
			}else{
				min_population_size = population_size;
			}
			
			population_size = (max_population_size+min_population_size)/2;
			System.arraycopy(args, 0, parameters, 0, args.length);
			parameters[parameters.length-1] = "seed="+System.currentTimeMillis();
			parameters[parameters.length-2] = "populationsize="+population_size;
			parameters[parameters.length-3] = "chromosomesize="+problem_size;
			if (problem_name.contains("Trap2"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap2_"+(problem_size/2)+"_"+population_size+".txt";
			else if (problem_name.contains("Trap3"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap3_"+(problem_size/3)+"_"+population_size+".txt";
			else if (problem_name.contains("Trap4"))
				parameters[parameters.length-4] = "logfile="+algorithm+"_trap4_"+(problem_size/4)+"_"+population_size+".txt";
		}

		population_size = oldsize;

	}
	
	
	public static boolean reliable_resizing(String[] args, String[] parameters, int population_size, int problem_size, double tau, double rho){
		boolean reliable = true;
		int count_fails = 0;

		counter_exp = 0;
		counter_ind = 0;
			
		for(int i=0;i<50 && reliable;i++){
			counter_exp = i;
			counter_ind = 0;
			System.arraycopy(args, 0, parameters, 0, args.length);
			parameters[parameters.length-1] = "seed="+System.currentTimeMillis();
			
			LoadProperties lp = new LoadProperties(parameters);
			Termination.reset();
			Configuration.setConfiguration(lp);
			
			 if (Configuration.Algorithm.equals("GGA")){
				if (Configuration.representation.equals("binary")){
					FactoryGGAResized.createBinaryGGA().run();
				}
			}else if (Configuration.Algorithm.equals("SSGA")){
				if (Configuration.representation.equals("binary")){
					FactorySSGA.createBinarySSGA().run();
				}
			}
			
			if (!success())
				count_fails++;
			
			if (count_fails == 2)
				reliable = false;
		}
		
		if (!reliable){
			File outputFile;
			if (Configuration.evaluation.contains("Trap2"))
				outputFile = new File("r"+algorithm+"_trap2_"+(problem_size/2)+"_"+rho+"_"+tau+"_"+population_size+".txt");
			else if (Configuration.evaluation.contains("Trap3"))
				outputFile = new File("r"+algorithm+"_trap3_"+(problem_size/3)+"_"+rho+"_"+tau+"_"+population_size+".txt");
			else //if (problem_name.contains("Trap4"))
				outputFile = new File("r"+algorithm+"_trap4_"+(problem_size/4)+"_"+rho+"_"+tau+"_"+population_size+".txt");
			
			outputFile.delete();
		}
		
		return reliable;
	}
	
	
	/**
	 * 
	 * @return true if the success rate is of 98%. false otherwise
	 */
	public static boolean reliable_success(String[] args, String[] parameters, int population_size, int problem_size){
		boolean reliable = true;
		int count_fails = 0;
		popaux = null;
		n_genaux = null;
		popaux=new Individual[50][];
		n_genaux = new int[50];
			
			
		for(int i=0;i<50 && reliable;i++){
			popaux[i] = new Individual[population_size];
			counter_exp = i;
			counter_ind = 0;
			System.arraycopy(args, 0, parameters, 0, args.length);
			parameters[parameters.length-1] = "seed="+System.currentTimeMillis();
			
			LoadProperties lp = new LoadProperties(parameters);
			Termination.reset();
			Configuration.setConfiguration(lp);
			
			 if (Configuration.Algorithm.equals("GGA")){
				if (Configuration.representation.equals("binary")){
					FactoryGGA.createBinaryGGA().run();
				}
			}else if (Configuration.Algorithm.equals("SSGA")){
				if (Configuration.representation.equals("binary")){
					FactorySSGA.createBinarySSGA().run();
				}
			}
			
			if (!success())
				count_fails++;
			
			if (count_fails == 2)
				reliable = false;
		}
		
		if (!reliable){
			File outputFile;
			popaux=null;
            n_genaux=null;
            
			if (Configuration.evaluation.contains("Trap2"))
				outputFile = new File(algorithm+"_trap2_"+(problem_size/2)+"_"+population_size+".txt");
			else if (Configuration.evaluation.contains("Trap3"))
				outputFile = new File(algorithm+"_trap3_"+(problem_size/3)+"_"+population_size+".txt");
			else //if (problem_name.contains("Trap4"))
				outputFile = new File(algorithm+"_trap4_"+(problem_size/4)+"_"+population_size+".txt");
			
			outputFile.delete();
		}else{
			pop = null;
			pop = popaux;
			n_gen = null;
			n_gen = n_genaux;
		}
		
		return reliable;
	}
	/**
	 * 
	 * @return true if the run succeed. false otherwise
	 */
	public static boolean success(){
		
		String log = Logger.getContents(new File(Configuration.logfile));
		
		int index = log.lastIndexOf(Termination.VALUERICHED);
		index = (index == -1) ? 0 : index;
		
		return (	log.indexOf(Termination.NOTIMPROVED,index) == -1 
					&& 	log.indexOf(Termination.NUMBEREVALUATION,index) == -1) 
					? true:false;
				

	}


}
