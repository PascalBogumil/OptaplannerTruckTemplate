package org.optaplanner.trucktemplate;

import java.util.stream.Collectors;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

public class TruckTemplateConstraints implements ConstraintProvider {

	@Override
	public Constraint[] defineConstraints(ConstraintFactory factory) {
		return new Constraint[] {
				//onlyOneSequencePerRow(factory),
				//maxWeightOfRow(factory),
				//maxPalletCountsOfRow(factory),
				//factory.from(OpRow.class).penalize("init", HardSoftScore.ONE_HARD),
				isConfigurationValid(factory),
				usedPalletWeight(factory),
				//usedWildcardRow(factory),
		};
	}

	//Hard Constraints
    /*private Constraint onlyOneSequencePerRow(ConstraintFactory factory) {
    	return factory.from(OpRow.class)
    			.filter(r -> r.getOpPallets().stream().map(OpPallet::getSequence).distinct().count() != 1) // Each row can have only pallets with same sequence
    			.penalize("only one sequence per row", HardSoftScore.ONE_HARD);
    }
    
    private Constraint maxWeightOfRow(ConstraintFactory factory) {
        return factory.from(OpRow.class)
        		.filter(r -> r.getOpPallets().stream().collect(Collectors.summingInt(OpPallet::getWeight)) > r.getMaxWeight())
        		.penalize("max weight of row", HardSoftScore.ONE_HARD);
    }
    
    private Constraint maxPalletCountsOfRow(ConstraintFactory factory) {
        return factory.from(OpRow.class)
        		.filter(r -> {
    				if(r.useWildcard()) 
        				return r.getWildcardNorm() < r.getNormalizedValueOfPallets();
        			
    				int sum = 0;
    				for(int type : r.getNeededTypes())
        			{
        				if(r.getOpPalletsOfType(type).size() != r.getPalletCountsMax()[type])
        					return true;
        				
        				sum += r.getOpPalletsOfType(type).size();
        			}
    				
        			return r.getOpPallets().size() > sum;
        		})
        		.penalize("max pallet counts of row", HardSoftScore.ONE_HARD);
    }*/
	
    private Constraint isConfigurationValid(ConstraintFactory factory) {
        return factory.from(OpRow.class)
        		//.filter(r -> r.getOpPallets().stream().map(OpPallet::getSequence).distinct().count() == 1) // Check if all pallets have the same sequence
        		//.filter(r -> r.getOpPallets().stream().collect(Collectors.summingInt(OpPallet::getWeight)) < r.getMaxWeight()) // Check if max weight is not exceeded
        		.filter(r -> {
        			/*if(r.getOpPallets().stream().collect(Collectors.summingInt(OpPallet::getWeight)) > r.getMaxWeight() || r.getOpPallets().stream().map(OpPallet::getSequence).distinct().count() != 1)
        				return true;	
        			
    				if(r.useWildcard()) 
        				return r.getWildcardNorm() != r.getNormalizedValueOfPallets();
        			
    				int sum = 0;
    				for(int type : r.getNeededTypes())
        			{
        				if(r.getOpPalletsOfType(type).size() != r.getPalletCountsMax()[type])
        					return true;
        				
        				sum += r.getOpPalletsOfType(type).size();
        			}
    				
        			return r.getOpPallets().size() > sum;*/
        			return !r.isValid();
        		})
        		.penalize("feasable configuration", HardSoftScore.ONE_HARD);
    }
    
    //Soft Constraints
    private Constraint usedPalletWeight(ConstraintFactory factory) {
        return factory.from(OpRow.class)
        		.filter(r -> !r.getSequence().isWildcard())
        		.reward("used pallet weight", HardSoftScore.ONE_SOFT, r -> r.getOpPallets().stream().collect(Collectors.summingInt(OpPallet::getWeight)));
    }
    
    private Constraint usedWildcardRow(ConstraintFactory factory) {
        return factory.from(OpRow.class)
        		.filter(r -> r.getSequence().isWildcard())
        		.penalize("used wildcard rows", HardSoftScore.ONE_SOFT, r -> TruckTemplateSolution.getWildcardPenaltyValue());
    }
    
}
