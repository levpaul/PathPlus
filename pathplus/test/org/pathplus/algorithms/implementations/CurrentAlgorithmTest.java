package org.pathplus.algorithms.implementations;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.pathplus.algorithms.implementations.BiMaxAlgorithm;
import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.state.implementations.EightPuzzleState;

public class CurrentAlgorithmTest {

	private BiMaxAlgorithm<EightPuzzleState> algorithm;
	private EightPuzzleState start, goal;

	/**
	 * Does the search.
	 */
	@Test(timeout = 10000)
	public void doSearch() {
		algorithm = new BiMaxAlgorithm<EightPuzzleState>();

		// The easiest of Korf's 100 test problem set. (ref)
		int[][] startStateArray = { { 2, 12, 14, 15 }, { 1, 13, 5, 11 },
				{ 3, 10, 8, 6 }, { 0, 9, 7, 4 } };
		int[][] goalStateArray = { { 13, 9, 5, 1 }, { 14, 10, 6, 2 },
			{ 15, 11, 7, 3 }, { 0, 12, 8, 4 } };

		start = new EightPuzzleState(startStateArray, goalStateArray);
		goal = new EightPuzzleState(goalStateArray, goalStateArray);

		// Execute the search.
		PathResult<EightPuzzleState> result = algorithm.search(start, goal);

		// Check the result is correct.
		assertTrue(result.getLength() == 33.0);
	}

}
