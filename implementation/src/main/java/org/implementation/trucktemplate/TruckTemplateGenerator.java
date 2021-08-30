package org.implementation.trucktemplate;

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

import org.kie.api.task.model.OrganizationalEntity;

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
	
	/* Parameter für die Generierung des Templates */
	private final int numberOfSections; // Anzahl der Sektion
	private final int maximumSequence; // Anzahl der Maximal möglichen Sequenzen pro Sektion
	private final int numberOfPallets; // Anzahl der zur Verfügung stehenden Paletten
	private final boolean allSectionsMaxSequences; // True, wenn jede Section  'maximaleSequenzen' Sequenzen haben soll	
	private final int[] loadCapacityOfSections; // Maximales Gewicht aller Sektionen
	private final Random rand; // Generator
	
	public TruckTemplateGenerator (int seed, int numberOfSections, int maximumSequence, int numberOfPallets, boolean allSectionsMaxSequences) {
		this(seed, numberOfSections, maximumSequence, numberOfPallets, allSectionsMaxSequences, new int[] {1000, 1000, 1000, 1500, 1500, 2000, 2000, 2000, 2000, 2000, 2000, 1500, 1500, 1000, 1000, 1000});
	}
	
	/**
	 * Konstruktor, in welchem die Parameter Instanziert werden
	 * 
	 * @param seed Seed für den Generator
	 * @param anzahlSektionen Anzahl der Sektionen
	 * @param maximaleSequenzen Anzahl der Maximal möglichen Sequenzen pro Sektion
	 * @param anzahlPaletten Anzahl der zur Verfügung stehenden Paletten
	 * @param allSectionsMaxSequences True, wenn jede Section  'maximaleSequenzen' Sequenzen haben soll, sonst false
	 * @param tragfaehigkeitJederSektion Maximales Gewicht aller Sektionen
	 */
	public TruckTemplateGenerator (int seed, int numberOfSections, int maximumSequence, int numberOfPallets, boolean allSectionsMaxSequences, int[] loadCapacityOfSections) {
		this.rand = new Random(seed);
		this.numberOfSections = numberOfSections;
		this.maximumSequence = maximumSequence;
		this.numberOfPallets = numberOfPallets;
		this.loadCapacityOfSections = loadCapacityOfSections;
		this.allSectionsMaxSequences = allSectionsMaxSequences;
	}
	
	/**
	 * Generiert das Template mit den gegebenen Parametern
	 * 
	 * @return Generiertes {@link TruckTemplate}
	 */
	public TruckTemplate generateTemplate () {
		//Generierung der Anzahl der Jeweiligen Paletten
		List<Integer> countOfTypes = generateNumbersWithSum(numberOfPallets, TruckTemplate.getNumberOfTypes());		
		TruckTemplate template = new TruckTemplate(numberOfSections, maximumSequence, countOfTypes);
		
		//Truck Template Generierung
		generateMaxRowWeight(template);
		generatePalletCounts(template);
		generatePallets(template);
		
		return template;
	}
	
	/**
	 * Generiert für jede Reihe die Tragfähigkeit aller Sequenzen
	 * 
	 * @param {@link TruckTemplate} Instanz, in welche die Daten gespeichert werden
	 */
	private void generateMaxRowWeight(TruckTemplate template) {		
		for(int k = 0; k < template.getNumberOfSections(); k++) {
			int sequencesCount = (allSectionsMaxSequences) ? template.getMaximumSequences() : rand.nextInt(template.getMaximumSequences())+1;
			for(int l = 0; l < sequencesCount; l++)
				template.getSequenceLoadCapacityForRows()[k][l] = loadCapacityOfSections[k];
		}

	}

	/**
	 * Generiert für jede Reihe die geforderte Palettenanzahl aller Sequenzen, hierbei wird diese Palettenanzahl so generiert,
	 * dass die summierte Größe aller geforderten Paletten einer Sequenz nicht den Platz eine Wildcard überschreitet.
	 * 
	 * @param {@link TruckTemplate} Instanz, in welche die Daten gespeichert werden
	 */
	private void generatePalletCounts(TruckTemplate template) {		
		for(int k = 0; k < template.getNumberOfSections(); k++)
			for(int l = 0; l < template.getMaximumSequences(); l++) {	
				int sum = 0;
				while (sum < TruckTemplate.getWildcardSpace()) {
					int newType = rand.nextInt(TruckTemplate.getNumberOfTypes());
					if(sum + TruckTemplate.getNormalizedValueOfType(newType) <= TruckTemplate.getWildcardSpace()) {
						template.getSequencePalletsForRows()[k][l][newType]++;
						sum += TruckTemplate.getNormalizedValueOfType(newType);
					}
				}
			}
	}

	/**
	 * Generiert die Paletten gemäß der vorher generierten Anzahl jedes Palettentyps. Dabei wird das maximal mögliche Gewicht eine Palettentype nicht überschritten.
	 * Jede Palette hat mindestens ein Gewicht von 1.
	 * 
	 * @param {@link TruckTemplate} Instanz, in welche die Daten gespeichert werden
	 */
	private void generatePallets (TruckTemplate template) {		
		int maxWeight = Arrays.stream(template.getSequenceLoadCapacityForRows()).flatMapToInt(Arrays::stream).max().getAsInt();

		for (int i = 0; i < TruckTemplate.getNumberOfTypes(); i++)
			for (int j = 0; j < template.getMaximumTypeCount(); j++)
					template.getPalletWeights()[j][i] = (j < template.getCountOfType(i)) ? rand.nextInt(TruckTemplate.getMaxWeightOfType(i))+1 : maxWeight+1;
	}
	
	/**
	 * Zieht die spezifizierte Anzahl von Zahlen, welche aufsummiert die spezifizierte Summe ergeben.
	 * 
	 * @param summedDraws Summierte Anzahl aller gezogenen zahlen
	 * @param drawCount Anzahl der Züge
	 * @return Liste der gezogenen Zahlen
	 */
	private List<Integer> generateNumbersWithSum (int summedDraws, int drawCount) {
	    List<Integer> draws = new ArrayList<>();

	    int currentSummedDraws = 0;
	    for (int i = 0; i < drawCount; i++) {
	        int next = (summedDraws == 0) ? 0 : rand.nextInt(summedDraws) + 1;
	        draws.add(next);
	        currentSummedDraws += next;
	    }
	    
	    double scale = 1d * summedDraws / currentSummedDraws;
	    currentSummedDraws = 0;
	    for (int i = 0; i < drawCount; i++) {
	    	draws.set(i, (int) (draws.get(i) * scale));
	    	currentSummedDraws += draws.get(i);
	    }

	    while(currentSummedDraws++ < summedDraws) {
	        int i = rand.nextInt(drawCount);
	        draws.set(i, draws.get(i) + 1);
	    }
	    
	    return draws;
	}	
}
