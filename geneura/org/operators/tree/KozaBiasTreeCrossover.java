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

import geneura.org.individuals.tree.IGenomeValidator;
import geneura.org.individuals.tree.ITreeGenome;
import geneura.org.individuals.tree.ITreeNode;
import random.CommonState;


/**
* Implements Koza's GP 1 crossover, where internal (interior) nodes and external (leaf)
* nodes have different probabilities of selection for crossover.
* @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
* @modifien Juan Luis Jiménez Laredo
* @see jeo.operators.tree.AbstractTreeCrossover
*/

public class KozaBiasTreeCrossover extends AbstractTreeCrossover {

   /**
    * Probability of selecting an internal node for crossover (remainder are leaf nodes)
    */
   protected double mInternalNodeProb;

   /**
    * Constructs a KozaBiasTreeCrossover with the given probability of internal node selection.
    * @param pGenomeValidator IGenomeValidator to use to check results
    * @param pInternalNodeProb Probability of selecting an internal node for crossover; the probability
    * of selecting a leaf node is 1 - pInternalNodeProb.
    */
   public KozaBiasTreeCrossover(IGenomeValidator pGenomeValidator, double pc, double pInternalNodeProb) {
       super(pGenomeValidator,pc);
       mInternalNodeProb = pInternalNodeProb;
   }

   /**
    * Selects the crossover node from the ITreeGenome, using the bias given by mInternalNodeProb.
    * @param pTreeGenome genome from which to select the crossover node
    * @return selected crossover ITreeNode
    */
   protected ITreeNode selectCrossoverNode(ITreeGenome pTreeGenome) {
       if (CommonState.r.nextDouble() < mInternalNodeProb){
    	   ITreeNode n = pTreeGenome.getRandomInteriorNode();
           return n;
       }else{
    	   ITreeNode n = pTreeGenome.getRandomLeafNode();
           return n;
       }
   }

}