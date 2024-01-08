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


/**
 * Interface for validating genomes. Operator implementations, or other classes,
 * may use this to make sure that a genome meets a set of pre-specified conditions.
 * @see java.io.Serializable
 * @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
 */

public interface IGenomeValidator extends java.io.Serializable {

    /**
     * Validates a genome.
     * @param pGenome the genome to check
     * @return true iff the genome is valid
     */
    public boolean isValid(ITreeGenome pGenome);

}