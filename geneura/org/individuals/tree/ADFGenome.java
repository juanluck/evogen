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

import java.util.List;

import geneura.org.individuals.GPIndividual;



/**
 * Implementation of an ITreeGenome. The tree is composed of an array of trees.
 * The first tree is the result defining tree and the others are the ADFs
 * @see jeo.genomes.tree.ITreeGenome
 * @author  Eva Alfaro Cid
 * @modified Juan Luis Jiménez Laredo
 */

public class ADFGenome implements ITreeGenome {


  public ITreeNode mRootNode;

  public int numADFs;
  
  protected Object problem;

  /**
  * Creates an ADFGenome with a number of ADFs equal to numADFs.
  * @param numADFs is the number of ADFs. The maximum number of ADFs is set to 4.
  * @param mRootNode is an special type of node that links all the trees together.
  * This is necessary for the evaluation.
  */
  public ADFGenome(int numADFs,ITreeNode mRootNode, Object problem) {
    this.mRootNode = mRootNode;
    if (numADFs>4) throw new IllegalArgumentException("Maximum number of ADFs is 4");
    else this.numADFs = numADFs;
    this.problem = problem;

    }

    /**
     * Returns an instance of &quot;this&quot; Genome.
     * @return an copy of Genome object.
     * @exception CloneNotSupportedException. This exception will take place if
     * some of the calls inside to copy method are not supported.
     */
    public ITreeGenome publicClone() throws CloneNotSupportedException {
       return new ADFGenome(numADFs,getRootNode().cloneSubtree(),problem);
    }

    /**
     * Returns the genes of the tree genome
     * @return the root node (an ITreeNode)
     */
    public  Object getGenes() {
        return mRootNode;
    }

    /**
    * Returns the genes of the result defining branch of the tree
    * @return the root node (an ITreeNode)
    */
    public  Object getDefGenes() {
        return mRootNode.getChildAt(0);
    }

    /**
    * Returns the genes of the ADF genome specified by numADF
    * @return the root node (an ITreeNode)
    */
    public  Object getADFGenes(int numADF) {
        if (numADF>numADFs-1) throw new ArrayIndexOutOfBoundsException("That ADF does not exist");
        return mRootNode.getChildAt(numADF+1);
    }

    /**
    * Returns the number of ADFs
    */
    public int getNumberOfADFs() {
        return numADFs;
    }

    /**
    * Sets the genes of the tree
    * @param pGenes the ITreeNode root node of the tree
    */
    public  void setGenes(Object pGenes) {
        mRootNode= (ITreeNode)pGenes;
    }

    /**
    * Sets the genes of the result defining branch of the tree
    * @param pGenes the ITreeNode root node of the tree
    */
    public  void setDefGenes(Object pGenes) {
        mRootNode.setChildAt(0,(ITreeNode)pGenes);
    }

    /**
    * Sets the genes of the ADFpointed by numADF.
    * @param pGenes the ITreeNode root node of the ADF tree
    */
    public  void setADFGenes(Object pGenes, int numADF) {
        if (numADF>numADFs-1) throw new ArrayIndexOutOfBoundsException("That ADF does not exist");
        mRootNode.setChildAt(numADF+1,(ITreeNode)pGenes);
    }

    /**
    * Sets the number of ADFs.
    */
    public void setNumberOfADFs(int pNumADFs) {
        numADFs = pNumADFs;
    }

    /**
    * Gets the total depth of the tree
    * @return Number of nodes in the tree.
    */
    public int getDepth() {
        return mRootNode.getSubtreeDepth();
    }

    /**
    * Gets the depth of the result defining branch of the tree
    * @return Number of nodes in the tree.
    */
    public int getDefDepth() {
        return mRootNode.getChildAt(0).getSubtreeDepth();
    }

    /**
    * Gets the total depth of the ADF pointed by numADF.
    * @return Number of nodes in the ADF tree.
    */
    public int getADFDepth(int numADF) {
        if (numADF>numADFs-1) throw new ArrayIndexOutOfBoundsException("That ADF does not exist");
        return mRootNode.getChildAt(numADF+1).getSubtreeDepth();
    }

    /**
    * Gets the total number of nodes in the tree
    * @return Number of nodes in the tree.
    */
    public int getNumNodes() {
        return mRootNode.getSubtreeNumNodes();
    }

    /**
    * Gets the number of nodes in the result defining branch of the tree
    * @return Number of nodes in the tree.
    */
    public int getDefNumNodes() {
        return mRootNode.getChildAt(0).getSubtreeNumNodes();
    }

    /**
    * Gets the total number of nodes in the ADFs pointed by numADF.
    * @return Number of nodes in the ADF tree.
    */
    public int getADFNumNodes(int numADF) {
        if (numADF>numADFs-1) throw new ArrayIndexOutOfBoundsException("That ADF does not exist");
        return mRootNode.getChildAt(numADF+1).getSubtreeNumNodes();
    }

    /**
    * Returns the genes of the tree genome
    * @return the root node (an ITreeNode)
    */
    public  ITreeNode getRootNode() {
        return mRootNode;
    }

    /**
    * Returns the genes of the result defining branch of the tree genome
    * @return the root node (an ITreeNode)
    */
    public  ITreeNode getDefRootNode() {
        return mRootNode.getChildAt(0);
    }
    
    /**
    * Returns the genes of the ADF genome specified by numADF
    * @return the root node (an ITreeNode)
    */
    public  ITreeNode getADFRootNode(int numADF) {
        if (numADF>numADFs-1) throw new ArrayIndexOutOfBoundsException("That ADF does not exist");
        return mRootNode.getChildAt(numADF+1);
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
    * Sets the root node of the result defining branch of the tree.
    * @param pRootNode The root node of the tree, to set.
    */
    public void setDefRootNode(ITreeNode pRootNode) {
        if (!pRootNode.isRoot())
            throw new RuntimeException("Tried to set root node, but node has parents!");
        mRootNode.setChildAt(0, pRootNode);
    }

    /**
    * Sets the root node of the ADF specified by numADF.
    * @param pRootNode The root node of the tree, to set.
    */
    public void setADFRootNode(ITreeNode pRootNode, int numADF) {
        if (!pRootNode.isRoot())
            throw new RuntimeException("Tried to set root node, but node has parents!");

        if (numADF>numADFs-1) throw new ArrayIndexOutOfBoundsException("That ADF does not exist");
        mRootNode.setChildAt(numADF+1, pRootNode);
    }

    /**
    * Gets a random node from the tree.
    * @return An interior node selected with uniform random probability from this tree.
    */
    public ITreeNode getRandomNode() {
        return mRootNode.getRandomNodeInSubtree();
    }

    /**
    * Gets a random node from the result defining branch of the tree.
    * @return An interior node selected with uniform random probability from this tree.
    */
    public ITreeNode getDefRandomNode() {
        return mRootNode.getChildAt(0).getRandomNodeInSubtree();
    }

    
    /**
    * Gets a random node from the ADF specified by numADF.
    * @return An interior node selected with uniform random probability from this tree.
    */
    public ITreeNode getADFRandomNode(int numADF) {
        if (numADF>numADFs-1) throw new ArrayIndexOutOfBoundsException("That ADF does not exist");
        return mRootNode.getChildAt(numADF+1).getRandomNodeInSubtree();
    }

    /**
    * Gets a random leaf (external) node from the tree.
    * @return An interior node selected with uniform random probability from this tree.
    */
    public ITreeNode getRandomLeafNode() {
        return mRootNode.getRandomLeafNodeInSubtree();
    }

    /**
    * Gets a random leaf (external) node from the result defining branch of the tree.
    * @return An interior node selected with uniform random probability from this tree.
    */
    public ITreeNode getDefRandomLeafNode() {
        return mRootNode.getChildAt(0).getRandomLeafNodeInSubtree();
    }

    /**
    * Gets a random leaf (external) node from the ADF specified by numADF.
    * @return An interior node selected with uniform random probability from this tree.
    */
    public ITreeNode getADFRandomLeafNode(int numADF) {
        if (numADF>numADFs-1) throw new ArrayIndexOutOfBoundsException("That ADF does not exist");
        return mRootNode.getChildAt(numADF+1).getRandomLeafNodeInSubtree();
    }

    /**
    * Gets a random non-leaf (internal) node from the tree.
    * @return An interior node selected with uniform random probability from this tree.
    */
    public ITreeNode getRandomInteriorNode() {
        return mRootNode.getRandomInteriorNodeInSubtree(); 
    }

    /**
    * Gets a random non-leaf (internal) node from the result defining branch of the tree.
    * @return An interior node selected with uniform random probability from this tree.
    */
    public ITreeNode getDefRandomInteriorNode() {
        return mRootNode.getChildAt(0).getRandomInteriorNodeInSubtree();
    }

    
    /**
    * Gets a random non-leaf (internal) node from the ADF specified by numADF.
    * @return An interior node selected with uniform random probability from this tree.
    */
    public ITreeNode getADFRandomInteriorNode(int numADF) {
        if (numADF>numADFs-1) throw new ArrayIndexOutOfBoundsException("That ADF does not exist");
        return mRootNode.getChildAt(numADF+1).getRandomInteriorNodeInSubtree();
    }

//    /**
//    * Evaluates a boolean tree, assuming all primitives take only boolean arguments and return only
//    * boolean values.
//    * @return The value for the given tree, assuming it is composed of boolean functions.
//    */
//    public boolean evaluateBooleanTree() {
//        return evaluateDefBooleanTree();
//    }

//    /**
//    * Evaluates the result defining branch, assuming all primitives take only boolean arguments and return only
//    * boolean values.
//    * @return The value for the given tree, assuming it is composed of boolean functions.
//    */
//    public boolean evaluateDefBooleanTree() {
//        return mRootNode.getChildAt(0).evaluateBooleanSubtree();
//    }    

    ///**
    //* Evaluates a boolean ADF tree specified by numADF, assuming all primitives take only boolean arguments and return only
    //* boolean values.
    //* @return The value for the given tree, assuming it is composed of boolean functions.
    //*/
    //public boolean evaluateADFBooleanTree(int numADF) {
    //    if (numADF>numADFs-1) throw new ArrayIndexOutOfBoundsException("That ADF does not exist");
    //    return mRootNode.getChildAt(numADF+1).evaluateBooleanSubtree();
    // }

    ///**
    //* Evaluates a double tree assuming all primitives take only double arguments and return only
    //* double values.
    //* @return The value for the given tree, assuming it is composed of boolean functions.
    //*/
    //public double evaluateDoubleTree() {
    //    return evaluateDefDoubleTree();
    //}
    
//    /**
//    * Evaluates the result defining branch, assuming all primitives take only double arguments and return only
//    * double values.
//    * @return The value for the given tree, assuming it is composed of double functions.
//    */
//    public double evaluateDefDoubleTree() {
//        return mRootNode.getChildAt(0).evaluateDoubleSubtree();
//    }


//    /**
//    * Evaluates a double ADF tree specified by numADF, assuming all primitives take only double arguments and return only
//    * double values.
//    * @return The value for the given tree, assuming it is composed of double functions.
//    */
//    public double evaluateADFDoubleTree(int numADF) {
//        if (numADF>numADFs-1) throw new ArrayIndexOutOfBoundsException("That ADF does not exist");
//        return mRootNode.getChildAt(numADF+1).evaluateDoubleSubtree();
//    }
    
    public String toString() {
	      String st = new String(mRootNode.getChildAt(0).toString());
	      for (int i=0; i<numADFs; i++)
		        st = st+"; ADF"+i+": "+mRootNode.getChildAt(i+1).toString();
	      return st;
    }

	public void eval(GPIndividual individual, Object problem) {
		// Evaluates the result defining branch
		mRootNode.getChildAt(0).eval(individual, problem);
	}
	
	

	public Object get_problem() {
		return problem;
	}
	
}



