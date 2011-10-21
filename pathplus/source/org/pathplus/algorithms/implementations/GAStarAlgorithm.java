package org.pathplus.algorithms.implementations;
import java.io.*;
import java.util.*;

public class GAStarAlgorithm {


	private PriorityQueue<FEightPuzzle> forwardOpenList = new PriorityQueue<FEightPuzzle>();
	private PriorityQueue<FEightPuzzle> backwardOpenList = new PriorityQueue<FEightPuzzle>();
	private BufferedWriter bw;
	private int ttime,  expands = 0, tReal = 0;
	private long timer, tlimit = 25200000, tspace;
	private int  gLim = 0;
	private double gfLim = 0, gbLim = 0 ;
	private FEightPuzzle[] hval = new FEightPuzzle[2];
	private int countage = 0;;
	private int[] fdist = new int[31], bdist = new int[31];
	private int dups = 0;

	private Hashtable<String, FEightPuzzle> forwardClosedList = new Hashtable<String, FEightPuzzle>(200000);

	private Hashtable<String, FEightPuzzle> fol = new Hashtable<String, FEightPuzzle>(200000);
	private Hashtable<String, FEightPuzzle> bol = new Hashtable<String, FEightPuzzle>(200000);

	//  private ArrayList<EightPuzzle> forwardClosedList = new ArrayList<EightPuzzle>();
	private Hashtable<String, FEightPuzzle> backwardClosedList = new Hashtable<String, FEightPuzzle>(200000);

	private long[] ftime = new long[10];
	private long[] btime = new long[10];
	private int[] lengths;

	private int evals, t_evals, l_min = 1000;
	private int soln = 0;

	FEightPuzzle middle = null;

	private String problem;
	private double fLim = 0;

	private int a = 0;
	double f1;
	private double f2;
	private boolean timeUp = false;
	private long tstart;
	private int numVals = 0, tNODEAV;
	private BufferedWriter data;
	private int bGen = 0, fGen = 0;

	public void start(){
		try{
			tstart = System.currentTimeMillis();
			BufferedReader br = new BufferedReader(new FileReader("testset.txt"));
			
			bw = new BufferedWriter(new FileWriter("FTB RESULTS.txt"));

			double time = System.currentTimeMillis();
			t_evals = 0;
			int space = 0;
			int length = 0;
			double t_time = 0, t_space =0 , t_length=0;

			int numProblems = 50;

			data = new BufferedWriter(new FileWriter("gastar.txt"));
			data.write("");
			data.close();


			data = new BufferedWriter(new FileWriter("predictions.txt"));
			data.write("");
			data.close();
			
			lengths = new int[numProblems];

			
			
			
			createDist(new int[][]{{7,4,1},{8,5,2},{0,6,3}});
	
			ArrayList<FEightPuzzle> li = new ArrayList<FEightPuzzle>(backwardClosedList.values());
			backwardClosedList.clear();
			
			System.out.println(li.size());
			
			 fdist = new int[31];
			 
			
			while(li.isEmpty() != true){
				fdist[(int) li.remove(0).calcH()]++;
			}
			
			int fTot = 0;
			for(int i = 0; i < fdist.length; i++){
				fTot += fdist[i];
				fdist[i] = fTot;
			}
			
			
			//TODO
			for(int i = 0; i < fdist.length; i++)
				System.out.println(i + ".\t" + fdist[i]);
			
			
			fol.clear();
			bol.clear();
			backwardOpenList.clear();
			forwardOpenList.clear();
			
			
			
			for(a = 0; a < numProblems; a++){

				timer = System.currentTimeMillis();

				l_min = 1000;
				int[][] testState = new int[3][3];
				evals = 0;


				System.out.println("Problem #" + (a+1));

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


	

				if(a >= 0){
					FEightPuzzle backInit = new FEightPuzzle(goalState,testState, 2);//initial 8-puz for forwards search
					FEightPuzzle forwardInit = new FEightPuzzle(testState,goalState, 1);//initial 8-puz for backwards search

					f1 = forwardInit.getFval();
					f2 = backInit.getFval();

					hval[1] = forwardInit;
					hval[0] = backInit;

				bGen = 0;
				fGen = 0;
					gLim = (int) f1;

					System.out.println("Start state: " + forwardInit + "\nGoal state: " + backInit);


					fLim = forwardInit.getFval();

					fol.clear();
					bol.clear();

					forwardOpenList.add(forwardInit);
					backwardOpenList.add(backInit);
					fol.put(forwardInit.key, forwardInit);
					bol.put(backInit.key, backInit);


					boolean notFound = true;


					gfLim = (int) Math.ceil(fLim/2); 
					gbLim = (int) Math.floor(fLim/2);

					ArrayList<Integer> fEx = new ArrayList<Integer>();


					countage = 0;
					//					countage = (forwardOpenList.size() + backwardOpenList.size() + forwardClosedList.size() + backwardClosedList.size());

					//					forwardInit.gFLim = Math.ceil(fLim*(24/42.0));
					//					backInit.gBLim = Math.floor(fLim*(20/42.0));
					FEightPuzzle.gFLim = gfLim;
					FEightPuzzle.gBLim = gbLim;
					FEightPuzzle.fLim = fLim;

					createDist(testState);
					
					
					
					ArrayList<FEightPuzzle> list = new ArrayList<FEightPuzzle>(backwardClosedList.values());
					backwardClosedList.clear();
					
					System.out.println(list.size());
					
					 bdist = new int[31];
					
					while(list.isEmpty() != true){
						bdist[(int) list.remove(0).calcH()]++;
					}
					
					
					//TODO
					for(int i = 0; i < fdist.length; i++)
						System.out.println(i + ".\t" + fdist[i]);
					
//					System.out.println(5/0);
					
					
					
					backwardClosedList.clear();
					bol.clear();
					backwardOpenList.clear();
					
					
					bol.put(backInit.key, backInit);
					backwardOpenList.add(backInit);
					fol.put(forwardInit.key, forwardInit);
					forwardOpenList.add(forwardInit);
					
					
					FEightPuzzle.gFLim = gfLim;
					FEightPuzzle.gBLim = gbLim;
					FEightPuzzle.fLim = fLim;

					notFound = true;

					forwardSearch();
					notFound = backwardSearch();

				





					while(notFound == true){
						fEx.add(countage);
						//						countage = (forwardOpenList.size() + backwardOpenList.size() + forwardClosedList.size() + backwardClosedList.size());
						if(timeUp){
							br.close();
							bw.close();
							return;
						}
						FEightPuzzle.fLim++;
						fLim = FEightPuzzle.fLim;

						System.out.println("fol: " + forwardOpenList.size() + " bol: " + backwardOpenList.size());

						if(forwardOpenList.size() < backwardOpenList.size()){
							FEightPuzzle.gFLim++;
							gfLim = FEightPuzzle.gFLim;
							System.out.println("Flim: " + FEightPuzzle.fLim + " Glim: " + gfLim + "(forward -1)");
							notFound = forwardSearch();
							if(notFound){
								System.out.println("Flim: " + FEightPuzzle.fLim + " Glim: " + gbLim + "(back -2)");
								notFound = backwardSearch();
							}
						}else{
							FEightPuzzle.gBLim++;
							gbLim = FEightPuzzle.gBLim;
							System.out.println("Flim: " + FEightPuzzle.fLim + " Glim: " + gbLim + "(back -1)");
							notFound = backwardSearch();
							if(notFound){
								System.out.println("Flim: " + FEightPuzzle.fLim + " Glim: " + gfLim + "(forward -2)");
								notFound = forwardSearch();
							}
						}


					}


					//					countage = (forwardOpenList.size() + backwardOpenList.size() + forwardClosedList.size() + backwardClosedList.size());

					fEx.add(countage);

					System.out.println("Found!");

					try{
						data = new BufferedWriter(new FileWriter("gastar.txt", true));
						data.close();
					}
					catch(Exception e){
						System.out.println("error with gastar printing");
					}

					tReal = 0;

					int counter = 0;
					System.out.println("Path is of length: " + counter); 

					ttime += ((System.currentTimeMillis() - timer)/1000.0);
					tspace += countage;
					tNODEAV += tReal;



					bw.write("Problem: " + problem + "\n");
					bw.write("Size: " + (countage) + "\n");
					bw.write("Size before last expansion: " + tReal + "\n");
					bw.write("Length: " + lengths[a] + "\n");
					bw.write("Time: " + ((System.currentTimeMillis() - timer)/1000.0) + " seconds\n\n") ;

					if(a >= 1){
						bw.write("==================================\n");
						bw.write("problems solved: " + (a+1) + "\n");
						bw.write("Avg time: " + (ttime/(a+1)) + " seconds\n");
						bw.write("Avg space: " + (tspace/(a+1)) + " nodes\n");
						bw.write("Size before last expansion: " + (tNODEAV/(a+1)) + "\n");
						bw.write("==================================\n\n\n");
					}



					t_length += counter;


					System.out.println("Total concrete nodes: " + (forwardOpenList.size() + forwardClosedList.size() + backwardOpenList.size() + backwardClosedList.size()));

					t_space += (forwardOpenList.size() + forwardClosedList.size() + backwardOpenList.size() + backwardClosedList.size());
					t_evals += numVals;


					System.out.println("fol: " + forwardOpenList.size());
					System.out.println("fcl: " + forwardClosedList.size());
					System.out.println("bol: " + backwardOpenList.size());
					System.out.println("bcl: " + backwardClosedList.size());


					forwardOpenList.clear();
					forwardClosedList.clear();
					backwardOpenList.clear();
					backwardClosedList.clear();
					fol.clear();
					bol.clear();

					numVals = 0;

					
					//TODO 
					calcProb(fdist, bdist, middle, lengths[a]);



					if(a == (numProblems -1)){
						br.close();
						bw.close();
						System.out.println("Average number of evaluations: " + (t_evals/numProblems));
						System.out.println("Average length is: " + (t_length/numProblems));
						System.out.println("Average space is: " + (t_space/numProblems));

						double to = 0;

						for(int i = 0; i < lengths.length; i++){
							System.out.println("Problem " + i + ": " +  lengths[i]);
						}

						for(int i = 0; i < 5; i++){
							System.out.println("FORWARD Time part " + i + " was: " + ftime[i]/1000.0 + " seconds");
							to += ftime[i];
						}
						System.out.println(to/1000 + " seconds total for forward");
						to = 0;

						for(int i = 0; i < 5; i++){
							System.out.println("BACK Time part " + i + " was: " + btime[i]/1000.0 + " seconds");
							to += btime[i];
						}
						System.out.println(to/1000 + " seconds total for backward");


						System.out.println("Total time taken was: " + ((System.currentTimeMillis() - time)/1000.0) + " seconds");



					}//end last prob


					System.out.println("======== F EXPANDS =======");
					for(int i = 0; i < fEx.size(); i++){
						System.out.println("flim: " + (fLim - fEx.size() + i + 1) + " size: "  + fEx.get(i));
					}

				}//end if a ==


				System.out.println(middle + " \n\n" + middle.d + "\n\n" + middle.gFLim + " " + middle.gBLim + " " + middle.fLim);



			}


		}catch (Exception e){
			try{
				bw.close();}catch(Exception f){}
				System.out.println("Error:" + e);
				for(int i = 0; i < e.getStackTrace().length; i++){
					System.out.println(e.getStackTrace()[i]);
				}
		}
	}

	private void calcProb(int[] fDist, int[] bDist, FEightPuzzle mid, int d) {
		// TODO Auto-generated method stub
		
		int fTot = 0, bTot = 0, fCol, bCol;
		
//		for (int i : fDist){
//			fTot += i;
//			i = fTot;
//		}
		

			fTot = fDist[30];
		
		
//		for(int i : bDist){
//			bTot += i;
//			i = bTot;
//	}
		for(int i = bDist.length-1; i >= 0; i--){
			bTot += bDist[i];
			bDist[i] = bTot;
		}
		
		double b = 8/3.0;
		
		if(mid.dir == 1){
			fCol = (int) mid.getGval();
			bCol = d-fCol;
		}else{
			bCol = (int) mid.getGval();
			fCol = d-bCol;
		}
		
		//now we have distribution info, collision points and depth
	
		double fSum = 0, bSum = 0;
		
		System.out.println("FTOTAL: " + fTot + "BCOL: " + bCol + " B: " + b);
		
		
		
		for(int i = 0; i < fCol; i++){
			fSum += Math.pow(b, i)*((double)fDist[d-fCol]/(double)fTot);
		}
		
		for(int i = 0; i < bCol; i++){
			bSum += Math.pow(b, i)*((double)bDist[d-bCol]/(double)bTot);
		}
		
		try{
			BufferedWriter buf = new BufferedWriter(new FileWriter("predictions.txt", true));
			buf.write((a+1) +	". predict forward:\t" + fSum +  "\tf-actual:\t" + fGen + "\tpredict back:\t" + bSum + "\tb-actual:\t" + bGen + "\n");
			buf.close();
		}catch(Exception e){System.out.println("Error");}
	
	}

	private void createDist(int[][] start) {
		// TODO Auto-generated method stub

		backwardOpenList.clear();
		forwardOpenList.clear();
		fol.clear();
		bol.clear();
		
		FEightPuzzle init = new FEightPuzzle(start, start, 2);
		
		bol.put(init.key, init);
		backwardOpenList.add(init);
		
		
		FEightPuzzle.fLim = 99999;
		FEightPuzzle.gFLim = 99999;
		FEightPuzzle.gBLim = 99999;

		while(backwardOpenList.isEmpty() == false){


				FEightPuzzle current = backwardOpenList.poll();
				bol.remove(current.key);



			if(backwardClosedList.containsKey(current.key))
				System.out.println(5/0);
			if(bol.containsKey(current.key))
				System.out.println(5/0);


			backwardClosedList.put(current.key, current);

			FEightPuzzle[] neighbours = current.getNeighbors(gLim);

			//go through possible neighbours and add them if they are NOT in the closed list
			for(int i = 0; i < neighbours.length; i++){
				boolean isInClosed = false;

				countage++;

				if(backwardClosedList.containsKey(neighbours[i].key)){

					if(backwardClosedList.get(neighbours[i].key).getGval() > neighbours[i].getGval()){
						System.out.println(5/0);
						backwardClosedList.remove(neighbours[i].key);
						backwardOpenList.add(neighbours[i]);
						bol.put(neighbours[i].key, neighbours[i]);
					}
					isInClosed = true;

				}//end if


				//==============
				//not likely to be used with ga*
				//==============
				if(bol.containsKey(neighbours[i].key)){

					if(((FEightPuzzle)bol.get(neighbours[i].key)).getGval() > neighbours[i].getGval()){
						System.out.println(5/0);
						bol.remove(neighbours[i].key);
						backwardOpenList.remove(neighbours[i]);
						backwardOpenList.add(neighbours[i]);
						bol.put(neighbours[i].key, neighbours[i]);
					}

					isInClosed = true;

				}

				if(isInClosed == false){//we know the neighbour node isn't in the open or closed list in this direction

					//if not in a closed list and not in opposite openlist
					backwardOpenList.add(neighbours[i]);
					bol.put(neighbours[i].key,neighbours[i]);

				}

			}//end outter for

		}    
		
		System.out.println(backwardClosedList.size());
		
	}





	//==================================================================================================
	//==================================================================================================
	//										      THIS IS
	//		***F***O***R***W***A***R***D***						***S***E***A***R***C***H***
	//==================================================================================================
	//==================================================================================================
	private boolean forwardSearch(){

		FEightPuzzle current = null;

		int count = 0;
		int currentIndex = 0;

		Object[] temp = forwardOpenList.toArray();		
		forwardOpenList.clear();

		for(int i = 0; i < temp.length; i++){
			forwardOpenList.add((FEightPuzzle)temp[i]);
			temp[i] = null;
		}

		temp = null;


		while(forwardOpenList.size() > 0){



			if(System.currentTimeMillis() - tstart > tlimit)
			{
				timeUp = true;
				return true;
			}


			count++;

			long ti = System.currentTimeMillis();

			current = null;
			if(forwardOpenList.peek().getGval() < gfLim && forwardOpenList.peek().getFval() <= fLim){
				current = forwardOpenList.poll();
				fol.remove(current.key);
			}

			if(current == null){//double check
				System.out.println(count + " nodes processed - current was null");
				return true;
			}

			if(current.getGval() >= gfLim || current.getFval() > fLim)
				System.out.println(5/0);






			forwardClosedList.put(current.key, current);

			//			try{
			//				data = new BufferedWriter(new FileWriter("gastar.txt", true));
			//				data.write(current.writeData(0, fLim));
			//				data.close();
			//				System.out.println(++countex);
			//			}catch (Exception e){
			//				System.out.println("error: " + e);
			//			}

			ftime[0] += System.currentTimeMillis() - ti;
			ti = System.currentTimeMillis();

			FEightPuzzle[] neighbours = current.getNeighbors();


			//===============================================================================================
			//						NEIGHBOUR EXPAND
			//===============================================================================================
			//go through possible neighbours and add them if they are NOT in the closed list or in the openlist
			fGen += neighbours.length;
			for(int i = 0; i < neighbours.length; i++){
				
				boolean isInClosed = false;
				ti = System.currentTimeMillis();

				countage++;

				if(forwardClosedList.containsKey(neighbours[i].key)){


					if(forwardClosedList.get(neighbours[i].key).getGval() > neighbours[i].getGval()){
						System.out.println(5/0);
						forwardClosedList.remove(neighbours[i].key);
						forwardOpenList.add(neighbours[i]);
						fol.put(neighbours[i].key, neighbours[i]);
						dups++;
					}	

					isInClosed = true;
				}

				ftime[1] += System.currentTimeMillis() - ti;
				ti = System.currentTimeMillis();


				if(backwardClosedList.containsKey(neighbours[i].key))//check if in bcl
					System.out.println(5/0);

				if(fol.containsKey(neighbours[i].key)){

					if(fol.get(neighbours[i].key).getGval() > neighbours[i].getGval()){
						System.out.println(5/0);
						fol.remove(neighbours[i].key);
						forwardOpenList.remove(neighbours[i]);
						forwardOpenList.add(neighbours[i]);
						fol.put(neighbours[i].key, neighbours[i]);
					}

					isInClosed = true;

				}


				if(isInClosed == false){//check if goal else add to fol

					if(bol.containsKey(neighbours[i].key)){
						middle = neighbours[i];
						forwardOpenList.add(neighbours[i]);
						fol.put(neighbours[i].key, neighbours[i]);
						System.out.println(count + " nodes processed - ended with goal being found");
						lengths[a] = (int) Math.max(fLim, middle.getFval());
						//                                	l_min = (int) (neighbours[i].getGval() + backwardOpenList.get(j).getGval());
						return false;
					}


					forwardOpenList.add(neighbours[i]);
					fol.put(neighbours[i].key, neighbours[i]);

					ftime[4] += System.currentTimeMillis() - ti;
				}
			}//end outter for
		}



		System.out.println(count + " nodes processed");
		return true;

	}

	private boolean backwardSearch(){

		FEightPuzzle current = null;


		Object[] temp = backwardOpenList.toArray();		
		backwardOpenList.clear();

		for(int i = 0; i < temp.length; i++){
			backwardOpenList.add((FEightPuzzle)temp[i]);
			temp[i] = null;
		}

		if(backwardOpenList.size() != bol.size())
			System.out.println(5/0);
		temp = null;

		System.out.println("node at front (back): " + backwardOpenList.peek().getGval() + " " + backwardOpenList.peek().getFval());

		//		int gLim = (int) Math.floor((fLim)/2);

		int count = 0;
		while(backwardOpenList.size() > 0){

			if(System.currentTimeMillis() - tstart > tlimit)
			{
				timeUp = true;
				return true;
			}



			count++;

			long ti = System.currentTimeMillis();

			current = null;
			if(backwardOpenList.peek().getGval() < gbLim && backwardOpenList.peek().getFval() <= fLim){
				current = backwardOpenList.poll();
				bol.remove(current.key);
			}

			btime[0] += System.currentTimeMillis() - ti;
			ti = System.currentTimeMillis();

			if(current == null){
				System.out.println(count + " nodes processed - current was null");
				return true;
			}

			if(current.getGval() >= gbLim || current.getFval() > fLim)
				System.out.println(5/0);

			btime[1] += System.currentTimeMillis() - ti;
			ti = System.currentTimeMillis();


			if(backwardClosedList.containsKey(current.key))
				System.out.println(5/0);
			if(forwardClosedList.containsKey(current.key))
				System.out.println(5/0);
			if(fol.containsKey(current.key)){
				System.out.println(count + " " + current.getParent() + "\n\n" +  fol.remove(current.key).getParent().dir);
				System.out.println(5/0);
			}
			if(bol.containsKey(current.key))
				System.out.println(5/0);


			backwardClosedList.put(current.key, current);



			//		try{
			//				data = new BufferedWriter(new FileWriter("gastar.txt", true));
			//				data.write(current.writeData(1, fLim));
			//				data.close();
			//				System.out.println(++countex);
			//			}catch (Exception e){
			//				System.out.println("error: " + e);
			//			}

			FEightPuzzle[] neighbours = current.getNeighbors(gLim);
			bGen += neighbours.length;
			//go through possible neighbours and add them if they are NOT in the closed list
			for(int i = 0; i < neighbours.length; i++){
				boolean isInClosed = false;
				ti = System.currentTimeMillis();

				countage++;

				if(backwardClosedList.containsKey(neighbours[i].key)){

					if(backwardClosedList.get(neighbours[i].key).getGval() > neighbours[i].getGval()){
						System.out.println(5/0);
						backwardClosedList.remove(neighbours[i].key);
						backwardOpenList.add(neighbours[i]);
						bol.put(neighbours[i].key, neighbours[i]);
					}
					isInClosed = true;

				}//end if


				//==============
				//not likely to be used with ga*
				//==============
				if(bol.containsKey(neighbours[i].key)){

					if(((FEightPuzzle)bol.get(neighbours[i].key)).getGval() > neighbours[i].getGval()){
						System.out.println(5/0);
						bol.remove(neighbours[i].key);
						backwardOpenList.remove(neighbours[i]);
						backwardOpenList.add(neighbours[i]);
						bol.put(neighbours[i].key, neighbours[i]);
					}

					isInClosed = true;

				}


				btime[2] += System.currentTimeMillis() - ti;
				ti = System.currentTimeMillis();




				if(isInClosed == false){//we know the neighbour node isn't in the open or closed list in this direction


					if(fol.containsKey(neighbours[i].key)){
						middle = neighbours[i];
						backwardOpenList.add(neighbours[i]);
						bol.put(neighbours[i].key,neighbours[i]);
						lengths[a] = (int) Math.max(fLim, middle.getFval());
						System.out.println(count + " nodes processed - goal node was found");
						//                                 	l_min = (int) (neighbours[i].getGval() + forwardOpenList.get(j).getGval());
						return false;
					}



					//if not in a closed list and not in opposite openlist
					backwardOpenList.add(neighbours[i]);
					bol.put(neighbours[i].key,neighbours[i]);




					btime[4] += System.currentTimeMillis() - ti;
					ti = System.currentTimeMillis();


				}


			}//end outter for
		}



		// System.out.println("OpenList Size: " + backwardOpenList.size() + "\tClosedList Size: " + backwardClosedList.size());


		return true;
	}    





}
