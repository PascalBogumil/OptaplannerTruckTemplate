
package org.optaplanner.trucktemplate;

import static java.util.Comparator.comparingInt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

public class TruckTemplateSolver {
	public static final String SOLVER_CONFIG = "org/optaplanner/trucktemplate/truckTemplateSolverConfig.xml";
	
	public static void main(String[] args) {
		SolverFactory<TruckTemplateSolution> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
		Solver<TruckTemplateSolution> solver = solverFactory.buildSolver();
		/*int[][] pallets = {{288,239,89,275,478,786},
		                   {376,389,24,293,6,492},
		                   {123,259,98,40,417,25},
		                   {662,359,128,288,566,753},
		                   {420,585,203,186,280,416},
		                   {621,567,349,0,90,547},
		                   {111,559,326,0,478,168},
		                   {606,197,247,0,601,645},
		                   {294,147,99,0,134,243},
		                   {739,133,208,0,178,706},
		                   {115,730,94,0,514,202},
		                   {168,382,364,0,0,75},
		                   {463,73,272,0,0,337},
		                   {103,609,276,0,0,194},
		                   {223,198,106,0,0,146},
		                   {330,39,185,0,0,443},
		                   {473,491,23,0,0,522},
		                   {0,85,251,0,0,662},
		                   {0,172,0,0,0,91},
		                   {0,0,0,0,0,155}};
		
		int[][][] palletCounts = {{{0,1,3,1,1,0},{2,0,1,1,0,1},{1,2,2,0,0,1},{0,3,1,1,0,2},{1,2,0,2,1,1}},
		                          {{1,1,1,0,0,2},{0,1,0,1,0,2},{0,3,0,0,1,3},{1,0,1,1,0,3},{2,2,1,0,0,3}},
		                          {{3,0,0,1,1,1},{1,1,1,1,0,1},{0,2,2,1,0,2},{1,2,0,0,1,0},{2,1,0,1,1,0}},
		                          {{1,1,1,0,1,1},{0,2,3,0,1,0},{1,2,0,0,2,0},{1,2,0,1,3,2},{0,2,2,0,1,2}},
		                          {{1,0,1,1,1,3},{1,2,2,0,0,2},{1,1,0,3,1,2},{1,2,2,0,1,2},{1,1,0,1,0,2}},
		                          {{0,2,0,0,1,2},{0,0,1,1,2,1},{2,3,1,1,0,2},{1,2,1,0,0,0},{2,0,2,0,0,2}},
		                          {{1,2,1,0,0,2},{2,3,0,0,1,1},{1,1,1,0,2,0},{1,1,0,0,0,1},{1,4,0,0,0,2}},
		                          {{2,2,0,0,0,3},{1,0,0,0,0,1},{2,1,1,0,1,3},{1,2,1,0,0,0},{1,1,1,0,0,3}},
		                          {{0,1,0,2,0,2},{2,2,1,2,1,3},{2,0,0,2,0,1},{0,0,0,2,1,3},{0,2,1,0,0,0}},
		                          {{0,1,1,1,0,0},{2,1,2,0,1,1},{1,2,1,1,0,1},{1,2,1,1,1,1},{0,0,0,0,0,2}},
		                          {{0,1,0,0,0,0},{0,1,0,1,0,2},{0,0,0,1,0,1},{0,2,0,1,0,2},{1,2,2,1,3,1}},
		                          {{1,2,2,0,1,1},{1,2,2,4,0,2},{2,0,2,0,0,1},{2,1,1,0,0,2},{0,0,1,3,1,1}},
		                          {{0,1,0,1,1,2},{0,2,0,0,0,1},{0,1,0,2,0,1},{2,0,1,2,1,1},{1,1,0,1,0,0}},
		                          {{0,1,1,2,0,0},{0,1,0,0,0,1},{1,1,0,0,1,2},{1,0,2,1,0,0},{0,0,1,0,0,0}},
		                          {{2,2,1,2,0,1},{1,3,0,0,1,1},{0,1,1,0,0,1},{0,1,0,1,0,2},{1,2,1,1,0,2}},
		                          {{2,3,1,2,1,1},{1,0,0,2,1,1},{0,1,2,2,0,0},{1,1,2,2,0,0},{1,1,1,3,1,0}}};
			
		int[][] maxWeight = {{1000,1000,1000,1000,1000},
		                     {1000,1000,1000,1000,1000},
		                     {1000,1000,1000,1000,1000},
		                     {1500,1500,1500,1500,1500},
		                     {1500,1500,1500,1500,1500},
		                     {2000,2000,2000,2000,2000},
		                     {2000,2000,2000,2000,2000},
		                     {2000,2000,2000,2000,2000},
		                     {2000,2000,2000,2000,2000},
		                     {2000,2000,2000,2000,2000},
		                     {2000,2000,2000,2000,2000},
		                     {1500,1500,1500,1500,1500},
		                     {1500,1500,1500,1500,1500},
		                     {1000,1000,1000,1000,1000},
		                     {1000,1000,1000,1000,1000},
		                     {1000,1000,1000,1000,1000}};
		
		/*int[][] pallets = {{836,743,218,17,280,258},
		                   {590,280,313,355,616,734},
		                   {49,219,294,53,10,229},
		                   {819,84,219,277,666,263},
			               {693,48,35,262,459,526},
			               {818,744,91,173,352,845},
			               {43,371,228,249,476,722},
			               {384,679,240,104,173,678},
			               {351,452,282,78,40,454},
			               {741,379,181,89,558,158},
			               {222,166,342,42,549,769},
			               {0,681,0,151,0,359},
			               {0,0,0,0,0,788}};

		int[][][] palletCounts = {{{0,1,3,1,1,0},{2,0,1,1,0,1},{1,2,2,0,0,1},{0,3,1,1,0,2},{1,2,0,2,1,1}},
				                 {{1,1,1,0,0,2},{0,1,0,1,0,2},{0,3,0,0,1,3},{1,0,1,1,0,3},{2,2,1,0,0,3}},
				                 {{3,0,0,1,1,1},{1,1,1,1,0,1},{0,2,2,1,0,2},{1,2,0,0,1,0},{2,1,0,1,1,0}},
				                 {{1,1,1,0,1,1},{0,2,3,0,1,0},{1,2,0,0,2,0},{1,2,0,1,3,2},{0,2,2,0,1,2}},
				                 {{1,0,1,1,1,3},{1,2,2,0,0,2},{1,1,0,3,1,2},{1,2,2,0,1,2},{1,1,0,1,0,2}},
				                 {{0,2,0,0,1,2},{0,0,1,1,2,1},{2,3,1,1,0,2},{1,2,1,0,0,0},{2,0,2,0,0,2}},
				                 {{1,2,1,0,0,2},{2,3,0,0,1,1},{1,1,1,0,2,0},{1,1,0,0,0,1},{1,4,0,0,0,2}},
				                 {{2,2,0,0,0,3},{1,0,0,0,0,1},{2,1,1,0,1,3},{1,2,1,0,0,0},{1,1,1,0,0,3}},
				                 {{0,1,0,2,0,2},{2,2,1,2,1,3},{2,0,0,2,0,1},{0,0,0,2,1,3},{0,2,1,0,0,0}},
				                 {{0,1,1,1,0,0},{2,1,2,0,1,1},{1,2,1,1,0,1},{1,2,1,1,1,1},{0,0,0,0,0,2}},
				                 {{0,1,0,0,0,0},{0,1,0,1,0,2},{0,0,0,1,0,1},{0,2,0,1,0,2},{1,2,2,1,3,1}},
				                 {{1,2,2,0,1,1},{1,2,2,4,0,2},{2,0,2,0,0,1},{2,1,1,0,0,2},{0,0,1,3,1,1}},
				                 {{0,1,0,1,1,2},{0,2,0,0,0,1},{0,1,0,2,0,1},{2,0,1,2,1,1},{1,1,0,1,0,0}},
				                 {{0,1,1,2,0,0},{0,1,0,0,0,1},{1,1,0,0,1,2},{1,0,2,1,0,0},{0,0,1,0,0,0}},
				                 {{2,2,1,2,0,1},{1,3,0,0,1,1},{0,1,1,0,0,1},{0,1,0,1,0,2},{1,2,1,1,0,2}},
				                 {{2,3,1,2,1,1},{1,0,0,2,1,1},{0,1,2,2,0,0},{1,1,2,2,0,0},{1,1,1,3,1,0}}};
		
		int[][] maxWeight = {{1000,1000,1000,1000,1000},
				             {1000,1000,1000,1000,1000},
				             {1000,1000,1000,1000,1000},
				             {1500,1500,1500,1500,1500},
				             {1500,1500,1500,1500,1500},
				             {2000,2000,2000,2000,2000},
				             {2000,2000,2000,2000,2000},
				             {2000,2000,2000,2000,2000},
				             {2000,2000,2000,2000,2000},
				             {2000,2000,2000,2000,2000},
				             {2000,2000,2000,2000,2000},
				             {1500,1500,1500,1500,1500},
				             {1500,1500,1500,1500,1500},
				             {1000,1000,1000,1000,1000},
				             {1000,1000,1000,1000,1000},
				             {1000,1000,1000,1000,1000}};*/
		
		//69
		/*int[][] pallets = {{418,453,154,265,297,426},
		                   {513,464,113,259,489,89},
		                   {344,42,226,151,294,330},
		                   {594,151,370,65,352,774},
		                   {735,280,222,273,352,149},
		                   {116,616,303,192,706,52},
		                   {328,10,329,276,721,295},
		                   {65,666,333,139,702,362},
		                   {157,459,269,331,449,49},
		                   {606,352,9,181,429,598},
		                   {367,476,163,0,204,461},
		                   {667,173,135,0,0,663},
		                   {680,40,20,0,0,61},
		                   {78,558,309,0,0,608},
		                   {127,549,136,0,0,0},
		                   {262,158,324,0,0,0},
		                   {523,734,186,0,0,0},
		                   {749,0,346,0,0,0},
		                   {479,0,77,0,0,0}};*/

		//10
		int[][] pallets = {{288,239,89,275,478,786},
						   {376,389,24,293,6,492},
						   {123,259,98,40,417,25},
						   {662,359,128,288,566,753},
						   {420,585,203,186,280,416},
						   {621,567,349,0,90,547},
						   {111,559,326,0,478,168},
						   {606,197,247,0,601,645},
						   {294,147,99,0,134,243},
						   {739,133,208,0,178,706},
						   {115,730,94,0,514,202},
						   {168,382,364,0,0,75},
						   {463,73,272,0,0,337},
						   {103,609,276,0,0,194},
						   {223,198,106,0,0,146},
						   {330,39,185,0,0,443},
						   {473,491,23,0,0,522},
						   {0,85,251,0,0,662},
						   {0,172,0,0,0,91},
						   {0,0,0,0,0,155}};
		
		
		int[][][] palletCounts = {{{0,1,3,1,1,0},{2,0,1,1,0,1},{1,2,2,0,0,1},{0,3,1,1,0,2},{1,2,0,2,1,1}},
		               			  {{1,1,1,0,0,2},{0,1,0,1,0,2},{0,3,0,0,1,3},{1,0,1,1,0,3},{2,2,1,0,0,3}},
		               			  {{3,0,0,1,1,1},{1,1,1,1,0,1},{0,2,2,1,0,2},{1,2,0,0,1,0},{2,1,0,1,1,0}},
		               			  {{1,1,1,0,1,1},{0,2,3,0,1,0},{1,2,0,0,2,0},{1,2,0,1,3,2},{0,2,2,0,1,2}},
		               			  {{1,0,1,1,1,3},{1,2,2,0,0,2},{1,1,0,3,1,2},{1,2,2,0,1,2},{1,1,0,1,0,2}},
		               			  {{0,2,0,0,1,2},{0,0,1,1,2,1},{2,3,1,1,0,2},{1,2,1,0,0,0},{2,0,2,0,0,2}},
		               			  {{1,2,1,0,0,2},{2,3,0,0,1,1},{1,1,1,0,2,0},{1,1,0,0,0,1},{1,4,0,0,0,2}},
		               			  {{2,2,0,0,0,3},{1,0,0,0,0,1},{2,1,1,0,1,3},{1,2,1,0,0,0},{1,1,1,0,0,3}},
		               			  {{0,1,0,2,0,2},{2,2,1,2,1,3},{2,0,0,2,0,1},{0,0,0,2,1,3},{0,2,1,0,0,0}},
		               			  {{0,1,1,1,0,0},{2,1,2,0,1,1},{1,2,1,1,0,1},{1,2,1,1,1,1},{0,0,0,0,0,2}},
		               			  {{0,1,0,0,0,0},{0,1,0,1,0,2},{0,0,0,1,0,1},{0,2,0,1,0,2},{1,2,2,1,3,1}},
		               			  {{1,2,2,0,1,1},{1,2,2,4,0,2},{2,0,2,0,0,1},{2,1,1,0,0,2},{0,0,1,3,1,1}},
		               			  {{0,1,0,1,1,2},{0,2,0,0,0,1},{0,1,0,2,0,1},{2,0,1,2,1,1},{1,1,0,1,0,0}},
		               			  {{0,1,1,2,0,0},{0,1,0,0,0,1},{1,1,0,0,1,2},{1,0,2,1,0,0},{0,0,1,0,0,0}},
		               			  {{2,2,1,2,0,1},{1,3,0,0,1,1},{0,1,1,0,0,1},{0,1,0,1,0,2},{1,2,1,1,0,2}},
		               			  {{2,3,1,2,1,1},{1,0,0,2,1,1},{0,1,2,2,0,0},{1,1,2,2,0,0},{1,1,1,3,1,0}}};

		int[][] maxWeight = {{1000,1000,1000,1000,1000},
							 {1000,1000,1000,1000,1000},
							 {1000,1000,1000,1000,1000},
							 {1500,1500,1500,1500,1500},
							 {1500,1500,1500,1500,1500},
							 {2000,2000,2000,2000,2000},
							 {2000,2000,2000,2000,2000},
							 {2000,2000,2000,2000,2000},
							 {2000,2000,2000,2000,2000},
							 {2000,2000,2000,2000,2000},
							 {2000,2000,2000,2000,2000},
							 {1500,1500,1500,1500,1500},
							 {1500,1500,1500,1500,1500},
							 {1000,1000,1000,1000,1000},
							 {1000,1000,1000,1000,1000},
							 {1000,1000,1000,1000,1000}};
		
		List<OpPallet> opPallets = new ArrayList<>();
		List<OpRow> opRows = new ArrayList<>();
				
		long id = 0;
		for(int j = 0; j < pallets.length; j++)
			for(int i = 0; i < pallets[j].length; i++)
				if(pallets[j][i] != 0)
					opPallets.add(new OpPallet(id++, pallets[j][i], i));
		
		id = 0;	
		int maxSequence = 0;
		for(int k = 0; k < palletCounts.length; k++)
			if(maxSequence < palletCounts[k].length) maxSequence = palletCounts[k].length;		
		
		
		for(int k = 0; k < palletCounts.length; k++)
		{
			List<OpSequence> sequences = new ArrayList<>();
			for(int l = 0; l < palletCounts[k].length; l++)
				sequences.add(new OpSequence(l, maxWeight[k][l], palletCounts[k][l]));
			
			opRows.add(new OpRow(id++, sequences, maxSequence));
		}
				
		
		System.out.println(opRows.toString());
		//Collections.sort(opRows);
		//opRows.sort(new OpRowDifficultyComparator());
		System.out.println(opRows.toString());
		
		
		List<OpSequence> seqs = opRows.stream().filter(r -> r.getId() == 11).findFirst().get().getSequences();
		
		System.out.println(opRows.stream().filter(r -> r.getId() == 11).findFirst().get());
		System.out.println(seqs.toString());
		//seqs.sort(new OpSequenceStrengthComparator());
		//Collections.sort(seqs);
		System.out.println(seqs.toString());
		//System.exit(0);
		
		
		
		
		TruckTemplateSolution unsolveTruckTemplate = new TruckTemplateSolution(opRows, opPallets);	
		solver.addEventListener(new SolverEventListener<TruckTemplateSolution>() {		
			@Override
			public void bestSolutionChanged(BestSolutionChangedEvent<TruckTemplateSolution> arg0) {
				//for(OpPallet p : arg0.getNewBestSolution().getOpPallets())
				//	System.out.println("Pallet: " + p.getId() + " Row: " + ((p.getRow() == null) ? -1 : p.getRow().getId()) + " Weight: " + p.getWeight());
				
				//for(OpRow r : arg0.getNewBestSolution().getOpRows())
				//	System.out.println("Row: " + r.getId() + " Sequence: " + ((r.useWildcard()) ? "W" : r.getSequence()) + " Pallets: (" + r.getOpPalletsOfType(0).size() + " " + r.getOpPalletsOfType(1).size() + " " + r.getOpPalletsOfType(2).size() + " " + r.getOpPalletsOfType(3).size() + " " + r.getOpPalletsOfType(4).size() + " " + r.getOpPalletsOfType(5).size() + ") Weight: " + r.getCurrentWeight());
			}
		});
		
		TruckTemplateSolution solved = solver.solve(unsolveTruckTemplate);
		
		int i = 1;
		for(OpPallet p : solved.getOpPallets())
			System.out.println(i++ + " " + p + " Row: " + ((p.getRow() == null) ? -1 : p.getRow().getId()) + " Weight: " + p.getWeight());
		
		int palletsUsed = 0;
		int weightUsed = 0;
		for(OpRow r : solved.getOpRows()) {
			palletsUsed += r.getOpPallets().size();
			weightUsed += r.getCurrentWeight();
			System.out.print(r + " " + r.getSequence() + " Pallets: (" + r.getOpPalletsOfType(0).size() + " " + r.getOpPalletsOfType(1).size() + " " + r.getOpPalletsOfType(2).size() + " " + r.getOpPalletsOfType(3).size() + " " + r.getOpPalletsOfType(4).size() + " " + r.getOpPalletsOfType(5).size() + ") Weight: " + r.getCurrentWeight() + " (" + r.getSequence().getMaxWeight() + ")" + " Valid: " + r.isValid());
			System.out.println(r.getOpPallets().toString());
		}
		System.out.println("Pallets used: " + palletsUsed);
		System.out.println("Weight used: " + weightUsed);
		
		System.out.print("Pallets: (");
		System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 0).count() + " ");
		System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 1).count() + " ");
		System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 2).count() + " ");
		System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 3).count() + " ");
		System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 4).count() + " ");
		System.out.println(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 5).count() + ")");

		System.out.println(solved.getOpPallets().stream().filter(p -> p.getRow() == null).toList());
	}
}
