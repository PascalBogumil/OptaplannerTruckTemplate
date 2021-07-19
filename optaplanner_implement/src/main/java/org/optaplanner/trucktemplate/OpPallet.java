package org.optaplanner.trucktemplate;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@PlanningEntity
public class OpPallet extends AbstractPersistable implements Comparable<OpPallet>{
	
	private int weight;
	private int type;
	
	@PlanningVariable(valueRangeProviderRefs = "rows", nullable = true)
	private OpRow row;
		
	public OpPallet () {

	}
	
	public OpPallet(long id, int weight, int type) {
		super(id);
		this.weight = weight;
		this.type = type;
	}
	
	//Simple Getter & Setter
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