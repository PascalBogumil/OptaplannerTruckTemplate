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
	
	//private static int wildcardPenelty = 1400;
	//private static long timeToSolve = 300;
	
	public static void main(String[] args) {
		/*// Test 1
		int seed = 1401;
		int numberofTemplates = 240;
		wildcardPenelty = 1000;
		
		TruckTemplate.setWildcardWeight(1200);
		TruckTemplateGenerator generator = new TruckTemplateGenerator(seed, 16, 5, 90, true);
		
		String problemFilePath = Path.of(TEST_FOLDER_CPLEX, "KalibrierungCPLEX\\40TestsProUWert1000Bis2000120Sekunden\\TruckTemplate%s.txt").toString();
		String solutionFilePath = Path.of(TEST_FOLDER_CPLEX, "KalibrierungCPLEX\\40TestsProUWert1000Bis2000120Sekunden\\TruckTemplateSolution%s.txt").toString();

		for(int i = 1; i < numberofTemplates+1; i++) {
			TruckTemplate template = generator.generateTemplate();
			template.saveTemplateAsCPLEXProblemFile(String.format(problemFilePath, i), wildcardPenelty);
			
			if(i % 40 == 0) {
				generator = new TruckTemplateGenerator(seed, 16, 5, 90, true);
				wildcardPenelty += 200;
			}
		}
		
		for(int i = 1; i < numberofTemplates+1; i++) {
			solveWithCPLEX(String.format(problemFilePath, i), String.format(solutionFilePath, i));
		}
		
		// Test 2
		seed = 2018;
		numberofTemplates = 240;
		wildcardPenelty = 1800;
		
		TruckTemplate.setWildcardWeight(2000);
		generator = new TruckTemplateGenerator(seed, 16, 5, 90, true);
		
		problemFilePath = Path.of(TEST_FOLDER_CPLEX, "KalibrierungCPLEX\\40TestsProUWert1800Bis2800120Sekunden\\TruckTemplate%s.txt").toString();
		solutionFilePath = Path.of(TEST_FOLDER_CPLEX, "KalibrierungCPLEX\\40TestsProUWert1800Bis2800120Sekunden\\TruckTemplateSolution%s.txt").toString();
		
		for(int i = 1; i < numberofTemplates+1; i++) {
			TruckTemplate template = generator.generateTemplate();
			template.saveTemplateAsCPLEXProblemFile(String.format(problemFilePath, i), wildcardPenelty);
			
			if(i % 40 == 0) {
				generator = new TruckTemplateGenerator(seed, 16, 5, 90, true);
				wildcardPenelty += 200;
			}
		}
		
		for(int i = 1; i < numberofTemplates+1; i++) {
			solveWithCPLEX(String.format(problemFilePath, i), String.format(solutionFilePath, i));
		}*/
		
		//sumDataFromCPLEXSolutionFiles(Path.of(TEST_FOLDER_CPLEX, "KalibrierungCPLEX\\40TestsProUWert1000Bis2000120Sekunden"), "Extraction.txt", "TruckTemplateSolution%s.txt", 240);
		//sumDataFromCPLEXSolutionFiles(Path.of(TEST_FOLDER_CPLEX, "KalibrierungCPLEX\\40TestsProUWert1800Bis2800120Sekunden"), "Extraction.txt", "TruckTemplateSolution%s.txt", 240);
		
		/*TruckTemplateGenerator generator = new TruckTemplateGenerator(156, 16, 5, 90, true);
		TruckTemplate template = generator.generateTemplate();
		
		String problemFilePath = Path.of(TEST_FOLDER_CPLEX, "TruckTemplateOne.txt").toString();
		String solutionFilePath = Path.of(TEST_FOLDER_CPLEX, "TruckTemplateOneSolutionCplex.txt").toString();

		template.saveTemplateAsCPLEXProblemFile(problemFilePath, wildcardPenelty);
		//solveWithCPLEX(problemFilePath, solutionFilePath);
		
		problemFilePath = Path.of(TEST_FOLDER_OPTAPLANNER, "TruckTemplateOne.xml").toString();
		solutionFilePath = Path.of(TEST_FOLDER_OPTAPLANNER, "TruckTemplateOneSolutionOptaplanner.txt").toString();
		
		template.saveTemplateAsOptaplannerProblemFile(problemFilePath);*/
		//solveWithOptaplanner(new String[]{problemFilePath}, solutionFilePath);
		
		//Entity and value tabu test
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_Wert_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(170, 10, 4, 10, 0, 0);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_Wert_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(170, 10, 4, 10, 0, 2);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_Wert_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(170, 10, 4, 10, 2, 0);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_Wert_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(170, 10, 4, 10, 2, 2);
		
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(440, 40, 4, 30, 0, 0);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(440, 40, 4, 30, 0, 2);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(440, 40, 4, 30, 2, 0);
		//OptaplannerTests.X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(440, 40, 4, 30, 2, 2);
		
		//Wildcard U-Value test
		//OptaplannerTests.X_Tests_Pro_UValue_X_Sekunden_Pro_Test(360, 40, 200, 10);
		OptaplannerTests.X_Tests_Pro_UValue_X_Sekunden_Pro_Test(360, 40, 200, 120);
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
            cplex.setParam(IloCplex.Param.DetTimeLimit, timeToSolve);
            cplex.setParam(IloCplex.Param.Advance, 0);
            cplex.setOut(null);
            
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
		pc.setTerminationConfig(tc);

		//MoveIteratorFactory
		MoveIteratorFactoryConfig mifc = new MoveIteratorFactoryConfig();
		mifc.setMoveIteratorFactoryClass(OpRowMoveIteratorFactory.class);
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
			writer.write("Test Pallets Weight Objective Time Wildcards U-Value AcceptedCountLimit\n");
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
					else if(getTextAfterSearch.apply(line, "U-Value: ") != "")
						writer.write(getTextAfterSearch.apply(line, "U-Value: ") + " ");
					else if(getTextAfterSearch.apply(line, "Accepted Count Limit: ") != "")
						writer.write(getTextAfterSearch.apply(line, "Accepted Count Limit: "));
				}
				writer.write("\n");
	 		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}