package org.implementation.trucktemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;

public class OpRowChangeMove extends AbstractMove<TruckTemplateSolution> {

	private OpRow opRow;
	private OpSequence toSequence;
		
	public OpRowChangeMove(OpRow opRow, OpSequence toSequence) {
		this.opRow = opRow;
		this.toSequence = toSequence;
	}
	
	@Override
	public boolean isMoveDoable(ScoreDirector<TruckTemplateSolution> scoreDirector) {
		for(int i = 0; i < OpPallet.getNumberOfTypes(); i++) {
			final int temp = i;
			
			int allNeededOfType = scoreDirector.getWorkingSolution().getOpRows().stream().filter(r -> !Objects.equals(r, opRow)).mapToInt(r -> r.getCurrentSequence().getPalletsNeededOfType(temp)).sum(); // needed count of type i by all rows except opRow
			int typeCountNeededForNewSequence = toSequence.getPalletsNeededOfType(temp); // needed count of type i by opRow with new sequence toSequence;
			long palletsOfTypeAvailable = scoreDirector.getWorkingSolution().getOpPallets().stream().filter(p -> p.getType() == temp).count();
			
			if(allNeededOfType + typeCountNeededForNewSequence > palletsOfTypeAvailable)
				return false;
		}
		
		return true;
	}

	@Override
	protected AbstractMove<TruckTemplateSolution> createUndoMove(ScoreDirector<TruckTemplateSolution> arg0) {
        return new OpRowChangeMove(opRow, opRow.getCurrentSequence());
	}

	@Override
	protected void doMoveOnGenuineVariables(ScoreDirector<TruckTemplateSolution> scoreDirector) {
		//We consider switching sequence and filling up all rows as one step, so Optaplanner can calculate the score after each sequence switch
		//If we do this in one step, each step might take significant longer, but we have significant lesser steps

		//Set new sequence
		scoreDirector.beforeVariableChanged(opRow, "currentSequence");
		opRow.setCurrentSequence(toSequence);
		scoreDirector.afterVariableChanged(opRow, "currentSequence");
		
		//Fill all rows
		
		List<OpRow> nonWildcardRows = scoreDirector.getWorkingSolution().getOpRows().stream().filter(r -> !r.getCurrentSequence().isWildcard()).collect(Collectors.toList());
		List<OpRow> wildcardRows = scoreDirector.getWorkingSolution().getOpRows().stream().filter(r -> r.getCurrentSequence().isWildcard()).toList();
		List<OpPallet> pallets = scoreDirector.getWorkingSolution().getOpPallets();
		
		Collections.sort(nonWildcardRows);
		Collections.sort(pallets);
		
		//Reset all pallets
		for(int i = 0; i < pallets.size(); i++) {
			scoreDirector.beforeVariableChanged(pallets.get(i), "row");		
			pallets.get(i).setRow(null);
		}
		
		boolean succeeded = fillNonWildcardRows(nonWildcardRows, pallets);
		if(!succeeded) return;
		
		fillWildcardRows(wildcardRows, pallets);	
		
		for(int i = 0; i < pallets.size(); i++) {
			scoreDirector.afterVariableChanged(pallets.get(i), "row");		
		}
	}

	private boolean fillNonWildcardRows(List<OpRow> nonWildcardRows, List<OpPallet> pallets) {
		for(OpRow row : nonWildcardRows) {
			// We Keep track of all added pallets to the row
			List<OpPallet> palletsOfRow = new ArrayList<>();

			//Iterate each type
			for(int i = 0; i < OpPallet.getNumberOfTypes(); i++) {
				final int temp = i;
				//Get list of pallets of needed type
				List<OpPallet> palletsNeeded = pallets.stream().filter(p -> p.getType() == temp && p.getRow() == null).toList();
				
				//We have enough pallets of this type add pallets to the row until it is full
				if(palletsNeeded.size() >= row.getCurrentSequence().getPalletsNeededOfType(i)) {
					for (int j = 0; j < row.getCurrentSequence().getPalletsNeededOfType(i); j++) {					
						palletsNeeded.get(j).setRow(row);
						palletsOfRow.add(palletsNeeded.get(j));
						
						//If one row is not feasable do not continue
						if(palletsOfRow.stream().mapToInt(OpPallet::getWeight).sum() > row.getCurrentSequence().getLoadCapacity())
							return false;
					}
				} 
			}
			
			//At this point we filled the row, now we can swap pallets so we use the maximum possible weight as best as possible
			//And use pallets with less weight for other rows
			
			//Sorting pallets so we swap the lightest pallets first
			Collections.sort(palletsOfRow);
			
			//Iterate through all pallets and check if there is a heavier pallet that fits, if there is -> swap
			int currentRowWeight = palletsOfRow.stream().mapToInt(OpPallet::getWeight).sum();
			for(OpPallet oldPallet : palletsOfRow) {
				Optional<OpPallet> newPallet = pallets.stream().filter(p -> p.getRow() == null && !Objects.equals(p.getRow(), oldPallet.getRow()) && p.getType() == oldPallet.getType() && p.getWeight() > oldPallet.getWeight()).max(OpPallet::compareTo);
				
				//If heavier pallet found then swap if weight is not exceeded
				if(newPallet.isPresent() && newPallet.get().getWeight() + currentRowWeight - oldPallet.getWeight() <= row.getCurrentSequence().getLoadCapacity()) {
					oldPallet.setRow(null);
					newPallet.get().setRow(row);
					currentRowWeight = currentRowWeight + newPallet.get().getWeight() - oldPallet.getWeight();
				}
			}
		}
		
		return true;
	}

	private void fillWildcardRows(List<OpRow> wildcardRows, List<OpPallet> pallets) {
		//Sort pallets by normalized value and then by weight
		//We want lowest normalized value and highest weight, so we can fit more and heavier pallets
		List<OpPallet> palletsToPickFrom = pallets.stream().filter(p -> p.getRow() == null).collect(Collectors.toList());
		palletsToPickFrom.sort((p1, p2) -> {
			if(p1.getSize() != p2.getSize())
				return p1.getSize() - p2.getSize();
			else
				return -(p1.getWeight() - p2.getWeight());
		});
		
		for(OpRow row : wildcardRows) {
			//Iterate each candidate pallet for the row
			for(int j = 0; j < palletsToPickFrom.size(); j++) {
				OpPallet pallet = palletsToPickFrom.get(j);
				List<OpPallet> palletsOfRow = pallets.stream().filter(p -> Objects.equals(p.getRow(), row)).toList();
				
				int currentSpaceLeft = palletsOfRow.stream().mapToInt(OpPallet::getSize).sum();
				int currentWeightOfRow = palletsOfRow.stream().mapToInt(OpPallet::getWeight).sum();

				//If normalized value and weight of the row are not exceeded add pallet to row
				if(currentSpaceLeft + pallet.getSize() <= OpSequence.getWildcardSpace() && currentWeightOfRow + pallet.getWeight() <= OpSequence.getWildcardLoadCapacity()) {
					pallet.setRow(row);
					palletsToPickFrom.remove(pallet);
				}
			}
		}
	}
	
	@Override
	public String getSimpleMoveTypeDescription() {
		return "OpRowChangeMove(OpRow.sequence)";
	}
	
	@Override
	public Collection<? extends Object> getPlanningEntities() {
		return Collections.singletonList(opRow);
	}
	
	@Override
	public Collection<? extends Object> getPlanningValues() {
		return Collections.singletonList(toSequence);
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        final OpRowChangeMove other = (OpRowChangeMove) obj;
        return Objects.equals(opRow, other.opRow) &&
                Objects.equals(toSequence, other.toSequence);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(opRow, toSequence);
	}
	
	@Override
	public String toString() {
        return opRow + " {" + opRow.getCurrentSequence() + " -> " + toSequence + "}";
	}

}
