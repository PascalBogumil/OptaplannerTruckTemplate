package org.optaplanner.trucktemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity(difficultyComparatorClass = OpRowDifficultyComparator.class)
public class OpRow implements Comparable<OpRow>{
	
	@PlanningId
	private long id;

	//Fixed
	//private final static int WildcardWeight = 1200;
	//private final static int WildcardNorm = 12;
	
	@ValueRangeProvider(id = "sequences")
	private List<OpSequence> sequences;
	
	@InverseRelationShadowVariable(sourceVariableName = "row")
	private List<OpPallet> opPallets;
	
	//@PlanningVariable(valueRangeProviderRefs = "sequences")
	//private Integer sequence;

	@PlanningVariable(valueRangeProviderRefs = "sequences", strengthComparatorClass = OpSequenceStrengthComparator.class)
	private OpSequence sequence;
	
	public OpRow () {

	}
		
	public OpRow (long id, List<OpSequence> sequences, int maxSequence) {
		this.id = id;
		this.opPallets = new ArrayList<>();
		this.sequences = sequences;
		this.sequences.add(new OpSequence(sequences.size()));
		sequences.sort(new OpSequenceStrengthComparator());
		//Collections.sort(this.sequences);
		this.sequence = sequences.get(0);
		//int i = sequences.size();
		//while(sequences.size() < maxSequence)
			//sequences.add(new OpSequence(i++, 0, new int[] {0,0,0,0,0,0}));
	}
	
	//Complex Methods
	//@ValueRangeProvider(id = "sequences")
	/*public CountableValueRange<Integer> getMaxSequence() {
		return ValueRangeFactory.createIntValueRange(-1, 5); //exclusive upper bound
	}
	
	public Set<Integer> getNeededTypes(Integer sequence) {
		return (!useWildcard(sequence)) ? sequences.get(sequence).getCountPerType().keySet() : Collections.emptySet();
	}
	
	public Set<Integer> getNeededTypes() {
		return getNeededTypes(getSequence());
	}
	
	public Integer getMaxWeight(Integer sequence) {
		return (!useWildcard(sequence)) ? sequences.get(sequence).getMaxWeight() : WildcardWeight;
	}
	
	public Integer getMaxWeight() {
		return getMaxWeight(getSequence());
	}
	
	public int[] getPalletCountsMax(Integer sequence) {
		return (!useWildcard(sequence)) ? sequences.get(sequence).getPalletCountsMax() : new int[] {0, 0, 0, 0, 0, 0};
	}
	
	public int[] getPalletCountsMax() {
		return getPalletCountsMax(getSequence());
	}
	
	public int getNormalizedValueOfPallets() {
		return getOpPallets().stream().mapToInt(OpPallet::getNormalizedValue).sum();
	}
	
	public boolean isValid() {
		return isValid(getSequence());
	}
	
	public boolean isValid(Integer sequence) {
		if(getCurrentWeight() > getMaxWeight())
			return false;	
		
		if(useWildcard(sequence)) 
			return getNormalizedValueOfPallets() <= getWildcardNorm();
		
		int sum = 0;
		for(int type : getNeededTypes(sequence))
		{
			if(getOpPalletsOfType(type).size() != getPalletCountsMax(sequence)[type])
				return false;
			
			sum += getOpPalletsOfType(type).size();
		}
		
		return getOpPallets().size() <= sum;
	}
	
	public int getCurrentWeight() {
		return getOpPallets().stream().collect(Collectors.summingInt(OpPallet::getWeight));
	}
	
	public int countOfNeededPallets(Integer sequence) {
		return sequences.get(sequence).getCountPerType().values().stream().collect(Collectors.summingInt(Integer::intValue));
	}
	
	public int countOfNeededPallets() {
		return sequences.get(getSequence()).getCountPerType().values().stream().collect(Collectors.summingInt(Integer::intValue));
	}

	public boolean useWildcard(Integer sequence) {
		return sequence == -1;
	}
	
	public boolean useWildcard() {
		return useWildcard(getSequence());
	}*/
	
	/*public Integer getMaxWeight() {
		return sequence.getMaxWeight();
	}
	
	public int[] getPalletCountsMax() {
		return sequence.getPalletCountsMax();
	}*/
	
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
	
	/*public boolean useWildcard() {
		return sequence.isWildcard();
	}*/
	
	public Set<Integer> getNeededTypes() {
		return sequence.getCountPerType().keySet();
	}

	//Simple Getter & Setter
	public long getId() {
		return id;
	}
	
	public List<OpPallet> getOpPalletsOfType(int type) {
		return opPallets.stream().filter(p -> p.getType() == type).toList();
	}

	public List<OpPallet> getOpPallets() {
		return opPallets;
	}

	public void setOpPallets(List<OpPallet> opPallets) {
		this.opPallets = opPallets;
	}	

	/*public int getWildcardNorm() {
		return OpSequence.getWildcardnorm();
	}*/
	
	/*public int getNumberOfSequences() {
		return sequences.size();
	}*/
	

	/*public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}*/
	
	public OpSequence getSequence() {
		return sequence;
	}

	public void setSequence(OpSequence sequence) {
		this.sequence = sequence;
	}
	
	//@ValueRangeProvider(id = "sequences")
	public List<OpSequence> getSequences() {
		//return sequences.subList((sequences.indexOf(sequence) + 1) % sequences.size(), sequences.size());
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
				.append(id, other.getId())
				//.append(opPallets, other.getOpPallets())
				//.append(sequence, other.getSequence())
				.append(sequences, other.getSequences())
				.isEquals();
	}
	
	@Override
	public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                //.append(opPallets)
                //.append(sequence)
                .append(sequences)
                .toHashCode();
	}
	
	@Override
	public String toString() {
		return "Row: " + id;
	}
	
	@Override
	public int compareTo(OpRow o) {
		return (int)getId() - (int)o.getId();
	}
}
