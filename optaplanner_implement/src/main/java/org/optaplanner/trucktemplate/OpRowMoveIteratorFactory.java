package org.optaplanner.trucktemplate;

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

	@Override
	public Iterator<OpRowChangeMove> createOriginalMoveIterator(ScoreDirector<TruckTemplateSolution> solution) {
		return new OpRowChangeMoveIterator(solution.getWorkingSolution());
	}

	@Override
	public Iterator<OpRowChangeMove> createRandomMoveIterator(ScoreDirector<TruckTemplateSolution> solution, Random rand) {
		return new OpRowChangeMoveIterator(solution.getWorkingSolution(), rand);
	}

	@Override
	public long getSize(ScoreDirector<TruckTemplateSolution> solution) {
		return solution.getWorkingSolution().getOpRows().size()^solution.getWorkingSolution().getOpRows().get(0).getSequences().size();
	}

	public static class OpRowChangeMoveIterator implements Iterator<OpRowChangeMove> {
		private List<OpPallet> pallets;
		private List<OpRow> rows;
		private Random rand;
		
		
		public OpRowChangeMoveIterator (TruckTemplateSolution solution) {
			pallets = solution.getOpPallets().stream()
					  .collect(Collectors.toList());
			rows = solution.getOpRows().stream()
					  .collect(Collectors.toList());			
			
			Collections.sort(pallets);
			Collections.sort(rows);
		}
		
		public OpRowChangeMoveIterator (TruckTemplateSolution solution, Random rand) {
			this.rand = rand;
			pallets = solution.getOpPallets();
			rows = solution.getOpRows();			
			
			Collections.sort(pallets);
			Collections.sort(rows);
		}
		
		@Override
		public boolean hasNext() {
			return rows.iterator().hasNext();
		}

		@Override
		public OpRowChangeMove next() {		
			if(rand != null) {
			    OpRow randomRow = rows.get(rand.nextInt(rows.size()));
			    OpSequence randomSequence = randomRow.getSequences().stream().filter(s -> !Objects.equals(s, randomRow.getSequence())).toList().get(rand.nextInt((int)randomRow.getSequences().stream().filter(s -> !Objects.equals(s, randomRow.getSequence())).count()));
				
				return new OpRowChangeMove(randomRow, randomSequence);
			}
			
			OpRow row = rows.iterator().next();
			Collections.sort(row.getSequences());
			OpSequence sequence = row.getSequences().stream().filter(s -> !Objects.equals(s, row.getSequence()) && row.getSequences().indexOf(s) > row.getSequences().indexOf(row.getSequence())).findFirst().orElse(null);
			sequence = (sequence == null) ? row.getSequences().get(0) : sequence;
				
			rows.add(rows.remove(0));
			
			return new OpRowChangeMove(row, sequence);
		}
	}
}