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

import geneura.org.GPEvaluator;
import geneura.org.config.Configuration;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.ITreeGenome;
import geneura.org.individuals.tree.ITreeNode;
import geneura.org.individuals.tree.SimpleTreeGenome;


/**
* Tree initer which uses Koza's "ramped half and half" method (GP 1, p. 93). This
* is apparently the most effective tree initialization method.
* Half of all trees are created using the grow method; the other half with the full method.
* Within each half, an equal number of trees are created with maximum
* depth ranging from 2 to the specified population-wide maximum depth.
* @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
* @see jeo.initers.genomes.AbstractTreeIniter
*/

public class RampedHalfAndHalfTreeIniter extends GPIniter{
   
	IPrimitive[] mPrimitives;
	
   /**
    * Constructs a RampedHalfAndHalfTreeIniter.
    * @param pDepth population-wide maximum depth for any tree
    * @param pFunctions set of non-terminal functions we can use to make tree
    * @param pTermianls set of functions we can use to make tree
    * @param pPopSize population size
    */
   public RampedHalfAndHalfTreeIniter(int depth,IPrimitive[] pPrimitives, GPEvaluator problem) {
	   super(depth,pPrimitives,problem);
	   mPrimitives = pPrimitives;
   }
   
   
   /**
    * Constructs and returns a tree.
    * Half of all trees are created using the grow method; the other half with the full method.
    * Within each half, an equal number of trees are created with maximum
    * depth ranging from 2 to the specified population-wide maximum depth.
    * @param pPopIndex index in the population of the created IInfoHabitant
    * @return IGenome containing the newly grown tree
    */
   public ITreeGenome init(int pPopIndex,GPEvaluator problem) {
       int vCurrentDepth = 2 + (int)((Configuration.gp_initial_depth-1) * (pPopIndex/(double)Configuration.population_size));
       
       if (pPopIndex % 2 == 0) {
           // Even-indexed individuals use the grow method
           GPIniter vGrowTreeIniter = new GrowTreeIniter(vCurrentDepth, mPrimitives,problem);
           return vGrowTreeIniter.init(pPopIndex,problem);
       } else {
           // Odd-indexed individuals use the full method
           GPIniter vFullTreeIniter = new FullTreeIniter(vCurrentDepth, mPrimitives,problem);
           return vFullTreeIniter.init(pPopIndex,problem);
       }
   }
   
   /**
    * This should never be called in this class, so we always throw an exception.
    */
   public void growSubtree(ITreeNode vParentNode, int pDepth) {
       throw new RuntimeException("growSubtree() should never be called in RampedHalfAndHalfTreeIniter!");
   }
   
}