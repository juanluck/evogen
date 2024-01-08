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
package geneura.org.statistics;

import geneura.org.Evaluator;
import geneura.org.config.Configuration;
import geneura.org.config.Logger;
import geneura.org.termination.ITermination;
import geneura.org.termination.Termination;

import java.util.ArrayList;
import java.util.Iterator;

public class Observer {

	protected ArrayList<IObservable> _observable;
	protected ArrayList<Evaluator> _eval;
	protected ArrayList<ITermination> _termination;
	protected static long _time;
	protected long _counttime;
	protected long _sample;
	protected long _prints = 1;
	protected boolean _finish = false;
	
	
	public Observer(long sample) {
		_observable = new ArrayList<IObservable>();
		_termination = new ArrayList<ITermination>();
		_time = System.currentTimeMillis();
		_sample = sample;
	}
	
	public static void restart(){
		_time = System.currentTimeMillis();
		Termination.reset();
	}
	
	public synchronized void Update(){
 
		if (!_finish && shouldprint()){
			_counttime = System.currentTimeMillis() - _time;
			String output = ""+_counttime+" ";
			
			for (Iterator<IObservable> it =_observable.iterator();it.hasNext();){
				output += it.next().getStateAsString()+" ";
			}
			output += "\n";
			if (Configuration.logfile.equals(""))
				System.out.print(output);
			else{
				Logger.append(Configuration.logfile, output);
			}
		}
	}
	
	
	public void registerObservable(IObservable obs){
		_observable.add(obs);
		if (obs instanceof ITermination) {
			_termination.add((ITermination)obs);
		}
		
	}
	
	private boolean shouldprint(){
		boolean print = false;
		//System.out.println("Imprime: "+((_sample* _prints))+" reales "+Termination.get_n_evaluation());
		 
		if (Termination.get_n_evaluation() >=  _sample * _prints){
			print = true;
			_prints++;
			
		}else if(Configuration.termination_max_cycles != 1000){
			if(Termination.get_n_generation() > _prints ){
				_prints++;
				print=true;
			}
		}
		
		boolean finish = false;
		for(Iterator<ITermination> ev=_termination.iterator();ev.hasNext() && !finish;){
			finish = ev.next().isFinish();
		}
		_finish = finish;
		return (finish || print);
	}
}
