package org.pathplus.algorithms.implementations;

import static org.junit.Assert.*;

import org.junit.Test;
import org.pathplus.algorithms.implementations.AStarAlgorithm;
import org.pathplus.utils.path.PathResult;

public class AStarAlgorithmTest {

	@Test
	public void testSearch() {
		AStarAlgorithm aStar = new AStarAlgorithm();

		int[][] startStateArray = { { 13, 9, 6, 5 }, { 14, 2, 10, 1 },
				{ 15, 3, 8, 4 }, { 0, 12, 11, 7 } };
		int[][] goalStateArray = { { 12, 8, 4, 0 }, { 13, 9, 5, 1 },
				{ 14, 10, 6, 2 }, { 15, 11, 7, 3 } };

		FifteenPuzzle start = new FifteenPuzzle(startStateArray);
		FifteenPuzzle goal = new FifteenPuzzle(goalStateArray);

		PathResult<FifteenPuzzle> result = aStar.search(start, goal);

		assertEquals(result.getLength(), 46.0);

	}

}
