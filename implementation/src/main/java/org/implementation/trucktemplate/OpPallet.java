package org.implementation.trucktemplate;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@PlanningEntity
@XStreamAlias("OpPallet")
public class OpPallet extends AbstractPersistable implements Comparable<OpPallet>{
	
	private final static int[] normalizedTypeValues = new int[] {4, 2, 1, 1, 2, 4};
	private final static int[] maxWeightEachType = new int[]{850, 750, 375, 375, 750, 850};
	private final static int numberOfTypes = 6;
	
	private int weight;
	private int type;
	
	@PlanningVariable(valueRangeProviderRefs = "rows", nullable = true)
	private OpRow row;
		
	public OpPallet () { }
	
	public OpPallet(long id, int weight, int type) {
		super(id);
		this.weight = weight;
		this.type = type;
	}
	
	public static int getMaxWeightOfType(int type) {
		return maxWeightEachType[type];
	}
	
	public static int getNumberOfTypes() {
		return numberOfTypes;
	}
	
	public OpRow getRow() {
		return row;
	}

	public void setRow(OpRow row) {
		this.row = row;
	}

	public int getWeight() {
		return weight;
	}

	public int getType() {
		return type;
	}
	
	public int getSize () {
		return normalizedTypeValues[type];
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if (obj == null || getClass() != obj.getClass())
            return false;
        
        OpPallet other = (OpPallet) obj;
		return new EqualsBuilder()
				.append(getId(), other.getId())
				.isEquals();
	}
	
	@Override
	public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
	}
	
	@Override
	public String toString() {
		return "Pallet: " + getId() + " (" + type + " " + weight + ")";
	}
	
	@Override
	public int compareTo(OpPallet o) {
		return (int)getWeight() - (int)o.getWeight();
	}
}