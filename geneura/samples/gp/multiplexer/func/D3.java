/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package geneura.samples.gp.multiplexer.func;

import geneura.org.config.Logger;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.tree.AbstractPrimitive;
import geneura.org.individuals.tree.GPData;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.ITreeNode;
import geneura.org.individuals.tree.SimpleTreeNode;
import geneura.samples.gp.multiplexer.Multiplexer;
import geneura.samples.gp.multiplexer.MultiplexerData;

/* 
 * D3.java
 * 
 * Created: Wed Nov  3 18:26:37 1999
 * By: Sean Luke
 */

/**
 * @author Sean Luke
 * @modified Juan Luis Jiménez Laredo
 * @version 1.0 
 */

public class D3 extends AbstractPrimitive
    {
	

	public String toString() { return "d3"; }

	public void eval(GPIndividual individual, Object problem, ITreeNode cNode) {
		((MultiplexerData)((GPIndividual)individual).problem_data).x =
            ((((Multiplexer)problem).dataPart >>> 3 ) & 1);
	}
	
	public IPrimitive getNewInstance() {
		return new D3();
	}

	public int getNumArguments() {
		return 0;
	}

	public boolean isTerminal() {
		return true;
	}
    }



