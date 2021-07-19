package org.optaplanner.trucktemplate;

import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;

public class SequenceSelectionFilter implements SelectionFilter<TruckTemplateSolution, Integer> {

	@Override
	public boolean accept(ScoreDirector<TruckTemplateSolution> arg0, Integer arg1) {
		System.out.print("SEQUENCE ");

		return true;
	}

}
