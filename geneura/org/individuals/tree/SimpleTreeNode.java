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
import random.CommonState;



/**
 * Simple implementation of an ITreeNode, used by SimpleTreeGenome.
 * @see jeo.genomes.tree.ITreeNode
 * @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
 * @modified Juan Luis Jiménez Laredo  (Dpto: ATC. Universidad de Granada)
 */

public class SimpleTreeNode implements ITreeNode {
    /**
     * Primitive function represented by this node.
     */
    protected IPrimitive        mPrimitive;
 
    /**
     * Parent of this node.
     */
    protected ITreeNode         mParentNode;

    /**
     * Array of children of this node.
     */
    protected ITreeNode[]       mChildren;
    
    /**
     * Number of children
     */
    protected int numChildren;
    
    /**
     * 
     */ 
    public SimpleTreeNode(IPrimitive pPrimitive ,ITreeNode pParentNode) {
        mPrimitive  = pPrimitive;
        mParentNode = pParentNode;
        numChildren = mPrimitive.getNumArguments();

        mChildren   = new ITreeNode[numChildren];
    }

    /**
     * Is this node a leaf?
     * @return True if and only if this tree node has no children.
     */
    public boolean isLeaf() {
        return (mChildren.length == 0);
    }


    /**
     * Is this node the root node?
     * @return Tree if and only if this tree node has no parent.
     */
    public boolean isRoot() {
        return (getParent() == null);
    }

    /**
     * Gets this node's child at pIndex.
     * @param pIndex index of the child
     * @return A pointer to the child ITreeNode at pIndex.
     */
    public ITreeNode getChildAt(int pIndex) {
        return (mChildren[pIndex]);
    }

    /**
     * Gets the arry of this node's children.
     * @return A pointer to the array of children ITreeNodes.
     */
    public ITreeNode[] getChildren() {
        return mChildren;
    }

    /**
     * Sets the child at the given index to be the given node. Note that the child's
     * parent is set to the current node as well.
     * @param pIndex index of child to set
     * @param pChild ITreeNode to set at index
     */
    public void setChildAt(int pIndex, ITreeNode pChild) {
        mChildren[pIndex] = pChild;
        pChild.setParent(this);
    }

    /**
     * How many children does this node have?
     * @return number of children
     */
    public int getNumChildren() {
        return mChildren.length;
    }

    /**
     * What is the depth of the tree rooted at this node?
     * @return depth of the subtree with this node as the root (depth 1 is one node)
     */
    public int getSubtreeDepth() {
        // find the maximum depth of the child subtrees
        int vMax = 0;
        for (int i = 0; i < this.getNumChildren(); i++) {
            int vSubtreeDepth = this.getChildAt(i).getSubtreeDepth();
            if (vSubtreeDepth > vMax) vMax = vSubtreeDepth;
        }

        // add 1 to the max depth to account for this node
        return (vMax + 1);
    }

    /**
     * How many nodes are in the subtree rooted at this node?
     * @return total number of nodes in this subtree, including this node
     * @todo cache this number to save time when called on unchanged tree
     */
    public int getSubtreeNumNodes() {
        // find the total number of nodes in the child subtrees
        int vTotal = 0;
        for (int i = 0; i < this.getNumChildren(); i++) {
            vTotal += this.getChildAt(i).getSubtreeNumNodes();
        }

        // add 1 to the num nodes to account for this node
        return (vTotal + 1);
    }

    /**
     * Gets number of leaf nodes.
     * @return number of leaf nodes
     */
    public int getSubtreeNumLeafNodes() {
        // we count the leaf node
        if (isLeaf()) return 1;

        // find the total number of leaf nodes in the child subtrees
        int vTotal = 0;
        for (int i = 0; i < this.getNumChildren(); i++) {
            vTotal += this.getChildAt(i).getSubtreeNumLeafNodes();
        }

        // return the total
        return vTotal;
    }

    /**
     * Gets number of interior nodes.
     * @return number of interior nodes
     */
    public int getSubtreeNumInteriorNodes() {
        // we ignore leaf nodes
        if (isLeaf()) return 0;

        // find the total number of leaf nodes in the child subtrees
        int vTotal = 0;
        for (int i = 0; i < this.getNumChildren(); i++) {
            vTotal += this.getChildAt(i).getSubtreeNumInteriorNodes();
        }

        // return the total, plus one to account for the current node
        return vTotal+1;
    }

    /**
     * Gets a Lisp-like string representation of the current tree.
     * @return A Lisp-like string representation of the current tree.
     */
    public String toString() {
        if (this.isLeaf()) {
            return getPrimitive().toString();
        } else {
            String vString = "(" + getPrimitive().toString() + " ";
            for (int i = 0; i < this.getNumChildren(); i++) {
                vString += this.getChildAt(i).toString();
                if (i != this.getNumChildren() - 1) vString += " ";
            }
            vString += ")";
            return vString;
        }
    }

    /**
     * Gets the parent of this node.
     * @return The parent of this tree node, or null if this is the root node.
     */
    public ITreeNode getParent() {
        return mParentNode;
    }

    /**
     * Sets the parent of this node.
     * @param pParent ITreeNode to set as parent.
     */
    public void setParent(ITreeNode pParent) {
        mParentNode = pParent;
    }


    /**
     * Gets the index in the array of children that points to the given node.
     * @param pChild Child whose index we want to determine.
     * @return Index of this child in the array of children, or -1 if not found.
     */
    public int getIndexOfChild(ITreeNode pChild) {
        int vNumChildren = getNumChildren();

        for (int i = 0; i < vNumChildren; i++)
            if (pChild == getChildAt(i)) return i;

        return -1;
    }

    /**
     * Gets the primitive (function or terminal) represented by this node.
     * @return The Primitive represented by this node.
     */
    public IPrimitive getPrimitive() {
        return mPrimitive;
    }


    /**
     * Sets the primitive (function or terminal) represented by this node.
     * @parameter pPrimitive The primitive represented by this node.
     */
    public void setPrimitive(IPrimitive pPrimitive) {
        mPrimitive = pPrimitive;
    }

    /**
     * Gets a random node from the subtree rooted at this node.
     * @return A node selected with uniform random probability from this subtree.
     */
    public ITreeNode getRandomNodeInSubtree() {
        // we imagine that nodes are numbered left-to-right, depth-first.
        // we return a node randomly selected from the list of numbered nodes.
        int vSelectedNodeNum = CommonState.r.nextInt(getSubtreeNumNodes());

        // now get this node
        return getNodeNumbered(vSelectedNodeNum, this, new Counter());
    }

    /**
     * Gets a random leaf node from the subtree rooted at this node.
     * @return A leaf node selected with uniform random probability from this subtree.
     */
    public ITreeNode getRandomLeafNodeInSubtree() {
        // we return a leaf node randomly selected from the list of numbered nodes.
        int vSelectedNodeNum = CommonState.r.nextInt(getSubtreeNumLeafNodes());


        // now get this node
        return getLeafNodeNumbered(vSelectedNodeNum, this, new Counter());
    }


    /**
     * Gets a random leaf node from the subtree rooted at this node.
     * Returns an interior node selected with uniform random probability from this subtree.
     * If there are none (e.g., the tree consists of a single terminal) we return this node.
     * @return An interior node selected with uniform random probability from this subtree.
     */
    public ITreeNode getRandomInteriorNodeInSubtree() {
        // if we're already at a leaf, we have no choice but to return it (technically NOT INTERIOR!)
        if (isLeaf()) return this;

        // we return an interior node randomly selected from the list of numbered nodes.
        int numNodes = getSubtreeNumInteriorNodes();
        int vSelectedNodeNum= CommonState.r.nextInt(numNodes);
        
    
        // now get this node
        return getInteriorNodeNumbered(vSelectedNodeNum, this, new Counter());
    }

    /**
     * Finds the node in the current tree which would have the given number if
     * nodes are labeled left-to-right, depth-first, NUMBERING ALL NODES.
     * @param pDesiredNodeNum The desired node number.
     * @param pCurrentNode The current node from which we are searching
     * @param pCounted Counter which contains the number of the current node.
     * @return the node with the given node number
     */
    public ITreeNode getNodeNumbered(int pDesiredNodeNum,
                        ITreeNode pCurrentNode, Counter pCounter) {
        int vNumChildren = pCurrentNode.getNumChildren();

        for (int i = 0; i < vNumChildren; i++) {
            ITreeNode vChild = pCurrentNode.getChildAt(i);
            ITreeNode vResult = getNodeNumbered(pDesiredNodeNum, vChild, pCounter);
            if (vResult != null) return vResult;
        }

        if (pCounter.val() == pDesiredNodeNum) {
            return pCurrentNode;
        } else {
            pCounter.incr();
            return null;
        }
    }

    /**
     * Finds the node in the current tree which would have the given number if
     * nodes are labeled left-to-right, depth-first, ONLY NUMBERING LEAVES.
     * @param pDesiredNodeNum The desired node number.
     * @param pCurrentNode The current node from which we are searching
     * @param pCounted Counter which contains the number of the current node.
     * @return the leaf node with the given number
     */
    /*protected*/public ITreeNode getLeafNodeNumbered(int pDesiredNodeNum,
                        ITreeNode pCurrentNode, Counter pCounter) {
        int vNumChildren = pCurrentNode.getNumChildren();

        for (int i = 0; i < vNumChildren; i++) {
            ITreeNode vChild = pCurrentNode.getChildAt(i);
            ITreeNode vResult = getLeafNodeNumbered(pDesiredNodeNum, vChild, pCounter);
            if (vResult != null) return vResult;
        }

        if (pCurrentNode.isLeaf()) {
            if (pCounter.val() == pDesiredNodeNum) {
                return pCurrentNode;
            } else {
                pCounter.incr();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Finds the node in the current tree which would have the given number if
     * nodes are labeled left-to-right, depth-first, ONLY NUMBERING INTERNAL NODES.
     * @param pDesiredNodeNum The desired node number.
     * @param pCurrentNode The current node from which we are searching
     * @param pCounted Counter which contains the number of the current node.
     * @return the internal node with the given number
     */
    /*protected*/public ITreeNode getInteriorNodeNumbered(int pDesiredNodeNum,
                        ITreeNode pCurrentNode, Counter pCounter) {
        int vNumChildren = pCurrentNode.getNumChildren();

        for (int i = 0; i < vNumChildren; i++) {
            ITreeNode vChild = pCurrentNode.getChildAt(i);
            ITreeNode vResult = getInteriorNodeNumbered(pDesiredNodeNum, vChild, pCounter);
            if (vResult != null) return vResult;
        }

        if (!pCurrentNode.isLeaf()) {
            if (pCounter.val() == pDesiredNodeNum) {
                return pCurrentNode;
            } else {
                pCounter.incr();
                return null;
            }
        } else {
            return null;
        }
    }


    /**
     * Checks to be sure the function has the required number of children, all of the
     * appropriate type, and recursively for all children.
     * @return true if the subtree is valid, otherwise false.
     */
    public boolean checkConstrains() {
        
        return true;
    }

    /**
     * Clones this subtree.
     * @return A copy of this tree such that modifications can be made to the structure
     * of a copied tree without affecting the original (but changes to the primitives
     * themselves will affect all trees).
     */
    public ITreeNode cloneSubtree() {
        // clone the subtree

        ITreeNode vClone = new SimpleTreeNode(this.mPrimitive.getCopy(), null);

        for (int i = 0; i < numChildren; i++) {
           vClone.setChildAt(i, this.getChildAt(i).cloneSubtree());
        }

        return vClone;
    }

	public void eval(GPIndividual individual, Object problem) {
		mPrimitive.eval(individual, problem, this);
	}

 
}
