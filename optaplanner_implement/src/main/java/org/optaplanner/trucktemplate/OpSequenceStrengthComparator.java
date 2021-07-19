package org.optaplanner.trucktemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class OpSequenceStrengthComparator implements Comparator<OpSequence> {
	
	@Override
	public int compare(OpSequence a, OpSequence b) {
		if(a.isWildcard() && b.isWildcard()) return 0;
		if(a.isWildcard()) return 1;
		if(b.isWildcard()) return -1;

		return new CompareToBuilder()
				.append(IntStream.of(a.getPalletCountsMax()).sum(), IntStream.of(b.getPalletCountsMax()).sum())
				////.append(a.maxPossibleWeight(), b.maxPossibleWeight())
				.append(Arrays.stream(a.getPalletCountsMax()).max().getAsInt()-Arrays.stream(a.getPalletCountsMax()).min().getAsInt(), Arrays.stream(b.getPalletCountsMax()).max().getAsInt()-Arrays.stream(b.getPalletCountsMax()).min().getAsInt())
				.append(a.getId(), b.getId())
				.toComparison();
	}
}
