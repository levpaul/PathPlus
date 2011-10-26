package org.pathplus.algorithms.implementations;

import java.util.Hashtable;
import java.util.PriorityQueue;

import org.pathplus.algorithms.Algorithm;
import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.state.State;

public class AStarAlgorithm<T extends State<T>> implements Algorithm<T> {

	private PriorityQueue<T> forwardOpenList = new PriorityQueue<T>();

	private Hashtable<Integer, T> fol = new Hashtable<Integer, T>();
	private Hashtable<Integer, T> forwardClosedList = new Hashtable<Integer, T>();

	T endState = null;

	public PathResult<T> search(T start, T goal) {

		start.setGoalState(goal);

		forwardOpenList.add(start);
		fol.put(start.getKey(), start);

		if (!forwardSearch(goal)) {
			return new PathResult<T>(endState);
		} else
			return null;
	}

	private boolean forwardSearch(T goalState) {

		T current = null;

		while (forwardOpenList.size() > 0) {

			current = forwardOpenList.poll();

			fol.remove(current.getKey());
			forwardClosedList.put(current.getKey(), current);

			T[] neighbours = (T[]) current.getNeighbours();

			// go through possible neighbours and add them if they are NOT in
			// the closed list or in the openlist
			for (int i = 0; i < neighbours.length; i++) {
				boolean isInClosed = false;

				
				// Check that new node isn't in the closed list.
				if (forwardClosedList.containsKey(neighbours[i].getKey())) {// check
					isInClosed = true;
				}

				if (isInClosed == false) {
					// check that new node isn't in openlist, this is why we
					// augment the openlist priority queue with a hashtable, to
					// make this check much quicker.
					if (fol.containsKey(neighbours[i].getKey()))
						isInClosed = true;
				}// end if

				if (isInClosed == false) {
					// Check if the new neighbour is a goal.
					if (neighbours[i].getKey() == goalState.getKey()) {
						endState = neighbours[i];
						return false;
					}
				}
				// now we know the node is not in open or closed and is not
				// goal, so we can add to the openList

				if (isInClosed == false) {
					fol.put(neighbours[i].getKey(), neighbours[i]);
					forwardOpenList.add(neighbours[i]);
				}// end if
			}// end outer for
		}// END WHILE LOOP

		return true;
	}

}
