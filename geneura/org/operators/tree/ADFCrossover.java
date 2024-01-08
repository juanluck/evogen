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
import geneura.org.individuals.tree.ADFGenome;
import geneura.org.individuals.tree.ADFGenomeValidator;
import geneura.org.individuals.tree.IGenomeValidator;
import geneura.org.individuals.tree.ITreeGenome;
import geneura.org.individuals.tree.ITreeNode;
import geneura.org.individuals.tree.SimpleTreeGenome;
import geneura.org.individuals.tree.SimpleTreeGenomeValidator;


/**
 * Choses the trees to undergo crossover and then implements Koza's GP crossover,
 * where internal (interior) nodes and external (leaf)
 * nodes have different probabilities of selection for crossover.
 * @author  Eva Alfaro Cid
 * @modified Juan Luis Jiménez Laredo
 * @see jeo.operators.tree.AbstractTreeCrossover
 */

public class ADFCrossover extends AbstractTreeCrossover {

    /**
     * Probability of selecting an internal node for crossover (remainder are leaf nodes)
     */
    protected double mInternalNodeProb;

    /**
     * Constructs an ADFCrossover with the given probability of internal node selection.
     * @param pGenomeValidator IGenomeValidator to use to check results
     * @param pInternalNodeProb Probability of selecting an internal node for crossover;
     * the probability of selecting a leaf node is 1 - pInternalNodeProb.
     */
    public ADFCrossover(IGenomeValidator pGenomeValidator,double pc, double pInternalNodeProb) {
        super(pGenomeValidator,pc);
        mInternalNodeProb = pInternalNodeProb;
    }

   
    /**
     * Does the crossover by calling this.doCross(IInfoHabitant, IInfoHabitant).
     * If the crossover fails,
     * we reset the genomes of each parent to a cached clone copy of the original,
     * so no genome is changed.
     * @param pMomHab the mom IInfoHabitant
     * @param pDadHab the dad IInfoHabitant
     * @return true iff this.doCross(IInfoHabitant, IInfoHabitant) returns true
     */
    public Individual cross(Individual father, Individual mother) {
     	
		GPIndividual treefather = (GPIndividual)father.clone();
		GPIndividual treemother = (GPIndividual)mother.clone();

        // if the crossover fails, we pretend we didn't even try
        Individual indcross = doCross(treefather, treemother);
        return indcross;      
    }


    /**
     * Does the crossover. We use selectCrossoverNode(IInfoHabitant) to select the
     * crossover node for each parent. There are two cases:
     * <ul>
     *  <li> Selected nodes are in the result defining branch.
     *  <li> Selected nodes are in an ADF branch.
     * </ul>
     * The crossover operation is considered successful if the mGenomeValidator
     * returns true for both children. 
     * @param pMomHab the mom IInfoHabitant
     * @param pDadHab the dad IInfoHabitant
     * @return true iff the mGenomeValidator returns true for both children.
     */
    public GPIndividual doCross(GPIndividual pDadHab, GPIndividual pMomHab) {
    	
		int nADFs = ((ADFGenome)pDadHab.getTreeGenome()).getNumberOfADFs();
		int []pMaxDepthADFs = new int[nADFs];
		//To the moment the initial maximum depth of the ADFs is 
		//contemplated to be equal to the result defining branch
		for (int i=0;i<nADFs;i++) pMaxDepthADFs[i]= Configuration.gp_tree_depth;		
    	//ADFGenomeValidator validator = new ADFGenomeValidator(Configuration.gp_initial_depth, pMaxDepthADFs);

    	
        ADFGenome vMom = (ADFGenome)((GPIndividual)pMomHab).getTreeGenome();
        ADFGenome vDad = (ADFGenome)((GPIndividual)pDadHab).getTreeGenome();

        /**
        * Selects if the crossover points are in the result defining branch
        * or in one of the ADFs.
        */
        
        double selector = CommonState.r.nextDouble();
	      Integer intNumNodes = new Integer(vMom.getNumNodes()-1);
	      Integer intNumNodesDefBranch = new Integer(vMom.getDefNumNodes());
        double propNodes = intNumNodesDefBranch.doubleValue()/intNumNodes.doubleValue();

        int i=-1;
        ITreeNode vMomBranchToCross;
        ITreeNode vDadBranchToCross;

        /**
        * Case 1: Selected nodes are in one of the ADFs
        */
        if (selector>propNodes) {
            while ((selector>propNodes)&(propNodes<1)) {
		          i++;
              propNodes = propNodes+vMom.getADFNumNodes(i);
            }
            vMomBranchToCross = vMom.getADFRootNode(i);
  	        vDadBranchToCross = vDad.getADFRootNode(i);
        /**
        * Case 2: Selected nodes are in the result defining branch
        */
        } else {
            vMomBranchToCross = vMom.getDefRootNode();
		    vDadBranchToCross = vDad.getDefRootNode();
        }

        /**
        * Selects the crossover point in the chosen branch.
        */
        ITreeNode vMomSubtree = selectCrossoverNode(new SimpleTreeGenome(vMomBranchToCross,vMom.get_problem()));
        ITreeNode vDadSubtree = selectCrossoverNode(new SimpleTreeGenome(vDadBranchToCross,vDad.get_problem()));

        ITreeNode vMomPreviousParent = vMomSubtree.getParent();
        ITreeNode vDadPreviousParent = vDadSubtree.getParent();

        int vMomIndexAsChild = vMomPreviousParent.getIndexOfChild(vMomSubtree);
        int vDadIndexAsChild = vDadPreviousParent.getIndexOfChild(vDadSubtree);

        // now do the cross
        vMomPreviousParent.setChildAt(vMomIndexAsChild, vDadSubtree);
        vDadPreviousParent.setChildAt(vDadIndexAsChild, vMomSubtree);
        
        if (CommonState.r.nextDouble()<0.5)
     	   if (mGenomeValidator.isValid(vMom))
     		   return pMomHab;
     	   else if(mGenomeValidator.isValid(vDad))
     		   return pDadHab;
     	   else
     		   return null;
        else
     	   if (mGenomeValidator.isValid(vDad))
     		   return pDadHab;
     	   else if(mGenomeValidator.isValid(vMom))
     		   return pMomHab;
     	   else
     		   return null;

    }

    protected ITreeNode selectCrossoverNode(ITreeGenome pTreeGenome) {
        if (CommonState.r.nextDouble() < mInternalNodeProb)
            return pTreeGenome.getRandomInteriorNode();
        else
            return pTreeGenome.getRandomLeafNode();
    }




}


