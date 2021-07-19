package org.optaplanner.trucktemplate;

import static java.util.Comparator.comparingInt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

public class OpRowChangeMoveFactory implements MoveListFactory<TruckTemplateSolution> {

	@Override
	public List<OpRowChangeMove> createMoveList(TruckTemplateSolution solution) {
		List<OpRowChangeMove> moveList = new ArrayList<>();

        List<OpRow> rowList = solution.getOpRows();
        rowList.sort(comparingInt((OpRow p) -> (int)p.getId()));
        
        List<OpPallet> palletList = solution.getOpPallets();    
        
        for (OpRow r : rowList) {
        	
        	boolean palletForRowAvailable = palletList.stream().filter(p -> {
        		boolean isPalletMovable = p.getRow() == null;// || !p.getRow().isValid() || p.getRow().useWildcard();
        		boolean isPalletAlreadyInRow = Objects.equals(p.getRow(), r);
        		boolean hasRowSpaceForPallet = r.getOpPalletsOfType(p.getType()).size() < r.getSequence().getPalletCountsMax()[p.getType()];
        		boolean hasRowFreeWeightForPallet = r.getCurrentWeight() + p.getWeight() < r.getSequence().getMaxWeight();
        		
        		return isPalletMovable && !isPalletAlreadyInRow && hasRowSpaceForPallet && hasRowFreeWeightForPallet;		
        	}).count() != 0;

        	if(palletForRowAvailable || r.isValid() || r.getSequence().isWildcard())
        		continue;
    		
        	Iterator<OpSequence> iter = r.getSequences().iterator();
        	while(iter.hasNext()) {
        		OpSequence s = iter.next();
        		if(!r.isValid() && r.getSequence().compareTo(s) < 0) {
        			moveList.add(new OpRowChangeMove(r, s));
        			break;
        		} /*else if(!iter.hasNext()) {
        			moveList.add(new OpRowChangeMove(r, -1));
        			break;
        		}*/
        	}
        }
        
        System.out.println(moveList);      
		return moveList;
	}
}
