/*********************************************
 * OPL 20.1.0.0 Model
 * Author: Pascal
 * Creation Date: 28.04.2021 at 16:08:34
 *********************************************/
 
/*
range maxPallet = 1..4; // input
range differentTypesCount = 1..4; //const
range rowCount = 1..3; // input

int weight[maxPallet][differentTypesCount] = [[300, 100, 200, 100],
											  [400, 300, 250, 100],
											  [500, 200, 125, 100],
											  [0, 200, 0, 100]];
int palletCountContraint[rowCount][differentTypesCount] = [[2, 1, 1, 1],
														   [1, 2, 2, 1],
														   [0, 1, 0, 2]];
int maxWeightPerColumn[rowCount] = [1200, 1275, 500];
int normalizedPalletCountContraint[rowCount] = [12, 11, 4];
int normalizedValueOfType[differentTypesCount] = [4, 2, 1, 1];
*/
 
int maxPallet = ...;
int rows = ...;
int maxSequences = ...;
 
// parameters
range differentTypesRange = 1..6; //const
range maxPalletRange = 1..maxPallet; // input max(#Typ1, #Typ2, ...)
range rowRange = 1..rows; // input
range maxSequenceRange = 1..maxSequences; // input

int weight[maxPalletRange][differentTypesRange] = ...;
int palletCounts[rowRange][maxSequenceRange][differentTypesRange] = ...;
int maxWeightPerColumn[rowRange][maxSequenceRange] = ...;
int normalizedValueOfType[differentTypesRange] = ...;

int M = ...;
int N = ...;
int U = ...;

// variables
dvar boolean x[rowRange][maxSequenceRange][maxPalletRange][differentTypesRange];
dvar boolean y[rowRange][maxSequenceRange];

dvar boolean v[rowRange][maxPalletRange][differentTypesRange];
dvar boolean w[rowRange];

// integer programm
maximize sum (k in rowRange, l in maxSequenceRange, j in maxPalletRange, i in differentTypesRange) x[k][l][j][i] * weight[j][i] + sum (k in rowRange, j in maxPalletRange, i in differentTypesRange) v[k][j][i] * weight[j][i] - U * sum (k in rowRange) w[k];

subject to {
  forall (k in rowRange)
    sequence_or_wildcard_constraint:
    w[k] + sum(l in maxSequenceRange) y[k][l] == 1;
  forall (k in rowRange)
    only_one_sequence_constraint:
    sum(l in maxSequenceRange) y[k][l] <= 1;  
  forall (k in rowRange, l in maxSequenceRange, j in maxPalletRange, i in differentTypesRange)
    only_pallets_for_selected_sequence_constraint:
    x[k][l][j][i] <= y[k][l];
  forall (k in rowRange, l in maxSequenceRange)
    weight_constraint:
    sum (j in maxPalletRange, i in differentTypesRange) x[k][l][j][i]*weight[j][i] <= y[k][l] * maxWeightPerColumn[k][l]; 
  forall (k in rowRange, l in maxSequenceRange, i in differentTypesRange)
    count_constraint:
    sum (j in maxPalletRange) x[k][l][j][i] == y[k][l] * palletCounts[k][l][i];
  forall (k in rowRange, j in maxPalletRange, i in differentTypesRange)
    wildcard_only_pallets_for_selected_wildcard_constraint:
    v[k][j][i] <= w[k];
  forall (k in rowRange)
    wildcard_weight_constraint:
    sum (j in maxPalletRange, i in differentTypesRange) v[k][j][i]*weight[j][i] <= w[k] * M; 
  forall (k in rowRange)
   	wildcard_count_constraint:
   	sum (i in differentTypesRange)((sum (j in maxPalletRange) v[k][j][i])*normalizedValueOfType[i]) <= w[k] * N;  
  forall (j in maxPalletRange, i in differentTypesRange)
    overlap_constraint:
    sum(k in rowRange) ((sum(l in maxSequenceRange) (x[k][l][j][i])) + v[k][j][i]) <= 1;
}


// postprocessing
execute {
	for(var k in rowRange) {
    	for(var l in maxSequenceRange) {
    		if(y[k][l] == 1) {
    		  	write("Row: ", k)
    		  	write(", Sequenz: ", l, ", ")
    		  	
				for(var j in maxPalletRange) {
				    for(var i in differentTypesRange) {
				    	if(x[k][l][j][i] == 1) {
				    		write("(", j, " ", i, " ", weight[j][i], "), ")
				    	}
				    }
				}
    		  	writeln()
    	  	}
    	}	
    	    	
		if(w[k] == 1) {
    		write("Row: ", k)
    		write(", Wildcard, ")
    		
			for(var j in maxPalletRange) {
			    for(var i in differentTypesRange) {
			    	if(v[k][j][i] == 1) {
			    		write("(", j, " ", i, " ", weight[j][i], "), ")
			    	}
			    }
			}
    		writeln()
    	}
    }
    
    var palletsUsed = 0
    var summedWeight = 0
	for(var k in rowRange) {
    	for(var l in maxSequenceRange) {
			for(var j in maxPalletRange) {
    			for(var i in differentTypesRange) {
    			  	palletsUsed += x[k][l][j][i]
					summedWeight += x[k][l][j][i]*weight[j][i] 
    			}
    		}
    	}
    }
    
    for(var k in rowRange) {
		for(var j in maxPalletRange) {
    		for(var i in differentTypesRange) {
				palletsUsed += v[k][j][i]
				summedWeight += v[k][j][i]*weight[j][i] 
    		}
    	}
    }
    
    writeln()
    writeln("Pallets used: ", palletsUsed)
    writeln("Weight used: ", summedWeight)
}