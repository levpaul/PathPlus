package org.pathplus.algorithms.implementations;

import java.io.*;
import java.util.*;

import org.pathplus.utils.state.BaseState;

public class AStarAlgorithm {

	private PriorityQueue<BaseState> forwardOpenList = new PriorityQueue<BaseState>();

	private Hashtable<Integer, BaseState> fol = new Hashtable<Integer, BaseState>(
			200000);
	private Hashtable<Integer, BaseState> forwardClosedList = new Hashtable<Integer, BaseState>(
			200000);
	private BaseState backInit;
	private BaseState forwardInit;

	private BufferedWriter bw;

	private int evals, t_evals, a;
	private int[] lengths;
	private long timer;

	BaseState middle = null;

	private double fLim = 0;
	private int[] gCounts = new int[100];

	private int numVals = 0;
	private String problem;
	private int ttime, tspace;
	private long tstart;
	private boolean timeUp = false;

	public void search(BaseState start, BaseState goal) {

		double time = System.currentTimeMillis();
		t_evals = 0;
		int space = 0;
		int length = 0;
		double t_time = 0, t_space = 0, t_length = 0;

		int numProblems = 100;

		lengths = new int[numProblems];

		System.out.println("Start state: " + forwardInit + "\nGoal state: "
				+ backInit);

		fLim = forwardInit.getFVal();

		forwardOpenList.add(forwardInit);
		fol.put(forwardInit.getKey(), forwardInit);

		forwardSearch();

	}

	private boolean forwardSearch() {

		BaseState current = null;

		while (forwardOpenList.size() > 0) {

			if (System.currentTimeMillis() - tstart > 12600000) {
				timeUp = true;
				return false;

			}

			current = forwardOpenList.poll();

			gCounts[(int) current.calcHValue()]++;

			fol.remove(current.getKey());
			forwardClosedList.put(current.getKey(), current);

			BaseState[] neighbours = current.getNeighbours();

			// go through possible neighbours and add them if they are NOT in
			// the closed list or in the openlist
			for (int i = 0; i < neighbours.length; i++) {
				boolean isInClosed = false;

				if (forwardClosedList.containsKey(neighbours[i].getKey())) {// check
					// that
					// new
					// node
					// isn't
					// in
					// closed
					// list
					isInClosed = true;
				}

				if (isInClosed == false) {// check that new node isn't in
											// openlist
					if (fol.containsKey(neighbours[i].getKey()))
						isInClosed = true;
				}// end if

				if (isInClosed == false) {
					if (neighbours[i].getKey() == backInit.getKey()) {// check
																		// if
																		// node
																		// is a
																		// goal
						middle = neighbours[i];
						System.out.println(neighbours[i] + "\n" + backInit);
						lengths[a] = (int) neighbours[i].getGVal();
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
