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
package geneura.org.config;

import random.CommonState;



public class Configuration {
	
	public static String Algorithm ="SSGA";
	public static boolean minimization = false;
	public static boolean dynamic=false;
	public static boolean dynamic_detection = true;
	public static double rho = -1;
	public static double tau = -1;
	
	public static long seed = 65987;
	public static int chromosome_size = 120;
	
	public static int population_size = 400;
	public static String neighborhood = "FixedPopulation";
	public static String neighborhood_init = "random";
	public static String representation = "binary";
	public static double range_min = -1;
	public static double range_max = -1;
	
	public static String crossover = "geneura.org.operators.binary.TwoPointsCrossover";
	public static double crossover_probability = 1.0;
	public static String mutation = "geneura.org.operators.binary.BitFlipMutation";
	public static double mutation_probability = 0.008;
	public static String selection = "geneura.org.selection.TournamentSelection";
	public static String selection2 = "null";
	public static Double selection2_prob = 0.0;
	public static boolean adaptive_selection = false; 
	public static int selection_param = 2;
	public static int selection_param2 = 8;
	
	public static String replacement = "geneura.org.replacement.ReplaceWorst";
	public static String evaluation = "geneura.samples.ssgaBenchmark.MMDP";
	
	public static long termination_max_evaluation = 1000000;
	public static boolean termination_not_improved = false;
	public static boolean termination_hit_value = false;
	public static boolean termination_hit_value_sleep = false;
	public static double termination_value_is_reached = Double.MIN_VALUE;
	public static long termination_max_cycles = 1000;
		
	
	public static double topology_rewiring = 0.05;
	public static int topology_cache = 20;
	public static boolean nwconverged = false; //True means that newscast has to converge to SW before processing 
	
	
	public static boolean statistic_n_evaluation = true;
	public static boolean statistic_time = true;
	public static boolean statistic_best = true;
	public static boolean statistic_avg = true;
	public static long statistic_sample = 400; 
	
	
	public static boolean churn = false;//It follows a weibull distribution with k=weibull_k and lambda=weibull_lambda
	public static String churnnewindividualpolicy = "random";
	public static String tracefile = "";
	public static double churn_rho = -1;
	public static double churn_tau = -1;
	public static boolean nodefailure = false; //It follows a failure rate from 0 to 100%
	public static int cyclefailure = 20; //G-x-tr It indicates the cycle x when does it begins the failures 0 stands for the inizialization graph
	public static boolean spontaneouspartitioning = false; //Just to test if a spontaneus partitioning of the newscast graph occurs.
    public static boolean informationexchange = false; //To test the information exchange sessions
    public static boolean overhead = false; //Check the overhead of the whole information exchange through a run
    public static int n_processors = 1; //To test the communication overhead between a established number of processors
	
	public static double weibull_k = 0.4;
	public static double weibull_lambda = 20;
	
	
	public static boolean entropy = false; 


	
	
	public static String logfile = "";
	
	public static boolean JUNG = false;
	public static boolean JUNGPAINT = false;
	
	
	public static double UMDAf = 0.1 ;
	public static boolean UMDAaggregative = false ;
	
	
	public static int multiplexer_input_bits = 3;
	public static int multiplexer_output_bits = 8;
	
	public static int parity_nbits = 5;
	public static boolean parity_even = true;
	
	public static int gp_initial_depth = 5;
	public static int gp_tree_depth = 17;
	public static String gp_initer = "geneura.org.operators.tree.RampedHalfAndHalfTreeIniter";
	public static String gp_adf_initer = "geneura.org.operators.tree.ADFIniter";
	public static String gp_data = "geneura.samples.gp.multiplexer.MultiplexerData";
	public static double gp_internalXprobability = 0.9;
	public static boolean gp_print_best = false;
	public static boolean gp_adf = false;
	
	// We can use auxint as an auxiliar int value. Uses:
		// - Number of Peaks in HPPEAK problem
	public static int auxint = 1;
	
	
	
	
	public static void setConfiguration(LoadProperties lp){
		Algorithm = lp.getProperty("algorithm","SSGA");
		minimization = Boolean.valueOf(lp.getProperty("minimization","false")).booleanValue();
		dynamic = Boolean.valueOf(lp.getProperty("dynamic","false")).booleanValue();
		dynamic_detection = Boolean.valueOf(lp.getProperty("dynamicdetection","true")).booleanValue();
		rho = Double.valueOf(lp.getProperty("rho","-1")).doubleValue();
		tau = Double.valueOf(lp.getProperty("tau","-1")).doubleValue();
		

		seed = (lp.getProperty("seed") == null) ? System.currentTimeMillis() : Long.valueOf(lp.getProperty("seed")).longValue();
		chromosome_size = Integer.valueOf(lp.getProperty("chromosomesize", "120")).intValue();
		population_size = Integer.valueOf(lp.getProperty("populationsize", "400")).intValue();
		neighborhood = lp.getProperty("neighborhood", "FixedPopulation");
		neighborhood_init = lp.getProperty("neighborhood.init", "random"); // random, wattsstrogatz
		representation = lp.getProperty("representation", "binary");
		range_min = Double.valueOf(lp.getProperty("range_min","-1")).doubleValue();
		range_max = Double.valueOf(lp.getProperty("range_max","-1")).doubleValue();
		crossover = lp.getProperty("crossover", "geneura.org.operators.binary.TwoPointsCrossover");
		crossover_probability = Double.valueOf(lp.getProperty("crossoverprobability", "1.0")).doubleValue();
		mutation = lp.getProperty("mutation", "geneura.org.operators.binary.BitFlipMutation");
		mutation_probability = Double.valueOf(lp.getProperty("mutationprobability", "0.008")).doubleValue();
		selection = lp.getProperty("selection", "geneura.org.selection.TournamentSelection");
		selection2 = lp.getProperty("selection2", "null");
		adaptive_selection = Boolean.valueOf(lp.getProperty("adaptiveselection", "false"));
		selection2_prob = Double.valueOf(lp.getProperty("selection2prob", "0.0"));
		selection_param = Integer.valueOf(lp.getProperty("selectionparam", "2")).intValue();
		selection_param2 = Integer.valueOf(lp.getProperty("selectionparam2", "8")).intValue();
		replacement = lp.getProperty("replacement", "geneura.org.replacement.ReplaceWorst");
		evaluation = lp.getProperty("evaluation", "geneura.samples.ssgaBenchmark.MMDP");
		
		termination_max_evaluation = Long.valueOf(lp.getProperty("terminationmaxevaluation", "120000")).longValue();
		termination_hit_value = Boolean.valueOf(lp.getProperty("terminationhitvalue", "false")).booleanValue();	
		termination_hit_value_sleep = Boolean.valueOf(lp.getProperty("terminationhitvaluesleep", "false")).booleanValue();
		termination_not_improved = Boolean.valueOf(lp.getProperty("terminationnotimproved", "false")).booleanValue();
		termination_value_is_reached = (lp.getProperty("terminationvalueisreached") == null) ? Double.MIN_VALUE : Double.valueOf(lp.getProperty("terminationvalueisreached")).doubleValue();
		termination_max_cycles = Long.valueOf(lp.getProperty("terminationmaxcycles", "1000")).longValue();

		
		topology_rewiring = Double.valueOf(lp.getProperty("topologyrewiring", "0.05")).doubleValue();
		topology_cache = Integer.valueOf(lp.getProperty("topologycache", "20")).intValue();
		nwconverged = Boolean.valueOf(lp.getProperty("nwconverged", "false")).booleanValue();
		
		statistic_n_evaluation = Boolean.valueOf(lp.getProperty("statisticnevaluation", "true")).booleanValue();
		statistic_time = Boolean.valueOf(lp.getProperty("statistictime", "true")).booleanValue();
		statistic_best = Boolean.valueOf(lp.getProperty("statisticbest", "true")).booleanValue();
		statistic_avg = Boolean.valueOf(lp.getProperty("statisticavg", "true")).booleanValue();
		statistic_sample = Long.valueOf(lp.getProperty("statisticsample", "1000")).longValue();
		
		
		churn = Boolean.valueOf(lp.getProperty("churn", "false")).booleanValue();
		churnnewindividualpolicy = lp.getProperty("churnnewindividualpolicy", "random");
		tracefile = lp.getProperty("tracefile", "");
		churn_rho = Double.valueOf(lp.getProperty("churnrho","-1")).doubleValue();
		churn_tau = Double.valueOf(lp.getProperty("churntau","-1")).doubleValue();
		nodefailure = Boolean.valueOf(lp.getProperty("nodefailure", "false")).booleanValue();
		cyclefailure = Integer.valueOf(lp.getProperty("cyclefailure", "0")).intValue();
		spontaneouspartitioning = Boolean.valueOf(lp.getProperty("spontaneouspartitioning", "false")).booleanValue();
		informationexchange = Boolean.valueOf(lp.getProperty("informationexchange", "false")).booleanValue();
		overhead = Boolean.valueOf(lp.getProperty("overhead", "false")).booleanValue();
		n_processors = Integer.valueOf(lp.getProperty("nprocessors", "1")).intValue();
		
		weibull_k = Double.valueOf(lp.getProperty("weibullk", "0.4")).doubleValue();
		weibull_lambda = Double.valueOf(lp.getProperty("weibulllambda", "20")).doubleValue();
		
		entropy = Boolean.valueOf(lp.getProperty("entropy", "false")).booleanValue();
		
		logfile = lp.getProperty("logfile", "");
		
		JUNG = Boolean.valueOf(lp.getProperty("JUNG", "false")).booleanValue();		
		JUNGPAINT = Boolean.valueOf(lp.getProperty("JUNGPAINT", "false")).booleanValue();
		if(JUNGPAINT)
			JUNG = true;
		
		UMDAaggregative = Boolean.valueOf(lp.getProperty("UMDAaggregative", "false")).booleanValue();
		UMDAf = Double.valueOf(lp.getProperty("UMDAf", "0.1")).doubleValue();
	
		
		gp_initial_depth = Integer.valueOf(lp.getProperty("gpinitialdepth", "6")).intValue();
		gp_tree_depth = Integer.valueOf(lp.getProperty("gptreedepth", "17")).intValue();
		
		gp_initer = lp.getProperty("gpiniter", "geneura.org.operators.tree.RampedHalfAndHalfTreeIniter");
		gp_adf_initer = lp.getProperty("gpadfiniter", "geneura.org.operators.tree.ADFIniter");
		gp_data = lp.getProperty("gpdata", "geneura.samples.gp.multiplexer.MultiplexerData");
		gp_internalXprobability = Double.valueOf(lp.getProperty("internalXprobability", "0.9")).doubleValue();
		gp_print_best = Boolean.valueOf(lp.getProperty("gpprintbest", "false")).booleanValue();
		gp_adf = Boolean.valueOf(lp.getProperty("gpadf", "false")).booleanValue();
		
		multiplexer_input_bits = Integer.valueOf(lp.getProperty("multiplexerinputbits", "3")).intValue();
		multiplexer_output_bits = Integer.valueOf(lp.getProperty("multiplexeroutputbits", "8")).intValue();
		
		parity_nbits = Integer.valueOf(lp.getProperty("paritynbits", "5")).intValue();
		
		parity_even = Boolean.valueOf(lp.getProperty("parityeven", "true")).booleanValue();
		
		
		auxint = Integer.valueOf(lp.getProperty("auxint", "1")).intValue();

	}
	
	

}
