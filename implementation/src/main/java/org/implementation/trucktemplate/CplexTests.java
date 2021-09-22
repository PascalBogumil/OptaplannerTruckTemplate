package org.implementation.trucktemplate;

import java.nio.file.Path;

public class CplexTests {
	private static final String TEST_FOLDER_CPLEX = "src/main/resources/org/Cplex/Tests/";

	public static void X_Tests_Pro_UValue_X_bis_X_X_Sekunden_Pro_Test(int seed, int testsPerValue, int timeToSolve, int startWildcardValue, int endWildcardValue) {
		String testFolder = String.format("KalibrierungCPLEX\\%s_Tests_Pro_UValue_%s_bis_%s_%s_Sekunden_Pro_Test", testsPerValue, startWildcardValue, endWildcardValue, timeToSolve);
		String problemFilePath = Path.of(TEST_FOLDER_CPLEX, testFolder, "TruckTemplate%s.txt").toString();
		String solutionFilePathCplex = Path.of(TEST_FOLDER_CPLEX, testFolder, "TruckTemplateSolution%s.txt").toString();
		
		TruckTemplateGenerator generator = new TruckTemplateGenerator(seed, 16, 5, 90, true, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000});

		int i = 1;
		while(startWildcardValue < endWildcardValue) {
			TruckTemplate template = generator.generateTemplate();
			template.saveTemplateAsCPLEXProblemFile(String.format(problemFilePath, i), startWildcardValue, timeToSolve);
			
			//Solve with Cplex
			TruckTemplateSolver.solveWithCPLEX(String.format(problemFilePath, i), String.format(solutionFilePathCplex, i), timeToSolve);
			
			if(i++ % testsPerValue == 0) {
				generator = new TruckTemplateGenerator(seed, 16, 5, 90, true, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000});
				startWildcardValue += 200;
			}
		}
		
		TruckTemplateSolver.sumDataFromSolutionFiles(Path.of(TEST_FOLDER_CPLEX, testFolder), "Extraction.txt", "TruckTemplateSolution%s.txt", i-1);
	}
}
