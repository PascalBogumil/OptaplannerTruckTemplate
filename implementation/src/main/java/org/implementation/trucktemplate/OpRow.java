package org.implementation.trucktemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@PlanningEntity
@XStreamAlias("OpRow")
public class OpRow extends AbstractPersistable implements Comparable<OpRow> {

	@ValueRangeProvider(id = "sequences")
	private List<OpSequence> sequences;
	
	@PlanningVariable(valueRangeProviderRefs = "sequences")
	private OpSequence currentSequence;
	
	@InverseRelationShadowVariable(sourceVariableName = "row")
	private List<OpPallet> opPallets;
	
	public OpRow () { }
	
	public OpRow (long id, List<OpSequence> sequences) {
		this(id, sequences, false);
	}
	
	
	public OpRow (long id, List<OpSequence> sequences, boolean useWildcard) {
		super(id);
		this.opPallets = new ArrayList<>();
		this.sequences = sequences;
		
		if (useWildcard) this.sequences.add(new OpSequence(this.sequences.size()));
		
		Collections.sort(this.sequences);
		this.currentSequence = sequences.get(0);
	}
	
	//Complex Methods
	
	public int getNormalizedValueOfPallets() {
		return getOpPallets().stream().mapToInt(OpPallet::getSize).sum();
	}	

	
	public boolean isValid() {
		if(currentSequence.isWildcard()) 
			return getNormalizedValueOfPallets() <= OpSequence.getWildcardSpace() && getCurrentWeight() <= OpSequence.getWildcardWeight();
		
		if(getCurrentWeight() > currentSequence.getMaxLoadCapacity())
			return false;	
		
		for(int i = 0; i < OpPallet.getNumberOfTypes(); i++)
			if(getOpPalletsOfType(i).size() != currentSequence.getPalletsNeededOfType(i))
				return false;
		
		return true;
	}
	
	public int getCurrentWeight() {
		return getOpPallets().stream().collect(Collectors.summingInt(OpPallet::getWeight));
	}

	public List<OpSequence> getListAfterCurrentSequence() {
		return sequences.stream().filter(s -> s.compareTo(currentSequence) == 1).toList();
	}
	
	public List<OpPallet> getOpPalletsOfType(int type) {
		return getOpPallets().stream().filter(p -> p.getType() == type).toList();
	}

	public List<OpPallet> getOpPallets() {
		if(opPallets == null) opPallets = new ArrayList<>();
		return opPallets;
	}

	public void setOpPallets(List<OpPallet> opPallets) {
		this.opPallets = opPallets;
	}	
	
	public OpSequence getCurrentSequence() {
		return currentSequence;
	}

	public void setCurrentSequence(OpSequence newSequence) {
		this.currentSequence = newSequence;
	}
	
	public List<OpSequence> getSequences() {
		return sequences;
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
				.append(getId(), o.getId())
				.toComparison();
	}
}
