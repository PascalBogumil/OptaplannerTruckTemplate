package org.optaplanner.trucktemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TruckTemplate {

	//Fest
	private static final int numberOfTypes = 6; //Fest
	private static final int wildcardWeight = 1200; //Fest
	private static final int wildcardSpace = 12; //Fest
	private static final int[] normalizedValueOfType = new int[] {4, 2, 1, 1, 2, 4}; //Fest
	private static final int[] maxWeightEachType = new int[]{850, 750, 375, 375, 750, 850}; //Fest
	
	private final int[][] palettenGewichte;
	private final int[][] sequenzTragfaehigkeitJederReihe;
	private final int[][][] sequenzPalettenJederReihe;
	
	private final int anzahlSektionen;
	private final int maximaleSequenzen;
	private final List<Integer> anzahlProTyp;
	private final int maximalerTyp;
	
	public TruckTemplate(int anzahlSektionen, int maximaleSequenzen, List<Integer> typenAnzahl) {
		this.anzahlSektionen = anzahlSektionen;
		this.maximaleSequenzen = maximaleSequenzen;
		this.anzahlProTyp = typenAnzahl;
		this.maximalerTyp = Collections.max(typenAnzahl);
		
		palettenGewichte = new int[maximalerTyp][numberOfTypes];
		sequenzTragfaehigkeitJederReihe = new int[anzahlSektionen][maximaleSequenzen];
		sequenzPalettenJederReihe = new int[anzahlSektionen][maximaleSequenzen][numberOfTypes];
	}
	
	public static int getNumberOfTypes() {
		return numberOfTypes;
	}
	
	public static int getWildcardWeight() {
		return wildcardWeight;
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
	
	public int getAnzahlSektionen() {
		return anzahlSektionen;
	}

	public int getMaximaleSequenzen() {
		return maximaleSequenzen;
	}

	public int getTypenAnzahl(int type) {
		return anzahlProTyp.get(type);
	}
	
	public int getMaximalerTyp() {
		return maximalerTyp;
	}
	
	public int[][] getPalettenGewichte() {
		return palettenGewichte;
	}

	public int[][] getSequenzTragfaehigkeitJederReihe() {
		return sequenzTragfaehigkeitJederReihe;
	}

	public int[][][] getSequenzPalettenJederReihe() {
		return sequenzPalettenJederReihe;
	}	
	
	public void saveTemplateAsCPLEXProblemFile(String filePath, int wildcardStrafe) {
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		
		try (FileWriter writer = new FileWriter(file)){
			writer.write("maxPallet = " + maximalerTyp + ";\n");
			writer.write("rows = " + anzahlSektionen + ";\n");
			writer.write("maxSequences = " + maximaleSequenzen + ";\n");
			
			writer.write("M = " + wildcardWeight + ";\n");
			writer.write("N = " + wildcardSpace + ";\n");
			writer.write("U = " + wildcardStrafe + ";\n");
			
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
			for (int j = 0; j < palettenGewichte.length; j++)
			{
				writer.write("[");
				for (int i = 0; i < palettenGewichte[j].length; i++)
				{
					writer.write(String.valueOf(palettenGewichte[j][i]));
					if(i+1 != palettenGewichte[j].length) writer.write(",");
				}
				writer.write("]");
				if(j+1 != palettenGewichte.length) writer.write(",\n");
			}
			writer.write("];\n");
			
			//Write PalletCounts
			writer.write("palletCounts = \n[");
			for (int k = 0; k < sequenzPalettenJederReihe.length; k++)
			{
				writer.write("[");
				for (int l = 0; l < sequenzPalettenJederReihe[k].length; l++)
				{
					writer.write("[");
					for (int i = 0; i < sequenzPalettenJederReihe[k][l].length; i++)
					{
						writer.write(String.valueOf(sequenzPalettenJederReihe[k][l][i]));
						if(i+1 != sequenzPalettenJederReihe[k][l].length) writer.write(",");
					}
					writer.write("]");
					if(l+1 != sequenzPalettenJederReihe[k].length) writer.write(",");
				}
				writer.write("]");
				if(k+1 != sequenzPalettenJederReihe.length) writer.write(",\n");
			}
			writer.write("];\n");
			
			//Write MaxWeight
			writer.write("maxWeightPerColumn = \n[");	
			for (int k = 0; k < sequenzTragfaehigkeitJederReihe.length; k++)
			{
				writer.write("[");
				for (int l = 0; l < sequenzTragfaehigkeitJederReihe[k].length; l++)
				{
					writer.write(String.valueOf(sequenzTragfaehigkeitJederReihe[k][l]));
					if(l+1 != sequenzTragfaehigkeitJederReihe[k].length) writer.write(",");
				}
				writer.write("]");
				if(k+1 != sequenzTragfaehigkeitJederReihe.length) writer.write(",\n");
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
		for(int k = 0; k < anzahlSektionen; k++) {
			List<OpSequence> sequences = new ArrayList<>();
			for(int l = 0; l < maximaleSequenzen; l++) {
				int sumOfWantedPallets = Arrays.stream(sequenzPalettenJederReihe[k][l]).sum();
				int weight = sequenzTragfaehigkeitJederReihe[k][l];
				if(sumOfWantedPallets != 0 && weight != 0) {		
					int[] typen = new int[numberOfTypes];
					for(int i = 0; i < numberOfTypes; i++) {
						typen[i] = sequenzPalettenJederReihe[k][l][i];
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
		for(int j = 0; j < maximalerTyp; j++) {
			for(int i = 0; i < numberOfTypes; i++) {
				if(palettenGewichte[j][i] <= Arrays.stream(sequenzTragfaehigkeitJederReihe).flatMapToInt(Arrays::stream).max().getAsInt())
					pallets.add(new OpPallet(palletCounter++, palettenGewichte[j][i], i));
			}
		}
				
		TruckTemplateSolution unsolved = new TruckTemplateSolution();
		unsolved.setOpRows(sections);
		unsolved.setOpPallets(pallets);
		
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		
		TruckTemplateXMLSolutioneFileIO xstream = new TruckTemplateXMLSolutioneFileIO();
		xstream.write(unsolved, file);
	}
}
