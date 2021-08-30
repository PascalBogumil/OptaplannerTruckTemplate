package org.implementation.trucktemplate;

import java.util.Collections;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.persistence.xstream.api.score.buildin.hardsoft.HardSoftScoreXStreamConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@PlanningSolution
@XStreamAlias("TruckTemplateSolution")
public class TruckTemplateSolution {
	
	private static int wildcardPenaltyValue = 1400;

	@PlanningEntityCollectionProperty
	private List<OpPallet> opPallets;
	
	@ValueRangeProvider(id = "rows")
	@PlanningEntityCollectionProperty
	private List<OpRow> opRows;
	
	@PlanningScore
    @XStreamConverter(HardSoftScoreXStreamConverter.class)
	private HardSoftScore score;
	
	public TruckTemplateSolution() { }
	
	public TruckTemplateSolution(List<OpRow> opRows, List<OpPallet> opPallets) {		
		this.opRows = opRows;
		this.opPallets = opPallets;
		Collections.sort(this.opRows);
		Collections.sort(this.opPallets);
	}
	
	public static int getWildcardPenaltyValue() {
		return wildcardPenaltyValue;
	}
	
	public static void setWildcardPenaltyValue(int wildcardPenaltyValue) {
		TruckTemplateSolution.wildcardPenaltyValue = wildcardPenaltyValue;
	}
	
	public List<OpPallet> getOpPallets() {
		return opPallets;
	}

	public List<OpRow> getOpRows() {
		return opRows;
	}

	public HardSoftScore getScore() {
		return score;
	}

	public void setScore(HardSoftScore score) {
		this.score = score;
	}
}
