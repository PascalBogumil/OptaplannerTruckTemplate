package org.optaplanner.trucktemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

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

public class TruckTemplateGenerator {
	
	/* Dateipfad */
	//private static final String rootPath = "C:\\Users\\Pascal\\Dropbox\\Informatik\\Studium\\Bachelorarbeit\\Tests\\";
	//private static String fileName;
	//private static String fileNameSolution;
	//private static String fileNameExtraction;
	
	/* Parameter für die Generierung des Templates */
	private int anzahlSektionen = 16;
	private int maximaleSequenzen = 5;
	//private static int anzahlTypen = 6; //Fest	
	//private static int maximalerTyp; //a = max(typenAnzahl[i]) Wird bei der Generierung der Paletten festgelegt
	
	//Wildcard
	//private static int wildcardGewicht = 1200; //Fest
	//private static int wildcardPlatz = 12; //Fest
	//private static int wildcardStrafe = 0;
	
	private int anzahlPaletten = 90;
	
	//Abhängig von der Sektionenanzahl
	private int[] tragfaehigkeitJederSektion; //Maximales Gewicht aller Sektionen, wenn die selbe Sequenz ausgewählt
	
	//Fest
	//private static List<Integer> maxWeightEachType = Arrays.asList(new Integer[]{850, 750, 375, 375, 750, 850});
	//private static int[] normalizedValueOfType = new int[] {4, 2, 1, 1, 2, 4};	
	
	/* Parameter zum Einstellen des ILP */
	//private int seed;
	private Random rand; //12
	//private static int time = 120;
	
	/* Hilfsvariablen für Generierung */
	//private static List<Integer> typenAnzahl;
	//private static int[][] palettenGewichte;
	//private static int[][] sequenzTragfaehigkeitJederReihe;
	//private static int[][][] sequenzPalettenJederReihe;
	
	public TruckTemplateGenerator (int seed, int anzahlSektionen, int maximaleSequenzen, int anzahlPaletten) {
		this(seed, anzahlSektionen, maximaleSequenzen, anzahlPaletten, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000});
	}
	
	public TruckTemplateGenerator (int seed, int anzahlSektionen, int maximaleSequenzen, int anzahlPaletten, int[] tragfaehigkeitJederSektion) {
		//this.seed = seed;
		this.rand = new Random(seed);
		this.anzahlPaletten = anzahlPaletten;
		this.tragfaehigkeitJederSektion = tragfaehigkeitJederSektion;
	}
	
	public TruckTemplate generateTemplate () {
		List<Integer> typenAnzahl = generateNumbersWithSum(anzahlPaletten, TruckTemplate.getNumberOfTypes());		
		TruckTemplate template = new TruckTemplate(anzahlSektionen, maximaleSequenzen, typenAnzahl);
		
		generateMaxRowWeight(template);
		generatePalletCounts(template);
		generatePallets(template);
		
		return template;
	}
	
	private void generateMaxRowWeight(TruckTemplate template) {		
		for(int k = 0; k < template.getAnzahlSektionen(); k++)
			for(int l = 0; l < template.getMaximaleSequenzen(); l++)
				template.getSequenzTragfaehigkeitJederReihe()[k][l] = tragfaehigkeitJederSektion[k];
	}

	private void generatePalletCounts(TruckTemplate template) {		
		for(int k = 0; k < template.getAnzahlSektionen(); k++)
			for(int l = 0; l < template.getMaximaleSequenzen(); l++) {	
				int sum = 0;
				while (sum < TruckTemplate.getWildcardSpace()) {
					int newType = rand.nextInt(TruckTemplate.getNumberOfTypes());
					if(sum + TruckTemplate.getNormalizedValueOfType(newType) <= TruckTemplate.getWildcardSpace()) {
						template.getSequenzPalettenJederReihe()[k][l][newType]++;
						sum += TruckTemplate.getNormalizedValueOfType(newType);
					}
				}
			}
	}

	private void generatePallets (TruckTemplate template) {		
		int maxWeight = Arrays.stream(template.getSequenzTragfaehigkeitJederReihe()).flatMapToInt(Arrays::stream).max().getAsInt();

		for (int i = 0; i < TruckTemplate.getNumberOfTypes(); i++)
			for (int j = 0; j < template.getMaximalerTyp(); j++)
				if(j < template.getTypenAnzahl(i))
					template.getPalettenGewichte()[j][i] = rand.nextInt(TruckTemplate.getMaxWeightOfType(i))+1;
				else
					template.getPalettenGewichte()[j][i] = maxWeight+1;
	}
	
	private List<Integer> generateNumbersWithSum (int summed, int draws) {
	    List<Integer> typeCount = new ArrayList<>();

	    int numberOfPalletsDrawn = 0;
	    for (int i = 0; i < draws; i++) {
	        int next = (summed == 0) ? 0 : rand.nextInt(summed) + 1;
	        typeCount.add(next);
	        numberOfPalletsDrawn += next;
	    }
	    
	    double scale = 1d * summed / numberOfPalletsDrawn;
	    numberOfPalletsDrawn = 0;
	    for (int i = 0; i < draws; i++) {
	    	typeCount.set(i, (int) (typeCount.get(i) * scale));
	    	numberOfPalletsDrawn += typeCount.get(i);
	    }

	    while(numberOfPalletsDrawn++ < summed) {
	        int i = rand.nextInt(draws);
	        typeCount.set(i, typeCount.get(i) + 1);
	    }
	    
	    return typeCount;
	}	
}
