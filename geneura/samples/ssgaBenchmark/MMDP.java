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
package geneura.samples.ssgaBenchmark;

import geneura.org.Evaluator;
import geneura.org.config.Configuration;
import geneura.org.individuals.Individual;
import binarybench.Benchmark;
import binarybench.Test_func;

public class MMDP extends Evaluator{
Test_func mytest = null;
	
	public MMDP() {
		Benchmark bench = new Benchmark();
		mytest = bench.testFunctionFactory(2, Configuration.chromosome_size, 1);
	}
	
	public void evaluate(Individual ind){
		super.evaluate(ind);
		boolean[] x = ind.getChr().asboolean();
		ind.setFitness(mytest.f(x));
	}

	public double hit_value() {
		return mytest.optimum();
	}

	public Object clone() {
		MMDP pp = new MMDP();
		pp.mytest = (Test_func)mytest.clone();
		return pp;
	}
}