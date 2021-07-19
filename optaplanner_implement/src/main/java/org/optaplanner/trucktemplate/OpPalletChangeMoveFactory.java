package org.optaplanner.trucktemplate;

import static java.util.Comparator.comparingInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

public class OpPalletChangeMoveFactory implements MoveListFactory<TruckTemplateSolution> {

	@Override
	public List<OpPalletChangeMove> createMoveList(TruckTemplateSolution solution) {
        List<OpPalletChangeMove> moveList = new ArrayList<>();
        List<OpRow> rowList = solution.getOpRows();
        rowList.sort(comparingInt((OpRow p) -> (int)p.getId()));
        
        List<OpPallet> palletList = solution.getOpPallets();
        palletList.sort(comparingInt((OpPallet p) -> p.getWeight()));
        
    	//Fix overfilled rows
    	for(OpRow r : rowList) {	
    		if(!r.getSequence().isWildcard()) {
        		//Remove pallets from overfilled rows
    			boolean removed = false;
    			for (int i = 0; i < TruckTemplateSolution.getNumberOfTypes(); i++) {
	    			if(r.getSequence().getPalletCountsMax()[i] < r.getOpPalletsOfType(i).size()) {
		            	moveList.add(new OpPalletChangeMove(r.getOpPalletsOfType(i).get(0), (OpRow)null));
		            	removed = true;
		            	continue;
		            }
    			}
    			if(removed) continue;
    		} else {
        		//Remove pallets from overfilled wildcard rows
    			if(OpSequence.getWildcardNorm() < r.getNormalizedValueOfPallets() || r.getSequence().getMaxWeight() < r.getCurrentWeight()) {
	            	moveList.add(new OpPalletChangeMove(r.getOpPallets().get(0), (OpRow)null));
	            	continue;
	            }
    		}
    	}
        
        for(OpPallet p : palletList) {
        	//if a pallet belongs to a finished row do not touch it anymore
        	if(p.getRow() != null && p.getRow().isValid() && !p.getRow().getSequence().isWildcard())
        		continue;        		
        	
        	//Remove pallet of count exceeded
        	/*if(p.getRow() != null && p.getRow().getPalletCountsMax()[p.getType()] < p.getRow().getOpPalletsOfType(p.getType()).size() && !p.getRow().useWildcard()) {
        		moveList.add(new OpPalletChangeMove(p, (OpRow)null));
        		continue;
        	}*/
        	
        	//A pallet is considered movable, if it is not used
        	//Handle all movable pallets
        	for(OpRow r : rowList) {	
        		//Swap the pallets of finished rows with other movable pallets, to maximize the weight of the row
             	if(r.isValid() && !r.getSequence().isWildcard()) {
             		List<OpPallet> palletsToSwap = r.getOpPalletsOfType(p.getType()).stream().filter(p2 -> p2.getWeight() < p.getWeight() && !Objects.equals(p2.getRow(), p.getRow())).toList();
             		if(!palletsToSwap.isEmpty()) {
        				int deltaWeight = Math.abs(p.getWeight() - palletsToSwap.get(0).getWeight());
             			if(palletsToSwap.get(0).getRow().getCurrentWeight() + deltaWeight < palletsToSwap.get(0).getRow().getSequence().getMaxWeight()) {
                 			moveList.add(new OpPalletChangeMove(p, palletsToSwap.get(0)));
                 			break;
             			}
             		}
             		
             		//Skip finished rows if no pallets can be swapped
             		continue;
             	}

             	//Add the current movable pallet to the next possible row
        		boolean isPalletAlreadyInRow = Objects.equals(p.getRow(), r);
        		boolean hasRowSpaceForPallet = r.getOpPalletsOfType(p.getType()).size() < r.getSequence().getPalletCountsMax()[p.getType()];
        		boolean hasRowFreeWeightForPallet = r.getCurrentWeight() + p.getWeight() <= r.getSequence().getMaxWeight();
        		//boolean isPalletMovable = p.getRow() == null || !p.getRow().isValid() || p.getRow().useWildcard();
        		
        		if(p.getRow() == null/* || p.getRow().getId() > r.getId()*/) {
        		//if(isPalletMovable || p.getRow().getId() > r.getId()) {
	        		if(!isPalletAlreadyInRow && hasRowSpaceForPallet && hasRowFreeWeightForPallet) {
	            		moveList.add(new OpPalletChangeMove(p, r));
	            		break;
	        		}
        		}
        		
        		//Fill up the wildcard rows with all leftover pallets
           		if(r.isValid() && r.getSequence().isWildcard() && p.getRow() == null) {
            		boolean hasRowNormalizedSpaceForPallet = r.getNormalizedValueOfPallets() + p.getNormalizedValue() <= r.getSequence().getWildcardNorm();
            		
            		if(!isPalletAlreadyInRow && hasRowNormalizedSpaceForPallet && hasRowFreeWeightForPallet) {
                		moveList.add(new OpPalletChangeMove(p, r));
                		break;
            		}
           		}
        	}
        }

        System.out.println(moveList);
		return moveList;
	}

}
