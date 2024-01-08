package geneura.org;

import geneura.org.config.Configuration;
import geneura.org.config.LoadProperties;
import geneura.org.factories.FactoryGraphStatistics;
import geneura.org.factories.FactoryTAKEOVER;

public class GraphStatistics {

	public static void main(String[] args) {
		LoadProperties lp = new LoadProperties(args);
		Configuration.setConfiguration(lp);

		Configuration.Algorithm="GraphStatistics";
		//Configuration.statistic_sample = 100;
		Configuration.representation = "binary";
		//Configuration.population_size = 1000;
		Configuration.minimization=false;
		Configuration.chromosome_size=10;
		//Configuration.termination_max_evaluation=1000000;
		Configuration.evaluation = "geneura.samples.traps.Trap2";
		Configuration.termination_hit_value=false;

		if (Configuration.Algorithm.equals("GraphStatistics")){
			if(Configuration.neighborhood.equals("newscast")){
					FactoryGraphStatistics.createNewscastGraphStatistics().start();
			}else if(Configuration.neighborhood.equals("panmictic")){
				FactoryGraphStatistics.createPanmicticGraphStatistics().start();
			}
			
		}

	}

}
