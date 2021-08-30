package org.implementation.trucktemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;

import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;

public class OpRowMoveIteratorFactory implements MoveIteratorFactory<TruckTemplateSolution, OpRowChangeMove> {

	@Override
	public Iterator<OpRowChangeMove> createOriginalMoveIterator(ScoreDirector<TruckTemplateSolution> solution) {
		return new OpRowChangeMoveIterator(solution);
	}

	@Override
	public Iterator<OpRowChangeMove> createRandomMoveIterator(ScoreDirector<TruckTemplateSolution> solution, Random rand) {
		return new OpRowChangeMoveIterator(solution, rand);
	}

	@Override
	public long getSize(ScoreDirector<TruckTemplateSolution> solution) {
		return solution.getWorkingSolution().getOpRows().size()^solution.getWorkingSolution().getOpRows().get(0).getSequences().size();
	}

	public static class OpRowChangeMoveIterator implements Iterator<OpRowChangeMove> {
		private final List<OpPallet> pallets;
		private final List<OpRow> rows;
		private final ScoreDirector<TruckTemplateSolution> solution;
		private final Random rand;
		//private Map<OpRow, Iterator<OpSequence>> sequenceIteratorOfRows;
		//private Map<OpRow, OpSequence> nextSequencesOfRow;
		//private Iterator<Entry<OpRow, OpSequence>> nextSequencesOfRowIterator;

		
		
		private static boolean firstCalculation = false;
		
		public OpRowChangeMoveIterator (ScoreDirector<TruckTemplateSolution> solution) {
			this(solution, null);
			
			/*sequenceIteratorOfRows = new HashMap<>();
			for(int i = 0; i < rows.size(); i++)
				sequenceIteratorOfRows.put(rows.get(i), rows.get(i).getListAfterCurrentSequence().iterator());*/
			
			/*nextSequencesOfRow = new HashMap<>();
			for(OpRow row : rows)
				if(!row.getListAfterCurrentSequence().isEmpty()) {
					nextSequencesOfRow.put(row, row.getListAfterCurrentSequence().get(0));
				}
			
			nextSequencesOfRowIterator = nextSequencesOfRow.entrySet().iterator();*/
			
			/*this.rand = null;
			this.solution = solution;		
			pallets = solution.getWorkingSolution().getOpPallets();
			rows = solution.getWorkingSolution().getOpRows();			
			
			Collections.sort(pallets);
			Collections.sort(rows);*/
		}
		
		public OpRowChangeMoveIterator (ScoreDirector<TruckTemplateSolution> solution, Random rand) {
			this.rand = rand;
			this.solution = solution;
			this.pallets = solution.getWorkingSolution().getOpPallets();
			this.rows = solution.getWorkingSolution().getOpRows();			
			
			Collections.sort(pallets);
			Collections.sort(rows);
		}
		
		@Override
		public boolean hasNext() {
			long palletsWithNoRow = pallets.stream().filter(p -> p.getRow() == null).count();
			boolean usedAllPallets = palletsWithNoRow == 0;
			
			return !usedAllPallets;// && (rand == null) ? nextSequencesOfRowIterator.hasNext() : true;
		}
		
		@Override
		public OpRowChangeMove next() {
			//Don't change the row for the first calculation
			if(!firstCalculation) {
				firstCalculation = true;
				return new OpRowChangeMove(rows.get(0), rows.get(0).getCurrentSequence());
			}
			
			OpRowChangeMove move = null;
			if(rand != null) {
				//Random row and sequence selection
				do {
					int randomRowIndex = rand.nextInt(rows.size());
				    OpRow randomRow = rows.get(randomRowIndex);
				    
				    int numberOfSequences = (int)randomRow.getSequences().stream().filter(s -> !Objects.equals(s, randomRow.getCurrentSequence())).count();
				    int randomSequenceIndex = rand.nextInt(numberOfSequences);
				    
				    OpSequence randomSequence = randomRow.getSequences().stream().filter(s -> !Objects.equals(s, randomRow.getCurrentSequence())).toList().get(randomSequenceIndex);
				    move = new OpRowChangeMove(randomRow, randomSequence);
				} while(!move.isMoveDoable(solution));
			//Original order of rows and sequence //Geht noch nicht
			} else {
				//Iterator<OpRow> iterRows = rows.iterator();
				//Iterator<OpSequence> iterSeqs = null;
				//OpRow currentRow = null;
				/*Iterator<Entry<OpRow, Iterator<OpSequence>>> sequenceIteratorsOfRowIterator = sequenceIteratorOfRows.entrySet().iterator();
				do {
					if(iterSeqs == null || !iterSeqs.hasNext()) {
						if(iterRows.hasNext()) {
							currentRow = iterRows.next();
							iterSeqs = currentRow.getListAfterCurrentSequence().iterator();
						}
					}
					
					OpSequence sequence = iterSeqs.next();
					
					if(!sequenceIteratorsOfRowIterator.hasNext()) {
						sequenceIteratorsOfRowIterator = sequenceIteratorOfRows.entrySet().iterator();
					}
					
					Entry<OpRow, Iterator<OpSequence>> sequenceIteratorOfRow = sequenceIteratorsOfRowIterator.next();
					
					if(!sequenceIteratorOfRow.getValue().hasNext()) {
						//sequenceIteratorOfRows.put(sequenceIteratorOfRow.getKey(), sequenceIteratorOfRow.getKey().getListAfterCurrentSequence().iterator());
						continue;
					}
					
					move = new OpRowChangeMove(sequenceIteratorOfRow.getKey(), sequenceIteratorOfRow.getValue().next());
				} while(move == null || !move.isMoveDoable(solution));*/
				
				//Entry<OpRow, OpSequence> newSequenceOfRow = nextSequencesOfRowIterator.next();
				//move = new OpRowChangeMove(newSequenceOfRow.getKey(), newSequenceOfRow.getValue());
			}
			
			return move;
		}
	}
}