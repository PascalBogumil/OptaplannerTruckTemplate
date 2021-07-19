package org.optaplanner.trucktemplate;

import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;

public class OpPalletOverFillSelectionFilter implements SelectionFilter<TruckTemplateSolution, ChangeMove<TruckTemplateSolution>> {

	@Override
	public boolean accept(ScoreDirector<TruckTemplateSolution> solution, ChangeMove<TruckTemplateSolution> move) {
		OpPallet pallet = (OpPallet)move.getEntity();
		OpRow toRow = (OpRow)move.getToPlanningValue();
		
		return pallet.getRow() != null && toRow == null && pallet.getRow().isTypeOverFilled(pallet.getType());
	}

}
