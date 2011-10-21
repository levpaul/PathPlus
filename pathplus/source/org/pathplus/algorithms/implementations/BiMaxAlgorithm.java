package org.pathplus.algorithms.implementations;
import java.io.*;
import java.util.*;

import org.pathplus.algorithms.BaseAlgorithm;


public class BiMaxAlgorithm extends BaseAlgorithm{

	private ArrayList<PriorityQueue<MEightPuzzle>> ol = new ArrayList<PriorityQueue<MEightPuzzle>>();
	private Hashtable<String, MEightPuzzle>[] cl = new Hashtable[2];
	private Hashtable<String, MEightPuzzle>[] hol = new Hashtable[2];
	private int count = 0;




	//  private ArrayList<EightPuzzle> forwardClosedList = new ArrayList<EightPuzzle>();

	private long[] ftime = new long[10];
	private long[] btime = new long[10];

	private double[] fmin = new double[2];
	private MEightPuzzle[] hval = new MEightPuzzle[2];
	private BufferedWriter bw, data;

	private int tspace, ttime;


	private int l_min = 99999999;


	private int evals, t_evals, l;

	MEightPuzzle middle = null;

	private double fLim = 0;

	private int numVals = 0, d = 0, od = 0, a;
	private double[] flim = new double[2];
	private int[] lengths;

	private long start;
	private int countex = 0;

	private ArrayList<ArrayList<Integer>> fEx = new ArrayList<ArrayList<Integer>>(2);



	@SuppressWarnings("unchecked")
	public void start(){
		start = System.currentTimeMillis();

		ol.add(new PriorityQueue<MEightPuzzle>());
		ol.add(new PriorityQueue<MEightPuzzle>());


		try{
			BufferedReader br = new BufferedReader(new FileReader("testset.txt"));
			bw = new BufferedWriter(new FileWriter("BIMAX RESULTS.txt"));


			cl[0] = new Hashtable<String, MEightPuzzle>(200000);
			cl[1] = new Hashtable<String, MEightPuzzle>(200000);

			hol[0] = new Hashtable<String, MEightPuzzle>(200000);
			hol[1] = new Hashtable<String, MEightPuzzle>(200000);

			double time = System.currentTimeMillis();
			t_evals = 0;
			int space = 0;
			int length = 0;
			double t_time = 0, t_space =0 , t_length=0;

			int numProblems = 1;
			lengths = new int[numProblems];

			for(a = 0; a < numProblems; a++){

				l_min = 99999999;


				double fLim = 0;


				//====================================================================================
				middle = null;
				long timer = System.currentTimeMillis();
				int[][] testState = new int[4][4];
				evals = 0;
				String problem;
				StringTokenizer st = new StringTokenizer(br.readLine());

				if(st.countTokens() == 0)
					st = new StringTokenizer(br.readLine());

				problem = st.nextToken();

				System.out.println("Problem: " + problem);

				testState[0][3] = Integer.parseInt(st.nextToken());
				testState[1][3] = Integer.parseInt(st.nextToken());
				testState[2][3] = Integer.parseInt(st.nextToken());
				testState[3][3] = Integer.parseInt(st.nextToken());

				//		            st = new StringTokenizer(br.readLine());
				testState[0][2] = Integer.parseInt(st.nextToken());
				testState[1][2] = Integer.parseInt(st.nextToken());
				testState[2][2] = Integer.parseInt(st.nextToken());
				testState[3][2] = Integer.parseInt(st.nextToken());

				//		            st = new StringTokenizer(br.readLine());
				testState[0][1] = Integer.parseInt(st.nextToken());
				testState[1][1] = Integer.parseInt(st.nextToken());
				testState[2][1] = Integer.parseInt(st.nextToken());
				testState[3][1] = Integer.parseInt(st.nextToken());

				//		            st = new StringTokenizer(br.readLine());
				testState[0][0] = Integer.parseInt(st.nextToken());
				testState[1][0] = Integer.parseInt(st.nextToken());
				testState[2][0] = Integer.parseInt(st.nextToken());
				testState[3][0] = Integer.parseInt(st.nextToken());


				int[][] goalState = {{12,8,4,0},{13,9,5,1},{14,10,6,2},{15,11,7,3}};
				//		            int[][] goalState = {{13,9,5,1},{14,10,6,2},{15,11,7,3},{0,12,8,4}};

				MEightPuzzle backInit = new MEightPuzzle(goalState,testState);//initial 8-puz for forwards search
				MEightPuzzle forwardInit = new MEightPuzzle(testState,goalState);//initial 8-puz for backwards search

				System.out.println("Start state: " + forwardInit + "\nGoal state: " + backInit);


				if(a >= 0){

					fEx.add( new ArrayList<Integer>());
					fEx.add( new ArrayList<Integer>());


					flim[0] = forwardInit.getFval();
					flim[1] = backInit.getFval();

					hol[0].put(forwardInit.key, forwardInit);
					hol[1].put(backInit.key, backInit);

					ol.get(0).add(forwardInit);
					ol.get(1).add(backInit);


					//used to store opposite goal states for DIFF method
					hval[0] = forwardInit;
					hval[1] = backInit;

					while(!ol.get(0).isEmpty() && !ol.get(1).isEmpty()){//step 4
						//						if(++count % 1000 == 0)
						//							System.out.println("fol: " + ol.get(0).size() + ", bol: " + ol.get(1).size() + "\tfcl: " + cl[0].size() + ", bcl: " + cl[1].size() + "  \t\ttotal: " + (ol.get(0).size() + ol.get(1).size() + cl[0].size() + cl[1].size()) + "\tlim: " + l_min);



						//we are suppose to set the direction based on cardinality principle AFTER an f-limit has been drained
						if(ol.get(0).size() <= ol.get(1).size())
							d = 0;
						else d = 1;//set the direction of search, step 5

						od = 1-d;//set the opposite direction, STEP 6

						fmin[od] = 999999; //step 1, 2, 3 complete


						while(true){//search until no nodes remain within flim
							MEightPuzzle current = null;

							System.out.println("flim: " + flim[d] + " dir: " + d);

							if(System.currentTimeMillis() - start > 28800000){//check timelimit
								System.out.println("timeup");
								br.close();
								bw.close();
								return;
							}

							if(ol.get(d).size() > 0 && ol.get(d).peek().getFval() <= flim[d])
							{
								current = ol.get(d).remove();//step 8
								hol[d].remove(current.key);

							}
							//end for loop, now we have current as the smallest node within flim, or null if no node satisfies this condition

							if(current == null){//step 7
								fEx.get(d).add(count);
								flim[d] += 1;//maybe should increment by 2, but I doubt it matters, as it breaks and if it re-enters it will just break again
								break;
							}

							cl[d].put(current.key, current);//step 9

							if(cl[od].containsKey(current.key)){//step 10
								System.out.println("got something: length of " + (current.getGval() + ((MEightPuzzle) (cl[od].get(current.key))).getGval()) + " when lmin is: " + l_min);
								//nip current in od and prune od

								//Nipping is done by NOT expanding current, which the if statement takes care of

								//						System.out.println("lmin = " + l_min + ", path = " + (((EightPuzzle) (cl[od].get(current.key))).getGval() + current.getGval())); 


								//pruning part




								Object[] t =  ol.get(od).toArray();
								MEightPuzzle[] temp = new MEightPuzzle[t.length];

								for(int i = 0; i < t.length; i++){
									temp[i] = (MEightPuzzle)t[i];
								}

								t = null;

								for(int i = 0; i < temp.length; i++){//go through openlist and find decendents of matcher - what if the decendents are more than one node further down?????
									if(temp[i].getParent().checkDup(current)){
										cl[od].put(temp[i].key, temp[i]);
										ol.get(od).remove(temp[i]);
										hol[od].remove(current.key);
										//										i--;
									}

								}
								temp = null;




								//take out of opposite search tree omega M
								//omega m is the set of nodes which are parents (?) 


							}else{
//								data = new BufferedWriter(new FileWriter("bimax.txt", true));
//								data.write(current.writeData(d, flim[d]));
//								data.close();
//								System.out.println(++countex);
//								if(countex == 73618)
//									System.out.println(current);
								expand(current);//step 13
							}
						}
					}//end while

					if (l_min > 1000)
						System.out.println("NO PATH EXIST");
					else{
						System.out.println("The path was found with a length of " + l_min);
						lengths[a] = l_min;
						l += l_min;
					}





					System.out.println("Found!");


					MEightPuzzle current = middle;




					System.out.println("index of middle in forward: " + cl[0].get(middle.key)
							+ ". index of middle in backward: " + cl[1].get(middle.key));

					int counter = 0;

					System.out.println("Path is of length: " + counter); 


					t_length += counter;


					System.out.println("Total concrete nodes: " + (ol.get(0).size() + cl[0].size() + ol.get(1).size() + cl[1].size()));

					t_space += (ol.get(0).size() + cl[0].size() + ol.get(1).size() + cl[1].size());
					t_evals += numVals;

					tspace += (ol.get(0).size() + cl[0].size() + ol.get(1).size() + cl[1].size());
					ttime += (System.currentTimeMillis() - timer)/1000.0;

					bw.write("Problem: " + problem + "\n");
					bw.write("Size: " + (ol.get(0).size() + cl[0].size() + ol.get(1).size() + cl[1].size()) + "\n");
					bw.write("Length: " + l_min + "\n");
					bw.write("Time: " + ((System.currentTimeMillis() - ttime)/1000.0) + " seconds\n\n") ;

					if(a != 0){
						bw.write("==================================\n");
						bw.write("problems solved: " + (a+1) + "\n");
						bw.write("Avg time: " + (ttime/(a+1)) + " seconds\n");
						bw.write("Avg space: " + (tspace/(a+1)) + " nodes\n");
						bw.write("==================================\n\n\n");
					}
					ol.get(0).clear();
					cl[0].clear();
					ol.get(1).clear();
					cl[1].clear();
					numVals = 0;
					hol[0].clear();
					hol[1].clear();

					if(a == (numProblems -1)){

						if(a != 0){
							bw.write("==================================\n");
							bw.write("problems solved: " + (a+1) + "\n");
							bw.write("Avg time: " + (ttime/a) + " seconds\n");
							bw.write("Avg space: " + (tspace/a) + " nodes\n");
							bw.write("==================================\n");
						}
						br.close();
						bw.close();


						System.out.println("Average length is: " + ((double)l/numProblems));
						System.out.println("Average space is: " + (t_space/numProblems));
						System.out.println("Time taken is: " + (timer/1000.0) + " seconds");




						System.out.println(flim[0] + " " + fEx.get(0).size() + "          " + flim[1] + " " +fEx.get(1).size());

						for(int i = 0; i < Math.min(fEx.get(0).size(), fEx.get(1).size()); i++){
							System.out.println("flim: " + (flim[0] - fEx.get(0).size()+ i + 0) + " size: "  + (Math.max(fEx.get(0).get(i),fEx.get(1).get(i))));
						}
						if(fEx.get(0).size()> fEx.get(1).size()){

							for(int i = fEx.get(1).size(); i < fEx.get(0).size(); i++){
								System.out.println("flim: " + (flim[0] - fEx.get(0).size()+ i ) + " size: "  + (fEx.get(0).get(i)));
							}

						}else{

							for(int i = fEx.get(0).size(); i < fEx.get(1).size(); i++){
								System.out.println("flim: " + (flim[0] - fEx.get(0).size()+ i) + " size: "  + (fEx.get(1).get(i)));
							}

						}



						System.out.println(count);
						for(int i = 0; i < lengths.length; i++){
							System.out.println("Problem " + i + ": " +  lengths[i]);

							return;
						}


					}


				}


				System.out.println("Total time taken was: " + ((System.currentTimeMillis() - time)/1000.0) + " seconds");

			} 

			bw.close();
			br.close();





		}catch (Exception e){
			System.out.println("Error:" + e);
			for(int i = 0; i < e.getStackTrace().length; i++){
				System.out.println(e.getStackTrace()[i]);
			}
		}
	}


	public void expand(MEightPuzzle m){

		boolean trim = false; //step 1

		MEightPuzzle[] neighbours = m.getNeighbors();

		for(int z = 0; z < neighbours.length; z++){
			MEightPuzzle n = neighbours[z];//step 2
			//step 3 taken care of inside EightPuzzle

			//			System.out.println("g1: " + n.getGval() + "   h2: " + hval[d].calcH(n) );
			//			if(n.calcH() - hval[od].calcH(n) != 0)
			//				System.out.println(5/0);
			//			
			//			double fd = Math.max(n.getFval(), fmin[od] + n.getGval() - hval[od].calcH(n));//step 4

			double fd = n.getFval();
			count++;

			MEightPuzzle temp;

			if(fd < l_min){
				//step 5 taken care of inside eightpuzzle (calc f val)

				if(!cl[d].containsKey(n.key)){//if n is not in the closed list on its own side

					boolean inOpen = false;
					MEightPuzzle otherNode = null;

					otherNode = (MEightPuzzle) hol[d].get(n.key);

					inOpen = hol[d].containsKey(n.key);

					//
					//===================STEP 6=====================
					//done

					if(!inOpen){//if n is not in the open list on its own side (and closed list)

						//step 7 done in creation of n in eightpuzzle

						ol.get(d).add(n);//step 8
						hol[d].put(n.key, n);

					}else if(n.getGval() < otherNode.getGval()){//step 9 - new path to duplicate OL node found which is shorter


						hol[d].remove(otherNode);

						ol.get(d).remove(otherNode);

						ol.get(d).add(n); //step 10, replace node

					}//end elseif

				}else {//when node n was already in the closed list
					temp =(MEightPuzzle)cl[d].get(n.key);

					if(temp.getGval() > n.getGval()){//step 11
						cl[d].remove(temp.key);//step 12
						//												System.out.println("shit" + cl[d].get(n.key));
						ol.get(d).add(n);//step 13
						hol[d].put(n.key, n);

					}


				}//now we know that the new node is not in its own tree

				MEightPuzzle other = null;

				if(cl[od].containsKey(n.key)){
					other = (MEightPuzzle) cl[od].get(n.key);
				}else {

					if(hol[od].containsKey(n.key)){
						other = (MEightPuzzle) hol[od].get(n.key);
					}

				}//end else

				if(other != null){//if n is in opposite tree

					if(other.getGval() + n.getGval() < l_min){//step 14
						System.out.println(l_min + "\n" + n + "\n" + other);
						l_min = (int) (other.getGval() + n.getGval());//step 15
						middle = n;
						trim = true;//step 17
					}
				}//end if

			}


		}//end for loop

		if(trim){//step 18


			for(int j = 0; j < 2; j++){
				Object[] t =  ol.get(j).toArray();
				MEightPuzzle[] temp = new MEightPuzzle[t.length];

				for(int i = 0; i < t.length; i++){
					temp[i] = (MEightPuzzle)t[i];
				}

				t = null;

				for(int i = 0; i < temp.length; i++){
					//					if(Math.max(ol.get(j).get(i).getFval(), fmin[1-j] + ol.get(j).get(i).getGval() - hval[1-j].calcH(ol.get(j).get(i))) >= l_min){

					if(temp[i].getFval() >= l_min){	
						ol.get(j).remove(i);
						hol[j].remove(temp[i].key);
						cl[j].put(temp[i].key, temp[i]);
					}//end if (trimming condition)


				}//end inner for

				temp = null;

			}//end trim - step 19 complete, end of expand


		}


	}//end expand

}
