package org.optaplanner.trucktemplate;

import java.util.Arrays;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.CountableValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class TruckTemplateSolution {
	
	//Configuration
	private final static int[] normalizedTypeValues = new int[] {4, 2, 1, 1, 2, 4};
	private final static int numberOfTypes = 6;
	private final static int WildcardPenaltyValue = 1000;
	private final static int[] maxWeightEachType = new int[]{850, 750, 375, 375, 750, 850};
	
	public static int[] getMaxWeightEachType() {
		return maxWeightEachType;
	}	
	
	@PlanningEntityCollectionProperty
	private List<OpPallet> opPallets;
	
	@ValueRangeProvider(id = "rows")
	@PlanningEntityCollectionProperty
	private List<OpRow> opRows;

	/*@ValueRangeProvider(id = "sequences")
	public CountableValueRange<Integer> getMaxSequence() {
		return ValueRangeFactory.createIntValueRange(-1, maxSequence); //exclusive upper bound
	}*/
	
	@PlanningScore
	private HardSoftScore score;
	
	public TruckTemplateSolution() {

	}
	
	public TruckTemplateSolution(List<OpRow> opRows, List<OpPallet> opPallets) {
		this.opRows = opRows;
		this.opPallets = opPallets;	
		//this.maxSequence = opRows.stream().mapToInt(OpRow::getNumberOfSequences).max().orElse(0);
	}
	
	/*public TruckTemplateSolution(int[][] pallets, int[][][] palletcounts, int[][] maxWeightPerColumn) {
		opPallets = new ArrayList<>();
		opRows = new ArrayList<>();
		maxSequence = palletcounts[0].length;
		
		long ids = 0;
		for(int j = 0; j < pallets.length; j++)
			for(int i = 0; i < pallets[j].length; i++)
				opPallets.add(new OpPallet(ids++, pallets[j][i], i));
		
		ids = 0;
		for(int k = 0; k < palletcounts.length; k++)
			opRows.add(new OpRow(ids++, maxWeightPerColumn[k], palletcounts[k]));
	}*/
	
	public List<OpPallet> getOpPallets() {
		return opPallets;
	}

	public void setOpPallets(List<OpPallet> opPallets) {
		this.opPallets = opPallets;
	}

	public List<OpRow> getOpRows() {
		return opRows;
	}

	public void setOpRows(List<OpRow> opRows) {
		this.opRows = opRows;
	}

	public HardSoftScore getScore() {
		return score;
	}

	public void setScore(HardSoftScore score) {
		this.score = score;
	}

	public static int getNumberOfTypes() {
		return numberOfTypes;
	}

	public static int getNormalizedValueOfType(int type) {
		return normalizedTypeValues[type];
	}

	public static int getWildcardPenaltyValue() {
		return WildcardPenaltyValue;
	}
}
