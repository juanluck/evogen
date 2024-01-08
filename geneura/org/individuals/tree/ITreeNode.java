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




/**
 * Interface which characterizes the node in a tree genome, used for genetic programming.
 * @see java.io.Serializable
 * @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
 * @modified Juan Luis Jiménez Laredo (Dpto: ATC. Universidad de Granada)
 */

public interface ITreeNode{

    /**
     * Is this tree a leaf (exterior) node?
     * @return True if and only if this tree node has no children.
     */
    public boolean isLeaf();

    /**
     * Is this tree the root node?
     * @return Tree if and only if this tree node has no parent.
     */
    public boolean isRoot();
 
    /**
     * Gets this node's child at pIndex.
     * @param pIndex index of the child
     * @return A pointer to the child ITreeNode at pIndex.
     */
    public ITreeNode getChildAt(int pIndex);

    /**
     * Gets the array of this node's children.
     * @return A pointer to the array of children ITreeNodes.
     */
    public ITreeNode[] getChildren();    
    
    /**
     * Sets this nodes child at the given index.
     * @param pIndex index of child to set
     * @param pChild ITreeNode to set at index
     */
    public void setChildAt(int pIndex, ITreeNode pChild);

    /**
     * How many children does this node have?
     * @return number of children
     */
    public int getNumChildren();

    /**
     * What is the depth of the tree rooted at this node?
     * @return depth of the subtree with this node as the root (depth 1 is one node)
     */
    public int getSubtreeDepth();

    /**
     * How many nodes are in the subtree rooted at this node?
     * @return total number of nodes in this subtree, including this node
     */
    public int getSubtreeNumNodes();

    /**
     * How many leaf nodes are in the subtree rooted at this node?
     * @return total number of leaf nodes in this subtree, including this node (if it's a leaf)
     */
    public int getSubtreeNumLeafNodes();

    /**
     * How many interior nodes are in the subtree rooted at this node?
     * @return total number of interior nodes in this subtree, including this node (if it's interior)
     */
    public int getSubtreeNumInteriorNodes();

    /**
     * Who is this node's parent?
     * @return The parent of this tree node, or null if this is the root node.
     */
    public ITreeNode getParent();

    /**
     * Sets this node's parent.
     * @param pParent ITreeNode parent to serve as the parent for this node.
     */
    public void setParent(ITreeNode pParent);

    /**
     * Gets the index in the array of children that points to the given node.
     * @param pChild Child whose index we want to determine.
     * @return Index of this child in the array of children, or -1 if not found.
     */
    public int getIndexOfChild(ITreeNode pChild);


    /**
     * Gets a random node from the subtree rooted at this node.
     * @return A node selected with uniform random probability form this subtree.
     */
    public ITreeNode getRandomNodeInSubtree();

  
    
    /**
     * Gets a random leaf node from the subtree rooted at this node. 
     * @return A leaf node selected with uniform random probability form this subtree.
     */
    public ITreeNode getRandomLeafNodeInSubtree();

    /**
     * Gets a random interior node from the subtree rooted at this node. 
     * If there are none (e.g., the subtree consists of a single terminal) we return this node.
     * @return An interior node selected with uniform random probability form this subtree.
     */
    public ITreeNode getRandomInteriorNodeInSubtree();

    /**
     * Checks to be sure the function has the required number of children, all of the
     * appropriate type, and recursively for all children.
     * @return true if the subtree is valid, otherwise false.
     */
    public boolean checkConstrains();

    /**
     * Clones this subtree.
     * @return A copy of this tree such that modifications can be made to the structure
     * of a copied tree without affecting the original (but changes to the primitives
     * themselves (if they are not purposely cloned) will affect all trees).
     */
    public ITreeNode cloneSubtree();

    /**
     * Fast evaluation implementation for uniform boolean tree.
     * @return boolean result from evaluating this subtree
     */
    //public boolean evaluateBooleanSubtree();    
    
    /**
     * Fast evaluation implementation for uniform double tree.
     * @return double result from evaluating this subtree
     */
    //public double evaluateDoubleSubtree();       
    
    public void eval(GPIndividual individual, Object problem);
}

