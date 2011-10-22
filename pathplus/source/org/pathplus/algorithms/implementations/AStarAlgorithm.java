package org.pathplus.algorithms.implementations;

import java.util.Hashtable;
import java.util.PriorityQueue;

import org.pathplus.algorithms.BaseAlgorithm;
import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.state.BaseState;

public class AStarAlgorithm extends BaseAlgorithm {

	private PriorityQueue<BaseState> forwardOpenList = new PriorityQueue<BaseState>();

	private Hashtable<Integer, BaseState> fol = new Hashtable<Integer, BaseState>();
	private Hashtable<Integer, BaseState> forwardClosedList = new Hashtable<Integer, BaseState>();

	BaseState endState = null;

	public PathResult search(BaseState start, BaseState goal) {

		start.setGoalState(goal);

		forwardOpenList.add(start);
		fol.put(start.getKey(), start);

		if (forwardSearch(goal)) {
			// TODO Implement the return of a PathResult.
			return getPath(endState);
		} else
			return null;
	}

	private PathResult getPath(BaseState endState2) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean forwardSearch(BaseState goalState) {

		BaseState current = null;

		while (forwardOpenList.size() > 0) {

			current = forwardOpenList.poll();

			fol.remove(current.getKey());
			forwardClosedList.put(current.getKey(), current);

			BaseState[] neighbours = current.getNeighbours();

			// go through possible neighbours and add them if they are NOT in
			// the closed list or in the openlist
			for (int i = 0; i < neighbours.length; i++) {
				boolean isInClosed = false;

				/*
				 * check that new node isn't in closed list
				 */
				if (forwardClosedList.containsKey(neighbours[i].getKey())) {// check
					isInClosed = true;
				}

				if (isInClosed == false) {// check that new node isn't in
											// openlist
					if (fol.containsKey(neighbours[i].getKey()))
						isInClosed = true;
				}// end if

				if (isInClosed == false) {
					if (neighbours[i].getKey() == goalState.getKey()) {
						/*
						 * check if node is a goal
						 */
						endState = neighbours[i];
						return false;
					}
				}
				// now we know the node is not in open or closed and is not
				// goal, so we can add to openList

				if (isInClosed == false) {
					fol.put(neighbours[i].getKey(), neighbours[i]);
					forwardOpenList.add(neighbours[i]);
				}// end if
			}// end outter for
		}// END WHILE LOOP

		return true;
	}

}
