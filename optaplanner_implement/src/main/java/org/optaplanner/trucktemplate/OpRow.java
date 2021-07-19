package org.optaplanner.trucktemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@PlanningEntity
public class OpRow extends AbstractPersistable implements Comparable<OpRow>{
	@ValueRangeProvider(id = "sequences")
	private List<OpSequence> sequences;
	
	@InverseRelationShadowVariable(sourceVariableName = "row")
	private List<OpPallet> opPallets;

	@PlanningVariable(valueRangeProviderRefs = "sequences")
	private OpSequence sequence;
	
	public OpRow () {

	}
		
	public OpRow (long id, List<OpSequence> sequences, int maxSequence) {
		super(id);
		this.opPallets = new ArrayList<>();
		this.sequences = sequences;
		this.sequences.add(new OpSequence(sequences.size()));
		Collections.sort(this.sequences);
		this.sequence = sequences.get(0);
	}
	
	//Complex Methods
	
	public int getNormalizedValueOfPallets() {
		return getOpPallets().stream().mapToInt(OpPallet::getNormalizedValue).sum();
	}	
	
	public boolean isAnyOverFilled() {
		for(int i = 0; i < TruckTemplateSolution.getNumberOfTypes(); i++)
			if(isTypeOverFilled(i)) return true;
		return false;
	}
	
	public boolean isTypeFilled(int type) {
		return sequence.getPalletCountsMax()[type] == getOpPalletsOfType(type).size();
	}
	
	public boolean isTypeOverFilled(int type) {
		return sequence.getPalletCountsMax()[type] < getOpPalletsOfType(type).size();
	}
	
	public boolean isValid() {
		if(getCurrentWeight() > sequence.getMaxWeight())
			return false;	
		
		if(sequence.isWildcard()) 
			return getNormalizedValueOfPallets() <= OpSequence.getWildcardNorm();
		
		int sum = 0;
		for(int type : getNeededTypes())
		{
			if(getOpPalletsOfType(type).size() != sequence.getPalletCountsMax()[type])
				return false;
			
			sum += getOpPalletsOfType(type).size();
		}
		return true;
		//return getOpPallets().size() <= sum;
	}
	
	public int getCurrentWeight() {
		return getOpPallets().stream().collect(Collectors.summingInt(OpPallet::getWeight));
	}
	
	public int countOfNeededPallets() {
		return sequence.getCountPerType().values().stream().collect(Collectors.summingInt(Integer::intValue));
	}
	
	public Set<Integer> getNeededTypes() {
		return sequence.getCountPerType().keySet();
	}

	//Simple Getter & Setter
	public List<OpPallet> getOpPalletsOfType(int type) {
		return opPallets.stream().filter(p -> p.getType() == type).toList();
	}

	public List<OpPallet> getOpPallets() {
		return opPallets;
	}

	public void setOpPallets(List<OpPallet> opPallets) {
		this.opPallets = opPallets;
	}	
	
	public OpSequence getSequence() {
		return sequence;
	}

	public void setSequence(OpSequence sequence) {
		this.sequence = sequence;
	}
	
	public List<OpSequence> getSequences() {
		return sequences;
	}

	public void setSequences(List<OpSequence> sequences) {
		this.sequences = sequences;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if (obj == null || getClass() != obj.getClass())
            return false;
        
        OpRow other = (OpRow) obj;
		return new EqualsBuilder()
				.append(getId(), other.getId())
				.append(sequences, other.getSequences())
				.isEquals();
	}
	
	@Override
	public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .append(sequences)
                .toHashCode();
	}
	
	@Override
	public String toString() {
		return "Row: " + getId();
	}
	
	@Override
	public int compareTo(OpRow o) {
		return new CompareToBuilder()
				////.append(Arrays.stream(a.getSequence().getPalletCountsMax()).sum(), Arrays.stream(b.getSequence().getPalletCountsMax()).sum())
				.append(getSequence().getMaxWeight(), o.getSequence().getMaxWeight())
				.append(getSequences().stream().mapToInt(s -> Arrays.stream(s.getPalletCountsMax()).sum()).sum(), o.getSequences().stream().mapToInt(s -> Arrays.stream(s.getPalletCountsMax()).sum()).sum())
				.append(getId(), o.getId())
				.toComparison();
	}
}
