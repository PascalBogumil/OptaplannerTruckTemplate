package org.optaplanner.trucktemplate;

import java.util.Collections;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.persistence.xstream.api.score.buildin.hardsoft.HardSoftScoreXStreamConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@PlanningSolution
@XStreamAlias("TruckTemplateSolution")
public class TruckTemplateSolution extends AbstractPersistable {
	
	//Configuration
	private final static int[] normalizedTypeValues = new int[] {4, 2, 1, 1, 2, 4};
	private final static int numberOfTypes = 6;
	private final static int[] maxWeightEachType = new int[]{850, 750, 375, 375, 750, 850};
	private static int wildcardPenaltyValue = 0;
	
	@PlanningEntityCollectionProperty
	private List<OpPallet> opPallets;
	
	@ValueRangeProvider(id = "rows")
	@PlanningEntityCollectionProperty
	private List<OpRow> opRows;
	
	@PlanningScore
    @XStreamConverter(HardSoftScoreXStreamConverter.class)
	private HardSoftScore score;
	
	public TruckTemplateSolution() {

	}
	
	public TruckTemplateSolution(List<OpRow> opRows, List<OpPallet> opPallets) {		
		this.opRows = opRows;
		this.opPallets = opPallets;
		Collections.sort(this.opRows);
		Collections.sort(this.opPallets);
	}
	
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

	public static int[] getMaxWeightEachType() {
		return maxWeightEachType;
	}	
	
	public static int getWildcardPenaltyValue() {
		return wildcardPenaltyValue;
	}
	
	public static void setWildcardPenaltyValue(int wildcardPenaltyValue) {
		TruckTemplateSolution.wildcardPenaltyValue = wildcardPenaltyValue;
	}
}