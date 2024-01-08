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
package geneura.samples.cecBenchmark;

import geneura.org.Evaluator;
import geneura.org.config.Configuration;
import geneura.org.individuals.Individual;
import geneura.samples.cec05.benchmark.benchmark;
import geneura.samples.cec05.benchmark.test_func;


public class WEIERSTRASS extends Evaluator{
    
    
	test_func mytest = null;
    
    public WEIERSTRASS() {
        benchmark bench = new benchmark();
        mytest = bench.testFunctionFactory(11, Configuration.chromosome_size);
    }
    
    public void evaluate(Individual ind){
        super.evaluate(ind);
        double[] x = ind.getChr().asdouble();
        ind.setFitness(mytest.f(x));
    }

    public double hit_value() {
    	return mytest.bias();
    }
    
    public Object clone() {
        WEIERSTRASS pp = new WEIERSTRASS();
        pp.mytest = this.mytest;
        return pp;
    }

}

