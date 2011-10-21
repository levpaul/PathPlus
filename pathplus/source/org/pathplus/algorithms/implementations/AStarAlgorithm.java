package org.pathplus.algorithms.implementations;
import java.io.*;
import java.util.*;

public class AStarAlgorithm {


	private PriorityQueue<DEightPuzzle> forwardOpenList = new PriorityQueue<DEightPuzzle>();

	private Hashtable<String, DEightPuzzle> fol = new Hashtable<String, DEightPuzzle>(200000);
	private Hashtable<String, DEightPuzzle> forwardClosedList = new Hashtable<String, DEightPuzzle>(200000);
	private DEightPuzzle backInit;
	private DEightPuzzle forwardInit;

	private BufferedWriter bw;

	private int evals, t_evals, a;
	private int[] lengths;
	private long timer;


	DEightPuzzle middle = null;

	private double fLim = 0;
	private int[] gCounts = new int[100];

	private int numVals = 0;
	private String problem;
	private int ttime, tspace;
	private long tstart;
	private boolean timeUp = false;

	public void start(){
		try{
			tstart = System.currentTimeMillis();

			BufferedReader br = new BufferedReader(new FileReader("testset.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("REG A RESULTS.txt"));

			double time = System.currentTimeMillis();
			t_evals = 0;
			int space = 0;
			int length = 0;
			double t_time = 0, t_space =0 , t_length=0;

			int numProblems = 100;

			lengths = new int[numProblems];

			for(a = 0; a < numProblems; a++){


				timer = System.currentTimeMillis();

				System.out.println("Problem #" + (a+1));

				int[][] testState = new int[3][3];
				evals = 0;

				StringTokenizer st = new StringTokenizer(br.readLine());

				if(st.countTokens() == 0)
					st = new StringTokenizer(br.readLine());

				testState[0][2] = Integer.parseInt(st.nextToken());
				testState[1][2] = Integer.parseInt(st.nextToken());
				testState[2][2] = Integer.parseInt(st.nextToken());
				st = new StringTokenizer(br.readLine());
				testState[0][1] = Integer.parseInt(st.nextToken());
				testState[1][1] = Integer.parseInt(st.nextToken());
				testState[2][1] = Integer.parseInt(st.nextToken());
				st = new StringTokenizer(br.readLine());
				testState[0][0] = Integer.parseInt(st.nextToken());
				testState[1][0] = Integer.parseInt(st.nextToken());
				testState[2][0] = Integer.parseInt(st.nextToken());


				
				int[][] goalState = {{7,4,1},{8,5,2},{0,6,3}};

				if(a == a){
					backInit = new DEightPuzzle(goalState,testState);//initial 8-puz for forwards search
					forwardInit = new DEightPuzzle(testState,goalState);//initial 8-puz for backwards search

					System.out.println("Start state: " + forwardInit + "\nGoal state: " + backInit);

					fLim = forwardInit.getFval();

					forwardOpenList.add(forwardInit);
					fol.put(forwardInit.key, forwardInit);

					forwardSearch();
					if(timeUp)
					{
						br.close();
						bw.close();
						return;
					}

					System.out.println("Found!");

					DEightPuzzle current = middle;

					int counter = 0;

					System.out.println("Path is of length: " + counter); 

					System.out.println("Number of heuristic comparisons made: " + numVals);

					ttime+= ((System.currentTimeMillis() - timer)/1000.0);
					tspace += (forwardOpenList.size() + forwardClosedList.size());

					bw.write("Problem: " + problem + "\n");
					bw.write("Size: " + (forwardOpenList.size() + forwardClosedList.size()) + "\n");
					bw.write("Length: " + lengths[a] + "\n");
					bw.write("Time: " + ((System.currentTimeMillis() - timer)/1000.0) + " seconds\n\n") ;

					if(a > 0){
						bw.write("==================================\n");
						bw.write("problems solved: " + (a+1) + "\n");
						bw.write("Avg time: " + (ttime/a) + " seconds\n");
						bw.write("Avg space: " + (tspace/a) + " nodes\n");
						bw.write("==================================\n\n\n");
					}
					t_length += counter;


					System.out.println("Total concrete nodes: " + (forwardOpenList.size() + forwardClosedList.size()));

					t_space += (forwardOpenList.size() + forwardClosedList.size());
					t_evals += numVals;

					forwardOpenList.clear();
					forwardClosedList.clear();
					numVals = 0;

				}
				if(a == (numProblems -1)){
					bw.close();
					br.close();
					System.out.println("Average number of evaluations: " + (t_evals/(double)numProblems));
					System.out.println("Average length is: " + (t_length/(double)numProblems));
					System.out.println("Average space is: " + (t_space/(double)numProblems));

					System.out.println("Total time taken was: " + ((System.currentTimeMillis() - time)/1000.0) + " seconds");

					double c = 0;
					for(int i = 0; i < lengths.length; i++){
						System.out.println("Problem " + i + ": " +  lengths[i]);
						c+= lengths[i];
					}
					System.out.println(c/lengths.length);

					int total = 0;
					System.out.println("gcounts:");
					for(int i = 0; i < gCounts.length; i++){
						
						if(gCounts[i] > 0){
							System.out.println("g: \t" + i + "\tcount: \t" + gCounts[i]);
							total += gCounts[i];
						}
					}
					
					System.out.println("Total: " + total);
				}
			}
		}catch (Exception e){
			backup();
			System.out.println("Error:" + e);
			for(int i = 0; i < e.getStackTrace().length; i++){
				System.out.println(e.getStackTrace()[i]);
			}
		}
	}

	private void backup(){
		try{
			bw.close();
			MStart map = new MStart();
			map.start();

		}catch (Exception e){

		}
	}

	private boolean forwardSearch(){

		DEightPuzzle current = null;

		while(forwardOpenList.size() > 0){

			if(System.currentTimeMillis() - tstart > 12600000)
			{
				timeUp = true;
				return false;

			}

			current = forwardOpenList.poll();

			gCounts[(int) current.calcH()] ++;



			fol.remove(current.key);
			forwardClosedList.put(current.key, current);

			DEightPuzzle[] neighbours = current.getNeighbors();

			//go through possible neighbours and add them if they are NOT in the closed list or in the openlist
			for(int i = 0; i < neighbours.length; i++){
				boolean isInClosed = false;



				if(forwardClosedList.containsKey(neighbours[i].key)){//check that new node isn't in closed list
					isInClosed = true;
				}

				if(isInClosed == false){//check that new node isn't in openlist
					if(fol.containsKey(neighbours[i].key))
						isInClosed = true;
				}//end if

				if(isInClosed == false){
					if(neighbours[i].checkDup(backInit) == true){//check if node is a goal
						middle = neighbours[i];
						System.out.println(neighbours[i] + "\n" + backInit);
						lengths[a] = (int) neighbours[i].getGval();
						return false;
					}
				}
//				now we know the node is not in open or closed and is not goal, so we can add to openList

				if(isInClosed == false){
					fol.put(neighbours[i].key, neighbours[i]);
					forwardOpenList.add(neighbours[i]);
				}//end if
			}//end outter for
		}//END WHILE LOOP

		return true;
	}

}
