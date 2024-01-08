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
import geneura.samples.gp.multiplexer.MultiplexerData;


/* 
 * Or.java
 * 
 * Created: Wed Nov  3 18:26:37 1999
 * By: Sean Luke
 */

/**
 * @author Sean Luke
 * @modified Juan Luis Jiménez Laredo
 * @version 1.0 
 */

public class Or extends AbstractPrimitive
    {
	
	public String toString() { return "or"; }

	public void eval(GPIndividual individual, Object problem, ITreeNode cNode) {
	
		// shortcutting AND
		cNode.getChildAt(0).eval(individual, problem);
		

        if (((MultiplexerData)((GPIndividual)individual).problem_data).x == 0 )  // return the second item
        	cNode.getChildAt(1).eval(individual, problem);
        // else return the first item (already there)
	}
	
	public IPrimitive getNewInstance() {
		return new Or();
	}

	public int getNumArguments() {
		return 2;
	}

	public boolean isTerminal() {
		return false;
	}
	
  
    }



