import java.awt.*;
import java.util.*;
import org.pathplus.utils.state.BaseState;

public class EightPuzzleState extends BaseState{

	public int[][] state;
	public static Point[] startStateTable = new Point[16];
	public static ArrayList<EightPuzzleState> perimeter;
	public double g_val = 0;
	public double h_val = 0;
	public double f_val = 0;
	public boolean gExceed = false;
	private int numEvals = 0;
	EightPuzzleState origin;
	FEightPuzzle oldParent;
	public boolean dub, beenExpanded;
	public int dir;
	public int d;
	public int rhs;

	public static double ep;
	static public double fLim, gBLim, gFLim;

	public String key = "";

	public EightPuzzleState parent = null;

	private Point[] goalStateTable = new Point[16];

	public EightPuzzleState(int[][] state, int[][] goal, int d){
		dir = d;
		this.state = state;//constructor
		transformTable(goal);//creates lookup table for heuristic estimates
		g_val = 0;
		h_val = calcStartH();
		f_val = g_val + h_val;
		setKey();
		beenExpanded = false;
	}
	public EightPuzzleState(FEightPuzzle x){
		oldParent = x.parent;
		this.key = x.key;
		this.state = x.state;//constructor
		g_val = x.g_val;
		h_val = x.h_val;
		f_val = g_val + h_val;
	}

	public EightPuzzleState(int[][] state , EightPuzzleState parent){
		dir = parent.dir;
		this.state = state;//constructor
		this.parent = parent;
		this.goalStateTable = parent.goalStateTable;
		beenExpanded = false;
		g_val = parent.getGval() + 1;
		calcPH();
		f_val = g_val + h_val;
		setKey();
	}

	public EightPuzzleState(int[][] state , EightPuzzleState parent, boolean asdf){
		this.origin = parent.origin;
		dir = parent.dir;
		this.state = state;//constructor
		this.parent = parent;
		this.goalStateTable = parent.goalStateTable;
		beenExpanded = false;
		g_val = parent.getGval() + 1;
		calcPH();
		f_val = g_val + h_val;
		setKey();
	}

	//this method changes a goal state into a lookup table to help
	//compute the Manhattan distance.
	public static void makeStart(int[][] x){
		for(int i =0; i < 4; i ++)
			for(int j = 0; j <4; j++)
				startStateTable[x[i][j]] = new Point(i,j);
	}

	private void transformTable(int[][] x){
		for(int i =0; i < 4; i ++)
			for(int j = 0; j <4; j++)
				goalStateTable[x[i][j]] = new Point(i,j);
	}


	public void setKey(){

		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				if(i == 3 && j ==3)
					key += state[i][j];
				else
					key += state[i][j] + " ";

	}
	//==================================================================
	//                        ACCESSORS AND MUTATORS
	//==================================================================

	public int[][] getState(){
		return state;
	}

	public double getFval(){
		return h_val + g_val;
	}

	public void calcPH(){
		double min = 1337;
		for(int i = 0; i < perimeter.size(); i++){
			transformTable(perimeter.get(i).state);
			if(min > calcH() + perimeter.get(i).g_val){
				min = calcH() + perimeter.get(i).g_val;
			}
		}

		h_val = min;

	}

	public double getGval(){
		return g_val;
	}

	public EightPuzzleState getParent(){
		return parent;
	}

	//=====================================================================
	// CALC H METHOD TO ESTIMATE DISTANCE FROM CURRENT STATE TO GOAL STATE
	//=====================================================================
	public double calcStartH(){


		int sum = 0;

		for(int i=0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(state[i][j] != 0){
					sum += Math.abs(i - goalStateTable[state[i][j]].x);
					sum += Math.abs(j - goalStateTable[state[i][j]].y);
				}
			}
		}

		return sum;
	}


	public double calcH(){
		double sum = 0;

		for(int i=0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(state[i][j] != 0){
					sum += Math.abs(i - goalStateTable[state[i][j]].x);
					sum += Math.abs(j - goalStateTable[state[i][j]].y);
				}
			}
		}
		return sum * ep;
	}


	public double calcKeyH(){

		System.out.println(5/0);
		double sum = 0;

		for(int i=0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(state[i][j] != 0){
					sum += Math.abs(i - startStateTable[state[i][j]].x);
					sum += Math.abs(j - startStateTable[state[i][j]].y);
				}
			}
		}
		return sum * ep;
	}
	//==============================================================================
	//                        CHECK DUPLICATE METHOD
	//==============================================================================

	public boolean checkDup(EightPuzzleState x){

		boolean same = true;
		for(int i = 0 ; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(this.state[i][j] != x.getState()[i][j])
					return false;
			}
		}
		return same;
	}

	public boolean equals(Object o){

		EightPuzzleState x = (EightPuzzleState) o;


		boolean same = true;
		for(int i = 0 ; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(this.state[i][j] != x.getState()[i][j])
					return false;
			}
		}
		return same;
	}


	//========================================================================
	// GET NEIGHBORS METHOD WHICH FINDS AND RETURNS ALL POSSIBLE NEIGHBORS
	//========================================================================
	public EightPuzzleState[] getNeighbors(int g){


		Point pos = new Point();

		//nested for loop to find the empty slot
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(state[i][j] == 0){
					pos.x = i;
					pos.y = j;
				}
			}//end y
		}//end i


		int option = 4*pos.x + pos.y;

		switch(option){
		case 0: {//0,0
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g)};
		}case 1: {//0,1
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g)};
		}case 2: {//0,2
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g)};
		}case 3: {//0,3
			return new EightPuzzleState[]{swapDown(pos,g ), swapRight(pos, g)};
		}case 4: {//1,0
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapLeft(pos, g)};
		}case 5: {//1,1
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g)};
		}case 6: {//1,2
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g)};
		}case 7: {//1,3
			return new EightPuzzleState[]{swapDown(pos, g), swapRight(pos, g), swapLeft(pos, g)};
		}case 8: {//2,0
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapLeft(pos, g)};
		}case 9: {//2,1
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g)};
		}case 10: {//2,2
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g)};
		}case 11: {//2,3
			return new EightPuzzleState[]{swapDown(pos, g), swapRight(pos, g), swapLeft(pos, g)};
		}case 12: {//3,0
			return new EightPuzzleState[]{swapUp(pos,g ), swapLeft(pos, g)};
		}case 13: {//3,1
			return new EightPuzzleState[]{swapUp(pos,g ), swapDown(pos, g), swapLeft(pos, g)};
		}case 14: {//3,2
			return new EightPuzzleState[]{swapUp(pos,g ), swapDown(pos, g), swapLeft(pos, g)};
		}case 15: {//3,3
			return new EightPuzzleState[]{swapDown(pos,g ), swapLeft(pos, g)};
		}


		}

		return null;
	}


	public EightPuzzleState[] getNeighbors(){





		Point pos = new Point();

		//nested for loop to find the empty slot
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(state[i][j] == 0){
					pos.x = i;
					pos.y = j;
				}
			}//end y
		}//end i


		int option = 4*pos.x + pos.y;

		int g = (int) this.g_val;
		switch(option){
		case 0: {//0,0
			return new EightPuzzleState[]{swapUp(pos, g ), swapRight(pos, g)};
		}case 1: {//0,1
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g)};
		}case 2: {//0,2
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g)};
		}case 3: {//0,3
			return new EightPuzzleState[]{swapDown(pos,g ), swapRight(pos, g)};
		}case 4: {//1,0
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapLeft(pos, g)};
		}case 5: {//1,1
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g)};
		}case 6: {//1,2
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g)};
		}case 7: {//1,3
			return new EightPuzzleState[]{swapDown(pos, g), swapRight(pos, g), swapLeft(pos, g)};
		}case 8: {//2,0
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapLeft(pos, g)};
		}case 9: {//2,1
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g)};
		}case 10: {//2,2
			return new EightPuzzleState[]{swapUp(pos, g), swapRight(pos, g), swapDown(pos, g), swapLeft(pos, g)};
		}case 11: {//2,3
			return new EightPuzzleState[]{swapDown(pos, g), swapRight(pos, g), swapLeft(pos, g)};
		}case 12: {//3,0
			return new EightPuzzleState[]{swapUp(pos,g ), swapLeft(pos, g)};
		}case 13: {//3,1
			return new EightPuzzleState[]{swapUp(pos,g ), swapDown(pos, g), swapLeft(pos, g)};
		}case 14: {//3,2
			return new EightPuzzleState[]{swapUp(pos,g ), swapDown(pos, g), swapLeft(pos, g)};
		}case 15: {//3,3
			return new EightPuzzleState[]{swapDown(pos,g ), swapLeft(pos, g)};
		}


		}


		return null;
	}

	//==============================================================
	//==============================================================


	//==============================================================
	//                    SWAP UP METHOD    
	//==============================================================

	private EightPuzzleState swapUp(Point pos, int g){

		int[][] newState = new int[4][4];

		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				newState[i][j] = state[i][j];

		newState[pos.x][pos.y] = state[pos.x][pos.y+1];
		newState[pos.x][pos.y+1] = 0;


		return new EightPuzzleState(newState, this);
	}


	//==============================================================
	//                    SWAP RIGHT METHOD    
	//==============================================================
	private EightPuzzleState swapRight(Point pos, int g){

		int[][] newState = new int[4][4];

		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				newState[i][j] = state[i][j];

		newState[pos.x][pos.y] = state[pos.x+1][pos.y];
		newState[pos.x+1][pos.y] = 0;

		return new EightPuzzleState(newState, this);
	}

	//==============================================================
	//                    SWAP DOWN METHOD    
	//==============================================================

	private EightPuzzleState swapDown(Point pos, int g){

		int[][] newState = new int[4][4];

		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				newState[i][j] = state[i][j];

		newState[pos.x][pos.y] = state[pos.x][pos.y-1];
		newState[pos.x][pos.y-1] = 0;

		return new EightPuzzleState(newState, this);

	}

	//==============================================================
	//                    SWAP LEFT METHOD    
	//==============================================================
	private EightPuzzleState swapLeft(Point pos, int g){
		int[][] newState = new int[4][4];

		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				newState[i][j] = state[i][j];

		newState[pos.x][pos.y] = state[pos.x-1][pos.y];
		newState[pos.x-1][pos.y] = 0;

		return new EightPuzzleState(newState, this);
	}

	//==============================================================
	//                    TO STRING METHOD    
	//==============================================================


	public String toString(){

		String output = "F = " + g_val + " + " + h_val + " = " + (g_val + h_val) + "\n";

		for(int i= 3; i >= 0; i--){
			for(int j = 0; j <  4; j++){
				output += state[j][i] + "\t";
			}
			output += "\n";
		}
		return output;

	}


	public double calcH(EightPuzzleState x){
		int[][] temp = this.state;

		this.state = x.state;

		double sum = 0;

		for(int i=0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(state[i][j] != 0){
					sum += Math.abs(i - goalStateTable[state[i][j]].x);
					sum += Math.abs(j - goalStateTable[state[i][j]].y);
				}
			}
		}

		this.state = temp;

		System.out.println(5/0);
		return sum*ep;
	}




	@Override
	public int compareTo(EightPuzzleState x) {
		return (int) (this.f_val - x.f_val);
	}

	public String writeData(int d, double fl) {

		String data = "";

		data += d + ", " ;
		data += (int)this.g_val + ", ";
		data += (int)this.f_val + ", ";
		data += (int)fl + ", ";
		data += this.key;
		if(this.parent != null)
			data += ", " + this.parent.key;

		data+="\n";

		return data;
	}



}
