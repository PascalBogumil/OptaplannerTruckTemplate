package org.optaplanner.trucktemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
		for(int i = 0; i < TruckTemplateSolution.getNumberOfTypes(); i++) {
			final int temp = i;
			scoreDirector.getWorkingSolution().getOpRows().stream().filter(r -> !Objects.equals(r, opRow)).mapToInt(r -> r.getNeedOfType(temp)).sum();
			if(scoreDirector.getWorkingSolution().getOpRows().stream().filter(r -> !Objects.equals(r, opRow)).mapToInt(r -> r.getNeedOfType(temp)).sum() + toSequence.getCountPerType().get(temp) > scoreDirector.getWorkingSolution().getOpPallets().stream().filter(p -> p.getType() == temp).count())
				return false;
		}
		
		return true;
	}

	@Override
	protected AbstractMove<TruckTemplateSolution> createUndoMove(ScoreDirector<TruckTemplateSolution> arg0) {
        return new OpRowChangeMove(opRow, opRow.getSequence());
	}

	@Override
	protected void doMoveOnGenuineVariables(ScoreDirector<TruckTemplateSolution> scoreDirector) {
		//We consider switching sequence and filling up all rows as one step, so Optaplanner can calculate the score after each sequence switch
		//If we do this in one step, each step might take significant longer, but we have significant lesser steps
		
		//Set new sequence
		scoreDirector.beforeVariableChanged(opRow, "sequence");
		opRow.setSequence(toSequence);
		scoreDirector.afterVariableChanged(opRow, "sequence");
		
		List<OpRow> rows = scoreDirector.getWorkingSolution().getOpRows();
		List<OpRow> wildcardRows = new ArrayList<>();
		List<OpPallet> pallets = scoreDirector.getWorkingSolution().getOpPallets();
		
		/*for(int i = 0; i < TruckTemplateSolution.getNumberOfTypes(); i++) {
			final int temp = i;
			if(scoreDirector.getWorkingSolution().getOpRows().stream().filter(r -> !Objects.equals(r, opRow)).mapToInt(r -> r.getNeedOfType(temp)).sum() + toSequence.getCountPerType().get(temp) > scoreDirector.getWorkingSolution().getOpPallets().stream().filter(p -> p.getType() == temp).count())
				return;
		}*/
		
		Collections.sort(rows);
		Collections.sort(pallets);
		
		for(int i = 0; i < pallets.size(); i++) {
			scoreDirector.beforeVariableChanged(pallets.get(i), "row");		
			pallets.get(i).setRow(null);
		}
		
		//Iterate each row
		for(int k = 0; k < rows.size(); k++) {
			OpRow row = rows.get(k);
			
			if(!row.getSequence().isWildcard() && !row.isValid()) {
				//Iterate each type
				for(int i = 0; i < TruckTemplateSolution.getNumberOfTypes(); i++) {
					final int temp = i;
					//Get list of pallets of needed type
					List<OpPallet> palletsNeeded = pallets.stream().filter(p -> p.getType() == temp && p.getRow() == null).toList();
					
					//We have enough pallets of this type add pallets to row until full
					if(palletsNeeded.size() >= row.getSequence().getPalletCountsMax()[i]) {
						for (int j = 0; j < row.getSequence().getPalletCountsMax()[i]; j++) {					
							palletsNeeded.get(j).setRow(row);
						}
					} 
					//We do not have enough pallets to fill row, so do not even bother filling it
					else {
						//break;
					}
				}
			} else {
				wildcardRows.add(row);
			}
			
			//At this point we filled the row, now we can swap pallets so we use the maximum possible weight as best as possible
			//And use pallets with less weight for other rows
			
			List<OpPallet> palletsOfRow = pallets.stream().filter(p ->  Objects.equals(p.getRow(), row)).toList();
			
			//Iterate through all pallets and check if there is a heavier pallet, if there is -> swap
			for(int i = 0; i < palletsOfRow.size(); i++) {
				OpPallet oldPallet = palletsOfRow.get(i);
				Optional<OpPallet> newPallet = pallets.stream().filter(p -> p.getRow() == null && !Objects.equals(p.getRow(), oldPallet.getRow()) && p.getType() == oldPallet.getType() && p.getWeight() > oldPallet.getWeight()).max(OpPallet::compareTo);

				int currentRowWeight = pallets.stream().filter(p -> Objects.equals(p.getRow(), row)).mapToInt(OpPallet::getWeight).sum();
				
				//If heavier pallet found then swap if weight is not exceeded
				if(newPallet.isPresent() && newPallet.get().getWeight() + currentRowWeight - oldPallet.getWeight() <= row.getSequence().getMaxWeight()) {
					oldPallet.setRow(null);
					newPallet.get().setRow(rows.get(k));
				}
			}
		}
		
		//Sort pallets by normalized value and then by weight
		//We want lowest normalized value and highest weight, so we can fit more and heavier pallets
		pallets.sort((p1, p2) -> {
			if(p1.getNormalizedValue() != p2.getNormalizedValue())
				return p1.getNormalizedValue() - p2.getNormalizedValue();
			else
				return -(p1.getWeight() - p2.getWeight());
		});
		
		//Iterate each wildcard row
		for(int i = 0; i < wildcardRows.size(); i++) {
			OpRow row = wildcardRows.get(i);
			List<OpPallet> palletsToPickFrom = pallets.stream().filter(p -> p.getRow() == null).toList();
			
			//Iterate each candidate pallet for the row
			for(int j = 0; j < palletsToPickFrom.size(); j++) {
				OpPallet pallet = palletsToPickFrom.get(j);
				List<OpPallet> palletsOfRow = pallets.stream().filter(p -> Objects.equals(p.getRow(), row)).toList();
				
				int currentNormalizedValueOfRow = palletsOfRow.stream().mapToInt(OpPallet::getNormalizedValue).sum();
				int currentWeightOfRow = palletsOfRow.stream().mapToInt(OpPallet::getWeight).sum();

				//If normalized value and weight of the row are not exceeded add pallet to row
				if(currentNormalizedValueOfRow + pallet.getNormalizedValue() <= OpSequence.getWildcardNorm() && currentWeightOfRow + pallet.getWeight() <= row.getSequence().getMaxWeight()) {
					pallet.setRow(row);
				}
			}
		}	
		
		for(int i = 0; i < pallets.size(); i++) {
			scoreDirector.afterVariableChanged(pallets.get(i), "row");		
		}
		
		
		//Now we should have filled each row with the desired pallets, now we can check if there is any row which has exceeded weight and try so swap pallets between rows to satisfy the weight of each row
		//TODO: Maybe
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
        return opRow + " {" + opRow.getSequence() + " -> " + toSequence + "}";
	}

}
