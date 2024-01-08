/*
EvoGen, Evolutionary Geneura is a framework for simulating distributed evolutionary computation experiments
Copyright (C) 2008 Junta de Andalucia CICE project P06-TIC-02025

This file is part of EvoGen.

EvoGen is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

EvoGen is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with EvoGen; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

contact:  http://geneura.ugr.es, https://forja.rediris.es/svn/geneura/evogen
*/
package geneura.org;

import geneura.org.config.Configuration;
import geneura.org.config.LoadProperties;
import geneura.org.factories.FactoryEVAG;
import geneura.org.factories.FactoryEVAGChurn;
import geneura.org.factories.FactoryEVAGseq;
import geneura.org.factories.FactoryEVAGseqChurn;
import geneura.org.factories.FactoryEVAGseqGP;
import geneura.org.factories.FactoryEVAGseqUMDA;
import geneura.org.factories.FactoryGGA;
import geneura.org.factories.FactoryGGAChurn;
import geneura.org.factories.FactoryGGAGP;
import geneura.org.factories.FactoryGGAGPChurn;
import geneura.org.factories.FactorySSGA;
import geneura.org.factories.FactorycGA;

public class EvoGen {
	
	public static void main(String[] args) {
	
		
		LoadProperties lp = new LoadProperties(args);
		Configuration.setConfiguration(lp);

		if (Configuration.Algorithm.equals("SSGA")){
			if (Configuration.representation.equals("binary")){
				FactorySSGA.createBinarySSGA().run();
			}
		}else if (Configuration.Algorithm.equals("GGA")){
			if (Configuration.representation.equals("binary")){
				FactoryGGA.createBinaryGGA().run();
			}else if (Configuration.representation.equals("real")){
				FactoryGGA.createDoubleGGA().run();
			} 
		}else if(Configuration.Algorithm.equals("GGAChurn")){
			if (Configuration.representation.equals("binary")){
				FactoryGGAChurn.createBinaryGGA().run();
			}
			
		}else if (Configuration.Algorithm.equals("EVAG")){
			if (Configuration.representation.equals("binary")){
				if(Configuration.neighborhood.equals("panmictic")){
					FactoryEVAG.createBinaryPanEVAG();
				}else if(Configuration.neighborhood.equals("wattsstrogatz")){
					FactoryEVAG.createBinaryWatssStrogatzEVAG();
				}else if(Configuration.neighborhood.equals("newscast")){
					if (Configuration.termination_max_cycles != 1000000){
						
						FactoryEVAGChurn.createBinaryNewscastEVAG();
						System.err.println("With Churn, cycles: "+Configuration.termination_max_cycles);
					}				
					else{
						FactoryEVAG.createBinaryNewscastEVAG();
						System.err.println("Without Churn");
					}
				}
			}else if(Configuration.representation.equals("real")){
				if(Configuration.neighborhood.equals("newscast")){
					FactoryEVAG.createDoubleNewscastEVAG();
				}
			}
		}else if (Configuration.Algorithm.equals("EVAGseq")){
			if (Configuration.representation.equals("binary")){
				if(Configuration.neighborhood.equals("newscast")){
					FactoryEVAGseq.createNewscastEVAGseq().start();
				} else if(Configuration.neighborhood.equals("wattsstrogatz")){
					FactoryEVAGseq.createWattsStrogatzEVAGseq().start();
				}
			}
		}else if (Configuration.Algorithm.equals("EVAGseqChurn")){
			if (Configuration.representation.equals("binary")){
				if(Configuration.neighborhood.equals("newscast")){
					FactoryEVAGseqChurn.createNewscastEVAGseq().start();
				} 
			}
		}else  if (Configuration.Algorithm.equals("EVAGseqUMDA")){
			if (Configuration.representation.equals("binary")){
				if(Configuration.neighborhood.equals("newscast")){
						FactoryEVAGseqUMDA.createNewscastEVAGseqUMDA().run();
				} 
			}
		}else if(Configuration.Algorithm.equals("cGA")){
			if (Configuration.representation.equals("binary")){
				FactorycGA.createBinarycGA().run();
			}
		}else if(Configuration.Algorithm.equals("EVAGseqGP")){
			if(Configuration.neighborhood.equals("newscast")){
				if(Configuration.churn){
					if (!Configuration.gp_adf)
						FactoryEVAGseqGP.createNewscastEVAGseqGPChurn().run();
					
					//else
					//	FactoryEVAGseqGP.createNewscastEVAGseqGPADF().run();
				}else{
					if (!Configuration.gp_adf)
						FactoryEVAGseqGP.createNewscastEVAGseqGP().run();
					else
						FactoryEVAGseqGP.createNewscastEVAGseqGPADF().run();	
				}
				
			}
		}else if(Configuration.Algorithm.equals("GGAGP")){
			FactoryGGAGP.createGGAGP().run();
		}else if(Configuration.Algorithm.equals("GGAGPChurn")){
			FactoryGGAGPChurn.createGGAGP().run();
		}
				
	}

}
