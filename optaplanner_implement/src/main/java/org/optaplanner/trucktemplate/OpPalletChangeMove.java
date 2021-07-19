package org.optaplanner.trucktemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;

public class OpPalletChangeMove extends AbstractMove<TruckTemplateSolution> {

	private OpPallet opPallet;
	private OpPallet swapPallet;
	private OpRow toOpRow;

	
	public OpPalletChangeMove(OpPallet opPallet, OpRow toOpRow) {
		this.opPallet = opPallet;
		this.toOpRow = toOpRow;
	}
	
	public OpPalletChangeMove(OpPallet opPallet, OpPallet swapPallet) {
		this.opPallet = opPallet;
		this.swapPallet = swapPallet;
	}
	
	@Override
	public boolean isMoveDoable(ScoreDirector<TruckTemplateSolution> scoreDirector) {
		return true;
	}

	@Override
	protected AbstractMove<TruckTemplateSolution> createUndoMove(ScoreDirector<TruckTemplateSolution> arg0) {
		if(toOpRow != null || (toOpRow == null && swapPallet == null)) {
			return new OpPalletChangeMove(opPallet, opPallet.getRow());
		} else if(swapPallet != null) {
			return new OpPalletChangeMove(swapPallet, opPallet);
		}
		return null;
	}

	@Override
	protected void doMoveOnGenuineVariables(ScoreDirector<TruckTemplateSolution> scoreDirector) {
		if(toOpRow != null || (toOpRow == null && swapPallet == null)) {
			scoreDirector.beforeVariableChanged(opPallet, "row");
			opPallet.setRow(toOpRow);
			scoreDirector.afterVariableChanged(opPallet, "row");
		} else if(swapPallet != null) {
			OpRow temp = scoreDirector.getWorkingSolution().getOpRows().stream().filter(r -> Objects.equals(r, opPallet.getRow())).findFirst().orElse(null);
			OpRow temp2 = scoreDirector.getWorkingSolution().getOpRows().stream().filter(r -> Objects.equals(r, swapPallet.getRow())).findFirst().orElse(null);
			scoreDirector.beforeVariableChanged(opPallet, "row");
			opPallet.setRow(temp2);
			scoreDirector.afterVariableChanged(opPallet, "row");
						
			scoreDirector.beforeVariableChanged(swapPallet, "row");
			swapPallet.setRow(temp);
			scoreDirector.afterVariableChanged(swapPallet, "row");
		}
		
		

		
		/*int palletsUsed = 0;
		int weightUsed = 0;
		for(OpRow r : scoreDirector.getWorkingSolution().getOpRows()) {
			palletsUsed += r.getOpPallets().size();
			weightUsed += r.getCurrentWeight();
			System.out.println("Row: " + r.getId() + " Sequence: " + ((r.useWildcard()) ? "W" : r.getSequence()) + " Pallets: (" + r.getOpPalletsOfType(0).size() + " " + r.getOpPalletsOfType(1).size() + " " + r.getOpPalletsOfType(2).size() + " " + r.getOpPalletsOfType(3).size() + " " + r.getOpPalletsOfType(4).size() + " " + r.getOpPalletsOfType(5).size() + ") Weight: " + r.getCurrentWeight() + " Valid: " + r.isValid());
		}
		System.out.println("Pallets used: " + palletsUsed);
		System.out.println("Weight used: " + weightUsed);
		System.out.print("Pallets: (");
		System.out.print(scoreDirector.getWorkingSolution().getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 0).count() + " ");
		System.out.print(scoreDirector.getWorkingSolution().getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 1).count() + " ");
		System.out.print(scoreDirector.getWorkingSolution().getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 2).count() + " ");
		System.out.print(scoreDirector.getWorkingSolution().getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 3).count() + " ");
		System.out.print(scoreDirector.getWorkingSolution().getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 4).count() + " ");
		System.out.println(scoreDirector.getWorkingSolution().getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 5).count() + ")");
		//System.out.println("do " + opPallet.getRow());
		/*if(opPallet.getRow() != null && opPallet.getRow().isValid()) {
			opPallet.getRow().getOpPallets().forEach(p ->{
				scoreDirector.beforeVariableChanged(p, "pinned");
				p.setPinned(true);
				scoreDirector.afterVariableChanged(p, "pinned");
			});
			System.out.println(opPallet.isPinned());
			//System.exit(0);
			scoreDirector.beforeVariableChanged(opPallet.getRow(), "pinned");
			opPallet.getRow().setPinned(true);
			scoreDirector.afterVariableChanged(opPallet.getRow(), "pinned");
		}*/
	}

	@Override
	public String getSimpleMoveTypeDescription() {
		return "OpPalletChangeMove(OpPallet.row)";
	}
	
	@Override
	public Collection<? extends Object> getPlanningEntities() {
		return Collections.singletonList(opPallet);
	}
	
	@Override
	public Collection<? extends Object> getPlanningValues() {
		return Collections.singletonList(toOpRow);
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        final OpPalletChangeMove other = (OpPalletChangeMove) obj;
        return Objects.equals(opPallet, other.opPallet) &&
                Objects.equals(toOpRow, other.toOpRow);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(opPallet, toOpRow);
	}
	
	@Override
	public String toString() {
		if(toOpRow != null || (toOpRow == null && swapPallet == null))
			return opPallet + " {" + opPallet.getRow() + " -> " + toOpRow + "}\n";
		if(swapPallet != null)
			return opPallet + " {" + opPallet.getRow() + "} <-> " + swapPallet + " {" + swapPallet.getRow() + "}\n";
		
		return "";
	}
}
