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
import geneura.org.config.Configuration;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.individuals.tree.IGenomeValidator;
import geneura.org.individuals.tree.ITreeGenome;
import geneura.org.individuals.tree.ITreeNode;
import geneura.org.individuals.tree.SimpleTreeGenomeValidator;
import geneura.org.operators.Crossover;

/**
* Abstract implementation of tree crossover. Subclasses need only implement selectCrossoverNode().
* For use with ITreeGenome. If the cross operation fails for any reason, we return false and
* leave the original trees unchanged. Note that use with, e.g., GenomeOperator may mean
* that repeated attempts are made to get a successful crossover between two parents.  
* @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
* @see jeo.operators.ICrossover
*/

abstract public class AbstractTreeCrossover extends Crossover {
   
		
	/**
    * Checks to make sure the produced genome is valid
    */
   protected IGenomeValidator mGenomeValidator;

   /**
    * Constructs an AbstractTreeCrossover
    * @param pGenomeValidator IGenomeValidator to use to check results
    */
   public AbstractTreeCrossover(IGenomeValidator pGenomeValidator, double pc) {
	   super(pc);
       mGenomeValidator = pGenomeValidator;
   }

   /**
    * 
    */
   public Individual cross(Individual father, Individual mother) {
	   
	   
	   
		//if (CommonState.r.nextDouble()<_pc){
			IGenomeValidator validator = new SimpleTreeGenomeValidator(Configuration.gp_tree_depth);	
			GPIndividual treefather = (GPIndividual)father.clone();
			GPIndividual treemother = (GPIndividual)mother.clone();
			
			ITreeGenome vDad = (ITreeGenome)treefather.getTreeGenome();
			ITreeGenome vMom = (ITreeGenome)treemother.getTreeGenome();
		    
			ITreeNode vDadSubtree = selectCrossoverNode(vDad);
			ITreeNode vMomSubtree = selectCrossoverNode(vMom);
		    
		    ITreeNode vDadPreviousParent = vDadSubtree.getParent();
			ITreeNode vMomPreviousParent = vMomSubtree.getParent();

		    if (vMomPreviousParent == null && vDadPreviousParent == null) {
		        // do nothing, swapping root nodes
		    	
		    	return null;
		    } else if (vMomPreviousParent == null) {
		    	  	// mom is root node
		    		//System.out.println(treefather.getTreeGenome().toString()+"\n");
		    	   
		           //vDadSubtree.setParent(null);
		           //vMom.setRootNode(vDadSubtree);
		           int vDadIndexAsChild = vDadPreviousParent.getIndexOfChild(vDadSubtree);
		           vDadPreviousParent.setChildAt(vDadIndexAsChild, vMomSubtree);
		           //vDad.setRootNode(vDadSubtree);
		           
		           //treefather.setChr(vDad);
		           //System.out.println(treefather.getTreeGenome().toString()+"\n\n");
		           if (validator.isValid(vDad)){
		        	   return treefather;
		           }else{
		        	   return null;
		           }
		    } else if (vDadPreviousParent == null) {
		           // dad is root node		        		  

		           //vMomSubtree.setParent(null);
		           //vDad.setRootNode(vMomSubtree);
		           int vMomIndexAsChild = vMomPreviousParent.getIndexOfChild(vMomSubtree);
		           vMomPreviousParent.setChildAt(vMomIndexAsChild, vDadSubtree);
		           //vMom.setRootNode(vMomSubtree);
		           
		           if (validator.isValid(vMom))
		        	   return treemother;
		           else
		        	   return null;
		    } else {
		           // normal case, swapping two subtrees
		    	
		          int vMomIndexAsChild = vMomPreviousParent.getIndexOfChild(vMomSubtree);
		           int vDadIndexAsChild = vDadPreviousParent.getIndexOfChild(vDadSubtree);

		           // now do the cross
		           vMomPreviousParent.setChildAt(vMomIndexAsChild, vDadSubtree);
		           vDadPreviousParent.setChildAt(vDadIndexAsChild, vMomSubtree);
		           if (CommonState.r.nextDouble()<0.5)
		        	   if (validator.isValid(vMom)){
		        		   
		        		   return treemother;
		        	   } else if(validator.isValid(vDad)){
		        		   
		        		   return treefather;
		        	   }else{
		        		   
		        		   return null;
		        	   }
		           else
		        	   if (validator.isValid(vDad)){
		        		   
		        		   return treefather;
		        	   }else if(validator.isValid(vMom)){
		        		  
		        		   return treemother;
		        	   }else{
		        		   return null;
		        	   }
		    }
		//If a uniform random number is smaller than the probability of crossover
		//it returns null
		//}else{
		//	return (Individual)father.clone();
		//}
			
      
   }


   /**
    * Selects the crossover node from the ITreeGenome which will serve as the
    * crossover swap point.
    * @param pTreeGenome genome from which to select the crossover node
    * @return selected crossover ITreeNode
    */
   abstract protected ITreeNode selectCrossoverNode(ITreeGenome pTreeGenome);

}