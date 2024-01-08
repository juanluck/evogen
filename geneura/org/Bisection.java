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
import geneura.org.factories.FactoryEVAGseq;
import geneura.org.factories.FactoryEVAGseqChurn;
import geneura.org.factories.FactoryEVAGseqGP;
import geneura.org.factories.FactoryEVAGseqUMDA;
import geneura.org.factories.FactoryGGA;
import geneura.org.factories.FactorySSGA;
import geneura.org.factories.FactorycGA;
import geneura.org.termination.Termination;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Bisection {
	
	static String problem_name="";
	static String algorithm="";
	static String neighbourhood;
	static int population_size = 250; //initial population size
	
	public static void main(String[] args) {
		
		int[] problems ={54};	
				
		LoadProperties lp = new LoadProperties(args);
		Termination.reset();
		Configuration.setConfiguration(lp);
		problem_name = Configuration.evaluation.substring(Configuration.evaluation.lastIndexOf(".")+1); 
		algorithm = Configuration.Algorithm;	
		if(Configuration.neighborhood.equals("wattsstrogatz")){
			neighbourhood = Configuration.neighborhood+"-"+Configuration.topology_rewiring;
		}else{
			neighbourhood = Configuration.neighborhood;
		}
	
		for(int i=0;i<problems.length;i++){
			//population_size = 40;
			int max_population_size = population_size;
			int min_population_size = population_size;
			long seed = System.currentTimeMillis();
			String[] parameters;
			
			parameters = new String[args.length + 4];
			
			System.arraycopy(args, 0, parameters, 0, args.length);
			
			parameters[parameters.length-1] = "seed="+seed;
			parameters[parameters.length-2] = "populationsize="+population_size;
			parameters[parameters.length-3] = "chromosomesize="+problems[i];

			if(Configuration.churn)
				parameters[parameters.length-4] = "logfile="+algorithm+"_"+neighbourhood+"_"+problem_name+"_"+(int)Configuration.weibull_lambda+"_"+problems[i]+"_"+population_size+".txt";
			else
				parameters[parameters.length-4] = "logfile="+algorithm+"_"+neighbourhood+"_"+problem_name+"_"+problems[i]+"_"+population_size+".txt";
			
			while (!reliable_success(args,parameters,population_size,problems[i])){
				min_population_size = population_size;
				population_size *= 2;
				if (population_size>5120) //The maximum considered population size
					System.exit(-1);
				System.arraycopy(args, 0, parameters, 0, args.length);
				parameters[parameters.length-1] = "seed="+System.currentTimeMillis();
				parameters[parameters.length-2] = "populationsize="+population_size;
				parameters[parameters.length-3] = "chromosomesize="+problems[i];
				if(Configuration.churn)
					parameters[parameters.length-4] = "logfile="+algorithm+"_"+neighbourhood+"_"+problem_name+"_"+(int)Configuration.weibull_lambda+"_"+problems[i]+"_"+population_size+".txt";
				else
					parameters[parameters.length-4] = "logfile="+algorithm+"_"+neighbourhood+"_"+problem_name+"_"+problems[i]+"_"+population_size+".txt";

				
			}
			
			max_population_size = population_size;
			population_size = (max_population_size+min_population_size)/2;
			System.arraycopy(args, 0, parameters, 0, args.length);
			parameters[parameters.length-1] = "seed="+System.currentTimeMillis();
			parameters[parameters.length-2] = "populationsize="+population_size;
			parameters[parameters.length-3] = "chromosomesize="+problems[i];
			if(Configuration.churn)
				parameters[parameters.length-4] = "logfile="+algorithm+"_"+neighbourhood+"_"+problem_name+"_"+(int)Configuration.weibull_lambda+"_"+problems[i]+"_"+population_size+".txt";
			else
				parameters[parameters.length-4] = "logfile="+algorithm+"_"+neighbourhood+"_"+problem_name+"_"+problems[i]+"_"+population_size+".txt";

			
			
			while((max_population_size-min_population_size)/(min_population_size*1.0) > (1/8.0) ){ // 1/8
				if (reliable_success(args,parameters,population_size,problems[i])){
					File outputFile;
					if(Configuration.churn)
						outputFile = new File(algorithm+"_"+neighbourhood+"_"+problem_name+"_"+(int)Configuration.weibull_lambda+"_"+problems[i]+"_"+max_population_size+".txt");
					else
						outputFile = new File(algorithm+"_"+neighbourhood+"_"+problem_name+"_"+problems[i]+"_"+max_population_size+".txt");
					outputFile.delete();
					max_population_size = population_size;
				}else{
					min_population_size = population_size;
				}
				
				population_size = (max_population_size+min_population_size)/2;
				System.arraycopy(args, 0, parameters, 0, args.length);
				parameters[parameters.length-1] = "seed="+System.currentTimeMillis();
				parameters[parameters.length-2] = "populationsize="+population_size;
				parameters[parameters.length-3] = "chromosomesize="+problems[i];
				if(Configuration.churn)
					parameters[parameters.length-4] = "logfile="+algorithm+"_"+neighbourhood+"_"+problem_name+"_"+(int)Configuration.weibull_lambda+"_"+problems[i]+"_"+population_size+".txt";
				else
					parameters[parameters.length-4] = "logfile="+algorithm+"_"+neighbourhood+"_"+problem_name+"_"+problems[i]+"_"+population_size+".txt";
				

			
			}

		}
	}
	
	/**
	 * 
	 * @return true if the success rate is of 98%. false otherwise
	 */
	public static boolean reliable_success(String[] args, String[] parameters, int population_size, int problem_size){
		boolean reliable = true;
		int count_fails = 0;
		
		for(int i=0;i<50 && reliable;i++){
			System.arraycopy(args, 0, parameters, 0, args.length);
			parameters[parameters.length-1] = "seed="+System.currentTimeMillis();
			
			LoadProperties lp = new LoadProperties(parameters);
			Termination.reset();
			Configuration.setConfiguration(lp);
			
			 if (Configuration.Algorithm.equals("GGA"))
					if (Configuration.representation.equals("binary")){
						FactoryGGA.createBinaryGGA().run();
					}
			 if (Configuration.Algorithm.equals("EVAGseq")){
					if (Configuration.representation.equals("binary")){
						if(Configuration.neighborhood.equals("newscast")){
								FactoryEVAGseq.createNewscastEVAGseq().run();								
						} else if(Configuration.neighborhood.equals("wattsstrogatz")){
							FactoryEVAGseq.createWattsStrogatzEVAGseq().run();
						}
					}
			 }

			 if (Configuration.Algorithm.equals("EVAGseqChurn")){
					if (Configuration.representation.equals("binary")){
						if(Configuration.neighborhood.equals("newscast")){
								FactoryEVAGseqChurn.createNewscastEVAGseq().run();
						} 
					}
			 }
			 
			 if (Configuration.Algorithm.equals("EVAGseqUMDA")){
					if (Configuration.representation.equals("binary")){
						if(Configuration.neighborhood.equals("newscast")){
								FactoryEVAGseqUMDA.createNewscastEVAGseqUMDA().run();
						} 
					}
			 }

			 if (Configuration.Algorithm.equals("cGA")){
					if (Configuration.representation.equals("binary")){
								FactorycGA.createBinarycGA().run();
					}
			 }
			 
			 if(Configuration.Algorithm.equals("EVAGseqGP")){
					if(Configuration.neighborhood.equals("newscast")){
						FactoryEVAGseqGP.createNewscastEVAGseqGP().run();
					}
				}
			 
			 if (Configuration.Algorithm.equals("SSGA"))
					if (Configuration.representation.equals("binary")){
						FactorySSGA.createBinarySSGA().run();
					}
			
			if (!success())
				count_fails++;
			
			if (count_fails == 2)
				reliable = false;
		}
		
		if (!reliable){
			File outputFile;
			if(Configuration.churn)
				outputFile = new File(algorithm+"_"+neighbourhood+"_"+problem_name+"_"+(int)Configuration.weibull_lambda+"_"+problem_size+"_"+population_size+".txt");
			else
				outputFile = new File(algorithm+"_"+neighbourhood+"_"+problem_name+"_"+problem_size+"_"+population_size+".txt");
 
			// in case that the a population size of 2560 individuals does not solve the problem, we keep the file 
			// and stop computing
			if (population_size<5120) 
				outputFile.delete();
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
		
		return (	log.indexOf(Termination.NOTIMPROVED,index) == -1 
					&& 	log.indexOf(Termination.NUMBEREVALUATION,index) == -1) 
					? true:false;
				

	}

}
