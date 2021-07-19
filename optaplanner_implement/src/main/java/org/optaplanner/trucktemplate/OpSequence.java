package org.optaplanner.trucktemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class OpSequence implements Comparable<OpSequence>{

	private long id;
	
	private int maxWeight;
	private int[] palletCountsMax;
	
	private final static int WildcardWeight = 1200;
	private final static int WildcardNorm = 12;
	private final boolean isWildcard;
	
	public OpSequence(long id) {
		this.id = id;
		this.maxWeight = WildcardWeight;
		this.palletCountsMax = new int[] {0, 0, 0, 0, 0, 0};
		this.isWildcard = true;
	}
	
	public OpSequence(long id, int maxWeight, int[] palletCountsMax) {
		this.id = id;
		this.maxWeight = maxWeight;
		this.palletCountsMax = palletCountsMax;
		this.isWildcard = false;
	}

	public long getId() {
		return id;
	}

	public int[] getPalletCountsMax() {
		return palletCountsMax;
	}
	
	public void setPalletCountsMax(int[] palletCountsMax) {
		this.palletCountsMax = palletCountsMax;
	}
	
	public int getMaxWeight() {
		return maxWeight;
	}
	
	public static int getWildcardweight() {
		return WildcardWeight;
	}

	public boolean isWildcard() {
		return isWildcard;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}
	
	public int maxPossibleWeight() {
		int[] eachTypePossibleWeight = new int[6];
		
		for(int i = 0; i < palletCountsMax.length; i++) {
			eachTypePossibleWeight[i] = palletCountsMax[i]*TruckTemplateSolution.getMaxWeightEachType()[i];
		}
		
		return Arrays.stream(eachTypePossibleWeight).sum();
	}
	
	public Map<Integer, Integer> getCountPerType() {
		if(isWildcard) return Collections.emptyMap();
		
		Map<Integer, Integer> types = new HashMap<>();
		for (int i = 0; i < palletCountsMax.length; i++)
			if(palletCountsMax[i] != 0)
				types.put(i, palletCountsMax[i]);
		return types;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if (obj == null || getClass() != obj.getClass())
            return false;
        
        OpSequence other = (OpSequence) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.append(maxWeight, other.getMaxWeight())
				.append(palletCountsMax, other.getPalletCountsMax())
				.isEquals();
	}
	
	@Override
	public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
				.append(maxWeight)
				.append(palletCountsMax)
                .toHashCode();
	}
	
	@Override
	public String toString() {
		return "Sequence: " + ((isWildcard()) ? "W" : id);
	}

	@Override
	public int compareTo(OpSequence o) {
		/*if(isWildcard && o.isWildcard) return 0;
		if(isWildcard) return 1;
		if(o.isWildcard) return -1;

		return -(IntStream.of(o.getPalletCountsMax()).sum() - IntStream.of(palletCountsMax).sum());*/
		return (int)getId() - (int)o.getId();
	}

	public static int getWildcardNorm() {
		return WildcardNorm;
	}
}
