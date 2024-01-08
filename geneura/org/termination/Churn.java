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

import geneura.org.config.Configuration;
import geneura.org.evoag.EVAGChurn;
import geneura.org.statistics.IObservable;

public class Churn implements IObservable,ITermination {

	protected EVAGChurn[] _evags;
	
	static long n_cycle=0;
	
	public Churn(EVAGChurn[] evags) {
		_evags = evags;
	}
	
	public String getStateAsString() {
		int count_alive = 0;
		long count_cycle = -1;
		for (int i=0;i<_evags.length;i++){
			if (!_evags[i].failure){
				count_alive++;
				if(_evags[i].get_local_cycle()>count_cycle || count_cycle == -1)
					count_cycle = _evags[i].get_local_cycle();
			}
		}
		
		
		
		if (20>count_alive || Configuration.termination_max_cycles <= count_cycle)
			return ""+count_cycle+" "+count_alive+" "+Termination.NUMBEREVALUATION;
		else
			return ""+count_cycle+" "+count_alive;
	}

	public boolean isFinish() {

		int count_alive = 0;
		long count_cycle = -1;
		for (int i=0;i<_evags.length;i++){
			if (!_evags[i].failure){
				count_alive++;
				if(_evags[i].get_local_cycle()>count_cycle || count_cycle == -1)
					count_cycle = _evags[i].get_local_cycle();
			}
		}
		
		if (Configuration.termination_max_cycles <= count_cycle)
			return true;
		else if (20>count_alive) // Just 20 individuals left
			return true;
		else
			return false;		
		
	}
	
	public Object clone(){
		return new Churn(this._evags);
	}


}
