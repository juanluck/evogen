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

import java.util.ArrayList;
import java.util.Iterator;

import geneura.org.Evaluator;
import geneura.org.config.Configuration;
import geneura.org.individuals.GPIndividual;
import geneura.org.statistics.IObservable;

public class NumberEvaluation implements ITermination,IObservable{
	
	protected long _max_evaluation = 100000;
		
	public NumberEvaluation(long max_evaluation) {
		_max_evaluation = max_evaluation;
	}
		

	public boolean isFinish() {
		return Termination.get_n_evaluation() >= _max_evaluation;
	}

	public String getStateAsString() {
			return (isFinish()) ? Termination.NUMBEREVALUATION:"";
	}
	
	
	public Object clone(){
		return new NumberEvaluation(this._max_evaluation);
	}
	

}
