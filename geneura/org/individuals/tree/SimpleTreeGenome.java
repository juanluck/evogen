/* 
 * 
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

import geneura.org.individuals.GPIndividual;

import java.util.Collection;
import java.util.Vector;

/**
 * Simple implementation of an ITreeGenome. The tree is composed of SimpleTreeNodes.
 * @see jeo.genomes.tree.ITreeGenome
 * @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
 * @modified Juan Luis Jiménez Laredo (Dpto: ATC. Universidad de Granada)
 */
public class SimpleTreeGenome implements ITreeGenome {
    /**
     * Root node of the tree.
     */
    protected ITreeNode mRootNode;
    
    protected Object problem;


    /**
     * Constructs a SimpleTreeGenome with the given root node.
     * @param pRootNode The root node of the tree
     */
    public SimpleTreeGenome(ITreeNode pRootNode, Object problem) {
        mRootNode = pRootNode;
        this.problem = problem;
    }
 
    /**
     * Returns the genes of the Genome
     * @return the root node (an ITreeNode)
     */
    public  Object getGenes() {
        return mRootNode;
    }

    /**
     * Sets the genes (root node) of the tree.
     * @param pGenes the ITreeNode root node of the tree
     */
    public void  setGenes(Object pGenes) {
        mRootNode = (ITreeNode)pGenes;
    }

    /**
     * Returns an instance of &quot;this&quot; Genome.
     * @return an copy of Genome object.
     * @exception CloneNotSupportedException. This exception will take place if
     * some of the calls inside to copy method are not supported.
     */
    public ITreeGenome publicClone() throws CloneNotSupportedException {
        return new SimpleTreeGenome(getRootNode().cloneSubtree(),problem);
    }

    /**
     * Gets the depth of the tree.
     * @return Maximum depth of the tree (where a tree consisting of just root node has depth 1).
     */
    public int getDepth() {
        return mRootNode.getSubtreeDepth();
    }

    /**
     * Gets the total number of nodes in the tree.
     * @return Number of nodes in the tree.
     */
    public int getNumNodes() {
        return mRootNode.getSubtreeNumNodes();
    }

    /**
     * Gets the root (top) node of the tree.
     * @return The root node of the tree.
     */
    public ITreeNode getRootNode() {
        return mRootNode;
    }

    /**
     * Sets the root node of the tree.
     * @param pRootNode The root node of the tree, to set.
     */
    public void setRootNode(ITreeNode pRootNode) {
        if (!pRootNode.isRoot())
            throw new RuntimeException("Tried to set root node, but node has parents!");

        mRootNode = pRootNode;
    }


    /**
     * Gets a random node in the tree.
     * @return A node selected with uniform random probability from this tree.
     */
    public ITreeNode getRandomNode() {
        return getRootNode().getRandomNodeInSubtree();
    }


    /**
     * Gets a random leaf (external) node from the tree.
     * @return A leaf node selected with uniform random probability from this tree.
     */
    public ITreeNode getRandomLeafNode() {
        return getRootNode().getRandomLeafNodeInSubtree();
    }


    /**
     * Gets a random non-leaf (internal) node from the tree.
     * @return An interior node selected with uniform random probability from this tree.
     */
    public ITreeNode getRandomInteriorNode() {
        return getRootNode().getRandomInteriorNodeInSubtree();
    }


    /**
     * Evaluates a boolean tree, assuming all primitives take only boolean arguments and return only
     * boolean values.
     * @return The value for the given tree, assuming it is composed of boolean functions.
     */
//    public boolean evaluateBooleanTree() {
//        return getRootNode().evaluateBooleanSubtree();
//    }

    /**
     * Evaluates a double tree, assuming all primitives take only double arguments and return only
     * double values.
     * @return The value for the given tree, assuming it is composed of double functions.
     */
//    public double evaluateDoubleTree() {
//        return getRootNode().evaluateDoubleSubtree();
//    }

    public void eval(GPIndividual individual,Object problem){
    	mRootNode.eval(individual,problem);
    }
    

    /**
     * A string representing the subtree.
     * @return The string representation given by the root node of the tree.
     */
    public String toString() {
        return mRootNode.toString();
    }

	public Object get_problem() {
		return problem;
	}

}
