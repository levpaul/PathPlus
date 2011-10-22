package org.pathplus.algorithms.implementations;

import java.util.Hashtable;
import java.util.PriorityQueue;

import org.pathplus.algorithms.BaseAlgorithm;
import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.state.BaseState;

public class AStarAlgorithm extends BaseAlgorithm {

	private PriorityQueue<BaseState> forwardOpenList = new PriorityQueue<BaseState>();

	private Hashtable<Integer, BaseState> fol = new Hashtable<Integer, BaseState>(
			200000);
	private Hashtable<Integer, BaseState> forwardClosedList = new Hashtable<Integer, BaseState>(
			200000);
	private BaseState backInit;
	private BaseState forwardInit;

	BaseState middle = null;

	private long tstart;
	private double fLim;

	public PathResult search(BaseState start, BaseState goal) {

		start.setGoalState(goal);

		fLim = start.getFVal();

		forwardOpenList.add(start);
		fol.put(start.getKey(), start);

		forwardSearch();

		// TODO Implement the return of a PathResult.
		return null;
	}

	private boolean forwardSearch() {

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
					if (neighbours[i].getKey() == backInit.getKey()) {
						/*
						 * check if node is a goal
						 */
						middle = neighbours[i];
						System.out.println(neighbours[i] + "\n" + backInit);
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
