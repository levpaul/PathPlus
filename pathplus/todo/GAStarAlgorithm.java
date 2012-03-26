package org.pathplus.algorithms.implementations;

import java.io.*;
import java.util.*;

import org.pathplus.algorithms.BaseAlgorithm;
import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.path.Result;
import org.pathplus.utils.state.BaseState;
import org.pathplus.utils.state.State;

public class GAStarAlgorithm  extends BaseAlgorithm {

	private PriorityQueue<BaseState> forwardOpenList = new PriorityQueue<BaseState>();
	private PriorityQueue<BaseState> backwardOpenList = new PriorityQueue<BaseState>();
	private BufferedWriter bw;
	private int ttime, tReal = 0;
	private long timer, tlimit = 25200000, tspace;
	private int gLim = 0;
	private double gfLim = 0, gbLim = 0;
	private BaseState[] hval = new BaseState[2];
	private int countage = 0;;
	private int dups = 0;

	private Hashtable<Integer, BaseState> forwardClosedList = new Hashtable<Integer, BaseState>(
			200000);

	private Hashtable<Integer, BaseState> fol = new Hashtable<Integer, BaseState>(
			200000);
	private Hashtable<Integer, BaseState> bol = new Hashtable<Integer, BaseState>(
			200000);

	// private ArrayList<EightPuzzle> forwardClosedList = new
	// ArrayList<EightPuzzle>();
	private Hashtable<Integer, BaseState> backwardClosedList = new Hashtable<Integer, BaseState>(
			200000);

	private long[] ftime = new long[10];
	private long[] btime = new long[10];
	private int[] lengths;

	private int evals, t_evals, l_min = 1000;
	private int soln = 0;

	BaseState middle = null;

	private String problem;

	private int a = 0;
	double f1;
	private double f2;
	private boolean timeUp = false;
	private long tstart;
	private int numVals = 0, tNODEAV;
	private BufferedWriter data;
	private int bGen = 0, fGen = 0;

	private double gFLim, gBLim, fLim;

	public PathResult search (BaseState start, BaseState goal) {
		tstart = System.currentTimeMillis();

		double time = System.currentTimeMillis();
		t_evals = 0;
		int space = 0;
		int length = 0;
		double t_time = 0, t_space = 0, t_length = 0;

		timer = System.currentTimeMillis();

		l_min = 1000;
		int[][] testState = new int[3][3];
		evals = 0;

		f1 = start.getFVal();
		f2 = goal.getFVal();

		hval[1] = start;
		hval[0] = goal;

		bGen = 0;
		fGen = 0;
		gLim = (int) f1;

		System.out.println("Start state: " + start + "\nGoal state: " + goal);

		fLim = start.getFVal();

		fol.clear();
		bol.clear();

		forwardOpenList.add(start);
		backwardOpenList.add(goal);
		fol.put(start.getKey(), start);
		bol.put(goal.getKey(), goal);

		boolean notFound = true;

		gfLim = (int) Math.ceil(fLim / 2);
		gbLim = (int) Math.floor(fLim / 2);

		ArrayList<Integer> fEx = new ArrayList<Integer>();

		countage = 0;

		gFLim = gfLim;
		gBLim = gbLim;

		ArrayList<BaseState> list = new ArrayList<BaseState>(
				backwardClosedList.values());
		backwardClosedList.clear();

		System.out.println(list.size());

		notFound = true;

		forwardSearch();
		notFound = backwardSearch();

		while (notFound == true) {
			fEx.add(countage);
			// countage = (forwardOpenList.size() + backwardOpenList.size() +
			// forwardClosedList.size() + backwardClosedList.size());

			fLim++;

			System.out.println("fol: " + forwardOpenList.size() + " bol: "
					+ backwardOpenList.size());

			if (forwardOpenList.size() < backwardOpenList.size()) {
				gFLim++;
				gfLim = gFLim;
				System.out.println("Flim: " + fLim + " Glim: " + gfLim
						+ "(forward -1)");
				notFound = forwardSearch();
				if (notFound) {
					System.out.println("Flim: " + fLim + " Glim: " + gbLim
							+ "(back -2)");
					notFound = backwardSearch();
				}
			} else {
				gBLim++;
				gbLim = gBLim;
				System.out.println("Flim: " + fLim + " Glim: " + gbLim
						+ "(back -1)");
				notFound = backwardSearch();
				if (notFound) {
					System.out.println("Flim: " + fLim + " Glim: " + gfLim
							+ "(forward -2)");
					notFound = forwardSearch();
				}
			}

		}

		// countage = (forwardOpenList.size() + backwardOpenList.size() +
		// forwardClosedList.size() + backwardClosedList.size());

		System.out.println("Found!");
		return null;

	}

	// ==================================================================================================
	// ==================================================================================================
	// THIS IS
	// ***F***O***R***W***A***R***D*** ***S***E***A***R***C***H***
	// ==================================================================================================
	// ==================================================================================================
	private boolean forwardSearch() {

		BaseState current = null;

		int count = 0;

		Object[] temp = forwardOpenList.toArray();
		forwardOpenList.clear();

		for (int i = 0; i < temp.length; i++) {
			forwardOpenList.add((BaseState) temp[i]);
			temp[i] = null;
		}

		temp = null;

		while (forwardOpenList.size() > 0) {

			if (System.currentTimeMillis() - tstart > tlimit) {
				timeUp = true;
				return true;
			}

			count++;

			long ti = System.currentTimeMillis();

			current = null;
			if (forwardOpenList.peek().getGVal() < gfLim
					&& forwardOpenList.peek().getFVal() <= fLim) {
				current = forwardOpenList.poll();
				fol.remove(current.getKey());
			}

			if (current == null) {// double check
				System.out.println(count
						+ " nodes processed - current was null");
				return true;
			}

			if (current.getGVal() >= gfLim || current.getFVal() > fLim)
				System.out.println(5 / 0);

			forwardClosedList.put(current.getKey(), current);

			ftime[0] += System.currentTimeMillis() - ti;
			ti = System.currentTimeMillis();

			BaseState[] neighbours = current.getNeighbours();

			// ===============================================================================================
			// NEIGHBOUR EXPAND
			// ===============================================================================================
			// go through possible neighbours and add them if they are NOT in
			// the closed list or in the openlist
			fGen += neighbours.length;
			for (int i = 0; i < neighbours.length; i++) {
				if (neighbours[i].getGVal() < gLim) {
					boolean isInClosed = false;
					ti = System.currentTimeMillis();

					countage++;

					if (forwardClosedList.containsKey(neighbours[i].getKey())) {

						if (forwardClosedList.get(neighbours[i].getKey())
								.getGVal() > neighbours[i].getGVal()) {
							System.out.println(5 / 0);
							forwardClosedList.remove(neighbours[i].getKey());
							forwardOpenList.add(neighbours[i]);
							fol.put(neighbours[i].getKey(), neighbours[i]);
							dups++;
						}

						isInClosed = true;
					}

					ftime[1] += System.currentTimeMillis() - ti;
					ti = System.currentTimeMillis();

					if (backwardClosedList.containsKey(neighbours[i].getKey()))// check
																				// if
																				// in
																				// bcl
						System.out.println(5 / 0);

					if (fol.containsKey(neighbours[i].getKey())) {

						if (fol.get(neighbours[i].getKey()).getGVal() > neighbours[i]
								.getGVal()) {
							System.out.println(5 / 0);
							fol.remove(neighbours[i].getKey());
							forwardOpenList.remove(neighbours[i]);
							forwardOpenList.add(neighbours[i]);
							fol.put(neighbours[i].getKey(), neighbours[i]);
						}

						isInClosed = true;

					}

					if (isInClosed == false) {// check if goal else add to fol

						if (bol.containsKey(neighbours[i].getKey())) {
							middle = neighbours[i];
							forwardOpenList.add(neighbours[i]);
							fol.put(neighbours[i].getKey(), neighbours[i]);
							System.out
									.println(count
											+ " nodes processed - ended with goal being found");
							lengths[a] = (int) Math.max(fLim, middle.getFVal());
							// l_min = (int) (neighbours[i].getGVal() +
							// backwardOpenList.get(j).getGVal());
							return false;
						}

						forwardOpenList.add(neighbours[i]);
						fol.put(neighbours[i].getKey(), neighbours[i]);

						ftime[4] += System.currentTimeMillis() - ti;
					}
				}// end outter for
			}
		}
		System.out.println(count + " nodes processed");
		return true;

	}

	private boolean backwardSearch() {

		BaseState current = null;

		Object[] temp = backwardOpenList.toArray();
		backwardOpenList.clear();

		for (int i = 0; i < temp.length; i++) {
			backwardOpenList.add((BaseState) temp[i]);
			temp[i] = null;
		}

		if (backwardOpenList.size() != bol.size())
			System.out.println(5 / 0);
		temp = null;

		System.out.println("node at front (back): "
				+ backwardOpenList.peek().getGVal() + " "
				+ backwardOpenList.peek().getFVal());

		// int gLim = (int) Math.floor((fLim)/2);

		int count = 0;
		while (backwardOpenList.size() > 0) {

			if (System.currentTimeMillis() - tstart > tlimit) {
				timeUp = true;
				return true;
			}

			count++;

			long ti = System.currentTimeMillis();

			current = null;
			if (backwardOpenList.peek().getGVal() < gbLim
					&& backwardOpenList.peek().getFVal() <= fLim) {
				current = backwardOpenList.poll();
				bol.remove(current.getKey());
			}

			btime[0] += System.currentTimeMillis() - ti;
			ti = System.currentTimeMillis();

			if (current == null) {
				System.out.println(count
						+ " nodes processed - current was null");
				return true;
			}

			if (current.getGVal() >= gbLim || current.getFVal() > fLim)
				System.out.println(5 / 0);

			btime[1] += System.currentTimeMillis() - ti;
			ti = System.currentTimeMillis();

			backwardClosedList.put(current.getKey(), current);

			// try{
			// data = new BufferedWriter(new FileWriter("gastar.txt", true));
			// data.write(current.writeData(1, fLim));
			// data.close();
			// System.out.println(++countex);
			// }catch (Exception e){
			// System.out.println("error: " + e);
			// }

			BaseState[] neighbours = current.getNeighbours();
			bGen += neighbours.length;
			// go through possible neighbours and add them if they are NOT in
			// the closed list
			for (int i = 0; i < neighbours.length; i++) {
				if (neighbours[i].getGVal() < gLim) {
					boolean isInClosed = false;
					ti = System.currentTimeMillis();

					countage++;

					if (backwardClosedList.containsKey(neighbours[i].getKey())) {

						if (backwardClosedList.get(neighbours[i].getKey())
								.getGVal() > neighbours[i].getGVal()) {
							System.out.println(5 / 0);
							backwardClosedList.remove(neighbours[i].getKey());
							backwardOpenList.add(neighbours[i]);
							bol.put(neighbours[i].getKey(), neighbours[i]);
						}
						isInClosed = true;

					}// end if

					// ==============
					// not likely to be used with ga*
					// ==============
					if (bol.containsKey(neighbours[i].getKey())) {

						if (((BaseState) bol.get(neighbours[i].getKey()))
								.getGVal() > neighbours[i].getGVal()) {
							System.out.println(5 / 0);
							bol.remove(neighbours[i].getKey());
							backwardOpenList.remove(neighbours[i]);
							backwardOpenList.add(neighbours[i]);
							bol.put(neighbours[i].getKey(), neighbours[i]);
						}

						isInClosed = true;

					}

					btime[2] += System.currentTimeMillis() - ti;
					ti = System.currentTimeMillis();

					if (isInClosed == false) {// we know the neighbour node
												// isn't in
												// the open or closed list in
												// this
												// direction

						if (fol.containsKey(neighbours[i].getKey())) {
							middle = neighbours[i];
							backwardOpenList.add(neighbours[i]);
							bol.put(neighbours[i].getKey(), neighbours[i]);
							lengths[a] = (int) Math.max(fLim, middle.getFVal());
							System.out.println(count
									+ " nodes processed - goal node was found");
							// l_min = (int) (neighbours[i].getGVal() +
							// forwardOpenList.get(j).getGVal());
							return false;
						}

						// if not in a closed list and not in opposite openlist
						backwardOpenList.add(neighbours[i]);
						bol.put(neighbours[i].getKey(), neighbours[i]);

						btime[4] += System.currentTimeMillis() - ti;
						ti = System.currentTimeMillis();

					}

				}// end outter for
			}
		}

		// System.out.println("OpenList Size: " + backwardOpenList.size() +
		// "\tClosedList Size: " + backwardClosedList.size());

		return true;
	}

	






}
