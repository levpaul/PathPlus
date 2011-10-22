package org.pathplus.algorithms.implementations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import org.pathplus.algorithms.BaseAlgorithm;
import org.pathplus.utils.path.PathResult;
import org.pathplus.utils.state.BaseState;

public class ADIStarAlgorithm extends BaseAlgorithm {

	private PriorityQueue<BaseState> openList;
	private Hashtable<Integer, BaseState> HopenList;

	private Hashtable<Integer, BaseState> closedList;
	private Hashtable<Integer, BaseState> incos;

	private PriorityQueue<BaseState> fol;
	private Hashtable<Integer, BaseState> fhol;
	private Hashtable<Integer, BaseState> fcl;
	private Hashtable<Integer, BaseState> bol;
	private ArrayList<BaseState> perimeter;

	private BaseState meeting;

	private BufferedWriter bw;
	private long timer, tlimit = 10000;
	private boolean timeLeft = true;
	private BaseState end;
	private int[] adlengths;
	private int[] adilengths;
	private double[] adtime;
	private double[] aditime;
	private int[] expanded;

	private ArrayList<BaseState> currentPath = new ArrayList<BaseState>();

	BaseState middle = null;
	BaseState start, goal;

	private String problem;

	private int a = 0;

	private boolean timeUp = false;
	private long tstart;
	private String fileName = "";
	private double ep = 1.0;
	
	double startGVal = 1337;
	double goalGVval = 1337;
	double startRhs = 1337;
	double goalRhs = 0;

	public ADIStarAlgorithm(double f, double p, String n) {
		ep = f;
		ep = p;
		fileName = n;
	}

	public PathResult search(BaseState start, BaseState goal) {

		double time = System.currentTimeMillis();

		int space = 0;
		int length = 0;
		double t_time = 0, t_space = 0, t_length = 0;
		timer = System.currentTimeMillis();

		admain();

		return null;
	}

	private void admain() {

		startGVal = 1337;
		goalGVval = 1337;
		startRhs = 1337;
		goalRhs = 0;

		openList = new PriorityQueue<BaseState>();
		HopenList = new Hashtable<Integer, BaseState>(200000);
		closedList = new Hashtable<Integer, BaseState>(200000);
		incos = new Hashtable<Integer, BaseState>(200000);

		openList.add(goal);
		HopenList.put(goal.getKey(), goal);
		improvePath();
		// savePath();
		printPath();

		timeLeft = true;

		adtime[a] = (System.currentTimeMillis() - timer) / 1000.0;

		runImprove();

		int count = 0;
		BaseState p = meeting;
		while (p.getParent() != null) {
			p = p.getParent();
			count++;
		}

		BaseState f = p.getParent();
		if (f != null)
			while (f.getParent() != null) {
				f = f.getParent();
				count++;
			}

		count++;

		aditime[a] = ((System.currentTimeMillis() - timer) / 1000.0);
		adlengths[a] = (int) start.getGVal();
		adilengths[a] = (int) (count + bol.get(meeting.getKey()).getGVal());

	}

	private void runImprove() {

		timer = System.currentTimeMillis();

		BaseState currentPos = start;
		BaseState head = start;
		int headCount = 0;

		while (head.getParent() != null && head.getParent() != goal
				&& headCount < 5) {
			head = head.getParent();
			headCount++;
		}
		fol = new PriorityQueue<BaseState>();
		fhol = new Hashtable<Integer, BaseState>();
		fcl = new Hashtable<Integer, BaseState>();
		meeting = null;

		bol = new Hashtable<Integer, BaseState>();
		perimeter = new ArrayList<BaseState>();

		if (head.getGVal() < 6)
			return;

		int lim = (int) head.getGVal();
		System.out.println("LIMIT IS: " + lim);

		fol.add(new BaseState(goal));
		fhol.put(goal.getKey(), new BaseState(goal));
		perimeter.add(new BaseState(head));

		headCount = 1;

		while (head.getParent() != null && !head.getParent().checkDup(goal)) {
			if (headCount % 4 == 0) {
				if (headCount > lim / 2) {
					fol.add(new BaseState(head));
					fhol.put(head.getKey(), new BaseState(head));
				} else {
					perimeter.add(new BaseState(head));
				}
			}
			head = head.getParent();
			headCount++;
		}// end while

		BaseState p = start;
		int pc = 0;

		while (p != null) {
			for (int i = 0; i < perimeter.size(); i++) {
				if (p.checkDup(perimeter.get(i))) {
					perimeter.get(i).g_val = pc;
				}
			}

			pc++;
			p = p.getParent();
		}

		BaseState.perimeter = perimeter;

		for (int i = 0; i < perimeter.size(); i++) {
			bol.put(perimeter.get(i).getKey(), perimeter.get(i));
		}

		System.out.println(fol.size() + " " + bol.size());
		perimeterSearch();

	}

	private void perimeterSearch() {
		// TODO Auto-generated method stub
		try {

			while (fol.peek() != null) {
				BaseState current = fol.remove();
				fcl.put(current.getKey(), current);

				// System.out.println(current.g_val + " " + current.h_val + " "
				// + current.f_val);

				// expand current
				BaseState[] neighbours = current.getNeighbours();
				expanded[a] += neighbours.length;

				for (int i = 0; i < neighbours.length; i++) {

					BaseState n = neighbours[i];

					if (!fcl.containsKey(n.getKey())) {

						boolean dup = false;

						if (fhol.containsKey(n.getKey()))
							if (fhol.get(n.getKey()).g_val > n.g_val) {
								fhol.remove(n.getKey());
								fhol.put(n.getKey(), n);
								fol.add(n);
								dup = true;
							}// end if node was expanded before

						if (!dup && bol.containsKey(n.getKey())) {
							// System.out.println("SOLUTION FOUND\n\n" +
							// n.toString() + "\n" + bol.get(n.getKey()) +
							// "\n====================");
							meeting = n;
							return;
							// maybe add
						}

						if (!dup) {
							fol.add(n);
							fhol.put(n.getKey(), n);
						}

					} else {
						if (fcl.get(n.getKey()).g_val > n.g_val) {
							fcl.remove(n.getKey());
							fhol.put(n.getKey(), n);
							fol.add(n);

						}// end if node was expanded before
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	private void savePath() {
		// TODO Auto-generated method stub
		currentPath.clear();

		int count = 0;
		BaseState current = start;

		while (current.getParent() != null) {
			currentPath.add(current);
			current = current.getParent();
			count++;
		}

		currentPath.add(current);
		System.out.println(current + "\n\nPath was of length: " + count);
	}

	private void printPath() {
		// TODO Auto-generated method stub
		int count = 0;
		BaseState current = start;
		// System.out.println("START\n" + start + "\nGOAL\n" + goal
		// +"\n==================");

		while (current.getParent() != null) {
			// System.out.println(current);
			current = current.getParent();
			count++;
		}

		System.out.println("Path was of length: " + count + "    ep: " + ep
				+ "  openlist: " + openList.size());
	}

	public void improvePath() {
		System.out.println(openList.peek().key()[0] + ", "
				+ openList.peek().key()[1] + " | " + start.key()[0] + ", "
				+ start.key()[1] + "    ep: " + ep);
		while ((openList.peek().compareTo(start) < 0 || start.g_val != start.rhs)) {

			if (System.currentTimeMillis() - timer > tlimit) {
				timeLeft = false;
				return;
			}

			BaseState s = openList.remove();
			HopenList.remove(s.getKey());

			if (s.checkDup(start))
				start = s;

			BaseState[] neighbours = s.getNeighbours();
			expanded[a] += neighbours.length;
			if (s.g_val > s.rhs) {
				// System.out.print("G: " + s.g_val + " RHS: " + s.rhs);
				s.g_val = s.rhs;
				// System.out.println("   G: " + s.g_val + " RHS: " + s.rhs);

				closedList.put(s.getKey(), s);

				for (int i = 0; i < neighbours.length; i++) {
					updateState(neighbours[i]);
				}// end for

			} else {
				s.g_val = 1337;
				updateState(s);
				for (int i = 0; i < neighbours.length; i++) {
					updateState(neighbours[i]);
				}// end for

			}

		}// end while

	}// end improvePath method

	public void updateState(BaseState s) {

		if (!(closedList.containsKey(s.getKey()) || incos.containsKey(s.getKey()) || HopenList
				.containsKey(s.getKey()))) {
			s.g_val = 1337;
		}

		ArrayList<BaseState> al = new ArrayList<BaseState>();
		if (!s.checkDup(goal)) {
			if (closedList.containsKey(s.getKey()))
				al.add(closedList.get(s.getKey()));
			if (HopenList.containsKey(s.getKey()))
				al.add(HopenList.get(s.getKey()));
			if (incos.containsKey(s.getKey()))
				al.add(incos.get(s.getKey()));

			int smallest = (int) (s.getParent().g_val + 1);

			if (al.size() == 0)
				s.rhs = (int) (s.getParent().g_val + 1);
			else {
				BaseState small = s.getParent();
				for (int i = 0; i < al.size(); i++) {
					if ((int) (al.get(i).getParent().g_val + 1) < smallest) {
						smallest = (int) (al.get(i).getParent().g_val + 1);
						small = al.get(i).getParent();
					}

				}
				s.parent = small;
				s.rhs = smallest;

			}
		}
		if (start.checkDup(s))
			start = s;

		if (HopenList.containsKey(s.getKey())) {
			HopenList.remove(s.getKey());
			openList.remove(s);
		}

		if (s.g_val != s.rhs) {
			if (!closedList.containsKey(s.getKey())) {
				openList.add(s);
				HopenList.put(s.getKey(), s);
			} else {
				incos.put(s.getKey(), s);
			}
		}// end outter if

	}

}
