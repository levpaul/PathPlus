package org.pathplus.algorithms.implementations;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.PriorityQueue;

import org.pathplus.algorithms.BaseAlgorithm;
import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.state.BaseState;

public class BiMaxAlgorithm extends BaseAlgorithm {

	private ArrayList<PriorityQueue<BaseState>> ol = new ArrayList<PriorityQueue<BaseState>>();
	private Hashtable<Integer, BaseState>[] cl = new Hashtable[2];
	private Hashtable<Integer, BaseState>[] hol = new Hashtable[2];
	private int count = 0;

	// private ArrayList<EightPuzzle> forwardClosedList = new
	// ArrayList<EightPuzzle>();

	private double[] fmin = new double[2];
	private BaseState[] hval = new BaseState[2];

	private int l_min = 99999999;

	private int l;

	BaseState middle = null;

	private int d = 0, od = 0, a;
	private double[] flim = new double[2];
	private int[] lengths;

	private ArrayList<ArrayList<Integer>> fEx = new ArrayList<ArrayList<Integer>>(
			2);

	public PathResult start(BaseState start, BaseState goal) {

		ol.add(new PriorityQueue<BaseState>());
		ol.add(new PriorityQueue<BaseState>());

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
		hval[0] = start;
		hval[1] = goal;

		while (!ol.get(0).isEmpty() && !ol.get(1).isEmpty()) {// step 4
			// if(++count % 1000 == 0)
			// System.out.println("fol: " + ol.get(0).size() + ", bol: " +
			// ol.get(1).size() + "\tfcl: " + cl[0].size() + ", bcl: " +
			// cl[1].size() + "  \t\ttotal: " + (ol.get(0).size() +
			// ol.get(1).size() + cl[0].size() + cl[1].size()) + "\tlim: " +
			// l_min);

			// we are suppose to set the direction based on cardinality
			// principle AFTER an f-limit has been drained
			if (ol.get(0).size() <= ol.get(1).size())
				d = 0;
			else
				d = 1;// set the direction of search, step 5

			od = 1 - d;// set the opposite direction, STEP 6

			fmin[od] = 999999; // step 1, 2, 3 complete

			while (true) {// search until no nodes remain within flim
				BaseState current = null;

				System.out.println("flim: " + flim[d] + " dir: " + d);

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
					System.out.println("got something: length of "
							+ (current.getGVal() + ((BaseState) (cl[od]
									.get(current.getKey()))).getGVal())
							+ " when lmin is: " + l_min);
					// nip current in od and prune od

					// Nipping is done by NOT expanding current, which the if
					// statement takes care of

					// System.out.println("lmin = " + l_min + ", path = " +
					// (((EightPuzzle) (cl[od].get(current.getKey()))).getGVal()
					// + current.getGVal()));

					// pruning part

					Object[] t = ol.get(od).toArray();
					BaseState[] temp = new BaseState[t.length];

					for (int i = 0; i < t.length; i++) {
						temp[i] = (BaseState) t[i];
					}

					t = null;

					/*
					 * go through openlist and find decendents of matcher - what
					 * if the decendents are more than one node further
					 * down?????
					 */
					for (int i = 0; i < temp.length; i++) {
						if (temp[i].getParent().getKey() == (current.getKey())) {
							cl[od].put(temp[i].getKey(), temp[i]);
							ol.get(od).remove(temp[i]);
							hol[od].remove(current.getKey());
							// i--;
						}

					}
					temp = null;

					// take out of opposite search tree omega M
					// omega m is the set of nodes which are parents (?)

				} else {
					// data = new BufferedWriter(new FileWriter("bimax.txt",
					// true));
					// data.write(current.writeData(d, flim[d]));
					// data.close();
					// System.out.println(++countex);
					// if(countex == 73618)
					// System.out.println(current);
					expand(current);// step 13
				}
			}
		}// end while

		if (l_min > 1000)
			System.out.println("NO PATH EXIST");
		else {
			System.out.println("The path was found with a length of " + l_min);
			lengths[a] = l_min;
			l += l_min;
		}

		System.out.println("Found!");

		BaseState current = middle;

		return null;
	}

	public void expand(BaseState m) {

		boolean trim = false; // step 1

		BaseState[] neighbours = m.getNeighbours();

		for (int z = 0; z < neighbours.length; z++) {
			BaseState n = neighbours[z];// step 2
			// step 3 taken care of inside EightPuzzle

			// System.out.println("g1: " + n.getGVal() + "   h2: " +
			// hval[d].calcH(n) );
			// if(n.calcH() - hval[od].calcH(n) != 0)
			// System.out.println(5/0);
			//
			// double fd = Math.max(n.getFVal(), fmin[od] + n.getGVal() -
			// hval[od].calcH(n));//step 4

			double fd = n.getFVal();
			count++;

			BaseState temp;

			if (fd < l_min) {
				// step 5 taken care of inside eightpuzzle (calc f val)

				if (!cl[d].containsKey(n.getKey())) {// if n is not in the
														// closed list on its
														// own side

					boolean inOpen = false;
					BaseState otherNode = null;

					otherNode = (BaseState) hol[d].get(n.getKey());

					inOpen = hol[d].containsKey(n.getKey());

					//
					// ===================STEP 6=====================
					// done

					if (!inOpen) {// if n is not in the open list on its own
									// side (and closed list)

						// step 7 done in creation of n in eightpuzzle

						ol.get(d).add(n);// step 8
						hol[d].put(n.getKey(), n);

					} else if (n.getGVal() < otherNode.getGVal()) {// step 9 -
																	// new path
																	// to
																	// duplicate
																	// OL node
																	// found
																	// which is
																	// shorter

						hol[d].remove(otherNode);

						ol.get(d).remove(otherNode);

						ol.get(d).add(n); // step 10, replace node

					}// end elseif

				} else {// when node n was already in the closed list
					temp = (BaseState) cl[d].get(n.getKey());

					if (temp.getGVal() > n.getGVal()) {// step 11
						cl[d].remove(temp.getKey());// step 12
						// System.out.println("shit" + cl[d].get(n.getKey()));
						ol.get(d).add(n);// step 13
						hol[d].put(n.getKey(), n);

					}

				}// now we know that the new node is not in its own tree

				BaseState other = null;

				if (cl[od].containsKey(n.getKey())) {
					other = (BaseState) cl[od].get(n.getKey());
				} else {

					if (hol[od].containsKey(n.getKey())) {
						other = (BaseState) hol[od].get(n.getKey());
					}

				}// end else

				if (other != null) {// if n is in opposite tree

					if (other.getGVal() + n.getGVal() < l_min) {// step 14
						System.out.println(l_min + "\n" + n + "\n" + other);
						l_min = (int) (other.getGVal() + n.getGVal());// step 15
						middle = n;
						trim = true;// step 17
					}
				}// end if

			}

		}// end for loop

		if (trim) {// step 18

			for (int j = 0; j < 2; j++) {
				Object[] t = ol.get(j).toArray();
				BaseState[] temp = new BaseState[t.length];

				for (int i = 0; i < t.length; i++) {
					temp[i] = (BaseState) t[i];
				}

				t = null;

				for (int i = 0; i < temp.length; i++) {
					// if(Math.max(ol.get(j).get(i).getFVal(), fmin[1-j] +
					// ol.get(j).get(i).getGVal() -
					// hval[1-j].calcH(ol.get(j).get(i))) >= l_min){

					if (temp[i].getFVal() >= l_min) {
						ol.get(j).remove(i);
						hol[j].remove(temp[i].getKey());
						cl[j].put(temp[i].getKey(), temp[i]);
					}// end if (trimming condition)

				}// end inner for

				temp = null;

			}// end trim - step 19 complete, end of expand

		}

	}// end expand

}
