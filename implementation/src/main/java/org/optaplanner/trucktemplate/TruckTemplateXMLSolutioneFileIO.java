package org.optaplanner.trucktemplate;

import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

public class TruckTemplateXMLSolutioneFileIO extends XStreamSolutionFileIO<TruckTemplateSolution> {
	
	public TruckTemplateXMLSolutioneFileIO() {
		super(TruckTemplateSolution.class);
	}
}
