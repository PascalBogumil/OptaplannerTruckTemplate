package org.optaplanner.trucktemplate;

import static java.util.Comparator.comparingInt;

import java.util.Comparator;

public class SequenceStrengthComparator implements Comparator<Integer> {
	private static final Comparator<Integer> COMPARATOR = comparingInt((Integer s) -> (s == null || s < 0) ? Integer.MAX_VALUE : s);
	
	@Override
	public int compare(Integer a, Integer b) {
		return COMPARATOR.compare(b, a);
	}

}
