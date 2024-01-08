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
import geneura.org.termination.Termination;

import java.io.File;

public class BisectionTraps {
	static String algorithm="";
	public static void main(String[] args) {
		String problem_name="";
		int problem_size = 12;

		// Cambiar problems{} para escalabilidad en distintas instancias
		int[] problems ={4};//2,4,16,32,64};
		
		LoadProperties lp = new LoadProperties(args);
		Termination.reset();
		Configuration.setConfiguration(lp);
		problem_name = Configuration.evaluation;
		algorithm = (Configuration.Algorithm.equals("GGA"))?"gga":"evag";
		
		for(int i=0;i<problems.length;i++){
			if(problem_name.contains("Trap2"))
				problem_size = problems[i]*2;
			else if (problem_name.contains("Trap3"))
				problem_size = problems[i]*3;
			else if (problem_name.contains("Trap4"))
				problem_size = problems[i]*4;
			
			int population_size = 20000;
			int max_population_size = population_size;
			int min_population_size = population_size;
			long seed = System.currentTimeMillis();

			
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
				
			}
			
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
			
			 if (Configuration.Algorithm.equals("GGA")){
					if (Configuration.representation.equals("binary")){
						FactoryGGA.createBinaryGGA().run();
					}
			}else if (Configuration.Algorithm.equals("EVAG")){
				if (Configuration.representation.equals("binary")){
					if(Configuration.neighborhood.equals("newscast")){
						Algorithm[] a = FactoryEVAG.createBinaryNewscastEVAG();
						try {
							Thread.currentThread().sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						boolean finished = false;
						while (!finished){
							for(int h=0;h<a.length && !finished;h++){
								if(a[h].shouldEnd()){
									finished = true;
									
								}
							}
							if(!finished)
								try {
									Thread.currentThread().sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}

						for(int h=0;h<a.length;h++){
							a[h].kill();
						}
						try {
							Thread.currentThread().sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(int h=0;h<a.length;h++){
							a[h]=null;
						}
						System.gc();
						
					}
					
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
				outputFile = new File(algorithm+"_trap2_"+(problem_size/2)+"_"+population_size+".txt");
			else if (Configuration.evaluation.contains("Trap3"))
				outputFile = new File(algorithm+"_trap3_"+(problem_size/3)+"_"+population_size+".txt");
			else //if (problem_name.contains("Trap4"))
				outputFile = new File(algorithm+"_trap4_"+(problem_size/4)+"_"+population_size+".txt");
			
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
