package org.optaplanner.trucktemplate;

import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;

public class OpRowSelectionFilter implements SelectionFilter<TruckTemplateSolution, ChangeMove> {

	@Override
	public boolean accept(ScoreDirector<TruckTemplateSolution> sol, ChangeMove p) {
		/*OpRow row = (OpRow)p.getEntity();
		System.out.println(row);
		if(row.getSequence() == null) System.out.println(row);
		if(row.isValid()) return false;
		
		if(p.getToPlanningValue() instanceof Integer) {
			Integer sequence = (Integer)p.getToPlanningValue();			
			//System.out.println(row + " with sequence: " + sequence + " " + (row.isValid(sequence)));	
			
			//if(row.isValid(sequence))
				return true;
		} else if (p.getToPlanningValue() instanceof Boolean) {
			return true;
		}*/
		
		
		
		
		//return ((OpRow)p.getEntity()).getId() == 0;
		
		
		//return true;
		
		OpRow row = (OpRow)p.getEntity();
		int indexNewSequence = row.getSequences().indexOf(row.getSequence()) + 1;
		if(indexNewSequence < row.getSequences().size())
			return row.getSequences().get(indexNewSequence).equals((OpSequence)p.getToPlanningValue());		
		
		return false;
	}

}
