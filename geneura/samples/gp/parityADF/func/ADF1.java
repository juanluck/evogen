package geneura.samples.gp.parityADF.func;

import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.tree.ADFGenome;
import geneura.org.individuals.tree.AbstractPrimitive;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.ITreeNode;
import geneura.samples.gp.parity.ParityData;

public class ADF1 extends AbstractPrimitive {

	
	public String toString() { return "ADF1"; }


	public void eval(GPIndividual individual, Object problem, ITreeNode cNode) {
		
		cNode.getChildAt(0).eval(individual, problem);
		ARG0.dummyarg = ((ParityData)((GPIndividual)individual).problem_data).x;
		cNode.getChildAt(1).eval(individual, problem);
		ARG1.dummyarg = ((ParityData)((GPIndividual)individual).problem_data).x;
		cNode.getChildAt(2).eval(individual, problem);
		ARG2.dummyarg = ((ParityData)((GPIndividual)individual).problem_data).x;
		
		((ADFGenome)((GPIndividual)individual).getTreeGenome()).getADFRootNode(1).eval(individual, problem);
			   
	}


	public IPrimitive getNewInstance() {
		return new ADF1();
	}

	public int getNumArguments() {
		return 3;
	}

	public boolean isTerminal() {
		return false;
	}

}
