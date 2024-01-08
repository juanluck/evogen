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
import geneura.org.individuals.tree.ADFGenome;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.ITreeGenome;
import geneura.org.individuals.tree.ITreeNode;
import geneura.org.individuals.tree.RootPrimitive;
import geneura.org.individuals.tree.SimpleTreeGenome;
import geneura.org.individuals.tree.SimpleTreeNode;

import java.util.*;

/**
 * Tree initer which uses whatever method stored in methodName to initialise
 * the result defining branch as well as the ADFs in an ADFGenome
 * @see jeo.initers.genomes.AbstractTreeIniter
 * @author Eva Alfaro Cid
 */

public class ADFIniter extends GPIniter {

    /**
    * Population size
    */
    protected int mPopSize;
    
    /**
    * Array of functions to use to build the tree
    */
    protected IPrimitive[] mPrimitives;

    /**
    * Initialisation method to use: Grow, Full or RampedHalfAndHalf
    */
    protected String methodName;

    /**
    * Number of ADFs to initialise
    */
    protected int numADFs;

    /**
    * List of arrays of functions to use to build the ADF trees
    */
    protected List vADFPrimitives;

    /**
    * Initial depth of the ADF trees
    */
    protected int[] depthADF;

    /**
    * Constructs an ADFIniter.
    * @param pDepth maximum initial depth for the result defining branch
    * @param depthDF maximum initial depths for the ADFs
    * @param pPrimitives set of terminal and non-terminal functions we can use
    * to make the result defining branch of the tree
    * @param pADFsPrimitives set of terminal and non-terminal functions we can use
    * to make each of the ADFs of the tree
    * @param pPopSize population size
    * @param pMethodName
    */
    public ADFIniter(int pDepth, IPrimitive[] pPrimitives, GPEvaluator problem) {
        super(pDepth, pPrimitives, problem);
        
        methodName = Configuration.gp_initer.substring(Configuration.gp_initer.lastIndexOf(".")+1);
        mPopSize = Configuration.population_size;
        mPrimitives = pPrimitives;
        vADFPrimitives = problem.getADFPrimitives();
        numADFs = problem.getADFPrimitives().size();
        depthADF = new int[problem.getADFPrimitives().size()];
        
        for (int i=0;i<numADFs;i++){
        	depthADF[i] = Configuration.gp_initial_depth;
        	// To the moment, all trees have the same initial depth
        }
     
        
    }
    
    
    /**
    * Constructs and returns a tree array.
    * @param pPopIndex index in the population of the created IInfoHabitant
    * @return IGenome containing the newly grown tree
    */
    public ITreeGenome init(int pPopIndex, GPEvaluator problem) {

    	
    	
        /**
        * Creates the result defining branch of the tree.
        */
	      SimpleTreeGenome vDefBranch = (SimpleTreeGenome)initTree(cdepth, mPrimitives, pPopIndex, problem);
	      ITreeNode root = new SimpleTreeNode(new RootPrimitive(numADFs), null);
	      root.setChildAt(0, vDefBranch.getRootNode());
	      
        /**
        * Creates the ADFs of the tree
        */
	      SimpleTreeGenome vADFBranch;
        for (int i=0; i<numADFs; i++) {
		        IPrimitive[] mPrimADF = (IPrimitive[])vADFPrimitives.get(i);
		        
		        vADFBranch = (SimpleTreeGenome)initTree(depthADF[i], mPrimADF, pPopIndex, problem);
 		        root.setChildAt(i+1, vADFBranch.getRootNode());
        }
        
        return new ADFGenome(numADFs, root,problem);
    }

    /**
    * Creates the tree according to the chosen method.
    */
    public ITreeGenome initTree(int depth, IPrimitive[] primitives, int pPopIndex, GPEvaluator problem) {
	      if ((methodName.startsWith("g")) || (methodName.startsWith("G"))) {
            // Individuals use the grow method
            GPIniter vGrowTreeIniter = new GrowTreeIniter(depth, primitives,problem);
            return vGrowTreeIniter.init(pPopIndex,problem);
	      } else {
		        if ((methodName.startsWith("f")) || (methodName.startsWith("F"))) {
			          // Individuals use the full method
			          GPIniter vFullTreeIniter = new FullTreeIniter(depth, primitives,problem);
			          return vFullTreeIniter.init(pPopIndex,problem);
		        } else {
			          if ((methodName.startsWith("r")) || (methodName.startsWith("R"))) {
				            // Individuals use the ramped half and half method
				            GPIniter vRampedHalfAndHalfTreeIniter = new RampedHalfAndHalfTreeIniter(depth, primitives,problem);
				            return vRampedHalfAndHalfTreeIniter.init(pPopIndex,problem);
			          } else throw new IllegalArgumentException("The " + methodName + " initialisation method is not implemented.");
		        }
	      }
    }

    /**
    * This should never be called in this class, so we always throw an exception.
    */
    public void growSubtree(ITreeNode vParentNode, int pDepth) {
        throw new RuntimeException("growSubtree() should never be called in ArrayTreeIniter!");
    }
    
}


