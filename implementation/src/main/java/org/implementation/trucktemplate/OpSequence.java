package org.implementation.trucktemplate;

import java.util.stream.IntStream;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("OpSequence")
public class OpSequence extends AbstractPersistable implements Comparable<OpSequence> {
	
	private final static int WildcardWeight = 1200;
	private final static int WildcardSpace = 12;
	
	private final int maxWeight;
	private final int maxSpace = WildcardSpace; // For XStream
	private final int maxWildcardWeight = WildcardWeight; // For XStream
	private final int[] palletsNeeded;
	private final boolean isWildcard;
	
	public OpSequence(long id) {
		this.id = id;
		this.isWildcard = true;
		
		//Not used in a wildcard
		this.maxWeight = 0;
		this.palletsNeeded = new int[] {0, 0, 0, 0, 0, 0};
	}
	
	public OpSequence(long id, int maxWeight, int[] palletCountsMax) {
		super(id);
		this.maxWeight = maxWeight;
		this.palletsNeeded = palletCountsMax;
		this.isWildcard = false;
	}

	public static int getWildcardWeight() {
		return WildcardWeight;
	}

	public static int getWildcardSpace() {
		return WildcardSpace;
	}
	
	public int getMaxLoadCapacity() {
		return maxWeight;
	}
	
	public int getPalletsNeededOfType(int type) {
		return palletsNeeded[type];
	}
	
	public int getNumberOfNeededPallets() {
		return IntStream.of(palletsNeeded).sum();
	}
	
	public int[] getPalletCountsMax() {
		return palletsNeeded;
	}
	
	public boolean isWildcard() {
		return isWildcard;
	}
	
	/*public int maxPossibleWeight() {
		int[] eachTypePossibleWeight = new int[6];
		
		for(int i = 0; i < palletsNeeded.length; i++) {
			eachTypePossibleWeight[i] = palletsNeeded[i]*OpPallet.getMaxWeightOfType(i);
		}
		
		return Arrays.stream(eachTypePossibleWeight).sum();
	}
	
	public Map<Integer, Integer> getCountPerType() {
		Map<Integer, Integer> types = new HashMap<>();
		for (int i = 0; i < palletsNeeded.length; i++)
			//if(palletCountsMax[i] != 0)
				types.put(i, palletsNeeded[i]);
		return types;
	}*/
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if (obj == null || getClass() != obj.getClass())
            return false;
        
        OpSequence other = (OpSequence) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				//.append(maxWeight, other.getMaxWeight())
				//.append(palletsNeeded, other.getPalletsNeededOfType())
				.isEquals();
	}
	
	@Override
	public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
				//.append(maxWeight)
				//.append(palletsNeeded)
                .toHashCode();
	}
	
	@Override
	public String toString() {
		String counts = "";
		for (int i = 0; i < palletsNeeded.length; i++) {
			counts += palletsNeeded[i] + " ";
		}
		
		return "Sequence " + ((isWildcard()) ? "W" : getId() + ": " + maxWeight + " ( " + counts + ")");
	}

	@Override
	public int compareTo(OpSequence o) {	
		if(isWildcard() && o.isWildcard()) return 0;
		if(isWildcard()) return -1;
		if(o.isWildcard()) return 1;
		
		return new CompareToBuilder()
				//.append(getNumberOfNeededPallets(), o.getNumberOfNeededPallets())
				////.append(getMaxLoadCapacity(), o.getMaxLoadCapacity())
				//.append(Arrays.stream(getPalletCountsMax()).max().getAsInt()-Arrays.stream(getPalletCountsMax()).min().getAsInt(), Arrays.stream(o.getPalletCountsMax()).max().getAsInt()-Arrays.stream(o.getPalletCountsMax()).min().getAsInt())
				.append(getId(), o.getId())
				.toComparison();
	}
}
