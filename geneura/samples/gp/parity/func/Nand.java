/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package geneura.samples.gp.parity.func;

import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.tree.AbstractPrimitive;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.ITreeNode;
import geneura.org.individuals.tree.SimpleTreeNode;
import geneura.samples.gp.parity.ParityData;


/* 
 * Nand.java
 * 
 * Created: Wed Nov  3 18:26:37 1999
 * By: Sean Luke
 */

/**
 * @author Sean Luke
 * @version 1.0 
 */

public class Nand extends AbstractPrimitive
{
public String toString() { return "Nand"; }


public void eval(GPIndividual individual, Object problem, ITreeNode cNode) {

		cNode.getChildAt(0).eval(individual, problem);
	
	// If first child is 1
	if (((ParityData)((GPIndividual)individual).problem_data).x == 1 )
			cNode.getChildAt(1).eval(individual, problem);
		
	

    ((ParityData)((GPIndividual)individual).problem_data).x ^= 1;
}

public IPrimitive getNewInstance() {
	return new Nand();
}

public int getNumArguments() {
	return 2;
}

public boolean isTerminal() {
	return false;
}
}


