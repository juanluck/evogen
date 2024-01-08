package geneura.org.operators.tree;

import random.CommonState;
import geneura.org.config.Configuration;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.ITreeGenome;
import geneura.org.individuals.tree.ITreeNode;
import geneura.org.individuals.tree.SimpleTreeNode;
import geneura.org.operators.Mutation;

public class SubTreeMutation extends Mutation {

	
	IPrimitive[] mTerminals;
	IPrimitive[] mFunctions;
	
	public SubTreeMutation(double pm)  {
	       super(pm);
	}
	
	@Override
	public Individual mutate(Individual ind) {
		mTerminals = GPIniter.get_terminals();
		mFunctions = GPIniter.get_functions();
		
		GPIndividual treeind = (GPIndividual)ind.clone();
		
		ITreeGenome treestructure = (ITreeGenome)treeind.getTreeGenome();
		
		ITreeNode vDadSubtree = selectCrossoverNode(treestructure);
		ITreeNode vDadPreviousParent = vDadSubtree.getParent();


		while(vDadPreviousParent == null){
			vDadSubtree = selectCrossoverNode(treestructure);
			vDadPreviousParent = vDadSubtree.getParent();
		}
 
		//Building the new subtree
    	int depth = vDadSubtree.getSubtreeDepth();
		growSubtree(vDadSubtree, depth);	
		
		return treeind;
	}
	
	
	 protected ITreeNode selectCrossoverNode(ITreeGenome pTreeGenome) {
	       if (CommonState.r.nextDouble() < Configuration.gp_internalXprobability){
	    	   ITreeNode n = pTreeGenome.getRandomInteriorNode();
	           return n;
	       }else{
	    	   ITreeNode n = pTreeGenome.getRandomLeafNode();
	           return n;
	       }
	   }
	 
	 /**
	    * Given a parent node, grow a subtree with the uniform given depth
	    * @param vParentNode parent node of the subtree
	    * @param pDepth uniform depth of the subtree
	    */
	   protected void growSubtree(ITreeNode vParentNode, int pDepth) {
	       // the parent node is the subtree
	       if (pDepth == 1) return;
	       
	       // primitives to use, depending on depth of tree
	       IPrimitive vPrimitives[] = (pDepth == 2) ? mTerminals : mFunctions;
	       
	       for (int i = 0; i < vParentNode.getNumChildren(); i++) {
	           IPrimitive vPrimitive = vPrimitives[CommonState.r.nextInt(vPrimitives.length)].getNewInstance();
	           ITreeNode vChildNode = new SimpleTreeNode(vPrimitive, vParentNode);
	           vParentNode.setChildAt(i, vChildNode);
	           growSubtree(vChildNode, pDepth - 1);
	       }
	   }

}
