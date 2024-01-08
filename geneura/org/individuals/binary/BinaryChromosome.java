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
package geneura.org.individuals.binary;

import geneura.org.individuals.IChromosome;
import random.CommonState;

public class BinaryChromosome implements IChromosome {

	private boolean[] gens;

	
	public BinaryChromosome(int bits) {
		gens = new boolean [bits];
		for(int i=0; i<bits; i++){
			gens[i] = CommonState.r.nextBoolean();
		}
	}
	
	

	public int getLength() {
		return gens.length;
	}

	
	public void setGen(int pos, Object gen) {
		gens[pos] = ((Boolean)gen).booleanValue();
	}


	public Object getGen(int pos) {
		return new Boolean(gens[pos]);
	}
	
	public Object clone(){
		BinaryChromosome bin = new BinaryChromosome(this.getLength());
		for (int i=0;i<this.getLength();i++){
			bin.gens[i] = this.gens[i];
		}
		return bin;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof BinaryChromosome) {
			BinaryChromosome chr = (BinaryChromosome) obj;
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



	public boolean[] asboolean() {
		return gens;
	}



	public double[] asdouble() {
		// TODO Auto-generated method stub
		return null;
	}
}
