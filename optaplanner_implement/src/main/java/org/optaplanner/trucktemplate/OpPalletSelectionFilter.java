package org.optaplanner.trucktemplate;

import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;

public class OpPalletSelectionFilter implements SelectionFilter<TruckTemplateSolution, ChangeMove<TruckTemplateSolution>> {

	@Override
	public boolean accept(ScoreDirector<TruckTemplateSolution> solution, ChangeMove<TruckTemplateSolution> move) {
		/*OpPallet pallet = (OpPallet)p.getEntity();
		OpRow row = null;
		if(p.getToPlanningValue() instanceof OpRow) {
			row = (OpRow)p.getToPlanningValue();
			System.out.println(pallet.getId() + " ");
			//System.out.println(row + " " + (row.getOpPalletsOfType(pallet.getType()).size() < row.getPalletCountsMax(0)[pallet.getType()]));
			if(row.getOpPalletsOfType(pallet.getType()).size() < row.getSequence().getPalletCountsMax()[pallet.getType()] && row.getCurrentWeight() + pallet.getWeight() <= row.getSequence().getMaxWeight())
				return true;
		}*/
		
		

		
		
		OpPallet pallet = (OpPallet)move.getEntity();
		OpRow oldRow = pallet.getRow();
		OpRow newRow = (OpRow)move.getToPlanningValue();
		
		if(oldRow != null && newRow != null || oldRow == null && newRow == null)
			return false;
		
		/*if(newRow == null && oldRow != null && oldRow.isTypeOverFilled(pallet.getType()))
		{
			TruckTemplateSolution solved = solution.getWorkingSolution();
			for(OpPallet p : solved.getOpPallets())
				System.out.println(p + " Row: " + ((p.getRow() == null) ? -1 : p.getRow().getId()) + " Weight: " + p.getWeight());
			
			int palletsUsed = 0;
			int weightUsed = 0;
			for(OpRow r : solved.getOpRows()) {
				palletsUsed += r.getOpPallets().size();
				weightUsed += r.getCurrentWeight();
				System.out.println(r + " " + r.getSequence() + " Pallets: (" + r.getOpPalletsOfType(0).size() + " " + r.getOpPalletsOfType(1).size() + " " + r.getOpPalletsOfType(2).size() + " " + r.getOpPalletsOfType(3).size() + " " + r.getOpPalletsOfType(4).size() + " " + r.getOpPalletsOfType(5).size() + ") Weight: " + r.getCurrentWeight() + " (" + r.getSequence().getMaxWeight() + ")" + " Valid: " + r.isValid());
			}
			System.out.println("Pallets used: " + palletsUsed);
			System.out.println("Weight used: " + weightUsed);
			
			System.out.print("Pallets: (");
			System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 0).count() + " ");
			System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 1).count() + " ");
			System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 2).count() + " ");
			System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 3).count() + " ");
			System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 4).count() + " ");
			System.out.println(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 5).count() + ")");

			System.out.println(solved.getOpPallets().stream().filter(p -> p.getRow() == null).toList());
			
			return true;
		}*/
		
		
		if(!(oldRow != null && (oldRow.isValid() || !oldRow.isAnyOverFilled())))	
			if(newRow != null && !newRow.isValid() && !newRow.isTypeOverFilled(pallet.getType()) && !newRow.isTypeFilled(pallet.getType()))
				if(newRow.getCurrentWeight() + pallet.getWeight() <= newRow.getSequence().getMaxWeight())
				{
					/*TruckTemplateSolution solved = solution.getWorkingSolution();
					for(OpPallet p : solved.getOpPallets())
						System.out.println(p + " Row: " + ((p.getRow() == null) ? -1 : p.getRow().getId()) + " Weight: " + p.getWeight());
					
					int palletsUsed = 0;
					int weightUsed = 0;
					for(OpRow r : solved.getOpRows()) {
						palletsUsed += r.getOpPallets().size();
						weightUsed += r.getCurrentWeight();
						System.out.println(r + " " + r.getSequence() + " Pallets: (" + r.getOpPalletsOfType(0).size() + " " + r.getOpPalletsOfType(1).size() + " " + r.getOpPalletsOfType(2).size() + " " + r.getOpPalletsOfType(3).size() + " " + r.getOpPalletsOfType(4).size() + " " + r.getOpPalletsOfType(5).size() + ") Weight: " + r.getCurrentWeight() + " (" + r.getSequence().getMaxWeight() + ")" + " Valid: " + r.isValid());
					}
					System.out.println("Pallets used: " + palletsUsed);
					System.out.println("Weight used: " + weightUsed);
					
					System.out.print("Pallets: (");
					System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 0).count() + " ");
					System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 1).count() + " ");
					System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 2).count() + " ");
					System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 3).count() + " ");
					System.out.print(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 4).count() + " ");
					System.out.println(solved.getOpPallets().stream().filter(p -> p.getRow() == null && p.getType() == 5).count() + ")");

					System.out.println(solved.getOpPallets().stream().filter(p -> p.getRow() == null).toList());*/
					
					return true;
				}
		
		return false;
	}
}
