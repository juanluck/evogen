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
*/package geneura.org.graphs;

import java.util.Collection;
import java.util.Set;

import random.CommonState;
import samples.preview_new_graphdraw.iter.UpdatableIterableLayout;

import edu.uci.ics.jung.algorithms.cluster.ClusterSet;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.ArchetypeGraph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseVertex;
import geneura.org.Algorithm;
import geneura.org.config.Configuration;
import geneura.org.config.Logger;
import geneura.org.evoag.EVAG;
import geneura.org.individuals.Individual;
import geneura.org.neighborhood.INeighborhood;
import geneura.org.neighborhood.demes.FixedPopulation;
import geneura.org.neighborhood.graphs.NodeNeighborhoodNewscast;
import geneura.org.termination.Termination;

public class StandAloneNewscast extends Algorithm{
	
	public StandAloneNewscast() {
		super();
	}
	
	public StandAloneNewscast(String name) {
		super(name);
	}
	
	
	public void run() {
		
		Individual aux[] = new Individual[population.getSize()];

		if(Configuration.informationexchange){
			
			for (int j=0;j<Configuration.cyclefailure;j++){ //0 means that there is no convergence
				((NodeNeighborhoodNewscast)population.getNeighborhood(0))._nws.reset_information_exchange();
				for (int i=0; i < population.getSize(); i++){
					INeighborhood neigh = population.getNeighborhood(i);
					((EVAG)neigh.executeNeighborhoodPolicy()).information_exchange++;	
				}
				Logger.append("", ""+((EVAG)population.getNeighborhood(0).executeNeighborhoodPolicy()).information_exchange+"\n");	
			}
		}else if(Configuration.overhead){
			
			for (int j=0;j<Configuration.cyclefailure;j++){ //0 means that there is no convergence

				((NodeNeighborhoodNewscast)population.getNeighborhood(0))._nws.reset_information_exchange();


				for (int i=0; i < population.getSize(); i++){
					INeighborhood neigh = population.getNeighborhood(i);
					neigh.executeNeighborhoodPolicy();

				}
				long overheadcount=0;
				long agentscount=0;
				for (int i=0; i < population.getSize(); i++){
					
					NodeNeighborhoodNewscast neigh = (NodeNeighborhoodNewscast)population.getNeighborhood(i);
					int nodeexchange =neigh.getEvAg().information_exchange;
					int agentexchange=neigh.getEvAg().agent_exchange;
					overheadcount += nodeexchange;
					agentscount += agentexchange;
				}
				
				Logger.append("", ""+overheadcount+" "+agentscount+"\n");	
			}
		}else if(Configuration.spontaneouspartitioning){//Just to print spontaneouspartitioning statistics
			
			
			for (int j=0;j<Configuration.cyclefailure;j++){ //0 means that there is no convergence
				for (int i=0; i < population.getSize(); i++){
					INeighborhood neigh = population.getNeighborhood(i);
					neigh.getBestIndividual();	
				}
				
				int n_clusters=0;
				if(j%500==0){
					((NodeNeighborhoodNewscast)population.getNeighborhood(0))._nws.repaintgraph();
					UndirectedSparseGraph G = (UndirectedSparseGraph) population.getNeighborhood(0).getGraph();
					WeakComponentClusterer wcc = new WeakComponentClusterer();
					ClusterSet cl = wcc.extract(G);
					
					n_clusters = cl.size();
				}
					
				
				if(n_clusters>1){
					Logger.append("", j+" P\n");
					System.exit(1);
				}
			}
			Logger.append("", Configuration.cyclefailure+" F\n");
			

		}else if (Configuration.nodefailure){		// Just to print the system degradation from 0% to 100% of the nodes
			for (int j=0;j<Configuration.cyclefailure;j++){ //0 means that there is no convergence
				for (int i=0; i < population.getSize(); i++){
					INeighborhood neigh = population.getNeighborhood(i);
					neigh.getBestIndividual();	
				}
			}
			((NodeNeighborhoodNewscast)population.getNeighborhood(0))._nws.repaintgraph();
			UndirectedSparseGraph G = (UndirectedSparseGraph) population.getNeighborhood(0).getGraph();
			
			int graph_size = G.numVertices();
			int one_percent = (int) graph_size / 100;

			Object[] vertices = G.getVertices().toArray();

			graph_size = G.numVertices();

			//Erasing 50% of the graph
			for (int j=0;j<(one_percent*50);j++){
				int vertex_index = CommonState.r.nextInt(graph_size);

				while (vertices[vertex_index]==null){
					vertex_index = CommonState.r.nextInt(graph_size);
				}
				G.removeVertex((Vertex) vertices[vertex_index]);
				vertices[vertex_index]=null;
			}

			
			//Processing first graph as a cluster
			WeakComponentClusterer wcc = new WeakComponentClusterer();
			ClusterSet cl = wcc.extract(G);
			
			cl.sort();

			Logger.append("", "50 "+cl.size()+" "+cl.getCluster(0).size()+"\n");

			
			//Removing vertices
			for (int i=50;i<99;i++){
				vertices = G.getVertices().toArray();

				graph_size = G.numVertices();
				for (int j=0;j<one_percent;j++){
					int vertex_index = CommonState.r.nextInt(graph_size);

					while (vertices[vertex_index]==null){
						vertex_index = CommonState.r.nextInt(graph_size);
					}
					G.removeVertex((Vertex) vertices[vertex_index]);
					vertices[vertex_index]=null;
				}
				
				//Processing clusters
				wcc = new WeakComponentClusterer();
				cl = wcc.extract(G);
				
				cl.sort();

				Logger.append("", (i+1)+" "+cl.size()+" "+cl.getCluster(0).size()+"\n");
				
			}
			
			
		}else{ //Usual running of the standalone mode
			//We wait 10 cycles for convergence to SW
			if(Configuration.nwconverged)
				for (int j=0;j<10;j++){
					for (int i=0; i < population.getSize(); i++){
						INeighborhood neigh = population.getNeighborhood(i);
						neigh.getBestIndividual();	
					}
				}
			
			while(!termination.isFinish()){
				
				
				for (int i=0; i < population.getSize(); i++){
					
					//Take the neighborhood for node i
					INeighborhood neigh = population.getNeighborhood(i);
					
					//Iteration for a single individual
					aux[i] = (Individual)selection.select(neigh).clone();
					evaluator.evaluate(aux[i]);
					
					//Execute newscast
					neigh.getBestIndividual();
					notifyObserver();	
				}
				
				//Replacement
				for (int i=0; i < population.getSize(); i++){
					Individual current_ind = population.getIndividual(i);
					if (aux[i].better(current_ind)){
						population.setIndividual(i, aux[i]);
					}
					
				}
				
				

			}
			notifyObserver();
		}
			
		}
		
	
}
