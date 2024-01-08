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

import java.util.Iterator;

import geneura.org.Algorithm;
import geneura.org.statistics.Observer;

public class Termination{
	
	public static final String NOTIMPROVED="NotImproved";
	public static String VALUERICHED="SUCCESS";
	public static String NUMBEREVALUATION="MAXEVALUATION";
	public static final String NUMBERGENERATION="MAXGENERATION";
	
	protected ITermination[] _termination;
	protected static long _n_evaluation = 0;
	protected static long _n_generation = 0;
	protected static long _n_cycles = 0;
	protected Observer observer;
	protected static boolean kill_exp = false;
		
	public Termination(ITermination[] termination){
		_termination = termination;
	}
	
	public boolean isFinish(){
		boolean finish = false;
		for (int i=0;i<_termination.length && !finish;i++)
			finish = _termination[i].isFinish();
		if (finish)
			observer.Update();
		if(kill_exp)
			finish = true;
		return finish;
	}
	
	public void attach(Observer obs){
		observer = obs;
	}
	
	public void dettach(Observer obs){
		observer = null;
	}
	
	public static synchronized void increment_evaluation() {
		_n_evaluation++;
	}
	

	public static synchronized long get_n_evaluation(){
		return _n_evaluation;
	}	

	public static synchronized void increment_generation() {
		_n_cycles++;
	}
	

	public static synchronized long get_n_generation(){
		return _n_cycles;
	}	

	
	public Object clone() {
		Termination term = new Termination(null);
		
		term._termination = new ITermination[_termination.length];
		for(int i=0; i< _termination.length; i++)
			term._termination[i] = (ITermination) _termination[i].clone();
		term.observer = this.observer;
		
		return term;
	}
	
	public static void kill(){
		kill_exp = true;
	}
	public static void reset(){
		_n_evaluation = 0;
		_n_generation = 0;
		_n_cycles = 0;
		Churn.n_cycle = 0;
	}
	
}
