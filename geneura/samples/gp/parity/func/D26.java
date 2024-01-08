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
import geneura.samples.gp.parity.Parity;
import geneura.samples.gp.parity.ParityData;

/* 
 * D26.java
 * 
 * Created: Wed Nov  3 18:26:38 1999
 * By: Sean Luke
 */

/**
 * @author Sean Luke
 * @version 1.0 
 */

public class D26 extends AbstractPrimitive
{
public String toString() { return "D26"; }


public void eval(GPIndividual individual, Object problem, ITreeNode cNode) {
    ((ParityData)((GPIndividual)individual).problem_data).x = 
        ((((Parity)problem).bits >>> 26 ) & 1);
}

public IPrimitive getNewInstance() {
	return new D26();
}

public int getNumArguments() {
	return 0;
}

public boolean isTerminal() {
	return true;
}
}
