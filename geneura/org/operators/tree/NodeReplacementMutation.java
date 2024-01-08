package geneura.org.operators.tree;

import random.CommonState;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.individuals.tree.IGenomeValidator;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.ITreeGenome;
import geneura.org.individuals.tree.ITreeNode;
import geneura.org.individuals.tree.SimpleTreeNode;
import geneura.org.operators.Mutation;

public class NodeReplacementMutation extends Mutation {


	public NodeReplacementMutation(double pm) {
	       super(pm);
	   }
	
	@Override
	public Individual mutate(Individual ind) {
		IPrimitive[] mTerminals = GPIniter.get_terminals();

		IPrimitive newnode = mTerminals[CommonState.r.nextInt(mTerminals.length)].getNewInstance();
		
		GPIndividual treeind = (GPIndividual)ind.clone();
		ITreeGenome treestructure = (ITreeGenome)treeind.getTreeGenome();
		ITreeNode oldnode = treestructure.getRandomLeafNode();
		ITreeNode nParent = oldnode.getParent();
		ITreeNode newBornNode = new SimpleTreeNode(newnode, nParent);
        
		//System.out.println("old: "+oldnode.toString()+" new: "+newBornNode.toString());
		
        int index = nParent.getIndexOfChild(oldnode);
        nParent.setChildAt(index, newBornNode);
        
		return treeind;
	}

}
