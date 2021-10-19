package org.implementation.trucktemplate;

import java.nio.file.Path;
import java.util.stream.IntStream;

import org.optaplanner.core.config.solver.SolverConfig;

public class BothTests {
	private static final String TEST_FOLDER_BOTH = "src/main/resources/org/Both/Tests/";
	
	public static void X_Sections_And_X_Sequences_And_X_Pallets_Test(int seed, int numberOfTests, int timeToSolve, int numberOfSections, int[] loadCapacityOfSections, int numberOfSequences, int numberOfPallets) {
		String testFolder = String.format("%s_Sections_And_%s_Sequences_And_%s_Pallets_Test", numberOfSections, numberOfSequences, numberOfPallets);
		test(testFolder, seed, numberOfTests, timeToSolve, numberOfSections, loadCapacityOfSections, numberOfSequences, numberOfPallets);
	}
	
	public static void X_Sections_And_X_Sequences_And_X_Pallets_Max_Weight_X_Test(int seed, int numberOfTests, int timeToSolve, int numberOfSections, int[] loadCapacityOfSections, int numberOfSequences, int numberOfPallets) {
		String testFolder = String.format("%s_Sections_And_%s_Sequences_And_%s_Pallets_Max_Weight_%s_Test", numberOfSections, numberOfSequences, numberOfPallets, IntStream.of(loadCapacityOfSections).sum());
		test(testFolder, seed, numberOfTests, timeToSolve, numberOfSections, loadCapacityOfSections, numberOfSequences, numberOfPallets);
	}
	
	private static void test(String testFolder, int seed, int numberOfTests, int timeToSolve, int numberOfSections, int[] loadCapacityOfSections, int numberOfSequences, int numberOfPallets) {
		String problemFilePath = Path.of(TEST_FOLDER_BOTH, testFolder, "TruckTemplate%s.txt").toString();
		String solutionFilePathCplex = Path.of(TEST_FOLDER_BOTH, testFolder, "TruckTemplateSolution%sCplex.txt").toString();
		String solutionFilePathOptaplanner = Path.of(TEST_FOLDER_BOTH, testFolder, "TruckTemplateSolution%sOptaplanner.txt").toString();
		
		TruckTemplateGenerator generator = new TruckTemplateGenerator(seed, numberOfSections, numberOfSequences, numberOfPallets, true, loadCapacityOfSections);
		
		for(int i = 1; i < numberOfTests+1; i++) {
			TruckTemplate template = generator.generateTemplate();
			template.saveTemplateAsCPLEXProblemFile(String.format(problemFilePath, i), 1400, timeToSolve);
			
			//Solve with Cplex
			TruckTemplateSolver.solveWithCPLEX(String.format(problemFilePath, i), String.format(solutionFilePathCplex, i), timeToSolve);
			
			//Solve with Optaplanner
			SolverConfig config = TruckTemplateSolver.createConfig(timeToSolve, 16, IntStream.of(loadCapacityOfSections).sum(), 8, 0, 2);
			TruckTemplateSolver.solveWithOptaplannerWithSolver(template.convertToOptaplannerProblem(), config, String.format(solutionFilePathOptaplanner, i), 1400);
		}
		
		TruckTemplateSolver.sumDataFromSolutionFiles(Path.of(TEST_FOLDER_BOTH, testFolder), "ExtractionCplex.txt", "TruckTemplateSolution%sCplex.txt", numberOfTests);
		TruckTemplateSolver.sumDataFromSolutionFiles(Path.of(TEST_FOLDER_BOTH, testFolder), "ExtractionOptaplanner.txt", "TruckTemplateSolution%sOptaplanner.txt", numberOfTests);
	}
}
