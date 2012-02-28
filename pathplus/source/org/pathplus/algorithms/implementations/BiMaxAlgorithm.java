package org.pathplus.algorithms.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.PriorityQueue;

import org.pathplus.algorithms.Algorithm;
import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.state.State;

public class BiMaxAlgorithm<T extends State<T>> implements Algorithm<T> {

	private ArrayList<PriorityQueue<T>> ol = new ArrayList<PriorityQueue<T>>();
	private Hashtable<Integer, T>[] cl = new Hashtable[2];
	private Hashtable<Integer, T>[] hol = new Hashtable[2];
	private int count = 0;

	private double[] fmin = new double[2];

	private int l_min = 99999999, l;

	private int d = 0, od = 0;
	private double[] flim = new double[2];
	private int[] lengths;

	private T startGoal, goalGoal, otherMiddle, middle;

	private ArrayList<ArrayList<Integer>> fEx = new ArrayList<ArrayList<Integer>>(
			2);

	public PathResult<T> search(T start, T goal) {

		cl[0] = new Hashtable<Integer, T>();
		cl[1] = new Hashtable<Integer, T>();

		hol[0] = new Hashtable<Integer, T>();
		hol[1] = new Hashtable<Integer, T>();

		ol.add(new PriorityQueue<T>());
		ol.add(new PriorityQueue<T>());

		start.setGoalState(goal);
		goal.setGoalState(start);

		fEx.add(new ArrayList<Integer>());
		fEx.add(new ArrayList<Integer>());

		flim[0] = start.getFVal();
		flim[1] = goal.getFVal();

		hol[0].put(start.getKey(), start);
		hol[1].put(goal.getKey(), goal);

		ol.get(0).add(start);
		ol.get(1).add(goal);

		// used to store opposite goal states for DIFF method
		startGoal = start;
		goalGoal = goal;

		while (!ol.get(0).isEmpty() && !ol.get(1).isEmpty()) {// step 4

			// we are suppose to set the direction based on cardinality
			// principle AFTER an f-limit has been drained
			if (ol.get(0).size() <= ol.get(1).size())
				d = 0;
			else
				d = 1;// set the direction of search, step 5

			od = 1 - d;// set the opposite direction, STEP 6

			fmin[od] = 999999; // step 1, 2, 3 complete

			while (true) {// search until no nodes remain within flim
				T current = null;

				if (ol.get(d).size() > 0
						&& ol.get(d).peek().getFVal() <= flim[d]) {
					current = ol.get(d).remove();// step 8
					hol[d].remove(current.getKey());

				}
				// end for loop, now we have current as the smallest node within
				// flim, or null if no node satisfies this condition

				if (current == null) {// step 7
					fEx.get(d).add(count);
					flim[d] += 1;// maybe should increment by 2, but I doubt it
									// matters, as it breaks and if it re-enters
									// it will just break again
					break;
				}

				cl[d].put(current.getKey(), current);// step 9

				if (cl[od].containsKey(current.getKey())) {// step 10
					// nip current in od and prune od

					// Nipping is done by NOT expanding current, which the if
					// statement takes care of

					// pruning part

					Iterator<T> temp = ol.get(od).iterator();

					temp.next();
					/*
					 * go through openlist and find decendents of matcher - what
					 * if the decendents are more than one node further
					 * down?????
					 */
					while (temp.hasNext()) {
						T curr = temp.next();
						if (curr.getParent().getKey() == (current.getKey())) {
							cl[od].put(curr.getKey(), curr);
							ol.get(od).remove(curr);
							hol[od].remove(current.getKey());
						}

					}
					temp = null;

					// take out of opposite search tree omega M
					// omega m is the set of nodes which are parents (?)

				} else {
					expand(current);// step 13
				}
			}
		}// end while

		if (l_min > 1000) {
			return null;
		} else {

			otherMiddle = otherMiddle.getParent();
			T middleTemp = middle;
			while (otherMiddle != null) {

				T temp = otherMiddle.getParent();
				otherMiddle.setParent(middleTemp);
				middleTemp = otherMiddle;
				otherMiddle = temp;

			}
			return new PathResult<T>(middleTemp);
		}

	}

	public void expand(T m) {

		boolean trim = false; // step 1

		T[] neighbours = m.getNeighbours();

		for (int z = 0; z < neighbours.length; z++) {
			T n = neighbours[z];// step 2
			// step 3 taken care of inside EightPuzzle

			// step 4
			double fd = n.getFVal();
			count++;

			T temp;

			if (fd < l_min) {
				// step 5 taken care of inside eightpuzzle (calc f val)

				if (!cl[d].containsKey(n.getKey())) {
					// if n is not in the
					// closed list on its
					// own side

					boolean inOpen = false;
					T otherNode = null;

					otherNode = (T) hol[d].get(n.getKey());

					inOpen = hol[d].containsKey(n.getKey());

					//
					// ===================STEP 6=====================
					// done

					if (!inOpen) {
						/*
						 * if n is not in the open list on its own side (and
						 * closed list)
						 */

						// step 7 done in creation of n in eightpuzzle

						ol.get(d).add(n);// step 8
						hol[d].put(n.getKey(), n);

					} else if (n.getGVal() < otherNode.getGVal()) {
						/*
						 * step 9 - new path to duplicate OL node found which is
						 * shorter
						 */

						hol[d].remove(otherNode);

						ol.get(d).remove(otherNode);

						ol.get(d).add(n); // step 10, replace node

					}// end elseif

				} else {// when node n was already in the closed list
					temp = (T) cl[d].get(n.getKey());

					if (temp.getGVal() > n.getGVal()) {// step 11
						cl[d].remove(temp.getKey());// step 12
						ol.get(d).add(n);// step 13
						hol[d].put(n.getKey(), n);

					}

				}// now we know that the new node is not in its own tree

				T other = null;

				if (cl[od].containsKey(n.getKey())) {
					other = (T) cl[od].get(n.getKey());
				} else {

					if (hol[od].containsKey(n.getKey())) {
						other = (T) hol[od].get(n.getKey());
					}

				}// end else

				if (other != null) {// if n is in opposite tree

					if (other.getGVal() + n.getGVal() < l_min) {// step 14
						l_min = (int) (other.getGVal() + n.getGVal());// step 15
						middle = n;
						otherMiddle = other;
						trim = true;// step 17
					}
				}// end if

			}

		}// end for loop

		if (trim) {// step 18

			for (int j = 0; j < 2; j++) {
				// Iterator<T> temp = ol.get(j).iterator();

				Object[] temp = ol.get(j).toArray();

				// while(temp.hasNext()){
				for (int i = 0; i < temp.length; i++) {
					@SuppressWarnings("unchecked")
					T curr = (T) temp[i];
					if (curr.getFVal() >= l_min) {
						ol.get(j).remove(curr); // TODO Optimise this line.
						hol[j].remove(curr.getKey());
						cl[j].put(curr.getKey(), curr);
					}// end if (trimming condition)

				}// end inner for

				temp = null;

			}// end trim - step 19 complete, end of expand

		}

	}// end expand

	public Collection<T> getNodes() {
		System.out.println("ol0: " + hol[0].size() + "\tol1: " + hol[1].size()
				+ "\tcl0: " + cl[0].size() + "\tcl1: " + cl[1].size());

		
		cl[0].putAll(cl[1]);
		
		Collection<T> nodes = cl[0].values();

		return nodes;

	}
}
