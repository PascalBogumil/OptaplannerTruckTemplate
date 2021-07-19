 package org.optaplanner.trucktemplate;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class OpRowDifficultyComparator implements Comparator<OpRow> {
	
	@Override
	public int compare(OpRow a, OpRow b) {
		return new CompareToBuilder()
				////.append(Arrays.stream(a.getSequence().getPalletCountsMax()).sum(), Arrays.stream(b.getSequence().getPalletCountsMax()).sum())
				.append(a.getSequence().getMaxWeight(), b.getSequence().getMaxWeight())
				.append(a.getSequences().stream().mapToInt(s -> Arrays.stream(s.getPalletCountsMax()).sum()).sum(), b.getSequences().stream().mapToInt(s -> Arrays.stream(s.getPalletCountsMax()).sum()).sum())
				.append(a.getId(), b.getId())
				.toComparison();
	}
}
