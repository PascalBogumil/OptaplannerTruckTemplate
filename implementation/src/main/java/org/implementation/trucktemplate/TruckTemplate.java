package org.implementation.trucktemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TruckTemplate {

	//Fixed Parameters for each Truck Template
	private static final int numberOfTypes = 6;
	private static int wildcardWeight = 1200;
	private static final int wildcardSpace = 12;
	private static final int[] normalizedValueOfType = new int[] {4, 2, 1, 1, 2, 4};
	private static final int[] maxWeightEachType = new int[]{850, 750, 375, 375, 750, 850};

	//Parameter of Truck Template
	private final int numberOfSections;
	private final int maximumSequences;
	private final List<Integer> countOfTypes;
	private final int maximumTypeCount;
	
	//Truck Template
	private final int[][] palletWeights;
	private final int[][] sequenceLoadCapacityForRows;
	private final int[][][] sequencePalletsForRows;
	
	public TruckTemplate(int numberOfSections, int maximumSequences, List<Integer> countOfTypes) {
		this.numberOfSections = numberOfSections;
		this.maximumSequences = maximumSequences;
		this.countOfTypes = countOfTypes;
		this.maximumTypeCount = Collections.max(countOfTypes);
		
		palletWeights = new int[maximumTypeCount][numberOfTypes];
		sequenceLoadCapacityForRows = new int[numberOfSections][maximumSequences];
		sequencePalletsForRows = new int[numberOfSections][maximumSequences][numberOfTypes];
	}
	
	public static int getNumberOfTypes() {
		return numberOfTypes;
	}
	
	public static int getWildcardWeight() {
		return wildcardWeight;
	}
	
	public static int setWildcardWeight(int weight) {
		return wildcardWeight = weight;
	}

	public static int getWildcardSpace() {
		return wildcardSpace;
	}
	
	public static int getNormalizedValueOfType(int type) {
		return normalizedValueOfType[type];
	}
	
	public static int getMaxWeightOfType(int type) {
		return maxWeightEachType[type];
	}
	
	public int getNumberOfSections() {
		return numberOfSections;
	}

	public int getMaximumSequences() {
		return maximumSequences;
	}

	public int getCountOfType(int type) {
		return countOfTypes.get(type);
	}
	
	public int getMaximumTypeCount() {
		return maximumTypeCount;
	}
	
	public int[][] getPalletWeights() {
		return palletWeights;
	}

	public int[][] getSequenceLoadCapacityForRows() {
		return sequenceLoadCapacityForRows;
	}

	public int[][][] getSequencePalletsForRows() {
		return sequencePalletsForRows;
	}	
	
	public void saveTemplateAsCPLEXProblemFile(String filePath, int wildcardPenelty) {
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		
		try (FileWriter writer = new FileWriter(file)){
			writer.write("maxPallet = " + maximumTypeCount + ";\n");
			writer.write("rows = " + numberOfSections + ";\n");
			writer.write("maxSequences = " + maximumSequences + ";\n");
			
			writer.write("M = " + wildcardWeight + ";\n");
			writer.write("N = " + wildcardSpace + ";\n");
			writer.write("U = " + wildcardPenelty + ";\n");
			
			//Write NormalizedValueOfTyp;
			writer.write("normalizedValueOfType = [");
			for (int i = 0; i < normalizedValueOfType.length; i++)
			{
				writer.write(String.valueOf(normalizedValueOfType[i]));
				if(i+1 != normalizedValueOfType.length) writer.write(",");
			}
			writer.write("];\n");
				
			//Write Pallets
			writer.write("weight = \n[");	
			for (int j = 0; j < palletWeights.length; j++)
			{
				writer.write("[");
				for (int i = 0; i < palletWeights[j].length; i++)
				{
					writer.write(String.valueOf(palletWeights[j][i]));
					if(i+1 != palletWeights[j].length) writer.write(",");
				}
				writer.write("]");
				if(j+1 != palletWeights.length) writer.write(",\n");
			}
			writer.write("];\n");
			
			//Write PalletCounts
			writer.write("palletCounts = \n[");
			for (int k = 0; k < sequencePalletsForRows.length; k++)
			{
				writer.write("[");
				for (int l = 0; l < sequencePalletsForRows[k].length; l++)
				{
					writer.write("[");
					for (int i = 0; i < sequencePalletsForRows[k][l].length; i++)
					{
						writer.write(String.valueOf(sequencePalletsForRows[k][l][i]));
						if(i+1 != sequencePalletsForRows[k][l].length) writer.write(",");
					}
					writer.write("]");
					if(l+1 != sequencePalletsForRows[k].length) writer.write(",");
				}
				writer.write("]");
				if(k+1 != sequencePalletsForRows.length) writer.write(",\n");
			}
			writer.write("];\n");
			
			//Write MaxWeight
			writer.write("maxWeightPerColumn = \n[");	
			for (int k = 0; k < sequenceLoadCapacityForRows.length; k++)
			{
				writer.write("[");
				for (int l = 0; l < sequenceLoadCapacityForRows[k].length; l++)
				{
					writer.write(String.valueOf(sequenceLoadCapacityForRows[k][l]));
					if(l+1 != sequenceLoadCapacityForRows[k].length) writer.write(",");
				}
				writer.write("]");
				if(k+1 != sequenceLoadCapacityForRows.length) writer.write(",\n");
			}
			writer.write("];");
		} catch (IOException e) {
			System.out.println("An error occurred.");
		    e.printStackTrace();
		}
	}
	
	public void saveTemplateAsOptaplannerProblemFile(String filePath) {
		//Convert sections
		List<OpRow> sections = new ArrayList<>();
		for(int k = 0; k < numberOfSections; k++) {
			List<OpSequence> sequences = new ArrayList<>();
			for(int l = 0; l < maximumSequences; l++) {
				int sumOfWantedPallets = Arrays.stream(sequencePalletsForRows[k][l]).sum();
				int weight = sequenceLoadCapacityForRows[k][l];
				if(sumOfWantedPallets != 0 && weight != 0) {		
					int[] typen = new int[numberOfTypes];
					for(int i = 0; i < numberOfTypes; i++) {
						typen[i] = sequencePalletsForRows[k][l][i];
					}
					
					sequences.add(new OpSequence(l, weight, typen));
				}
			}
			sequences.add(new OpSequence(sequences.size()+1));
			sections.add(new OpRow(k, sequences));	
		}
		
		//Convert pallets
		int palletCounter = 0;
		List<OpPallet> pallets = new ArrayList<>();
		for(int j = 0; j < maximumTypeCount; j++) {
			for(int i = 0; i < numberOfTypes; i++) {
				if(palletWeights[j][i] <= Arrays.stream(sequenceLoadCapacityForRows).flatMapToInt(Arrays::stream).max().getAsInt())
					pallets.add(new OpPallet(palletCounter++, palletWeights[j][i], i));
			}
		}
				
		TruckTemplateSolution unsolved = new TruckTemplateSolution(sections, pallets);
		
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		
		TruckTemplateXMLSolutioneFileIO xstream = new TruckTemplateXMLSolutioneFileIO();
		xstream.write(unsolved, file);
	}
}
