package geneura.samples.gp.parity.func;

import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.tree.AbstractPrimitive;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.ITreeNode;
import geneura.samples.gp.parity.ParityData;

public class ARG1 extends AbstractPrimitive {

	static int dummyarg;
	
	public String toString() { return "ARG1"; }

	public void eval(GPIndividual individual, Object problem, ITreeNode cNode) {
		((ParityData)((GPIndividual)individual).problem_data).x = dummyarg;
	}

	public IPrimitive getNewInstance() {
		return new ARG1();
	}

	public int getNumArguments() {
		return 0;
	}

	public boolean isTerminal() {
		return true;
	}

}
