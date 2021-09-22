package org.implementation.trucktemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.optaplanner.benchmark.config.PlannerBenchmarkConfig;
import org.optaplanner.benchmark.config.ProblemBenchmarksConfig;
import org.optaplanner.benchmark.config.SolverBenchmarkConfig;
import org.optaplanner.benchmark.config.ranking.SolverRankingType;
import org.optaplanner.benchmark.config.report.BenchmarkReportConfig;
import org.optaplanner.benchmark.config.statistic.ProblemStatisticType;
import org.optaplanner.benchmark.config.statistic.SingleStatisticType;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.heuristic.selector.move.factory.MoveIteratorFactoryConfig;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.localsearch.decider.acceptor.LocalSearchAcceptorConfig;
import org.optaplanner.core.config.localsearch.decider.forager.LocalSearchForagerConfig;
import org.optaplanner.core.config.localsearch.decider.forager.LocalSearchPickEarlyType;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;

import ilog.concert.IloException;
import ilog.concert.IloIntMap;
import ilog.opl.IloCplex;
import ilog.opl.IloOplDataSource;
import ilog.opl.IloOplErrorHandler;
import ilog.opl.IloOplFactory;
import ilog.opl.IloOplModel;
import ilog.opl.IloOplModelDefinition;
import ilog.opl.IloOplModelSource;
import ilog.opl.IloOplSettings;

public class TruckTemplateSolver {
	//private static final String SOLVER_CONFIG = "org/implementation/trucktemplate/truckTemplateSolverConfig.xml";
	//private static final String BENCHMARK_CONFIG = "org/implementation/trucktemplate/TruckTemplateBenchmark.xml";
	
	private static final String TEST_FOLDER_OPTAPLANNER = "src/main/resources/org/Optaplanner/Tests/";
	private static final String TEST_FOLDER_CPLEX = "src/main/resources/org/Cplex/Tests/";
	private static final String ILP_CPLEX = "src/main/resources/org/Cplex/ILP.mod";
	
	public static void main(String[] args) {
		//Wildcard U-Value Test
		//CplexTests.X_Tests_Pro_UValue_X_bis_X_X_Sekunden_Pro_Test(4437995, 20, 10, 0, 3000); //U-Value 0 - 3000
		//CplexTests.X_Tests_Pro_UValue_X_bis_X_X_Sekunden_Pro_Test(443799, 40, 120, 1000, 2000); //U-Value 1000 - 2000
		//CplexTests.X_Tests_Pro_UValue_X_bis_X_X_Sekunden_Pro_Test(44379, 40, 10, 0, 1400); //U-Value 0 - 1400
		//CplexTests.X_Tests_Pro_UValue_X_bis_X_X_Sekunden_Pro_Test(4437, 40, 120, 0, 1400); //U-Value 0 - 1400
		
		//Entity and value tabu test
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(170, 10, 4, 10, 0, 0);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(170, 10, 4, 10, 0, 2);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(170, 10, 4, 10, 2, 0);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(170, 10, 4, 10, 2, 2);
		
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(440, 40, 4, 30, 0, 0);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(440, 40, 4, 30, 0, 2);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(440, 40, 4, 30, 2, 0);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(440, 40, 4, 30, 2, 2);
		
		//Wildcard U-Value test
		//OptaplannerTests.X_Tests_Pro_UValue_X_Sekunden_Pro_Test(360, 40, 200, 10);
		//OptaplannerTests.X_Tests_Pro_UValue_X_Sekunden_Pro_Test(360, 40, 200, 120);
		
		//Comparison Sections
		/*BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(6, 50, 10, 1, new int[] {1000}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(7, 50, 10, 4, new int[] {1000, 1500, 1500, 1000}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(8, 50, 10, 8, new int[] {1000, 1500, 1500, 2000, 2000, 1500, 1500, 1000}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(9, 50, 10, 12, new int[] {1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(10, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(11, 50, 10, 20, new int[] {1000, 1000, 1000, 1000, 1500, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1500, 1000, 1000, 1000, 1000}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(12, 50, 10, 24, new int[] {1000, 1000, 1000, 1000, 1500, 1500, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1500, 1500, 1000, 1000, 1000, 1000}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(13, 50, 10, 28, new int[] {1000, 1000, 1000, 1000, 1000, 1500, 1500, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1500, 1500, 1000, 1000, 1000, 1000, 1000}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(14, 50, 10, 32, new int[] {1000, 1000, 1000, 1000, 1000, 1000, 1500, 1500, 1500, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1500, 1500, 1500, 1000, 1000, 1000, 1000, 1000, 1000}, 5, 90);
		
		//Comparison Sequences
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(0, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 1, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(1, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 2, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(2, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 4, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(3, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 6, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(4, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 8, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(5, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 10, 90);
		
		//Comparison Pallets
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(15, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 10);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(16, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 20);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(17, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 40);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(18, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 60);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(19, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 80);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(20, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 100);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(21, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 120);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(22, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 140);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(23, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 160);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(24, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 180);

		//Comparison Section Weights
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Max_Weight_X_Test(25, 50, 10, 16, new int[] {250, 250, 250, 375, 375, 500, 500, 500, 500, 500, 500, 375, 375, 250, 250, 250}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Max_Weight_X_Test(26, 50, 10, 16, new int[] {500, 500, 500, 750, 750, 1000, 1000, 1000, 1000, 1000, 1000, 750, 750, 500, 500, 500}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Max_Weight_X_Test(27, 50, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Max_Weight_X_Test(28, 50, 10, 16, new int[] {2000, 2000, 2000, 3000, 3000, 4000, 4000, 4000, 4000, 4000, 4000, 3000, 3000, 2000, 2000, 2000}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Max_Weight_X_Test(29, 50, 10, 16, new int[] {4000, 4000, 4000, 6000, 6000, 8000, 8000, 8000, 8000, 8000, 8000, 6000, 6000, 4000, 4000, 4000}, 5, 90);*/
	
		//Comparison Sections with same pallet/section ratio
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(40, 250, 10, 8, new int[] {1000, 1500, 1500, 2000, 2000, 1500, 1500, 1000}, 5, 45);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(41, 250, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 90);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(42, 250, 10, 24, new int[] {1000, 1000, 1000, 1000, 1500, 1500, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1500, 1500, 1000, 1000, 1000, 1000}, 5, 135);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(43, 250, 10, 32, new int[] {1000, 1000, 1000, 1000, 1000, 1500, 1500, 1500, 1500, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1500, 1500, 1500, 1500, 1000, 1000, 1000, 1000, 1000}, 5, 180);
		
		//Comparison Sequences with same pallet/sequence ratio
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(44, 250, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 1, 18);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(45, 250, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 2, 36);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(46, 250, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 4, 72);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(47, 250, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 6, 108);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(48, 250, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 8, 144);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Test(49, 250, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 10, 180);
		
		//Comparison Section Weights with 360 pallets
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Max_Weight_X_Test(50, 250, 10, 16, new int[] {250, 250, 250, 375, 375, 500, 500, 500, 500, 500, 500, 375, 375, 250, 250, 250}, 5, 360);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Max_Weight_X_Test(51, 250, 10, 16, new int[] {500, 500, 500, 750, 750, 1000, 1000, 1000, 1000, 1000, 1000, 750, 750, 500, 500, 500}, 5, 360);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Max_Weight_X_Test(52, 250, 10, 16, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000}, 5, 360);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Max_Weight_X_Test(53, 250, 10, 16, new int[] {2000, 2000, 2000, 3000, 3000, 4000, 4000, 4000, 4000, 4000, 4000, 3000, 3000, 2000, 2000, 2000}, 5, 360);
		BothTests.X_Sections_And_X_Sequences_And_X_Pallets_Max_Weight_X_Test(54, 250, 10, 16, new int[] {4000, 4000, 4000, 6000, 6000, 8000, 8000, 8000, 8000, 8000, 8000, 6000, 6000, 4000, 4000, 4000}, 5, 360);
	}
	
	
	
	/* CPLEX */
	
	public static void solveWithCPLEX (String problemFilePath, String solutionFilePath, int timeToSolve) {
        try {
            IloOplFactory.setDebugMode(false);
            
            IloOplFactory oplF = new IloOplFactory();
            IloOplModelSource modelSource = oplF.createOplModelSource(new File(ILP_CPLEX).getAbsolutePath());

            IloOplErrorHandler errHandler = oplF.createOplErrorHandler(); 
            IloOplSettings settings = oplF.createOplSettings(errHandler);
            IloOplModelDefinition def = oplF.createOplModelDefinition(modelSource,settings);
            
            IloCplex cplex = oplF.createCplex();
            cplex.setParam(IloCplex.Param.DetTimeLimit, timeToSolve*1000);
            cplex.setParam(IloCplex.Param.Advance, 0);
            cplex.setOut(System.out);
            
            IloOplModel opl = oplF.createOplModel(def, cplex);
            IloOplDataSource dataSource = oplF.createOplDataSource(problemFilePath);
            opl.addDataSource(dataSource);
            
            opl.generate();          
            
            if (cplex.solve())
            {               
            	PrintStream output = new PrintStream(solutionFilePath);

            	IloIntMap dVarY = opl.getElement("y").asIntMap();
                IloIntMap dVarW = opl.getElement("w").asIntMap();
                IloIntMap dVarX = opl.getElement("x").asIntMap();
                IloIntMap dVarV = opl.getElement("v").asIntMap();
                IloIntMap varPallets = opl.getElement("weight").asIntMap();
                int u = opl.getElement("U").asInt();
                
            	int palletsUsed = 0;
            	int summedWeight = 0;
            	int wildcards = 0;
            	
    			for (int k = 1; k < dVarX.getSize()+1; k++) {
    				for (int l = 1; l < dVarX.getSub(k).getSize()+1; l++) {
    					if(dVarY.getSub(k).get(l) == 1)
    					{
    						output.print(String.format("Row: %s, Sequenz %s, Pallets: ", k, l));
                			for(int j = 1; j < dVarX.getSub(k).getSub(l).getSize()+1; j++) {
                		    	for(int i = 1; i < dVarX.getSub(k).getSub(l).getSub(j).getSize()+1; i++) {
                		    		if(dVarX.getSub(k).getSub(l).getSub(j).get(i) == 1) {
                		    			palletsUsed += dVarX.getSub(k).getSub(l).getSub(j).get(i);
                		    			summedWeight += varPallets.getSub(j).get(i);
                		    			output.print(String.format("(%s %s %s) ", j, i, varPallets.getSub(j).get(i)));

                		    		}
                		    	}
                		    }
                			output.println();
    					}
    				}
    				
    				if(dVarW.get(k) == 1) {
    					output.print(String.format("Row: %s, Wildcard, Pallets: ", k));
    					wildcards++;

            			for(int j = 1; j < dVarV.getSub(k).getSize()+1; j++) {
            		    	for(int i = 1; i < dVarV.getSub(k).getSub(j).getSize()+1; i++) {
            		    		if(dVarV.getSub(k).getSub(j).get(i) == 1) {
            		    			palletsUsed += dVarV.getSub(k).getSub(j).get(i);
            		    			summedWeight += varPallets.getSub(j).get(i);
            		    			output.print(String.format("(%s %s %s) ", j, i, varPallets.getSub(j).get(i)));
            		    		}
            		    	}
            		    }
            			output.println();
    		    	}
    			}
    			
    			output.println("\nPallets used: " + palletsUsed);
    			output.println("Weight used: " + summedWeight);
    			output.println("OBJECTIVE: " + opl.getCplex().getObjValue());
    			output.println("Computation Time: " + opl.getCplex().getDetTime()/1000);
    			output.println("Wildcards used: " + wildcards);
    			output.println("U-Value: " + u);
                //System.setOut(System.out);
                output.close();
                //opl.postProcess();
                //opl.printSolution(System.out);
            }
            else {
                System.out.println("No solution!");
            }
            oplF.end();
        } catch (IloException ilx) {
        	ilx.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Optaplanner */
	
	public static void solveWithOptaplannerWithSolver (TruckTemplateSolution unsolved, SolverConfig config, String solutionFilePath, int wildcardPenelty) {
		TruckTemplateSolution.setWildcardPenaltyValue(wildcardPenelty);
		
		if(config.getPhaseConfigList().isEmpty()) {
			System.err.println("Solver is not configured");
		}

		//Start solver
		SolverFactory<TruckTemplateSolution> solverFactory = SolverFactory.create(config);
		Solver<TruckTemplateSolution> solver = solverFactory.buildSolver();
		TruckTemplateSolution solved = solver.solve(unsolved);
		

		try (PrintStream output = new PrintStream(solutionFilePath)) {
			for(OpRow row : solved.getOpRows()) {
				output.print(String.format("Row: %s, %s, Pallets: ", (row.getId()+1), (row.getCurrentSequence().isWildcard()) ? "Wildcard" : "Sequenz: " + (row.getCurrentSequence().getId()+1)));
				for(OpPallet pallet : row.getOpPallets()) {
					output.print(String.format("(%s %s) ", pallet.getType(), pallet.getWeight()));
				}
				output.println();
			}
			
			output.println("\nPallets used: " + solved.getOpRows().stream().mapToInt(r -> r.getOpPallets().size()).sum());
			output.println("Weight used: " + solved.getOpRows().stream().mapToInt(r -> r.getCurrentWeight()).sum());
			output.println("OBJECTIVE: " + solved.getScore().getSoftScore());
			output.println("Computation Time: " + config.getTerminationConfig().getSecondsSpentLimit());
			output.println("Wildcards used: " + solved.getOpRows().stream().filter(r -> r.getCurrentSequence().isWildcard()).count());
			output.println("U-Value: " + wildcardPenelty);
			output.println("Accepted Count Limit: " + ((LocalSearchPhaseConfig)config.getPhaseConfigList().get(0)).getForagerConfig().getAcceptedCountLimit());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static SolverConfig createConfig(int timeToSolve, int numberOfSections, int maxPossibleWeight, int acceptedCountLimit, int entityTabuSize, int valueTabusize) {
		//Config
		SolverConfig config = new SolverConfig();
		config.setRandomSeed(0L);
		config.setSolutionClass(TruckTemplateSolution.class);
		config.setEntityClassList(Arrays.asList(OpPallet.class, OpRow.class));
		
		//Score calculator
		ScoreDirectorFactoryConfig sdfc = new ScoreDirectorFactoryConfig();
		sdfc.setConstraintProviderClass(TruckTemplateConstraints.class);
		config.setScoreDirectorFactoryConfig(sdfc);

		//Termination config
		TerminationConfig tc = new TerminationConfig();
		tc.setSecondsSpentLimit((long)timeToSolve);
		config.setTerminationConfig(tc);
		
		//LocalSearch phase
		LocalSearchPhaseConfig pc = new LocalSearchPhaseConfig();
		
		//LocalSearch Termination
		tc = new TerminationConfig();
		//tc.setUnimprovedSecondsSpentLimit(20L);
		//tc.setStepCountLimit(1000);
		pc.setTerminationConfig(tc);

		//MoveIteratorFactory
		MoveIteratorFactoryConfig mifc = new MoveIteratorFactoryConfig();
		mifc.setMoveIteratorFactoryClass(OpRowMoveIteratorFactory.class);
		//mifc.setSelectedCountLimit(10L);
		pc.setMoveSelectorConfig(mifc);

		//Acceptor
		LocalSearchAcceptorConfig lsac = new LocalSearchAcceptorConfig();
		lsac.setEntityTabuSize(entityTabuSize);
		lsac.setValueTabuSize(valueTabusize);
		lsac.setSimulatedAnnealingStartingTemperature(numberOfSections + "hard/" + maxPossibleWeight + "soft");
		pc.setAcceptorConfig(lsac);
		
		//Forager
		LocalSearchForagerConfig lsfc = new LocalSearchForagerConfig();
		lsfc.setAcceptedCountLimit(acceptedCountLimit);
		//lsfc.setPickEarlyType(LocalSearchPickEarlyType.FIRST_BEST_SCORE_IMPROVING);
		pc.setForagerConfig(lsfc);

		//Set LocalSearch phase to config
		config.setPhaseConfigList(Arrays.asList(pc));
		
		return config;
	}
		
	/* Both */
	
	public static void sumDataFromSolutionFiles (Path folderPath, String sumFileName, String solutionFilesName, int numberOfFiles) {
		BiFunction<String, String, String> getTextAfterSearch = (text, search) -> (text.indexOf(search) != -1) ? text.substring(text.indexOf(search) + search.length()) : "";
		File file = new File(folderPath.toString());
		file.mkdir();
		Path sumFilePath = Path.of(folderPath.toString(), sumFileName);

		try (FileWriter writer = new FileWriter(sumFilePath.toString())) {
			//writer.write("Test Pallets Weight Objective Time Wildcards U-Value AcceptedCountLimit\n");
			writer.write("Test Pallets Weight Objective Time Wildcards\n");
			for(int i = 1; i < numberOfFiles+1; i++)
			{		
				writer.write(i + " ");
				Path solutionFilePath = Path.of(folderPath.toString(), String.format(solutionFilesName, i));
				List<String> lines = Files.readAllLines(solutionFilePath);
				for(String line : lines) {
					if(getTextAfterSearch.apply(line, "Pallets used: ") != "")
						writer.write(getTextAfterSearch.apply(line, "Pallets used: ") + " ");
					else if(getTextAfterSearch.apply(line, "Weight used: ") != "")
						writer.write(getTextAfterSearch.apply(line, "Weight used: ") + " ");
					else if(getTextAfterSearch.apply(line, "OBJECTIVE: ") != "")
						writer.write(getTextAfterSearch.apply(line, "OBJECTIVE: ") + " ");
					else if(getTextAfterSearch.apply(line, "Computation Time: ") != "")
						writer.write(getTextAfterSearch.apply(line, "Computation Time: ") + " ");
					else if(getTextAfterSearch.apply(line, "Wildcards used: ") != "")
						writer.write(getTextAfterSearch.apply(line, "Wildcards used: ") + " ");
					/*else if(getTextAfterSearch.apply(line, "U-Value: ") != "")
						writer.write(getTextAfterSearch.apply(line, "U-Value: ") + " ");
					else if(getTextAfterSearch.apply(line, "Accepted Count Limit: ") != "")
						writer.write(getTextAfterSearch.apply(line, "Accepted Count Limit: "));*/
				}
				writer.write("\n");
	 		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}