package org.pathplus.utils.state.implementations;

import java.awt.Point;

import org.pathplus.utils.state.State;

public class EightPuzzleState implements State<EightPuzzleState> {

	public int[][] state;
	public double g_val = 0;
	public double h_val = 0;
	public double f_val = 0;

	EightPuzzleState goalState;

	private int key = 0;

	public EightPuzzleState parent = null;

	private Point[] goalStateTable = new Point[16];

	public EightPuzzleState(int[][] state, int[][] goal) {
		this.state = state;// constructor
		transformTable(goal);// creates lookup table for heuristic estimates
		g_val = 0;
		h_val = calcHValue();
		f_val = g_val + h_val;
		generateKey();
	}

	public EightPuzzleState(int[][] state, EightPuzzleState parent) {
		this.state = state;// constructor
		this.parent = parent;
		this.goalStateTable = parent.goalStateTable;
		g_val = parent.getGVal() + 1;
		h_val = calcHValue();
		f_val = g_val + h_val;
		generateKey();
	}

	private void transformTable(int[][] x) {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				goalStateTable[x[i][j]] = new Point(i, j);
	}

	public void generateKey() {

		String tempKey = "";

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (i == 3 && j == 3)
					tempKey += state[i][j];
				else
					tempKey += state[i][j] + " ";

		key = tempKey.hashCode();
	}

	// ==================================================================
	// ACCESSORS AND MUTATORS
	// ==================================================================

	public int[][] getState() {
		return state;
	}

	public double getFVal() {
		return h_val + g_val;
	}

	public double getGVal() {
		return g_val;
	}

	public double getHVal() {
		return h_val;
	}

	public EightPuzzleState getParent() {
		return parent;
	}

	public void setParent(EightPuzzleState parent) {
		this.parent = parent;
	}

	public void setGoalState(EightPuzzleState goalState) {
		transformTable(goalState.getState());
		this.goalState = goalState;
	}

	public EightPuzzleState getGoalState() {
		return goalState;
	}

	public int getKey() {
		return key;
	}

	// =====================================================================
	// CALC H METHOD TO ESTIMATE DISTANCE FROM CURRENT STATE TO GOAL STATE
	// =====================================================================
	public double calcHValue() {

		int sum = 0;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (state[i][j] != 0) {
					sum += Math.abs(i - goalStateTable[state[i][j]].x);
					sum += Math.abs(j - goalStateTable[state[i][j]].y);
				}
			}
		}
		return sum;
	}

	public boolean equals(EightPuzzleState o) {

		EightPuzzleState x = o;

		boolean same = true;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.state[i][j] != x.getState()[i][j])
					return false;
			}
		}
		return same;
	}

	public EightPuzzleState[] getNeighbours() {

		Point pos = new Point();

		// nested for loop to find the empty slot
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (state[i][j] == 0) {
					pos.x = i;
					pos.y = j;
				}
			}// end y
		}// end i

		int option = 4 * pos.x + pos.y;

		int g = (int) this.g_val;
		switch (option) {
		case 0: {// 0,0
			return new EightPuzzleState[] { swapUp(pos, g), swapRight(pos, g) };
		}
		case 1: {// 0,1
			return new EightPuzzleState[] { swapUp(pos, g),
					swapRight(pos, g), swapDown(pos, g) };
		}
		case 2: {// 0,2
			return new EightPuzzleState[] { swapUp(pos, g),
					swapRight(pos, g), swapDown(pos, g) };
		}
		case 3: {// 0,3
			return new EightPuzzleState[] { swapDown(pos, g),
					swapRight(pos, g) };
		}
		case 4: {// 1,0
			return new EightPuzzleState[] { swapUp(pos, g),
					swapRight(pos, g), swapLeft(pos, g) };
		}
		case 5: {// 1,1
			return new EightPuzzleState[] { swapUp(pos, g),
					swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g) };
		}
		case 6: {// 1,2
			return new EightPuzzleState[] { swapUp(pos, g),
					swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g) };
		}
		case 7: {// 1,3
			return new EightPuzzleState[] { swapDown(pos, g),
					swapRight(pos, g), swapLeft(pos, g) };
		}
		case 8: {// 2,0
			return new EightPuzzleState[] { swapUp(pos, g),
					swapRight(pos, g), swapLeft(pos, g) };
		}
		case 9: {// 2,1
			return new EightPuzzleState[] { swapUp(pos, g),
					swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g) };
		}
		case 10: {// 2,2
			return new EightPuzzleState[] { swapUp(pos, g),
					swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g) };
		}
		case 11: {// 2,3
			return new EightPuzzleState[] { swapDown(pos, g),
					swapRight(pos, g), swapLeft(pos, g) };
		}
		case 12: {// 3,0
			return new EightPuzzleState[] { swapUp(pos, g), swapLeft(pos, g) };
		}
		case 13: {// 3,1
			return new EightPuzzleState[] { swapUp(pos, g), swapDown(pos, g),
					swapLeft(pos, g) };
		}
		case 14: {// 3,2
			return new EightPuzzleState[] { swapUp(pos, g), swapDown(pos, g),
					swapLeft(pos, g) };
		}
		case 15: {// 3,3
			return new EightPuzzleState[] { swapDown(pos, g),
					swapLeft(pos, g) };
		}

		}

		return null;
	}

	// ==============================================================
	// ==============================================================

	// ==============================================================
	// SWAP UP METHOD
	// ==============================================================

	private EightPuzzleState swapUp(Point pos, int g) {

		int[][] newState = new int[4][4];

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				newState[i][j] = state[i][j];

		newState[pos.x][pos.y] = state[pos.x][pos.y + 1];
		newState[pos.x][pos.y + 1] = 0;

		return new EightPuzzleState(newState, this);
	}

	// ==============================================================
	// SWAP RIGHT METHOD
	// ==============================================================
	private EightPuzzleState swapRight(Point pos, int g) {

		int[][] newState = new int[4][4];

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				newState[i][j] = state[i][j];

		newState[pos.x][pos.y] = state[pos.x + 1][pos.y];
		newState[pos.x + 1][pos.y] = 0;

		return new EightPuzzleState(newState, this);
	}

	// ==============================================================
	// SWAP DOWN METHOD
	// ==============================================================

	private EightPuzzleState swapDown(Point pos, int g) {

		int[][] newState = new int[4][4];

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				newState[i][j] = state[i][j];

		newState[pos.x][pos.y] = state[pos.x][pos.y - 1];
		newState[pos.x][pos.y - 1] = 0;

		return new EightPuzzleState(newState, this);

	}

	// ==============================================================
	// SWAP LEFT METHOD
	// ==============================================================
	private EightPuzzleState swapLeft(Point pos, int g) {
		int[][] newState = new int[4][4];

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				newState[i][j] = state[i][j];

		newState[pos.x][pos.y] = state[pos.x - 1][pos.y];
		newState[pos.x - 1][pos.y] = 0;

		return new EightPuzzleState(newState, this);
	}

	// ==============================================================
	// TO STRING METHOD
	// ==============================================================

	public String toString() {

		String output = "F = " + g_val + " + " + h_val + " = "
				+ (g_val + h_val) + "\n";

		for (int i = 3; i >= 0; i--) {
			for (int j = 0; j < 4; j++) {
				output += state[j][i] + "\t";
			}
			output += "\n";
		}
		return output;

	}

	public int compareTo(EightPuzzleState other) {
		return (int) (this.f_val - other.getFVal());
	}

	// =================================================================
	// TOSTRING METHODS (FOR NEO4J)
	// =================================================================

	public String stateToString() {
		String stateString = this.toString();
		return stateString;
	}

	public String neighboursToString() {
		String neighboursString = "";

		EightPuzzleState[] neighbours = getNeighbours();

		neighboursString = neighbours.length + "\n";
		
		for (EightPuzzleState n : neighbours)
			neighboursString += n.toString() + "\n";

		return neighboursString;
	}

	public String parentToString() {

		if (parent == null) {
			return "null";
		} else {
			String parentString = parent.toString();
			return parentString;
		}
	}

}
