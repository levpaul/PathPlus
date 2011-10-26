package org.pathplus.algorithms.implementations;

import static org.junit.Assert.*;

import org.junit.Test;
import org.pathplus.algorithms.implementations.AStarAlgorithm;
import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.state.implementations.FifteenPuzzleState;

public class AStarAlgorithmTest {

	@Test /*(timeout = 5000)*/
	public void testSearch() {
		AStarAlgorithm<FifteenPuzzleState> aStar = new AStarAlgorithm<FifteenPuzzleState>();

		int[][] startStateArray = { { 13, 9, 6, 5 }, { 14, 2, 10, 1 },
				{ 15, 3, 8, 4 }, { 0, 12, 11, 7 } };
		int[][] goalStateArray = { { 12, 8, 4, 0 }, { 13, 9, 5, 1 },
				{ 14, 10, 6, 2 }, { 15, 11, 7, 3 } };

		FifteenPuzzleState start = new FifteenPuzzleState(startStateArray, goalStateArray);
		FifteenPuzzleState goal = new FifteenPuzzleState(goalStateArray, goalStateArray);

		PathResult<FifteenPuzzleState> result = aStar.search(start, goal);

		assertTrue(result.getLength() == 46.0);

	}

}
