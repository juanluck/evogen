package geneura.org.operators.tree;

import java.util.ArrayList;

import random.CommonState;

import geneura.org.Evaluator;
import geneura.org.GPEvaluator;
import geneura.org.config.Configuration;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.ITreeGenome;
import geneura.org.individuals.tree.ITreeNode;
import geneura.org.individuals.tree.SimpleTreeGenome;
import geneura.org.individuals.tree.SimpleTreeNode;

public abstract class GPIniter {
	
	/**
     * Array of functions to use to build the tree
     */
	protected static IPrimitive[] mFunctions;
    
    /**
     * Array of primitives to use to build the tree
     */
    protected static IPrimitive[] mTerminals;
    
    protected int cdepth;
    
    protected GPEvaluator theproblem;
    

	/**
     * Constructs an GPIniter.

     * @param pFunctions set of non-terminal functions we can use to make tree
     * @param pTermianls set of functions we can use to make tree
     */
    
    public GPIniter(int current_depth, IPrimitive[] pPrimitives, GPEvaluator problem) {
        initFunctionsAndTerminals(pPrimitives);
        cdepth = current_depth;
        theproblem = problem;
    }
    
    /**
     * Private helper function to initialize arrays of functions and terminals
     * from list of primitives.
     */
    protected void initFunctionsAndTerminals(IPrimitive[] pPrimitives) {
        ArrayList vTerminals = new ArrayList();
        ArrayList vFunctions = new ArrayList();
        
        for (int i = 0; i < pPrimitives.length; i++) {
            if (pPrimitives[i].isTerminal()) {
                vTerminals.add(pPrimitives[i]);
            } else {
                vFunctions.add(pPrimitives[i]);
            }
        }
        
        mTerminals = new IPrimitive[vTerminals.size()];
        vTerminals.toArray(mTerminals);
        
        mFunctions = new IPrimitive[vFunctions.size()];
        vFunctions.toArray(mFunctions);
    }
    
    /**
     * Constructs and returns a randomly generated tree using growSubtree().
     * @param pPopIndex index in the population of the created IInfoHabitant; ignore if you wish.
     * @return IGenome containing the newly grown tree
     */
    public ITreeGenome init(int pPopIndex, GPEvaluator problem) {
        // randomly set root node (NOTE: NO TREE MAY BE A SINGLE NODE)
        IPrimitive vRootFunction = mFunctions[CommonState.r.nextInt(mFunctions.length)].getNewInstance();
        ITreeNode vRootNode = new SimpleTreeNode(vRootFunction, null);
        
        // create the subtrees (for now, ignore type information)
       
        growSubtree(vRootNode, cdepth);
        
        ITreeGenome vTreeGenome = new SimpleTreeGenome(vRootNode,problem);
        
        return vTreeGenome;
    }
    
    /**
     * Given a parent node, grows a subtree with the given maximum depth.
     * @param vParentNode parent of the subtree root node
     * @param pDepth maximum depth for the subtree
     */
    abstract public void growSubtree(ITreeNode vParentNode, int pDepth);
    
    public int getCdepth() {
		return cdepth;
	}

	public void setCdepth(int cdepth) {
		this.cdepth = cdepth;
	}
	
	public static IPrimitive[] get_terminals(){
		return mTerminals;
	}
	
	public static IPrimitive[] get_functions(){
		return mFunctions;
	}

}
