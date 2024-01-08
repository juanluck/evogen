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

import geneura.org.ResizingBisection;
import geneura.org.config.Configuration;
import geneura.org.individuals.GPIndividual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.statistics.IObservable;

public class ValueReached implements ITermination,IObservable{

	protected double _value;
	protected INeighborhood _neighborhood;
	
	public ValueReached(double value, INeighborhood neighborhood) {
		_value = value;
		_neighborhood = neighborhood;
	}

	public boolean isFinish() {
		if (Configuration.termination_hit_value_sleep){
			if (_neighborhood.getIndividual(_neighborhood.getBest()).value_reached(_value))
				Termination.NUMBEREVALUATION =Termination.VALUERICHED;
			return false;
		}
		
		if (ResizingBisection.bisection && _neighborhood.getIndividual(_neighborhood.getBest()).value_reached(_value)){
			ResizingBisection.n_genaux[ResizingBisection.counter_exp] = (int)Termination.get_n_generation();
		}
		return _neighborhood.getIndividual(_neighborhood.getBest()).value_reached(_value);
	}

	public String getStateAsString() {
		if (!Configuration.termination_hit_value_sleep){
			if(_neighborhood.getIndividual(_neighborhood.getBest()).value_reached(_value)){
				if (!Configuration.Algorithm.equals("EVAGseqGP"))
					return Termination.VALUERICHED;
				else if (Configuration.gp_print_best){
					GPIndividual ind = (GPIndividual)_neighborhood.getIndividual(_neighborhood.getBest());
					return Termination.VALUERICHED+"\n"+ind.getTreeGenome().getRootNode().toString();
				}
				else
					return Termination.VALUERICHED;
			}		
			else
				return "";	
		}
		return "";
	}

	public Object clone(){
		return new ValueReached(_value,_neighborhood);
	}
}
