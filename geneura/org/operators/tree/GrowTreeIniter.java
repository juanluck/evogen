
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
* Implementation of AbstractTreeIniter which uses Koza's "grow" method given in GP 1.
* This produces trees with uneven branches. The probability of selecting a terminal
* at a given node is equal to the ratio of terminals to the total number of primitives.
* @todo make the probability of selecting a terminal user-settable
* @todo Currently supports trees which only have one uniform argument/return type!
* @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
* @modified Juan Luis Jiménez Laredo
*/

public class GrowTreeIniter extends GPIniter {
   /**
    * The probability of selecting a terminal during tree construction
    */
   protected double kProbTerminal;
   
   /**
    * Constructs a GrowTreeIniter.
    * @param pDepth maximum depth of the tree
    * @param pFunctions array of functions to use
    * @param pTerminals array of terminals to use
    */
   public GrowTreeIniter(int pDepth, IPrimitive[] pPrimitives, GPEvaluator problem) {
       super(pDepth, pPrimitives,problem);
       kProbTerminal = mTerminals.length / (double)(mTerminals.length + mFunctions.length);
   }
   
   
   /**
    * Given a parent node, grow a subtree using Koza's "grow" method (GP 1)
    * @param vParentNode parent node of the subtree
    * @param pDepth maximum depth of the subtree
    */
   public void growSubtree(ITreeNode vParentNode, int pDepth) {
       // the parent node is the whole subtree
       if (pDepth == 1) return;
       
       // primitives to use
       IPrimitive vPrimitives[];
       
       for (int i = 0; i < vParentNode.getNumChildren(); i++) {
           vPrimitives = getPrimitivesToUse(pDepth);
           IPrimitive vPrimitive = vPrimitives[CommonState.r.nextInt(vPrimitives.length)].getNewInstance();
           ITreeNode vChildNode = new SimpleTreeNode(vPrimitive, vParentNode);
           vParentNode.setChildAt(i, vChildNode);
           growSubtree(vChildNode, pDepth - 1);
       }
   }
   
   /**
    * Private helper method.
    * @param pDepth depth at which we're selecting a child
    * @return a terminal if the child is the last level, otherwise select
    *  a terminal or function with kProbTerminal of selecting a terminal
    */
   private IPrimitive[] getPrimitivesToUse(int pDepth) {
       if (pDepth == 2) {
           return mTerminals;
       } else {
           return (CommonState.r.nextDouble() < kProbTerminal) ? mTerminals : mFunctions;
       }
   }
}