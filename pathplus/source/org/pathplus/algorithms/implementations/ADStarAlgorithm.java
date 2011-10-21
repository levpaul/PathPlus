package org.pathplus.algorithms.implementations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import org.pathplus.algorithms.BaseAlgorithm;
import org.pathplus.utils.state.BaseState;

public class ADStarAlgorithm extends BaseAlgorithm{

	private PriorityQueue<BaseState> openList;
	private Hashtable<String, BaseState> HopenList;

	private Hashtable<String, BaseState> closedList;
	private Hashtable<String, BaseState> incos;

	private BufferedWriter bw;
	private long timer, tlimit = 1000;
	private boolean timeLeft = true;
	private BaseState end;
	private int[] lengths;

	private int expanded;

	private ArrayList<BaseState> currentPath = new ArrayList<BaseState>();

	BaseState middle = null;
	BaseState start, goal;

	private String problem;

	private int a = 0;

	private boolean timeUp = false;
	private long tstart;

	public ADStarAlgorithm(int time) {
		tlimit = time * 1000;
	}

	public void start() {
		try {
			tstart = System.currentTimeMillis();
			BufferedReader br = new BufferedReader(new FileReader("sorted.txt"));
			bw = new BufferedWriter(new FileWriter("AD RESULTS "
					+ (tlimit / 1000) + ".txt"));

			double time = System.currentTimeMillis();

			bw.write("Time Limit: " + tlimit / 1000 + " seconds per problem.\n");
			int space = 0;
			int length = 0;
			double t_time = 0, t_space = 0, t_length = 0;

			int numProblems = 30;

			// data = new BufferedWriter(new FileWriter("gastar.txt"));
			// data.write("");
			// data.close();

			lengths = new int[numProblems];

			for (a = 0; a < numProblems; a++) {

				timer = System.currentTimeMillis();

				int[][] testState = new int[4][4];

				System.out.println("Problem #" + (a + 1));

				StringTokenizer st = new StringTokenizer(br.readLine());

				if (st.countTokens() == 0)
					st = new StringTokenizer(br.readLine());

				problem = st.nextToken();
				System.out.println("Problem: " + problem);

				testState[0][3] = Integer.parseInt(st.nextToken());
				testState[1][3] = Integer.parseInt(st.nextToken());
				testState[2][3] = Integer.parseInt(st.nextToken());
				testState[3][3] = Integer.parseInt(st.nextToken());

				// st = new StringTokenizer(br.readLine());
				testState[0][2] = Integer.parseInt(st.nextToken());
				testState[1][2] = Integer.parseInt(st.nextToken());
				testState[2][2] = Integer.parseInt(st.nextToken());
				testState[3][2] = Integer.parseInt(st.nextToken());

				// st = new StringTokenizer(br.readLine());
				testState[0][1] = Integer.parseInt(st.nextToken());
				testState[1][1] = Integer.parseInt(st.nextToken());
				testState[2][1] = Integer.parseInt(st.nextToken());
				testState[3][1] = Integer.parseInt(st.nextToken());

				// st = new StringTokenizer(br.readLine());
				testState[0][0] = Integer.parseInt(st.nextToken());
				testState[1][0] = Integer.parseInt(st.nextToken());
				testState[2][0] = Integer.parseInt(st.nextToken());
				testState[3][0] = Integer.parseInt(st.nextToken());

				int[][] goalState = { { 12, 8, 4, 0 }, { 13, 9, 5, 1 },
						{ 14, 10, 6, 2 }, { 15, 11, 7, 3 } };

				if (a >= 0) {

					timer = System.currentTimeMillis();

					start = new BaseState(testState, goalState, 1);
					goal = new BaseState(goalState, testState, 2);

					BaseState.makeStart(testState);

					admain();

					BaseState c = start;
					int ccount = 0;

					while (c.getParent() != null) {
						System.out.println(c);
						c = c.getParent();
						ccount++;
					}
					System.out.println(c);

					System.out.println("total length: " + ccount);

					bw.write("\nProblem: " + (a + 1) + "\tlength:\t" + ccount
							+ "\tgenerated\t" + expanded);

					expanded = 0;

				}
			}
			bw.close();
		} catch (Exception e) {
			System.out.println("Error: ");
			for (StackTraceElement i : e.getStackTrace()) {
				System.out.println(i.toString());
			}
		}// end catch

	}

	private void admain() {

		start.g_val = 1337;
		goal.g_val = 1337;
		start.rhs = 1337;
		goal.rhs = 0;
		BaseState.ep = 2.5;

		openList = new PriorityQueue<BaseState>();
		HopenList = new Hashtable<String, BaseState>(200000);
		closedList = new Hashtable<String, BaseState>(200000);
		incos = new Hashtable<String, BaseState>(200000);

		openList.add(goal);
		HopenList.put(goal.key, goal);
		improvePath();
		// savePath();
		printPath();

		timeLeft = true;
		timer = System.currentTimeMillis();

		while (timeLeft) {

			if (BaseState.ep > 1) {
				BaseState.ep = BaseState.ep - 0.25;
			}

			if (BaseState.ep <= 1)
				break;

			openList.addAll(incos.values());
			HopenList.putAll(incos);
			incos.clear();

			Object[] temp = openList.toArray();
			openList.clear();

			for (int i = 0; i < temp.length; i++) {
				openList.add((BaseState) temp[i]);
			}

			closedList.clear();

			improvePath();
			printPath();
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

		System.out.println("Path was of length: " + count + "    ep: "
				+ BaseState.ep + "  openlist: " + openList.size());
	}

	public void improvePath() {
		System.out.println(openList.peek().key()[0] + ", "
				+ openList.peek().key()[1] + " | " + start.key()[0] + ", "
				+ start.key()[1] + "    ep: " + BaseState.ep);
		while ((openList.peek().compareTo(start) < 0 || start.g_val != start.rhs)) {

			if (System.currentTimeMillis() - timer > tlimit) {
				timeLeft = false;
				return;
			}

			BaseState s = openList.remove();
			HopenList.remove(s.key);

			if (s.checkDup(start))
				start = s;

			BaseState[] neighbours = s.getNeighbors();
			expanded += neighbours.length;

			if (s.g_val > s.rhs) {
				// System.out.print("G: " + s.g_val + " RHS: " + s.rhs);
				s.g_val = s.rhs;
				// System.out.println("   G: " + s.g_val + " RHS: " + s.rhs);

				closedList.put(s.key, s);

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

		if (!(closedList.containsKey(s.key) || incos.containsKey(s.key) || HopenList
				.containsKey(s.key))) {
			s.g_val = 1337;
		}

		ArrayList<BaseState> al = new ArrayList<BaseState>();
		if (!s.checkDup(goal)) {
			if (closedList.containsKey(s.key))
				al.add(closedList.get(s.key));
			if (HopenList.containsKey(s.key))
				al.add(HopenList.get(s.key));
			if (incos.containsKey(s.key))
				al.add(incos.get(s.key));

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

		if (HopenList.containsKey(s.key)) {
			HopenList.remove(s.key);
			openList.remove(s);
		}

		if (s.g_val != s.rhs) {
			if (!closedList.containsKey(s.key)) {
				openList.add(s);
				HopenList.put(s.key, s);
			} else {
				incos.put(s.key, s);
			}
		}// end outter if

	}

}
