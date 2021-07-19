package org.optaplanner.trucktemplate;

import static java.util.Comparator.comparingInt;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class OpPalletDifficultyComparator extends Object implements Comparator<OpPallet> {
    private static final Comparator<OpPallet> COMPARATOR = comparingInt((OpPallet p) -> -p.getWeight());

	@Override
	public int compare(OpPallet a, OpPallet b) {		
		return COMPARATOR.compare(a, b);
	}
}
