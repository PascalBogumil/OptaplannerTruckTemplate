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
				isConfigurationValid(factory),
				usedPalletWeight(factory),
				//usedWildcardRow(factory),
		};
	}

	//Hard Constraints	
    private Constraint isConfigurationValid(ConstraintFactory factory) {
        return factory.from(OpRow.class)
        		.filter(r -> !r.isValid())
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
