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
package geneura.org.individuals;



public abstract class Individual implements IComparator{
	protected IChromosome chr;
	public static boolean _min = true;
	
	protected double fitness;
	
	public Individual() {
	}

	public IChromosome getChr() {
		return chr;
	}

	public void setChr(IChromosome chr) {
		this.chr = chr;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public abstract Object clone();
	
	public boolean better(Individual ind) {
		return (_min) ? ind.fitness > this.fitness: ind.fitness < this.fitness;
	}
	
	public boolean worst(Individual ind) {
		return (_min) ? ind.fitness < this.fitness: ind.fitness > this.fitness;
	}
	
	public boolean value_reached(double value){
		return (_min) ? this.fitness <= value:  this.fitness >= value;
	}
	
	public boolean equals(Object obj) {
	if (obj instanceof Individual) {
		Individual ind = (Individual) obj;
		if(ind.fitness == this.fitness)
			if (!ind.chr.equals(this.chr)) return false;
		else return false;
	}else return false;
	return true;
	}
}
