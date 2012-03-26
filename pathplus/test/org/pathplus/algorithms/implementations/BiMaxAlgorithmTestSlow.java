package org.pathplus.algorithms.implementations;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.state.implementations.FifteenPuzzleState;

public class BiMaxAlgorithmTestSlow {

	@Test (timeout = 10000) 
	public void testSearch() {
		BiMaxAlgorithm<FifteenPuzzleState> biMax = new BiMaxAlgorithm<FifteenPuzzleState>();

		// The easiest of Korf's 100 test problem set. (ref)
		int[][] startStateArray = { { 2, 12, 14, 15 }, { 1, 13, 5, 11 },
				{ 3, 10, 8, 6 }, { 0, 9, 7, 4 } };

		// int[][] goalStateArray = { { 12, 8, 4, 0 }, { 13, 9, 5, 1 },
		// { 14, 10, 6, 2 }, { 15, 11, 7, 3 } }

		int[][] goalStateArray = { { 13, 9, 5, 1 }, { 14, 10, 6, 2 },
				{ 15, 11, 7, 3 }, { 0, 12, 8, 4 } };

		FifteenPuzzleState start = new FifteenPuzzleState(startStateArray,
				goalStateArray);
		FifteenPuzzleState goal = new FifteenPuzzleState(goalStateArray,
				goalStateArray);

		PathResult<FifteenPuzzleState> result = biMax.search(start, goal);

		assertTrue(result.getLength() == 42.0);

	}

}
