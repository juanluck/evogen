package geneura.org;

import java.io.IOException;

import geneura.org.config.Configuration;
import geneura.org.config.LoadProperties;
import geneura.org.factories.FactoryEVAG;
import geneura.org.factories.FactoryEVAGChurn;
import geneura.org.factories.FactoryGGA;
import geneura.org.factories.FactorySSGA;
import geneura.org.factories.FactoryTAKEOVER;

public class TakeOverTime {

	public static void main(String[] args) {


		LoadProperties lp = new LoadProperties(args);
		Configuration.setConfiguration(lp);

		Configuration.Algorithm="TAKEOVER";
		//Configuration.statistic_sample = 1600;
		Configuration.representation = "binary";
		//Configuration.population_size = 1600;
		Configuration.minimization=false;
		Configuration.chromosome_size=10;
		//Configuration.termination_max_evaluation=1000000;
		Configuration.evaluation = "geneura.samples.traps.Trap2";
		Configuration.termination_hit_value=false;
		

		//Configuration.setConfiguration(lp);

		if (Configuration.Algorithm.equals("TAKEOVER")){
			if(Configuration.neighborhood.equals("panmictic")){
					FactoryTAKEOVER.createBinaryPanTakeOver();
			}else if(Configuration.neighborhood.equals("wattsstrogatz")){
					FactoryTAKEOVER.createBinaryWatssStrogatzTakeOver();
			}else if(Configuration.neighborhood.equals("newscast")){
					FactoryTAKEOVER.createBinaryNewscastTakeOver();
			}
			
		}
		
	}
}
