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
package geneura.org.termination;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import random.CommonState;

import geneura.org.config.Configuration;
import geneura.org.evoag.EVAGChurn;
import geneura.org.statistics.IObservable;

public class Traces {
	
	int totalindlost;
	int []indlost;
	int []totalind;
	boolean [][]indlife;
	int index=0;
	
	static long n_cycle=0;
	
	public Traces() {
		if(Configuration.tracefile.equals("sinchurn")){
			indlife = new boolean[Configuration.population_size][(int)Configuration.termination_max_cycles+1];
			totalind= new int[(int)Configuration.termination_max_cycles+1];
			for(int j=0;j<=(int)Configuration.termination_max_cycles;j++)
				totalind[j]=Configuration.population_size;
			for(int i=0;i<Configuration.population_size;i++)
				for(int j=0;j<=(int)Configuration.termination_max_cycles;j++)
					indlife[i][j]=true;
		}else
			setup();
	}
	
	public void setup(){
		indlost = new int[(int)Configuration.termination_max_cycles+1];
		totalind = new int[(int)Configuration.termination_max_cycles+1];
		indlife = new boolean[Configuration.population_size][(int)Configuration.termination_max_cycles+1];
			
		String content = getContents(new File(Configuration.tracefile));
		BufferedReader allcontent = new BufferedReader(new StringReader(content));
		String line=null;
		try {
			line = allcontent.readLine();
		} catch (IOException e1) {
		} //not declared within while loop

		int [] values= decodeline(line);
		int maxpcs = values[1];
		double poppcs = (Configuration.population_size*1.0) / maxpcs;
		int currentpcs = maxpcs;
		int pclost = 0;
		
	    try {
			while (( line = allcontent.readLine()) != null && values[0]<Configuration.termination_max_cycles+1){
				totalind[values[0]] = (int)(values[1]*poppcs);
				indlost[values[0]] = (int)(pclost*poppcs);
				totalindlost += (int)(pclost*poppcs);
				values= decodeline(line);
				pclost = currentpcs - values[1];
				currentpcs = currentpcs-pclost;
			 }
		} catch (IOException e) {}
		
		try {
			allcontent.close();
		} catch (IOException e) {
		}
		
		//Initializing
		for (int i=0;i<Configuration.population_size;i++)
			for (int j=0;j<Configuration.termination_max_cycles+1;j++)
				indlife[i][j]=true;

		
		//Determining the individual living cycle
		
		for (int i=1;i<Configuration.termination_max_cycles+1;i++){
			int numberoflost = indlost[i];

			//Some individuals come back to life!
			while(numberoflost<0){
				int zombies=0;
				for(int j=0;j<Configuration.population_size;j++){
					if(!indlife[j][i-1])
						zombies++;
				}
				int relativeindex = CommonState.r.nextInt(zombies);
				
				boolean found = false;
				for(int j=0;j<Configuration.population_size && !found;j++){
					if(!indlife[j][i-1] && relativeindex>0)
						relativeindex--;
					else if (!indlife[j][i-1] && relativeindex==0){
						for(int h=i;h<Configuration.termination_max_cycles+1;h++)
							indlife[j][h]=true;
						found=true;
					}
				}
				numberoflost++;
			}
			
			//Some individuals die
			//System.out.println("L: "+numberoflost);
			while(numberoflost>0){
				int alive=0;
				for(int j=0;j<Configuration.population_size;j++){
					if(indlife[j][i-1])
						alive++;
				}
				int relativeindex = CommonState.r.nextInt(alive);
				
				boolean found=false;
				for(int j=0;j<Configuration.population_size && !found;j++){
					if(indlife[j][i-1] && relativeindex>0)
						relativeindex--;
					else if (indlife[j][i-1] && relativeindex==0){
						for(int h=i;h<Configuration.termination_max_cycles+1;h++)
							indlife[j][h]=false;
						found=true;
					}
				}
				numberoflost--;
			}
			int countingalive=0;
			for(int j=0;j<Configuration.population_size;j++){
				if(indlife[j][i])
					countingalive++;
			}
			//System.out.println("A: "+countingalive);
								
				
		}
		
	}
	
	private int[] decodeline(String str){
		
		int [] numbers = new int[2];
		
		numbers[0] = Integer.parseInt(str.substring(0, str.indexOf(" ")).trim());// for the generation
		numbers[1] = Integer.parseInt(str.substring(str.indexOf(" ")).trim());// for the number of pcs
		return numbers;
	}
		
	public boolean[] get(){
		
		boolean[] livingcycle = indlife[index];
//		System.out.println(" i: "+index +" g: "+dyingat);
		index++;
		return livingcycle;
		//return dyingat;
	}
	
	public int popatgeneration (int generation){
		return totalind[generation];
	}
	
	
	private String getContents(File aFile) {
	    //...checks on aFile are elided
	    StringBuffer contents = new StringBuffer();

	    //declared here only to make visible to finally clause
	    BufferedReader input = null;
	    try {
	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      input = new BufferedReader( new FileReader(aFile) );
	      String line = null; //not declared within while loop
	      /*
	      * readLine is a bit quirky :
	      * it returns the content of a line MINUS the newline.
	      * it returns null only for the END of the stream.
	      * it returns an empty String if two newlines appear in a row.
	      */
	      while (( line = input.readLine()) != null){
	        contents.append(line);
	        contents.append(System.getProperty("line.separator"));
	      }
	    }
	    catch (FileNotFoundException ex) {
	      ex.printStackTrace();
	    }
	    catch (IOException ex){
	      ex.printStackTrace();
	    }
	    finally {
	      try {
	        if (input!= null) {
	          //flush and close both "input" and its underlying FileReader
	          input.close();
	        }
	      }
	      catch (IOException ex) {
	        ex.printStackTrace();
	      }
	    }
	    return contents.toString();
	  }



}
