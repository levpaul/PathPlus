package org.pathplus.algorithms.implementations;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class ADIStarAlgorithm {


	private PriorityQueue<FEightPuzzle> openList;
	private Hashtable<String, FEightPuzzle> HopenList;

	private Hashtable<String, FEightPuzzle> closedList;
	private Hashtable<String, FEightPuzzle>  incos;

	private PriorityQueue<PEightPuzzle> fol;
	private Hashtable<String, PEightPuzzle> fhol;
	private Hashtable<String, PEightPuzzle> fcl;
	private Hashtable<String, PEightPuzzle> bol;
	private ArrayList<PEightPuzzle> perimeter;

	private PEightPuzzle meeting;

	private BufferedWriter bw;
	private long timer, tlimit = 10000;
	private boolean timeLeft = true;
	private FEightPuzzle end;
	private int[] adlengths;
	private int[] adilengths;
	private double[] adtime;
	private double[] aditime;
	private int[] expanded;

	private ArrayList<FEightPuzzle> currentPath = new ArrayList<FEightPuzzle>();

	FEightPuzzle middle = null;
	FEightPuzzle start, goal;

	private String problem;

	private int a = 0;

	private boolean timeUp = false;
	private long tstart;
	private String fileName = "";

	public ADIStarAlgorithm(double f, double p, String n){
		FEightPuzzle.ep = f;
		PEightPuzzle.ep = p;
		fileName = n;
	}
	
	public void start(){
		try{
			tstart = System.currentTimeMillis();
			BufferedReader br = new BufferedReader(new FileReader("sorted.txt"));
			bw = new BufferedWriter(new FileWriter(fileName + ".txt"));

			double time = System.currentTimeMillis();

			int space = 0;
			int length = 0;
			double t_time = 0, t_space =0 , t_length=0;

			int numProblems = 30;
			
			adlengths = new int[numProblems];
			adilengths = new int[numProblems];
			adtime = new double[numProblems];
			aditime = new double[numProblems];
			expanded = new int[numProblems];
			

			//			data = new BufferedWriter(new FileWriter("gastar.txt"));
			//			data.write("");
			//			data.close();


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

				if(a >= 0){



					start = new FEightPuzzle(testState, goalState, 1);
					goal = new FEightPuzzle(goalState, testState, 2);


					System.out.println(start +"\n" + goal +"\n=============================");
					FEightPuzzle.makeStart(testState);

					admain();


					//					FEightPuzzle c = start;
					//					int ccount = 0;
					//					
					//					while(c.getParent() != null){
					//						System.out.println(c);
					//						c = c.getParent();
					//						ccount++;
					//					}
					//					System.out.println(c);

					System.out.println("total length: " + start.g_val);

					bol.clear();
					fol.clear();
					incos.clear();
					fhol.clear();
					fcl.clear();
					openList.clear();
					HopenList.clear();
					closedList.clear();
					perimeter.clear();
					


				}
			}
			
			double ad = 0, adi = 0, adt = 0, adit = 0, expand = 0;
	
			for(int i = 0; i < adlengths.length; i++){
				System.out.println("Problem: " + (i+1));
				System.out.println("AD*:\t" + adlengths[i] + "\tADI*:\t" + adilengths[i] + "\tTimeIMP:\t" + adtime[i]);
				bw.write("\nProblem:\t" + (i+1) + "\tAD*:\t" + adlengths[i] + "\tADI*:\t" + adilengths[i] + "\tGenerated:\t" + expanded[i]);
				ad += adlengths[i];
				adi += adilengths[i];
				adt += adtime[i];
				adit += aditime[i];
				expand += expanded[i];
				
			}
			
			System.out.println("\n\nTOTAL ");
			System.out.println("AD*:\t" + ad/(double)adtime.length + "\tADI*:\t" + adi/(double)adtime.length + "\tTimeINIT:\t" + adt/adtime.length + "\tTimeIMP:\t" + adit/(double)adtime.length);
			
			bw.write("\n\nTOTAL:\t");
			bw.write("AD*:\t" + ad/(double)adtime.length + "\tADI*:\t" + adi/(double)adtime.length + "\tTimeINIT:\t" + adt/(double)adtime.length + "\tTimeIMP:\t" + adit/(double)adtime.length+ "\tNodes:\t" + expand/(double)adtime.length);
			
			bw.close();
		}catch(Exception e){
			try{
				bw.close();
			}catch(Exception f){}
			e.printStackTrace();
		}//end catch

	}



	private void admain(){

		start.g_val =1337;
		goal.g_val = 1337;
		start.rhs = 1337;
		goal.rhs = 0;

		openList = new PriorityQueue<FEightPuzzle>();
		HopenList = new Hashtable<String, FEightPuzzle>(200000);		
		closedList = new Hashtable<String, FEightPuzzle>(200000);
		incos = new Hashtable<String, FEightPuzzle>(200000);


		openList.add(goal);
		HopenList.put(goal.key, goal);
		improvePath();
		//		savePath();
		printPath();

		timeLeft = true;
		
		adtime[a] = (System.currentTimeMillis() - timer)/1000.0;

		runImprove();

		int count = 0;
		PEightPuzzle p = meeting;
		while(p.getParent() != null){
			p = p.getParent();
			count++;
		}




		FEightPuzzle f = p.oldParent;
		if(f!= null)
			while(f.getParent() != null){
				f = f.getParent();
				count++;
			}

		count++;
		
		aditime[a] = ((System.currentTimeMillis() - timer)/1000.0);
		adlengths[a] = (int) start.g_val;
		adilengths[a] = (int) (count + bol.get(meeting.key).g_val);
		
		
		System.out.println("total meeting length: " + (count + bol.get(meeting.key).g_val));

		System.out.println("fol: " + fol.size() + "    fhol: " + fhol.size());
		System.out.println("fcl: " + fcl.size());
		System.out.println("bol: " + bol.size() + "    perim: " + PEightPuzzle.perimeter.size());
		
		System.out.println("Time taken to improve: " + ((System.currentTimeMillis() - timer)/1000.0) + "s to get an improvement of length: " + (start.g_val - (count + bol.get(meeting.key).g_val)));
	}

	private void runImprove(){

		timer = System.currentTimeMillis();

		FEightPuzzle currentPos = start;
		FEightPuzzle head = start;
		int headCount = 0;

		while(head.getParent() != null && head.getParent() != goal && headCount < 5){
			head = head.getParent();
			headCount++;
		}
		fol = new PriorityQueue<PEightPuzzle>() ;
		fhol = new Hashtable<String, PEightPuzzle>();
		fcl = new Hashtable<String, PEightPuzzle>();
		meeting = null;

		bol = new Hashtable<String, PEightPuzzle>();
		perimeter = new ArrayList<PEightPuzzle>();

		

		if(head.g_val < 6)
			return;

		int lim = (int) head.g_val;
		System.out.println("LIMIT IS: " + lim);

		fol.add(new PEightPuzzle(goal));
		fhol.put(goal.key, new PEightPuzzle(goal));
		perimeter.add(new PEightPuzzle(head));

		//		fol.add(new PEightPuzzle(head));
		//		fhol.put(head.key, new PEightPuzzle(head));
		//		perimeter.add(new PEightPuzzle(goal));

		headCount = 1;

		while(head.getParent() != null && !head.getParent().checkDup(goal)){
			if(headCount%4 == 0){
				if(headCount > lim/2){
					fol.add(new PEightPuzzle(head));
					fhol.put(head.key, new PEightPuzzle(head));
				}else{
					perimeter.add(new PEightPuzzle(head));
				}
			}
			head = head.getParent();
			headCount++;
		}//end while



		FEightPuzzle p = start;
		int pc = 0;

		while(p != null){
			for(int i = 0; i < perimeter.size(); i++){
				if(p.checkDup(perimeter.get(i))){
					perimeter.get(i).g_val = pc;
				}
			}

			pc++;
			p=p.getParent();
		}

		PEightPuzzle.perimeter = perimeter;

		for(int i = 0; i < perimeter.size(); i++){
			bol.put(perimeter.get(i).key, perimeter.get(i));
		}

		System.out.println(fol.size() + " " + bol.size());
		perimeterSearch();


	}

	private void perimeterSearch() {
		// TODO Auto-generated method stub
		try{

			while(fol.peek() != null){
				PEightPuzzle current = fol.remove();
				fcl.put(current.key, current);

				//				System.out.println(current.g_val + " " + current.h_val + " " + current.f_val);

				//expand current
				PEightPuzzle[] neighbours = current.getNeighbors(); 
				expanded[a] += neighbours.length;

				for(int i = 0; i < neighbours.length; i++){

					PEightPuzzle n = neighbours[i];

					if(!fcl.containsKey(n.key)){

						boolean dup = false;

						if(fhol.containsKey(n.key))
							if(fhol.get(n.key).g_val > n.g_val){
								fhol.remove(n.key);
								fhol.put(n.key, n);
								fol.add(n);
								dup = true;
							}//end if node was expanded before



						if(!dup && bol.containsKey(n.key)){
//							System.out.println("SOLUTION FOUND\n\n" + n.toString() + "\n" + bol.get(n.key) + "\n====================");
							meeting = n;
							return;
							//maybe add
						}

						if(!dup){
							fol.add(n);
							fhol.put(n.key, n);
						}

					}else{
						if(fcl.get(n.key).g_val > n.g_val){
							fcl.remove(n.key);
							fhol.put(n.key, n);
							fol.add(n);


						}//end if node was expanded before
					}





				}



			}
		}catch(Exception e){
			e.printStackTrace();

		}

	}



	private void savePath() {
		// TODO Auto-generated method stub
		currentPath.clear();

		int count = 0;
		FEightPuzzle current = start;


		while(current.getParent() != null){
			currentPath.add(current);
			current = current.getParent();
			count++;
		}

		currentPath.add(current);
		System.out.println(current + "\n\nPath was of length: " + count);
	}

	private void printPath(){
		// TODO Auto-generated method stub
		int count = 0;
		FEightPuzzle current = start;
		//		System.out.println("START\n" + start + "\nGOAL\n" + goal +"\n==================");

		while(current.getParent() != null){
			//			System.out.println(current);
			current = current.getParent();
			count++;
		}

		System.out.println("Path was of length: " + count + "    ep: " + FEightPuzzle.ep + "  openlist: " + openList.size());
	}



	public void improvePath(){
		System.out.println(openList.peek().key()[0]+ ", " + openList.peek().key()[1] + " | " + start.key()[0] + ", " + start.key()[1] + "    ep: " + FEightPuzzle.ep);
		while((openList.peek().compareTo(start) < 0  || start.g_val != start.rhs)){

			if(System.currentTimeMillis() - timer > tlimit){		
				timeLeft = false;
				return;
			}


			FEightPuzzle s = openList.remove();
			HopenList.remove(s.key);

			if(s.checkDup(start))
				start = s;

			FEightPuzzle[] neighbours = s.getNeighbors();
			expanded[a] += neighbours.length;
			if(s.g_val > s.rhs){
				//				System.out.print("G: " + s.g_val + " RHS: " + s.rhs);
				s.g_val = s.rhs;
				//				System.out.println("   G: " + s.g_val + " RHS: " + s.rhs);

				closedList.put(s.key, s);



				for(int i = 0;i < neighbours.length; i++){
					updateState(neighbours[i]);
				}//end for

			}else{
				s.g_val = 1337;
				updateState(s);
				for(int i = 0;i < neighbours.length; i++){
					updateState(neighbours[i]);
				}//end for

			}

		}//end while

	}//end improvePath method


	public void updateState(FEightPuzzle s){

		if(!(closedList.containsKey(s.key) || incos.containsKey(s.key) || HopenList.containsKey(s.key))){
			s.g_val = 1337;
		}

		ArrayList<FEightPuzzle> al = new ArrayList<FEightPuzzle>();
		if(!s.checkDup(goal)){
			if(closedList.containsKey(s.key))
				al.add(closedList.get(s.key));
			if(HopenList.containsKey(s.key))
				al.add(HopenList.get(s.key));
			if(incos.containsKey(s.key))
				al.add(incos.get(s.key));

			int smallest = (int) (s.getParent().g_val + 1);


			if(al.size() == 0)
				s.rhs = (int) (s.getParent().g_val + 1);
			else{
				FEightPuzzle small = s.getParent();
				for(int i = 0; i < al.size(); i++){
					if((int)(al.get(i).getParent().g_val + 1) < smallest)
					{
						smallest = (int)(al.get(i).getParent().g_val + 1);
						small = al.get(i).getParent();
					}

				}
				s.parent = small;
				s.rhs = smallest;

			}
		}
		if(start.checkDup(s))
			start = s;

		if(HopenList.containsKey(s.key)){
			HopenList.remove(s.key);
			openList.remove(s);
		}

		if(s.g_val != s.rhs){
			if(!closedList.containsKey(s.key)){
				openList.add(s);
				HopenList.put(s.key, s);
			}else{
				incos.put(s.key, s);
			}
		}//end outter if


	}

}


