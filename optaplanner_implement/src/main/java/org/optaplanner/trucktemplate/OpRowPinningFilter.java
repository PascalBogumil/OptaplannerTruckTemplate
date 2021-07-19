package org.optaplanner.trucktemplate;

import org.optaplanner.core.api.domain.entity.PinningFilter;

public class OpRowPinningFilter implements PinningFilter<TruckTemplateSolution, OpRow> {

	@Override
	public boolean accept(TruckTemplateSolution arg0, OpRow row) {
		return row.isValid();
		//return false;
	}

}
