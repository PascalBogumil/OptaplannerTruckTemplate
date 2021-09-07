package org.implementation.trucktemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.Random;

import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;

import com.github.javaparser.ast.expr.ThisExpr;

public class OpRowMoveIteratorFactory implements MoveIteratorFactory<TruckTemplateSolution, OpRowChangeMove> {

	@Override
	public Iterator<OpRowChangeMove> createOriginalMoveIterator(ScoreDirector<TruckTemplateSolution> solution) {
		throw new UnsupportedOperationException();
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
		private static boolean firstCalculation = false;

		private final Random rand;
		private final ScoreDirector<TruckTemplateSolution> solution;		
		private final List<OpPallet> pallets;
		private final List<OpRow> rows;
		private final List<OpRow> candidateRows;
		
		public OpRowChangeMoveIterator (ScoreDirector<TruckTemplateSolution> solution) {
			this(solution, null);
		}
		
		public OpRowChangeMoveIterator (ScoreDirector<TruckTemplateSolution> solution, Random rand) {
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
			long palletsWithoutRow = pallets.stream().filter(p -> p.getRow() == null).count();
			return !firstCalculation || palletsWithoutRow != 0 && candidateRows.size() > 0;
		}
		
		@Override
		public OpRowChangeMove next() {
			//Don't change the row for the first calculation
			if(!firstCalculation) {
				firstCalculation = true;
				return new OpRowChangeMove(rows.get(0), rows.get(0).getCurrentSequence());
			}
			
			OpRowChangeMove move = null;
			
			//Random row and sequence selection
			if(rand != null) {
				do {
					int randomRowIndex = rand.nextInt(candidateRows.size());
				    OpRow randomRow = candidateRows.get(randomRowIndex);
				    
				    int numberOfSequences = (int)randomRow.getSequences().stream().filter(s -> !Objects.equals(s, randomRow.getCurrentSequence())).count();
				    int randomSequenceIndex = rand.nextInt(numberOfSequences);
				    
				    OpSequence randomSequence = randomRow.getSequences().stream().filter(s -> !Objects.equals(s, randomRow.getCurrentSequence())).toList().get(randomSequenceIndex);
				    move = new OpRowChangeMove(randomRow, randomSequence);
				} while(!move.isMoveDoable(solution));
			} 
			
			return move;
		}
	}
}