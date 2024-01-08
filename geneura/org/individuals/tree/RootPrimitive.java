/* Copyright (c) 2002 The European Commission DREAM Project IST-1999-12679
*
* This file is part of JEO.
* JEO is free software; you can redistribute it and/or modify it under the terms of GNU 
* General Public License as published by the Free Sortware Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* JEO is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied 
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
* License for mor details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software Foundation, Inc., 59 TEmple Place, Suite 330,
* Boston, MA 02111-1307 USA
*
*/

package geneura.org.individuals.tree;

import geneura.org.individuals.GPIndividual;


/**
* Primitive which takes and returns doubles. Returns 0. It is used as a root
* for the ADFGenome.
* @see jeo.genomes.primitives.doubleMath.PureDoubleFunction
* @author  Eva Alfaro Cid
*/

public class RootPrimitive extends AbstractPrimitive {

   int vADFNum;

   /**
    * Creates a RootPrimitive with the givennumber of ADFs
    * @param pADFNum number of ADFs (arity of the primitive = number of ADFs+1)
    */
   public RootPrimitive(int pADFNum) {
       vADFNum = pADFNum;
   }

   
   /**
   * Gets a new instance
   */
   public IPrimitive getNewInstance() {
       return new RootPrimitive(vADFNum);
       }

   public void eval(GPIndividual individual, Object problem, ITreeNode cNode) {
	   // ADFs hang from children 1 ahead
	   cNode.getChildAt(0).eval(individual, problem);
   }


   public int getNumArguments() {
	   return vADFNum+1;
   }

	public boolean isTerminal() {
		return false;
	}




}
