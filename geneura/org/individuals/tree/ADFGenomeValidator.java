/**
 * Copyright (c) 2002 The European Commission DREAM Project IST-1999-12679
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

import geneura.org.GPEvaluator;
import geneura.org.config.Configuration;



/**
 * IGenomeValidator for an ADFGenome. Checks to make sure that the tree depths
 * of the result defining branch and the ADFs do not exceed given maximums.
 * @see jeo.genomes.IGenomeValidator
 * @author  Eva Alfaro Cid
 * @modified Juan Luis Jiménez Laredo
 */



public class ADFGenomeValidator implements IGenomeValidator {
    	/**
     	* Checks to make sure the produced genome is valid
     	*/
     	int mMaxDepth;
      int[] mMaxDepthADFs;

    	/**
     	* Constructs an ADFGenomeValidator
     	* @param pMaxDepth maximum allowed depth of the result defining branch
      * @param pMaxDepthADFs[] maximum allowed depths of the ADFs
     	*/
    	public ADFGenomeValidator (int pMaxDepth, GPEvaluator problem) {
        	mMaxDepth = pMaxDepth;
        	int numADFs = problem.getADFPrimitives().size();
        	mMaxDepthADFs = new int[problem.getADFPrimitives().size()];
            
            for (int i=0;i<numADFs;i++){
            	mMaxDepthADFs[i] = Configuration.gp_tree_depth;
            		// To the moment, all trees have the same maximum depth;
            }
    	}

    	/**
     	* Check if all the trees in the ADFGenome are smaller
      * than their maximum allowed depth
     	*/
    	public boolean isValid (ITreeGenome pGenome) {
		      ADFGenome vGenome = (ADFGenome)pGenome;
		      SimpleTreeGenome mTree = new SimpleTreeGenome(vGenome.getDefRootNode(),null);
		      SimpleTreeGenomeValidator stgv = new SimpleTreeGenomeValidator(mMaxDepth);
		      boolean bool = stgv.isValid(mTree);

		      for (int i=0;i<vGenome.getNumberOfADFs();i++){
			        mTree = new SimpleTreeGenome(vGenome.getADFRootNode(i),null);
			        stgv = new SimpleTreeGenomeValidator(mMaxDepthADFs[i]);
			        bool = bool&stgv.isValid(mTree);
		      }
		      return bool;
    	}

	
}


