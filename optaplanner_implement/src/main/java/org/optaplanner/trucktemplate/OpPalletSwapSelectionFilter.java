package org.optaplanner.trucktemplate;

import java.util.Objects;

import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;

public class OpPalletSwapSelectionFilter implements SelectionFilter<CourseSchedule, SwapMove> {

	@Override
	public boolean accept(ScoreDirector<CourseSchedule> arg0, SwapMove move) {
		OpPallet left = (OpPallet)move.getLeftEntity();
		OpPallet right = (OpPallet)move.getRightEntity();

		if(Objects.equals(left, right))
			return false;
		
		if(left.getType() != right.getType())
			return false;
		
		if(Objects.equals(left.getRow(), right.getRow()))
			return false;
		
		if(left.getRow() != null && right.getRow() != null || left.getRow() == null && right.getRow() == null)
			return false;	
		
		if(left.getWeight() == right.getWeight())
			return false;
		
		OpPallet newPallet = (left.getWeight() > right.getWeight()) ? left : right;
		OpPallet oldPallet = (left.getWeight() > right.getWeight()) ? right : left;
		
		if(newPallet.getRow() == null && oldPallet.getRow().getCurrentWeight() - oldPallet.getWeight() + newPallet.getWeight() <= oldPallet.getRow().getSequence().getMaxWeight())
			return true;
		
		/*if(left.getRow() == null && left.getWeight() > right.getWeight() && right.getRow().getCurrentWeight() - right.getWeight() + left.getWeight() <= right.getRow().getSequence().getMaxWeight())
			return true;	
		
		if(right.getRow() == null && right.getWeight() > left.getWeight() && left.getRow().getCurrentWeight() - left.getWeight() + right.getWeight() <= left.getRow().getSequence().getMaxWeight())
			return true;*/
		

		return false;
	}
}