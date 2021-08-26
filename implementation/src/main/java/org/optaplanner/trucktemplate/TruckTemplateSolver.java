
package org.optaplanner.trucktemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiFunction;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

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
	private static final String SOLVER_CONFIG = "org/optaplanner/trucktemplate/truckTemplateSolverConfig.xml";
	private static final String BENCHMARK_CONFIG = "org/optaplanner/trucktemplate/TruckTemplateBenchmark.xml";
	private static final String TEST_FOLDER_OPTAPLANNER = "C:\\Users\\Pascal\\Documents\\GitHub\\TruckTemplateImplementation\\implementation\\src\\main\\java\\org\\optaplanner\\trucktemplate\\Tests\\Optaplanner";
	private static final String TEST_FOLDER_CPLEX = "C:\\Users\\Pascal\\Documents\\GitHub\\TruckTemplateImplementation\\implementation\\src\\main\\java\\org\\optaplanner\\trucktemplate\\Tests\\Cplex";
	
	private static final int wildcardPenelty = 0;
	private static final int timeToSolve = 30;
	
	public static void main(String[] args) {
		TruckTemplateGenerator generator = new TruckTemplateGenerator(0, 16, 5, 9000);
		TruckTemplate template = generator.generateTemplate();
		template.saveTemplateAsCPLEXProblemFile(Path.of(TEST_FOLDER_CPLEX, "TruckTemplateOne.txt").toString(), wildcardPenelty);
		template.saveTemplateAsOptaplannerProblemFile(Path.of(TEST_FOLDER_OPTAPLANNER, "TruckTemplateOne.xml").toString());
		
		String problemFilePath = Path.of(TEST_FOLDER_CPLEX, "TruckTemplateOne.txt").toString();
		String solutionFilePath = Path.of(TEST_FOLDER_CPLEX, "TruckTemplateOneSolution.txt").toString();

		//solveWithCPLEX(problemFilePath, solutionFilePath);
		
		problemFilePath = Path.of(TEST_FOLDER_OPTAPLANNER, "TruckTemplateOne.xml").toString();
		solutionFilePath = Path.of(TEST_FOLDER_OPTAPLANNER, "TruckTemplateOneSolution.txt").toString();
		
		solveWithOptaplanner(new String[]{problemFilePath}, solutionFilePath);
	}
	
	
	
	/* CPLEX */
	
	private static void solveWithCPLEX (String problemFilePath, String solutionFilePath) {
        try {
            IloOplFactory.setDebugMode(false);
            
            IloOplFactory oplF = new IloOplFactory();
            IloOplModelSource modelSource = oplF.createOplModelSource("C:\\Users\\Pascal\\Dropbox\\Informatik\\Studium\\CPLEX Projekte\\TruckTemplates\\TruckTemplateFinal2.mod");

            IloOplErrorHandler errHandler = oplF.createOplErrorHandler(); 
            IloOplSettings settings = oplF.createOplSettings(errHandler);
            IloOplModelDefinition def = oplF.createOplModelDefinition(modelSource,settings);
            
            IloCplex cplex = oplF.createCplex();
            cplex.setParam(IloCplex.Param.DetTimeLimit, timeToSolve*1000);
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
    			
    			output.println();    			
    			output.println("Pallets used: " + palletsUsed);
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
        	System.out.println(ilx.getMessage());
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void sumDataFromCPLEXSolutionFiles (Path folderPath, String sumFileName, String solutionFilesName) {
		BiFunction<String, String, String> getTextAfterSearch = (text, search) -> (text.indexOf(search) != -1) ? text.substring(text.indexOf(search) + search.length()) : "";
		File file = new File(folderPath.toString());
		file.mkdir();
		Path sumFilePath = Path.of(folderPath.toString(), sumFileName);
		
		try {
			Files.writeString(file.toPath(), "Test Pallets Weight Objective Time Wildcards U-Value\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (FileWriter writer = new FileWriter(sumFilePath.toString())) {
			for(int i = 1; i < file.listFiles().length+1; i++)
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
						writer.write(getTextAfterSearch.apply(line, "U-Value: "));
				}
				writer.write("\n");
	 		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	/* Optaplanner */
	
	private static void solveWithOptaplanner (String problemFilePaths[], String solutionFilePath) {
		//SolverFactory<TruckTemplateSolution> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
		//Solver<TruckTemplateSolution> solver = solverFactory.buildSolver();
		TruckTemplateSolution.setWildcardPenaltyValue(wildcardPenelty);
		
		
		PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource(BENCHMARK_CONFIG);
		
		TruckTemplateXMLSolutioneFileIO xstream = new TruckTemplateXMLSolutioneFileIO();
		
		TruckTemplateSolution[] unsolved = new TruckTemplateSolution[problemFilePaths.length];
		for(int i = 0; i < problemFilePaths.length; i++) {
			unsolved[i] = xstream.read(new File(problemFilePaths[i]));
		}

		PlannerBenchmark benchmark = benchmarkFactory.buildPlannerBenchmark(unsolved);
		benchmark.benchmarkAndShowReportInBrowser();
	}	
	
	private static void sumDataFromOptaplannerSolutionFiles (String folderName, String sumFileName) {
	}
}