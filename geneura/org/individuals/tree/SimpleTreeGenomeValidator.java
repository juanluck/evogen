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

import geneura.org.individuals.tree.IGenomeValidator;

/**
 * IGenomeValidator for a SimpleTreeGenome. Currently, checks only to make sure
 * that the tree depth does not exceed a given maximum.
 * @see jeo.genomes.IGenomeValidator
 * @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
 * @modified Juan Luis Jiménez Laredo (Dpto: ATC. Universidad de Granada)
 */

public final class SimpleTreeGenomeValidator implements IGenomeValidator {
    /**
     * Max depth of any tree genome allowed
     */
    protected int mMaxDepth;
 
    /**
     * Creates a SimpleTreeGenomeValidator which checks to make sure the genome is of depth
     * no larger than the given max.
     * @param pMaxDepth maximum possible depth of the tree.
     */
    public SimpleTreeGenomeValidator(int pMaxDepth) {
        mMaxDepth = pMaxDepth;
    }

    /**
     * Validates a genome, making sure that the depth does not exceed the given maximum.
     * @param pGenome the genome to check
     * @return true iff the genome is valid
     */
    public boolean isValid(ITreeGenome pGenome) {
        return (((ITreeGenome)pGenome).getDepth() <= mMaxDepth);
    }
}