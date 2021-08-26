package org.optaplanner.trucktemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("OpSequence")
public class OpSequence extends AbstractPersistable implements Comparable<OpSequence> {
	
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
		super(id);
		this.maxWeight = maxWeight;
		this.palletCountsMax = palletCountsMax;
		this.isWildcard = false;
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
		Map<Integer, Integer> types = new HashMap<>();
		for (int i = 0; i < palletCountsMax.length; i++)
			//if(palletCountsMax[i] != 0)
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
		String counts = "";
		for (int i = 0; i < palletCountsMax.length; i++) {
			counts += palletCountsMax[i] + " ";
		}
		
		return "Sequence " + ((isWildcard()) ? "W" : getId() + ": " + maxWeight + " ( " + counts + ")");
	}

	@Override
	public int compareTo(OpSequence o) {	
		if(isWildcard() && o.isWildcard()) return 0;
		if(isWildcard()) return 1;
		if(o.isWildcard()) return -1;
		
		return new CompareToBuilder()
				//.append(IntStream.of(getPalletCountsMax()).sum(), IntStream.of(o.getPalletCountsMax()).sum())
				////.append(a.maxPossibleWeight(), b.maxPossibleWeight())
				//.append(Arrays.stream(getPalletCountsMax()).max().getAsInt()-Arrays.stream(getPalletCountsMax()).min().getAsInt(), Arrays.stream(o.getPalletCountsMax()).max().getAsInt()-Arrays.stream(o.getPalletCountsMax()).min().getAsInt())
				.append(getId(), o.getId())
				.toComparison();
	}

	public static int getWildcardNorm() {
		return WildcardNorm;
	}
}
