package org.implementation.trucktemplate.unused;

import org.implementation.trucktemplate.TruckTemplateSolution;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

public class TruckTemplateXMLSolutioneFileIO extends XStreamSolutionFileIO<TruckTemplateSolution> {
	
	public TruckTemplateXMLSolutioneFileIO() {
		super(TruckTemplateSolution.class);
	}
}
