package org.implementation.trucktemplate;

import java.nio.file.Path;
import java.util.stream.IntStream;

import org.optaplanner.core.config.solver.SolverConfig;

public class OptaplannerTests {
	private static final String TEST_FOLDER_OPTAPLANNER = "src/main/resources/org/Optaplanner/Tests/";
	
	public static void X_Tests_Pro_AcceptedCountLimit_X_Sekunden_Pro_Test_EntityTabu_X_und_Value_Tabu_X(int numberOfTests, int testsPerValue, int valueSteps, int timeToSolve, int entityTabuSize, int valueTabuSize) {
		String testFolder = String.format("KalibrierungOptaplanner\\%s_Tests_Pro_AcceptedCountLimit_%s_Sekunden_Pro_Test_EntityTabu_%s_und_ValueTabu_%s", testsPerValue, timeToSolve, entityTabuSize, valueTabuSize);
		String problemFilePath = Path.of(TEST_FOLDER_OPTAPLANNER, testFolder, "TruckTemplate%s.txt").toString();
		String solutionFilePath = Path.of(TEST_FOLDER_OPTAPLANNER, testFolder, "TruckTemplateSolution%s.txt").toString();
		int wildcardPenelty = 1400;
		int numberOfSections = 16;
		int[] loadCapacityOfSections = new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000};
		int seed = 0;
		
		TruckTemplateGenerator generator = new TruckTemplateGenerator(seed, numberOfSections, 5, 90, true, loadCapacityOfSections);
		int acceptedCountLimit = 1;
		
		for(int i = 1; i < numberOfTests+1; i++) {
			TruckTemplate template = generator.generateTemplate();
			template.saveTemplateAsCPLEXProblemFile(String.format(problemFilePath, i), wildcardPenelty, timeToSolve);
			SolverConfig config = TruckTemplateSolver.createConfig(timeToSolve, numberOfSections, IntStream.of(loadCapacityOfSections).sum(), acceptedCountLimit, entityTabuSize, valueTabuSize);
			TruckTemplateSolver.solveWithOptaplannerWithSolver(template.convertToOptaplannerProblem(), config, String.format(solutionFilePath, i), wildcardPenelty);
			
			if(i % testsPerValue == 0) {
				generator = new TruckTemplateGenerator(seed, numberOfSections, 5, 90, true, loadCapacityOfSections);
				if(acceptedCountLimit == 1)
					acceptedCountLimit += valueSteps-1;
				else
					acceptedCountLimit += valueSteps;
			}
			
		}
		
		TruckTemplateSolver.sumDataFromSolutionFiles(Path.of(TEST_FOLDER_OPTAPLANNER, testFolder), "Extraction.txt", "TruckTemplateSolution%s.txt", numberOfTests);
	}
	
	public static void X_Tests_Pro_UValue_X_Sekunden_Pro_Test(int numberOfTests, int testsPerValue, int valueSteps, int timeToSolve) {
		String testFolder = String.format("KalibrierungOptaplanner\\%s_Tests_Pro_UValue_%s_Sekunden_Pro_Test", testsPerValue, timeToSolve);
		String problemFilePath = Path.of(TEST_FOLDER_OPTAPLANNER, testFolder, "TruckTemplate%s.txt").toString();
		String solutionFilePath = Path.of(TEST_FOLDER_OPTAPLANNER, testFolder, "TruckTemplateSolution%s.txt").toString();
		int wildcardPenelty = 1400;
		int numberOfSections = 16;
		int[] loadCapacityOfSections = new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000};
		int seed = 1;
		
		TruckTemplateGenerator generator = new TruckTemplateGenerator(seed, numberOfSections, 5, 90, true, loadCapacityOfSections);
		int acceptedCountLimit = 8;
		
		for(int i = 1; i < numberOfTests+1; i++) {
			TruckTemplate template = generator.generateTemplate();
			template.saveTemplateAsCPLEXProblemFile(String.format(problemFilePath, i), wildcardPenelty, timeToSolve);
			SolverConfig config = TruckTemplateSolver.createConfig(timeToSolve, numberOfSections, IntStream.of(loadCapacityOfSections).sum(), acceptedCountLimit, 2, 0);
			TruckTemplateSolver.solveWithOptaplannerWithSolver(template.convertToOptaplannerProblem(), config, String.format(solutionFilePath, i), wildcardPenelty);
			
			if(i % testsPerValue == 0) {
				generator = new TruckTemplateGenerator(seed, numberOfSections, 5, 90, true, loadCapacityOfSections);
				wildcardPenelty += valueSteps;
			}
			
		}
		
		TruckTemplateSolver.sumDataFromSolutionFiles(Path.of(TEST_FOLDER_OPTAPLANNER, testFolder), "Extraction.txt", "TruckTemplateSolution%s.txt", numberOfTests);	
	}
}
