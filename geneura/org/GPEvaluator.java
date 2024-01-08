package geneura.org;

import java.util.List;

import geneura.org.individuals.tree.IPrimitive;

public abstract class GPEvaluator extends Evaluator{

	protected IPrimitive[] mPrimitives;
	
	protected List<IPrimitive[]> ADFPrimitives; 
	
	
	public IPrimitive [] getPrimitives(){
		return mPrimitives;
	}
	
	public List<IPrimitive[]> getADFPrimitives(){
		return ADFPrimitives;
	}
	
	
}
