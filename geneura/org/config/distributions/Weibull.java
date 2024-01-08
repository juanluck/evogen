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
package geneura.org.config.distributions;

import java.util.Random;

import random.CommonState;

public class Weibull {
	public static double get(double lambda, double k){
		
		double X=0;
		
		double U = Math.random();

		while (U == 0){
			U = Math.random();
		}
		
		
		return lambda* Math.pow((-Math.log(U)),1/k);
	}
	
	public static double distro(double lambda, double k){
		double U = Math.random();

		while (U == 0){
			U = Math.random();
		}
		
		return Math.pow((-1.0/lambda)* Math.log(U), 1.0/k);
			
		
	}
	
	public static double gaussian(double mu, double sigma){
		
		double Z = CommonState.r.nextGaussian();
		
		return sigma*Z+mu;	
		
	}
	
	
	public static void main(String[] args) {
		Random r = new Random();
		for (int i=0;i<1000;i++)
			System.out.println(gaussian(15,Math.sqrt(5)));
	}

}
