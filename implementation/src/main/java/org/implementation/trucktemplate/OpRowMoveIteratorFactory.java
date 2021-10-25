package org.implementation.trucktemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;

public class OpRowMoveIteratorFactory implements MoveIteratorFactory<TruckTemplateSolution, OpRowChangeMove> {

	private boolean firstCalculation = false;
	private List<String> alreadyVisitedConfiguration = new ArrayList<String>();
	
	@Override
	public Iterator<OpRowChangeMove> createOriginalMoveIterator(ScoreDirector<TruckTemplateSolution> solution) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<OpRowChangeMove> createRandomMoveIterator(ScoreDirector<TruckTemplateSolution> solution, Random rand) {
		return new OpRowChangeMoveIterator(solution, rand, this);
	}

	@Override
	public long getSize(ScoreDirector<TruckTemplateSolution> solution) {
		return (long) Math.pow(solution.getWorkingSolution().getOpRows().stream().map(OpRow::getSequences).mapToInt(List::size).max().getAsInt(), solution.getWorkingSolution().getOpRows().size());
	}

	public static class OpRowChangeMoveIterator implements Iterator<OpRowChangeMove> {
		OpRowMoveIteratorFactory opRowMoveIteratorFactory;
		private final Random rand;
		private final ScoreDirector<TruckTemplateSolution> solution;		
		private final List<OpPallet> pallets;
		private final List<OpRow> rows;
		private final List<OpRow> candidateRows;
		
		public OpRowChangeMoveIterator (ScoreDirector<TruckTemplateSolution> solution, Random rand, OpRowMoveIteratorFactory opRowMoveIteratorFactory) {
			this.opRowMoveIteratorFactory = opRowMoveIteratorFactory;
			this.rand = rand;
			this.solution = solution;
			this.pallets = solution.getWorkingSolution().getOpPallets();
			this.rows = solution.getWorkingSolution().getOpRows();			
			this.candidateRows = this.rows.stream().filter(r -> r.getSequences().size() > 1).collect(Collectors.toList());
			Collections.sort(pallets);
			Collections.sort(rows);
		}
		
		@Override
		public boolean hasNext() {
            //long palletsWithoutRow = pallets.stream().filter(p -> p.isUnassigned()).count();
            boolean rowsWithMoreThanOneSequence = candidateRows.size() > 0;
            boolean visitedLessConfigurationsThanAvailable = opRowMoveIteratorFactory.alreadyVisitedConfiguration.size() <= opRowMoveIteratorFactory.getSize(solution);
            return (!opRowMoveIteratorFactory.firstCalculation || rowsWithMoreThanOneSequence) && visitedLessConfigurationsThanAvailable;
		}
		
		@Override
		public OpRowChangeMove next() {
			//Don't change the row for the first calculation
			if(!opRowMoveIteratorFactory.firstCalculation) {
				opRowMoveIteratorFactory.firstCalculation = true;
				return new OpRowChangeMove(rows.get(0), rows.get(0).getCurrentSequence());
			}
			
			OpRowChangeMove move = null;
			
			//Random row and sequence selection
			//int countOfSelectedMoves = 0;
			if(rand != null) {
				do {
					int randomRowIndex = rand.nextInt(candidateRows.size());
				    OpRow randomRow = candidateRows.get(randomRowIndex);
				    
				    int numberOfSequences = (int)randomRow.getSequences().stream().filter(s -> !Objects.equals(s, randomRow.getCurrentSequence())).count();
				    int randomSequenceIndex = rand.nextInt(numberOfSequences);
				    
				    OpSequence randomSequence = randomRow.getSequences().stream().filter(s -> !Objects.equals(s, randomRow.getCurrentSequence())).toList().get(randomSequenceIndex);
				    move = new OpRowChangeMove(randomRow, randomSequence);
				    
				    if(!opRowMoveIteratorFactory.alreadyVisitedConfiguration.contains(solution.getWorkingSolution().toString() + ":" + move.toString()))
                        opRowMoveIteratorFactory.alreadyVisitedConfiguration.add(solution.getWorkingSolution().getOpRows().stream().map(OpRow::getCurrentSequence).collect(Collectors.toList())+ ":" + move.toString());
				} while(!move.isMoveDoable(solution));// && countOfSelectedMoves++ < 100000);
			} 
			
			return move;
		}
	}
}