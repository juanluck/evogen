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
package geneura.org.individuals.real;

import random.CommonState;
import geneura.org.config.Configuration;
import geneura.org.individuals.IChromosome;

public class DoubleChromosome implements IChromosome{

	private double[] gens;
	
	public DoubleChromosome(int size) {
		gens = new double [size];
		for(int i=0; i<size; i++){
			gens[i] = (CommonState.r.nextDouble()*(Configuration.range_max-Configuration.range_min))+Configuration.range_min;
		}
	}
	
	public boolean[] asboolean() {
		// TODO Auto-generated method stub
		return null;
	}

	public double[] asdouble() {
		return gens;
	}

	public Object getGen(int pos) {
		return new Double(gens[pos]);
	}

	public int getLength() {
		return gens.length;
	}

	public void setGen(int pos, Object gen) {
		gens[pos] = ((Double)gen).doubleValue();
	}
	
	public Object clone(){
		DoubleChromosome ge = new DoubleChromosome(this.getLength());
		for (int i=0;i<this.getLength();i++){
			ge.gens[i] = this.gens[i];
		}
		return ge;
	}
	
	public boolean equals(Object obj){
		if (obj instanceof DoubleChromosome) {
			DoubleChromosome chr = (DoubleChromosome) obj;
			if(chr.getLength() == this.getLength()){
				for(int i=0;i<this.getLength();i++)
					if(chr.gens[i] != this.gens[i])
					 return false;
	
			}else{
				return false;
			}
			
		}else{
			return false;
		}
		return true;

	}

	@Override
	public String toString() {
		String output=super.toString()+"\n";
		for(int i=0;i<this.getLength();i++)
			output+= " "+gens[i];
		return output;
	}
}
