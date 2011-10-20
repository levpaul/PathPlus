package org.pathplus.utils.algorithmsindex;

/**
 * 
 * @author levi.lovelock
 *
 */

public class SearchAlgorithms {

	
	/*
	 * A Class which contains an index of search algorithms that are currently available.
	 * 
	 * Algorithms that require a weight value will start at index 1000.
	 * 
	*/
	
	
	// Regular A* search.
	public static final int A_STAR = 0;
	
	// BiMAX-BSF - the fastest performing bidirectional heuristic search algorithm
	// currently published. 
	// TODO Add reference to the paper - implement the algorithm.
	public static final int BIMAX_BSF = 1;
	
	
	// HPA* is a very commonly used hierarchical pathfinding algorithm in video games.
	// TODO Add reference to Adi Botea.
	public static final int HPA_STAR = 2;
	
	// Automatically Repairing A* - A good weighted A* algorithm
	// TODO Add reference to the paper - implement the algorithm.
	public static final int ARA_STAR = 1000;



}
