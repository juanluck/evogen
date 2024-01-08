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


import geneura.org.individuals.GPIndividual;

import java.util.Collection;

/**
 * Interface which characterizes the tree genome, used for genetic programming.
 * @see jeo.IGenome
 * @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
 * @modified Juan Luis Jiménez Laredo (Dpto: ATC. Universidad de Granada)
 */

public interface ITreeGenome{

    /**
     * Gets the depth of the tree.
     * @return Maximum depth of the tree (where a tree consisting of just root node has depth 1).
     */
    public int getDepth();

    /**
     * Gets the total number of nodes in the tree.
     * @return Number of nodes in the tree.
     */
    public int getNumNodes();
 
    /**
     * Gets a random node in the tree.
     * @return A node selected with uniform random probability from this tree.
     */
    public ITreeNode getRandomNode();

    /**
     * Gets a random leaf (external) node from the tree.
     * @return A leaf node selected with uniform random probability from this tree.
     */
    public ITreeNode getRandomLeafNode();

    /**
     * Gets a random non-leaf (internal) node from the tree.
     * @return An interior node selected with uniform random probability from this tree.
     */
    public ITreeNode getRandomInteriorNode();

    /**
     * Gets the root (top) node of the tree.
     * @return The root node of the tree.
     */
    public ITreeNode getRootNode();

    /**
     * Sets the root node of the tree.
     * @param pRootNode The root node of the tree, to set.
     */
    public void setRootNode(ITreeNode pRootNode);

    /**
     * Evaluates a boolean tree, assuming all primitives take only boolean arguments and return only
     * boolean values.
     * @return The value for the given tree, assuming it is composed of boolean functions.
     */
//    public boolean evaluateBooleanTree();
    public void eval(GPIndividual individual, Object problem);

    public Object get_problem();
    
    
    public ITreeGenome publicClone() throws CloneNotSupportedException;
    
    
}
