package org.optaplanner.trucktemplate;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity(difficultyComparatorClass = OpPalletDifficultyComparator.class)
public class OpPallet implements Comparable<OpPallet>{
	
	@PlanningId
	private long id;
	
	private int weight;
	private int type;
	
	@PlanningVariable(valueRangeProviderRefs = "rows", nullable = true)
	private OpRow row;
		
	public OpPallet () {

	}
	
	public OpPallet(long id, int weight, int type) {
		this.id = id;
		this.weight = weight;
		this.type = type;
	}
	
	//Simple Getter & Setter
	public long getId() {
		return id;
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

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getNormalizedValue () {
		return TruckTemplateSolution.getNormalizedValueOfType(type);
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if (obj == null || getClass() != obj.getClass())
            return false;
        
        OpPallet other = (OpPallet) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				//.append(row, other.getRow())
				.isEquals();
	}
	
	@Override
	public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                //.append(row)
                .toHashCode();
	}
	
	@Override
	public String toString() {
		return "Pallet: " + id + " (" + type + " " + weight + ")";
	}
	
	@Override
	public int compareTo(OpPallet o) {
		return (int)getWeight() - (int)o.getWeight();
	}
}