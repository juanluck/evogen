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

package geneura.org.operators.tree;

import random.CommonState;
import geneura.org.GPEvaluator;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.ITreeNode;
import geneura.org.individuals.tree.SimpleTreeNode;

/**
* Full tree implementation of AbstractTreeIniter.  Grows trees with uniform depth.
* @todo Currently supports trees which only have one uniform argument/return type!
* @see jeo.initers.genomes.AbstractTreeIniter
* @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
*/

public class FullTreeIniter extends GPIniter{
   
   /**
    * Constructs a FullTreeIniter.
    * @param pDepth uniform depth of the tree
    * @param pFunctions array of functions to use
    * @param pTerminals array of terminals to use
    */
   public FullTreeIniter(int vCurrentDepth, IPrimitive[] pPrimitives, GPEvaluator problem) {
	   super(vCurrentDepth, pPrimitives, problem);
   }
      
   /**
    * Given a parent node, grow a subtree with the uniform given depth
    * @param vParentNode parent node of the subtree
    * @param pDepth uniform depth of the subtree
    */
   public void growSubtree(ITreeNode vParentNode, int pDepth) {
       // the parent node is the subtree
       if (pDepth == 1) return;
       
       // primitives to use, depending on depth of tree
       IPrimitive vPrimitives[] = (pDepth == 2) ? mTerminals : mFunctions;
       
       for (int i = 0; i < vParentNode.getNumChildren(); i++) {
           IPrimitive vPrimitive = vPrimitives[CommonState.r.nextInt(vPrimitives.length)].getNewInstance();
           ITreeNode vChildNode = new SimpleTreeNode(vPrimitive, vParentNode);
           vParentNode.setChildAt(i, vChildNode);
           growSubtree(vChildNode, pDepth - 1);
       }
   }
}