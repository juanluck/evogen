package geneura.org.individuals;

import edu.uci.ics.jung.utils.UserDataContainer.CopyAction.Clone;
import geneura.org.Evaluator;
import geneura.org.GPEvaluator;
import geneura.org.config.Configuration;
import geneura.org.individuals.binary.BinaryChromosome;
import geneura.org.individuals.real.DoubleIndividual;
import geneura.org.individuals.tree.GPData;
import geneura.org.individuals.tree.IPrimitive;
import geneura.org.individuals.tree.ITreeGenome;
import geneura.org.individuals.tree.ITreeNode;
import geneura.org.individuals.tree.SimpleTreeGenome;
import geneura.org.operators.tree.GPIniter;
import geneura.samples.ssgaBenchmark.PPEAKS;

public abstract class GPIndividual extends Individual implements IComparator{
	
	protected ITreeGenome chr;
	public GPData problem_data;
	public IPrimitive [] pPrimitives;
	
	public GPIndividual(GPData problem_data){
		this.problem_data =problem_data;
		
	}
	
	public GPIndividual(IPrimitive[] pPrimitives,GPEvaluator problem, GPData problem_data,int popIndex) {

		this.pPrimitives = pPrimitives;
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Class[] initer_args = new Class[] {int.class,IPrimitive[].class,GPEvaluator.class};
		Object[] initer_obj = new Object[]{new Integer(Configuration.gp_initial_depth),pPrimitives,problem};
		GPIniter initer = null;
		
		if (Configuration.gp_adf){
			try {
				initer = (GPIniter) loader.loadClass(Configuration.gp_adf_initer)
				.getConstructor(initer_args)
				.newInstance(initer_obj);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}else{
			try {
				initer = (GPIniter) loader.loadClass(Configuration.gp_initer)
				.getConstructor(initer_args)
				.newInstance(initer_obj);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
		
		
		ITreeGenome gen = initer.init(popIndex,problem);
		setChr(gen);
		this.problem_data = problem_data;
	}

	public ITreeGenome getTreeGenome() {
		return chr;
	}

	public void setChr(ITreeGenome chr) {
		this.chr = chr;
	}

		
	public boolean equals(Object obj) {
	if (obj instanceof GPIndividual) {
		GPIndividual ind = (GPIndividual) obj;
		if(ind.fitness == this.fitness)
			if (!ind.chr.equals(this.chr)) return false;
		else return false;
	}else return false;
	return true;
	}

	
	public Object clone(){
		GPIndividual ind = new GPIndividual(this.problem_data) {
		};
		ITreeGenome genome=null;
		try {
			genome =  this.getTreeGenome().publicClone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		ind.problem_data = this.problem_data;
		ind.pPrimitives = this.pPrimitives;
		ind.setChr(genome);
		ind.fitness = this.fitness;
		return ind;
	}

}
