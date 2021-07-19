package org.optaplanner.trucktemplate;

import org.optaplanner.core.api.domain.entity.PinningFilter;

public class OpPalletPinningFilter implements PinningFilter<TruckTemplateSolution, OpPallet> {

	@Override
	public boolean accept(TruckTemplateSolution arg0, OpPallet pallet) {
		return false;
		/*if(pallet.getId() == 10)
			System.out.println(pallet.getRow());
		
		
		return (pallet.getRow() != null) ? pallet.getRow().isValid() : false;*/
	}

}
